package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class RaidControlPanel extends JPanel
{
	GUI.IntegerField raid_times;
	GUI.IntegerField raid_score_num;
	GUI.IntegerField raid_level_num;
	public RaidControlPanel()
	{
		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(GUI.label("Raid"));
		
		JPanel panel = new JPanel();
		panel.add(GUI.label("Dump Raid X times: "));
		panel.add(raid_times = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
		panel.add(GUI.label("Max. score: "));
		panel.add(raid_score_num = new GUI.IntegerField(12500));
		p.add(panel);
		
		panel = new JPanel();
		panel.add(GUI.label("Max. level: "));
		panel.add(raid_level_num = new GUI.IntegerField(26));
		p.add(panel);
		
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Dump Raid",() -> raid_dump_pressed()));
			panel.add(GUI.buttonAsync("Claim Raid Rewards", () -> raid_claim_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Info Raid", () -> raid_info_pressed()));
			panel.add(GUI.buttonAsync("Ranking Raid", () -> raid_ranking_pressed()));
			panel.add(GUI.buttonAsync("Ranking Members Raid", () -> raid_ranking_members_pressed()));
		p.add(panel);
	
		add(p);
	}
	
	public void raid_dump_pressed()
	{
		int raid_nums = raid_times.getNumber();
		int raid_level_limit = raid_level_num.getNumber();
		int raid_score_limit = raid_score_num.getNumber();

		BotControlPanel.dumpBotsParallel("Raid", raid_nums, (b) -> {
			int[] data = b.getRaidData();
		
			if(data[0]>0)
			{
				if(data[1]>=raid_score_limit)return "Score limit reached";
				if(data[3]>=raid_level_limit)return "Level limit reached";
				b.fightRaidBattle();
				int[] new_data = b.getRaidData();
				if(new_data[1]-data[1]==100)return "WIN";
				if((new_data[1]-data[1])>0)return "LOSS";
				if(new_data[0]==data[0])return "No fight started";
				return "Possible fight interruption";
			}
			return "No energy left";});
	}
	
	public void raid_info_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Guild",
                "Energy",
                "Damage",
                "ID",
                "Level",
                "Health",
                "Max Helath",
                "Remaining Time",
                "Claimed Rewards"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][10];
		
		
		TUM.pp.forEach("Raid data loading", bs, (b,j) -> {
			int[] raid = b.getRaidData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(raid[0]), new Integer(raid[1]), new Integer(raid[2]), new Integer(raid[3]), new Integer(raid[4]), new Integer(raid[5]), new Integer(raid[6]), new Integer(raid[7])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "Raid Infos");
	}
	
	public void raid_claim_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		TUM.pp.forEach("Claiming Raid Rewards", bs, (b) -> {
			b.claimRaidRewards();
			return b.getName();
		});
	}
	
	public void raid_ranking_members_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Guild",
				"Name",
                "Damage"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("Raid rankings members loading", bs, (b,j) -> {
			for(Object[] o : b.getRaidMembersLeaderBoard())
			{
				boolean contains = false;
				for(Object[] d : data)
				{
					if(d[1].equals(o[0])) contains=true;
				}
				
				Object[] n_o = new Object[o.length+1];
				n_o[0] = b.getGuild();
				for(int i = 1; i < n_o.length;i++)
				{
					n_o[i] = o[i-1];
				}
				
				if(!contains)data.add(n_o);
			}
			return b.getName();
		});
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Raid rankings members",2);
	}
	
	public void raid_ranking_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "Stat",
                "Raid Level",
                "Raid Level Rank"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("Raid rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getRaidLeaderBoard())
			{
				boolean contains = false;
				for(Object[] d : data)
				{
					if(d[0].equals(o[0])) contains=true;
				}
				if(!contains)data.add(o);
			}
			return b.getName();
		});
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Raid Rankings",1);
	}
}
