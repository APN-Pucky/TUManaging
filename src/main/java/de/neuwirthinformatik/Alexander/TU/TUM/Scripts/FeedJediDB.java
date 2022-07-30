package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;


import java.util.ArrayList;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Mode;

public class FeedJediDB {
	
	public static void main(String[] args) throws Exception
	{
			TUM.settings.setCookiesFolder("cookies");
			String search = "ImmortalDYN";
			ArrayList<Card[]> all_ret = new ArrayList<Card[]>();
			
			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];
			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				Bot b = bots[i];
				if(b.getStamina() > 0)
				{
					b.checkPreFight();
					Object[][] oo =  b.getCurl().getHuntingTargets();
					for(Object[] o : oo)
					{
						if(((String)o[2])==search)
						{
							b.getCurl().startHuntingBattle((int) o[0]);
							b.winFight(Mode.pvp, null);
							b.saveJSONDeck("Battle");
						}
					}	
					b.update();
				}
			}
	}
}

