package de.neuwirthinformatik.Alexander.TU.TUM.Test;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;

public class CardTest {
	public static void main(String[] args)
	{
		GlobalData.init();
		CardInstance test = GlobalData.getCardInstanceByNameAndLevel("Bulkhead Brawler-1");
		System.out.println(GlobalData.getCardInstanceById(60586).getLevel());
		System.out.println(GlobalData.getCardInstanceById(60586).description());
		System.exit(0);;
		//System.out.println(test.description());
		//System.out.println(Data.getCardInstanceByNameAndLevel("Bulkhead Brawler").description());
	}
}
