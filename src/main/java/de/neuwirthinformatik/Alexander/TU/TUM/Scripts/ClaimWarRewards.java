package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class ClaimWarRewards {
	public static void main(String[] args) throws Exception
	{
		//TUM.settings.startViewSettings();
		//TUM.settings.frame.setVisible(false);
		TUM.settings.setCookiesFolder("cookies_rce");
			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];

			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				int[] ids = bots[i].getInventory();
				//Octane Optimized, Barracus the Traitor, Arkadios Ultimate, Co-operated Crawler, Hurkol Bloodvessel, Irvos Mistwalker, Shockstorm, Tomb-broken, Bunker Builder
				
				/*for(Card c : Data.constructCardArray("Petrisis Gorefist,Octane Optimized, Barracus the Traitor, Arkadios Ultimate, Co-operated Crawler, Hurkol Bloodvessel, Irvos Mistwalker, Shockstorm, Tomb-broken, Bunker Builder"))
						for(int j = 0; j <Data.getCount(Data.getCardArrayFromIDArray(bots[i].getInventory()), c);j++)System.out.println(c.getName());
				
				
				//bots[i].setCurlDeck(bots[i].getDeck());
				/*System.out.println("DUMP STAMINA: " + bots[i].getStamina());
				bots[i].dumpStamina();
				System.out.println("AFTER STAMINA: " + bots[i].getStamina());
				if(bots[i].getEnergy()>125)
				{
					System.out.println("DUMPING CAMP [" + bns[i] + "]");
					System.out.println("ENERGY: "+ bots[i].getEnergy());
					bots[i].dumpCampaign(23, 1);
					bots[i].updateData();
					System.out.println("POST ENERGY: "+ bots[i].getEnergy());
				}	//*/	
				bots[i].getCurl().claimFactionWarRewards();
				//Thread.sleep(1000*60*50);//100 min * 0.5
			}
	}
}
