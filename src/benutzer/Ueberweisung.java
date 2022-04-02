package benutzer;

import java.sql.Timestamp;
import java.util.Comparator;

import manager.FehlerManager;

/* 
 * @author 
 * 

*/

/**
 * Alle Daten die in der DB liegen werden in einer korrespondierenden
 * Überweisung geschrieben Nutzer verwaltet seine Suchergebnisse und die
 * Überweisungen des aktuellen Kontos in ArrayLists<Ueberweisung> Für alle
 * Attribute gibt es Getter& Setter Methoden
 * 
 * @author Tobias Liebl
 *
 */
public class Ueberweisung {

	public Ueberweisung(String fehler) {
		if (fehler == "keine") {
			this.setBetreff("Keine Überweisungsdaten verfügbar.");
		} else
			this.setBetreff(fehler);

	}

	public Ueberweisung(int uid, int btrg, int eid, String vid, String vib, String btrf, Timestamp tmstmp) {
		this.setUeberweisungs_id(uid);
		this.setBetrag(btrg);
		this.setEmpfaenger_konto_id(eid);
		this.setVersender(vid);
		this.setBetreff(btrf);
		this.setVersender_iban(vib);
		this.setTimestamp(tmstmp);
	}

	public Ueberweisung(int btrg, String vib, String btrf, Timestamp tmstmp) {
		this.setBetrag(btrg);
		this.setBetreff(btrf);
		this.setVersender_iban(vib);
		this.setTimestamp(tmstmp);
	}


	public class SortBetrag implements Comparator<Ueberweisung> {

	    
	    public int compare(Ueberweisung a, Ueberweisung b)
	    {
	        return a.betrag - b.betrag;
	    }
	}	
	
	public class SortBetreff implements Comparator<Ueberweisung> {

	    
	    public int compare(Ueberweisung a, Ueberweisung b)
	    {
	        return a.betreff.toLowerCase().compareTo(b.betreff.toLowerCase());
	    }
	}
	
	public class SortNameAnderePartei implements Comparator<Ueberweisung> {

	    
	    public int compare(Ueberweisung a, Ueberweisung b)
	    {
	        return a.versender.toLowerCase().compareTo(b.versender.toLowerCase());
	    }
	}
	
	public class SortIBAN implements Comparator<Ueberweisung> {

	    public int compare(Ueberweisung a, Ueberweisung b)
	    {
	        return a.versender_iban.toLowerCase().compareTo(b.versender_iban.toLowerCase());
	    }
	}
	
	public class SortDatum implements Comparator<Ueberweisung> {

	    
	    public int compare(Ueberweisung a, Ueberweisung b)
	    {
	        return a.timestamp.compareTo(b.timestamp);
	    }
	}
	

	
	public int getUeberweisungs_id() {
		return ueberweisungs_id;
	}

	public void setUeberweisungs_id(int ueberweisungs_id) {
		this.ueberweisungs_id = ueberweisungs_id;
	}

	public int getBetrag() {
		return betrag;
	}


	public String getEuroBetrag() {
		return FehlerManager.machBetragAusInt(betrag);
	}
	
	public void setBetrag(int betrag) {
		this.betrag = betrag;
	}

	public int getEmpfaenger_konto_id() {
		return empfaenger_konto_id;
	}

	public void setEmpfaenger_konto_id(int empfaenger_konto_id) {
		this.empfaenger_konto_id = empfaenger_konto_id;
	}

	public String getVersender() {
		return versender;
	}

	public void setVersender(String versender) {
		this.versender = versender;
	}

	public String getBetreff() {
		return betreff;
	}

	public void setBetreff(String betreff) {
		this.betreff = betreff;
	}

	public String getVersender_iban() {
		return versender_iban;
	}

	public void setVersender_iban(String versender_iban) {
		this.versender_iban = versender_iban;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	

	public String getKategorien() {
		return kategorien;
	}

	public void setKategorien(String kategorien) {
		this.kategorien = kategorien;
	}


	private int ueberweisungs_id;
	private int betrag;
	private int empfaenger_konto_id;
	private String versender;
	private String betreff;
	private String versender_iban;
	private String kategorien;
	private Timestamp timestamp;

}
