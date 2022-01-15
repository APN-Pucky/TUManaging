package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.Basic.Mission;
import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class MissionControlPanel extends JPanel {
	GUI.IntegerField mission_level;
	GUI.IntegerField mission_times;
	GUI.IntegerField mission_max_times;
	// GUI.NumberField mission_increases;
	JComboBox<Mission> missionList;

	public MissionControlPanel() {

		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));

		p.add(GUI.label("Mission"));

		JPanel panel = new JPanel();
		panel.add(GUI.label("Mission id: "));
		missionList = new JComboBox<Mission>(GlobalData.missions);
		panel.add(missionList);
		p.add(panel);

		panel = new JPanel();
		panel.add(GUI.label("Fight mission X times: "));
		panel.add(mission_times = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
		panel.add(GUI.label("Fight mission to X wins: "));
		panel.add(mission_max_times = new GUI.IntegerField(-1));
		p.add(panel);

		/*
		 * panel = new JPanel();
		 * panel.add(GUI.label("Increase the misson ID X times: "));
		 * panel.add(mission_increases = new GUI.NumberField(0)); p.add(panel);
		 */

		panel = new JPanel();
		panel.add(GUI.label("Mission level (Only for LSE): "));
		panel.add(mission_level = new GUI.IntegerField(1));
		p.add(panel);

		panel = new JPanel();
		panel.add(GUI.buttonAsync("Dump mission", () -> dump_mission_pressed()));
		panel.add(GUI.buttonAsync("Info mission", () -> info_mission_pressed()));
		panel.add(GUI.buttonAsync("Info all missions", () -> info_all_mission_pressed()));

		p.add(panel);
		add(p);
	}

	public void dump_mission_pressed() {
		Mission mission = (Mission) missionList.getSelectedItem();
		// int m_incs = mission_increases.getNumber()+1;
		int m_times = mission_times.getNumber();
		int level = mission_level.getNumber();
		int max = mission_max_times.getNumber();

		BotControlPanel.dumpBotsParallel("Mission", m_times, (b) -> {
			int energy = b.getEnergy();
			int new_energy = -1;
			if (energy >= mission.getCosts() ) {
				int[] t = null;
				if(max == -1 ||  (t=b.getMissionData(mission)) == null  || t[1]<max)
				{
					b.fightMission(mission, level);
					b.updateData();
					new_energy = b.getEnergy();
				}
				else
				{
					return "MAX WINS REACHED";
				}
			}
			return (new_energy == -1 ? "NOT ENOUGH ENERGY: " + energy
					: new_energy < energy ? "FINE" : "MISSION NOT AVAILABLE");
		});

	}

	public void info_mission_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		Mission m = (Mission) missionList.getSelectedItem();

		String[] columnNames = { "Name", "Guild", "Energy", "Mission ID", "Wins", "Level", "2nd Star", "3rd Star" };

		Object[][] data = new Object[bs.length - GlobalBotData.getNullCount(bs)][8];

		int j = 0;
		for (int i = 0; i < bs.length; i++) {
			if (bs[i] != null) {
				int[] battle = bs[i].getMissionData(m);
				bs[i].updateData();
				data[j] = new Object[] { bs[i].getName(), bs[i].getGuild(), bs[i].getEnergy(), new Integer(battle[0]),
						new Integer(battle[1]), new Integer(battle[2]), new Integer(battle[3]),
						new Integer(battle[4]) };
				j++;
			}
		}

		GUI.createDataTableWindow(data, columnNames, "Mission " + m + " Infos");
	}

	public void info_all_mission_pressed() {
		String[] columnNames = { "Mission ID", "Wins", "Level", "2nd Star", "3rd Star" };

		Bot[] bs = TUM._this.botovp.getSelectedBots();

		TUM.pp.forEach("Loading all mission infos.", bs, (b, j) -> {
			GUI.createDataTableWindow(GUI.toIntegerArray(b.getMissionData()), columnNames, "Mission " + b + " Infos",
					0);
			return b.toString();
		});

		/*
		 * GUI.LoadBar lb = new
		 * GUI.LoadBar("Loading all mission infos.",bs.length);
		 * 
		 * for(int i = 0; i < bs.length;i++) { if(bs[i] !=null) {
		 * GUI.createDataTableWindow(GUI.toIntegerArray(bs[i].getMissionData()),
		 * columnNames, "Mission " + bs[i] +" Infos", 0); }
		 * 
		 * if(!lb.setProgress(i, bs[i].toString()))break; }
		 */
	}
}
