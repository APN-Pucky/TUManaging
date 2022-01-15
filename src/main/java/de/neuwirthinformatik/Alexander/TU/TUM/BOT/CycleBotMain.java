package de.neuwirthinformatik.Alexander.TU.TUM.BOT;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;

public class CycleBotMain {
	public static void main(String[] args) {
		//simple();
		if(args[0].equals("simple"))simple();else
		while(true)
		{
			String[] bns = GlobalData.getBotList();
			for(String s : bns)
			{
				String[] args2 = new String[args.length+5-1];
				for(int i = 1;i < args.length;i++)
				{
					args2[i-1] = args[i];
				}
				args2[args2.length-5] = "-c";
				args2[args2.length-4] = s;
				args2[args2.length-3] = "-o";
				args2[args2.length-2] = "-ss";
				args2[args2.length-1] = "0";
				StartBotMain.main(args2);
			}
		}
	}
	
	static void simple()
	{
		System.out.println("SimpleCycleMode");
		String[] bns = GlobalData.getBotList();
		Bot[] bots = new Bot[bns.length];
		while (true) {
			System.out.println("Next DUMP");
			for (int i = 0; i < bns.length; i++) {
				System.out.println("DUMP: " + (i * 100) / bns.length + "%" + "[" + bns[i] + "]");
				bots[i] = new Bot(bns[i]);
				// bots[i].setCurlDeck(bots[i].getDeck());
				System.out.println("STAMINA: " + bots[i].getStamina());
				bots[i].dumpStamina();
				System.out.println("AFTER STAMINA: " + bots[i].getStamina());
				if (bots[i].getEnergy() > 200) {
					System.out.println("ENERGY: " + bots[i].getEnergy());
					while (bots[i].getEnergy() > 200) {
						bots[i].fightMission(142,-1);
						bots[i].updateData();
					}
					System.out.println("AFTER ENERGY: " + bots[i].getEnergy());
				}
				// bots[i].getCurl().claimFactionWarReward();
			}
			// Thread.sleep(1000*60*50);//100 min * 0.5
		}
	}
	
	
}
