package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class SetAHAll {
	public static void main(String[] args) throws Exception
	{
			//TUM.settings.setCookiesFolder("cookies_merc");
			TUM.settings.A_H = false;
			TUM.settings.PULL_GUILD = false;
			GlobalData.init();
			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];
			//while(true){
			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				Bot b = bots[i];
				
				if(!b.getGuild().equals("TrveKvlt") && !b.getGuild().equals("Nihilists")&& !b.getGuild().equals("LizardSquad")/* && !b.getGui ld().equals("MonoOnly")*/)continue;
				
				b.getCurl().setActiveDeck(2);
				b.update();
				System.out.println("pre " + GlobalData.getDeckString(b.getOffDeck()));
				GlobalData.getNameAndLevelByID(b.getDefDeck()[1]);
				//b.setCurlDecks("Cyrus-1," + Data.getNameByID(b.getDefDeck()[1]) + ",Hurkol Bloodvessel", "Cyrus-1," + Data.getNameByID(b.getDefDeck()[1]) + ",Hurkol Bloodvessel", 1, 1);
				b.setCurlDecks(GlobalData.getDeckString(b.getDefDeck()),GlobalData.getDeckString(b.getDefDeck()), b.getOffDeckID(), b.getDefDeckID());
				b.updateData();
				b.updateDeck();
				System.out.println("post " + GlobalData.getDeckString(b.getOffDeck()));
				//Data.appendLine("CoupBrawlData.csv", );
			}
			System.out.println("wait");
			Thread.sleep(60000*15);
			System.out.println("wait end");
			//}
			
	}
}

