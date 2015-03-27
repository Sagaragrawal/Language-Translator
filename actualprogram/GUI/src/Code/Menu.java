package Code;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import Exceptions.InvalidMenuSelectionException;

/**
 * The Class Menu.
 *
 * @author josephabudar, radubirgauan, thomasneill, toysifislam
 */
public class Menu {
	/** The working dir. */
	static protected Path workingDir = null;
	/** The container. */
	static Container container = null;
	/** The is update. */
	private static boolean isUpdate = false;
	/** The dictionary. */
	private Dictionary dictionary = null;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		File packed = new File("data\\languages.db");
		if(!packed.exists()) {
			System.out.println("No Dictionary container detected. Would you like to download version " + getRemoteVersion() + " from the internet? (Y/n)");
			String input = Genio.getString().toLowerCase();
			if(input.equals("y")) {
				downloadRemoteVersion();
			} else {
				while(true) {
					System.out.println("ERROR: No dictionary detected, please download the dictionary before continuing.");
					System.out.println("Program will now exit..");
					System.exit(0);
				}
			}
		}
		
		try {
			workingDir = Files.createTempDirectory("ac12001_team#1_");
		} catch (IOException e) {
			e.printStackTrace();
		}
		container = new Container(packed);
		Menu main = new Menu();
		while (true) {
			try {
				main.displayMenu();
			} catch (InvalidMenuSelectionException e) {
				e.print();
			}
		}
	}

	/**
	 * Menu Constructor.
	 */
	public Menu() {
		try {
			displayMenu(-1);
		} catch (InvalidMenuSelectionException e) {
			e.print();
		}
	}

	/**
	 * Displays the menu.
	 * 
	 * @throws InvalidMenuSelectionException
	 */
	public void displayMenu() throws InvalidMenuSelectionException {
		displayMenu(0);
	}

	/**
	 * Displays the menu.
	 *
	 * @param level
	 *            the level of menu to display
	 * @throws InvalidMenuSelectionException
	 */
	public void displayMenu(int level) throws InvalidMenuSelectionException {
		switch (level) {
		case -1:
			Object[] dictionaries = container.getDictionaries();
			System.out.println(" ========= Select Dictionary ========= ");
			for (int i = 0; i < dictionaries.length; i++) {
				System.out.println("       " + (i + 1) + "). "
						+ ((Dictionary) dictionaries[i]).getName() + "\t(v"
						+ ((Dictionary) dictionaries[i]).getVersion() + ")");
			}
			System.out.println(" =====================================");
			int toLoad = 0;
			while (toLoad < 1 || toLoad > dictionaries.length) {
				toLoad = Genio.getInteger();
				if(toLoad < 1 || toLoad > dictionaries.length) {
					System.out.println("ERROR: Invalid menu selection, " + toLoad + ", please try again.");
					throw new InvalidMenuSelectionException();
				}
			}
			dictionary = (Dictionary) dictionaries[toLoad - 1];
			dictionary.load();
			break;
		case 0:
			System.out.println(" ======== Language Translator ======== \n");
			System.out.println("       1). Translate");
			System.out.println("       2). Translate to file");
			System.out.println("       3). Dictionary Options");
			System.out.println("       4). Check for updates\n");
			System.out.println("       0). Exit");
			System.out.println("\n =====================================");
			getSelection(level);
			break;
		case 3:
			System.out.println(" ======== Dictionary Options ======== \n");
			System.out.println("       1). Add Word");
			System.out.println("       2). Remove Word");
			System.out.println("       3). Display Dictionary");
			if (!dictionary.modified()) {
				System.out.println("       4). Change Dictionary\n");
			}
			if (dictionary.modified()) {
				System.out.println("       4). Change Dictionary");
				System.out.println("       5). Save Dictionary\n");
			}
			System.out.println("       0). Return\n");
			System.out.println("       [" + dictionary.size() + "] RECORDS\n");
			System.out.println(" ======================================");
			getSelection(level);
			break;
		default:
			throw new InvalidMenuSelectionException();
		}
	}

	/**
	 * Gets the selection from the user.
	 *
	 * @param level
	 *            the level of menu to process selections for
	 * @return the selection
	 * @throws InvalidMenuSelectionException
	 */
	public void getSelection(int level) throws InvalidMenuSelectionException {
		int selection = Genio.getInteger();
		switch (level) {
		case 0:
			switch (selection) {
			case 1:
				toTranslate();
				break;
			case 2:
				PrintWriter pw;
				System.out
						.println("Please enter the filename to save the translation as:");
				String name = Genio.getString() + ".txt";
				File translation = new File(name);
				if (!translation.exists()) {
					try {
						translation.createNewFile();
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
				try {
					pw = new PrintWriter(translation);
					long startTime = System.nanoTime();
					System.out
							.println("Please enter the word or setence you would like to translate.");
					String toTranslate = Genio.getString();
					pw.println(getTranslation(toTranslate));
					long endTime = System.nanoTime();
					System.out.println(((endTime - startTime) / 1000000)
							+ "ms to translate.");
					System.out.println("Translation saved as:");
					System.out.println(translation.getAbsolutePath());

				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}
				break;
			case 3:
				displayMenu(3);
				break;
			case 4:
				update();
				break;
			case 0:
				exit();
				break;
			default:
				System.out.println("ERROR: Invalid Selection");
				break;
			}
			break;
		case 3:
			switch (selection) {
			case 1:
				System.out.println("Please enter the English and the "
						+ dictionary.getName() + ", seperated by a comma.");
				String in = Genio.getString();
				String[] sd = in.split(",");
				dictionary.add(sd[0], sd[1]);
				displayMenu(3);
				break;
			case 2:
				System.out.println("Please enter the word, English or "
						+ dictionary.getName() + ", to be removed.");
				String toRemove = Genio.getString();
				dictionary.remove(toRemove);
				displayMenu(3);
				break;
			case 3:
				dictionary.print();
				displayMenu(3);
				break;
			case 4:
				displayMenu(-1);
				break;
			case 5:
				dictionary.save();
				container.repack();
				displayMenu(3);

				break;
			case 0:
				displayMenu();
				break;
			default:
				System.out.println("ERROR: Invalid Selection");
				displayMenu(3);
				break;
			}
			break;
		default:
			throw new InvalidMenuSelectionException();
		}
	}

	/**
	 * To translate.
	 */
	public void toTranslate() {
		System.out
				.println("Please enter the word or setence you would like to translate.");
		String toTranslate = Genio.getString();
		System.out.println("In " + dictionary.getName() + ":");
		long startTime = System.nanoTime();
		System.out.println(getTranslation(toTranslate));
		long endTime = System.nanoTime();
		System.out.println(((endTime - startTime) / 1000000)
				+ "ms to translate.");
	}

	/**
	 * Gets the translation.
	 *
	 * @param phrase
	 *            the phrase
	 * @return the translation
	 */
	public String getTranslation(String phrase) {
		String[] words = phrase.split("\\s+");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(dictionary.getTranslation(word) + " ");
		}
		return sb.toString();
	}

	/**
	 * Exits the program.
	 */
	private void exit() {
		File temp = new File(workingDir.toString());
		temp.deleteOnExit();
		System.exit(0);
	}

	/**
	 * Update.
	 */
	public static void update() {
		String toSplit = getRemoteVersion();
		int[] remoteVersion = { 0, 0, 0 };
		String[] raw = toSplit.split("\\.");
		remoteVersion[0] = Integer.parseInt(raw[0]);
		remoteVersion[1] = Integer.parseInt(raw[1]);
		remoteVersion[2] = Integer.parseInt(raw[2]);
		int[] localVersion = container.getVersion();
		if (localVersion[0] < remoteVersion[0]) {
			isUpdate = true;
		} else if (localVersion[1] < remoteVersion[1]) {
			isUpdate = true;
		} else if (localVersion[2] < remoteVersion[2]) {
			isUpdate = true;
		}
		if (isUpdate) {
			System.out
					.println("NOTICE: There is an updated database available for download.");
			System.out.println("Local Version:\t" + localVersion[0] + "."
					+ localVersion[1] + "." + localVersion[2]);
			System.out.println("Remote Version:\t" + remoteVersion[0] + "."
					+ remoteVersion[1] + "." + remoteVersion[2]);
			System.out.println("\nWould you like to update? (Y/n)");
			String resp = Genio.getString().toLowerCase();
			if (resp.equals("y")) {
				System.out.println();
				downloadRemoteVersion();
			} else if (resp.equals("n")) {
				System.out.println("Didn't agreed to update.");
			}
		}
	}

	/**
	 * Gets the latest version held by the server.
	 *
	 * @return the remote version
	 */
	private static String getRemoteVersion() {
		URL remoteVersion = null;
		BufferedReader in = null;
		String inputLine = null;
		try {
			remoteVersion = new URL(
					"https://dl.dropboxusercontent.com/u/209495545/VERSION");
			in = new BufferedReader(new InputStreamReader(
					remoteVersion.openStream()));
			inputLine = in.readLine();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputLine;
	}
	
	private static void downloadRemoteVersion() {
		URL remoteVersion = null;
		BufferedReader in = null;
		PrintWriter pw = null;
		FileOutputStream out = null;
		String nextLine = null;
		File data = new File("data");
		if(!data.exists()) {
			data.mkdir();
		}
		try {
			remoteVersion = new URL("http://github.com/jmsnll/TopSecretLegitThing/raw/master/eclipse/data/languages.db");
			in = new BufferedReader(new InputStreamReader(remoteVersion.openStream()));
			out = new FileOutputStream("data\\languages.db.temp");
			pw = new PrintWriter(out);
			
			nextLine = in.readLine();
			while(nextLine != null) {
				pw.println(nextLine);
				System.out.println(nextLine);
				nextLine = in.readLine();
			}
			
			pw.close();
			out.close();
			System.out.println("Download finished!");
			
		} catch (IOException e) {
			System.out.println("ERROR: An error occurred reading or writing the file, maybe the server is down.  Please try again.");
		}
		cleanUp();
	}
	
	private static void cleanUp() {
		File olddb = new File("data\\languages.db");
		File newdb = new File("data\\languages.db.temp");

		if(olddb.exists()) {
			olddb.delete();
		}
		newdb.renameTo(olddb);
		System.out.println("Update applied!");
		System.out.println("Update finished!");
	}
}
