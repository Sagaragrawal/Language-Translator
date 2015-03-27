package Code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Updater {

	protected URL VersionUrl = null;

	public Updater() {

	}

	public Updater(URL VersionUrl) {

	}

	public void setVersionUrl(URL VersionUrl) {
		this.VersionUrl = VersionUrl;
	}

	public void setVersionUrl(String Url) {
		try {
			this.VersionUrl = new URL(Url);
		} catch (MalformedURLException e) {

		}
	}

	public int[] getRemoteVerison() {
		BufferedReader in = null;
		String inputLine = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					VersionUrl.openStream()));
			inputLine = in.readLine();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] remoteVersion = { 0, 0, 0 };
		String[] raw = inputLine.split("\\.");
		remoteVersion[0] = Integer.parseInt(raw[0]);
		remoteVersion[1] = Integer.parseInt(raw[1]);
		remoteVersion[2] = Integer.parseInt(raw[2]);
		return remoteVersion;
	}
}
