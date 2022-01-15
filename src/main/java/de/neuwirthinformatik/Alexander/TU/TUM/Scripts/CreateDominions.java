package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class CreateDominions {
	public static void main(String[] args) throws Exception
	{

			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];
			GlobalData.init();
			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				Bot b = bots[i];
				Card c =GlobalData.getCardByName("Alpha Bombard");
				int num = GlobalData.getCount(b.getInventory(), c.getHighestID());
				b.getCreator().createCard(c, b.getInventory(), b.getRestore());
				System.out.println("success: " + (num < GlobalData.getCount(b.getInventory(), c.getHighestID())));
			}
	}
}
