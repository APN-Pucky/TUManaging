package de.neuwirthinformatik.Alexander.TU.TUM;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.JediDeckGrab;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.LSE;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class LiveSimPanel extends JPanel{


	Bot b;
	LSE lse;
	Thread auto_thread;
	//JFrame frame;
	//Container pane;

	JTextField enemy_name;
	JTextField guild_name;
	JTextField enemy_time;

	JTextArea deck;
	//int[] deck_ids;
	JTextArea enemy_deck;
	//int[] enemy_deck_ids;
	JTextField hand_field;
	//int[] hand_field_ids;
	JTextField cur_own_field;
	//int[] cur_own_field_ids;
	JTextField cur_own_fort;
	//int[] cur_own_fort_ids;
	JTextField cur_enemy_field;
	//int[] cur_enemy_field_ids;
	JTextField status; 
	JTextField result; // clear on update
	JTextField playcard; // clear on update
	JCheckBox mission_raid_campaign;

	JButton grab_deck;
	JButton load_guild;
	JToggleButton automatic;
	JButton update_field;
	JButton sim;
	JButton reset;

	public void setBot(Bot bot)
	{
		b = bot;
		lse = new LSE(b,mission_raid_campaign.isSelected(), TUM.settings.mode());

		deck.setText(lse.deck);
	}
	
	public LiveSimPanel() {
		super();
		
		
		enemy_name = GUI.textEdit();
		guild_name = GUI.textEdit();
		enemy_time = GUI.text();
		deck = GUI.area();
		enemy_deck = GUI.area();
		hand_field = GUI.text();
		cur_own_field = GUI.text();
		cur_own_fort = GUI.text();
		cur_enemy_field = GUI.text();
		status = GUI.text();
		result = GUI.text();
		playcard = GUI.text();
		mission_raid_campaign = GUI.check("Mission/Raid/Campaign");
		mission_raid_campaign.setSelected(false);

		grab_deck = new JButton("Grab Deck");
		load_guild = new JButton("Load Guild");
		automatic = new JToggleButton("Auto");
		update_field = new JButton("update");
		sim = new JButton("SIM");
		reset = new JButton("RESET");
		
		//LOAD Stuff


		JPanel p = new JPanel();
		p.setLayout((LayoutManager) new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setPreferredSize(new Dimension(1000,600));
		//JScrollPane scrPane = new JScrollPane(p);
		//frame.add(p);

		
		
		JComboBox<Bot> botList = new JComboBox<Bot>(GlobalBotData.bots);
		if(GlobalBotData.bots.length >0){
			botList.setSelectedIndex(0);
			setBot( (Bot) botList.getSelectedItem()); //TODO ggf null checks here or on method
			deck.setText(lse.deck);
		}
		botList.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
				setBot( (Bot) botList.getSelectedItem());
				
			}
			
		});
		

		
		p.add(botList);
		
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
			panel.add(GUI.label("Enemy Name: "));
			panel.add(enemy_name);	

			panel.add(GUI.label("Guild Name: "));
			panel.add(guild_name);	
			
			panel.add(GUI.label("Deck time: "));	
			panel.add(enemy_time);

			GUI.makeCompactGrid(panel,
	                    3, 2, 		//rows, cols
	                    6, 6,        //initX, initY
	                    6, 6);       //xPad, yPad
		p.add(panel);
		
		panel  = new JPanel();
			panel.add(grab_deck);	
			panel.add(load_guild);
		p.add(panel);
		
		
		
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
			panel.add(GUI.label("Your deck (without dominion): "));
			panel.add(deck);		
		//p.add(panel);
		
			panel.add(GUI.label("Enemy deck (with dominion): "));
			panel.add(enemy_deck);	
		

			panel.add(GUI.label("Your card draw order"));
			panel.add(hand_field);
		
			panel.add(GUI.label("Your played cards:"));
			panel.add(cur_own_field);
		
			panel.add(GUI.label("Your forts and dominions:"));
			panel.add(cur_own_fort);
		
			panel.add(GUI.label("Enemy played cards:"));
			panel.add(cur_enemy_field);
		GUI.makeCompactGrid(panel,
                    6, 2, 		//rows, cols
                    6, 6,        //initX, initY
                    6, 6);       //xPad, yPad
		p.add(panel);
		
		panel  = new JPanel();
			panel.add(mission_raid_campaign);
			panel.add(automatic);
			panel.add(update_field);
			panel.add(sim);
			panel.add(reset);
		p.add(panel);
		
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
			panel.add(GUI.label("Status:"));
			panel.add(status);
			panel.add(GUI.label("Ordered Result:"));
			panel.add(result);
			panel.add(GUI.label("Best Play:"));
			panel.add(playcard);
			
			GUI.makeCompactGrid(panel,
                    6, 1, 		//rows, cols
                    6, 6,        //initX, initY
                    6, 6);   
		p.add(panel);

		load_guild.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JediDeckGrab.loadGuild(guild_name.getText());

			}

		});
		
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}

		});

		grab_deck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String gdeck = GlobalData.getDeckString(JediDeckGrab.getDeck(enemy_name.getText()));
				enemy_deck.setText(gdeck);
				enemy_time.setText(JediDeckGrab.getTime(enemy_name.getText()));
			}

		});
		
		automatic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(automatic.isSelected())
				{
					if(auto_thread == null)
					{
						auto_thread = new Thread(new Runnable(){

							@Override
							public void run() {
								while(auto_thread != null)
								{
									String own = lse.cur_own_field; //TODO Check Manual changing
									String enemy = lse.cur_enemy_field;
									update();
									if(!lse.cur_own_field.equals(own) || !lse.cur_enemy_field.equals(enemy))
									{
										sim();
									}
									try {
										Thread.sleep(2500);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
							}
							
						});
						auto_thread.start();
					}
				}
				else
				{
					auto_thread = null;
				}
				
				
			}

		});

		update_field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				update();
			}
		});

		sim.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				sim();                                           
			}

		});

		JScrollPane scrollPane = new JScrollPane(p);

        this.add(scrollPane);
		//this.add(p);
		//pane.add(p);

		//frame.pack();
		//frame.setVisible(true);
	}

	protected void sim() {
		lse.enemy_deck = enemy_deck.getText();
		lse.deck = deck.getText();
		result.setText("");
		playcard.setText("");
		status.setText("Simming");
		
		String tuo_deck = lse.sim();
		

		// fix order by add instead of replace
		Param p = new Param("",lse.param_edeck, TUM.settings.lse_iter());
		
		
		p.BGE = lse.bge;
		p.ye = lse.ybge;
		p.ee = lse.ebge;
		
		
		p.yf = GlobalData.getDeckString(lse.cur_own_fort_ids);
		p.hand = hand_field.getText();
		p.enemy_hand = cur_enemy_field.getText();
		p.freeze = GlobalData.constructCardArray(cur_own_field.getText()).length;//- Data.constructCardArray(ViewSettings.yf()).length;
		p.op = OP.reorder;
		p.order = Order.ordered;
		//p.enemy_ordered = true;
		p.deck = tuo_deck;
		String cur_score = String.valueOf(TUMTUO.sim(b, p).WINS);
		p.order = Order.random;
		{ //manual hand ordering
			int[] cdeck = GlobalData.constructDeck(p.hand).toIDArray();
			for (int id : lse.cur_own_field_ids) {
				for (int i = 0; i < cdeck.length; i++) {
					if (cdeck[i] == id) {
						cdeck[i] = 0;
						break;
					}
				}
			}

			p.hand = GlobalData.getDeckString(lse.cur_own_field_ids) + " ," + GlobalData.getDeckString(cdeck);

		}
		
		String auto_score_1 = String.valueOf(TUMTUO.sim(b, p).WINS);
		p.hand = cur_own_field.getText();
		String auto_score_2 = String.valueOf(TUMTUO.sim(b, p).WINS);
		result.setText(cur_score + " ( auto: "+ auto_score_1 + ", random: " + auto_score_2 + " )"+ " || " + tuo_deck);// TODO print WR
		
		
		//get first card currently in hand
		
		int cur_hand_best = lse.get_first(tuo_deck);
		playcard.setText(GlobalData.getNameAndLevelByID(cur_hand_best));
		//for(int i = p.freeze; i < )
		status.setText("Simming DONE");
	}
	
	protected void reset()
	{
		lse.reset();
		lse.is_edeck_list = !mission_raid_campaign.isSelected();
		auto_thread = null;
		hand_field.setText("");
		cur_own_field.setText("");
		cur_own_fort.setText("");
		cur_enemy_field.setText("");
		playcard.setText("");
		result.setText(""); // clear on update
	}

	protected void update() {
		status.setText("Updating");
		lse.update();
		syncStrings();
		status.setText("Updating DONE");
	}
	
	private void syncStrings()
	{
		String tmp_name = enemy_name.getText();
		enemy_name.setText(lse.enemy_name);
		
		
		if(!tmp_name.equals(lse.enemy_name))
		{
			String edeck = GlobalData.getDeckString(JediDeckGrab.getDeck(lse.enemy_name));
			lse.enemy_deck = edeck;
			enemy_deck.setText(lse.enemy_deck);
		}
		
		lse.deck = deck.getText();
		lse.enemy_deck = enemy_deck.getText();
		
		hand_field.setText(lse.hand_field);;
		cur_own_field.setText(lse.cur_own_field);
		cur_own_fort.setText(lse.cur_own_fort);
		cur_enemy_field.setText(lse.cur_enemy_field);
	}

}
