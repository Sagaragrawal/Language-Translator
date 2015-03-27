package Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;

import Exceptions.InvalidContainerException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

/**
 * The Class Container.
 * 
 * @author josephabudar, radubirgauan, thomasneill, toysifislam
 */
public class Container {
	/** The core. */
	private ZipFile core = null;
	/** The parameters. */
	ZipParameters parameters = new ZipParameters();
	/** The dictionaries. */
	private HashSet<Dictionary> dictionaries = new HashSet<Dictionary>();
	/** The version. */
	private String version = null;
	/** The working dir. */
	Path workingDir = null;

	/**
	 * Instantiates a new container.
	 *
	 * @param container
	 *            the container
	 */
	public Container(File container) {
		try {
			core = new ZipFile(container);
			workingDir = GUI.Menu.workingDir;
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			unpack();
		} catch (InvalidContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadAvailableTables();
	}

	/**
	 * Gets the available dictionaries.
	 *
	 * @return the available dictionaries
	 */
	public HashSet<Dictionary> getAvailableDictionaries() {
		return dictionaries;
	}

	/**
	 * Gets the dictionaries.
	 *
	 * @return the dictionaries
	 */
	public Object[] getDictionaries() {
		return dictionaries.toArray();
	}

	/**
	 * Gets the dictionary.
	 *
	 * @param name
	 *            the name
	 * @return the dictionary
	 */
	public Dictionary getDictionary(String name) {
		if (name == null) {
			return null;
		}
		for (Dictionary e : dictionaries) {
			if (e.getName().toLowerCase().startsWith(name.toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Gets the number of dictionaries.
	 *
	 * @return the number of dictionaries
	 */
	public int getNumberOfDictionaries() {
		return dictionaries.size();
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public int[] getVersion() {
		String[] split = version.split("\\.");
		int[] toReturn = { 0, 0, 0 };
		toReturn[0] = Integer.parseInt(split[0]);
		toReturn[1] = Integer.parseInt(split[1]);
		toReturn[2] = Integer.parseInt(split[2]);
		return toReturn;
	}

	/**
	 * Load available tables.
	 */
	private void loadAvailableTables() {
		File phrases; // the phrases.db file
		File words; // the words.db file
		File dict_version; // the VERSION file
		// A list of all files (and directories) in the root of the container.
		String[] libs = new File(workingDir.toString()).list();
		for (String lib : libs) {
			// get the path of the dictionary to load
			String path = workingDir.toString() + "\\" + lib;
			// make it into a file
			File dir = new File(path);
			// if its a directory
			if (dir.isDirectory()) {
				// set phrases to the location of the dictionaries phrases.db
				// file
				phrases = new File(path + "\\phrases.db");
				// set words to the location of the dictionaries words.db file
				words = new File(path + "\\words.db");
				// set dict_verison to the location of the dictionaries VERSION
				// file
				dict_version = new File(path + "\\VERSION");
				// declare default state of the version
				String ver = "unknown";
				// if the VERSION file doesn't exist.
				if (!dict_version.exists()) {
					if (phrases.exists() && words.exists()) {
						dictionaries.add(new Dictionary(lib, ver,
								new File(path)));
					}
				} else {
					try {
						BufferedReader br = new BufferedReader(new FileReader(
								dict_version));
						ver = br.readLine();
						if (phrases.exists() && words.exists()) {
							dictionaries.add(new Dictionary(lib, ver, new File(
									path)));
						}
						br.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (dir.isFile() && dir.getName().equals("VERSION")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(dir));
					try {
						version = br.readLine();
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Repackage the container.
	 */
	public void repack() {
		File toSave = core.getFile();
		parameters.setIncludeRootFolder(false);
		parameters.setCompressionLevel(7);
		toSave.delete();
		File root = new File(workingDir.toString());
		System.out.println("Root Folder: " + root.getAbsolutePath());
		try {
			core.createZipFileFromFolder(root, parameters, false, 0L);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unpackage the container.
	 */
	private void unpack() throws InvalidContainerException {
		try {
			if (core.isValidZipFile()) {
			    core.extractAll(workingDir.toString());
			} else {
				throw new InvalidContainerException();
			}
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
}
