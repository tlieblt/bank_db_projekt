package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.stream.LongStream;
import benutzer.Benutzer;
import benutzer.Ueberweisung;
import manager.UeberweisungsManager;

/**
 * Klasse fuer Verwaltung der Transaktionen
 * 
 * @author Tobias Liebl
 *
 */
public class TransaktionsDatabase {

	private static Connection con = null;
	
	/**
	 * 
	 * Aus der Datenbank werden nach gewünschter Sortierung die Einträge bis zum
	 * Timestamp letzteueberweisung gesucht und zurückgegeben
	 * 
	 * @param userKonto
	 * @param letzteUeberweisung
	 * @param sortierungNach
	 * @param sortierungsArt
	 * @return
	 */
	
	public static ArrayList<Ueberweisung> holSortierteÜberweisungen(int userKonto, Timestamp letzteUeberweisung, String sortierungNach, String sortierungsArt) {
		ArrayList<Ueberweisung> sortierteUeberweisungen = new ArrayList<Ueberweisung>();

		//möchte der Nutzer	
		if( letzteUeberweisung == null ) {
			letzteUeberweisung = new Timestamp(0);
		}
		
		//Standardmäßig nach Betrag (falls Input nicht zugeordnet werden kann)
		String sortierNach = "betrag";
		
		//Standardeinstellung ist umgekehrte Reihenfolger - wie spezifiziert
		String sort = " DESC";
		
		//damit wir nicht irgendeinen Input aus dem 
		//Request einfach direkt an das PSTMT hängen
		//Reihenfolge aufsteigend nur wenn es explizit gewünscht wird
		if( sortierungsArt.equals("asc") ) {
			sort = " ASC";
		}
				
		try {
		
		con = DatabaseConnection.getConnection();

		
		//Alle String Einträge werden nach Kleinbuchstaben sortiert
		//sonst sortiert ist ein Kleinbuchstabe immer kleiner als
		//ein Großbuchstabe und in der Sortierung will man 
		//ja nur alphabetische Sortierung und nicht nach Großschreibung
		if( sortierungNach.equals("name") ) {			
			sortierNach = " LOWER (name_andere_partei) ";
		}
		
		else if( sortierungNach.equals("iban") ) {
			sortierNach = " LOWER (iban_andere_partei) ";
		}
		else if( sortierungNach.equals("betreff") ) {
			sortierNach = " LOWER (betreff) ";
		}
		else if(sortierungNach.equals("datum")) {
			sortierNach = " timestamp ";
		}
			
			PreparedStatement namensSuche = con.prepareStatement("SELECT * FROM kontotransaktionen "
					+ " WHERE benutzer_konto_id = ?  AND timestamp > ? ORDER BY " + sortierNach + sort );
			
				namensSuche.setInt(1, userKonto);
				namensSuche.setTimestamp(2, letzteUeberweisung);
				
				ResultSet sortiertNachName = namensSuche.executeQuery();
				
				while(sortiertNachName.next()) {
					
					Ueberweisung temp = new Ueberweisung(sortiertNachName.getInt(1), sortiertNachName.getInt(6), userKonto, sortiertNachName.getString(3),
							sortiertNachName.getString(4), sortiertNachName.getString(5), sortiertNachName.getTimestamp(7));
	
					sortierteUeberweisungen.add(temp);
				
				}

	}
	catch(SQLException ex) {
		
	}
		
		return sortierteUeberweisungen;

	}
	
	/**
	 * Erzeugt einen Datenbankeneintrag für eine spezifische Überweisung
	 * 
	 * @param benutzer
	 * @param name_andere_partei
	 * @param iban_andere_partei
	 * @param verwendungszweck
	 * @param betrag
	 * @param datum
	 * @return ob erstellen der Transaktion erfolgreich
	 */
	
	public static boolean erstelleEineTransaktion(Benutzer benutzer, String name_andere_partei,
			String iban_andere_partei, String verwendungszweck, int betrag, Timestamp datum) {
		boolean erfolg = false;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement neueTransaktion = con.prepareStatement(
					"INSERT INTO kontotransaktionen (ueberweisungs_id, benutzer_konto_id, name_andere_partei, iban_andere_partei, betreff, betrag, timestamp) VALUES (nextVal('kontotransaktionen_transaktionsid_seq'), ?, ?, ?, ?, ? ,?);");
			String derBetrag = String.valueOf(betrag);
			int betrag2 = Integer.parseInt(derBetrag);

			neueTransaktion.setInt(1, (Integer.valueOf(benutzer.getCurKonto())));
			neueTransaktion.setString(2, name_andere_partei);
			neueTransaktion.setString(3, iban_andere_partei);
			neueTransaktion.setString(4, verwendungszweck);
			neueTransaktion.setInt(5, betrag2);
			neueTransaktion.setTimestamp(6, datum);
			neueTransaktion.executeUpdate();

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB- erstelleEineTransaktionen()" + e.toString());
		} finally {
			try {
				con.close();
				erfolg = true;
			} catch (SQLException e) {
				System.err.println(
						"[SQL] Fehler bei TDB-erstelleEineTransaktion() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;
	}


	/**
	 * Erstellt @param transaktionsZahl neue Überweisungen und fügt diese in die DB
	 * ein
	 * 
	 * @param benutzer.getCurKontoID() ist das aktuelle Referenzkonto dem die
	 *                                 Überweisungen zugeordnet werden
	 * @param benutzer
	 * @param transaktionsZahl
	 * @return
	 */
	public static boolean erstelleNeueZufallsTransaktionen(Benutzer benutzer, int transaktionsZahl) {
		boolean erfolg = false;

		try {
			con = DatabaseConnection.getConnection();
			// zufaellige Timestamps generieren
			Random zeitenAendern = new Random();
			LongStream zufaelligeZeitintervalle = zeitenAendern.longs(transaktionsZahl,
					System.currentTimeMillis() - 31536000000L, System.currentTimeMillis());

			long[] zufallsZeiten = zufaelligeZeitintervalle.toArray();

			String[][] daten = UeberweisungsManager.machZufallsTransaktionsDaten(transaktionsZahl);
			// machZufallsTransaktionsDaten(transaktionsZahl);

			System.out.println("current kontonr is " + Integer.valueOf(benutzer.getCurKonto()));
			for (int i = 0; i < transaktionsZahl; i++) {

				con.setAutoCommit(false);

				Timestamp irjendwann = new Timestamp(zufallsZeiten[i]);

				PreparedStatement neueTransaktion = con.prepareStatement(
						"INSERT INTO kontotransaktionen (ueberweisungs_id, benutzer_konto_id, name_andere_partei, iban_andere_partei, betreff, betrag, timestamp) VALUES (nextVal('kontotransaktionen_transaktionsid_seq'), ?, ?, ?, ?, ? ,?);");

				int betrag = Integer.parseInt(daten[i][2]);
				neueTransaktion.setInt(1, (Integer.valueOf(benutzer.getCurKonto())));
				neueTransaktion.setString(2, daten[i][0]);
				neueTransaktion.setString(3, daten[i][1]);
				neueTransaktion.setString(4, "");
				neueTransaktion.setInt(5, betrag);
				neueTransaktion.setTimestamp(6, irjendwann);
				neueTransaktion.executeUpdate();
			}
			// gab es bis hierhin keine Exceptions hat alles funktioniert

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB-erstelleNeueZufallsTransaktionen()" + e.toString());

		} finally {
			try {
				con.commit();
				con.setAutoCommit(true);
				con.close();
				erfolg = true;

			} catch (SQLException e) {
				System.err.println(
						"[SQL] Fehler bei TDB-erstelleNeueZufallsTransis() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		System.err.println("transaktion erstellen erfolg ist: " + erfolg);
		return erfolg;
	}

	/**
	 * hol alle Transaktionen
	 * 
	 * @param anzahl
	 * @param konto_id
	 * @return die Transaktionen
	 */
	public static ArrayList<Ueberweisung> getTransaktionen(int anzahl, int konto_id) {

		ArrayList<Ueberweisung> transis = new ArrayList<Ueberweisung>();

		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM kontotransaktionen WHERE benutzer_konto_id = ? ORDER BY timestamp DESC LIMIT ?");

			pstmt.setInt(1, konto_id);
			pstmt.setInt(2, anzahl);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				Ueberweisung temp = new Ueberweisung(res.getInt(1), res.getInt(6), konto_id, res.getString(3),
						res.getString(4), res.getString(5), res.getTimestamp(7));

				transis.add(temp);
			}

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB- getTransaktionen" + e.toString());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err
						.println("[SQL] Fehler bei TDB - getTransaktionen - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return transis;

	}

	/**
	 * Holt auf Wunsch alle Transaktionen für ein Konto
	 * 
	 * @param konto_id
	 * @return die Transaktionen
	 */
	public static ArrayList<Ueberweisung> getAlleTransaktionen(int konto_id) {
		ArrayList<Ueberweisung> transaktionen = new ArrayList<Ueberweisung>();
		try {

			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM kontotransaktionen WHERE" + " benutzer_konto_id = ?  ORDER BY timestamp desc");

			pstmt.setInt(1, konto_id);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				// int value = Integer.valueOf(res.getString(6));
				Ueberweisung temp = new Ueberweisung(res.getInt(1), res.getInt(6), konto_id, res.getString(3),
						res.getString(4), res.getString(5), res.getTimestamp(7));

				transaktionen.add(temp);
			}

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB getAlleTransaktionen()" + e.toString());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println(
						"[SQL] Fehler bei TDB getAlleTransaktionen() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return transaktionen;

	}

	/**
	 * Hol aeltere Transaktionen
	 * 
	 * @param anzahl
	 * @param konto_id
	 * @param currAelteste
	 * @return die Transaktionen die aelter sind
	 */
	public static ArrayList<Ueberweisung> getAeltereTransaktionen(int anzahl, int konto_id, Timestamp currAelteste) {
		ArrayList<Ueberweisung> transis = new ArrayList<Ueberweisung>();

		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT * FROM kontotransaktionen WHERE benutzer_konto_id = ? "
							+ "and timestamp < ? ORDER BY timestamp DESC LIMIT ?");

			pstmt.setInt(1, konto_id);
			pstmt.setTimestamp(2, currAelteste);
			pstmt.setInt(3, anzahl);
			ResultSet res = pstmt.executeQuery();

			while (res.next()) {
				Ueberweisung temp = new Ueberweisung(res.getInt(1), res.getInt(6), konto_id, res.getString(3),
						res.getString(4), res.getString(5), res.getTimestamp(7));

				transis.add(temp);
			}

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB- getAeltereTransaktionen()" + e.toString());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei TDB - getAeltereTr.(Benutzer benutzer) - Verbindung geschlossen(?)"
						+ e.toString());
			}
		}
		return transis;

	}

	/**
	 * Suche fuer Transaktionen
	 * 
	 * @param user  der sucht - beinhaltet curKontoId zu dem Überweisungen gesucht
	 *              werden
	 * @param suche - die "vorgespülten" Suchbegriffe, Aufbereitung der Suchbefehle
	 *              geschieht in @see UeberweisungsManager
	 * @return die Suchergebnisse
	 */

	public static boolean transaktionsSuche(Benutzer user, HashMap<String, String> suche) {
		boolean sucherfolg = false;

		/*
		 * Wurde eine Suchdimension nicht angegeben, wird implizit angenommen dass bspw.
		 * die komplette Zeitspanne sozusagen ohne Einschränkung gewünscht ist
		 * Transaktionen über 2 Mrd. müssen dann in zwei Tranchen gemacht werden :)
		 */

		int bMax = Integer.valueOf(suche.get("hoherBetrag"));

		int bMin = Integer.valueOf(suche.get("niedrigerBetrag"));

		// Timestamp tMin = new Timestamp(0);
		Timestamp tMin = new Timestamp(Long.valueOf(suche.get("zeitBeginn")));
		// Timestamp tMax= new Timestamp(System.currentTimeMillis());
		Timestamp tMax = new Timestamp(Long.valueOf(suche.get("zeitEnde")));

		String ibanAnderePartei = suche.get("ibanAnderePartei");
		String betreff = suche.get("betreff");

		if (ibanAnderePartei == null) {
			ibanAnderePartei = "";
		}
		if (betreff == null) {
			betreff = "";
		}

		/*
		 * % Zeichen kennzeichnet, dass nach Teilbegriff als SQL Wildcard gesucht wird -
		 * der String ist gewhitelistet also kann man ihm hoffentlich so weit trauen
		 * ansonsten wird er ja zusätzlich sowieso als preparedStatement sicher gesetzt
		 * Achtung Groß-Kleinschreibung wird gematcht
		 */
		ibanAnderePartei = "%" + ibanAnderePartei + "%";

		betreff = "%" + betreff + "%";

		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT * FROM kontotransaktionen WHERE benutzer_konto_id = ? "
							+ "AND timestamp > ? AND timestamp < ? AND betreff like ? and "
							+ " name_andere_partei like ? and betrag < ? and betrag > ? ORDER BY timestamp DESC");

			pstmt.setInt(1, Integer.valueOf(user.getCurKonto()));
			pstmt.setTimestamp(2, tMin);
			pstmt.setTimestamp(3, tMax);
			pstmt.setString(4, betreff);
			pstmt.setString(5, ibanAnderePartei);
			pstmt.setInt(6, bMax);
			pstmt.setInt(7, bMin);

			ResultSet res = pstmt.executeQuery();

			ArrayList<Ueberweisung> suchErgebnisse = new ArrayList<Ueberweisung>();

			while (res.next()) {

				Ueberweisung temp = new Ueberweisung(res.getInt(1), res.getInt(6), user.getMeiEidi(), res.getString(3),
						res.getString(4), res.getString(5), res.getTimestamp(7));

				suchErgebnisse.add(temp);
			}

			user.setSuchErgebnisse(suchErgebnisse);

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei TDB- suche" + e.toString());
		} finally {
			try {
				con.close();
				sucherfolg = true;
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei TDB- suche - Verbindung geschlossen(?)" + e.toString());
			}
		}

		return sucherfolg;
	}

	/**
	 * loesche Transaktionen eines Konto
	 * 
	 * @param kontoId
	 * @return ob loeschen erfolgreich
	 */
	public static boolean loescheUeberweisungenZu(int kontoId) {
		boolean erfolg = false;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement ueberweisungenWeg = con
					.prepareStatement("DELETE FROM kontotransaktionen WHERE benutzer_konto_id = ? ;");

			// für große Userbases setLong
			ueberweisungenWeg.setInt(1, kontoId);
			erfolg = !ueberweisungenWeg.execute();

			// gab es bis hierhin keine Exceptions hat die Kontoerstellung funktioniert
			System.out.println("Konto " + kontoId + " gelöscht");

		} catch (SQLException e) {
			System.err.println("[SQL]Tr-DB Fehler bei loescheUeberweisungen()" + e.toString());
		}

		finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println(
						"[SQL]Tr-DB Fehler bei loescheUeberweisungen() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;
	}

}
