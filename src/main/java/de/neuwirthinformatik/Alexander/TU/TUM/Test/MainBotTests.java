package de.neuwirthinformatik.Alexander.TU.TUM.Test;

import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;

public class MainBotTests 
{
	public static void main(String[] args) throws Exception
	{
		Bot b =   new Bot("playertuplayer");
		
		String j = b.getCurl().curl_pull("getBattleResults", "");
		System.out.println(j);
	}
}
