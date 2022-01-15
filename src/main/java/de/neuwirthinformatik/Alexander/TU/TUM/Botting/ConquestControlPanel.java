package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class ConquestControlPanel extends JPanel{

	GUI.IntegerField cq_times;
	GUI.IntegerField cq_points;
	JComboBox<ConquestZone> zoneList;
	public ConquestControlPanel()
	{
		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.add(GUI.label("Conquest"));
		
		zoneList = new JComboBox<ConquestZone>(ConquestZone.values());
		
		
		JPanel panel = new JPanel();
		panel.add(GUI.label("Dump Conquest X times: "));
		panel.add(cq_times = new GUI.IntegerField(1));
		p.add(panel);
		
		panel = new JPanel();
		panel.add(GUI.label("Dump Conquest to X Points: "));
		panel.add(cq_points = new GUI.IntegerField(-1));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(zoneList);
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Dump Conquest",() -> cq_dump_pressed()));
			panel.add(GUI.buttonAsync("Claim Conquest Rewards", () -> cq_claim_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Info Conquest", () -> cq_info_pressed()));
			panel.add(GUI.buttonAsync("Overview Conquest", () -> cq_rank_all()));
		p.add(panel);
			
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Ranking Conquest", () -> cq_rank_pressed()));
			panel.add(GUI.buttonAsync("Top Conquest", () -> cq_top_pressed()));
			panel.add(GUI.buttonAsync("Members Conquest", () -> cq_members_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Rank Zone", () -> cq_rank_zone()));
			panel.add(GUI.buttonAsync("Top Zone", () -> cq_top_zone()));
			panel.add(GUI.buttonAsync("Members Zone", () -> cq_members_zone()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.buttonAsync("Live Zone", () -> cq_live_zone()));
			panel.add(GUI.buttonAsync("Live Overview", () -> cq_live_all()));
		p.add(panel);

		add(p);
	}
	
	public static enum ConquestZone
	{
		NULL(0),The_Spire(1),Norhaven(2),Jotuns_Pantheon(3),Tyrolian_Outpost(4),Infested_Depot(5),Brood_Nest(6), SkyCom_Complex(7),Red_Maw_Base(8), Mech_Graveyard(9),Seismic_Beacon(10),Phobos_Station(11),  Ashpodel_Nexus(12),Magma_Foundry(13),Cleave_Rock(14),Malorts_Den(15),Borean_Forges(16),Enclave_Landing(17), Andar_Quarantine(18), Elder_Port(19) , Barons_Claw_Labs(20), Ashrock_Redoubt(21), Colonial_Relay(22), ;
		public final int id;
		private ConquestZone(int id) {this.id = id;}
	}
	
	public void cq_dump_pressed()
	{
		int cq_nums = cq_times.getNumber();
		int cq_point = cq_points.getNumber();
		int zone_id = ((ConquestZone) zoneList.getSelectedItem()).id;
		

		BotControlPanel.dumpBotsParallel("Conquest", cq_nums, (b) -> {
			int[] data = b.getConquestData();
		
			if(data[1]>0)
			{
				if(cq_point != -1)
				{
					int cur_points = 0;
					for(Object[] o : b.getConquestLiveZone(zone_id))
					{
						if(b.getGuild().equals(o[0]))cur_points=(Integer)o[2];
					}
					if(cur_points>=cq_point) return "Max Points reached";
				}
				b.fightConquestBattle(zone_id);
				int[] new_data = b.getConquestData();
				if(new_data[4]-data[4]==50)return "WIN";
				if((new_data[4]-data[4])>0)return "LOSS";
				if(new_data[1]==data[1])return "No fight started ";
				return "ossible fight interruption";
			}
			return "No energy left";});
	}
	
	public void cq_info_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Guild",
                "CQ ID",
                "Energy",
                "Points",
                "Rank",
                "Influence",
                "Claimed Rewards"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][8];
		
		
		TUM.pp.forEach("Conquest data loading", bs, (b,j) -> {
			int[] cq = b.getConquestData();
			data[j] = new Object[]{b.getName(), b.getGuild(), new Integer(cq[0]), new Integer(cq[1]), new Integer(cq[2]), new Integer(cq[3]), new Integer(cq[4]), new Integer(cq[5])};
			return b.getName();
		});
		
		GUI.createDataTableWindow(data,columnNames, "Conquest Infos");
	}
	
	public void cq_claim_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		TUM.pp.forEach("Claiming Conquest Rewards", bs, (b) -> {
			b.claimConquestRewards();
			return b.getName();
		});
	}
	
	
	
	
	public void cq_rank_all()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = Stream.of(ConquestZone.values()).map(Object::toString).toArray(String[]::new);

		Object[][] data = new Object[100][ConquestZone.values().length];
		for(int i =0;i < 100;i++)data[i][0] = i+1;
		
		TUM.pp.forEach("All CQ rankings loading", ConquestZone.values(), (c,k) -> {
			int pos = pos(ConquestZone.values(),c);
			int zone_id = c.id;
			TUM.pp.forEach(c+" rankings loading", bs, (b,j) -> {
				if(c==ConquestZone.NULL)return b.getName();
				for(Object[] o : b.getConquestLeaderboardZone(zone_id))
				{
					if((int) o[1]<100 && (int) o[1]>0)
					{
						while(data[(int) o[1]-1][pos]!=null && !((String)data[(int) o[1]-1][pos]).equals(o[0] + "~" + o[2]))o[1] = (int) o[1]+1;
						if((int) o[1]<100)data[(int) o[1]-1][pos]= o[0] + "~" + o[2];
					}
				}
				for(Object[] o : b.getConquestTopLeaderboardZone(zone_id))
				{
					
					if((int) o[1]<100&&(int) o[1]>0)
					{
						while(data[(int) o[1]-1][pos]!=null && !((String)data[(int) o[1]-1][pos]).equals(o[0] + "~" + o[2]))o[1] = (int) o[1]+1;
						if((int) o[1]<100)data[(int) o[1]-1][pos]= o[0] + "~" + o[2];
					}
				}
				return b.getName();
			});
			return c.name();
		});
		
		//sort
		boolean[] b_has_value = new boolean[columnNames.length];
		for(int j = 0; j  < data.length;j++)
		{
			for(int i = 0; i < columnNames.length;i++)
			{
				if(data[j][i]!=null)b_has_value[i]=true;
			}
		}
		int c=0;
		for(boolean b : b_has_value)if(b)c++;
		//data
		for(int j = 0; j< data.length;j++)
		{
			Object[] neww = new Object[c];
			int k = 0;
			for(int i =0;i < columnNames.length;i++)
			{
				if(b_has_value[i])
				{
					neww[k] = data[j][i];
					k++;
				}
			}
			data[j] = neww;
		}
		//Strings
		String[] neww = new String[c];
		int k = 0;
		for(int i =0;i < columnNames.length;i++)
		{
			if(b_has_value[i])
			{
				neww[k] = columnNames[i];
				k++;
			}
		}
		columnNames = neww;
		GUI.createDataTableWindow(data,columnNames,"All Conquest Rankings",0);
	}
	
	private int pos(ConquestZone[] cq, ConquestZone c)
	{
		for(int i = 0; i < cq.length;i++)
		{
			if(cq[i]==c)return i;
		}
		return 0;
	}
	
	public void cq_rank_zone()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ConquestZone cqz = (ConquestZone) zoneList.getSelectedItem();
		int zone_id = cqz.id;
		
		TUM.pp.forEach(cqz+" rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestLeaderboardZone(zone_id))
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, cqz + " Rankings",1);
	}
	
	public void cq_rank_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		TUM.pp.forEach("Conquest rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestLeaderboard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Conquest Rankings",1);
	}
	
	public void cq_top_zone()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ConquestZone cqz = (ConquestZone) zoneList.getSelectedItem();
		int zone_id = cqz.id;
		
		TUM.pp.forEach(cqz+" top-rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestTopLeaderboardZone(zone_id))
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, cqz + " Top-Rankings",1);
	}
	
	public void cq_top_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		TUM.pp.forEach("Conquest top-rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestTopLeaderboard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, "Conquest Top-Rankings",1);
	}
	
	public void cq_members_zone()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ConquestZone cqz = (ConquestZone) zoneList.getSelectedItem();
		int zone_id = cqz.id;
		
		TUM.pp.forEach(cqz+" member-rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestMembersLeaderBoardZone(zone_id))
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, cqz + " Member-Rankings",1);
	}
	public void cq_members_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		TUM.pp.forEach("Conquest member-rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestMembersLeaderboard())
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames,  "Conquest Member-Rankings",1);
	}
	
	
	public void cq_live_zone()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {"Name",
                "Rank",
                "points"};

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ConquestZone cqz = (ConquestZone) zoneList.getSelectedItem();
		int zone_id = cqz.id;
		
		TUM.pp.forEach(cqz+" live-rankings loading", bs, (b,j) -> {
			for(Object[] o : b.getConquestLiveZone(zone_id))
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
		
		GUI.createDataTableWindow(data.toArray(new Object[0][0]),columnNames, cqz + " Live-Rankings",1);
	}
	
	public void cq_live_all()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = Stream.of(ConquestZone.values()).map(Object::toString).toArray(String[]::new);

		Object[][] data = new Object[100][ConquestZone.values().length];//Todo missing +1?
		for(int i =0;i < 100;i++)data[i][0] = i+1;
		
		TUM.pp.forEach("All-Live CQ rankings loading", ConquestZone.values(), (c,k) -> {
			int pos = pos(ConquestZone.values(),c);
			int zone_id = c.id;
			TUM.pp.forEach(c+" rankings loading", bs, (b,j) -> {
				if(c==ConquestZone.NULL)return b.getName();
				for(Object[] o : b.getConquestLiveZone(zone_id))
				{
					if((int) o[1]<100 && (int) o[1]>0)
					{
						while(data[(int) o[1]-1][pos]!=null && !((String)data[(int) o[1]-1][pos]).equals(o[2] + "~" + o[0]))o[1] = (int) o[1]+1;
						if((int) o[1]<100)data[(int) o[1]-1][pos]= o[2] + "~" + o[0];
					}
				}
				return b.getName();
			});
			return c.name();
		});
		//sort
		boolean[] b_has_value = new boolean[columnNames.length];
		for(int j = 0; j  < data.length;j++)
		{
			for(int i = 0; i < columnNames.length;i++)
			{
				if(data[j][i]!=null)b_has_value[i]=true;
			}
		}
		int c=0;
		for(boolean b : b_has_value)if(b)c++;
		//data
		for(int j = 0; j< data.length;j++)
		{
			Object[] neww = new Object[c];
			int k = 0;
			for(int i =0;i < columnNames.length;i++)
			{
				if(b_has_value[i])
				{
					neww[k] = data[j][i];
					k++;
				}
			}
			data[j] = neww;
		}
		//Strings
		String[] neww = new String[c];
		int k = 0;
		for(int i =0;i < columnNames.length;i++)
		{
			if(b_has_value[i])
			{
				neww[k] = columnNames[i];
				k++;
			}
		}
		columnNames = neww;
		GUI.createDataTableWindow(data,columnNames,"All-Live Conquest Rankings",0);
	}
	
}
