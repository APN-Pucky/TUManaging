package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import org.json.JSONArray;
import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;

public class BuyQuadra {
	public static void main(String[] args) throws Exception
	{
			TUM.settings.setCookiesFolder("cookies_TNLSM");
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
				if(!b.getGuild().equals("TrveKvlt") && !b.getGuild().equals("Nihilists")&& !b.getGuild().equals("LizardSquad")&& !b.getGuild().equals("SPQRome")/* && !b.getGuild().equals("MonoOnly")*/)continue;

				JSONObject o = new JSONObject(b.getCurl().curl_pull("buyStorePromoTokens", "&expected_cost=180&item_id=2002&item_type=3"));
				if(o.has("new_cards"))
						{
					JSONArray a = o.getJSONArray("new_cards");
					System.out.println(GlobalData.getCardByID(Integer.parseInt((String)a.get(0))).description());
						}
			}
			System.out.println("wait");
			Thread.sleep(1);
			System.out.println("wait end");
			//}
			
	}
}
