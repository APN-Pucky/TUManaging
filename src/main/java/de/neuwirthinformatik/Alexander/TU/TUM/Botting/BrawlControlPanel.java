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

public class BrawlControlPanel extends JPanel
{

	private GUI.IntegerField brawl_times;
	
	public BrawlControlPanel()
	{
		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		//p.setPreferredSize(new Dimension(500,600));

		p.add(GUI.label("Brawl"));
		
		JPanel panel = new JPanel();
			panel.add(GUI.label("Dump Brawl X times: "));
			panel.add(brawl_times = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Dump Brawl", () -> brawl_dump_pressed()));
			panel.add(GUI.buttonAsync("Claim Brawl Rewards", () -> brawl_claim_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Info Brawl", () -> brawl_info_pressed()));
			panel.add(GUI.buttonAsync("Ranking Brawl", () -> brawl_ranking_pressed()));
			panel.add(GUI.buttonAsync("Ranking Members Guild Brawl", () -> brawl_ranking_members_pressed()));
		p.add(panel);
		
		add(p);
	}
	
	public void brawl_dump_pressed()
	{
		int brawl_nums = brawl_times.getNumber();

		BotControlPanel.dumpBotsParallel("Brawl", brawl_nums, (b) -> {
			int[] data = b.getBrawlData();
			if(data[0] >0)
			{
				b.fightBrawlBattle();
				int[] new_data = b.getBrawlData();
				if(new_data[3] > data[3])return "WIN" +  "| @ #" + new_data[1] + " with " + new_data[2] + "p, w/l:" + new_data[3] + "/" +new_data[4];
				if(new_data[4] > data[4])return "LOSS" +  "| @ #" + new_data[1] + " with " + new_data[2] + "p, w/l:" + new_data[3] + "/" +new_data[4];
				if(new_data[0]==data[0])return "No fight started " +  "| @ #" + new_data[1] + " with " + new_data[2] + "p, w/l:" + new_data[3] + "/" +new_data[4];
				return "Possible fight interruption" +  "| @ #" + new_data[1] + " with " + new_data[2] + "p, w/l:" + new_data[3] + "/" +new_data[4];
			}
			return "No energy left";});
		
		
	}
	
	public void brawl_info_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Guild",
                "Energy",
                "Rank",
                "Points",
                "Wins",
                "Losses",
                "Claimed Rewards"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][7];
		
		
		TUM.pp.forEach("Brawl data loading", bs, (b,j) -> {
			int[] brawl = b.getBrawlData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(brawl[0]), new Integer(brawl[1]), new Integer(brawl[2]), new Integer(brawl[3]), new Integer(brawl[4]), new Integer(brawl[5])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "Brawl Infos");
	}
	
	public void brawl_claim_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		TUM.pp.forEach("Claiming Brawl Rewards", bs, (b) -> {
			b.claimBrawlRewards();
			return b.getName();
		});
	}
	
	public void brawl_ranking_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "Score"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("Brawl rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getBrawlLeaderBoard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Brawl Rankings",1);
	}
	
	public void brawl_ranking_members_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Guild",
				"Name",
                "Rank",
                "Score"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("Brawl rankings members loading", bs, (b,j) -> {
			for(Object[] o : b.getBrawlMembersLeaderBoard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Brawl rankings members",2);
	}
	
}
