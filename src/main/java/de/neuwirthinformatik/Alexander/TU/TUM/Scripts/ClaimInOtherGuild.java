package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;

public class ClaimInOtherGuild {
	public static void main(String[]args)
	{
		//return;
		TUM.settings.setCookiesFolder("cookies_all");
		Bot off = new Bot("DR_F3LL");
		
		String[] bns = new String[]{"MrDoolittle"};//Data.getBotList();
		Bot[] bots = new Bot[bns.length];
		
		for(int i = 0; i < bns.length;i++)
		{
			System.out.println("%%%: " + (i*100)/bns.length + "% [" + bns[i] + "]");
			bots[i] = new Bot(bns[i]);
			
			if(off.getUserID() != bots[i].getUserID())
			{
				int[] data = bots[i].getBrawlData();
				if(data[2] >= 7200 && data[5] != 1)
				{
					System.out.println(bots[i].getName() + " moved");
					bots[i].leaveGuild();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					off.sendGuildInvite(bots[i].getUserID());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bots[i].acceptGuildInvite(off.getGuildID());
					
					bots[i].claimBrawlRewards();
					bots[i].leaveGuild();
				}
			}
		}
		
		/**
		  deasy54
		  doughthehug
		  GizMode
		 	mdalessio999 moved
merciless91 moved
MercReborn moved
onecrableg moved
rany 02
rosen garten
satcombowen moved
		 */
		
	}
}
