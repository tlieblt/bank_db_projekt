package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;

import benutzer.Benutzer;

/**
 * Zustaendig fuer die Interaktion mit den Datenbanken um Nutzer hinzuzufuegen
 * oder Login Daten zu ueberpruefen, sowie Daten im Profil zu aendern.
 * 
 * @author Tobias Liebl
 */
public class KontoDatabase {

	private static Connection con = null;

	/**
	 * Wird beim Registrieren oder von einem aktiven Nutzer @see KontoErstellen
	 * aufgerufen
	 * 
	 * @param Userobjekt in der Session wird übergeben um seine ID als Referenz für
	 *                   das Konto mit anlegen zu können
	 * 
	 * @return ob loeschen erfolgreich war
	 */

	public static boolean loesche(int kId) {

		boolean erfolg = false;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement neuesKonto = con.prepareStatement("DELETE FROM konten WHERE konto_id = ? ;");

			// für große Userbases setLong
			neuesKonto.setInt(1, kId);
			erfolg = !neuesKonto.execute();

			// gab es bis hierhin keine Exceptions hat die Kontoerstellung funktioniert

		} catch (SQLException e) {
			System.err.println("[SQL]KontoDB Fehler bei loesche()" + e.toString());
		}

		finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println("[SQL]KontoDB Fehler bei loesche() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;

	}

	/**
	 * Erstelle neues Konto und fuege es in Datenbank ein
	 * 
	 * @param benutzer
	 * @return ob erstellen erfolgreich war
	 */
	public static boolean erstelleNeuesKonto(Benutzer benutzer) {
		boolean erfolg = false;
		try {
			con = DatabaseConnection.getConnection();

			if (benutzer == null) {
				return erfolg;
			}
			int eiDi = benutzer.getMeiEidi();

			/*
			 * Beim Erstellen eines neuen Kontos wird die IBAN aus der ID generiert deswegen
			 * mit temporären Wert "kommtNoch" initialisieren
			 */
			PreparedStatement neuesKonto = con
					.prepareStatement("INSERT INTO konten (konto_id, zugehoerige_benutzer_id, iban) "
							+ " VALUES (nextVal('konten_kontoid_seq'), ?, 'kommtNoch');");

			// für große Userbases setLong
			neuesKonto.setInt(1, eiDi);
			neuesKonto.executeUpdate();

			// gab es bis hierhin keine Exceptions hat die Kontoerstellung funktioniert
			erfolg = true;

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei erstelleNeuesKonto()" + e.toString());
		} catch (IllegalArgumentException i) {
			System.err.println("Kontonummer konnte nicht erstellt werden!" + i.toString());

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei erstelleNeuesKonto() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;
	}

	/**
	 * Generiert eine Iban für ein neues Konto und setzt den DB-Eintrag Aufgerufen
	 * von:
	 * 
	 * @see KontoErstellen
	 * @see RegistrierungsManager
	 * 
	 * @param iban
	 * @param konto_id
	 * @return ob neue IBAN erfolgreich
	 * @throws IllegalArgumentException
	 *
	 */
	public static boolean gibKontoNeueIban(String iban, int konto_id) throws IllegalArgumentException {
		boolean erfolg = false;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement nummerAnpassen = con.prepareStatement("UPDATE konten SET iban=? WHERE konto_id=?");

			nummerAnpassen.setString(1, iban);
			nummerAnpassen.setInt(2, konto_id);

			nummerAnpassen.executeUpdate();

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei  DB getNeueKontoIban() " + e.toString());
		} finally {
			try {
				con.close();
				erfolg = true;
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei DB getNeueKontoIban() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;
	}

	/*
	
	 */
	/**
	 * Erzeugt zwei Arraylists mit den Kontoinformationen IDs und Ibans für
	 * einen @param benutzerId - wichtig vor allem beim Neuerstellen von Kontos &
	 * beim Registrieren (automatisch neues Konto)
	 * 
	 * @param benutzerId
	 * @return
	 */
	public static ArrayList<ArrayList<String>> getKontoInfoZuBenutzerID(int benutzerId) {
		ResultSet konten;
		ArrayList<ArrayList<String>> mehr = new ArrayList<ArrayList<String>>();
		ArrayList<String> kIdArray = new ArrayList<String>();
		ArrayList<String> kIbanArray = new ArrayList<String>();

		try {

			// wir holen die nach ID sortierten Konten die dem Nutzer mit benutzerId gehören
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select konto_id, iban from konten where " + "zugehoerige_benutzer_id=? order by konto_id");

			pstmt.setInt(1, benutzerId);

			konten = pstmt.executeQuery();

			while (konten.next()) {
				kIdArray.add(String.valueOf(konten.getInt("konto_id")));
				kIbanArray.add(konten.getString("iban"));

			}
			//
			mehr.add(kIdArray);
			mehr.add(kIbanArray);

		} catch (SQLException s) {
			System.err.println("[SQL] Fehler bei KDB-getKontoIDsZuBenutzerID " + s);
		}

		finally {
			try {
				con.close();

			} catch (SQLException e) {
				System.err.println(
						"[SQL] Fehler bei kdb-getKontoIdsZuBenutzerID() - Verbindung geschlossen(?)" + e.toString());
			}
		}

		return mehr;

	}
}
