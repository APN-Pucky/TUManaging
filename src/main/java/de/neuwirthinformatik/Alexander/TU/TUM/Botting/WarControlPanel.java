package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import lombok.extern.java.Log;

@Log
public class WarControlPanel extends JPanel{
	

	
	private JComboBox<WarZone> warList;
	private GUI.IntegerField war_times;
	
	public WarControlPanel()
	{
		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		
		
		JPanel panel = new JPanel();
		panel.add(GUI.label("Dump Brawl X times: "));
		panel.add(war_times = new GUI.IntegerField(20));
		p.add(panel);
		
		p.add(GUI.label("War"));
		panel = new JPanel();
		panel.add(GUI.label("War target: "));
		warList = new JComboBox<WarZone>(WarZone.values());
		panel.add(warList);
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Dump War",()->dump_war_pressed()));

			panel.add(GUI.buttonAsync("Claim War Rewards", () -> war_claim_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Info War", () -> info_war_pressed()));
			panel.add(GUI.buttonAsync("Ranking War", () -> war_ranking_pressed()));
			panel.add(GUI.buttonAsync("Ranking Members Guild War", () -> war_ranking_members_pressed()));
			panel.add(GUI.buttonAsync("Ranking Enemies Guild War", () -> war_ranking_enemies_pressed()));
		p.add(panel);
		add(p);
	}
	
	private enum WarZone
	{
		Core(1),Top_Off(21),Bot_Off(22), Top_Def(11),Bot_Def(12);
		public final int id;
		private WarZone(int id) {this.id = id;}
	}
	
	public void dump_war_pressed()
	{
		int war_nums = war_times.getNumber();
		int id = ((WarZone)warList.getSelectedItem()).id;

		BotControlPanel.dumpBotsParallel("War", war_nums, (b) -> {
			int[] data = b.getWarData();
			if(data[0] >0)
			{
				b.fightWarBattle(id);
				int[] new_data = b.getWarData();
				if(new_data[1] > data[1])return "WIN" +  "| cur: " + new_data[6] + "p sum: " + new_data[5] + "p, w/l: off:" + new_data[1] + "/" +new_data[2] + " def:" + new_data[3] + "/" +new_data[4];
				if(new_data[2] > data[2])return "LOSS" +  "| cur:" + new_data[6] + "p sum: " + new_data[5] + "p, w/l: off:" + new_data[1] + "/" +new_data[2] + " def:" + new_data[3] + "/" +new_data[4];
				if(new_data[0]==data[0])return "No fight started " +  "| @ #" + new_data[1] + " with " + new_data[2] + "p, w/l:" + new_data[3] + "/" +new_data[4];
				return "Possible fight interruption" +  "| cur:" + new_data[6] + " sum: " + new_data[5] + ", w/l: off:" + new_data[1] + "/" +new_data[2] + " def:" + new_data[3] + "/" +new_data[4];
			}
			return "No energy left ";});
		
		
	}
	
	public void info_war_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Guild",
                "Energy",
                "Wins",
                "Lossses",
                "Def Wins",
                "Def Losses",
                "Faction War Points",
                "Current War Points",
                "Claimed Rewards"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][7];
		
		
		TUM.pp.forEach("War data loading", bs, (b,j) -> {
			int[] war = b.getWarData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(war[0]), new Integer(war[1]), new Integer(war[2]), new Integer(war[3]), new Integer(war[4]), new Integer(war[5]), new Integer(war[6]), new Integer(war[7])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "War Infos");
	}
	
	public void war_ranking_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points",
                "mmr",
                "wins",
                "losses"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("War rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getWarLeaderBoard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "War Rankings",1);
	}
	
	public void war_ranking_members_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Guild",
				"Name",
                "Energy",
	            "Wins",
	            "Lossses",
	            "Def Wins",
	            "Def Losses",
	            "Faction War Points",
	            "Current War Points",};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("War rankings members loading", bs, (b,j) -> {
			for(Object[] o : b.getWarOwnMembersLeaderBoard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "War rankings members",2);
	}
	
	public void war_ranking_enemies_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"VS",
				"Name",
                "Energy",
	            "Wins",
	            "Lossses",
	            "Def Wins",
	            "Def Losses",
	            "Faction War Points",
	            "Current War Points",};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		
		
		TUM.pp.forEach("War rankings enemies loading", bs, (b,j) -> {
			for(Object[] o : b.getWarEnemyMembersLeaderBoard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "War rankings enemies",2);
	}
	
	public void war_claim_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		TUM.pp.forEach("Claiming War Rewards", bs, (b) -> {
			b.claimWarRewards(); //TODO add checks for already claimed to all claims
			return b.getName();
		});
	}
}
