package de.neuwirthinformatik.Alexander.TU.TUM.Test;

import javax.swing.UIManager;

import de.neuwirthinformatik.Alexander.TU.TUM.BotControlPanel;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;

public class Test {
	public static void main(String[] args1) throws Exception
	{
		//Data.encryptFile("TUM.jar", "TUM-enc.jar");
		//Data.decryptFile("TUM-enc.jar", "TUM-dec.jar");
		UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
		new TUM();
		BotControlPanel.waitloop("Wait",1,0);
		//for(int i =0;i  < 1000;i++)TUM.log.m("hihih","hihi");
		/*
		Data.init();
		TUM.settings.setCookiesFolder("cookies");
		Bot b = new Bot("DARTH_N1H1LUS");
		Bot b2 = new Bot("Agent_5mith");
		System.out.println(b.getCurl().getProfileData(b2.getUserID()));
		for(int s : (int[])b.getGuildMemberDeck(b2.getUserID(),true))System.out.println(s);
		/*
		b.getCurl().fightPracticeBattle(b2.getUserID(), false, false);
		b.winFightRandom();

		LSE lse = new LSE(b, false, null);
		lse.update();
		if(lse.winner == 1) System.out.println("WIN");
		else if(lse.winner == 0) System.out.println("LOSS");
		else System.out.println("ERROR");
		
		b.saveJSONDeck("Battle");
		*/
		
	}
}
