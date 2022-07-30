package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Deck;
import de.neuwirthinformatik.Alexander.TU.Basic.Deck.Type;
import de.neuwirthinformatik.Alexander.TU.Basic.Mission;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.CreatorV2.CreatorV2Return;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.GameSaver;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.JediDeckGrab;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.LSE;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.util.Curl;

public class Bot {
	Curl curl;
	CreatorV2 creator;
	String name;
	String cookie;
	String guild = "";
	int guild_id;
	int leader_id;
	int guild_role;
	int[] guild_member_ids;
	String[] guild_member_names;

	int user_id;
	int energy;
	int stamina;

	int wb;
	int money;
	int salvage;
	int daily_bonus;

	int off_deck_id;
	int def_deck_id;

	int num_cards;
	int max_salvage;
	int max_cards;

	int[] off_deck;
	int[] def_deck;
	public int[] inv;
	int[] restore;
	public Bot() {name ="NULL";}
	public Bot(String name) {
		this(name,TUM.settings.cookies_folder()+"/");
		/*this.name = name;
		cookie = Data.getCookie(name);
		String[] c = cookie.split(";");
		for (int i = 0; i < c.length; i++) {
			String[] t = c[i].split("=");
			if (t[0].equalsIgnoreCase("user_id"))
				user_id = Integer.parseInt(t[1]);
		}
		// System.out.println(user_id);
		// System.out.println(cookie);

		curl = new Curl(cookie);
		creator = new CreatorV2(this);
		update();*/
	}

	public Bot(String name, String f) {
		this.name = name;
		cookie = GlobalBotData.getCookie(name, f);
		String[] c = cookie.split(";");
		for (int i = 0; i < c.length; i++) {
			String[] t = c[i].split("=");
			if (t[0].equalsIgnoreCase("user_id"))
				user_id = Integer.parseInt(t[1]);
		}
		// System.out.println(user_id);
		// System.out.println(cookie);

		curl = new Curl(cookie);
		creator = new CreatorV2(this);
		update();
		initGuildMemberDefDecks();
	}
	
	public void initGuildMemberDefDecks()
	{
		if(guild_member_ids == null)return;
		for(int i =0; i < guild_member_ids.length;++i)
		{
			if(TUM.settings.PULL_GUILD)if(!JediDeckGrab.manualAddedPlayerLocal(guild_member_names[i],TUM.start_time))JediDeckGrab.manualAddPlayerLocal(guild_member_names[i], getGuild(), getGuildMemberDeck(guild_member_ids[i], true),TUM.start_time); //pull defs
			//System.out.println("check");
		}
	}

	public void update() {
		updateAll();
		// updateData();
		// updateGuild();
		// updateDeck();
		// updateRestoreAndInventory();
		// updateInv();
		// updateRestore();
	}

	public void updateAll() {
		Object[] all;
		try {
			all = curl.getDataGuildRestoreInventoryOffDef();
			updateData((int[]) all[0]);
			updateGuild((Object[]) all[1]);
			updateRestoreAndInventory(new int[][]{(int[]) all[2],(int[]) all[3]});
			updateDeck((int[]) all[4],(int[]) all[5]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateGuild(Object[] guild_data) {
		if (guild_data == null)
			guild_data = new Object[] { "", 0, 0, 0 };
		guild = (String) guild_data[0];
		guild_id = (int) guild_data[1];
		leader_id = (int) guild_data[2];
		guild_role = (int) guild_data[3];
		guild_member_ids  = (int[]) guild_data[4];
		guild_member_names = (String[]) guild_data[5];
	}

	public void updateInv(int[] inv) {
		this.inv = inv;
		TUMTUO.updateOwnedCards(this, inv, restore);
	}
	
	public void updateRestore(int[] r)
	{
		TUM.log.m("Updating Restore", getName());
		restore = r;
		/*ArrayList<Integer> rests = new ArrayList<Integer>(); 
		for(int id : r)
		{
			for(int a : Data.getIDsFromCardInstances((new CardInstance(id)).getLowestMaterials().toArray(new CardInstance[]{})))rests.add(a);
		}
		restore_singles = rests.stream().mapToInt(i -> i).toArray();*/
	}
	

	public void updateRestoreAndInventory(int[][] restoreinv) {
		TUM.log.m("Updating Restore And Inventory", getName());
		updateRestore(restoreinv[0]);
		updateInv(restoreinv[1]);
		//restore = restoreinv[0];
		//inv = restoreinv[1];
		//TUO.updateOwnedCards(this, inv, restore);
	}
	

	public void updateRestoreInventoryData(int[][] data){
		updateRestoreAndInventory(data);
		updateData(data[2]);
	}

	public void updateDeck(int[] off, int[] def) {
		off_deck = off;
		def_deck = def;
		TUMTUO.updateGauntlet(this, def_deck);
	}

	public void updateData(int[] ms) {

		money = ms[0];
		salvage = ms[1];
		energy = ms[2];
		stamina = ms[3];
		daily_bonus = ms[4];
		off_deck_id = ms[5];
		def_deck_id = ms[6];
		num_cards = ms[7];
		max_salvage = ms[8];
		max_cards = ms[9];
		wb = ms[10];
	}

	public void leaveGuild() {
		try {
			getCurl().leaveFaction();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object[][] getFactionInvites() {
		try {
			return getCurl().getFactionInvites();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void acceptGuildInvite(int id) {
		try {
			getCurl().acceptFactionInvite(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendGuildInvite(int id) {
		try {
			getCurl().sendFactionInvite(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendGuildMessage(String msg) {
		try {
			getCurl().sendFactionMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void kickMember(int id) {
		try {
			getCurl().kickFactionMember(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void salvageCommonRare() {
		salvageCommonRare(false);
	}
	public void salvageCommonRare(boolean unlock) {
		synchronized (getCreator()) {
			TUM.log.m("Salvaging Common and Rare manual", "Salvage", getName());
			for (int id : GlobalData.removeDuplicates(inv)) {
				Card c = GlobalData.getCardByID(id);
				if (c != null && ((GlobalData.getSalvageValue(c) == 1 || GlobalData.getSalvageValue(c) == 5) && c.type != CardType.COMMANDER)) {
					salvageAll(c.getLowestID(),unlock);
				}
			}
		}
	}

	public void salvageCommonRareDirect() {
		synchronized (getCreator()) {
			TUM.log.m("Salvaging Common and Rare by Api", "Salvage", getName());
			try {
				int[][] r = getCurl().salvageCommonRareInvData();
				updateInv(r[0]);
				updateData(r[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void unlockAll()
	{
		for(int id:GlobalData.removeDuplicates(getInventory()))
		{
			try {
				getCurl().setCardLock(id, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void salvageAll(int id) {
		 salvageAll(id,true);
	}
	public void salvageAll(int id,boolean set_unlock) {
		salvageAllTo(id,0,set_unlock);
	}
	public void salvageAllTo(int id, int to) {
		 salvageAllTo(id,to,true);
	}
	public void salvageAllTo(int id,int to,boolean set_unlock) {
		synchronized (getCreator()) {
			int sp_gain = GlobalData.getSalvageValue(id);
			int num = GlobalData.getCount(inv, id);
			num -= to;
			if (sp_gain * num + salvage > max_salvage)
				return;
			if (num > 0)
				TUM.log.m("Salvaging: " + GlobalData.getCardByID(id).getName() + "#" + num + " == " + sp_gain * num + " SP",
						"Salvage", getName());
			String resp = null;
			for (int i = 0; i < num; i++) {
				try {
					if(set_unlock)getCurl().setCardLock(id, false);
					resp = getCurl().salvageCard(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(resp != null)
			{
				updateData(curl.getData(resp));
				updateInv(curl.getInventory(resp));
			}
		}
	}

	public TUMTUO.Param getOffParamWithDom(int itr) {
		return getOffParamWithDom(TUM.settings.enemy_deck(), itr);
	}

	//AH FIX, Fake DOM => YF
	public TUMTUO.Param getOffParamWithDom(String d, int iter) {
		Param p = new Param(GlobalData.getDeckString(getSimOffDeck()), d, iter);
		if (GlobalData.isDominion(off_deck[1]))
			p.deck += ", " + GlobalData.getNameAndLevelByID(off_deck[1]);
		else if (off_deck[0] != off_deck[1] && off_deck[1] != 0) {
			p.yf += ", " + GlobalData.getNameAndLevelByID(off_deck[1]);
			p.dom = Dom.dom_none;
		}
		return p;
	}

	public TUMTUO.Param getDefParamWithDom() {
		return getDefParamWithDom(0);
	}

	public TUMTUO.Param getDefParamWithDom(int itr) {
		return getDefParamWithDom(TUM.settings.enemy_deck(), itr);
	}

	//TODO NEEDS HEAVY TESTING
	public TUMTUO.Param getDefParamWithDom(String d, int iter) {
		Param p = new Param(GlobalData.getDeckString(getSimDefDeck()), d, iter);
		if (GlobalData.isDominion(def_deck[1]))
			p.deck += ", " + GlobalData.getNameAndLevelByID(def_deck[1]);
		else if (def_deck[0] != def_deck[1])
			p.yf += ", " + GlobalData.getNameAndLevelByID(def_deck[1]);
		return p;
	}

	public TUMTUO.Result simOffDeck() {
		Param p = getOffParamWithDom(TUM.settings.sim_iter());
		return TUMTUO.sim(this, p);
	}

	public TUMTUO.Result simDefDeck() {
		Param p = getDefParamWithDom(TUM.settings.sim_iter());
		return TUMTUO.sim(this, p);
	}

	public String reorderDeck() {
		Param p = getOffParamWithDom(TUM.settings.reorder_iter());

		p.order = Order.ordered;
		String r = TUMTUO.reorder(this, p);
		return r;
	}

	public String climbDeck() {
		return climbDeck(TUM.settings.order());
	}

	public String climbDeck(Order order) {
		updateInv();
		//Param p = new Param(Data.getDeckString(getSimOffDeck()), TUM.settings.enemy_deck(), TUM.settings.climb_iter());
		Param p = getOffParamWithDom(TUM.settings.sim_iter());
		p.iterations= TUM.settings.climb_iter();	
		p.order = order;
		String r = TUMTUO.climb(this, p);
		return r;
	}

	public String fundClimbDeck() {
		return fundClimbDeck(TUM.settings.order());
	}

	public String fundClimbDeck(Order order) {
		updateInv();
		//Param p = new Param(Data.getDeckString(getSimOffDeck()), TUM.settings.enemy_deck(), TUM.settings.climb_iter());
		Param p = getOffParamWithDom(TUM.settings.sim_iter());
		p.iterations= TUM.settings.climb_iter();	
		p.fund = getFund();
		p.order = order;
		String r = TUMTUO.climb(this, p);
		return r;
	}

	public String climbAndSetDeck(Order order,Type type) {
		String r = climbDeck(order);

		// print("Climbed DECK: " + r);
		//setCurlDecks(type.off()?r:Data.getDeckString(getOffDeck()), type.def()?r:Data.getDeckString(getDefDeck()), off_deck_id, def_deck_id);
		setCurlDeck(r,type);
		
		updateDeck();
		return r;
	}

	public String climbAndSetDeck(Type type) {
		return climbAndSetDeck(TUM.settings.order(),type);
	}
	
	
	public synchronized void setCurlDeck(String deck, Type type) {
		setCurlDecks(type.off()?deck:GlobalData.getDeckString(getOffDeck()), type.def()?deck:GlobalData.getDeckString(getDefDeck()), off_deck_id, def_deck_id);

	}
	public synchronized void setCurlDecks(String off_deck, String def_deck, int off_id, int def_id) {
		try {

			getCurl().setDecksCards(GlobalData.constructDeck(off_deck), GlobalData.constructDeck(def_deck), off_id, def_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void setCurlDecks(int[] off_deck, int[] def_deck, int off_id, int def_id) {
		try {
			getCurl().setDecksCards(GlobalData.constructDeck(off_deck), GlobalData.constructDeck(def_deck), off_id, def_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public boolean couldCreateCard(Card c) {
		synchronized (getCreator()) {
			return getCreator().couldCreateCard(c, Data.getCardArrayFromIDArray(inv), true, inv);
		}
	}

	public int couldCreateCardCosts(Card c) {
		synchronized (getCreator()) {
			return getCreator().couldCreateCardCosts(c, Data.getCardArrayFromIDArray(inv), true, inv);
		}
	}*/

	public CreatorV2Return couldCreateCard(Card c) {
		synchronized (getCreator()) {
			return getCreator().couldCreateCard(c, inv,restore);
		}
	}
	private void setLockCards(CardInstance[] ids, boolean lock)
	{
		TUM.log.d("Locking  " +  Arrays.stream(ids).distinct().map(i -> i.toString()).collect(Collectors.joining(", ")) + " to " + lock, getName(), "LOCK");
		for(int i : GlobalData.removeDuplicates(GlobalData.getIDsFromCardInstances(ids)))setLockCard(i,lock);
	}
	private void setLockCard(int id, boolean lock)
	{
		try {
			getCurl().setCardLock(id, lock);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean createCard(Card c) {
		synchronized (getCreator()) {
			if (c != null) {
				
				setLockCards((GlobalData.getCardInstanceById (c.getLowestID())).getLowestMaterials().toArray(new CardInstance[]{}),true);
				
				updateRestoreInventoryData();

				int num = GlobalData.getCount(getInventory(), c.getHighestID());
				CreatorV2Return cr;
				int tmp_sp = 0;
				int i = 0; //TODO Fix Commander fusion bug, with too low sp => after that remove i < 20 break hotfix
				while ((cr = couldCreateCard(c)).possible && i < 20 && cr.sp_cost <= getFund() && ((tmp_sp = getSalvage()) >= 1925|| tmp_sp >= cr.sp_cost || (getMoney() >= 2000 && (tmp_sp = buy()) >= 1925 || tmp_sp >= cr.sp_cost))) {
					TUM.log.m(i + ". Fusing " + c.getName() + " costs " + cr.sp_cost + " current sp: " + tmp_sp + " fund: "
							+ getFund(), "Create", getName());
					getCreator().createCard(c, inv,restore);
					updateRestoreInventoryData();
					
					i++;
					if (num < GlobalData.getCount(getInventory(), c.getHighestID())) {
						TUM.log.m("Created " + c.getName(), "Create", getName());
						setLockCards((GlobalData.getCardInstanceById (c.getLowestID())).getLowestMaterials().toArray(new CardInstance[]{}),false);
						return true;
					}
				}
				TUM.log.e("Failed to create " + c.getName() +" Missing: "+ cr, "Create", getName());
				setLockCards((GlobalData.getCardInstanceById (c.getLowestID())).getLowestMaterials().toArray(new CardInstance[]{}),false);
			}
			TUM.log.e("Card is null -> can't be created","Create");
			return false;
		}
	}

	

	public String createFundedDeck(Order order, Type type) {
		String sdeck = fundClimbDeck(order);
		if (!setFundedDeck(sdeck,type)) {
			sdeck = climbAndSetDeck(type);
			updateDeck();
		}
		return sdeck;
	}

	public String createFundedDeck(Type type) {
		return createFundedDeck(TUM.settings.order(),type);
	}

	

	public boolean setFundedDeck(String sdeck, Type type) {
		synchronized (getCreator()) {
			TUM.log.m("Set Funded Deck: " + sdeck, getName(), "Deck");
			Deck deck = GlobalData.constructDeck(sdeck);
			int[] ideck = deck.toIDArray();
			updateInv();
			/*
			 * TODO Lock every card from sdeck with: setLockCards((new CardInstance (c.getLowestID())).getLowestMaterials().toArray(new CardInstance[]{}),true);
			 * TODO Unlock every card from sdeck afterwards
			 */
			/*
			 * if(!checkFundedDeck(ideck)) {TUM.log.e("Unable to make Deck: " +
			 * sdeck, getName(), "Deck"); return false;}
			 */
			boolean errors = false;
			for (int i = 0; i < ideck.length; i++) {
				int n_in_deck = GlobalData.getCount(ideck, ideck[i]);
				int n_in_inv = GlobalData.getCount(getInventory(), ideck[i]);
				if (n_in_deck > n_in_inv) {
					for (int j = 0; j < n_in_deck - n_in_inv; j++) {
						boolean suc = createCard(GlobalData.getCardByID(ideck[i]));
						errors |= !suc;
					}
				}
			}
			if (errors)
				return false;
			else {
				setCurlDeck(sdeck, type);
				updateDeck();
				return true;
			}
		}
	}


	public void buy20Pack() {
		synchronized (getCreator()) {
			try {
				getCurl().buy20Pack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int buy() {
		synchronized (getCreator()) {
			salvageCommonRare(false);
			//updateInv();
			//updateData();
			int num_buys = 0;
			while ((num_buys = (getMaxCards() - getNumCards()) / 20) > 0
					&& (num_buys = (getMaxSalvage() - getSalvage() - 45) / 30 > num_buys ? num_buys
							: (getMaxSalvage() - getSalvage() - 45) / 30) > 0
					&& (num_buys = getMoney() / 2000 > num_buys ? num_buys : getMoney() / 2000) > 0) {
				for (int i = 0; i < num_buys; i++)
					buy20Pack();
				int old_sp = getSalvage();
				int old_money = getMoney();
				salvageCommonRareDirect();
				//updateInv();
				//updateData();
				TUM.log.m((old_money - getMoney()) / 2000 + " of " + num_buys + " packs " + ", SP " + old_sp + " -> "
						+ getSalvage() + " |Diff:" + (getSalvage() - old_sp) + "| /" + getMaxSalvage() + ", Cards "
						+ getNumCards() + "/" + getMaxCards() + ", Gold " + old_money + " -> " + getMoney(), "Buy",
						getName());
			}
			return getSalvage();
		}

	}

	public int consumeShards() {
		synchronized (getCreator()) {
			try {
				getCurl().consumeAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateData();
			return getSalvage();
		}
	}

	/**
	 * Fighterrei
	 */

	public synchronized void setAutopilot(boolean b) {
		try {
			getCurl().setUserFlag("autopilot", b ? 1 : 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void playCard(int id) {
		try {
			getCurl().playCard(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void winFightOrdered(String mission_raid_camp_name, Mode mode) {
		setAutopilot(false);// TODO REMOVE
		LSE lse = new LSE(this, mission_raid_camp_name == null ? false : true, mode);

		lse.update();

		if (mission_raid_camp_name == null) {
			String edeck = GlobalData.getDeckString(JediDeckGrab.getDeck(lse.enemy_name));
			lse.enemy_deck = edeck;
		} else {
			lse.enemy_deck = mission_raid_camp_name;
		}
		int turn=0;
		while (lse.winner == -1) {
			
			if(turn>=TUM.settings.lse_turns())
			{
				TUM.log.m("LSE turn limit -> Auto-Random", "LSE", getName());
				winFightRandom();
			}		
			else
			{
				int[] real_id = lse.get_first_ids(lse.sim());
				if (GlobalData.getNameAndLevelByID(real_id[0]).equals(Card.NULL.name)) { //TODO check condition
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
					TUM.log.e("!!!!!!!!!!!! Possible interuption or deck changed -> AUTO", "LSE", getName());
	
					winFightRandom();
				} else {
					playCard(real_id[1]);
					turn++;
				}
			}

			lse.update();
		}
	}

	public synchronized void saveJSONDeck() {
		saveJSONDeck("Battle");
	}

	public synchronized void saveJSONDeck(String mode) {
		/*try {
			if (!TUM.settings.save_decks_to_jedi_db())
				return;
			LSE lse = new LSE(this, false, null);
			lse.update();
			if (lse.winner == -1) {
				TUM.log.w("Fight is not done yet!!!", "JediDB", getName());
				return;
			}
			int[] edeck = lse.cur_enemy_field_ids;
			TUM.log.m("Enemy deck cards (" + (edeck.length - 2) + "/" + lse.enemy_size + ")", "JediDB",	getName());
			if (edeck.length - 2 < lse.enemy_size/2) { //Save half deck already
				return;
			}

			String eguild = JediDeckGrab.getGuildName(lse.enemy_name);
			if (eguild == null) {
				TUM.log.w("Enemy Guild unknown!!!", "JediDB", getName());
				return;
			}

			JSONObject obj = new JSONObject();

			obj.put("name", lse.enemy_name);
			obj.put("guild", eguild);
			JSONObject tmp_deck = new JSONObject();
			JSONArray cards = new JSONArray();
			for (int i = 1; i < edeck.length; i++) {
				if(Data.isFortress(edeck[i]))continue;
				JSONObject tmp = new JSONObject();
				tmp.put("name", Data.getNameAndLevelByID(edeck[i]));
				tmp.put("card_id", edeck[i]);
				cards.put(tmp);
			}
			tmp_deck.put("cards", cards);
			JSONObject tmp = new JSONObject();
			tmp.put("name", Data.getNameAndLevelByID(edeck[0]));
			tmp.put("card_id", edeck[0]);
			tmp_deck.put("commander", tmp);
			obj.put("deck", tmp_deck);
			obj.put("mode", "Defense");
			obj.put("type", "Battle");
			
			obj.put("bge", new JSONObject(" { \"enemy\": {\"enemy_id\": \"" + (lse.ebge_id == -1 ? "None" : lse.ebge_id)
					+ "\",\"name\": \"" + (lse.ebge.equals("") ? "None" : lse.ebge) + "\"},\"global\": {  \"name\": \""
					+ (lse.bge.equals("") ? "None" : lse.bge) + "\", \"global_id\": \""
					+ (lse.bge_id == -1 ? "None" : lse.bge_id) + "\"   }, \"friendly\": {  \"friendly_id\": \""
					+ (lse.ybge_id == -1 ? "None" : lse.ybge_id) + "\",\"name\": \""
					+ (lse.ybge.equals("") ? "None" : lse.ybge) + "\" }}"));
			obj.put("friendly_structures", "");
			obj.put("enemy_structures", "");
			obj.put("date",
					new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'0000Z'").format(Calendar.getInstance().getTime()));

			JediDeckGrab.pushDeckJSON(eguild, "Battle", (lse.bge.equals("") ? "None" : lse.bge), obj.toString());
			if (!mode.equals("Battle"))
				JediDeckGrab.pushDeckJSON(eguild, mode, (lse.bge.equals("") ? "None" : lse.bge), obj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			LSE lse = new LSE(this, false, null);
			lse.update();
			if (lse.winner == -1) {
				TUM.log.w("Fight is not done yet!!!", "DeckDB", getName());
				return;
			}
			int[] edeck = lse.cur_enemy_field_ids;
			TUM.log.m("Enemy deck cards (" + (edeck.length - 2) + "/" + lse.enemy_size + ")", "JediDB",	getName());
			String[] vd = getCurl().getVersionAndDate();
			
			GameSaver.pushDeck(lse.is_surge,mode,GlobalData.getNameAndLevelByID(getSimOffDeck()[0]),lse.cur_own_field,lse.cur_own_fort,lse.cur_enemy_field,lse.bge,lse.ebge, lse.ybge,lse.winner,vd[0],vd[1]);
			
			if (TUM.settings.save_decks_to_jedi_db() && !(edeck.length - 2 < lse.enemy_size/2))//Save half deck already
			{
				String eguild = JediDeckGrab.getGuildName(lse.enemy_name);
				if (eguild == null) {
					TUM.log.w("Enemy Guild unknown!!!", "JediDB", getName());
					return;
				}
				JediDeckGrab.pushDeck(eguild, lse.enemy_name, edeck, mode, lse.bge_id, lse.bge, lse.ebge_id, lse.ebge, lse.ybge_id, lse.ybge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized void winFightRandom() {
		try {
			getCurl().autoSkipFight();
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void winFight(Mode mode, String mcr_name) {
		if (TUM.settings.use_lse())
			winFightOrdered(mcr_name, mode);
		else {
			winFightRandom();
		}
	}

	public synchronized void checkPreFight() {
		boolean ag = true;
		try {
			ag = getCurl().isGameActive();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ag) {
			TUM.log.e("!!!!!!!!!!!! Still in an active Game -> AUTO", "LSE", getName());
			TUM.log.e("!!!!!!!!!!!! Still in an active Game -> AUTO", "LSE", getName());
			TUM.log.e("!!!!!!!!!!!! Still in an active Game -> AUTO", "LSE", getName());
			TUM.log.e("!!!!!!!!!!!! Still in an active Game -> AUTO", "LSE", getName());
			TUM.log.e("!!!!!!!!!!!! Still in an active Game -> AUTO", "LSE", getName());

			winFightRandom();
		}

	}

	public synchronized void fightPracticeBattle(int target, boolean surge, boolean vs_off) {	fightPracticeBattle(target,surge,vs_off,TUM.settings.use_lse());}
	public synchronized void fightPracticeBattle(int target, boolean surge, boolean vs_off, boolean lse) {
		try {
			checkPreFight();
			getCurl().fightPracticeBattle(target,surge,vs_off);
			if(lse)winFight(surge?Mode.surge:Mode.pvp, null);
			else winFightRandom();
			saveJSONDeck("Battle"); //no json because guild privacy
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightBrawlBattle() {
		try {
			checkPreFight();
			getCurl().fightBrawlBattle();
			winFight(Mode.brawl, null);
			saveJSONDeck("Brawl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightWarBattle(int slot) {
		try {
			checkPreFight();
			getCurl().fightWarBattle(slot);
			winFight(Mode.gw, null);
			saveJSONDeck("War");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void fightConquestBattle(int zone) {
		try {
			checkPreFight();
			getCurl().fightConquestBattle(zone);
			winFight(Mode.surge, null);
			saveJSONDeck("Conquest");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightRaidBattle() {
		int[] raid = getRaidData();
		fightRaidBattle(raid[2], raid[3]);
	}

	public synchronized void fightRaidBattle(int raid_id, int raid_level) {
		try {
			checkPreFight();
			getCurl().fightRaidBattle(raid_id, raid_level);
			winFight(Mode.raid, "Raid#" + raid_id + "-" + raid_level);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightGuildQuest(int quest) {
		try {
			checkPreFight();
			getCurl().fightFactionQuest(quest);
			winFightRandom();
			//winFight(Mode.pvp, ""); // GQ NAME HERE //TODO LSE
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightMission(Mission m, int level) {
		fightMission(m.getID(), level);
	}

	public synchronized void fightMission(int mission_id, int level) {
		try {
			checkPreFight();
			getCurl().startMission(mission_id);
			winFight(Mode.pvp, "Mission#" + mission_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[][] getCampaignDeck() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<Integer> reserve_ids = new ArrayList<Integer>();
		int added = 0;
		for (int i : off_deck) {
			if(ids.contains(i))continue;
			//ids.add(i);
			int spare = GlobalData.getCount(inv, i) - GlobalData.getCount(off_deck, i);
			for(int j =0;j <GlobalData.getCount(off_deck, i);j++ )
			{
				ids.add(i);
			}
			while (added < 5 && spare > 0) { //5 num reserves
				reserve_ids.add(i);
				added++;
				spare--;
			}
		}
		int[][] ret = new int[2][];
		ret[0] = new int[ids.size()];
		for (int j = 0; j < ids.size(); j++) {
			ret[0][j] = ids.get(j);
		}
		ret[1] = new int[reserve_ids.size()];
		for (int j = 0; j < reserve_ids.size(); j++) {
			ret[1][j] = reserve_ids.get(j);
		}
		return ret;
	}

	public synchronized void dumpCampaign(int diff) {
		dumpCampaign(getCampaignData()[0],diff);
	}
	
	public synchronized void dumpCampaign(int id, int diff) {
		try {
			checkPreFight();
			int[][] camp_deck = getCampaignDeck();
			TUM.log.m("Camp Deck: " + GlobalData.constructDeck(camp_deck[0]).toString());
			getCurl().startCampaign(GlobalData.constructDeck(camp_deck[0]).getOffenseDeck(),camp_deck[1], id, diff);
			for (int i = 0; i < 7; i++) {
				getCurl().fightCampaignBattle(id);
				// winFight();

				Thread.sleep(1000);
				winFightRandom();
				//winFight(Mode.campaign, "");// CAMP NAME //TODO LSE CAMPAIGN
				// getCurl().autoSkipFight();

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void fightPVPBattle() {
		try {
			checkPreFight();
			getCurl().startHuntingBattle((int) getCurl().getHuntingTargets()[0][0]);
			winFight(Mode.pvp, null);
			saveJSONDeck("Battle");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void dumpStamina() {
		while (stamina > 0) {
			try {
				checkPreFight();
				getCurl().startHuntingBattle((int) getCurl().getHuntingTargets()[0][0]);
				winFight(Mode.pvp, null);
				saveJSONDeck("Battle");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateData();
		}
	}

	public void claimDailyReward() {
		try {
			getCurl().useDailyBonus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[] getRaidData() {
		try {
			return getCurl().getRaidData(getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int[][] getMissionData() {
		int[][] data;
		try {
			data = getCurl().getMissionData();
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int[] getMissionData(int id) {
		int[][] data;
		try {
			data = getCurl().getMissionData();
			for (int i = 0; i < data.length; i++) {
				if (data[i][0] == id)
					return data[i];
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int[] getMissionData(Mission m) {
		return getMissionData(m.getID());
	}

	public Object[][] getBrawlLeaderBoard() {
		try {
			return getCurl().getBrawlLeaderboard(user_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object[][] getRaidLeaderBoard(int raid_id) {
		try {
			return getCurl().getRaidLeaderboard(getUserID(), raid_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getWarLeaderBoard() {
		try {
			return getCurl().getWarLeaderboard(getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object[][] getRaidLeaderBoard() {
		try {
			return getCurl().getRaidLeaderboard(getUserID(), getRaidData()[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object[][] getRaidMembersLeaderBoard() {
		try {
			return getCurl().getRaidMembersLeaderBoard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object[][] getBrawlMembersLeaderBoard() {
		try {
			return getCurl().getBrawlMembersLeaderBoard(user_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	public int[] getCampaignData()
	{
		try {
			return getCurl().getCampaignData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestMembersLeaderboard() {
		try {
			return getCurl().getConquestMembersLeaderboard(getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestMembersLeaderBoardZone(int zone_id) {
		try {
			return getCurl().getConquestMembersLeaderboardZone(user_id,zone_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestLeaderboard() {
		try {
			return getCurl().getConquestLeaderboard(getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestLeaderboardZone(int zone_id) {
		try {
			return getCurl().getConquestLeaderboardZone(getUserID(), zone_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestLiveZone(int zone_id) {
		try {
			return getCurl().getConquestLiveZone(getUserID(), zone_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestTopLeaderboard() {
		try {
			return getCurl().getConquestTopLeaderboard(getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getConquestTopLeaderboardZone(int zone_id) {
		try {
			return getCurl().getConquestTopLeaderboardZone(getUserID(),zone_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getConquestData() {
		try {
			return getCurl().getCQData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getBrawlData() {
		try {
			return getCurl().getBrawlData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object[][] getWarOwnMembersLeaderBoard() {
		try {
			return getCurl().getWarOwnMembersLeaderboard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object[][] getWarEnemyMembersLeaderBoard() {
		try {
			return getCurl().getWarEnemyLeaderboard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getWarData() {
		try {
			return getCurl().getWarData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] getBattleData() {
		try {
			return getCurl().getBattleData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int[] getGuildQuestData() {
		try {
			int[][] data = getCurl().getGuildQuestData();
			return GlobalData.concat(data[0], GlobalData.concat(data[1], data[2]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void claimConquestRewards() {
		try {
			getCurl().claimConquestReward();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void claimRaidRewards() {
		claimRaidRewards(getRaidData()[2]);
	}

	public void claimRaidRewards(int raid_id) {
		try {
			getCurl().claimRaidReward(raid_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void claimBrawlRewards() {
		try {
			getCurl().claimBrawlRewards();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void claimWarRewards() {
		try {
			getCurl().claimFactionWarRewards();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			getCurl().getUserAccount();
			getCurl().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setActiveDeckSlot(int id)
	{
		try {
			getCurl().setActiveDeck(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDefenseDeckSlot(int id)
	{
		try {
			getCurl().setDefenseDeck(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCookie() {
		return cookie;
	}

	public String getName() {
		return name;
	}

	public boolean isLeader() {
		return guild_role == 31;
		// return (leader_id==0||user_id==0) ? false : leader_id==user_id;
	}

	public boolean isOfficer() {
		return guild_role == 21;
		// return (leader_id==0||user_id==0) ? false : leader_id==user_id;
	}

	public int getUserID() {
		return user_id;
	}

	public String getGuild() {
		return guild;
	}

	public int getGuildID() {
		return guild_id;
	}
	
	public int[] getGuildMemberIDs() {
		return guild_member_ids;
	}
	
	public String[] getGuildMemberNames() {
		return guild_member_names;
	}
	
	public int[] getGuildMemberDeck(int member, boolean defense) {
		try {
			return getCurl().getGuildMemberDeck(member, defense);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getMoney() {
		return money;
	}

	public int getWB() {
		return wb;
	}

	public int getSalvage() {
		return salvage;
	}

	public int getEnergy() {
		return energy;
	}

	public int getStamina() {
		return stamina;
	}

	public int getDailyBonus() {
		return daily_bonus;
	}

	public int getFund() {
		return (int) (getSalvage() + getMoney() * 1.3 / 100); // TODO Adjust
																// this param
																// 1.5 was too
																// high
	}

	public int[] getSimOffDeck() {
		int[] n = off_deck.clone();
		n[1] = 0;
		return n;
	}
	
	public int[] getSimDefDeck() {
		int[] n = def_deck.clone();
		n[1] = 0;
		return n;
	}

	public int[] getOffDeck() {
		return off_deck;
	}

	public int[] getDefDeck() {
		return def_deck;
	}

	public int getOffDeckID() {
		return off_deck_id;
	}

	public int getDefDeckID() {
		return def_deck_id;
	}

	public int[] getInventory() {
		return inv;
	}

	public int[] getRestore() {
		return restore;
	}

	public Curl getCurl() {
		return curl;
	}

	public CreatorV2 getCreator() {
		return creator;
	}

	public String toString() {
		String gn = guild.replaceAll("[a-z_0-9]", "") ;
		if(gn.length() > 3)gn = gn.substring(0, 3);
		return " [" + gn + "]" + name; //+ ;
	}

	/*
	 * public void print(String msg) { System.out.println("[" + getName() +
	 * "]: " + msg); }
	 */

	public int getMaxCards() {

		return max_cards;
	}

	public int getNumCards() {

		int ret = inv.length - GlobalData.getCount(inv, GlobalData.getIDByNameAndLevel("Dominion Shard-2"));
		for(int id : inv)
		{
			if(GlobalData.isCommander(id))
			{
				ret--;
			}
		}
		return ret;
	}

	public int getMaxSalvage() {
		// TODO Auto-generated method stub
		return max_salvage;
	}

	// Curl Catches

	public void updateData() {
		try {
			updateData(curl.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateGuild() {
		try {
			updateGuild(curl.getGuild());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateInv() {
		try {
			updateInv(curl.getInventory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateRestoreInventoryData() {
		try {
			updateRestoreInventoryData(curl.getRestoreInventoryData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRestoreAndInventory() {
		try {
			updateRestoreAndInventory(curl.getRestoreAndInventory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDeck() {
		try {
			updateDeck(curl.getDeck(off_deck_id), curl.getDeck(def_deck_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
