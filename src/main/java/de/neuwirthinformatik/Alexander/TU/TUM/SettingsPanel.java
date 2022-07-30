package de.neuwirthinformatik.Alexander.TU.TUM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Endgame;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import de.neuwirthinformatik.Alexander.TU.util.Task;

public class SettingsPanel extends JPanel {

	/**
	 *
	 * Outdated lists:
	 *
	 * Salvage List:
	 * Greatheart,Relliger,Fissure,Vesta,  Wraith, Nephtar, Nen Hexotype, Mana Courier, Scarblade Squad,Quadshot Automaton, Shockshooter,Quadra Assist, Kentauros Retainer, Gall Gatherer, Cerebrum, Riptide Cloaker, Igniting Conveyer, Quadshot Automation, Amon,  Cyclone, Gallant Knight, Sphere Bot, Autocrat, Hyperion, Styxelt, Anasi,  Prophet of Damnation, Fissure, Metronome, Origin Explorer, Zethus, Amulet Heartsoothe, Abraxas Slumbering, Kishar, Gehenna's Birth, Gehenna's Core, Deeps Traverser, Uniter of Faiths,Sorrid Gunwalker,Haklok, Deserted Expatriate, Golgorian Augur, Fort Builder,  Mana Courier, Gozek, Gozek Sootshell, Takalak, Expatriate, Charger, Mana Courier, Contempt, Eden Shinobi, Shinobi, Calm Invoker, Sensor Driver, Wastes Craft, Indignant, Traitor Detector, Brutal Mantodea, Jyackavor, Pantyke, Onyx, Zultaca, Noruko, Warding Agent, Kerza, Petrisis Thug, Fighter Jet, Shockwalker, Quadshot Automation, Mana Courier, Xanadu, Baughe, Eupnoi, Mongore, Gluttonous Fiend, Salvage Stockpile, Salvage Crate, Varangian, Foxhole Builder, Ifrit Mechanic, Oroshi, Mobilized Casket, Decisive Ironclad, Arch Nova, Incendiary Drifter, Primal Monitor, Ozone Battler, Sargon Dormant, Heavyweight,Edenite Idealist, Spatha, Reserve Spatha, Extinguisher, Multishot Automaton, Ancile Defender, Manna Bearer, Hexotype, Scarsnout, Razorback, Deeps Explorer, Uniter, Algol, Ancient Presence, Fissure, Propher of Damnation, Winseer, Grizrael, Infectious Rot, Tartarian Rot, Vast Torturian, Vast Torturiael,Gorivore, Hexsin, Toxin Tank, Ophanim, Klaxon Bot, Grotesque, Cyberpod, Skulliton Death, Azurial Armored, Omniseer, Blisandro, Vegna, Irrian, Qotic, Needler Vektar, Dyspnoi, March Sustainer, Campaign Sustainer, Syndicate Gladiator, Stinger, Airship, Reaver, Pantheon Shard, Velve, Ophanim Quadriga, Bruiser, Oluth's Will, Nephtar, Marshall, Buggy, Vincentius, Demi-culverin, Primus, Sierra, Divinity, Boneforge, Xedronic Rover, Subterran Horror, Entangler, Staring Oru, Yog-zoth, Violent Mantodea, Bane, Dictate of Command, Edenite, Idealist, Lowbrow, Retriever, Ramshack Rickshaw, Diesel Leadcar, Messiar, Meld, Beama, Symbiotic, Force Twin, Elite Soldier, Quake, Miasma Refiner, Sulfuris Refiner, Aragon Vestige, Omenosis, Overmind, Blood Cur, Valencio, Serraco, Magus, Gemini Life Aspect, Frenzied Barrager, Halesurge,Tikal, Pantheon Progeny, Xillafang,Auger, Keres, Miasma Refiner, Empire Biker, Head of Rituals,Bulkhead Breacher,Perimeter Breacher,Rubble Crawler, Aquanaut,Cleave Breaker,Ayrkrane, Temper of Triton,Kentauros,Artifact Constructors,Uh'gorlin Cub,Howler,Rescue Zuka, Vigilant Racer,Tomb-bound, Golgorian Enchanter,  Hephatat, Frontier Settlement, Zorbo,Scamper,Siera,Grail Watcher, Zuruwing,Needler Vekt, Infernal,Serraco,Xixod,Burrower,Quake,Fanatic,Miracle Tracker,Autonomous Collector, Gorrus, Hovel Hoarder, Octane's Base, AD4M,Cherisher,Neo Excavator,Kilnshop,Gigantos, Aggeroth, Aragon Vestige, Blessed Jaunter, Minerva's Decree, Tygarm, Gladius, Flesh Assimilator,Whistler, Trench,  Sporadic Nado, Geist,Tartarus Scion,All Purpose,Munitions,Dion, Debris Crawler,Regiment,Nexusisite,Rampage,Brasshouse, Flash of Courage, Dark Covert, Winsalvo, Yog-zor, Fallen Archon, Riptide Lurker, Minaret, Igniter, Hill Quaker, Amon, Chevalier,Briareos, Manta Copter,Nighthawk, Astral Strafer, Skulker, Comet,Neocyte Soldier,Hektor, Skulliton,Myrvern, Surn of the Night,Skyrender, Cazarm, Exicon, , 8-Barrel Blaster, Gilgamesh,Zashik, Growing Dam, Bluster, Nightcrawler, Gethar, Retriver, enrized, Barrager, Chronoshift, Minotaur, Gravity Dampener 
	 * 
	 * Base List:
	 * Draconian Quee-1#12, Smog Tank-1#12, Blight Crusher-1#12, Blood Pool-1#12, Sinew Feeder-1#12, Tiamar-1#12, Aegis-1#12, Windreaver-1#12, Absorption Shield-1#12, Blackrock-1#12, Havoc-1#12, Bulldozer-1#12, Iron Maider-1#12, Missile Silo-1#12, Demon of Embers-1#12, Vigil-1#12, Contaminant Scour-1#12, Equalizer-1#12, Sanctuary-1#12, Falcion-1#12, Dreadship-1#12, Xeno Mothership-1#12, Daemon-1#12, Genetics Pit-1#12, Lurker Beast-1#12, 
	 * Create List:
	 * Alpha Type-C,Octane Optimized, Tomb-broken, Shockstorm, Bunker Builder, Nentrok Hexotype, 
	 * 
	 * Display List:
	 * Octane Optimized,Petrisis Gorefist,Barracus the Traitor, Arkadios Ultimate,Absorption Forcefield,Razogoth's Gunhound, Steady Gavel, Tactical Sapper,The Indoctrinated , Pox Infector, Swift Veyastro, Absolute Testament, Nentrok Hexotype,Darius' Muscle,Clockwork Lunatic,Sacred Sanctuary, Dreamhaunter,Revolt Ranger,Sif's Evangel,Morbid Kivoron,  Ezamit Tranq, Needler Vektarok,Kleave's Zanthopod,Sin Swallower, Co-operated Crawler, Hurkol Bloodvessel, Shockstorm, Tomb-broken, Bunker Builder, Ashpodel Monitor, Exicon Vanisher, Trepidation ,Faithful Hospitaller,Edenite Emancipator,
	 */
	//Hard-coded data
	public String salvage_all= "Xeno Queller,Takalak, Cleave Breaker, Stealthy Niaq,Duo Blasttek, Aqua Bombers, Vesles, Moroi, Mahabha, Ninurta, Exaticast Desolator, Kinirok, Wreckshot, Prototype Grendel, Tricettar Sharpshot, Lurpak, Lazarus, Geodon,Oculum, Fissure, Ruins Inspector,Farsyo, Lucidier, Nephtar, Aqua Bombers, Anvil, Xeno Queller, Tazerok,Greatheart,Relliger,Fissure,Vesta,  Wraith, Nephtar, Nen Hexotype, Mana Courier, Scarblade Squad,Quadshot Automaton, Shockshooter,Quadra Assist, Kentauros Retainer, Gall Gatherer, Cerebrum,Riptide Cloaker, Igniting Conveyer, Quadshot Automation, Amon,  Cyclone, Gallant Knight, Sphere Bot, Autocrat, Hyperion, Styxelt, Anasi,  Prophet of Damnation, Fissure, Metronome, Origin Explorer, Zethus, Amulet Heartsoothe, Abraxas Slumbering, Kishar, Gehenna's Birth, Gehenna's Core, Deeps Traverser, Uniter of Faiths,Sorrid Gunwalker,Haklok, Deserted Expatriate, Golgorian Augur, Fort Builder,  Mana Courier, Gozek, Gozek Sootshell, Takalak, Expatriate, Charger, Mana Courier, Contempt, Eden Shinobi, Shinobi, Calm Invoker, Sensor Driver, Wastes Craft, Indignant, Traitor Detector, Brutal Mantodea, Jyackavor, Pantyke, Onyx, Zultaca, Noruko, Warding Agent, Kerza, Petrisis Thug, Fighter Jet, Shockwalker, Quadshot Automation, Mana Courier, Xanadu, Baughe, Eupnoi, Mongore, Gluttonous Fiend, Salvage Stockpile, Salvage Crate, Varangian, Foxhole Builder, Ifrit Mechanic, Oroshi, Mobilized Casket, Decisive Ironclad, Arch Nova, Incendiary Drifter, Primal Monitor, Ozone Battler, Sargon Dormant, Heavyweight,Edenite Idealist, Spatha, Reserve Spatha, Extinguisher, Multishot Automaton, Ancile Defender, Manna Bearer, Hexotype, Scarsnout, Razorback, Deeps Explorer, Uniter, Algol, Ancient Presence, Fissure, Propher of Damnation, Winseer, Grizrael, Infectious Rot, Tartarian Rot, Vast Torturian, Vast Torturiael,Gorivore, Hexsin, Toxin Tank, Ophanim, Klaxon Bot, Grotesque, Cyberpod, Skulliton Death, Azurial Armored, Omniseer, Blisandro, Vegna, Irrian, Qotic, Needler Vektar, Dyspnoi, March Sustainer, Campaign Sustainer, Syndicate Gladiator, Stinger, Airship, Reaver, Pantheon Shard, Velve, Ophanim Quadriga, Bruiser, Oluth's Will, Nephtar, Marshall, Buggy, Vincentius, Demi-culverin, Primus, Sierra, Divinity, Boneforge, Xedronic Rover, Subterran Horror, Entangler, Staring Oru, Yog-zoth, Violent Mantodea, Bane, Dictate of Command, Edenite, Idealist, Lowbrow, Retriever, Ramshack Rickshaw, Diesel Leadcar, Messiar, Meld, Beama, Symbiotic, Force Twin, Elite Soldier, Quake, Miasma Refiner, Sulfuris Refiner, Aragon Vestige, Omenosis, Overmind, Blood Cur, Valencio, Serraco, Magus, Gemini Life Aspect, Frenzied Barrager, Halesurge,Tikal, Pantheon Progeny, Xillafang,Auger, Keres, Miasma Refiner, Empire Biker, Head of Rituals,Bulkhead Breacher,Perimeter Breacher,Rubble Crawler, Aquanaut,Cleave Breaker,Ayrkrane, Temper of Triton,Kentauros,Artifact Constructors,Uh'gorlin Cub,Howler,Rescue Zuka, Vigilant Racer,Tomb-bound, Golgorian Enchanter,  Hephatat, Frontier Settlement, Zorbo,Scamper,Siera,Grail Watcher, Zuruwing,Needler Vekt, Infernal,Serraco,Xixod,Burrower,Quake,Fanatic,Miracle Tracker,Autonomous Collector, Gorrus, Hovel Hoarder, Octane's Base, AD4M,Cherisher,Neo Excavator,Kilnshop,Gigantos, Aggeroth, Aragon Vestige, Blessed Jaunter, Minerva's Decree, Tygarm, Gladius, Flesh Assimilator,Whistler, Trench,  Sporadic Nado, Geist,Tartarus Scion,All Purpose,Munitions,Dion, Debris Crawler,Regiment,Nexusisite,Rampage,Brasshouse, Flash of Courage, Dark Covert, Winsalvo, Yog-zor, Fallen Archon, Riptide Lurker, Minaret, Igniter, Hill Quaker, Amon, Chevalier,Briareos, Manta Copter,Nighthawk, Astral Strafer, Skulker, Comet,Neocyte Soldier,Hektor, Skulliton,Myrvern, Surn of the Night,Skyrender, Cazarm, Exicon, , 8-Barrel Blaster, Gilgamesh,Zashik, Growing Dam, Bluster, Nightcrawler, Gethar, Retriver, enrized, Barrager, Chronoshift, Minotaur, Gravity Dampener";
		
	public String salvage_to= "Draconian Quee-1#20, Smog Tank-1#20, Blight Crusher-1#20, Blood Pool-1#20, Sinew Feeder-1#20, Tiamar-1#20, Aegis-1#20, Windreaver-1#20, Absorption Shield-1#20, Blackrock-1#20, Havoc-1#20, Bulldozer-1#20, Iron Maider-1#20, Missile Silo-1#20, Demon of Embers-1#20, Vigil-1#20, Contaminant Scour-1#20, Equalizer-1#20, Sanctuary-1#20, Falcion-1#20, Dreadship-1#20, Xeno Mothership-1#20, Daemon-1#20, Genetics Pit-1#20, Lurker Beast-1#20";			 
		
	// public static JTextField cookies_folder;
	String cookies_folder;
	JTextField enemy_deck;
	JTextField flags;
	GUI.IntegerField sim_iter;
	GUI.IntegerField reorder_iter;
	GUI.IntegerField climb_iter;
	GUI.IntegerField lse_iter;
	GUI.IntegerField threads;
	//GUI.IntegerField endgame;
	GUI.IntegerField temperature;
	GUI.IntegerField temperature_decrease;
	GUI.IntegerField lse_turns;
	JTextField yf;
	JTextField ef;
	JTextField ye;
	JTextField ee;
	JTextField bge;
	JTextField fuse_options;

	JTextField acc_name;

	JCheckBox cb_sim_sim;
	JCheckBox cb_sim_quad;
	JCheckBox cb_sim_dual;
	JCheckBox cb_sim_single;
	JCheckBox cb_climb_all;
	JCheckBox cb_fund_all;
	JCheckBox cb_tuo_out;
	JCheckBox cb_lse_out;
	JCheckBox cb_use_lse;
	JCheckBox cb_save_decks_to_jedi_db;
	JCheckBox cb_force_full;

	JButton start;
	JButton start_single;
	JButton start_lse;
	JButton start_brawl_dump;
	
	JComboBox<Mode> modeList;
	JComboBox<Order> orderList;
	JComboBox<OP> climbList;
	JComboBox<Dom> domList;
	JComboBox<Endgame> egList;

	public String[] guilds = new String[] { /*
			  "DireTide", "WarHungryFTMFW",
			  "MasterJedis", "ImmortalDYN",
			  "Decepticon", "TidalWave", "Russia",
			  "NewHope", "AllSpark", "NovaSlayers",
			  "Predacons", "TheFalleKnights", "DeutscheHeldenDYN", 
			  "LizardSquad", "Nihilists", "TrveKvlt", 
			  "ASYLUM", "BiPolarBairs", "ForActivePlayers", 
			  "Cyberdyne", "RisingChaosElite", "CarryTheNite",
			  "TheStrippers", "DeadlyWolves", "NeverStop",
			  "Darkenesis", "gravybairs", "AtomicWarBeasts",
			  "AllHailBolas", "Supr3macy", "MetalCorp", 
			  "AUGM3NT", "Mexxx", "WarThirstyFTMFW",
			  "LadyKillerz", "Polska", "EternalDYN",
			  "wolframium", "Odyssey", "KarmaGetSome",
			  "RIPsquad", "OVERTHETOP", "ApsychoAAkillerA" ,
			  "SupremeBairs"
			  //*/
			  };
	
	public String[] guilds() {
		return guilds;
	}
	
	public String cookies_folder() {
		return (cookies_folder != null) ? cookies_folder : "cookies";
	}

	public String enemy_deck() {
		return (enemy_deck != null) ? enemy_deck.getText().trim() : "Raid#49-20";//my_guild_decks
	}
	
	public String flags() {
		return (flags != null) ? flags.getText().trim() : "";
	}

	public Mode mode() {
		return (modeList != null) ? (Mode) modeList.getSelectedItem() : Mode.raid;
	}

	public Order order() {
		return (orderList != null) ? (Order) orderList.getSelectedItem() : Order.random;
	}
	
	public OP climb_algorithm() {
		return (climbList != null) ? (OP) climbList.getSelectedItem() : OP.climb;
	}
	
	public Dom dom() {
		return (domList != null) ? (Dom) domList.getSelectedItem() : Dom.dom_owned;
	}

	public String yf() {
		return (yf != null) ? yf.getText().trim() : "";
	}

	public String ef() {
		return (ef != null) ? ef.getText().trim() : "";
	}

	public String ye() {
		return (ye != null) ? ye.getText().trim() : "";
	}

	public String ee() {
		return (ee != null) ? ee.getText().trim() : "";
	}

	public String bge() {
		return (bge != null) ? bge.getText().trim() : "Fortification";
	}


	public String fuse_options() {
		return (fuse_options != null) ? fuse_options.getText().trim() : "Octane";
	}
	
	public Card[] fuseCards() {
		if (TUM.settings.fuse_options().equals(""))
			return GlobalData.distinct_cards;
		else
			return GlobalData.constructCardArray(fuse_options());
	}

	public int sim_iter() {
		return (sim_iter != null) ? sim_iter.getNumber() : 1000;
	}

	public int reorder_iter() {
		return (reorder_iter != null) ? reorder_iter.getNumber() : 1000;
	}

	public double[] climb_iter() {
		return (climb_iter != null) ? new double[]{climb_iter.getNumber(),temperature(),temperature_decrease()} : new double[]{1000};
	}

	public int temperature() {
		return (temperature != null) ? temperature.getNumber() : 100;
	}
	
	public double temperature_decrease() {
		return (temperature_decrease != null) ? temperature_decrease.getNumber()/(double)100000 : 0.001;
	}
	
	public int lse_iter() {
		return (lse_iter != null) ? lse_iter.getNumber() : 1000;
	}
	
	public int lse_turns() {
		return (lse_turns != null) ? lse_turns.getNumber() : 10;
	}

	public int threads() {
		return (threads != null) ? threads.getNumber() : 4;
	}
	
	public boolean force_full_deck() {
		return (cb_force_full != null) ? cb_force_full.isSelected() : false;
	}

	public Endgame endgame() {
		return (egList != null) ? (Endgame) egList.getSelectedItem() : Endgame.fused;
	}
	

	public boolean sim_options() {
		return (cb_sim_sim != null) ? cb_sim_sim.isSelected() : false;
	}

	public boolean sim_quad() {
		return (cb_sim_quad != null) ? cb_sim_quad.isSelected() : true;
	}

	public boolean sim_dual() {
		return (cb_sim_dual != null) ? cb_sim_dual.isSelected() : false;
	}

	public boolean sim_single() {
		return (cb_sim_single != null) ? cb_sim_single.isSelected() : false;
	}
	
	public boolean use_lse() {
		return (cb_use_lse != null) ? cb_use_lse.isSelected() : false;
	}

	public boolean save_decks_to_jedi_db() {
		return (cb_save_decks_to_jedi_db != null) ? cb_save_decks_to_jedi_db.isSelected() : false;
	}
	

	/*public boolean climb_all() {
		return (cb_climb_all != null) ? cb_climb_all.isSelected() : false;
	}

	public boolean fund_all() {
		return (cb_fund_all != null) ? cb_fund_all.isSelected() : false;
	}*/

	public boolean tuo_out() {
		return (cb_tuo_out != null) ? cb_tuo_out.isSelected() : false;
	}
	
	public boolean lse_out() {
		return (cb_lse_out != null) ? cb_lse_out.isSelected() : false;
	}
	
	public static enum OS {LINUX,WINDOWS};
	 
	public OS os = OS.WINDOWS;
	
	private static OS detectOS() {
		//detect os
		String OpS = System.getProperty("os.name").toLowerCase();
		
		if (OpS.indexOf("win") >= 0) {
			return OS.WINDOWS;
			//System.out.println("This is Windows");
		} else if (OpS.indexOf("mac") >= 0) {
			return OS.LINUX;
			//System.out.println("This is Mac");
		} else if (OpS.indexOf("nix") >= 0 || OpS.indexOf("nux") >= 0 || OpS.indexOf("aix") > 0 ) {
			return OS.LINUX;
			//System.out.println("This is Unix or Linux");
		} else if (OpS.indexOf("sunos") >= 0) {
			return OS.LINUX;
			//System.out.println("This is Solaris");
		} else {
			return OS.WINDOWS;
			//System.out.println("Your OS is not support!! => Falling back to Windows");
		}
	}
	
	public String tuo = "tuo";

	public String VERSION = "Version not set";
	public String TUO_VERSION = null;
	
	
	
	public static String getManifestInfo() {
	    Enumeration resEnum;
	    try {
	        resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
	        while (resEnum.hasMoreElements()) {
	            try {
	                URL url = (URL)resEnum.nextElement();
	                InputStream is = url.openStream();
	                if (is != null) {
	                    Manifest manifest = new Manifest(is);
	                    Attributes mainAttribs = manifest.getMainAttributes();
	                    String version = mainAttribs.getValue("Implementation-Version");
	                    if(version != null) {
	                        return version;
	                    }
	                }
	            }
	            catch (Exception e) {
	                // Silently ignore wrong manifests on classpath?
	            }
	        }
	    } catch (IOException e1) {
	        // Silently ignore wrong manifests on classpath?
	    }
	    return null; 
	}
	
	public void detectVersions() {
		//TUM VERSION
		VERSION = getManifestInfo();
		if(VERSION==null)VERSION="DirtyDebug";
		
		//TUO VERSION
		try {
			ProcessBuilder builder = new ProcessBuilder((os==OS.WINDOWS?"":"./") + tuo, "-version");
			final Process process = builder.start();
		    InputStream is = process.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(isr);
		    TUO_VERSION = br.readLine();
		}catch(Exception e)
		{
			e.printStackTrace(); //no tuo
		}
		if(TUO_VERSION==null)TUO_VERSION="NO TUO";
	}

	public String name() {return "TUM - " + VERSION;}
	
	public boolean PULL_GUILD = false;
	
	public boolean PROCESS_WINDOWS = true;
	
	public boolean DEBUG_LOGGING = false;
	
	public boolean ASSERT = false;
	
	public boolean AUTO_UPDATE = false;
	
	public boolean UPDATE_MSG = true;

	public boolean A_H = false;
	
	public boolean IS_FULL_FEATURED = false;
	
	public String[] permissions = new String[] {};
	

	//public final String default_name = "tuplayertu";

	// public int thread_num_max = 10;
	//public static int thread_num = 0;

	public SettingsPanel() {
		/*
		 * frame = new JFrame(); frame.setTitle( "TUM - v3.00" );
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		 * frame.setResizable(true);
		 */

		super();	
		
		
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		// JScrollPane scrPane = new JScrollPane(panel);

		// JTabbedPane tabbedPane = new JTabbedPane();
		// tabbedPane.addTab("Settings", null, scrPane, "Adjust Settings");

		// frame.add(tabbedPane);

		// panel.add(text("TUO settings"));

		JPanel p = new JPanel();
		p.add(GUI.textSmall("TUM Settings"));
		panel.add(p);

		panel.add(new JSeparator());

		/*
		 * p = new JPanel(); p.add(text("cookies folder")); p.add(cookies_folder
		 * = textEdit(cookies_folder())); this.add(p);
		 */

		p = new JPanel();
		p.add(GUI.textSmall("enemy deck"));
		p.add(enemy_deck = GUI.textEdit(enemy_deck()));
		panel.add(p);

		panel.add(new JSeparator());

		/*
		 * p = new JPanel(); p.add(text("Keep sim count low"));
		 * p.add(text("Otherwise really slow")); panel.add(p);
		 */
		

		p = new JPanel();
		p.setLayout(new SpringLayout());
		p.add(GUI.textSmall("sim iterations"));
		p.add(sim_iter = GUI.numericEdit(sim_iter()));

		p.add(GUI.textSmall("reorder iterations"));
		p.add(reorder_iter = GUI.numericEdit(reorder_iter()));

		p.add(GUI.textSmall("climb iterations"));
		p.add(climb_iter = GUI.numericEdit((int) climb_iter()[0]));
		
		p.add(GUI.textSmall("lse iterations"));
		p.add(lse_iter = GUI.numericEdit(lse_iter()));


		p.add(GUI.textSmall("threads"));
		p.add(threads = GUI.numericEdit(threads()));
		
		GUI.makeCompactGrid(p,
                5, 2, 		//rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		panel.add(p);

		JPanel lp = new JPanel();
		lp.add(cb_tuo_out = GUI.check("Display TUO Output(much)?",tuo_out()));
		lp.add(cb_lse_out = GUI.check("Display LSE scores(slow)?",lse_out()));
		lp.add(cb_force_full = GUI.check("Only 10-card decks", force_full_deck()));
		panel.add(lp);

		panel.add(new JSeparator());

		p = new JPanel();
		p.add(cb_sim_sim = GUI.check("Sim possible Cards?",sim_options()));
		panel.add(p);

		p = new JPanel();
		p.add(cb_sim_quad = GUI.check("Show possible Quads?",sim_quad()));
		p.add(cb_sim_dual = GUI.check("Show possible Duals?",sim_dual()));
		p.add(cb_sim_single = GUI.check("Show possible Singles?",sim_single()));
		panel.add(p);

		panel.add(new JSeparator());

		p = new JPanel();
		p.setLayout(new SpringLayout());
		p.add(GUI.textSmall("battleground effect"));
		p.add(bge = GUI.textEdit(bge()));

		p.add(GUI.textSmall("your fort"));
		p.add(yf = GUI.textEdit(yf()));

		p.add(GUI.textSmall("your BGE"));
		p.add(ye = GUI.textEdit(ye()));

		p.add(GUI.textSmall("enemy fort"));
		p.add(ef = GUI.textEdit(ef()));

		p.add(GUI.textSmall("enemy BGE"));
		p.add(ee = GUI.textEdit(ee()));
		
		
		p.add(GUI.textSmall("mode (pvp/pvp-defense)"));
		
		int index =0;
		for(Mode m : Param.Mode.values())
		{
			if(m.equals(mode()))break;
			index++;
		}
		modeList = new JComboBox<Mode>(Param.Mode.values());
		modeList.setSelectedIndex(index);
		p.add(modeList);
		

		p.add(GUI.textSmall("endgame"));
		index =0;
		for(Endgame m : Param.Endgame.values())
		{
			if(m.equals(endgame()))break;
			index++;
		}
		egList = new JComboBox<Endgame>(Param.Endgame.values());
		egList.setSelectedIndex(index);
		p.add(egList);
		
		p.add(GUI.textSmall("dom"));
		index =0;
		for(Dom m : Param.Dom.values())
		{
			if(m.equals(dom()))break;
			index++;
		}
		domList = new JComboBox<Dom>(Param.Dom.values());
		domList.setSelectedIndex(index);
		p.add(domList);

		p.add(GUI.textSmall("order"));
		index =0;
		for(Order m : Param.Order.values())
		{
			if(m.equals(order()))break;
			index++;
		}
		orderList = new JComboBox<Order>(Param.Order.values());
		orderList.setSelectedIndex(index);
		p.add(orderList);
		p.add(GUI.textSmall("flags"));
		p.add(flags = GUI.textEdit(flags()));
		GUI.makeCompactGrid(p,
                10, 2, 		//rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		panel.add(p);
		
		p = new JPanel();
		p.setLayout(new SpringLayout());
		p.add(GUI.textSmall("climb-algorithm"));
		index =0;
		for(OP m : Param.OP.valuesClimb())
		{
			if(m.equals(climb_algorithm()))break;
			index++;
		}
		climbList = new JComboBox<OP>(Param.OP.valuesClimb());
		climbList.setSelectedIndex(index);
		p.add(climbList);
		
		p.add(GUI.textSmall("temperature/start-iterations"));
		p.add(temperature = GUI.numericEdit(temperature()));
		
		p.add(GUI.textSmall("100k*temperature-decrease"));
		p.add(temperature_decrease = GUI.numericEdit((int)(temperature_decrease()*100000)));
		
		GUI.makeCompactGrid(p,
                1, 6, 		//rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		panel.add(p);
		
		panel.add(new JSeparator());
		
		JPanel lsep = new JPanel();
		//p.add(text("Use LSE for Botting"));
		lsep.add(cb_use_lse = GUI.check("Use LSE for Botting",use_lse()));
		panel.add(lsep);
		
		JPanel lsetp = new JPanel();
		lsetp.add(GUI.textSmall("LSE turns"));
		lsetp.add(lse_turns = GUI.numericEdit(lse_turns()));
		panel.add(lsetp);
		
		panel.add(new JSeparator());
		
		JPanel jedip = new JPanel();
		//p.add(text("Use LSE for Botting"));
		jedip.add(cb_save_decks_to_jedi_db= GUI.check("Save Enemy decks to Jedi DB",save_decks_to_jedi_db()));
		panel.add(jedip);

		panel.add(new JSeparator());


		

		p = new JPanel();
		p.add(GUI.textSmall("Display only these fusions"));
		p.add(fuse_options = GUI.textEdit(fuse_options()));
		panel.add(p);


		panel.add(new JSeparator());
		p = new JPanel();
		p.add(GUI.buttonSync("Full Update", () -> TUM.update()));
		p.add(GUI.buttonSync("TUM Update", () -> TUM.updateTUM()));
		p.add(GUI.buttonSync("TUO Update", () -> {TUM.updateTUO();Task.sleepForAll();}));
		p.add(GUI.buttonSync("XML Update", () -> {
			TUM.updateXML();
	        Task.sleepForAll();
	        GlobalData.init();
	    }));
		panel.add(p);
		panel.add(new JSeparator());
		
		JScrollPane scrollPane = new JScrollPane(panel);

		

		os = detectOS();

		//---------Load default settings
		String config_json=null;
		try {
		config_json = GlobalData.readFile("config.json").replaceAll("\n", "");
		}catch(Exception e){}
		if(config_json != null && !config_json.equals(""))
		{
			JSONObject conf = new JSONObject(config_json);
			JSONArray arr_guilds = conf.optJSONArray("guilds");
			if(arr_guilds != null)guilds = arr_guilds.toList().toArray(new String[0]);
			JSONArray arr_permissions = conf.optJSONArray("permissions");
			if(arr_permissions != null)permissions = arr_permissions.toList().toArray(new String[0]);
			flags.setText(conf.optString("flags",flags()));
			bge.setText(conf.optString("bge",bge()));
			egList.setSelectedItem(Endgame.get(conf.optInt("endgame", endgame().toInt())));	
			climbList.setSelectedItem(OP.get(conf.optString("climb-algorithm", climb_algorithm().toString())));
			orderList.setSelectedItem(Order.get(conf.optString("order", order().toString())));
			domList.setSelectedItem(Dom.get(conf.optString("dom", dom().toString())));
			modeList.setSelectedItem(Mode.get(conf.optString("mode", mode().toString())));
			
			enemy_deck.setText(conf.optString("enemy_deck",enemy_deck()));
			yf.setText(conf.optString("yf",yf()));
			ef.setText(conf.optString("ef",ef()));
			ee.setText(conf.optString("ee",ee()));
			ye.setText(conf.optString("ye",ye()));
			
			setCookiesFolder(conf.optString("cookie_folder", cookies_folder()));
			fuse_options.setText(conf.optString("fuse_options", fuse_options()));
			salvage_all = conf.optString("salvage_all", salvage_all);
			salvage_to = conf.optString("salvage_to", salvage_to);
			fuse_options.setText(conf.optString("fuse_options", fuse_options()));
			
			cb_tuo_out.setSelected(conf.optBoolean("tuo_output",tuo_out()));
			cb_lse_out.setSelected(conf.optBoolean("lse_output",lse_out()));
			
			cb_sim_sim.setSelected(conf.optBoolean("sim_options", sim_options()));
			cb_sim_quad.setSelected(conf.optBoolean("sim_quad", sim_quad()));
			cb_sim_dual.setSelected(conf.optBoolean("sim_dual", sim_dual()));
			cb_sim_single.setSelected(conf.optBoolean("sim_single", sim_single()));
			
			temperature.setValue(conf.optInt("temperature",temperature()));
			temperature_decrease.setValue(conf.optInt("temperature_decrease",(int)(temperature_decrease()*10000)));
		
			threads.setNumber(conf.optInt("threads",threads()));
			cb_save_decks_to_jedi_db.setSelected(conf.optBoolean("save_deck_to_jedi_db",save_decks_to_jedi_db()));
			cb_use_lse.setSelected(conf.optBoolean("use_lse",use_lse()));
		
			sim_iter.setNumber(conf.optInt("sim_iter",sim_iter()));
			climb_iter.setNumber(conf.optInt("climb_iter",(int)climb_iter()[0]));
			reorder_iter.setNumber(conf.optInt("reorder_iter",reorder_iter()));
			lse_iter.setNumber(conf.optInt("lse_iter",lse_iter()));
			lse_turns.setNumber(conf.optInt("lse_turns",lse_turns()));
			
			cb_force_full.setSelected(conf.optBoolean("force_full_deck",force_full_deck()));
			
			A_H = conf.optBoolean("a_h",A_H);
			ASSERT = conf.optBoolean("assert",ASSERT);
			DEBUG_LOGGING = conf.optBoolean("debug",DEBUG_LOGGING);	
			PROCESS_WINDOWS = conf.optBoolean("process_windows", PROCESS_WINDOWS);
			AUTO_UPDATE = conf.optBoolean("auto_update", AUTO_UPDATE);
			UPDATE_MSG = conf.optBoolean("update_msg", UPDATE_MSG);
			PULL_GUILD = conf.optBoolean("pull_guild",PULL_GUILD);
			IS_FULL_FEATURED = conf.optBoolean("full_featured",IS_FULL_FEATURED);
			tuo = conf.optString("tuo",tuo + (os==OS.WINDOWS?".exe":""));
		}
		if(!Permissions.has(this,"settings_lse"))
		{
			panel.remove(lsep);
			panel.remove(lsetp);
			lp.remove(cb_lse_out);
		}
		if(!Permissions.has(this,"settings_jedi"))
		{
			panel.remove(jedip);
		}
		//os = detectOS();
		detectVersions();
		System.out.println(name());
		System.out.println(TUO_VERSION);
		
        this.add(scrollPane);
	}

	
	public void setCookiesFolder(String n)
	{
		cookies_folder = n;
	}

	/*public static synchronized void incThreads(int i) {
		thread_num += i;
		System.out.println("Cur Threads: " + thread_num);
	}

	public static synchronized int getThreads() {
		return thread_num;
	}*/

}
