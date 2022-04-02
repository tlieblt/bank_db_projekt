package database;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import benutzer.Benutzer;

/**
 * Klasse um die Verbindung zur SQL-Datenbank zu erstellen
 */
public class KategorieDB{

	private static Connection con = null;


	//Erstellt einen Kategorieeintrag in der DB für einen Benutzer
	public static boolean erstelleNeueKategorie(String kategorieName, Benutzer user) {
		boolean erfolg = false;

		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement pstmt = con.prepareStatement("insert into kategorien "
					+ " (kategorie_id, kat_ben_id, kategorie_elemente, kategorie_name) "
					+ "  values (nextVal('kategorien_kategorieid_seq'), ?, '{}', ? );");

			pstmt.setInt(1, user.getMeiEidi());
			pstmt.setString(2, kategorieName);
			pstmt.executeUpdate();

		}
		catch (SQLException sc) {

		}
		finally {
			try {
				//Ging beim Insert nix schief, hatten wir Erfolg
				con.close();
				erfolg = true;
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei erstelleNeuesKonto() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;

	}

	//Kategorieänderungen werden direkt im User zwischengespeichert
	//und dann gegettet und in die DB zu einer bestehenden
	//Kategorie geschrieben
	public static boolean updateKategorie(String kategorieName, Benutzer user) {
		boolean erfolg = false;

		HashMap<String,String[]> kategorienUpgedated = user.getKategorien();
		String[] kategorieElemente = kategorienUpgedated.get(kategorieName);

		try {
			con = DatabaseConnection.getConnection();

			PreparedStatement pstmt = con.prepareStatement("UPDATE kategorien SET kategorie_elemente = ? "
					+ " WHERE kat_ben_id = ? AND kategorie_name = ? ");

			pstmt.setArray(1, con.createArrayOf("VARCHAR", kategorieElemente));
			pstmt.setInt(2, user.getMeiEidi());
			pstmt.setString(3, kategorieName);

			int wot = pstmt.executeUpdate();

			//0 würde heißen etwas ging schief, sonst Erfolg
			if(wot>0) {
				erfolg = true;
			}

		} catch(Exception e) {
			System.err.println("Kat-DB updateKategorie "); 
			e.printStackTrace();

		}

		finally {
			try {
				con.close();
			}
			catch (Exception e) {
				System.err.println("Kat-DB updateKategorie - SQL Fehler Con Close ");
				e.printStackTrace();
			}
		}


		return erfolg;
	}


	//Erstellt einen Kategorieeintrag und sein erstes Element in der DB für einen Benutzer
	//Zur Initialisierung erstmal nur ein Begriff - mehr wäre auch möglich, aber Zeit...

	public static boolean erstelleNeueKategorie(String kategorieName, String kategorieElement, Benutzer user) {
		boolean erfolg = false;


		try {
			con = DatabaseConnection.getConnection();

			String[] kategorieElemente = user.getKategorienElemente(kategorieName);

			int arrayGroesse;
			if(kategorieElemente == null) {
				arrayGroesse = 0;
			}
			else {
				arrayGroesse = kategorieElemente.length; }
			int i=0;
			String[] jo = new String[arrayGroesse+1];
			if(arrayGroesse >0) {
				for(String katEl : kategorieElemente) {
					jo[i] = katEl;
					i++;
				}}
			jo[i] = kategorieElement;


			PreparedStatement pstmt = con.prepareStatement("insert into kategorien "
					+ " (kategorie_id, kat_ben_id, kategorie_elemente, kategorie_name) "
					+ "  values (nextVal('kategorien_kategorieid_seq'), ?, ?, ? );");

			pstmt.setInt(1, user.getMeiEidi());
			pstmt.setArray(2,  con.createArrayOf("VARCHAR", jo) );

			pstmt.setString(3, kategorieName);


			pstmt.executeUpdate();


		}
		catch (SQLException sc) {

		}
		finally {
			try {
				con.close();
				erfolg = true;
			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei erstelleNeuesKonto() - Verbindung geschlossen(?)" + e.toString());
			}
		}
		return erfolg;

	}

//speichert die aktuellen Kategorien im Nutzerobjekt
	public static Benutzer aktualisierNutzerKategorien(Benutzer user) {
		ResultSet kategorieBundle;
		HashMap<String,String[]> mehr = new HashMap<String,String[]>();

		try {

			//man holt die KategorieNamen und -Elemente die dem Nutzer mit benutzerId gehören
			con= DatabaseConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("select kategorie_name, kategorie_elemente from kategorien where "
					+ "kat_ben_id=? order by kategorie_name");

			pstmt.setInt(1, user.getMeiEidi());

			kategorieBundle = pstmt.executeQuery();


			// gehe alle Suchergebnisse des ResultSets durch:
			while(kategorieBundle.next()) {

				//holen uns den Kategorienamen
				String kategorieTMP = String.valueOf(kategorieBundle.getString("kategorie_name"));

				//die KategorieElemente in SQL-Array Form
				Array katEl = (kategorieBundle.getArray("kategorie_elemente"));

				// Wir casten die Daten 
				String[] arr = (String[]) katEl.getArray();

				//und packen sie in die Map mit dem korresp. Kategorienamen als Schlüssel
				mehr.put(kategorieTMP, arr);


			}

			//letztendlich noch das Nutzerobjekt updaten
			user.setKategorien(mehr);

		} catch (SQLException s) {
			System.err.println("[SQL] Fehler bei KDB-getKontoIDsZuBenutzerID " + s);
		}

		finally {
			try {
				con.close();

			} catch (SQLException e) {
				System.err.println("[SQL] Fehler bei kdb-getKontoIdsZuBenutzerID() - Verbindung geschlossen(?)" + e.toString());
			}
		}


		return user;

	}

	public static boolean loescheKategorie(String kategorieName, Benutzer user) {
		boolean erfolg = false;

		try {
			con = DatabaseConnection.getConnection();
			
			PreparedStatement idstmt = con.prepareStatement("SELECT kategorie_id FROM kategorien WHERE kat_ben_id = ? AND kategorie_name = ? ;");

			idstmt.setInt(1, user.getMeiEidi());
			idstmt.setString(2, kategorieName);
			
			ResultSet id = idstmt.executeQuery();
			
			if(id.next()) {
				int eidi = id.getInt("kategorie_id");
			
			
			
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM kategorien WHERE kategorie_id = ? ;");
			
			pstmt.setInt(1, eidi);
			
			//wurde gelöscht kommt ein leeres "ResultSet" zurück und pstmt.execute = false 
			erfolg = !pstmt.execute();
			}
		} catch (SQLException e) {
			System.err.println("[SQL] Fehler bei loescheKategorie " + e.getMessage());

		}
		finally {
			try {
				con.close();
			}
			catch(SQLException ex) {
				System.err.println("[SQL] Fehler bei conn Close von loescheKategorie " + ex.getStackTrace());

			}
		}
		
		return erfolg;	
	}



}