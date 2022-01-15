package de.neuwirthinformatik.Alexander.TU.TUM;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.TUM.BOT.Bot;
import de.neuwirthinformatik.Alexander.TU.TUM.BOT.GlobalBotData;
import de.neuwirthinformatik.Alexander.TU.TUM.Save.JediDeckGrab;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import de.neuwirthinformatik.Alexander.TU.util.Task;
import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class TUM {

	static {
		GUI.setLookAndFeel();
	}
	public JFrame frame;
	public static String start_time = time();
	public static TUM _this;
	public static SettingsPanel settings = new SettingsPanel();
	public static ProcessPanel pp = new ProcessPanel();
	public static Log log = new Log();
	public BotOverviewPanel botovp;
	public LiveSimPanel lsp;

	public TUM() {
		TUM._this = this;
		frame = new JFrame();

		frame.setIconImage(GUI.icon);
		frame.setTitle(
				settings.name() + " | " + settings.TUO_VERSION + " | " + " @ " + settings.cookies_folder() + "/");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Settings", null, new JScrollPane(settings), "Adjust Settings");

		tabbedPane.addTab("Bots", null, botovp = new BotOverviewPanel(), "Inspect Bots");

		if (Permissions.has("live_sim")) {
			tabbedPane.addTab("Live Sim", null, new JScrollPane(lsp = new LiveSimPanel()),
					"Simmulate your running battle");
		}

		tabbedPane.addTab("Processes", null, new JScrollPane(pp), "View process");

		frame.add(tabbedPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static String time() {
		return Task.time();
	}

	public static String cookieFolderChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select a cookies folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		chooser.setSelectedFile(new File("cookies"));

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getName();
		} else {
			return "cookies";
		}
	}

	public static void loadUpdate() {
		String up = checkUpdates();
		if (!up.equals("")) {
			int selection = JOptionPane.showConfirmDialog(null, up, "Update", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, new ImageIcon(GUI.icon));
			if (selection == JOptionPane.OK_OPTION)
				update();
		}
	}

	public static void loadGuilds() {
		final String name;
		if (Permissions.has("load_jedi")) {
			name = JOptionPane.showInputDialog(null, "BGE for JediDB deck load", TUM.settings.bge());
		} else {
			name = null;
		}
		Task.start(() -> {
			String nname = name;
			if (name == null) {
				nname = "None";
				TUM.settings.bge.setText(name);
				return;
			} else if (name.equals("")) {
				nname = "None";
			}
			TUM.settings.bge.setText(nname);
			TUM.pp.forEach("Loading the guilds.", TUM.settings.guilds(), (g, j) -> {
				JediDeckGrab.loadGuild(g);
				return g;
			});

			/*
			 * GUI.LoadBar lb = new GUI.LoadBar("Loading the guilds.",guilds.length); for
			 * (int i =0; i < guilds.length; i++) { JediDeckGrab.loadGuild(guilds[i]);
			 * if(!lb.setProgress(i, guilds[i]))break; }
			 */
		});
	}

	public static void loadBots() {

		String cookies = cookieFolderChooser();
		settings.setCookiesFolder(cookies);

		Task.start(() -> {
			String[] bns = GlobalBotData.getBotList();
			Bot[] bots = new Bot[bns.length];

			TUM.pp.forEach("Loading the bots.", bns, (b, j) -> {
				bots[j] = new Bot(b);
				return bots[j].toString();
			});

			/*
			 * GUI.LoadBar lb = new GUI.LoadBar("Loading the bots.",bns.length);
			 * 
			 * for (int i = 0 * bns.length / 100; i < bns.length; i++) { bots[i] = new
			 * Bot(bns[i]); lb.setProgress(i, bns[i]); if(lb.isCanceled())break; }
			 * lb.close();
			 */
			GlobalBotData.bots = bots;
		});
	}

	public static void startTUM() {

		if (TUM.settings.UPDATE_MSG)
			loadUpdate();

		loadGuilds();
		loadBots();

		Task.sleepForAll();

		new TUM();
	}

	public static String checkUpdates() {
		String ret = "";
		Wget.wGet("tum.json", "https://api.github.com/repos/APN-Pucky/TUM/releases/latest");
		String tumjson = GlobalData.readFile("tum.json").replaceAll("\n", "");
		JSONObject tum = new JSONObject(tumjson);
		String tum_tag_name = tum.getString("tag_name");
		if (!tum_tag_name.equals(settings.VERSION)) {
			ret += "New TUM.jar version: " + tum_tag_name + " available:\n";
			ret += " - " + tum.getString("name") + "\n";
		}

		Wget.wGet("tuo.json", "https://api.github.com/repos/APN-Pucky/tyrant_optimize/releases/latest");
		String tuojson = GlobalData.readFile("tuo.json").replaceAll("\n", "");
		JSONObject tuo = new JSONObject(tuojson);
		String tag_name = (tuo).getString("tag_name");

		if (!settings.TUO_VERSION.equals("Tyrant Unleashed Optimizer " + tag_name)) {
			ret += "New tuo.exe version: " + tag_name + " available:\n";
			ret += " - " + tuo.getString("name");
		}

		return ret;
	}

	public static void updateTUM() {
		// TUM
		System.out.println("Downloading new TUM.jar ...");
		// Task.start(() -> Wget.wGet("TUM-new.jar",
		// "https://drive.google.com/uc?export=download&id=0BxMgxg5B0xsDYWhMamxQR0hTZjQ"));
		Wget.wGet("tum.json", "https://api.github.com/repos/APN-Pucky/TUM/releases/latest");
		String tumjson = GlobalData.readFile("tum.json").replaceAll("\n", "");
		String tum_tag_name = (new JSONObject(tumjson)).getString("tag_name");
		Task.start(() -> {
			Wget.wGet("TUM-enc.jar",
					"https://github.com/APN-Pucky/TUM/releases/download/" + tum_tag_name + "/TUM-enc.jar");
			GlobalData.decryptFile("TUM-enc.jar", "TUM-new.jar");
		});

		// Restart
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "TUM-new.jar", "postautoupdate");
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		System.out.println("Waiting for all Processes to end...");
		Task.sleepForAll();
		System.out.println("Everything updated. Restarting");
		try {
			Process p = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}

	public static void updateTUO() {
		// TUO
		System.out.println("Downloading new tuo.exe ...");
		Wget.wGet("tuo.json", "https://api.github.com/repos/APN-Pucky/tyrant_optimize/releases/latest");
		String tuojson = GlobalData.readFile("tuo.json").replaceAll("\n", "");
		String tag_name = (new JSONObject(tuojson)).getString("tag_name");
		Task.start(() -> Wget.wGet("tuo.exe",
				"https://github.com/APN-Pucky/tyrant_optimize/releases/download/" + tag_name + "/tuo.exe"));
		Task.start(() -> Wget.wGet("tuo-x86.exe",
				"https://github.com/APN-Pucky/tyrant_optimize/releases/download/" + tag_name + "/tuo-x86.exe"));
	}

	public static void updateXML() {
		updateXML(false);
	}

	public static void updateXML(boolean dev) {
		String tyrant_url = (dev ? "http://mobile-dev.tyrantonline.com/assets/"
				: "http://mobile.tyrantonline.com/assets/");
		// XML
		System.out.println("Downloading new XMLs ...");
		Task.start(() -> {
			int i = 0;
			Wget.Status status = Wget.Status.Success;
			while (status == Wget.Status.Success) {
				i++;
				String sec = "cards_section_" + i + ".xml";
				status = Wget.wGet("data" + GlobalData.file_seperator + sec, tyrant_url + sec);
			}
		});
		final String[] arr = new String[] { "fusion_recipes_cj2", "missions", "levels", "skills_set" };
		for (int i = 0; i < arr.length; i++) {
			final String sec = arr[i] + ".xml";
			Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + sec, tyrant_url + sec));
		}
		Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + "raids.xml",
				"https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/raids.xml"));
		Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + "bges.txt",
				"https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/bges.txt"));
	}

	public static void update() {
		updateTUO();

		updateXML();

		updateTUM();
	}

	public static void main(String[] args) {
		if (args.length != 0 && args[0].equals("selfencrypt")) {
			System.out.println("Encrypting TUM.jar");
			GlobalData.encryptFile("TUM.jar", "TUM-enc.jar");
			System.out.println("Encrypted TUM.jar");
			System.exit(0);
		}
		if (args.length == 0 || !args[0].equals("postautoupdate")) // TUM.jar
		{

			if (TUM.settings.AUTO_UPDATE) {
				// AUTOUPDATE-IMAGE
				TUM.update();
			}
			// loading image
			GlobalData.init();
		} else // TUM-new.jar
		{
			// loading image
			GlobalData.init(); // load xmls and wait for TUM.jar to exit
			GlobalData.copyFile("TUM-new.jar", "TUM.jar");
		}

		startTUM();
	}

}
