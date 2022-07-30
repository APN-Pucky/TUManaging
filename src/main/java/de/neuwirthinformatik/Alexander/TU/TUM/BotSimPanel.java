package de.neuwirthinformatik.Alexander.TU.TUM;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;

import de.neuwirthinformatik.Alexander.TU.Basic.Deck;
import de.neuwirthinformatik.Alexander.TU.Basic.Deck.Type;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Result;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class BotSimPanel extends JPanel {
	
	private JComboBox<Deck.Type> decktype;
	private GUI.IntegerField off_id, def_id;
	
	public BotSimPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(GUI.label("Options"));
		
		JPanel p = new JPanel();
			//p.setLayout(new SpringLayout());
			p.add(GUI.label("Deck: "));
			decktype = new JComboBox<Deck.Type>(Deck.Type.values());
			decktype.setSelectedIndex(0);
			p.add(decktype);
		panel.add(p);
		
		p = new JPanel(); 
			//p.setLayout(new SpringLayout());
			JPanel panel1 = new JPanel();
			panel1.add(GUI.label("Off ID: "));
			panel1.add(off_id = new GUI.IntegerField(2));
			p.add(panel1);
	
			panel1 = new JPanel();
			panel1.add(GUI.label("Def ID: "));
			panel1.add(def_id = new GUI.IntegerField(1));
			p.add(panel1);
			
			p.add(GUI.buttonAsync("Set IDS", () -> {for(Bot b : TUM._this.botovp.getSelectedBots()){b.setActiveDeckSlot(off_id.getNumber()); b.setDefenseDeckSlot(def_id.getNumber());}} ));
		panel.add(p);
		
		
		panel.add(new JSeparator());
		panel.add(GUI.label("Change Decks"));
		
		p = new JPanel();
		p.setLayout(new SpringLayout());
		p.add(GUI.buttonAsync("Climb And Save random selected decks", () -> button_climb_all_pressed(Order.random)));
		p.add(GUI.buttonAsync("Climb And Save ordered selected decks", () -> button_climb_all_pressed(Order.ordered)));
		
		p.add(GUI.buttonAsync("Fund,Fuse,Climb And Save random selected decks", () -> button_fund_all_pressed(Order.random)));
		p.add(GUI.buttonAsync("Fund,Fuse,Climb And Save ordered selected decks", () -> button_fund_all_pressed(Order.ordered)));
		
		GUI.makeCompactGrid(p,
                2, 2, 		//rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		panel.add(p);
		

		panel.add(new JSeparator());
		
		
		panel.add(GUI.label("Benchmark Bots"));
		
		
		p = new JPanel();
		p.setLayout(new SpringLayout());
		
		//With Active hack support
		p.add(GUI.buttonAsync("Benchmark random Bots", () -> button_benchmark_sim_all_pressed()));
		p.add(GUI.buttonAsync("Benchmark ordered Bots", () -> button_benchmark_reorder_all_pressed()));
		p.add(GUI.buttonAsync("Benchmark flexible Bots", () -> button_benchmark_flexible_all_pressed()));
		
		
		//Without active hack support
		p.add(GUI.buttonAsync("Benchmark climb random Bots", () -> button_benchmark_climb_all_pressed(Order.random)));
		p.add(GUI.buttonAsync("Benchmark climb ordered Bots", () -> button_benchmark_climb_all_pressed(Order.ordered)));
		p.add(GUI.buttonAsync("Benchmark climb flexible Bots", () -> button_benchmark_climb_all_pressed(Order.flexible)));
		
		p.add(GUI.buttonAsync("Benchmark fund-climb random Bots", () -> button_benchmark_fund_all_pressed(Order.random)));
		p.add(GUI.buttonAsync("Benchmark fund-climb ordered Bots", () -> button_benchmark_fund_all_pressed(Order.ordered)));
		p.add(GUI.buttonAsync("Benchmark fund-climb flexible Bots", () -> button_benchmark_fund_all_pressed(Order.flexible)));
		
		
		GUI.makeCompactGrid(p,
                3, 3, 		//rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		panel.add(p);
		
		
		add(panel);
	}
	
	public void button_climb_all_pressed(Order order)
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];
		Type t = (Type) decktype.getSelectedItem();
		
		TUM.pp.forEach("Climbing " + order + " Bots", bots, (b,j) -> 
		{	
			String d = b.climbAndSetDeck(order,t);
			Param    p = b.getOffParamWithDom(TUM.settings.sim_iter());
			p.order = order;
			p.deck = d;
			Result r = TUMTUO.sim(b,p);
			data[j] = new Object[]{b.getName(),b.getGuild(),d, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + d;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Climb " + order + " Results",3,Double.class);
	
		
	}
	
	public void button_fund_all_pressed(Order order) 
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];

		Type t = (Type) decktype.getSelectedItem();
		TUM.pp.forEach("Funding " + order +" Bots", bots, (b,j) -> 
			{
				b.consumeShards();
				String d = b.createFundedDeck(order,t);
				Param p = b.getOffParamWithDom(TUM.settings.sim_iter());
				p.order = order;
				p.deck = d;
				Result r = TUMTUO.sim(b,p);
				data[j] = new Object[]{b.getName(),b.getGuild(),d, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
				return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + d;
			});
		GUI.createDataTableWindow(data, columnNames, "Funded Climb " + order + " Results",3,Double.class);
		
	}
	
	public void button_benchmark_sim_all_pressed()
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];
		
		TUM.pp.forEach("Benchmarking-Simming Bots", bots, (b,j) -> {
			Param p = b.getOffParamWithDom(TUM.settings.sim_iter());
			p.order = Order.random;
			Result r = TUMTUO.sim(b, p);
			String deck = GlobalData.getDeckString(b.getOffDeck());
			data[j] = new Object[]{b.getName(),b.getGuild(),deck, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + deck;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Benchmark Sim",3,Double.class);
	}
	
	public void button_benchmark_reorder_all_pressed()
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];
		
		TUM.pp.forEach("Benchmark-Reordering Bots", bots, (b,j) -> {
			String d = b.reorderDeck();
			Param p = b.getOffParamWithDom(TUM.settings.sim_iter());
			p.deck = d;
			p.order = Order.ordered;
			Result r = TUMTUO.sim(b, p);
			
			data[j] = new Object[]{b.getName(),b.getGuild(),d, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + d;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Benchmark Reorder",3,Double.class);
	}
	
	public void button_benchmark_flexible_all_pressed()
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];
		
		TUM.pp.forEach("Benchmarking-Simming Bots", bots, (b,j) -> {
			Param p = b.getOffParamWithDom(TUM.settings.sim_iter());
			p.order = Order.flexible;
			Result r = TUMTUO.sim(b, p);
			String deck = GlobalData.getDeckString(b.getOffDeck());
			data[j] = new Object[]{b.getName(),b.getGuild(),deck, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + deck;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Benchmark Sim",3,Double.class);
	}
	
	public void button_benchmark_climb_all_pressed(Order order)
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];

		Type t = (Type) decktype.getSelectedItem();
		TUM.pp.forEach("Benchmark-Climbing-" + order + " Bots", bots, (b,j) -> {
			String d = b.climbDeck(order);
			Param p = new Param(d, TUM.settings.sim_iter());
			p.order = order;
			Result r = TUMTUO.sim(b,p);
			
			data[j] = new Object[]{b.getName(),b.getGuild(),d, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + d;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Benchmark Climb " + order ,3,Double.class, "Set Climbed " + order + " Selected Decks", (o)->
		{
			
			TUM.pp.forEach("Setting Climbed " + order + " Selected Decks",o, (line) -> {
				Bot b = GlobalBotData.getBotByName((String)line[0]);
				return b +": " + (b.setFundedDeck((String)line[2],t)?"success":"error");
			});
		});
	}
	
	
	
	public void button_benchmark_fund_all_pressed(Order order)
	{
		String[] columnNames = {
				"Name",
                "Guild",
                "Deck",
                "Score",
                "Wins",
                "Stalls",
                "Losses"};
		
		Bot[] bots = TUM._this.botovp.getSelectedBots();
		Object[][] data = new Object[bots.length][7];

		Type t = (Type) decktype.getSelectedItem();
		TUM.pp.forEach("Benchmark-Funding " + order + " Bots", bots, (b,j) -> {
			String d = b.fundClimbDeck(order);
			Param p = new Param(d, TUM.settings.sim_iter());
			p.order = order;
			Result r = TUMTUO.sim(b,p);
			
			data[j] = new Object[]{b.getName(),b.getGuild(),d, r.SCORE, r.WINS, r.STALLS, r.LOSSES};
			return b +": " +r.WINS+ "% (" + r.SCORE+ "): " + d;
		});
		
		GUI.createDataTableWindow(data, columnNames, "Benchmark Fund " + order + "",3,Double.class, "Set Funded " + order + " Selected Decks", (o)->
		{
			TUM.pp.forEach("Setting Climbed " + order + " Selected Decks",o, (line) -> {
				Bot b = GlobalBotData.getBotByName((String)line[0]);
				return b + ": "+ (b.setFundedDeck((String)line[2],t)?"success":"error");
			});
		});
	}
}
