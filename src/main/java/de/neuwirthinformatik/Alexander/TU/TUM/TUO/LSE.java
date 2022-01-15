package de.neuwirthinformatik.Alexander.TU.TUM.TUO;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUO.Result;

public class LSE {

	

	Bot b;
	// Thread auto_thread;
	// JFrame frame;
	// Container pane;

	// JTextArea enemy_name;
	// JTextArea guild_name;
	// JTextArea enemy_time;
	public int winner;
	public int enemy_size;
	public String enemy_name;
	public String deck;
	//public int[] deck_ids;
	public String enemy_deck;
	//public int[] enemy_deck_ids;
	public String hand_field;
	public int[] hand_field_ids;
	public String cur_own_field;
	public int[] cur_own_field_ids;
	public String cur_own_fort;
	public int[] cur_own_fort_ids;
	public String cur_enemy_field;
	public int[] cur_enemy_field_ids;
	
	public String bge="",ybge="",ebge="";
	public int bge_id=-1,ybge_id=-1,ebge_id=-1;
	
	
	public String param_edeck;
	public boolean is_edeck_list = true; //TODO split norm and mission/raid/camp ... => false 
	
	public boolean is_surge;
	
	public Mode mode = Mode.surge;
	
	//public HashMap<Integer,Integer> idmap= new HashMap<Integer,Integer>(); 
	//public ArrayList<Integer> played_map= new ArrayList<Integer>(); 
	// JTextField status;
	// JTextField result; // clear on update
	// JTextField playcard; // clear on update

	// JButton grab_deck;
	// JButton load_guild;
	// JToggleButton automatic;
	// JButton update_field;
	// JButton sim;
	// JButton reset;

	public LSE(Bot b, boolean is_mission_campaign_raid,Mode mode) {
		this.b = b;
		this.is_edeck_list = !is_mission_campaign_raid;
		this.mode = mode;
		// LOAD Stuff

		//loadGuilds(); //todo central rm
		b.updateData();
		b.updateDeck();
		int[] full_deck = b.getSimOffDeck();
		full_deck[1] = 0; // no dom
		deck = GlobalData.getDeckString(full_deck);
	}

	public String sim() {
		// TODO Auto-generated method stub
		int itter = TUM.settings.lse_iter();
		if(enemy_deck == null || enemy_deck.equals(""))
		{

			TUM.log.w("NO ENEMY DECK TO SIM => USING OWN DECK AS ENEMY'S", "LSE", b.getName());
			enemy_deck = deck;
		}
		/*
		 * TODO grab ids from Text lines
		 */

		int[] cdeck = GlobalData.constructDeck(deck).toIDArray();
		for (int id : cur_own_field_ids) {
			for (int i = 0; i < cdeck.length; i++) {
				if (cdeck[i] == id) {
					cdeck[i] = 0;
					break;
				}
			}
		}
		param_edeck = null;
		
		if(is_edeck_list)
		{
			int[] edeck = GlobalData.constructDeck(enemy_deck).toIDArray();
			edeck[0] = 0;
			edeck[1] = 0;
			int fort_count = 0;
			for (int id : cur_enemy_field_ids) {
				if(GlobalData.isFortress(id))fort_count++;
				for (int i = 0; i < edeck.length; i++) {
					if (edeck[i] == id) {
						edeck[i] = 0;
						break;
					}
				}
			}

			param_edeck = GlobalData.getDeckString(cur_enemy_field_ids) + GlobalData.getDeckString(edeck);
			

			edeck = GlobalData.constructDeck(param_edeck).toIDArray();
			for (int i = 10+2+fort_count; i < edeck.length; i++) {
				edeck[i]=0;
			}
			param_edeck = GlobalData.getDeckString(edeck);
		}
		else
		{
			param_edeck = enemy_deck;
		}
		
		
		
		TUM.log.m("Enemy Deck: " + param_edeck,"LSE",b.getName());
		// fix order by add instead of replace
		Param p = new Param(GlobalData.getDeckString(cur_own_field_ids) + " ," + GlobalData.getDeckString(cdeck), param_edeck, itter);
		p.mode = mode;
		p.fight_surge = is_surge?"surge":"fight";
		
		p.BGE = bge;
		p.ye = ybge;
		p.ee = ebge;
		
		p.yf = GlobalData.getDeckString(cur_own_fort_ids);
		p.ef = "";
		p.hand = hand_field;
		//System.out.println(p.hand);
		p.enemy_hand = cur_enemy_field;
		p.freeze = GlobalData.constructCardArray(cur_own_field).length;
		
		//System.out.println("FREEZE: " + p.freeze);
		p.op = OP.reorder;
		p.order = Order.ordered;
		// p.enemy_ordered = true;
		String tuo_deck = TUO.climbAny(b, p);
		/**
		 * p.deck = tuo_deck; String cur_score = String.valueOf(TUO.sim(b,
		 * p).WINS); p.order = "random"; String auto_score_1 =
		 * String.valueOf(TUO.sim(b, p).WINS); p.hand = cur_own_field; String
		 * auto_score_2 = String.valueOf(TUO.sim(b, p).WINS);
		 **/ // Only graphics
		TUM.log.d("SIMMED: " + tuo_deck,"LSE",b.getName());
		

		p.deck = tuo_deck;
		if(TUM.settings.lse_out()) {
		Result r = TUO.sim(b, p);
		String cur_win = String.valueOf(r.WINS);
		String cur_score = String.valueOf(r.SCORE);
		TUM.log.m("WINS: " + cur_win + " (SCORE: " + cur_score + ")","LSE",b.getName());
		}
		return tuo_deck;
		
	}

	public int get_first(String tuo_deck) {
		// get first card currently in hand

		return get_first_ids(tuo_deck)[0];
	}
	/**
	 * Each card from the deck has a separate id to its card-id. This gets the card to be played from the sim() return
	 * @param tuo_deck
	 * @return
	 */
	public int[] get_first_ids(String tuo_deck) {
		// get first card currently in hand

		boolean changed = false;
		int[] hand = GlobalData.constructDeck(hand_field).toIDArray();
		for (int id : cur_own_field_ids) {
			for (int i = 0; i < hand.length && !changed; i++) {
				if (hand[i] == id) {
					hand[i] = 0;
					changed = true;
				}
			}
			changed = false;
		}

		int cur_hand_best = 0;
		int best_pos = 0;
		int real_pos=0;
		for (int i = 0; i < 3; i++) {
			int cur_id = 0;
			int cur_pos=0;

			for (int j = 0; j < hand.length; j++) {
				if (hand[j] != 0) {
					cur_id = hand[j];
					cur_pos = j;
					hand[j] = 0;
					break;
				}
			}

			// get its pos in remaining hand
			int[] ord_deck = GlobalData.constructDeck(tuo_deck).toIDArray();
			int freeze = GlobalData.constructCardArray(cur_own_field).length;
			for (int k = freeze; k < ord_deck.length; k++) {
				if (cur_id != cur_hand_best && ord_deck[k] == cur_id && ord_deck.length - k >= best_pos) {
					cur_hand_best = cur_id;
					real_pos = cur_pos;
					best_pos = ord_deck.length - k;
					break;
				}
			}
		}
		TUM.log.d("Play: " + GlobalData.getNameAndLevelByID(cur_hand_best) + "@" + (real_pos+ (is_surge ? 101 : 1)), "LSE",b.getName());
		// for(int i = p.freeze; i < )
		return new int[]{cur_hand_best,real_pos+ (is_surge ? 101 : 1)};
	}
/**
 *  Get enemy played card etc.
 */
	public void update() {
		// TODO Auto-generated method stub

		//idmap.clear();
		//played_map.clear();
		String resp = "";
		try {
			resp = b.getCurl().curl_pull("getBattleResults", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(resp);
		JSONObject obj = new JSONObject(resp);
		JSONObject b_data = obj.getJSONObject("battle_data");
		is_surge = !b_data.getBoolean("host_is_attacker");
		try{
		if(b_data.has("battleground_effects"))
		{
			JSONObject b =b_data.getJSONObject("battleground_effects");
			//System.out.println("BGEJSON: " + b.toString());
			if(b.has("global"))
			{
				JSONObject glob = b.getJSONObject("global");
				String tmpbge = "";
				int tmpbgeid=-1;
				if(glob.keys().hasNext())
				{
					JSONObject tb = glob.getJSONObject(glob.keys().next());
					if(tb.has("name"))tmpbge = tb.getString("name");		
					if(tb.has("id"))tmpbgeid = tb.getInt("id");		
				}
				if(!tmpbge.matches(".*\\d+.*"))
				{
					tmpbge = tmpbge.replaceAll(" ", "");
				}
				bge = tmpbge;
				bge_id = tmpbgeid;
			}
			if(b.has("attacker"))
			{
				JSONObject glob = b.getJSONObject("attacker");
				String tmpbge = "";
				int tmpbgeid=-1;
				if(glob.keys().hasNext())
				{
					JSONObject tb = glob.getJSONObject(glob.keys().next());
					if(tb.has("name"))tmpbge = tb.getString("name");	
					if(tb.has("id"))tmpbgeid = tb.getInt("id");			
				}
				if(!tmpbge.matches(".*\\d+.*"))
				{
					tmpbge = tmpbge.replaceAll(" ", "");
				}
				if(!is_surge){ebge = tmpbge;ebge_id = tmpbgeid;}else{ybge= tmpbge;ybge_id = tmpbgeid;}
			}
			if(b.has("defender"))
			{
				JSONObject glob = b.getJSONObject("defender");
				String tmpbge = "";
				int tmpbgeid=-1;
				if(glob.keys().hasNext())
				{
					JSONObject tb = glob.getJSONObject(glob.keys().next());
					if(tb.has("name"))tmpbge = tb.getString("name");	
					if(tb.has("id"))tmpbgeid = tb.getInt("id");			
				}
				if(!tmpbge.matches(".*\\d+.*"))
				{
					tmpbge = tmpbge.replaceAll(" ", "");
				}
				if(!is_surge){ybge = tmpbge;ybge_id = tmpbgeid;}else{ebge= tmpbge;ebge_id = tmpbgeid;}
			}
		}}catch(Exception e){}
		TUM.log.d("gbge: " + bge + ", ybge: " + ybge + ", ebge: " + ebge,"LSE",b.getName()); 
		
		
		if(b_data.has("enemy_name"))enemy_name = b_data.getString("enemy_name");else enemy_name="UNIDENTIFIED"; //TODO Detect mission/campaign/raid
		enemy_size = b_data.getInt("enemy_size");
		TUM.log.m("Enemy Name: " + enemy_name,"LSE",b.getName());
		if(b_data.has("winner"))
		{
			winner = b_data.getInt("winner");
			TUM.log.m("WINNER: " + winner,"LSE",b.getName());
		}
		else
		{
			winner = -1;
		}

		int a_cmd = b_data.getInt("attack_commander");
		int d_cmd = b_data.getInt("defend_commander");

		JSONObject targets2 = is_surge ? b_data.getJSONObject("defend_deck") : b_data.getJSONObject("attack_deck");

		int[] ordered_deck = new int[targets2.keySet().size()];
		// ordered_deck[0] = a_cmd;
		int k = 0;
		for (Iterator iterator = targets2.keySet().iterator(); iterator.hasNext(); k++) {
			String cur = (String) iterator.next();
			int map_id = Integer.parseInt(cur);
			int real_id = targets2.getInt(cur);
			//idmap.put(map_id, real_id);
			ordered_deck[Integer.parseInt(cur) - (is_surge ? 101 : 1)] = targets2.getInt(cur);
		}
		Card[] cs = GlobalData.getCardArrayFromIDArray(ordered_deck);

		hand_field=(GlobalData.getDeckString(ordered_deck));
		hand_field_ids = ordered_deck;
		TUM.log.d("Your (ordered) hand: " + GlobalData.getDeckString(ordered_deck), "LSE", b.getName());
		if(b_data.has("card_map"))
		{
			b_data = b_data.getJSONObject("card_map");
		}
		else
		{
			b_data=new JSONObject("{}");
		}

		// collect played cards and forts
		ArrayList<Integer> tmp_arr_ordered_enemy_played = new ArrayList<Integer>();
		ArrayList<Integer> tmp_arr_ordered_own_played = new ArrayList<Integer>();
		ArrayList<Integer> tmp_arr_ordered_own_forts = new ArrayList<Integer>();
		tmp_arr_ordered_enemy_played.add(is_surge ? a_cmd : d_cmd);
		// tmp_arr_ordered_own_played.add(is_surge?d_cmd:a_cmd);
		k = 0;
		for (Iterator iterator = b_data.keySet().iterator(); iterator.hasNext(); k++) {
			String cur = (String) iterator.next();
			int map_id = Integer.parseInt(cur);
			int real_id = b_data.getInt(cur);
			if ((!is_surge && map_id >= 51 && map_id < 100) || (is_surge && map_id >= 151 && map_id < 200))
				tmp_arr_ordered_own_forts.add(real_id);	//TODO else iff surges flipped -> enemy_ordered forts
			else if (map_id > 100){
				if (is_surge){
					//played_map.add(map_id);
					tmp_arr_ordered_own_played.add(real_id);}
				else{
					tmp_arr_ordered_enemy_played.add(real_id);}
				}
			else if (is_surge){
				tmp_arr_ordered_enemy_played.add(real_id);}
			else{
				//played_map.add(map_id);
				tmp_arr_ordered_own_played.add(real_id);}
		}
		int[] ordered_enemy_played = tmp_arr_ordered_enemy_played.stream().mapToInt(i -> i).toArray();
		int[] ordered_own_played = tmp_arr_ordered_own_played.stream().mapToInt(i -> i).toArray();
		cur_own_fort_ids = tmp_arr_ordered_own_forts.stream().mapToInt(i -> i).toArray();

		// cs = Data.getCardArrayFromIDArray(ordered_enemy_played);

		cur_enemy_field_ids = ordered_enemy_played;

		TUM.log.d("Enemy (ordered,played) deck: " + GlobalData.getDeckString(cur_enemy_field_ids),"LSE",b.getName());
		cur_enemy_field=(GlobalData.getDeckString(cur_enemy_field_ids));

		// cs = Data.getCardArrayFromIDArray(ordered_own_played);

		if (cur_own_field_ids == null)
			cur_own_field_ids = ordered_own_played;
		int tmp = 0;
		for (int i : ordered_own_played) {
			if (i != 0 && (GlobalData.getCount(ordered_own_played, i) > GlobalData.getCount(cur_own_field_ids, i))) {
				tmp = i;
				break;
			}
		}
		int[] tmp2 = new int[ordered_own_played.length];
		for (int i = 0; i < cur_own_field_ids.length; i++) {
			tmp2[i] = cur_own_field_ids[i];
		}
		if (tmp != 0)
			tmp2[ordered_own_played.length - 1] = tmp;
		cur_own_field_ids = tmp2;

		cur_own_fort=(GlobalData.getDeckString(cur_own_fort_ids));
		TUM.log.d("Own forts: " + GlobalData.getDeckString(cur_own_fort_ids),"LSE",b.getName());

		cur_own_field=(GlobalData.getDeckString(cur_own_field_ids));
		TUM.log.d("Own (ordered,played) deck: " + GlobalData.getDeckString(cur_own_field_ids),"LSE",b.getName());
	}

	public void reset() {
		//idmap.clear();
		//played_map.clear();
		hand_field=("");
		hand_field_ids = null;
		cur_own_field=("");
		cur_own_field_ids = null;
		cur_own_fort=("");
		cur_own_fort_ids = null;
		cur_enemy_field=("");
		cur_enemy_field_ids = null;
	}
}
