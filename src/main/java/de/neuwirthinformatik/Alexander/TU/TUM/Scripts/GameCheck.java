package de.neuwirthinformatik.Alexander.TU.TUM.Scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.neuwirthinformatik.Alexander.TU.TUM.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUM.Log;
import de.neuwirthinformatik.Alexander.TU.TUM.TUM;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Result;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Endgame;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUM.TUO.TUMTUO.Param.Order;

public class GameCheck {
		
	public static void main(String[] args)
	{	
		int iter = 10000;
		double confidence = 1;
		double eps = 0.000001;
		int fac = 1000;
		String csvFile = "appended.txt";
        String line = "";
        String cvsSplitBy = ";";
		GlobalData.init();
		TUM.log = new Log.NoLog();
		ArrayList<Integer> o_ids = new ArrayList<Integer>();
		ArrayList<Integer> e_ids = new ArrayList<Integer>();
		ArrayList<Integer> g_o_ids = new ArrayList<Integer>();
		ArrayList<Integer> g_e_ids = new ArrayList<Integer>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] cells= line.split(cvsSplitBy);

                int win = Integer.parseInt(cells[0]);
                String mode = cells[1];
                String own_deck = cells[2];
                String enemy_deck = cells[3];
                String g_bge = cells[4];
                String ye = cells[5];
                String ee = cells[6];
                Result r = TUMTUO.sim(new Param(own_deck,enemy_deck,OP.sim,Order.exact_ordered,Mode.get(mode),Dom.dom_owned,"","",ye,ee,g_bge,new double[] {iter},0,Endgame.none,"enemy:exact-ordered")); 
                if(win == 1 && r.won(Mode.get(mode)) <confidence || win == 0 && r.lost(Mode.get(mode)) <confidence)
                {	
                	if(r.won(Mode.get(mode))<eps || r.lost(Mode.get(mode))<eps)
                		System.out.println("!!!!!!!!!!!!!!!!!!");
                	System.out.println(line);
                	System.out.println("("+  r.WINS + "," + r.STALLS + "," + r.LOSSES + ")");
                	for(int i : GlobalData.constructDeckArray(own_deck))
                		o_ids.add(i);
                	for (int i : GlobalData.constructDeckArray(enemy_deck))
                		e_ids.add(i);
                }
                for(int i : GlobalData.constructDeckArray(own_deck))
            		g_o_ids.add(i);
            	for (int i : GlobalData.constructDeckArray(enemy_deck))
            		g_e_ids.add(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("My cards:");
        Map<Integer, Long> result = sortByValue(count(o_ids));
        final Map<Integer, Long> g_result = count(g_o_ids);
        result.forEach((item, value) -> System.out.println(GlobalData.getNameAndLevelByID(item) + " - " + ((double)value)/g_result.get(item)*fac));

        System.out.println("");
        System.out.println("Enemy cards:");
        result = sortByValue(count(e_ids));
        final Map<Integer, Long> g_result2 = count(g_e_ids);
        result.forEach((item, value) -> System.out.println(GlobalData.getNameAndLevelByID(item) + " - " + ((double)value)/g_result2.get(item)*fac));

        System.out.println("");
        System.out.println("All cards:");
        o_ids.addAll(e_ids);
        g_o_ids.addAll(g_e_ids);
        result = sortByValue(count(o_ids));
        final Map<Integer, Long> g_result3 = count(g_o_ids);
        result.forEach((item, value) -> System.out.println(GlobalData.getNameAndLevelByID(item) + " - " + ((double)value)/g_result3.get(item)*fac));
        System.out.println("done");
        System.exit(0);
	}
	
	private static Map<Integer, Long>  count(List<Integer> items) {
        Map<Integer, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );
        return result;
    }
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
