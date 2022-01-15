package de.neuwirthinformatik.Alexander.TU.TUM;

import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Deck.Type;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import de.neuwirthinformatik.Alexander.TU.util.GUI.LoadBar;

public class BotManagingPanel extends JPanel {

	private JTextField massFund;
	private JTextField massFuse;
	private JTextField massFuseTo;
	private JTextField massSalvage;
	private JTextField massSalvageTo;

	public BotManagingPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel p = new JPanel();
		p.add(GUI.buttonAsync("Open selected accs", () -> button_open_pressed()));
		p.add(GUI.buttonAsync("Open Bot data", () -> button_bot_data_pressed()));
		p.add(GUI.buttonAsync("Move selected accs", () -> button_move_pressed()));
		p.add(GUI.buttonAsync("Kick accs", () -> button_kick_pressed()));
		panel.add(p);

		// p = new JPanel();
		// p.add(GUI.text("Set Funded Deck on seleceted accs once"));
		// p.add(massFund = GUI.textEdit());
		// p.add( GUI.buttonAsync("Go",() -> button_fund_pressed()));
		// panel.add(p);

		p = new JPanel();
		p.add(GUI.text("Create on selected accs once"));
		p.add(massFuse = GUI.textEdit());
		p.add(GUI.buttonAsync("Go", () -> button_create_pressed()));
		panel.add(p);
		
		p = new JPanel();
		p.add(GUI.text("Create on selected accs to"));
		p.add(massFuseTo = GUI.textEdit());
		p.add(GUI.buttonAsync("Go", () -> button_create_to_pressed()));
		panel.add(p);

		p = new JPanel();
		p.add(GUI.text("Salvage all on selected accs"));
		p.add(massSalvage = GUI.textEdit(TUM.settings.salvage_all));
		p.add(GUI.buttonAsync("Go", () -> button_salvage_pressed()));
		panel.add(p);

		p = new JPanel();
		p.add(GUI.text("Salvage all on selected accs to"));
		p.add(massSalvageTo = GUI.textEdit(TUM.settings.salvage_to));
		p.add(GUI.buttonAsync("Go", () -> button_salvage_to_pressed()));
		panel.add(p);

		p = new JPanel();
		p.add(GUI.buttonAsync("Salvage Common and Rare", () -> button_salvage_cr_pressed()));
		p.add(GUI.buttonAsync("Buy Max SP", () -> button_buy_pressed()));
		panel.add(p);

		p = new JPanel();
		p.add(GUI.buttonAsync("Buy Holiday Quadra (temporary)", () -> button_quadra_pressed()));
		panel.add(p);
		
		p = new JPanel();
		p.add(GUI.buttonAsync("Fix Dom (temporary)", () -> button_fixdom_pressed()));
		panel.add(p);
		
		p = new JPanel();
		p.add(GUI.buttonAsync("Set/Fix Dom 2(temporary)", () -> button_fixdom2_pressed()));
		panel.add(p);
		

		p = new JPanel();
		p.add(GUI.buttonAsync("Generate Guild Gauntlet (1)", () -> button_guild_gauntlet()));
		panel.add(p);
		
		p = new JPanel();
		p.add(GUI.buttonAsync("Unlock All Cards", () -> button_unlock_all()));
		panel.add(p);

		add(panel);
	}
	
	public void button_unlock_all()
	{
		
		Bot[] bs = TUM._this.botovp.getSelectedBots();
	
		TUM.pp.forEach("Unlock All", bs, (b) -> {
			try {
				b.unlockAll();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return b + ": =" + "NULL";
		});
	}
	
	public void button_fixdom_pressed() {
		
			Bot[] bs = TUM._this.botovp.getSelectedBots();

			TUM.pp.forEach("Fix Dom", bs, (b) -> {
				try {
					int[] a = b.getDefDeck();
					a[1] = -1;
					b.getCurl().setDeckCardsActive(a, 1);
					b.getCurl().setDeckCardsActive(a, 2);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return b + ": =" + "NULL";
			});
	}
	
	public void button_fixdom2_pressed() {
		
		Bot[] bs = TUM._this.botovp.getSelectedBots();

		TUM.pp.forEach("Fix Dom2", bs, (b) -> {
			String s = "NULL/-1";
			try {
				int[] a = b.getDefDeck();
				for(int i : b.getInventory())
					if(GlobalData.isDominion(i)) {
						a[1] = i;
						s=GlobalData.getCardInstanceById(i).getName();
						b.getCurl().setDeckCardsActive(a, 1);
						b.getCurl().setDeckCardsActive(a, 2);
						break;
					}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return b + ": =" + s;
		});
}
	
	public void button_quadra_pressed() {
		int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure? No guarantees on anything!", "Warning",
				JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			Bot[] bs = TUM._this.botovp.getSelectedBots();

			TUM.pp.forEach("Buying Quadra", bs, (b) -> {
				try {
					JSONObject o = new JSONObject(b.getCurl().curl_pull("buyStorePromoTokens",
							"&expected_cost=180&item_id=2002&item_type=3"));
					if (o.has("new_cards")) {
						JSONArray a = o.getJSONArray("new_cards");
						return b + ": =" + GlobalData.getCardByID(Integer.parseInt((String) a.get(0))).getName();
					}
				} catch (Exception e) {
				}
				return b + ": =" + "NULL";
			});
		}
	}

	public void button_guild_gauntlet() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		if (bs.length == 0)
			return;
		String guild = bs[0].getGuild();
		final StringBuilder gauntlet = new StringBuilder(guild + "_off: /^" + guild + "_off_.*$/\n");
		gauntlet.append(guild + "_def: /^" + guild + "_def_.*$/\n");
		TUM.pp.forEach("Generating Guild Gauntlet", bs[0].getGuildMemberNames(), (b, j) -> {
			gauntlet.append(guild + "_def_" + b + ": "
					+ GlobalData.getDeckString(bs[0].getGuildMemberDeck(bs[0].getGuildMemberIDs()[j], true)) + "\n"); // pull
																														// defs
			gauntlet.append(guild + "_off_" + b + ": "
					+ GlobalData.getDeckString(bs[0].getGuildMemberDeck(bs[0].getGuildMemberIDs()[j], false)) + "\n"); // pull
																														// defs

			return "Generated " + b;
		});
		GUI.createTextWindow(guild + " - gauntlet", gauntlet.toString());
	}

	public void button_bot_data_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		String[] columnNames = { "Name", "Guild", "Energy", "Stamina", "Money", "WB", "Salvage", "Fund" };

		Object[][] data = new Object[bs.length - GlobalBotData.getNullCount(bs)][7];

		TUM.pp.forEach("Loading Bot Data", bs, (b, j) -> {
			b.updateData();
			b.updateGuild();
			data[j] = new Object[] { b.getName(), b.getGuild(), new Integer(b.getEnergy()), new Integer(b.getStamina()),
					new Integer(b.getMoney()), new Integer(b.getWB()), new Integer(b.getSalvage()),
					new Integer((b.getFund())) };
			return b.getName();
		});

		/*
		 * int j=0; for(int i = 0; i < bs.length;i++) { if(bs[i] !=null) { j++; } }
		 */

		GUI.createDataTableWindow(data, columnNames, "Bot Data");
	}

	public void button_buy_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();

		TUM.pp.forEach("Buying max SP", bs, (b) -> {
			return b + ": SP=" + b.buy();
		});
	}

	public void button_move_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();

		String[] gs = GlobalBotData.getInviteGuildNames();
		if (gs.length == 0)
			return;
		String guild = (String) JOptionPane.showInputDialog(null, "Choose a Guild", "Guild Move",
				JOptionPane.QUESTION_MESSAGE, null, gs, gs[0]);

		TUM.pp.forEach("Moving Bots to " + guild, bs, (b) -> {

			for (Bot l : GlobalBotData.getGuildOfficers()) {
				if (guild != null && guild.equals(l.getGuild())) {
					System.out.println("Moving to Guild: " + guild);
					if (!b.getGuild().equals(guild))
						b.leaveGuild();
					l.sendGuildInvite(b.getUserID());
					b.acceptGuildInvite(l.getGuildID());
					b.updateGuild();
					return b.getName() + " moved (now: " + b.getGuild() + ")";
				}
			}
			return b.getName() + " no officer or leader available.";
		});
	}
	
	public void button_kick_pressed() {

		Bot[] gs = GlobalBotData.getGuildLeaders();
		String guild = (String) JOptionPane.showInputDialog(null, "Choose a Guild", "Guild Kick",
				JOptionPane.QUESTION_MESSAGE, null, Arrays.stream(gs).map(x -> x.getGuild()).toArray(String[]::new), gs[0].getGuild());
		for ( Bot g : gs)
		{
			if(g.getGuild().equals(guild))
			{
				String [] ks = g.getGuildMemberNames();
				int [] kss = g.getGuildMemberIDs();
				String k = (String) JOptionPane.showInputDialog(null, "Choose a member from " + guild, "Guild Kick",
						JOptionPane.QUESTION_MESSAGE, null, ks, ks[0]);
				for ( int i = 0; i < ks.length;i++)
				{
					if(k.equals(ks[i]))
					{
						g.kickMember(kss[i]);
					}
				}
			}
		}
	}

	public void button_open_pressed() {
		for (Bot b : TUM._this.botovp.getSelectedBots()) {
			new ViewBot(b);
		}
	}

	public void button_fund_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();

		String d = massFund.getText();

		TUM.pp.forEach("Setting Funded Deck", bs, (b, j) -> {
			return b.getName() + (b.setFundedDeck(d, Type.BOTH) ? "success" : "error");
		});
	}

	// TODO register as process
	public void button_create_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		GUI.LoadBar lb = new LoadBar("Mass creating cards", bs.length);
		String cards = massFuse.getText();
		Card[] arr_cards = GlobalData.constructCardArray(cards);
		if (!cards.equals("")) {
			for (int i = 0; i < bs.length; i++) {
				bs[i].consumeShards();
				lb.setProgress(i, bs[i].toString());
				for (Card cr : arr_cards) {
					boolean ret = bs[i].createCard(cr);
					if (ret)
						lb.setProgress(i, bs[i] + "->" + cr.getName());
				}
				bs[i].updateData();
				if (lb.isCanceled())
					break;
			}
		}
		lb.close();
	}
	// TODO register as process
	public void button_create_to_pressed() {
			Bot[] bs = TUM._this.botovp.getSelectedBots();
			GUI.LoadBar lb = new LoadBar("Mass creating cards to", bs.length);
			String cards = massFuseTo.getText();
			Card[] arr_cards = GlobalData.constructCardArray(cards);
			if (!cards.equals("")) {
				for (int i = 0; i < bs.length; i++) {
					bs[i].consumeShards();
					lb.setProgress(i, bs[i].toString());
					for (Card cr : arr_cards) {
						//need more?
						if(GlobalData.getCount(arr_cards, cr)>GlobalData.getCount(bs[i].getInventory(), cr.getHighestID()))
						{
							boolean ret = bs[i].createCard(cr);
							if (ret)
								lb.setProgress(i, bs[i] + "->" + cr.getName());
						}
					}
					bs[i].updateData();
					if (lb.isCanceled())
						break;
				}
			}
			lb.close();
		}

	public void button_salvage_cr_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();

		TUM.pp.forEach("Salvaging Common and Rare cards", bs, (b, j) -> {
			b.salvageCommonRare();
			return b.toString();
		});
	}

	public void button_salvage_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		String cards = massSalvage.getText();

		if (!cards.equals("")) {
			TUM.pp.forEach("Mass salvaging cards", bs, (b, j) -> {
				for (Card c : GlobalData.constructCardArray(cards)) {

					if (c != null) {
						b.salvageAll(c.getLowestID());
					}
				}
				return b.getName();
			});
		}
	}

	public void button_salvage_to_pressed() {
		Bot[] bs = TUM._this.botovp.getSelectedBots();
		String cards = massSalvageTo.getText();

		if (!cards.equals("")) {
			TUM.pp.forEach("Mass salvaging cards", bs, (b, j) -> {
				Card[] cs = GlobalData.constructCardArray(cards);
				for (Card c : cs) {

					if (c != null) {
						b.salvageAllTo(c.getLowestID(), GlobalData.getCount(cs, c));
					}
				}
				return b.getName();
			});
		}
	}

}
