package de.neuwirthinformatik.Alexander.TU.TUM;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Deck.Type;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.CreatorV2.CreatorV2Return;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import de.neuwirthinformatik.Alexander.TU.util.Pair;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;

/*
 * Log!
 *
 */
public class ViewBot {

	Bot b;
	JFrame frame;
	Container pane;
	JTextField off_field;
	JTextField def_field;
	JTextField off_id_field;
	JTextField def_id_field;
	JTextArea s_field, log, fusion_log;
	JTextArea area;
	JTable table;
	SimpleDateFormat sdf;
	String last_update = "";

	public ViewBot(Bot b) {
		if (b == null)
			return;

		this.b = b;
		b.update();

		sdf = new SimpleDateFormat("HH:mm:ss");
		last_update = sdf.format(Calendar.getInstance().getTime());

		frame = new JFrame();
		frame.setTitle(b.getName() + " [" + b.getGuild() + "]");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(true);

		pane = frame.getContentPane();

		Dimension d;

		off_field = GUI.text("", true, 700);
		// d = off_field.getPreferredSize();
		// d.height +=20;
		// off_field.setPreferredSize(d);

		def_field = GUI.text("", true, 700);
		// d = def_field.getPreferredSize();
		// d.height +=20;
		// def_field.setPreferredSize(d);

		off_id_field = GUI.text("", true, 50);
		def_id_field = GUI.text("", true, 50);

		updateDeck();
		JPanel pl = new JPanel();
		pl.setLayout(new BoxLayout(pl, BoxLayout.Y_AXIS));
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(new TextField("Off Deck"));
		p1.add(off_id_field);
		p1.add(off_field);
		pl.add(p1);
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		p2.add(new TextField("Def Deck"));
		p2.add(def_id_field);
		p2.add(def_field);
		pl.add(p2);
		pane.add(pl, BorderLayout.PAGE_START);

		Object[][] data = getFusable();
		final double cur_wins;
		if (TUM.settings.sim_options())
			cur_wins = b.simOffDeck().WINS;
		else
			cur_wins = 0;
		String[] columnNames1 = { "name", "costs", "wins (" + cur_wins + ")" };
		String[] columnNames2 = { "name", "costs" };

		DefaultTableModel model = new DefaultTableModel(data,
				TUM.settings.sim_options() ? columnNames1 : columnNames2) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				default:
					return Double.class;
				}
			}
		};

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				createCard((String) table.getValueAt(row, 0));
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);

		pane.add(scrollPane, BorderLayout.CENTER);

		area = new JTextArea();
		area.setEditable(false);
		updateInv();

		scrollPane = new JScrollPane(area);
		pane.add(scrollPane, BorderLayout.LINE_START);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		s_field = new JTextArea();
		updateMS();
		p.add(s_field, p);
		JButton button_s = new JButton("Search");
		button_s.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String msg = JOptionPane.showInputDialog(null, "Search string", "");
				if (msg == null)
					return;
				String rep = "--------------------\nInventory:\n--------------------\n";
				int count = 0;
				for (int j = 0; j < b.getInventory().length;) {
					count = GlobalData.getCount(b.getInventory(), b.getInventory()[j]);
					if (b.getInventory()[j] != 0 && GlobalData.getCardByID(b.getInventory()[j]) != null && (StringUtil
							.containsIgnoreSpecial(GlobalData.getCardByID(b.getInventory()[j]).getName(), msg)))
						rep += GlobalData.getNameAndLevelByID(b.getInventory()[j]) + "#" + count + "\n";
					j += count;
				}
				rep += "--------------------\nRestore:\n--------------------\n";
				for (int j = 0; j < b.getRestore().length;) {
					count = GlobalData.getCount(b.getRestore(), b.getRestore()[j]);
					if (b.getRestore()[j] != 0 && GlobalData.getCardByID(b.getRestore()[j]) != null && (StringUtil
							.containsIgnoreSpecial(GlobalData.getCardByID(b.getRestore()[j]).getName(), msg)))
						rep += GlobalData.getNameAndLevelByID(b.getRestore()[j]) + "#" + count + "\n";
					j += count;
				}
				// JOptionPane.showMessageDialog(null, rep,"Card
				// search",JOptionPane.INFORMATION_MESSAGE);
				GUI.info("Card search", rep);
			}
		});
		p.add(button_s, p);
		JButton button_ms = new JButton("Update Gold+SP");
		button_ms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log("Update MS");
				b.updateData();
				updateMS();
			}
		});
		p.add(button_ms, p);
		JButton button_buy = new JButton("Buy Max. SP");
		button_buy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log("Buy start");
				new Thread(new Runnable() {
					public void run() {
						b.buy();
						log("Buy end");
						update();
					}
				}).start();
			}
		});
		p.add(button_buy, p);

		JButton create_card = new JButton("Create Card");
		create_card.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log("create card manual");
				String name = JOptionPane.showInputDialog(frame, "Create Card:");
				createCard(name);

			}
		});
		p.add(create_card, p);

		JButton button_items = new JButton("Use items");
		button_items.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log("Items start");
				new Thread(new Runnable() {
					public void run() {
						b.consumeShards();
						log("Items end");
					}
				}).start();
			}
		});
		p.add(button_items, p);

		p.add(GUI.buttonAsync("Leave Guild", () -> b.leaveGuild()), p);

		p.add(GUI.buttonAsync("Guild Invites",
				() -> GUI.createDataTableWindow(b.getFactionInvites(),
						new String[] { "Guild", "Leader", "Inviter", "Id", "Members", "Raing" }, "Guild Invites", 3,
						Integer.class, "Join Selected", (o) -> {
							if (o != null && o.length != 0)
								b.acceptGuildInvite((int) o[0][3]);
						})),
				p);

		JButton button_guild = new JButton("Move to Guild");
		button_guild.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String guild = (String) JOptionPane.showInputDialog(null, "Choose a Guild", "Guild Move",
						JOptionPane.QUESTION_MESSAGE, null, GlobalBotData.getInviteGuildNames(), b.getGuild());
				for (Bot l : GlobalBotData.getGuildOfficers()) {
					if (guild != null && guild.equals(l.getGuild())) {
						log("Moving to Guild: " + guild);
						if (!b.getGuild().equals(guild))
							b.leaveGuild();

						l.sendGuildInvite(b.getUserID());
						b.acceptGuildInvite(l.getGuildID());
						b.updateGuild();
						return;
					}
				}

			}
		});
		p.add(button_guild, p);

		JButton button_update = new JButton("Update");
		button_update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					public void run() {
						update();
					}
				}).start();
				;
			}
		});
		p.add(button_update, p);

		JButton button_deck = new JButton("Save Deck");
		button_deck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				fusion_log("Saving Deck\n");

				b.setCurlDecks(off_field.getText(), def_field.getText(), Integer.parseInt(off_id_field.getText()),
						Integer.parseInt(def_id_field.getText()));
				b.updateData();
				b.updateDeck();
				updateDeck();
				updateInv();
			}
		});
		p.add(button_deck, p);

		JButton button_climb_deck = new JButton("Climb+Save Deck");
		button_climb_deck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						// fusion_log("Saving Deck\n");
						log("Start Climb");
						b.climbAndSetDeck(Type.BOTH); // TODO maybe other optionnnnnnns
						log("Best climb deck: " + GlobalData.getDeckString(b.getOffDeck()));
						fusion_log("Best Deck\n");
						updateDeck();
						updateInv();
					}
				}).start();
			}
		});
		p.add(button_climb_deck, p);

		JButton button_fund_deck = new JButton("Fund Climb Deck");
		button_fund_deck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						// fusion_log("Saving Deck\n");
						log("Start Fund Climb");
						fusion_log("\nBest fund climb: " + b.fundClimbDeck() + "\n");
						// updateDeck();
						// updateInv();
					}
				}).start();
			}
		});
		p.add(button_fund_deck, p);

		JButton button_msg = new JButton("Guild Message");
		button_msg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						// fusion_log("Saving Deck\n");
						String msg = JOptionPane.showInputDialog(null, "Guild message", "");
						if (msg == null)
							return;
						log("Send Message: " + msg);
						b.sendGuildMessage(msg);
						// fusion_log("\nBest fund climb: " + b.fundClimbDeck() +"\n");
						// updateDeck();
						// updateInv();
					}
				}).start();
			}
		});
		p.add(button_msg, p);

		pane.add(new JScrollPane(p), BorderLayout.LINE_END);

		pl = new JPanel();
		pl.setLayout(new BoxLayout(pl, BoxLayout.X_AXIS));

		log = new JTextArea();
		scrollPane = new JScrollPane(log);
		d = scrollPane.getPreferredSize();
		d.height += 100;
		scrollPane.setPreferredSize(d);
		DefaultCaret caret = (DefaultCaret) log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pl.add(scrollPane);

		fusion_log = new JTextArea();
		scrollPane = new JScrollPane(fusion_log);
		d = scrollPane.getPreferredSize();
		d.height += 100;
		scrollPane.setPreferredSize(d);
		caret = (DefaultCaret) fusion_log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pl.add(scrollPane);

		pane.add(pl, BorderLayout.PAGE_END);

		// frame.setContentPane(panel);
		frame.setPreferredSize(new Dimension(625, 600));

		frame.pack();
		frame.setVisible(true);
	}

	public synchronized void update() {
		log("Update start");
		frame.setTitle("Updating");
		try {
			b.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		last_update = sdf.format(Calendar.getInstance().getTime());
		updateDeck();
		updateFuse();
		updateInv();
		updateMS();
		frame.setTitle(b.getName());
		log("Update end");
		// SwingUtilities.invokeLater(new Runnable() {public void
		// run(){frame.revalidate();}});
		// SwingUtilities.invokeLater(new Runnable() {public void
		// run(){frame.repaint();}});
		/*
		 * frame.dispose(); new ViewBot(b);
		 */
	}

	public synchronized void updateFuse() {
		Object[][] data = getFusable();
		final double cur_wins;
		if (TUM.settings.sim_options())
			cur_wins = b.simOffDeck().WINS;
		else
			cur_wins = 0;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// table
				String[] columnNames1 = { "name", "costs", "wins (" + cur_wins + ")" };
				String[] columnNames2 = { "name", "costs" };
				((DefaultTableModel) table.getModel()).setDataVector(data,
						TUM.settings.sim_options() ? columnNames1 : columnNames2);
			}
		});
	}

	public synchronized Object[][] getFusable() {
		if (TUM.settings.sim_options())
			log("Mass simming Quad Options");
		ArrayList<Pair<Card, Integer>> ac = new ArrayList<Pair<Card, Integer>>();
		CreatorV2Return cr;
		for (Card c : TUM.settings.fuseCards()) {
			// add to
			if (c != null
					&& ((c.getFusionLevel() == 2 && TUM.settings.sim_quad())
							|| (c.getFusionLevel() == 1 && TUM.settings.sim_dual())
							|| (c.getFusionLevel() == 0 && TUM.settings.sim_single()))
					&& (cr = b.couldCreateCard(c)).possible)
				ac.add(new Pair<Card, Integer>(c, cr.sp_cost));
		}

		Object[][] data = new Object[ac.size()][3];

		for (int i = 0; i < ac.size(); i++) {
			// data[i][0] = ac.get(i);
			data[i][0] = ac.get(i).t.getName();
			data[i][1] = ac.get(i).u;
			// System.out.println("SIM: " + ac.get(i).getName());
			if (TUM.settings.sim_options()) {
				log("Mass sim progress: " + (i * 100) / ac.size() + "%");
				Param p = b.getOffParamWithDom(TUM.settings.sim_iter());
				p.deck = data[i][0] + ", " + p.deck;
				data[i][2] = TUMTUO.sim(b, p).WINS;
			}
		}
		return data;
	}

	public void updateInv() // TODO StringBuilder?
	{
		// inv
		String inv = "U: " + last_update + "\n-----------------\n";
		inv += "DECK_SIZE: " + (off_field.getText().split(",").length - 3) + "\n-----------------\n";
		inv += "Inventory (Dual+Quad)" + "\n-----------------\n";
		int shard_id = GlobalData.getIDByNameAndLevel("Dominion Shard-2");
		int count = 0;
		for (int j = 0; j < b.getInventory().length;) {
			count = GlobalData.getCount(b.getInventory(), b.getInventory()[j]);
			if (b.getInventory()[j] != 0 && GlobalData.getCardByID(b.getInventory()[j]) != null
					&& (GlobalData.getCardByID(b.getInventory()[j]).getFusionLevel() >= 1
							|| b.getInventory()[j] == shard_id))
				inv += GlobalData.getNameAndLevelByID(b.getInventory()[j]) + "#" + count + "\n";
			j += count;
		}

		// restore
		inv += "\n-----------------\n" + "Restore (Dual+Quad)" + "\n-----------------\n";
		count = 0;
		for (int j = 0; j < b.getRestore().length;) {
			count = GlobalData.getCount(b.getRestore(), b.getRestore()[j]);
			if (b.getRestore()[j] != 0 && GlobalData.getCardByID(b.getRestore()[j]) != null
					&& (GlobalData.getCardByID(b.getRestore()[j]).getFusionLevel() >= 1
							|| b.getRestore()[j] == shard_id))
				inv += GlobalData.getNameAndLevelByID(b.getRestore()[j]) + "#" + count + "\n";
			j += count;
		}
		area.setText(inv);

	}

	public void updateDeck() {
		off_id_field.setText("" + b.getOffDeckID());
		def_id_field.setText("" + b.getDefDeckID());
		// deck
		off_field.setText(GlobalData.getDeckString(b.getOffDeck()));
		def_field.setText(GlobalData.getDeckString(b.getDefDeck()));
	}

	public void updateMS() {
		s_field.setText("GOLD: " + b.getMoney() + "\n" + "SP: " + b.getSalvage() + "/" + b.getMaxSalvage() + "\n"
				+ "FUND: " + b.getFund() + "\nCards: " + b.getNumCards() + "/" + b.getMaxCards());
	}

	public void createCard(String name) {
		if (name == null)
			return;
		Card card = GlobalData.getCardByName(name);
		b.updateData();
		updateMS();

		if (b.getSalvage() < 1925)
			JOptionPane.showConfirmDialog(frame, "WARNING: NOT ENOUGH SP");
		int n = JOptionPane.showConfirmDialog(frame, "Fuse: " + card.getName() + "?", "Confirm fuse",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			log("Fusing " + card.getName());
			new Thread(new Runnable() {
				public void run() {
					b.createCard(card);
					fusion_log(card.getName());
					b.updateInv();
					b.updateData();
					updateInv();
					updateMS();
					updateFuse();
				}
			}).start();
		}
	}

	public void log(String msg) {
		System.out.println(b.getName() + "-Log: " + msg);
		if (log != null)
			log.append("[" + sdf.format(Calendar.getInstance().getTime()) + "]: " + msg + "\n");
	}

	public void fusion_log(String msg) {
		// System.out.println(b.getName() +"-Log:" + msg);
		if (fusion_log != null)
			fusion_log.append(msg + ", ");
		log("Fuse-log: " + msg);
	}

	public static void main(String[] args) throws Exception {
		// System.out.println("Single user mode");
		Main.main(args);
	}
}
