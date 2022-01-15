package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import de.neuwirthinformatik.Alexander.TU.Basic.Mission;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;

public class StartBotMain {
	static Random ran = new Random();
	static final SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]: ");

	// NOT OOP yet
	static String path = "/home/pi/TU/bot-tyrant";

	// Default values
	static String NAME = "Tyrant Unleashed BASH Bot";
	static String VERSION = "0.3.1";
	static int LOG = 1;
	static String LOG_FILE = "";
	static int PRE_LOG = 1;
	static String PRE_LOG_STR = "";
	static int INFO = 0;
	static int SAVETODB = 0;
	static int BATTLE = 1;
	static int BATTLE_STAMINA_CAP = 8;
	static int BRAWL = 0;
	static int BRAWL_ENERGY = 0;
	static int CONQUEST = 0;
	static int CONQUEST_AUTO = 1;
	static int CONQUEST_ZONE_ID = 0;
	static int CLAIM_REWARDS = 0;
	static int RAID = 0;
	static int WAR = 0;
	static int WAR_SLOT = 1;
	static int CAMPAIGN = 0;
	static int CAMPAIGN_ID = 0;
	static int CAMPAIGN_LEVEL = 1;
	static int CAMPAIGN_ENERGY = 125;
	static int CLAIM = 1;
	static int GUILD_QUEST = 0;
	static int QUEST_ENERGY = 50;
	static int QUEST_ORDER = 0;
	static int QUEST_RANDOM = 0;
	static int MISSION = 1;
	static int MISSION_ENERGY = 200;
	static int MISSION_ID = 130;
	static int MISSION_INCREASE = 0;
	static int MISSION_INCREASE_AFTER = -1;
	static int MISSION_INCREASE_AFTER_SAVE = 0;
	static int MISSION_INCREASE_AFTER_TIMES = 0;
	static int MISSION_ENERGY_CAP = 200;
	static int CLAIM_READY = 0;
	static int STOP = -1;
	static int SLEEPTIME = 120;
	static int RANDOM_MAX = 0; // min;
	static int CURL_RAND_WAIT = 0; // sec
	static String CURL_RES = "";

	static String[] PARAMS;
	static int START_SLEEP;
	static String ACCOUNT;
	static String GUILD;
	static String COOKIE;

	static String MISSION_NAME;
	static int QUEST_COUNT = 0;
	static int QUEST1, QUEST2, QUEST3;
	static int QUEST1_DONE, QUEST2_DONE, QUEST3_DONE;
	static int QUEST1_MAX, QUEST2_MAX, QUEST3_MAX;

	static int BRAWL_UP, BRAWL_ACTIVE, BRAWL_POINTS, BRAWL_WINS, BRAWL_LOSSES, BRAWL_CLAIMED, BRAWL_RANK;
	static int WAR_UP, WAR_ACTIVE, WAR_POINTS, WAR_CLAIMED, WAR_RANK, WAR_ENERGY;
	static int RAID_UP, RAID_ACTIVE, RAID_CLAIMED, RAID_POINTS, RAID_ENERGY, RAID_LEVEL, RAID_ID;
	static int CONQUEST_ENERGY, CONQUEST_UP, CONQUEST_ACTIVE, CONQUEST_INFLUENCE, CONQUEST_CLAIMED, CONQUEST_RANK,
			CONQUEST_POINTS;

	static Bot bot;

	public static void main(String[] args) {
		logParams(args);
		init();
		parse_param(args);
		log ("SLEEPING FOR "+START_SLEEP+" minutes");
		sleep_m(START_SLEEP);

		

		log("START DATA");
		PRE_LOG_STR = "	";
		info();
		PRE_LOG_STR = "";

		log("*****START*****");

		while ((STOP > 0) || (STOP < 0)) {

			bot.init();
			bot.setAutopilot(false);

			log("----WHILE BEGIN----");

			if (BATTLE > 0)
				dumpStamina();
			if (BRAWL > 0)
				dumpBrawl();
			if (CONQUEST > 0)
				dumpConquest();
			if (WAR > 0)
				dumpWar();
			if (RAID > 0)
				dumpRaid();
			if (CAMPAIGN > 0)
				dumpCampaign();
			if (GUILD_QUEST > 0)
				dump_guild_quest();
			if (MISSION > 0)
				dump_mission();
			if (CLAIM > 0)
				claim_daily_reward();
			if (STOP > 0)
				STOP--;
			
			
			update_data(false);
			
			
			if ((STOP > 0) || (STOP < 0)) {
				int SLEEP_RAND = random_int(RANDOM_MAX);
				int SLEEP_LEFT1 = MISSION_ENERGY_CAP - bot.getEnergy();
				int SLEEP_LEFT2 = SLEEPTIME + SLEEP_RAND;
				int SLEEP_LEFT3 = (BATTLE_STAMINA_CAP - bot.getStamina()) * 15;
				int SLEEP_LEFT = 0;
				if (SLEEP_LEFT1 < SLEEP_LEFT2) {
					if (SLEEP_LEFT1 < SLEEP_LEFT3)
						SLEEP_LEFT = SLEEP_LEFT1 - SLEEP_RAND;
					else
						SLEEP_LEFT = SLEEP_LEFT3 - SLEEP_RAND;
				} else {
					if (SLEEP_LEFT2 < SLEEP_LEFT3)
						SLEEP_LEFT = SLEEP_LEFT2 - SLEEP_RAND;
					else
						SLEEP_LEFT = SLEEP_LEFT3 - SLEEP_RAND;
				}

				int SLEEP_ALL = SLEEP_LEFT + SLEEP_RAND;
				log("ENERGY: " + bot.getEnergy());
				log("SLEEPING FOR " + SLEEP_ALL + " minutes (" + SLEEP_LEFT + " + " + SLEEP_RAND + ")");
				sleep_m(SLEEP_ALL);
				log("WAKING UP");
			}
			log("----WHILE END----");
		}
		log("*****DONE*****");
		//exit();
	}

	private static void claim_daily_reward() {
		if (is_claim_ready())
			bot.claimDailyReward();
	}

	private static void dumpCampaign() {
		log("---CAMPAIGN---");
		PRE_LOG_STR = "	";
		
		update_data(true);
		if (bot.getEnergy() > CAMPAIGN_ENERGY) {
			log("DUMP");
			bot.dumpCampaign(CAMPAIGN_ID, CAMPAIGN_LEVEL);
		}

		PRE_LOG_STR = "";
		log("---CAMPAIGN DONE---");
	}

	private static void dumpStamina() {
		log("---BATTLES---");
		PRE_LOG_STR = "	";
		update_data(false);
		bot.dumpStamina();
		update_data(false);

		PRE_LOG_STR = "";
		log("---BATTLES DONE---");
	}

	private static void dumpConquest() {
		// TODO
		// TMP
		dump_conquest();
	}

	private static void dumpWar() {
		// TODO
		// TMP
		dump_war();
	}

	private static void dumpRaid() {
		// TODO
		// TMP
		dump_raid();
	}

	private static void dumpBrawl() {
		// TODO
		// TMP
		dump_brawl();
	}

	private static void dump_guild_quest() {
		log("---GUILD QUEST---");
		PRE_LOG_STR = "	";

		update_data(true);
		update_quest_data();

		QUEST_COUNT = GUILD_QUEST + random_int(QUEST_RANDOM);

		if (QUEST_ORDER < 0) {
			dump_quest3();
			dump_quest2();
			dump_quest1();
		} else {
			dump_quest1();
			dump_quest2();
			dump_quest3();
		}

		PRE_LOG_STR = "";
		log("---GUILD QUEST DONE---");
	}

	private static void dump_quest1() {
		while (QUEST1_MAX > QUEST1_DONE && bot.getEnergy() > QUEST_ENERGY && QUEST_COUNT > 0) {
			QUEST_COUNT = QUEST_COUNT - 1;
			bot.fightGuildQuest(QUEST1);
			update_data(true);
			update_quest_data();
			log("QUEST1 dump - " + QUEST1_DONE);
		}
	}

	private static void dump_quest2() {
		while (QUEST2_MAX > QUEST2_DONE && bot.getEnergy() > QUEST_ENERGY && QUEST_COUNT > 0) {
			QUEST_COUNT = QUEST_COUNT - 1;
			bot.fightGuildQuest(QUEST2);
			update_data(true);
			update_quest_data();
			log("QUEST2 dump - " + QUEST2_DONE);
		}
	}

	private static void dump_quest3() {
		while (QUEST3_MAX > QUEST3_DONE && bot.getEnergy() > QUEST_ENERGY && QUEST_COUNT > 0) {
			QUEST_COUNT = QUEST_COUNT - 1;
			bot.fightGuildQuest(QUEST3);
			update_data(true);
			update_quest_data();
			log("QUEST3 dump - " + QUEST3_DONE);
		}
	}

	public static void update_quest_data() {
		int[][] qd = null;
		try {
			qd = bot.getCurl().getGuildQuestData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		QUEST1 = qd[0][0];
		QUEST1_DONE = qd[0][1];
		QUEST1_MAX = qd[0][2];
		QUEST2 = qd[1][0];
		QUEST2_DONE = qd[1][1];
		QUEST2_MAX = qd[1][2];
		QUEST3 = qd[2][0];
		QUEST3_DONE = qd[2][1];
		QUEST3_MAX = qd[2][2];

		log_quest_data();
	}

	private static void dump_mission() {
		log("---MISSION DUMP---");
		PRE_LOG_STR = "	";
		update_data(true);
		while (bot.getEnergy() > MISSION_ENERGY) {
			log("Mission Fight: " + MISSION_ID);
			log("ENERGY: " + bot.getEnergy());
			int TMP_ENERGY = bot.getEnergy();
			bot.fightMission(MISSION_ID,-1);
			bot.updateData();
			if (TMP_ENERGY == bot.getEnergy()) {
				log("Lost last mission-fight?!?");
				if (MISSION_ID > 1499)
					MISSION_ID = 107;
				MISSION_ID = MISSION_ID - 1;
				update_mission_energy();
			} else {
				if (MISSION_INCREASE > 0) {
					MISSION_INCREASE_AFTER = MISSION_INCREASE_AFTER - 1;
					if (MISSION_INCREASE_AFTER < 1 && MISSION_INCREASE_AFTER_TIMES != 0) {
						MISSION_INCREASE_AFTER_TIMES = MISSION_INCREASE_AFTER_TIMES - 1;
						MISSION_ID = MISSION_ID + 1;
						if (MISSION_ID == 143)
							MISSION_ID = 142;
						MISSION_INCREASE_AFTER = MISSION_INCREASE_AFTER_SAVE;
						update_mission_energy();
					}
				}
			}
		}
		PRE_LOG_STR = "";
		log("---MISSIONS DONE---");
	}

	private static boolean is_claim_ready() {
		return bot.getDailyBonus() == 1;
	}

	private static void update_mission_energy() {
		if(MISSION_ID <= 0) MISSION_ID = 1;
		Mission m = GlobalData.getMissionByID(MISSION_ID);
		if (m != null)MISSION_ENERGY = m.getCosts();
	}

	private static void update_data(boolean nlog) {
		bot.updateData();
		if (!nlog)
			log_data();
	}

	private static void log_quest_data() {
		log("ENERGY: " + bot.getEnergy());
		log("QUEST1: " + QUEST1 + " " + QUEST1_DONE + " " + QUEST1_MAX);
		log("QUEST2: " + QUEST2 + " " + QUEST2_DONE + " " + QUEST2_MAX);
		log("QUEST3: " + QUEST3 + " " + QUEST3_DONE + " " + QUEST3_MAX);
	}

	private static void log_data() {
		// log "COOKIE: $COOKIE"
		// log( "TRAGET_ID: "+TARGET_ID);
		// log ("ENEMY_NAME: " +ENEMY_NAME);
		// log ("ENEMY_GUILD: "+ENEMY_GUILD);
		// log ("ENEMY_BR: "+ENEMY_BR);
		log("STAMINA: " + bot.getStamina());
		log("ENERGY: " + bot.getEnergy());
		log("CLAIM_READY: " + bot.getDailyBonus());
		log("GOLD: " + bot.getMoney());
		log("SP: " + bot.getSalvage());
	}

	private static void info() {
		// TODO
		log("PARAMS are: ");
		logParams(PARAMS);
		update_data(false);
		// update_brawl_energy();
		// update_quest_data();
		// update_conquest_data();
		// log_conquest_zones();
		// update_raid_data();
		// update_war_data();
	}

	private static void log_conquest_zones() {
	}// TODO

	private static void log(String msg) {
		if (LOG == 1) {
			String TIMESTAMP = sdf.format(new Timestamp(System.currentTimeMillis()));
			// TODO LOGFILE
			if (!LOG_FILE.equals("")) {
				GlobalData.appendLine(LOG_FILE, TIMESTAMP  + PRE_LOG_STR  + " "+ msg);
			} else {
				System.out.println(TIMESTAMP  + PRE_LOG_STR + " "+ msg);
			}
		}
	}

	private static void logParams(String[] args) {
		for (String s : args)
			System.out.print(s + " ");
		System.out.println("");
	}

	private static void sleep_m(int time) {
		try {
			Thread.sleep(1000 * 60 * time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parse_param(String[] args) {
		if (args.length == 0) {
			print_help();
			exit();
		}
		PARAMS = args;
		for (int i = 0; i < PARAMS.length; i++) {
			//log(PARAMS[i]);
			switch (PARAMS[i]) {
			case ("-ss"):
			case ("--start-sleep"):
				START_SLEEP = random_int(s2i(PARAMS[++i]));
				break;
			case ("-c"):
			case ("--cookie"):
				log("" + PARAMS[1+i]);
				ACCOUNT = PARAMS[++i];
				COOKIE = GlobalData.getCookie(ACCOUNT);
				bot = new Bot(ACCOUNT);
				break;
			case ("-g"):
			case ("--guild"):
				GUILD = PARAMS[++i];
				break;
			case ("-ba"):
			case ("--battle"):
				BATTLE = s2i(PARAMS[++i]);
				break;
			case ("-bsc"):
			case ("--battle-stamina-cap"):
				BATTLE_STAMINA_CAP = s2i(PARAMS[++i]);
				break;
			case ("-br"):
			case ("--brawl"):
				BRAWL = s2i(PARAMS[++i]);
				break;
			case ("-cq"):
			case ("--conquest"):
				CONQUEST = s2i(PARAMS[++i]);
				break;
			case ("-cqa"):
			case ("--conquest-auto"):
				CONQUEST_AUTO = s2i(PARAMS[++i]);
				break;
			case ("-cqi"):
			case ("--conquest-zone-id"):
				CONQUEST_ZONE_ID = s2i(PARAMS[++i]);
				break;
			case ("-ra"):
			case ("--raid"):
				RAID = s2i(PARAMS[++i]);
				break;
			case ("-war"):
			case ("--war"):
				WAR = s2i(PARAMS[++i]);
				break;
			case ("-ws"):
			case ("--war-slot"):
				WAR_SLOT = s2i(PARAMS[++i]);
				break;
			case ("-q"):
			case ("--quest"):
				GUILD_QUEST = s2i(PARAMS[++i]);
				break;
			case ("-qe"):
			case ("--quest-energy"):
				QUEST_ENERGY = s2i(PARAMS[++i]);
				break;
			case ("-qo"):
			case ("--quest-order"):
				QUEST_ORDER = s2i(PARAMS[++i]);
				break;
			case ("-m"):
			case ("--mission"):
				MISSION = s2i(PARAMS[++i]);
				break;
			case ("-mi"):
			case ("--mission-id"):
				MISSION_ID = s2i(PARAMS[++i]);
				update_mission_energy();
				break;
			case ("-mn"):
			case ("--mission-name"):
				MISSION_NAME = PARAMS[++i];
				break;
			case ("-me"):
			case ("--mission-energy"):
				MISSION_ENERGY = s2i(PARAMS[++i]);
				break;
			case ("-mec"):
			case ("--mission-enery-cap"):
				MISSION_ENERGY_CAP = s2i(PARAMS[++i]);
				break;
			case ("-mia"):
			case ("--mission-increase-after"):
				MISSION_INCREASE = 1;
				MISSION_INCREASE_AFTER = s2i(PARAMS[++i]);
				MISSION_INCREASE_AFTER_SAVE = MISSION_INCREASE_AFTER;
				break;
			case ("-miat"):
			case ("--mia-times"):
				MISSION_INCREASE_AFTER_TIMES = s2i(PARAMS[++i]);
				break;
			case ("-s"):
			case ("--savetodb"):
				SAVETODB = s2i(PARAMS[++i]);
				break;
			case ("-st"):
			case ("--sleep-time"):
				SLEEPTIME = s2i(PARAMS[++i]);
				break;
			case ("-o"):
			case ("--once"):
				STOP = 1;
				break;
			case ("-r"):
			case ("--runs"):
				STOP = s2i(PARAMS[++i]);
				break;
			case ("-cl"):
			case ("--claim"):
				CLAIM = s2i(PARAMS[++i]);
				break;
			case ("-clr"):
			case ("--claim-rewards"):
				CLAIM_REWARDS = s2i(PARAMS[++i]);
				break;
			case ("-qr"):
			case ("--quest-random"):
				QUEST_RANDOM = s2i(PARAMS[++i]);
				break;
			case ("-rm"):
			case ("--random-max"):
				RANDOM_MAX = s2i(PARAMS[++i]);
				break;
			case ("-crw"):
			case ("--curl-rand-wait"):
				CURL_RAND_WAIT = s2i(PARAMS[++i]);
				break;
			case ("--reset"):
				init();
				break;
			case ("-l"):
			case ("--log"):
				LOG = s2i(PARAMS[++i]);
				break;
			case ("-lf"):
			case ("--log-file"):
				LOG_FILE = PARAMS[++i];
				break;
			case ("-pl"):
			case ("--pre-log"):
				PRE_LOG = s2i(PARAMS[++i]);
				break;
			case ("-i"):
			case ("--info"):
				INFO = 1;
				break;
			case ("-h"):
			case ("--help"):
				print_help();
				exit();
				break;
			case ("-camp"):
			case ("--campaign"):
				CAMPAIGN = s2i(PARAMS[++i]);
				break;
			case ("-camp-id"):
			case ("--campaign-id"):
				CAMPAIGN_ID = s2i(PARAMS[++i]);
				break;
			case ("-camp-lvl"):
			case ("--campaign-level"):
				CAMPAIGN_LEVEL = s2i(PARAMS[++i]);
				break;
			case ("-camp-e"):
			case ("--campaign-energy"):
				CAMPAIGN_ENERGY = s2i(PARAMS[++i]);
				break;
			default:
				System.out.println("UNKNOWN OPTION: '" + PARAMS[i] + "', ABORTING");
				print_help();
				exit();

			}		
			
		}
		if(COOKIE.equals(""))
		{
			System.out.println("[!!!] No cookies set or empty. Aborting.");
			print_help();
			exit();
		}
		
		if(! LOG_FILE.equals(""))
		{
			GlobalData.deleteFile(LOG_FILE);
		}
		
		if(INFO > 0)
		{
			info();
			exit();
		}
	}

	private static int s2i(String s) {
		return Integer.parseInt(s);
	}

	private static int random_int(int i) {
		return i<=0?0:ran.nextInt(i);
	}

	private static void print_help() {
		System.out.println("$NAME v$VERSION");
		System.out.println("Usage: ./bot.sh --cookie test [OPTION]...");
		System.out.println(
				"Tyrant Unleashed Auto-Play-Skip-Bot for Mission, Guild War, Brawl, Conquest, Guild Quests, Raids and Battles");
		System.out.println("");
		System.out.println("Options:");
		System.out.println("Last option value overrides previous value.");
		System.out.println("");

		System.out.println("  -c,	--cookie NAME 		cookie file './cookies/cookie_NAME'");
		System.out.println("				This value needs to be set.");
		System.out.println("");
		System.out.println("  -g,	--guild NAME");
		System.out.println("");

		System.out.println("  -ba,	--battle BOOL		battle dump (default=0)");
		System.out.println("");

		System.out.println("  -br,	--brawl BOOL		brawl dump (default=0)");
		System.out.println("");

		System.out.println("  -cq,	--conqest BOOL		conquest dump (default=0)");
		System.out.println("");
		System.out.println("  -cqa,	--conqest-auto BOOL	conquest dump on first zone(default=1)");
		System.out.println("");
		System.out.println("  -cqi,	--conqest-zone-id ID	conquest dump on selected zone");
		System.out.println("");

		System.out.println("  -q,	--quest BOOL		enable quest dump (default=0)");
		System.out.println("");
		System.out.println("  -qe,	--quest-energy E	mission energy costs for dumps (default=50)");
		System.out.println("");
		System.out.println(
				"  -qo,	--quest-order ORDER	ORDER low than zero: quest-order:3,2,1 else: 1,2,3 (default=0)");
		System.out.println("");

		System.out.println("  -m,	--mission BOOL		mission dump (default=0)");
		System.out.println("");
		System.out.println("  -mi,	--mission-id ID		mission id for mission dump");
		System.out.println("");
		System.out.println("  -me,	--mission-energy E	mission energy costs for dumps");
		System.out.println("");
		System.out.println("  -mia,	--mission-increase-after N");
		System.out.println("					mission-id increase after N mission fights");
		System.out.println("  -mec,	--mission-energy-cap N");
		System.out.println("					energy cap");

		System.out.println("  -s,	--savetodb BOOL		save decks/names/id/guilds to mysql (default=0)");
		System.out.println("");
		System.out.println("  -st,	--sleep-time MINUTES	sleep time between dump cylces (default=100)");
		System.out.println("");

		System.out.println("  -o,	--once			Only one dump cylce. Same as '--runs 1'.");
		System.out.println("  -r,	--runs NUMBER		number of dump cycles (default=INF/-1)");
		System.out.println("");

		System.out.println("  -cl,	--claim	 BOOL		claim daily reward (default=1)");
		System.out.println("  -clr,	--claim-rewards BOOL	claim event rewards (default=0)");
		System.out.println("");

		System.out.println(
				"  -qr,	--quest-random NUM	0-NUM random additional quest dumps per dump cycle (default=0)");
		System.out.println("  -rm,	--random-max NUM	0-NUM random additional minutes to --sleep-time (default=0)");
		System.out.println(
				"  -crw,	--curl-rand-wait NUM	0-NUM random seconds slee after finished curl request (default=0)");
		System.out.println("");
		System.out.println("  -l,	--log BOOL		log infos (default=1)");
		System.out.println("  -lf,	--log-file FILE		log to file");
		System.out.println("  -pl,	--pre-log BOOL		structured log (default=1)");
		System.out.println("");
		System.out.println("  -i,	--info			display info about account and exit");
		System.out.println("  -h, 	--help			display this help and exit");

		System.out.println("");
		System.out.println("Example(s):");
		System.out.println("");
		System.out.println(
				"./bot.sh --conquest 1 --conquest-auto 0 --conquest-zone-id 16 --brawl 1 --claim 1 --quest 3 --mission 1 --battle 1 --cookie test_acc --mission-id 81 --mission-energy 150 --sleep-time 150  --savetodb 0 --random-max 0 --curl-rand-wait 5");
		System.out.println("");
		System.out.println("Use at your own risk!!!");
	}

	private static void exit() {
		System.exit(0);
	}

	private static void init() {
		// Default values
		NAME = "Tyrant Unleashed BASH Bot";
		VERSION = "0.3.1";
		LOG = 1;
		LOG_FILE = "";
		PRE_LOG = 1;
		PRE_LOG_STR = "";
		INFO = 0;
		SAVETODB = 0;
		BATTLE = 0;
		BATTLE_STAMINA_CAP = 8;
		BRAWL = 0;
		BRAWL_ENERGY = 0;
		CONQUEST = 0;
		CONQUEST_AUTO = 1;
		CONQUEST_ZONE_ID = 0;
		CLAIM_REWARDS = 0;
		RAID = 0;
		CLAIM = 1;
		GUILD_QUEST = 0;
		QUEST_ENERGY = 50;
		QUEST_ORDER = 0;
		QUEST_RANDOM = 0;
		MISSION = 0;
		MISSION_ENERGY = 200;
		MISSION_ID = 130;
		MISSION_INCREASE = 0;
		MISSION_INCREASE_AFTER = -1;
		MISSION_ENERGY_CAP = 200;
		CLAIM_READY = 0;
		STOP = -1;
		SLEEPTIME = 120;
		RANDOM_MAX = 0; // min;
		CURL_RAND_WAIT = 0; // sec
		CURL_RES = "";
		System.out.println("");
	}

	// !!!!!!!!!!!!!!!!!!!! DIRTY STUFF DON't regex AND DON'T CURL DIRECTLY AND
	// DO USE JSON!!!!!!!!!!
	public static int contains_str(String j, String c) {
		return j.contains(c) ? 1 : 0;
	}

	public static int contains_str_i(String j, String c) {
		return j.toLowerCase().contains(c.toLowerCase()) ? 1 : 0;
	}

	public static String get_last(String j, String s) {
		j = j.replaceAll(".*\"" + s + "\":", "");
		j = j.replaceAll(",.*", "");
		j = j.replaceAll("\"", "");
		j = j.replaceAll("\\{", "");
		j = j.replaceAll("}", "");
		return j;
	}

	public static String get_first(String j, String s) {
		String p = "\"" + s + "\":([0-9a-zA-Z\"]*).*";
		return j.replaceAll(p, "$1").replaceAll(".*,", "").replaceAll("\"", "");
	}

	private static void update_brawl_energy() throws Exception {
		String json = bot.getCurl().curl_pull("getHuntingTargets", "");
		BRAWL_UP = contains_str(json, "active_brawl_data\":{\"id\":");
		if (BRAWL_UP > 0) {

			if (s2i(get_last(json, "time")) > s2i(get_last(json, "end_time")))
				BRAWL_ACTIVE = 0;
			else
				BRAWL_ACTIVE = 1;

			BRAWL_ENERGY = s2i(get_last(json, "battle_energy"));
			BRAWL_WINS = s2i(get_last(json, "wins"));
			BRAWL_LOSSES = s2i(get_last(json, "losses"));
			BRAWL_RANK = s2i(get_last(json, "current_rank"));
			BRAWL_POINTS = s2i(get_last(json, "points"));
			BRAWL_CLAIMED = s2i(get_last(json, "claimed_rewards"));
			log("BRAWL_ENERGY: " + BRAWL_ENERGY);
			log("RANK " + BRAWL_RANK + ". WITH " + BRAWL_POINTS + " POINTS W/L: " + BRAWL_WINS + "/" + BRAWL_LOSSES
					+ " CLAIMED: " + BRAWL_CLAIMED);
		}
	}

	private static void dump_brawl() {
		log("---BRAWL--");
		PRE_LOG_STR = "	";
		update_data(true);
		try {
			update_brawl_energy();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (BRAWL_UP > 0) {
			log("--DUMPING---");
			if (BRAWL_ACTIVE > 0) {
				while (BRAWL_ENERGY > 0 && BRAWL_ACTIVE > 0) {

					bot.fightBrawlBattle();

					update_data(true);
					try {
						update_brawl_energy();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else {
				if (BRAWL_CLAIMED < 1 && CLAIM_REWARDS > 0) {
					log("Claiming brawl reward");
					bot.claimBrawlRewards();
				}
			}
		}

		PRE_LOG_STR = "";
		log("---BRAWL DONE---");
	}

	private static void update_war_data() throws Exception {
		String json = bot.getCurl().curl_pull("updateFactionWar", "");
		WAR_UP = contains_str(json, "faction_war\":{\"faction_war_id\":");
		if (WAR_UP > 0) {
			if (s2i(get_last(json, "time")) > s2i(get_last(json, "end_time")))
				WAR_ACTIVE = 0;
			else
				WAR_ACTIVE = 1;

			WAR_ENERGY = s2i(get_last(json, "battle_energy"));

			WAR_RANK = s2i(get_first(json, "our_rank"));
			WAR_CLAIMED = s2i(get_last(json, "claimed_rewards"));

			WAR_POINTS = 0;

			// #if [[ $(contains_str_i "$ACCOUNT\",\"damage") -gt 0 ]]; then
			// #WAR_POINTS=$(echo $CURL_RES | sed
			// "s/.*\"$ACCOUNT\",\"damage\"://I" | sed 's/,.*//' | sed 's/"//g'
			// | sed 's/{//g' | sed 's/\}//g')
			// #else
			// #WAR_POINTS=0
			// #fi

			log("WAR_ENERGY: " + WAR_ENERGY + ", WAR_ACTIVE:" + WAR_ACTIVE);
			log("RANK " + WAR_RANK + ". WITH " + WAR_POINTS + " Points, CLAIMED: " + WAR_CLAIMED);
		}
	}

	private static void dump_war() {
		log("---WAR---");
		PRE_LOG_STR = "	";
		update_data(true);
		try {
			update_war_data();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (WAR_UP > 0) {
			if (WAR_ACTIVE > 0) {
				log("--DUMPING---");
				while (WAR_ENERGY > 0 && WAR_ACTIVE > 0) {
					// #start raid fight
					bot.fightWarBattle(WAR_SLOT);

					update_data(true);
					try {
						update_war_data();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if (WAR_CLAIMED < 1 && CLAIM_REWARDS > 0) {
					log("Claiming war reward");
					bot.claimWarRewards();
				}
			}
		}
		PRE_LOG_STR = "";
		log("---WAR DONE---");
	}

	private static void update_raid_data() throws Exception {
		String json = bot.getCurl().curl_pull("updateFaction", "");
		RAID_UP = contains_str(json, "raid");
		if (RAID_UP > 0) {
			json = bot.getCurl().curl_pull("getRaidInfo", "");
			// #RAID_ACTIVE=$(contains_str '"energy":{')
			if (s2i(get_last(json, "time")) > s2i(get_last(json, "raid_level_end")))
				RAID_ACTIVE = 0;
			else
				RAID_ACTIVE = 1;

			RAID_ENERGY = s2i(get_last(json, "battle_energy"));

			RAID_ID = s2i(get_first(json, "raid_id"));
			RAID_LEVEL = s2i(get_first(json, "raid_level"));
			RAID_CLAIMED = s2i(get_last(json, "claimed_rewards"));

			// if [[ $(contains_str_i "$ACCOUNT\",\"damage") -gt 0 ]]; then
			// RAID_POINTS=$(echo $CURL_RES | sed
			// "s/.*\"$ACCOUNT\",\"damage\"://I" | sed 's/,.*//' | sed 's/"//g'
			// | sed 's/{//g' | sed 's/\}//g')
			// else
			// RAID_POINTS=0
			// fi

			log("RAID_ENERGY: " + RAID_ENERGY + ", ID:" + RAID_ID + ", RAID_ACTIVE:" + RAID_ACTIVE);
			log("LEVEL " + RAID_LEVEL + ". WITH " + RAID_POINTS + " Points, CLAIMED: " + RAID_CLAIMED);
		}
	}

	private static void dump_raid() {
		log("---RAID---");
		PRE_LOG_STR = "	";
		update_data(true);
		try {
			update_raid_data();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (RAID_UP > 0) {
			if (RAID_ACTIVE > 0) {
				log("--DUMPING---");
				while (RAID_ENERGY > 0 && RAID_ACTIVE > 0) {
					// #start raid fight
					bot.fightRaidBattle(RAID_ID, RAID_LEVEL);

					// #RAID_ENERGY=$((RAID_ENERGY-1))
					update_data(true);
					try {
						update_raid_data();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if (RAID_CLAIMED < 1 && CLAIM_REWARDS > 0) {
					log("Claiming raid reward");
					bot.claimRaidRewards(RAID_ID);
				}
			}
		}

		PRE_LOG_STR = "";
		log("---RAID DONE---");
	}

	private static void update_conquest_data() throws Exception {
		String json = bot.getCurl().curl_pull("updateFaction", "");
		CONQUEST_UP = contains_str(json, "conquest_data\":{\"id\":");
		if (CONQUEST_UP > 0) {
			json = bot.getCurl().curl_pull("updateFactionWar", "");
			CONQUEST_ACTIVE = contains_str(json, "Conquest!");
			json = bot.getCurl().curl_pull("getConquestUpdate", "");

			// if (CONQUEST_AUTO > 0 )
			// CONQUEST_ZONE_ID=$(echo $CURL_RES | sed 's/.*"zone_data":{"//' |
			// sed 's/":.*//')

			// #shrink
			json = json.replaceAll("rewards.*", "");

			// #sed -i 's/rewards.*//' $TMP_LOG
			// #impl. option set Conquest zone_id
			try {
				CONQUEST_ENERGY = s2i(get_last(json, "battle_energy"));
			} catch (Exception e) {
				CONQUEST_ENERGY = 1;
			}
			;
			// #CONQUEST_WINS=get_last (json,'wins');
			// #CONQUEST_LOSSES=get_last (json,'losses');
			CONQUEST_RANK = s2i(get_first(json, "conquest_rank"));
			CONQUEST_POINTS = s2i(get_first(json, "conquest_points"));
			CONQUEST_INFLUENCE = s2i(get_first(json, "influence"));
			CONQUEST_CLAIMED = s2i(get_last(json, "claimed_reward"));
			log("CONQUEST_ENERGY: $CONQUEST_ENERGY ZONE:$CONQUEST_ZONE_ID");
			log("RANK $CONQUEST_RANK. WITH $CONQUEST_POINTS AND INFL: $CONQUEST_INFLUENCE CLAIMED: $CONQUEST_CLAIMED");
		}
	}

	private static void dump_conquest() {
		log("---CONQUEST---");
		PRE_LOG_STR = "	";
		update_data(true);
		try {
			update_conquest_data();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (CONQUEST_UP > 0) {
			if (CONQUEST_ACTIVE > 0) {
				log("--DUMPING---");
				// #first dump
				while (CONQUEST_ENERGY > 0 && CONQUEST_ACTIVE > 0) {
					// #start conquest fight
					bot.fightConquestBattle(CONQUEST_ZONE_ID);

					update_data(true);
					try {
						update_conquest_data();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if (CONQUEST_CLAIMED < 1 && CLAIM_REWARDS > 0) {
					log("Claiming conquest reward");
					bot.claimConquestRewards();
				}
			}
		}
		PRE_LOG_STR = "";
		log("---CONQUEST DONE---");
	}
}
