package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class BrawlData {
	public static void main(String[] args) throws Exception
	{

			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];
			GlobalData.deleteFile("CoupBrawlData.csv");
			GlobalData.createFile("CoupBrawlData.csv");
			GlobalData.appendLine("CoupBrawlData.csv","Bot"+"," + "Guild" + "," + "Rank" + "," + "Score" +"," + "Wins" +"," + "Losses");  
			for(int i = 0; i < bns.length;i++)
			{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				int[] data = bots[i].getCurl().getBrawlData();
				GlobalData.appendLine("CoupBrawlData.csv",bots[i].getName() + "," +bots[i].getGuild() + "," + data[0]+"," + data[1]+"," + data[2]+"," + data[3]);
				//Data.appendLine("CoupBrawlData.csv", );
			}
	}
}
