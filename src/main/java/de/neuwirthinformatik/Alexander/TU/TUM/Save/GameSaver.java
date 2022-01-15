package de.neuwirthinformatik.Alexander.TU.TUM.Save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;

public class GameSaver {
	public static synchronized void pushDeck(boolean is_surge,String mode,String own_com,String own_deck, String own_fort, String enemy_deck,String bge, String ebge, String ybge, int winner,String version , String date)
	{
		if(winner==-1)return;
		if(mode.equalsIgnoreCase("battle") && is_surge)mode = "surge";
		File file = new File("appended.txt");
		FileWriter fr;
		try {
			fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);
			PrintWriter pr = new PrintWriter(br);
			String line = winner +  ";" + mode +";" +own_com + ","+own_deck + own_fort + ";" + enemy_deck + ";" + bge + ";" +ybge + ";" + ebge + ";"+ GlobalData.distinct_cards.length +  ";" + version + ";" + date;
			TUM.log.m(line, "GameSaver");
			pr.println(line);
			pr.close();
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
