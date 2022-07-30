package de.neuwirthinformatik.Alexander.TU.TUM.Botting;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.JediDeckGrab;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.LSE;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Result;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class PracticeControlPanel  extends JPanel{
	
	final int sec_save_sleep = 30;//TODO imprv. maybe 20

	GUI.IntegerField practice_times;
	GUI.IntegerField tuo_iters;
	JCheckBox surge;
	JCheckBox vs_off;
	
	public PracticeControlPanel()
	{

		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(GUI.label("Practice"));

		JPanel panel = new JPanel();
		panel.add(GUI.label("Dump Practice X times: "));
		panel.add(practice_times = new GUI.IntegerField(100));
		p.add(panel);
		
		panel = new JPanel();
		panel.add(GUI.label("TUO iters: "));
		panel.add(tuo_iters = new GUI.IntegerField(1000));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(surge = GUI.check("Surge", false));
			panel.add(vs_off = GUI.check("VS OFF", false));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.label("Every Bot random vs himself: "));
			panel.add(GUI.buttonAsync("Self Practice", () -> self_practice_dump_pressed()));
		p.add(panel);
		
		panel = new JPanel();
			panel.add(GUI.label("First Bot LiveSim vs every guild member: "));
			panel.add(GUI.buttonAsync("Guild Practice (1)", () -> guild_practice_dump_pressed()));
		p.add(panel);
		
		add(p);
	}
	
	//TODO sleep when <5 bots selected
	public void self_practice_dump_pressed()
	{
		int practice_nums = practice_times.getNumber();
		int tuo_nums = tuo_iters.getNumber();
		boolean is_surge = surge.isSelected();
		boolean versus_off = vs_off.isSelected();

		Bot[] bots = TUM._this.botovp.getSelectedBots();
		
		String[] columnNames = {
				"Name",
                "Guild",
                "Off_Deck",
                "Def_Deck",
                "FIGHTS",
                "WINS",
                "WINRATE [%]",
                "TUOWINRATE [%]",
                "DIFFRATE [%]"};

		Object[][] data = new Object[bots.length-GlobalBotData.getNullCount(bots)][9];
		
		BotControlPanel.dumpBotsSingle(bots,"Random Practice",practice_nums, (b, j) -> {
			synchronized(b)
			{
				b.fightPracticeBattle(b.getUserID(), is_surge, versus_off,false);
	
				LSE lse = new LSE(b, false, null);
				lse.update();
				try {
					if(data.length<5)Thread.sleep(1000 * sec_save_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	
				if(!lse.enemy_name.equals(b.getName())) return "E: Wrong name";
				if(lse.winner != 0 && lse.winner != 1) 	return "E: No winner";
				if(is_surge != lse.is_surge) return "E: Surge error";
				
				if(data[j][0] == null) { 
					data[j][0]=b.getName();
					data[j][1]= b.getGuild();
					String off =GlobalData.getDeckString(b.getOffDeck());
					String def = GlobalData.getDeckString(b.getDefDeck());
					data[j][2] = off;
					data[j][3] = def;
					data[j][4] = Double.valueOf(0);
					data[j][5] = Double.valueOf(0);
					data[j][6] = Double.valueOf(0);
					Param p = b.getOffParamWithDom(versus_off?off:def,tuo_nums);
					p.BGE = lse.bge;
					p.order = Order.random;
					p.mode = Mode.pvp;
					p.op = OP.sim;
					p.fight_surge = is_surge?"surge":"fight";
					Result r = TUMTUO.sim(b, p);
					data[j][7] = Double.valueOf(r.WINS);
					data[j][8] = Double.valueOf(0);
				}
				if(lse.winner == 1) {
					data[j][4] = (Double)data[j][4]+1;
					data[j][5] = (Double)data[j][5]+1;
					data[j][6] = (Double)data[j][5]/(Double)data[j][4]*100;
					data[j][8] = Math.abs((Double)data[j][7]-(Double)data[j][6]);
					return "WIN";
				}
				if(lse.winner == 0) {
					data[j][4] = (Double)data[j][4]+1;
					data[j][6] = (Double)data[j][5]/(Double)data[j][4]*100;
					data[j][8] = Math.abs((Double)data[j][7]-(Double)data[j][6]);
					return "LOSS";
				}
			}
			return "E: This shouldn't happen";
		});

		GUI.createDataTableWindow(data,columnNames, "Practice Results",4,Double.class);
	}
	//TODO finalize
	public void guild_practice_dump_pressed()
	{
		int practice_nums = practice_times.getNumber();
		int tuo_nums = tuo_iters.getNumber();
		boolean is_surge = surge.isSelected();
		boolean versus_off = vs_off.isSelected();

		Bot b = TUM._this.botovp.getSelectedBots()[0];
		
		String[] columnNames = {
				"Name",
                "Enemy",
                "OWN_Off_Deck",
                "ENEMY_Def_Deck",
                "FIGHTS",
                "WINS",
                "WINRATE [%]",
                "TUOWINRATE [%]",
                "DIFFRATE [%]"
                };

		int[] guild_members_ids = b.getGuildMemberIDs();
		String[] guild_names_ids = b.getGuildMemberNames();
		
		Object[][] data = new Object[guild_members_ids.length][9];
		BotControlPanel.dumpBotsSingle(guild_names_ids,"LSE Practice",practice_nums, (s, j) -> {

			synchronized(b) {

				if(data[j][0] == null) { 
					JediDeckGrab.manualAddPlayerLocal(s, b.getGuild(), b.getGuildMemberDeck(guild_members_ids[j], !versus_off),TUM.time());
				}
				b.fightPracticeBattle(guild_members_ids[j], is_surge, versus_off,true);
	
				LSE lse = new LSE(b, false, null);
				lse.update();
				try {
					Thread.sleep(1000 * sec_save_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(!lse.enemy_name.equals(s)) return "E: Wrong name";
				if(lse.winner != 0 && lse.winner != 1) 	return "E: No winner";
				if(is_surge != lse.is_surge) return "E: Surge error";
				
				if(data[j][0] == null) { 
					data[j][0]=b.getName();
					data[j][1]= s;
					String off = GlobalData.getDeckString(b.getOffDeck());
					String def = GlobalData.getDeckString(b.getGuildMemberDeck(guild_members_ids[j], !versus_off)); //TODO get ENEMY deck
					data[j][2] = off;
					data[j][3] = def;
					data[j][4] = Double.valueOf(0);
					data[j][5] = Double.valueOf(0);
					data[j][6] = Double.valueOf(0);
					Param p = b.getOffParamWithDom(def,tuo_nums);
					p.BGE = lse.bge;
					p.order = Order.flexible;
					p.mode = Mode.pvp;
					p.op = OP.sim;
					p.fight_surge = is_surge?"surge":"fight";
					Result r = TUMTUO.sim(b, p);
					data[j][7] = Double.valueOf(r.WINS);
					data[j][8] = Double.valueOf(0);
				}
				if(lse.winner == 1) {
					data[j][4] = (Double)data[j][4]+1;
					data[j][5] = (Double)data[j][5]+1;
					data[j][6] = (Double)data[j][5]/(Double)data[j][4]*100;
					data[j][8] = Math.abs((Double)data[j][7]-(Double)data[j][6]);
					return "WIN";
				}
				if(lse.winner == 0) {
					data[j][4] = (Double)data[j][4]+1;
					data[j][6] = (Double)data[j][5]/(Double)data[j][4]*100;
					data[j][8] = Math.abs((Double)data[j][7]-(Double)data[j][6]);
					return "LOSS";
				}
			}
			return "E: This shouldn't happen";
		});

		GUI.createDataTableWindow(data,columnNames, "Practice Results",4,Double.class);
	}

}
