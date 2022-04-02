package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import benutzer.Benutzer;

/**
 * Zustaendig fuer die Interaktion mit den Datenbanken um Nutzer hinzuzufuegen
 * oder Login Daten zu ueberpruefen, sowie Daten im Profil zu aendern.
 * 
 * @author Tobias Liebl
 *
 */
public class BenutzerDatabase {

	private static Connection con = null;

	/**
	 * Methode die ueberprueft ob E-Mail schon in Datenbank ist
	 * 
	 * @param email
	 * @return ob Email schon vorhanden
	 */
	public static boolean emailBekannt(String email) {
		boolean emailGibtsSchon = false;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM benutzer WHERE email = ? ;");

			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				emailGibtsSchon = true;
			}

		} catch (SQLException s) {

		} finally {
			try {

				con.close();
			} catch (SQLException s) {
				System.err.println("[SQL] Fehler bei Connection close" + s);
			}
		}
		return emailGibtsSchon;

	}

	/**
	 * Fuegt einen Nutzer in die Datenbank ein und gibt eine Meldung zurueck, ob
	 * dies geklappt hat oder warum nicht. Autocommit aus, damit ein Rollback noch
	 * durch -gefuehrt werden kann wenn beim Insert PW nach dem User etwas schief
	 * geht.
	 * 
	 * @param user
	 * @return
	 */
	public static Benutzer insertUser(Benutzer user) {
		Benutzer anmeldeVersuch = null;
		if (getBenutzerId(user.getEmail()) >= 0) {
			return anmeldeVersuch;
		} else {
			try {
				con = DatabaseConnection.getConnection();
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(
						"INSERT INTO benutzer VALUES (nextval('benutzer_benutzerid_seq'), ?, ?, ?, ?, ?);");
				PreparedStatement pwstmt = con
						.prepareStatement("INSERT INTO passwoerter VALUES (currval('benutzer_benutzerid_seq'), ?);");
				pstmt.setString(1, user.getEmail());
				pstmt.setString(2, user.getVorname());
				pstmt.setString(3, user.getNachname());
				pstmt.setInt(4, user.getAlter());
				pstmt.setString(5, user.getRolle());

				pwstmt.setString(1, user.getPasswort());
				int sql_return1 = pstmt.executeUpdate();
				int sql_return2 = pwstmt.executeUpdate();
				if (sql_return1 > 0 && sql_return2 > 0) {
					anmeldeVersuch = user;
				}

			} catch (SQLException s) {
				System.err.println("[SQL] Fehler bei insertUser " + s);
				try {
					con.rollback();
				} catch (SQLException ess) {
					System.err.println("[SQL] Fehler bei Rollback von insertUser " + ess);
				}
			}

			finally {
				try {
					con.commit();

					con.close();
				} catch (SQLException s) {
					System.err.println("[SQL] Fehler bei Connection close" + s);
				}
			}
			return anmeldeVersuch;
		}
	}

	/**
	 * Methode die einen Nutzer mit gegebener @param:Email loescht
	 * 
	 * @param email
	 * @return obe geloescht oder nicht
	 */
	public static boolean userLoeschen(String email) {
		boolean geloescht = false;
		int delId = getBenutzerId(email);

		if (delId > 0) {
			try {
				con = DatabaseConnection.getConnection();
				con.setAutoCommit(false);
				PreparedStatement pStmt = con.prepareStatement("DELETE from benutzer WHERE benutzer_id = ?;");
				pStmt.setInt(1, delId);

				PreparedStatement pwStmt = con.prepareStatement("DELETE from passwoerter WHERE benutzer_id = ?;");
				pwStmt.setInt(1, delId);

				int sql_return1 = pStmt.executeUpdate();
				int sql_return2 = pwStmt.executeUpdate();
				if (sql_return1 > 0 && sql_return2 > 0) {
					geloescht = true;
				}

			} catch (SQLException s) {
				System.err.println("[SQL] Fehler bei userLoeschen " + s);
				try {
					con.rollback();
				} catch (SQLException ess) {
					System.err.println("[SQL] Fehler bei Rollback von userLoeschen " + ess);
				}
			}

			finally {
				try {
					con.commit();
					con.close();
				} catch (SQLException s) {
					System.err.println("[SQL] userLoeschen Fehler bei Connection close" + s);
				}
			}
		}
		return geloescht;
	}

	/**
	 * Gleicht Logindaten mit gespeicherten ab.
	 * 
	 * @param email
	 * @param password
	 * @return ob Datenabgleich erfolgreich
	 */
	public static boolean checkLogin(String email, String password) {
		// ResultSet treffer;
		boolean erfolg = false;
		// Benutzer benutzer = null;
		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement pstmt = con.prepareStatement(
					"SELECT * FROM benutzer NATURAL JOIN passwoerter WHERE email = ? AND passwort = ?;");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				erfolg = true;
			}
		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei login(Benutzer benutzer)" + e.toString());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println(
						"[SQL] Fehler bei checkLogin(Benutzer benutzer) - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;
	}

	/**
	 * Methode die einen Nutzer in der Session updated
	 * 
	 * @param email
	 * @return upgedateten Nutzer
	 */
	public static Benutzer updateBenutzer(String email) {
		Benutzer benutzer = null;
		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT * FROM benutzer NATURAL JOIN passwoerter WHERE email = ?;");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();

			benutzer = new Benutzer(email);

			// Falls es einen Eintrag dazu gibt, war die Eingabe offensichtlich korrekt
			// (erfolgreicher login)
			if (rs.next()) {
				benutzer.setEmail(rs.getString("email"));
				benutzer.setPasswort(rs.getString("passwort"));
				benutzer.setRolle(rs.getString("rolle"));
				benutzer.setVorname(rs.getString("vorname"));
				benutzer.setNachname(rs.getString("nachname"));
				benutzer.setAlter(rs.getInt("alter"));
				benutzer.setMeiEidi(rs.getInt("benutzer_id"));
			}

		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei updateBenutzer(Benutzer benutzer)" + e.toString());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei updateBenutzer(Benutzer benutzer) - Verbindung geschlossen(?)"
						+ e.toString());
			}
		}

		return benutzer;
	}



	/**
	 * Eine Funktion mit der die Daten von Nutzerprofilen geaendert werden koennen
	 * Prueft, ob bei einer Emailaenderung diese bereits in der Datenbank vorhanden
	 * ist.
	 * 
	 * @param user
	 * @return ob Userdaten geaendert
	 */
	public static boolean updateUser(Benutzer user) {
		boolean erfolg = false;

		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE benutzer SET "
					+ " email = ?, vorname = ?, nachname = ?, alter = ?, " + " rolle = ? WHERE benutzer_id = ?;");
			pstmt.setString(1, user.getEmail());
			pstmt.setString(2, user.getVorname());
			pstmt.setString(3, user.getNachname());
			pstmt.setInt(4, user.getAlter());
			pstmt.setString(5, user.getRolle());
			pstmt.setInt(6, user.getMeiEidi());

			pstmt.executeUpdate();

		} catch (SQLException s) {
			System.err.println("[SQL] Fehler bei updettUser " + s);

		} finally {
			try {
				con.close();
				erfolg = true;

			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei bdb-update() - Verbindung geschlossen(?)" + e.toString());
			}
		}

		return erfolg;
	}

	/**
	 * Da es oefter noetig wurde, wurde das Checken eine Mailadresse schon in der
	 * Datenbank gespeichert ist auf diese extra Funktion ausgelagert relevant fuer
	 * insert und update User
	 * 
	 * @param email
	 * @return die BenutzerID eines neuangelegten Nutzers
	 */
	 
	public static int getBenutzerId(String email) {
		ResultSet treffer;
		int benutzerId = -1;
		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM benutzer WHERE email=?");
			pstmt.setString(1, email);
			treffer = pstmt.executeQuery();
			if (treffer.next()) {
				benutzerId = Integer.valueOf(treffer.getString("benutzer_id"));
			}
		} catch (SQLException s) {
			System.err.println("[SQL] Fehler bei insertUser " + s);
		}
		return benutzerId;
	}

}
