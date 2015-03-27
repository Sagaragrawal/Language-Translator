package Code;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * The Class Dictionary.
 *
 * @author josephabudar, radubirgauan, thomasneill, toysifislam
 */
public class Dictionary {
	/** The dictionary. */
	private File dictionary;
	/** The name. */
	private String name = null;
	/** The phrases. */
	HashMap<String, String> phrases = new HashMap<String, String>();
	/** The unsaved changes. */
	private boolean unsavedChanges = false;
	/** The version. */
	private String version = null;
	/** The words. */
	HashMap<String, String> words = new HashMap<String, String>();

	/**
	 * Initialises the dictionary.
	 *
	 * @param name
	 *            the name
	 * @param version
	 *            the version
	 * @param dictionary
	 *            the dictionary
	 */
	public Dictionary(String name, String version, File dictionary) {
		this.name = name;
		this.dictionary = dictionary;
		this.version = version;
		loadDictionary(dictionary);
	}

	/**
	 * Adds a word to the dictionary.
	 *
	 * @param eng
	 *            the English word
	 * @param trans
	 *            the translation
	 * @return if added successfully
	 */
	public void add(String eng, String trans) {
		eng = eng.toLowerCase();
		trans = trans.toLowerCase();
		if (eng.equals(trans)) {
			return;
		}
		if (eng.contains(" ") && trans.contains(" ")) {
			phrases.putIfAbsent(eng, trans);
		} else {
			words.putIfAbsent(eng, trans);
		}
		unsavedChanges = true;
	}

	/**
	 * Clear.
	 */
	public void clear() {
		words.clear();
		phrases.clear();
	}

	/**
	 * Checks if word exists.
	 *
	 * @param word
	 *            the word
	 * @return if exists
	 */
	public boolean doesExist(String word) {
		for (String val : words.keySet()) {
			if (val.toString().equals(word)
					|| words.get(val).toString().equals(word)) {
				return true;
			}
		}
		for (String val : phrases.keySet()) {
			if (val.toString().equals(word)
					|| words.get(val).toString().equals(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the translation of a word.
	 *
	 * @param word
	 *            the word
	 * @return translation
	 */
	public String getTranslation(String word) {
		word = word.toLowerCase();
		// If the word is English
		if (isEnglish(word)) {
			// and is in the HashMap 'words'
			if (words.containsKey(word)) {
				// for each Entry in the HashMap
				for (Entry<String, String> e : words.entrySet()) {
					// if the entry equals the word
					if (e.getKey().equals(word)) {
						// return the value for that entry
						return e.getValue();
					}
				}
			}
		} else {
			if (words.containsValue(word)) {
				for (Entry<String, String> e : words.entrySet()) {
					if (e.getValue().equals(word)) {
						return e.getKey();
					}
				}
			}
		}
		return word;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Checks if a word is English.
	 *
	 * @param word
	 *            the word
	 * @return if is English or not
	 */
	public boolean isEnglish(String word) {
		for (String val : words.keySet()) {
			if (val.toString().equals(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads the dictionary from a file into the HashMap.
	 *
	 * @param dictionary
	 *            the dictionary to load
	 */
	public void loadDictionary(File dictionary) {
		words = new HashMap<String, String>();
		phrases = new HashMap<String, String>();
		BufferedReader br = null;
		final String dictionaryRoot = dictionary.getAbsolutePath();
		try {
			if (dictionary.exists() && dictionary.isDirectory()) {
				// First the words
				br = new BufferedReader(new FileReader(new File(dictionaryRoot
						+ "\\words.db")));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.contains(",")) {
						String[] split = line.split(",");
						try {
							if (split.length == 2) {
								if (!split[0].equals(split[1])) {
									putWords(split[0], split[1]);
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							e.printStackTrace();
						}
					}
				}
				// Now the phrases
				br = new BufferedReader(new FileReader(new File(dictionaryRoot
						+ "\\phrases.db")));
				while ((line = br.readLine()) != null) {
					if (line.contains(",")) {
						String[] split = line.split(",");
						try {
							if (!split[0].equals(split[1])) {
								putPhrases(split[0], split[1]);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							e.printStackTrace();
						}
					}
				}
			}
			unsavedChanges = false;
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Gets if the dictionary has been modified since loading.
	 *
	 * @return if the dictionary has been modified
	 */
	public boolean modified() {
		return unsavedChanges;
	}

	/**
	 * Prints the dictionary to the console.
	 */
	public void print() {
		System.out.println(" ========= BEGIN  DICTIONARY ========= ");
		for (Map.Entry<String, String> entry : phrases.entrySet()) {
			String english = entry.getKey();
			String translation = entry.getValue();
			System.out.println(english + " || " + translation);
		}
		for (Map.Entry<String, String> entry : words.entrySet()) {
			String english = entry.getKey();
			String translation = entry.getValue();
			System.out.println(english + " || " + translation);
		}
		System.out.println(" ========== END  DICTIONARY ==========");
	}

	/**
	 * Put phrases.
	 *
	 * @param eng
	 *            the English
	 * @param trans
	 *            the translation
	 */
	private void putPhrases(String eng, String trans) {
		eng = eng.toLowerCase();
		trans = trans.toLowerCase();
		if (eng.equals(trans)) {
			return;
		}
		phrases.putIfAbsent(eng, trans);
	}

	/**
	 * Put words.
	 *
	 * @param eng
	 *            the English
	 * @param trans
	 *            the translation
	 */
	private void putWords(String eng, String trans) {
		eng = eng.toLowerCase();
		trans = trans.toLowerCase();
		if (eng.equals(trans)) {
			return;
		}
		words.putIfAbsent(eng, trans);
	}

	/**
	 * Removes a word and its translation from the dictionary.
	 *
	 * @param word
	 *            the word to remove
	 * @return if removed successfully
	 */
	public boolean remove(String word) {
		Iterator<Entry<String, String>> it;
		word = word.toLowerCase();
		if (!doesExist(word)) {
			System.out.println("ERROR: Word does not exist.");
			return true;
		}
		// If word contains a space, it's most likely a phrase, so iterate over
		// the phrases HashMap first.
		if (word.contains(" ")) {
			it = phrases.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> set = (Map.Entry<String, String>) it
						.next();
				String english = set.getKey();
				String translation = set.getValue();
				if (english.equals(word) || translation.equals(word)) {
					it.remove();
				}
			}
		} else {
			it = words.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> set = (Map.Entry<String, String>) it
						.next();
				String english = set.getKey();
				String translation = set.getValue();
				if (english.equals(word) || translation.equals(word)) {
					it.remove();
				}
			}
		}
		if (!doesExist(word)) {
			System.out.println("Entry deleted successfully.");
			unsavedChanges = true;
			return true;
		}
		return false;
	}

	/**
	 * Saves the dictionary to file.
	 */
	public void save() {
		PrintWriter pw = null;
		File words_db = new File(dictionary.getAbsoluteFile() + "\\words.db");
		File phrases_db = new File(dictionary.getAbsoluteFile()
				+ "\\phrases.db");
		try {
			pw = new PrintWriter(words_db);
			if (words_db.delete()) {
				pw.close();
				System.out.println("ERROR: File could not be deleted.");
				return;
			}
			for (String val : words.keySet()) {
				pw.println(val.toString() + "," + words.get(val).toString());
				System.out.println(val.toString() + ","
						+ words.get(val).toString());
			}
			pw.close();
			pw = new PrintWriter(phrases_db);
			if (phrases_db.delete()) {
				pw.close();
				System.out.println("ERROR: File could not be deleted.");
				return;
			}
			for (String val : phrases.keySet()) {
				pw.println(val.toString() + "," + phrases.get(val).toString());
			}
			pw.close();
			words.clear();
			loadDictionary(dictionary);
			unsavedChanges = false;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Gets the size of the dictionary.
	 *
	 * @return the number of entries in the dictionary
	 */
	public int size() {
		int count = 0;
		count += words.size();
		count += phrases.size();
		return count;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return dictionary;
	}

	/**
	 * Load.
	 */
	public void load() {
		words = new HashMap<String, String>();
		phrases = new HashMap<String, String>();
		loadDictionary(dictionary);
	}
}
