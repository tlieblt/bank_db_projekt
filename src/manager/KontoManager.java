package manager;

import java.util.ArrayList;
import java.util.Collections;

import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.KontoDatabase;
import database.TransaktionsDatabase;

/**
 * Verwaltet Konto Aufgaben
 * 
 * @author Tobias Liebl
 *
 */
public class KontoManager {


	/**
	 * Ein neues Konto für einen Nutzer wird erstellt und aktualisiert die Daten des
	 * Nutzerobjekts direkt
	 * 
	 * @param user
	 * @return ob neues Konto erfolgreich erstellt wurde
	 */
	public static boolean machNeuesKontoFuer(Benutzer user) {
		boolean erfolg = false;
		if (user == null) {
			return erfolg;
		}
		boolean kontoErstellt = KontoDatabase.erstelleNeuesKonto(user);
		if (kontoErstellt) {

			ArrayList<ArrayList<String>> konten = KontoDatabase.getKontoInfoZuBenutzerID(user.getMeiEidi());

			if (konten != null) {

				ArrayList<String> kontoIds = konten.get(0);

				int neuesteKId = Integer.valueOf(kontoIds.get(kontoIds.size() - 1));

				user.setCurKonto(neuesteKId);

				String curIban = KontoManager.machNeueIban(neuesteKId);

				if (curIban != null) {

					boolean allesFertig = KontoDatabase.gibKontoNeueIban(curIban, neuesteKId);

					if (allesFertig) {
						user.setCurIban(curIban);

						ArrayList<String> kontoIbans = konten.get(1);

						// Da in KontoIbans am Ende die IDs stehen, können
						// beide sortiert werden - Reihenfolge für Auwahl des
						// aktuellen Kontos wichtig - AenderKID
						Collections.sort(kontoIds);
						Collections.sort(kontoIbans);

						kontoIbans.remove(kontoIbans.size() - 1);
						kontoIbans.add(curIban);

						ArrayList<Integer> kIDs = new ArrayList<Integer>();

						for (String s : kontoIds) {
							kIDs.add(Integer.valueOf(s));
						}

						user.setKonten(kIDs);
						user.setKontoIbans(kontoIbans);

						// in den sortierten ArrayLists sollte das letzte Element das neueste Konto sein
						user.setCurKonto(kIDs.get(kIDs.size() - 1));
						user.setCurIban(kontoIbans.get(kontoIbans.size() - 1));

						ArrayList<Ueberweisung> keine = new ArrayList<Ueberweisung>();
						keine.add(new Ueberweisung("keine"));

						user.setCurKontoueberweisungen(keine);

						erfolg = true;
					}
				}
			}
		}
		return erfolg;
	}


	/**
	 * die aktuellen Konto-Informationen in ein Nutzerobjekt zB = zu
	 * Bearbeiten
	 * 
	 * @param user
	 * @return upgedateten User
	 */
	public static Benutzer updateBenutzerKontenInfo(Benutzer user) {
		Benutzer zB = user;

		ArrayList<ArrayList<String>> kontoInfo = KontoDatabase.getKontoInfoZuBenutzerID(user.getMeiEidi());
		if (kontoInfo == null) {
			return null;
		} else if (kontoInfo.get(0).isEmpty() || kontoInfo.get(1).isEmpty()) {
			return null;
		} else {

			int kontoZahl = kontoInfo.get(0).size();

			zB.setCurKonto(Integer.valueOf(kontoInfo.get(0).get(kontoZahl - 1)));
			zB.setCurIban(kontoInfo.get(1).get(kontoZahl - 1));
			ArrayList<Integer> kontoIds = new ArrayList<Integer>();

			for (String s : kontoInfo.get(0)) {
				kontoIds.add(Integer.valueOf(s));
			}
			zB.setKontoIbans(kontoInfo.get(1));
			zB.setKonten(kontoIds);

			return zB;

		}

	}


	/**
	 * 
	 * Erst im Nachhinein wurde klar, dass man auch genau so
	 * den Nutzer eine beliebige IBAN angeben lassen könnte
	 * 
	 * Erstellt eine IBAN aus Präfix und konto_id(auch wenn das nicht gefragt ist)
	 * Damit die IBAN einmalig ist wird sie aus der konto_id erstellt
	 * 
	 * @param kontoId
	 * @return IBAN als String
	 */
	public static String machNeueIban(int kontoId) {
		String iban = null;

		int ziffernAnzahl = String.valueOf(kontoId).length();

		String nullenDavor = "0";

		// je nachdem wie lange die neue KontoID ist, desto weniger 0en in IBAN

		for (int i = 1; i < 9 - ziffernAnzahl; i++) {
			nullenDavor += "0";

		}

		iban = "DE69105506670" + nullenDavor + String.valueOf(kontoId);

		return iban;
	}

	/**
	 * Loesche Konto eines Users
	 * 
	 * @param user
	 * @param kId
	 * @return ob loeschen erfolgreich
	 */
	public static boolean loescheKonto(Benutzer user, int kId) {
		boolean erfolg = false;

		if (KontoDatabase.loesche(kId)) {
			if (TransaktionsDatabase.loescheUeberweisungenZu(kId)) {
				erfolg = true;
			}

		}

		return erfolg;
	}

}
