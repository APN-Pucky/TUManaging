package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.util.Curl;

public class DeckGrab {
	
	public static ArrayList<Card[]> grab(String[] args) throws Exception
	{
			//ViewSettings.cookies_folder  = new JTextField();
			String f= "cookies_all/";
			//String search = "Nihilists";
			ArrayList<Card[]> all_ret = new ArrayList<Card[]>();
			
			String[] bns = GlobalBotData.getBotList(f);
			Curl[] bots = new Curl[bns.length];
			for(int i = 0; i < bns.length;i++)
			{
				try{
				System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
				bots[i] = new Curl(GlobalBotData.getCookie(bns[i],f));
				Object[][] data = bots[i].getHuntingTargets();
				
				boolean found = false;
				for(int j =0; j < data.length;j++)
				{
					//System.out.println("" + (String)data[j][1]);
					for(String search : args)
					{
						if(((String)data[j][1]).equals(search) || ((String)data[j][2]).equals(search))
						{
							Bot b = new Bot(bns[i],f);
							if(b.getStamina() > 0)
							{
									b.getCurl().startHuntingBattle((int)data[j][0]);
									String resp = b.getCurl().autoSkipFight();
									
									JSONObject obj = new JSONObject(resp);
									JSONObject targets = obj.getJSONObject("battle_data");
									
									int cmd = targets.getInt("defend_commander");
									targets = targets.getJSONObject("card_map");
									//System.out.println(json);
									//System.out.println("Card Count: " +cards.keySet().size());
									int[] ret = new int[targets.keySet().size()+1];
									ret[0] = cmd;
									int k =0;
									for(Iterator iterator = targets.keySet().iterator(); iterator.hasNext();k++)
									{
										String cur = (String) iterator.next();
										if(Integer.parseInt(cur)>100)ret[k+1]=targets.getInt(cur);
									}
									Card[] cs = GlobalData.getCardArrayFromIDArray(ret);
									String deck = GlobalData.getDeckString(ret);
									System.out.println(deck);
									all_ret.add(GlobalData.constructCardArray(deck));
									//for(Card c : cs )System.out.println(c.getName());
									System.out.println();
							}
							System.out.println("" + (String)data[j][1] + " in " +((String)data[j][2]));
						}
					}
				}
				}catch(Exception e){}
				//System.out.println(bns[i] + )
				//Data.appendLine("CoupBrawlData.csv",bots[i].getName() + "," +bots[i].getGuild() + "," + data[0]+"," + data[1]+"," + data[2]+"," + data[3]);
				//Data.appendLine("CoupBrawlData.csv", );
			}
			return all_ret;
	}
	
	public static void main(String[] args) throws Exception
	{
		for(Card[] cs : grab(new String[]{"DireTide","ImmortalDyn", "MasterJedis", "NovaSlayers", "Decepticon"})){
			for(Card c : cs )
				System.out.println(c.getName() + " ,");
			System.out.println();
		}
	}
}
