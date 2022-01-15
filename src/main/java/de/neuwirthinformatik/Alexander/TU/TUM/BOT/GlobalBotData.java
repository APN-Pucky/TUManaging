package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;

public class GlobalBotData {

	public static Bot[] bots = new Bot[]{}; 
	
	public static Bot getBotByName(String n)
	{
		for(Bot b : GlobalBotData.bots)
		{
			if(b !=null && b.getName().equals(n))return b;
		}
		return null;
	}
	
	public static Bot[] getGuildLeaders()
	{
		ArrayList<Bot> l = new ArrayList<Bot>();
		for(Bot b : bots)
		{
			if(b != null && b.isLeader())l.add(b);
		}
		return l.toArray(new Bot[]{});
	}
	

	
	public static String[] getLeadGuildNames()
	{
		Bot[] ls  = getGuildLeaders();
		String[] r = new String[ls.length];
		for(int i =0;i < ls.length;i++)
		{
			r[i] = ls[i].getGuild();
		}
		return r;
	}
	
	public static Bot[] getGuildOfficers()
	{
		ArrayList<Bot> l = new ArrayList<Bot>();
		for(Bot b : bots)
		{
			if( b != null && (b.isLeader() || b.isOfficer()))l.add(b);
		}
		return l.toArray(new Bot[]{});
	}
	
	public static String[] getInviteGuildNames()
	{
		Bot[] ls  = getGuildOfficers();
		//String[] r = new String[ls.length];
		List<String> al = new ArrayList<>();
		for(int i =0;i < ls.length;i++)
		{
			al.add(ls[i].getGuild());
			//r[i] = ls[i].getGuild();
		}
		Set<String> hs = new HashSet<>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		return hs.toArray(new String[]{});
	}
	
	
	public static int getNullCount(Bot[] bs)
	{
		int c = 0;
		for(Bot i : bs)if(i == null)c++;
		return c;
	}
	
	public static void saveGauntlet(Bot b, int[] ids)
	{
		String gaunt_name = "gauntlet_" + b.getGuild() + "_" + b.getName();
		GlobalData.saveGauntlet(gaunt_name,ids);
	}
	
	public static void saveOwnedCards(Bot b, int[] ids)
	{
		synchronized(b.getInventory())
		{	
			try
			{
				File o = new File("data/ownedcards_" + b.getName() + ".txt");
				File o_tmp = new File("data/ownedcards_" + b.getName() + "_tmp.txt");
				
				String inv = GlobalData.getInvString(ids);
		
				o.delete();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(o_tmp));
		
				writer.write(inv.replace("\n", System.getProperty("line.separator")));
				
				writer.close(); 
				
				o_tmp.renameTo(o);		
				TUM.log.d("Saved Ownedcards", b.getName());
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static String getCookie(String name)
	{
		return getCookie(name,TUM.settings.cookies_folder()+"/");
		/*//System.out.println("Loading Cookie: " + name);
		String text = "";
		try 
		{
			BufferedReader brTest = new BufferedReader(new FileReader(TUM.settings.cookies_folder()+"/cookie_" +name));		    
			text = brTest .readLine();
		    brTest.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Loaded Cookie: " + name);

		text = text.replaceAll("&", ";");
	    return text;*/
	}
	
	public static String getCookie(String name,String folder)
	{
		//System.out.println("Loading Cookie: " + name);
		String text = "";
		try 
		{
			BufferedReader brTest = new BufferedReader(new FileReader(folder+"cookie_" +name));		    
			text = brTest .readLine();
		    brTest.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Loaded Cookie: " + name);
		text = text.replaceAll("&", ";");
	    return text;
	}
	
	public static String[] getBotList()
	{
		File folder = new File(TUM.settings.cookies_folder());
		
		File[] listOfFiles = folder.listFiles();
		String[] ret = new String[listOfFiles.length];
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				ret[i] = listOfFiles[i].getName().split("cookie_")[1];
			} 
		}
		return ret;
	}
	
	public static String[] getBotList(String sfolder)
	{
		File folder = new File(sfolder);
		
		File[] listOfFiles = folder.listFiles();
		String[] ret = new String[listOfFiles.length];
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				ret[i] = listOfFiles[i].getName().split("cookie_")[1];
			} 
		}
		return ret;
	}
	

	
}
