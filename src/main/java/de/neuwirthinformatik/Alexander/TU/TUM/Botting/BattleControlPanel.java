package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class BattleControlPanel extends JPanel{
	

	GUI.IntegerField battle_times;
	
	public BattleControlPanel()
	{

		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(GUI.label("Battle"));

		JPanel panel = new JPanel();
		panel.add(GUI.label("Dump Battle X times: "));
		panel.add(battle_times = new GUI.IntegerField(30));
		p.add(panel);
		
		panel = new JPanel();
			panel.add( GUI.buttonAsync("Dump Battle", () -> battle_dump_pressed()));
			panel.add(GUI.buttonAsync("Info Battle", () -> battle_info_pressed()));
		p.add(panel);
		add(p);
	}
	
	public void battle_dump_pressed()
	{
		int battle_nums = battle_times.getNumber();
		
		BotControlPanel.dumpBotsParallel("Battle",battle_nums, (b) -> {
			int[] data = b.getBattleData();
		
			if(data[0] >0)
			{
				b.fightPVPBattle();
				int[] new_data = b.getBattleData();
				if(data[0] == new_data[0])return "no fight started";
				return ((new_data[3]>data[3])?"WIN":"LOSS");
			}
			return "no stamina left";});
		
		/*
		BotControlPanel.waitpredump("battle");
		//loop
		TUM.pp.forEachDouble("Battle dumping Bots", bots, (b,j) -> {
			int[] data = b.getBattleData();
		
			if(data[0] >0)
			{
				b.fightPVPBattle();
				int[] new_data = b.getBattleData();
				if(data[0] == new_data[0])return b.getName() + " |" + " no fight started " +  "|";
				return b.getName() + " |" + ((new_data[3]>data[3])?"WIN":"LOSS") +  "|";
			}
			return b.getName() + " |" + " no stamina left " +  "|";}
		, "Repeat battle dump " + battle_nums + " times", battle_nums, (j) -> j+1 + " stamina dumped");
		*/
		/*
		GUI.LoadBar lbh = new LoadBar("Repeat battle dump " + brawl_nums + " times", brawl_nums);
		GUI.LoadBar lbl = new LoadBar("Battle dumping Bots", bots.length);
		for(int j = 0; j < brawl_nums ;j++)
		{
			
			for (int i =0; i < bots.length;i++)
			{
				int[] data = bots[i].getBattleData();
				if(data[0] >0)bots[i].fightPVPBattle();
				int[] new_data = bots[i].getBattleData();
				if(!lbl.setProgress(i, bots[i].getName() + " |" + ((new_data[3]>data[3])?"WIN":"LOSS") +  "|"))break;
			}
			;
			if(!lbh.setProgress(j, j+1 + " stamina dumped") || lbl.reset())break;
		}
		lbl.close();
		lbh.close();*/
	}
	
	public void battle_info_pressed()
	{
		Bot[] bs  = TUM._this.botovp.getSelectedBots();
		
		
		String[] columnNames = {
				"Name",
                "Guild",
                "Stamina",
                "Max Stamina",
                "Battle Rating",
                "PVP Wins",
                "Level",
                "League Points"};
		
		Object[][] data = new Object[bs.length-GlobalBotData.getNullCount(bs)][8];
		int j=0;
		for(int i = 0; i < bs.length;i++)
		{
			if(bs[i] !=null)
			{
				int[] battle = bs[i].getBattleData();
				data[j] = new Object[]{bs[i].getName(), bs[i].getGuild(), new Integer(battle[0]), new Integer(battle[1]), new Integer(battle[2]), new Integer(battle[3]), new Integer(battle[4]), new Integer(battle[5])};
				j++;
			}
		}
		
		GUI.createDataTableWindow(data,columnNames, "Battle Infos");
	}
}
