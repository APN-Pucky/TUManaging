package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class UnlockCommanders {
	public static void main(String[] args) throws Exception
	{
			TUM.settings.setCookiesFolder("cookies_mono");
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
				if(!b.getGuild().equals("MonoOnly"))continue;
				for(String s : new String[] {"Barracus-1","Petrisis-1", "Alaric-1", "Tabitha-1", "Halcyon-1", "Nexor-1", "Krellus-1", "Constantine-1", "Dracorex-1", "Yurich-1"})
				{
					CardInstance c = GlobalData.getCardInstanceByNameAndLevel(s);
					b.getCurl().curl_pull("unlockCommander", "&commander_id="+c.getID());
					System.out.println("MO " + s + " " + c.getID() );
				}
				
			}
			System.out.println("wait");
			Thread.sleep(1);
			System.out.println("wait end");
			//}
			
	}
}
