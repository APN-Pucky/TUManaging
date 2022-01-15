package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class GuildQuestControlPanel extends JPanel{
	
	private GUI.IntegerField quest_id,quest_times;
	
	public GuildQuestControlPanel()
	{
		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.add(GUI.label("Guild Quest - No LSE"));
		
		
		JPanel panel = new JPanel();
		
		panel = new JPanel();
			panel.add(GUI.label("Guild Quest ID: "));
			panel.add(quest_id = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.label("Guild Quest dump X times: "));
			panel.add(quest_times = new GUI.IntegerField(1));
		p.add(panel);

		panel = new JPanel();
			panel.add(GUI.buttonAsync("Dump Guild Quest", () -> dump_guild_quest()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Info Guild Quest", () -> info_guild_quest()));
		p.add(panel);
		add(p);
	}
	
	private void dump_guild_quest()
	{
		//Mission mission = (Mission) missionList.getSelectedItem();
		// int m_incs = mission_increases.getNumber()+1;
		int m_times = quest_times.getNumber();
		int id = quest_id.getNumber();

		BotControlPanel.dumpBotsParallel("Mission", m_times, (b) -> {
			int energy = b.getEnergy();
			int new_energy = -1;
			if (energy >= 50) {
				b.fightGuildQuest(id);
				b.updateData();
				new_energy = b.getEnergy();
			}
			return (new_energy == -1 ? "NOT ENOUGH ENERGY: " + energy
					: new_energy < energy ? "FINE" : "MISSION NOT AVAILABLE");
		});
	}
	
	private void info_guild_quest()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Guild",
                "ID Quest 1", "Progress Q1", "Max Q1",
                "ID Quest 2", "Progress Q2", "Max Q2",
                "ID Quest 3", "Progress Q3", "Max Q3"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][7];
		
		
		TUM.pp.forEach("Guild quest data loading", bs, (b,j) -> {
			int[] quest = b.getGuildQuestData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(quest[0]), new Integer(quest[1]), new Integer(quest[2]), new Integer(quest[3]), new Integer(quest[4]), new Integer(quest[5]), new Integer(quest[6]), new Integer(quest[7]), new Integer(quest[8])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "Guild Quest Infos");
	}
	
}
