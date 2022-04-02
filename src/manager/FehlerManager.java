package manager;

import java.util.ArrayList;
import java.util.regex.Pattern;

import benutzer.Benutzer;
import database.BenutzerDatabase;

/**
 * 
 * Fehlermanager, kann Email-Format ueberpruefen und Nutzer loeschen, 
 * Beträge menschenfreundlich Formatieren...
 * 
 * @author Tobias Liebl
 */

public class FehlerManager {

	/**
	 * Aendere int in einen Betrag mit € Zeichen
	 * 
	 * @param betrag
	 * @return den Betrag
	 */
	public static String machBetragAusInt(int betrag) {

		String btrg = "";

		try {
			btrg = String.valueOf(betrag);
		} catch (Exception e) {
		}

		if (!btrg.equals("")) {

			if (btrg.length() == 1) {
				btrg = "0.0" + btrg + "&euro;";
			} else if (btrg.length() == 2) {
				btrg = "0." + btrg + "&euro;";
			}

			// Die If-Abfragen vorher sind nur um ArrayIndexOutofBounsd Exceptions zu
			// umgehen...
			else if (btrg.length() > 2) {
				btrg = btrg.substring(0, btrg.length() - 2) + "." + btrg.substring(btrg.length() - 2) + "&euro;";
			}
		}
		return btrg;
	}

	/**
	 * Prueft ob Loeschung klappt
	 * 
	 * @param user
	 * @return ob loeschen geklappt hat
	 */
	public static boolean userLoeschen(Benutzer user) {

		ArrayList<Integer> konten = user.getKonten();
		for (Integer kId : konten) {
			boolean laeuft = KontoManager.loescheKonto(user, kId);
			if (!laeuft) {
				return false;
			}
		}

		boolean geloescht = BenutzerDatabase.userLoeschen(user.getEmail());

		return geloescht;
	}

	public static boolean emailEchtheitscheck(String email) {
		if (email == null) {
			return false;
		}
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+"
				+ "[a-zA-Z]{2,7}$";
		Pattern richtigesEmailPattern = Pattern.compile(emailRegex);
		return richtigesEmailPattern.matcher(email).matches();

	}

	/**
	 * Pruefe ob Passwort nach Vorgabe passt.
	 * 
	 * @param password
	 * @return
	 */
	public static boolean passwortGut(String password) {
		if (password == null) {
			return false;
		}
		// Am Anfang und am Ende soll genau ein beliebiger Wort-Char stehen = kein
		// Sonderzeichen
		String anfangEnde = "(\\w){1}.{6,}(\\w){1}";
		// prueft ob mind. 1 Grossbuchstabe vorhanden ist
		String einGrosses = ".*[A-Z]{1}.*";
		// ein Kleinbuchstabe
		String einKleines = ".*[a-z]{1}.*";
		// und ein Sonderzeichen \W
		String einSonder = ".*[\\W]{1}.*";

		Pattern richtigesEmailPattern = Pattern.compile(anfangEnde);
		boolean passt = richtigesEmailPattern.matcher(password).matches();
		if (passt == false) {
			return false;
		} else {
			passt = Pattern.compile(einKleines).matcher(password).matches();
			if (passt == false) {
				return false;
			} else {
				passt = Pattern.compile(einGrosses).matcher(password).matches();
				if (passt == false) {
					return false;
				} else {
					passt = Pattern.compile(einSonder).matcher(password).matches();
					if (passt == false) {
						return false;
					}
					// wenn passt ab der ersten Prüfung bis jetzt true ist, passt das PW
					return passt;
				}
			}
		}
	}

}
