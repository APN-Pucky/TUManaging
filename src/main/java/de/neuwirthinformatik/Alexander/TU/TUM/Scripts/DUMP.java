package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import java.util.ArrayList;

import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.JediDeckGrab;

public class DUMP {
	

	public static int thread_num = 0;
	
	public static void main(String[] args) throws Exception
	{
			//ViewSettings.cookies_folder  = new JTextField();
			//String f= "cookies/";
			//String search = "Nihilists";
			TUM.settings.setCookiesFolder("cookies_save");
			ArrayList<Card[]> all_ret = new ArrayList<Card[]>();
			
			String[] guilds = new String[] { 
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
					  "RIPsquad", "OVERTHETOP", "ApsychoAAkillerA",
					  "SupremeBairs"
					  };
			for (String g : guilds) {
				JediDeckGrab.loadGuild(g,"Fortification");
			}
			
			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];
			while(true){
			for(int i = 0*bns.length/100; i < 100*bns.length/100;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				if(bots[i]==null)bots[i] = new Bot(bns[i]);
				Bot b = bots[i];
				//if(b.getGuild().equals("LizardSquad"))continue;
				//if(b.getGuild().equals("Nihilists"))continue;
				//if(b.getGuild().equals("TrveKvlt"))continue;
				
				while(getThreads()>=TUM.settings.threads())
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				incThreads(1);
				(new Thread(new Runnable(){ public void run(){
				
				
				String json = "";
				try {
					//json = b.getCurl().curl_pull("updateFactionWar", "");
					json = b.getCurl().curl_pull("getHuntingTargets", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject o = new JSONObject(json);
				//o = o.getJSONObject("faction_war");
				o = o.getJSONObject("player_brawl_data");
				//System.out.println(json);
				
				//System.out.println("points#rank: " + o.getInt("points") +"#" + o.getInt("current_rank"));
				//System.out.println("W/R: " +  o.getInt("wins") +"/" + o.getInt("losses"));
				
				o=o.getJSONObject("energy");
				int stam =0;
				try{
				if(o.has("battle_energy"))stam = Integer.parseInt(o.getString("battle_energy"));
				}catch(Exception e1) {try{stam = o.getInt("battle_energy");}catch(Exception e2){stam=0;}}
				
				try {
					//if(!ViewSettings.cookies_folder().equals("cookies/"))b.getCurl().setActiveDeck(2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b.update();
				
				System.out.println("Stam: " + stam);
				
				/*if(b.getEnergy() > 125)
				{
					System.out.println("BEFORE ENERGY: " + b.getEnergy());
					//b.fightMission(142);
					b.dumpCampaign(25,1);
					b.updateData();
					System.out.println("AFTER ENERGY: " + b.getEnergy());	
				}
				if(b.getStamina() > 10)
				{
					while(b.getStamina() > 0)
					{
						b.dumpStamina();
						b.update();
					}
				}*/
				if(stam>0)
				//if(stam >10) // 25,22,20,15,10,5,0  //while(stam>0)
				{
					
					
					stam--;
					
					//b.fightWarBattle(1);
					b.fightBrawlBattle();
				}

				incThreads(-1);
				try {
					//if(!ViewSettings.cookies_folder().equals("cookies/"))b.getCurl().setActiveDeck(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}})).start();
				/*
				Thread.sleep(1000);
				if(stam == 24 || stam == 25)
				{
					System.out.println("ALARM!!!!!!!!!!");
					try {
						b.getCurl().fightBrawlBattle();
						b.getCurl().autoSkipFight();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			}
			
			while(getThreads()>0){
				//if(Math.random()>0.99)System.out.println("WAITING 444");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(int i =0; i < 10;i++)
			{
				System.out.println(10-i + " Seconds left until next dump");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			}
	}
	
	private static int s2i(String s) {
		return Integer.parseInt(s);
	}
	
	public static String get_last(String j, String s) {
		j = j.replaceAll(".*\"" + s + "\":", "");
		j = j.replaceAll(",.*", "");
		j = j.replaceAll("\"", "");
		j = j.replaceAll("\\{", "");
		j = j.replaceAll("}", "");
		return j;
	}
	

	
	public static synchronized void incThreads(int i)
	{
		thread_num += i;
		System.out.println("Cur Threads: " + thread_num);
	}
	
	public static synchronized int getThreads()
	{
		return thread_num;
	}
	
}
