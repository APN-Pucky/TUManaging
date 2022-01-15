package de.neuwirthinformatik.Alexander.TU.TUM.Test;

import java.util.Arrays;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;
import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class CustomTest {
	private static String[][] amazon_coin_urls = new String[][] {{"USA","https://www.amazon.com/Amazon-50-000-Coins/dp/B018HB6E80"},
		{"DE","https://www.amazon.de/dp/B018GWRCV8"}, {"UK","https://www.amazon.co.uk/dp/B018GRDG5O"}, {"FR","https://www.amazon.fr/dp/B018EZT2YM"}
	};
	private static int getAmazonCoinPrice(String url) {
		String web = Wget.wGet(url);
    	String[] lines = web.split("\n");
    	String cur = "";
    	for(String l : lines)
    	{
    		cur = l;
    		if(cur.matches(".*id=\"priceblock_ourprice\".*"))break;
    	}
    	String m = cur.substring(cur.indexOf("\">"), cur.indexOf("</span>"));
    	String value = m.replaceAll(".*(\\d\\d\\d).*", "$1");
    	return Integer.parseInt(value);
	}
	private static String getBGEUrl(String bge) {
		String general = Wget.wGet("https://www.kongregate.com/forums/2468-general/topics/387545-q-a-account-sharing-etiquette-faq-support-player-made-guides");
		general = general.substring(general.indexOf("Global Battleground Effects"), general.indexOf("Restore Information"));
		String[] lines = general.split("\n");
		String fin = "";
		for (String l : lines) {
			if (StringUtil.containsIgnoreSpecial(l,bge)) {
				fin = l;
				break;
			}
		}
		//System.out.println(fin);
		String url = fin.replaceFirst(".*(https?://www\\.kongregate\\.com/forums/2468-general/topics/\\d+).*","$1");
		if(url.matches("https?://www\\.kongregate\\.com/forums/2468-general/topics/\\d+"))
			return url;
		return null;
	}
	
	private static String getInsultShort() {
		String msg = Wget.wGet("http://www.robietherobot.com/insult-generator.htm");
		msg = msg.split("Call them a...")[1].split("<h1>")[1].split("</h1>")[0];
		//msg = msg.replaceAll("\"", "").replaceAll("&quot;", "\"");
		return msg;
	}
	
	
    static public void main( String[] s ) throws Exception{
    	de.neuwirthinformatik.Alexander.TU.TUM.GlobalData.init();
    	//String web = Wget.wGet("https://www.amazon.co.uk/dp/B018GRDG5O");
    	//String web = Wget.wGet("https://www.amazon.de/dp/B018GWRCV8");
    	//String web = Wget.wGet("https://www.amazon.com/Amazon-50-000-Coins/dp/B018HB6E80");
    	GlobalData.getCardInstanceByNameAndLevel("Obsidian Overlord");
    	
    	//System.out.println( getInsultShort().trim().replaceAll("\\s+", " "));
    	/*String msg = "";
		for(String[] a : amazon_coin_urls)
		{
			int price = getAmazonCoinPrice(a[1]);
			msg += a[0] + ":\t " + price + "(" + (100-((double)price)/5) + "%)\n"; 
		}
    	System.out.println(msg);
    	/*String xkcd = Wget.wGet("https://api.imgflip.com/get_memes");
    	  JSONObject json = new JSONObject(xkcd);
    	  JSONArray arr = json.getJSONObject("data").getJSONArray("memes");
    	  Random r = new Random();
    	  String url = arr.getJSONObject(r.nextInt(arr.length())).getString("url");
    	System.out.println(url);*/
    	/*
    	String skill = "cold sleep";
    	
    	String url = getBGEUrl(skill);
    	if(url == null)System.out.println("ERROR");
    	String road = Wget.wGet(url);

		String map = road.substring(road.indexOf("<div class=\"raw_post\""));
		map = map.substring(map.indexOf(">") + 1);
		map = map.substring(0, map.indexOf("</div>"));
		
		map = map.substring(StringUtil.indexOfIgnoreCard(map, skill));
		String ret = "";
		String[] lines = map.split("\n");
		lines[0] = lines[0].replaceAll("\\*+", "");
		for(String l : lines) 
		{
			if(l.contains("will start"))break;
			ret+=l + "\n\n";
		}
		System.out.println(ret);
    	
    	/*
    	String xkcd = Wget.wGet("https://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=" +"afdhiosdapuffdiosajfdioadjfsa" +"&rating=pg-13");
    	  JSONObject json = new JSONObject(xkcd);
    	  if(json.has("data"))System.out.print(true);
    	  JSONObject data = json.optJSONObject("data");
    	  String url = data.getString("image_mp4_url");
    	  String url2 = data.getString("image_url");
    	
    	//String json = Wget.wGet("https://www.reddit.com/r/Art/random.json");
    	//String url = new JSONArray(json).getJSONObject(0).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data").getString("url");
    	System.out.println(url);
    	/*String msg = Wget.sendGet("https://geek-jokes.sameerkumar.website/api");
		System.out.println(msg);
    	msg = Wget.wGet("https://geek-jokes.sameerkumar.website/api");
		System.out.println(msg);
		msg = msg.replaceAll("\"","").replaceAll("&quot;","\"");
    	System.out.println(msg);
    	

		System.out.println("Testing 1 - Send Http GET request");

		String url = "https://geek-jokes.sameerkumar.website/api";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header

		String USER_AGENT = "Mozilla/5.0";
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());*/
    	
    	/*TUM.settings.setCookiesFolder("cookies");
    	Bot b = new Bot("VIZExCleric");
    	System.out.println("UID " + b.getUserID());
    	System.out.println(b.getCurl().curl_pull("getRaidLeaderboard&user_id="+b.getUserID(),""));
    	
    	GUI.createDataTableWindow(b.getRaidLeaderBoard(1),new String[] {"Name",
                "Rank",
                "Stat",
                "Raid Level",
                "Raid Level Rank"}, "Raid Rankings",1);
    	//int[] d=b.getRaidData();
    	//for(int a : d)System.out.println(a + ", ");//*/
    	/*
    	
    	//System.out.println(Data.getSPNeededToMax(Data.getCardInstanceByNameAndLevel("Brood Minion")));
    	
    	for(CardInstance ci : Data.getCardInstanceByNameAndLevel("Hurkol Bloodvessel").getLowestMaterials())System.out.println(ci);
    	
    	//CreatorV2 cr  = new CreatorV2(null);
    	
    	CardInstance[] inv = new CardInstance[]{ Data.getCardInstanceByNameAndLevel("Brood Minion-3"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Ascension Altar-1"),
    			
    			Data.getCardInstanceByNameAndLevel("Octane-1"),
    		};
    	CardInstance[] restore = new CardInstance[]{ Data.getCardInstanceByNameAndLevel("Brood Minion-3"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Neocyte Core-1"),
    			Data.getCardInstanceByNameAndLevel("Halcyon's Regiment-1"),
    			
    			Data.getCardInstanceByNameAndLevel("Octane-1"),
    		};
    	//int p = ;
    	CardInstance goal = Data.getCardInstanceByNameAndLevel("Octane Optimized");
    	//System.out.println(cr.couldCreateCard(goal, inv, restore));
    	//cr.createCard(goal, inv, restore);*/
    	
    	/*for(Card c : Data.distinct_cards)
    	{
    		if(c.category == CardCategory.FORTRESS_SIEGE || c.category == CardCategory.FORTRESS_DEFENSE|| c.category == CardCategory.FORTRESS_CONQUEST)
    		{
    			System.out.println(c);
    		}
    	}
    	//Data.init();
    	//Bot b = new Bot("DARTH_N1H1LUS");
    	//System.out.println(Data.getCardInstanceByNameAndLevel("cassiusatv").description());
    	String general = Wget.wGet("https://www.kongregate.com/forums/2468-general");
    	String[] lines = general.split("\n");
    	String fin = "";
    	for(String l : lines)
    	{
    		if(l.contains("Roadmap"))
    		{
    			fin = l;
    			break;
    		}
    	}
    	String url = "https://www.kongregate.com" + fin.substring(fin.indexOf("href=\"/forums/2468-general")+6, fin.indexOf("\">[Dev]"));
    	
    	String road = Wget.wGet(url);
    	
    	String map = road.substring(road.indexOf("<div class=\"raw_post\""));
    	map = map.substring(map.indexOf(">")+1);
    	map = map.substring(0,map.indexOf("</div>"));
    	String rep="";
    	String[] sections = map.split("\\*\\*");
    	Date min = null;
    	for(int i = 3; i < sections.length; i+=2)
    	{
    		String title = sections[i];
    		String msg = sections[i+1];
			String[] split =  msg.split("\\*");
			String date =split[1];
			String content = split[2];
			for(int j = 3 ; j < split.length;j++)
				content += "*" + split[j];
			content = content.trim();
			//rep += title + "\n";
			//rep += date + "\n\n";
			
			SimpleDateFormat parser = new SimpleDateFormat("yyyyMMMMMMMMM d");
			String[] dates = date.split("-");
			String d1 = dates[0];
			d1 = StringUtil.replaceLast(Calendar.getInstance().get(Calendar.YEAR)+d1.trim(),"(\\d)(st|nd|rd|th)", "$1");
			
	        Date dd1 = parser.parse(d1);
	        

	        if((min==null || dd1.before(min)) && dd1.after(parser.parse("2018August 7")))
	        {
	        	min =dd1;
	        	rep = title + "\n" + date + "\n\n" + content;
	        }
	        //System.out.println(dd1);
			if(dates.length > 1)
			{
				String d2 = dates[1];
				d2 = StringUtil.replaceLast(Calendar.getInstance().get(Calendar.YEAR)+d2.trim(),"(\\d)(st|nd|rd|th)", "$1");
		        Date dd2 = parser.parse(d2);
		        Calendar cal = Calendar.getInstance();
		        cal.setTime(dd2);
		        cal.add(Calendar.DATE, 1);
		        dd2 = cal.getTime();

		        //System.out.println(dd2);
		        //if(Calendar.getInstance().getTime().before(dd2) && Calendar.getInstance().getTime().after(dd1)) System.out.println(title + "\n" + date + "\n\n" + content);
			}
			
    	}
    	
    	System.out.println(rep);
    	
    	
    	/*CreatorV2Return c2r = b.getCreator().couldCreateCard(Data.getCardInstanceByNameAndLevel("Obsidian Overlord"), new CardInstance[] {}, new CardInstance[] {});
    	int cost = c2r.sp_cost;
    	System.out.println("" + cost);
    	for (CardInstance c : c2r.cards) {
        	System.out.println("" + cost);cost += Data.getSPNeededToMax(c.getLowest());}
    	System.out.println("" + cost);
    	
    	String msg = "";
    	int number = 7;
		ArrayList<Card> printed = new ArrayList<Card>();
		for(int i = 1; i < Data.all_cards.length && number > 0; i++)
		{
			Card c = Data.all_cards[Data.all_cards.length-i];
			if(c != null && c.fusion_level == 2 && !printed.contains(c) && !c.getName().toLowerCase().startsWith("test"))
			{
				printed.add(c);
				for(int idi : c.getIDs())msg += idi + ", ";
				msg += "REAL ID: " + (Data.all_cards.length-i) + "\n";
				msg += c.description() + "\n\n";
				number--;
			}
		}
		System.out.println(msg);*/
    	System.exit(0);
    }
}
