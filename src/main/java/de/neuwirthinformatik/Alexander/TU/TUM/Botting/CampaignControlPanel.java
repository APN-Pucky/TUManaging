package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class CampaignControlPanel extends JPanel
{
	
	private GUI.IntegerField campaign_id,campaign_level,campaign_times,campaign_max_times;
	
	public CampaignControlPanel()
	{

		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(GUI.label("Campaign - No LSE"));
		
		JPanel panel = new JPanel();
			//panel.add(GUI.label("Campaign id: "));
			//panel.add(campaign_id = new GUI.IntegerField(26));
		//p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.label("Campaign level: "));
			panel.add(campaign_level = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.label("Campaign times: "));
			panel.add(campaign_times = new GUI.IntegerField(6));
		p.add(panel);

		panel = new JPanel();
			panel.add(GUI.label("Fight campaign to X wins: "));
			panel.add(campaign_max_times = new GUI.IntegerField(6));
		p.add(panel);
		
		panel = new JPanel();
			panel.add( GUI.buttonAsync("Dump Campaign", () -> dump_campaign()));
		p.add(panel);
		

		panel = new JPanel();
			panel.add( GUI.buttonAsync("Info Campaign", () -> info_campaign()));
		p.add(panel);
		
		add(p);
	}
	
	public void dump_campaign()
	{
		//Bot[] bots = TUM._this.botovp.getSelectedBots();
		int m_times = campaign_times.getNumber();
		//int id_camp = campaign_id.getNumber();
		int camp_level = campaign_level.getNumber();
		int max = campaign_max_times.getNumber();

		//BotControlPanel.waitpredump("campaign");
		BotControlPanel.dumpBotsParallel("Campaign", m_times, (b) -> {
			int[] data = b.getCampaignData();
			int energy = data[1];
			int new_energy = -1;
			if(data[2] == -1)return "campaign not unlocked";
			if (data[1] >= data[2]) { //125 == camp cost
				if(max == -1 || data[camp_level*2+1]<max)
				{
					b.dumpCampaign(data[0], camp_level);
					b.updateData();
					int[] new_data = b.getCampaignData();
					new_energy = new_data[1];
					if(data[3]+data[5]+ data[7] < new_data[3]+new_data[5]+ new_data[7]) return "SUCCESS";
					if(energy == new_energy)return "No fight started, Campaign not available? ";
					return "lost or possible fight interruption or unfinished campaign";
				}
				else {
					return "Max. wins reached";
				}
			}
			return "NOT ENOUGH ENERGY: " + energy;
			
		});//, "Repeat Campaign dump " + m_times + " times", m_times, (j) -> j + 1 + " Campaigns dumped");
	}
	
	public void info_campaign()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
				"Guild",
				"ID",
				"Energy",
                "Energy Costs",
                "L1 Done",
                "L1 All",
                "L2 Done",
                "L2 All",
                "L3 Done",
                "L3 All",};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][11];
		
		
		TUM.pp.forEach("Campaign data loading", bs, (b,j) -> {
			int[] camp = b.getCampaignData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(camp[0]), new Integer(camp[1]), new Integer(camp[2]), new Integer(camp[3]), new Integer(camp[4]), new Integer(camp[5]), new Integer(camp[6]), new Integer(camp[7]), new Integer(camp[8])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "Campaign Infos");
	}
}
