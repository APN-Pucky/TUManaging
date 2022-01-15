package de.neuwirthinformatik.Alexander.TU.TUM.Save;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.util.StreamUtil;

public class JediDeckGrab 
{
	static {
		trustAllHosts();
	}
	
	static HashMap<String,int[]> hm_player_decks = new HashMap<String,int[]>();
	static HashMap<String,String> hm_player_time = new HashMap<String,String>();
	static HashMap<String,String> hm_player_guild = new HashMap<String,String>();
	
	public static boolean manualAddedPlayerLocal(String name, String time)
	{
		return hm_player_time.containsKey(name)?hm_player_time.get(name).equals(time):false;
	}
	
	public static void manualAddPlayerLocal(String name, String guild, int[] deck, String time) //TODO save to JediDB?
	{
		hm_player_guild.put(name,guild);
		hm_player_decks.put(name,deck);	
		hm_player_time.put(name, time);
		TUM.log.d("Loaded " + name  + "[" + guild +"]"+ "@ "+ time + ": " + GlobalData.getDeckString(deck), "JediDB");	
	}
	public static void pushDeck(String guild, String name, int[] deck) {pushDeck(guild,name,deck,"Battle",-1,"",-1,"",-1,"");}
	public static void pushDeck(String guild, String name, int[] deck,String mode, int bge_id, String bge, int ebge_id, String ebge, int ybge_id, String ybge)
	{
		try {
			JSONObject obj = new JSONObject();

			obj.put("name", name);
			obj.put("guild", guild);
			JSONObject tmp_deck = new JSONObject();
			JSONArray cards = new JSONArray();
			for (int i = 1; i < deck.length; i++) {
				if(GlobalData.isFortress(deck[i]))continue;
				JSONObject tmp = new JSONObject();
				tmp.put("name", GlobalData.getNameAndLevelByID(deck[i]));
				tmp.put("card_id", deck[i]);
				cards.put(tmp);
			}
			tmp_deck.put("cards", cards);
			JSONObject tmp = new JSONObject();
			tmp.put("name", GlobalData.getNameAndLevelByID(deck[0]));
			tmp.put("card_id", deck[0]);
			tmp_deck.put("commander", tmp);
			obj.put("deck", tmp_deck);
			obj.put("mode", "Defense");
			obj.put("type", "Battle");
			
			obj.put("bge", new JSONObject(" { \"enemy\": {\"enemy_id\": \"" + (ebge_id == -1 ? "None" : ebge_id)
					+ "\",\"name\": \"" + (ebge.equals("") ? "None" : ebge) + "\"},\"global\": {  \"name\": \""
					+ (bge.equals("") ? "None" : bge) + "\", \"global_id\": \""
					+ (bge_id == -1 ? "None" : bge_id) + "\"   }, \"friendly\": {  \"friendly_id\": \""
					+ (ybge_id == -1 ? "None" : ybge_id) + "\",\"name\": \""
					+ (ybge.equals("") ? "None" : ybge) + "\" }}"));
			obj.put("friendly_structures", "");
			obj.put("enemy_structures", "");
			obj.put("date",TUM.time());

			JediDeckGrab.pushDeckJSON(guild, "Battle", (bge.equals("") ? "None" : bge), obj.toString());
			if (!mode.equals("Battle"))
				JediDeckGrab.pushDeckJSON(guild, mode, (bge.equals("") ? "None" : bge), obj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getGuildName(String name)
	{//alternative TODO check contains
		/*for(String s : hm_player_guild.keySet())
		{
			if(s.equals(name))return hm_player_guild.get(s);
		}
		return null;*/
		return hm_player_guild.get(name);
	}
	
	public static String getTime(String name)
	{
		/*for(String s : hm_player_time.keySet())
		{
			if(s.equals(name))return hm_player_time.get(s);
		}
		return null;*/
		return hm_player_time.get(name);
	}
	
	public static int[] getDeck(String name)
	{
		/*for(String s : hm_player_decks.keySet())
		{
			if(s.equals(name))return hm_player_decks.get(s);
		}
		return null;*/
		return hm_player_decks.get(name);
	}
	
	public static int[] getDeck(String name, String guild)
	{
		int[] ret = hm_player_decks.get(name);
		if(ret != null)return ret;
		/*for(String s : hm_player_decks.keySet())
		{
			if(s.equals(name))return hm_player_decks.get(s);
		}*/
		loadGuild(guild);
		/*for(String s : hm_player_decks.keySet())
		{
			if(s.equals(name))return hm_player_decks.get(s);
		}
		return null;*/
		return hm_player_decks.get(name);
	}
	
	public static void loadGuild(String guild_name)
	{
		loadGuild(guild_name,TUM.settings.bge());
	}
	
	public static void loadGuild(String guild_name, String gbge)
	{
		loadGuild(guild_name,"Battle",gbge);
	}
	
	public static void loadGuild(String guild_name,String btype, String gbge)
	{		
		String decks = getGuildDecks(guild_name,btype, gbge);
		//JSONObject obj = new JSONObject(decks);
		JSONArray arr = new JSONArray(decks);
		String[] ds = new String[arr.length()+1];
		//arr.forEach(arg0);
		int i =0;
		for(Iterator<Object> iterator = arr.iterator(); iterator.hasNext();i++)
		{
			JSONObject cur = (JSONObject) iterator.next();
			if(getDeck(cur.getString("name"))!=null){ds[i+1]="";continue;} //no double 
			/*try {
				if(getTime(cur.getString("name")) != null &&  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'0000Z'").parse(getTime(cur.getString("name"))).after( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'0000Z'").parse( cur.getString("date")))){ds[i+1]="";System.out.println("older duplicate-> skipping " +cur.getString("name") + " in " + guild_name );continue;} //only latest
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}*/
			JSONObject deck = cur.getJSONObject("deck");
			int commander_id = deck.getJSONObject("commander").getInt("card_id");
			int[] cards = new int[deck.getJSONArray("cards").length()+1];
			cards[0] = commander_id;
			int j =1;
			for(Iterator<Object> jterator = deck.getJSONArray("cards").iterator(); jterator.hasNext();j++)
			{
				JSONObject c = (JSONObject) jterator.next();
				cards[j] = c.getInt("card_id");
				if(GlobalData.isDominion(cards[j]))
				{
					int tmp = cards[1];
					cards[1] = cards[j];
					cards[j] = tmp;
				}
				if(GlobalData.isFortress(cards[j]))
				{
					cards[j]=0;
				}
			}
			cards = GlobalData.removeDuplicates(cards[1], cards);
			hm_player_guild.put(cur.getString("name"),guild_name);
			hm_player_decks.put(cur.getString("name"), cards);	
			hm_player_time.put(cur.getString("name"), cur.getString("date"));	
			//Data.saveGauntlet(guild_name +"_"+cur.getString("name"),cards);
			String deckstring = GlobalData.getDeckString(cards);
			if(cards[0]==cards[1])deckstring = deckstring.substring(deckstring.indexOf(",")+1);
			ds[i+1] = guild_name +"_"+cur.getString("name")+ ": " + deckstring;
			TUM.log.d("Loaded " + cur.getString("name")  + "[" + guild_name +"]"+ "@ "+ cur.getString("date")  + ": " + deckstring, "JediDB");	
		}
		ds[0] = guild_name+": /^"+guild_name+"_.*$/";
		GlobalData.saveGauntletRaw(guild_name, ds);
	}
	
	public static void pushDeckJSON(String guild_name, String battle_type, String gbge, String json)
	{
		TUM.log.d("POST guild=" + guild_name + "&battle_type=" + battle_type + "&gbge=" + gbge + " => " + json, "JediDB");
		try{
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://jediguild.com/deckslist/?guild=" + guild_name + "&battle_type=" + battle_type + "&format=json" + "&gbge=" + gbge).openConnection()));
		httpcon.setDoOutput(true);
		//HEADERS
		httpcon.setRequestProperty("Host", "jediguild.com");
		httpcon.setRequestProperty("authorization","Token 3275026f19ef9a3efae4047e3794b9c493459b8c");
		httpcon.setRequestProperty("Content-Type","application/json");
		//
		httpcon.setRequestMethod("POST");
		httpcon.connect();
	
		byte[] outputBytes = json.getBytes("UTF-8");
		
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();
		
		
		System.out.println(StreamUtil.readFullyAsString(httpcon.getInputStream(), "UTF-8"));}catch(Exception e){}
	}

	public static String getGuildDecks(String guild_name, String battle_type, String gbge) 
	{
		TUM.log.d("GET guild=" + guild_name + "&battle_type=" + battle_type + "&gbge=" + gbge, "JediDB");
		String json="";
		try{
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://jediguild.com/deckslist/?guild=" + guild_name + "&battle_type=" + battle_type + "&format=json" + "&gbge=" + gbge).openConnection()));
		
		httpcon.setDoOutput(true);
		//HEADERS
		httpcon.setRequestProperty("Host", "jediguild.com");
		httpcon.setRequestProperty("authorization","Token 3275026f19ef9a3efae4047e3794b9c493459b8c");
		httpcon.setRequestProperty("Content-Type","application/json");
		//
		httpcon.setRequestMethod("GET");
		httpcon.connect();
	
		/*byte[] outputBytes = (cookie + add_cookie).replaceAll(";", "&").getBytes("UTF-8");
		
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();*/
		
		
		json = StreamUtil.readFullyAsString(httpcon.getInputStream(), "UTF-8");}catch(Exception e){e.printStackTrace();}
		TUM.log.d("=> " + json, "JediDB");
		return json;
	}
	
    
    public static void trustAllHosts()
    {
    	System.out.println("Disabling SSL Certificate Checks");
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager()
                    {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new  HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
