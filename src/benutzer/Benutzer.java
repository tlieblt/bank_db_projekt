package benutzer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Nutzerklasse die die grundlegenden Eigenschaften eines Nutzers speichern kann
 * und Methoden zur Interaktion mit Nutzerinstanzen bereitstellt. Getter &
 * Setter Methoden fuer alle Attribute.
 * 
 * @author Tobias Liebl
 *
 */
public class Benutzer extends SimpleTagSupport {
	// Benutzerkonstruktor der bei fehlerhaften Logins benutzt wird um die
	// Fehlermeldung zu uebermitteln
	public Benutzer(String email) {
		this.email = email;
	}

	public Benutzer(String email, String vorname, String nachname, String rolle, int alter) {
		this.email = email;
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setRolle(rolle);

	}

	public Benutzer(String email, String passwort, String vorname, String nachname, String rolle, int alter) {
		this.email = email;
		this.password = passwort;
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setRolle(rolle);
		this.setAlter(alter);

	}
	
	public String getEmail() {
		return email;
	}

	public String getPasswort() {
		return this.password;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getRolle() {
		return rolle;
	}

	public void setRolle(String rolle) {
		this.rolle = rolle;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public boolean exists() {
		return true;
	}

	public void setPasswort(String passwort) {
		this.password = passwort;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCurKonto() {
		return curKontoId;
	}

	public void setCurKonto(int currKonto) {
		this.curKontoId = currKonto;
	}

	public String getCurIban() {
		return curIban;
	}

	public void setCurIban(String currIban) {
		this.curIban = currIban;
	}

	public int getMeiEidi() {
		return meiEid;
	}

	public void setMeiEidi(int meiEid) {
		this.meiEid = meiEid;
	}

	public ArrayList<Ueberweisung> getSuchErgebnisse() {
		return suchErgebnisse;
	}

	public void setSuchErgebnisse(ArrayList<Ueberweisung> suchErgebnisse) {
		this.suchErgebnisse = suchErgebnisse;
	}

	public ArrayList<Integer> getKonten() {
		return kontoIds;
	}

	public void setKonten(ArrayList<Integer> konten) {
		this.kontoIds = konten;
	}

	public ArrayList<Ueberweisung> getCurKontoUeberweisungen() {
		return curKontoUeberweisungen;
	}

	public void setCurKontoueberweisungen(ArrayList<Ueberweisung> kontoUeberweisungen) {
		this.curKontoUeberweisungen = kontoUeberweisungen;
	}

	public Timestamp getAeltesteAktuelleUeberweisung() {
		// aktuell geladene Überweisungen

		if (this.curKontoUeberweisungen == null) {
			return new Timestamp(0);
		}

		if (this.curKontoUeberweisungen.isEmpty()) {
			return new Timestamp(0);
		}

		Ueberweisung letzteUeberweisung = this.curKontoUeberweisungen.get(this.curKontoUeberweisungen.size() - 1);
		return letzteUeberweisung.getTimestamp();

	}

	public boolean hatKeinKonto() {
		if (this.kontoIbans == null || this.kontoIbans.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> getKontoIbans() {
		return kontoIbans;
	}

	public void setKontoIbans(ArrayList<String> kontoIbans) {
		this.kontoIbans = kontoIbans;
	}

	public HashMap<String, String[]> getKategorien() {
		return kategorien;
	}

	public ArrayList<String> getKategorieNamen() {
		ArrayList<String> namen = new ArrayList<String>();
		if(this.kategorien == null || this.kategorien.isEmpty()) {
			return namen;
		}
		else {
			for (var entry : kategorien.entrySet()) {	
				namen.add(entry.getKey());
			}
			return namen;
		}
		
		
	}

	public void setKategorien(HashMap<String, String[]> kategorien) {
		this.kategorien = kategorien;
	}

	public String[] getKategorienElemente(String kategorieName) {
		if (this.kategorien == null || !this.kategorien.containsKey(kategorieName)) {
			String[] leer = {};
			return leer;
		} else {
			return this.kategorien.get(kategorieName);

		}
	}

	private String email;
	private String password;
	private String vorname;
	private String nachname;
	private String rolle;
	private int curKontoId;
	private String curIban;

	private int meiEid;
	private int alter;

	private ArrayList<Integer> kontoIds;
	private ArrayList<String> kontoIbans;
	private ArrayList<Ueberweisung> suchErgebnisse;
	private ArrayList<Ueberweisung> curKontoUeberweisungen;
	private HashMap<String, String[]> kategorien;
	
	
}
