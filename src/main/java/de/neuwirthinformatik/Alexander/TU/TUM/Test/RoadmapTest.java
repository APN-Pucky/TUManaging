package de.neuwirthinformatik.Alexander.TU.TUM.Test;

import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class RoadmapTest {
	private static String getRoadMapUrl() {
		String general = Wget.wGet("https://www.kongregate.com/forums/2468-general");
		String[] lines = general.split("\n");
		String fin = "";
		for (String l : lines) {
			if (l.contains("Roadmap") || l.contains("Showdown at Avalon")) {
				fin = l;
				break;
			}
		}
		String[] guesses = new String[] {"Dev","ComDev", "COMDEV","DEV"};
		String url = "";
		try {
			for(String g : guesses) {
				if(!url.equals(""))break;
				url = "https://www.kongregate.com"
						+ fin.substring(fin.indexOf("href=\"/forums/2468-general") + 6, fin.indexOf("\">["+g+"]"));
			}
		}
		catch(Exception e) {

			url = "https://www.kongregate.com"
					+ fin.substring(fin.indexOf("href=\"/forums/2468-general") + 6, fin.indexOf("\">[COMDEV]"));
		}
		return url;
	}
	
	private static String getFirstKongPost(String url) {
		String road = Wget.wGet(url);

		String map = road.substring(road.indexOf("<div class=\"raw_post\""));
		map = map.substring(map.indexOf(">") + 1);
		map = map.substring(0, map.indexOf("</div>"));
		return map;
	}

	private static String getRoadMap(String url) {
		return getFirstKongPost(url);
	}

	private static String getRoadMap() {
		return getRoadMap(getRoadMapUrl());
	}
	public static void main(String[] args) {
		
		String url = getRoadMapUrl();
		String map = getRoadMap(url);


		String rep = "";
		String[] sections = map.split("\\*\\*");
		System.out.println(sections[3]);
		for (int i = 3; i < sections.length; i += 2) {
			String title = sections[i];
			String msg = sections[i + 1];
			String date = msg.split("\\*")[1];
			rep += title + "\n";
			rep += date + "\n\n";
		}
		rep += url;
		System.out.println(rep);
	}
}
