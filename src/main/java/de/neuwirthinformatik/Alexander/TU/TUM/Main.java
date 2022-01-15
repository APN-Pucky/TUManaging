package de.neuwirthinformatik.Alexander.TU.TUM;

import javax.swing.UIManager;

import de.neuwirthinformatik.Alexander.TU.TUM.BOT.CycleBotMain;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.StartBotMain;

public class Main 
{
	public static void main(String[] args) throws Exception
	{
		
		
		UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
		
		/*String[] guilds = new String[] { 
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
				  "RIPsquad", "OVERTHETOP", "ApsychoAAkillerA"
				  };
		for (String g : guilds) {
			//JediDeckGrab.loadGuild(g,"Fortification");
		}*/
		
		
		if(args.length == 0){
			new TUM();
		}
		else
		{
			if(args[0].equals("cycle"))
				CycleBotMain.main(args);
			else
				StartBotMain.main(args);
		}
		
		//start bot like bash sh bot
		
		
		
		/*
			
		
		String[] bns = Data.getBotList();
			Bot[] bots = new Bot[bns.length];
			int allgold = 0;
			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("Load: " + (i*100)/bns.length + "%");
				bots[i] = new Bot(bns[i]);
				allgold+= bots[i].getMoney();
				//if(bots[i].getCreator().couldCreateCard(Data.getCardByName("Needler Vektarok"), Data.getCardArrayFromIDArray(bots[i].getInventory())))System.out.println("CC: " + bns[i]);
				//if(Data.count(bots[i].getInventory(),Data.nameAndLevelToID("Dominion Shard-2"))>=50)System.out.println("DDDDDDDDDDDDDDDDDDDD: " + bns[i]);
				//if(!Data.getCardByID(bots[i].getDeck()[1]).getName().contains("Alpha")){System.out.println("BUGG_Deck: " + bns[i] );}
				
				final int i_t = i;
				//new Thread(new Runnable(){public void run(){
					//System.out.println("Climbing: " + bns[i_t]);
					//bots[i_t].climbDeck();
					//System.out.println("Climbed: " + bots[i_t].getName() +  " "+ Data.getDeckString(bots[i_t].getDeck()));
				//}}).start();
				//if(i==10)break;
				
			}
			System.out.println("GOLD ON ALL ACCS: " +allgold);
			new ViewBotsMoney(bots);
		
		//System.out.println(data.getCardByName("Primal Monitor").toString());
		
		//System.out.println(data.nameToID("Blitz Plate"));
		//System.out.println(data.getCardByName("Cassius Stalwart").toString());
		
		//System.out.println(data.getCardLevel(47326));
		
		/*curl.displayInv();
		curl.displayDecks();
		for(int i : curl.getDecks())
		{
			if(i!=0)System.out.println(data.idToNameAndLevel(i));
		}
		for(int i : curl.getInv())
		{
			if(i!=0)System.out.println(data.idToNameAndLevel(i));
		}
		//c.upgradeCard(47327);
		//c.upgradeCard(47327);*/
		//System.out.println(new Bot("haxor02").toString());
		/*********for(String s : Data.getBotList()) System.out.println(s);
		
		System.out.println("---------Deck");
		Curl curl = new Curl(data.getCookie("haxor02"));
		Creator creator = new Creator(curl);
		for(int i : curl.getDecks())
		{
			if(i!=0)System.out.print(data.idToNameAndLevel(i) + ", ");
		}
		System.out.println();
		System.out.println(data.getCardByName("Nexus Monitor").toString());
		System.out.println(data.nameAndLevelToID("Nexus Monitor-6"));*******************/
		
		//curl.setDeckCards(new int[]{1192,0,11714,43111,40079}, 1);
		
		//creator.createCard(data.getCardByName("Irvos Mistwalker"));
		
			
		//curl.buy1925();
		/*
		System.out.println("----------Options");
		int[] invs = curl.getInv();
		Card[] inv = new Card[invs.length];
		for(int i = 0; i < invs.length;i++)
		{
			inv[i] = data.getCardByID(invs[i]);
		}
		//System.out.println(creator.couldCreateCard(data.getCardByName("Deranged Malgoth"), inv));
		for(Card c: data.cards)
		{
			if(c.getFusionLevel() == 2 && creator.couldCreateCard(c, inv.clone()))
				System.out.println(c.toString());
		}*/
		
	}
}
