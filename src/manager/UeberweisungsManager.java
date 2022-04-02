package manager;

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.TransaktionsDatabase;

/**
 * Klasse die Methoden fuer Uberweisungen bereitstellt
 * 
 * @author Tobias Liebl
 *
 */
public class UeberweisungsManager {


	/**
	 * Hier werden die Daten nach denen gesucht werden soll aufbereitet bzw. logisch
	 * ergänzt wenn Felder fehlerhaft oder gar nicht ausgefüllt wurden
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */


	/**
	 * Setzt in den Überweisungen die Kategorien, je nachdem ob
	 * ein Teil des Betreffs oder Namens einem Kategorieelement
	 * entspricht
	 * 
	 * Liefert die kategorisierten Ueberweisungen zurück
	 * 
	 * @param in
	 * @param kategorien
	 * @return
	 */
	public static ArrayList<Ueberweisung> gibUeberweisungenKategorien(ArrayList<Ueberweisung> in, HashMap<String,String[]> kategorien) {


		for(Ueberweisung uebi : in) {

			if(kategorien == null || kategorien.size() == 0) {
				uebi.setKategorien("<a href='kategorien.jsp' class='link-success'>Kategorien anlegen</a>");
			}
			else {
				ArrayList<String> treffer = new ArrayList<String>();

				//Für alle Kategorieelemente prüfen ob die Überweisung dazu passt
				for (var entry : kategorien.entrySet()) {	

					for(String katEl : entry.getValue()) {
						if(uebi.getBetreff().contains(katEl) || uebi.getVersender().contains(katEl)) {
							//sobald eine Kategorie passt, merken wir uns die in treffer und stoppen die 
							// weitere Suche in dieser Kategorie -> es wird weiter nach den anderen Kat. gesucht
							treffer.add(entry.getKey());
							break;
						}
					}	
				}
				String kategorieTreffer ="";
				for(String trf : treffer) {
					kategorieTreffer += "Kategorie: " + trf +"; ";
				}

				//hat bisher kein KategorieElement zu der Überweisung gepasst
				// teilen wir das dem Benutzer mit

				if(treffer.isEmpty()) {

					kategorieTreffer = "<a href='kategorien.jsp'>Nicht kategorisiert.</a>";
				}
				uebi.setKategorien(kategorieTreffer);
			}}
		return in;
	}

	/**
	 * Zuerst wurde alles mit Java sortiert, dann ist aufgefallen, dass
	 * man im DB-Kurs vielleicht auch die Datenbank sortieren lassen sollte
	 * deshalb werden die zeitlich geordneten Kontoüberweisungen von der DB sortiert
	 * und die Suchergebnisse werden von Java sortiert - alternative Lösung:
	 * letzte Such-SQL-Suchabfrage im Nutzer speichern und statt am Ende
	 * nach Timestamp zu sortieren, könnte man den gewünschten 
	 * Suchbegriffe &-reihenfolge anhängen und sich auch von der DB ausgeben lassen
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void sortiereMitDb(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		String wasSortieren = request.getParameter("wasSortieren");

		Timestamp letzteUeberweisung = new Timestamp(0);

		HashMap<String, String[] > kategorien = user.getKategorien();

		ArrayList<Ueberweisung> sortierteUeberweisungen = new ArrayList<Ueberweisung>();

		String sortierungsReihenfolge = (String) request.getParameter("reihenfolge");
		String sortierungNach = (String) request.getParameter("sortierenNach");

		//Standardeinstellung: absteigend sortieren
		String naechsteSortierung ="desc";


		//Standardfall: Sortieren über Kontoüberweisungen
		if( wasSortieren == null || wasSortieren.equals("") || wasSortieren.equals("konto") ) {
			request.setAttribute("wasSortieren", "konto");

			letzteUeberweisung = user.getAeltesteAktuelleUeberweisung();

			//Initialisierung der Suchbegriffe, falls im Request 
			//(warum auch immer) etwas unerwartetes war
			if (sortierungNach == null || sortierungNach.equals("")) {
				sortierungNach = "betrag";		
			}
			//Nur "asc" wird in der Form mitgeteilt, sonst wird
			//angenommen, dass es descending sein soll
			if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {
				sortierungsReihenfolge = "desc";
			}

			if(sortierungsReihenfolge.equals("desc")) {
				naechsteSortierung = "asc";
			}
			else if(sortierungsReihenfolge.equals("asc")) {
				naechsteSortierung = "desc";
			}
			// Wird im sortieren.jsp benötigt um zu markieren wonach sortiert wurde
			// und was die nächste Sortierungsreihenfolge sein soll
			request.setAttribute(sortierungNach+"Hervorheben","btn-success");
			request.setAttribute(sortierungNach+"Reihenfolge", naechsteSortierung );


			sortierteUeberweisungen = TransaktionsDatabase.holSortierteÜberweisungen(user.getCurKonto(), letzteUeberweisung, sortierungNach, sortierungsReihenfolge);



			if(sortierteUeberweisungen == null || sortierteUeberweisungen.isEmpty()) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Die Überweisungen zu ihrem Konto sind leer - bitte erstellen Sie neue um Sortieren zu können!");

				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			}
			else {

				sortierteUeberweisungen = gibUeberweisungenKategorien(sortierteUeberweisungen, kategorien);



				request.setAttribute("sortierteUeberweisungen", sortierteUeberweisungen );
				request.getRequestDispatcher("sortieren.jsp").forward(request, response);
			}
		}

		else if( wasSortieren.equals("suche") ) {
			//für die Suchergebnisse
			ArrayList<Ueberweisung> zuSortieren = new ArrayList<Ueberweisung>();
			request.setAttribute("wasSortieren", "suche");

			zuSortieren = user.getSuchErgebnisse();
			Ueberweisung test = new Ueberweisung("");


			if(zuSortieren == null || zuSortieren.size() == 0) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Ihre Suchergebnisse waren leer - bitte suchen Sie erneut um die Suchergebnisse sortieren zu können.");

				request.getRequestDispatcher("suche.jsp").forward(request, response);
			}
			else {
				/*
				 * Wurde zuletzt nach betreff sortiert, wird der entsprechende 
				 * Button für Betreff grün hinterlegt und in dem Form die
				 * nächste Sortierungsreihenfolge geschrieben - leer ist desc
				 * 
				 * Analaog bei allen anderen Sortierkategorien
				 */

				if(sortierungNach.equals("betreff")) {	
					request.setAttribute("betreffHervorheben", "btn-success");

					if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {
						Collections.sort(zuSortieren, test.new SortBetreff().reversed());	
						request.setAttribute("betreffReihenfolge", "asc");
					}
					else if(sortierungsReihenfolge.equals("asc")) {
						Collections.sort(zuSortieren, test.new SortBetreff());
						request.setAttribute("betreffReihenfolge", "");

					}


				}

				else if(sortierungNach.equals("name")) {
					request.setAttribute("nameHervorheben", "btn-success");

					if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {

						Collections.sort(zuSortieren, test.new SortNameAnderePartei().reversed());	
						request.setAttribute("nameReihenfolge", "asc");

					}
					else if(sortierungsReihenfolge.equals("asc")) {
						Collections.sort(zuSortieren, test.new SortNameAnderePartei());
						request.setAttribute("nameReihenfolge", "");

					}

				}

				else if(sortierungNach.equals("betrag")) {
					request.setAttribute("betragHervorheben", "btn-success");

					if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {
						Collections.sort(zuSortieren,  test.new SortBetrag());
						request.setAttribute("betragReihenfolge", "asc");

					}
					else if(sortierungsReihenfolge.equals("asc")) {
						Collections.sort(zuSortieren, test.new SortBetrag().reversed());
						request.setAttribute("betragReihenfolge", "");

					}

				}

				else if(sortierungNach.equals("datum")) {
					request.setAttribute("datumHervorheben", "btn-success");

					if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {

						Collections.sort(zuSortieren, test.new SortDatum());
						request.setAttribute("datumReihenfolge", "asc");

					}
					else if(sortierungsReihenfolge.equals("asc")) {
						Collections.sort(zuSortieren, test.new SortDatum().reversed());
						request.setAttribute("datumReihenfolge", "");

					}
				}

				else if(sortierungNach.equals("iban")) {
					request.setAttribute("ibanHervorheben", "btn-success");

					if(sortierungsReihenfolge == null || sortierungsReihenfolge.equals("")) {

						Collections.sort(zuSortieren, test.new SortIBAN());
						request.setAttribute("ibanReihenfolge", "asc");

					}
					else if(sortierungsReihenfolge.equals("asc")) {
						Collections.sort(zuSortieren, test.new SortIBAN().reversed());
						request.setAttribute("ibanReihenfolge", "");

					}
				}

				zuSortieren = gibUeberweisungenKategorien(zuSortieren, kategorien);

				request.setAttribute("sortierteUeberweisungen", zuSortieren);

				request.getRequestDispatcher("sortieren.jsp").forward(request, response);	
			}		


		}
	}




	public static void suche(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		if (user == null) {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bitte zur Suche eingeloggen.");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
			;

		}
		/* Auch wenn der Filter prinzipiell sicherstellen sollte,
		dass nur ein eingeloggter User auf suche.jsp kommt,
		schadet mehr Überprüfung ja nicht */
		if (user != null) {

			String b = request.getParameter("betreff");
			String e = request.getParameter("empfaenger");
			String zs = request.getParameter("start");
			String ze = request.getParameter("ende");
			String nb = request.getParameter("niedrigerBetrag");
			String hb = request.getParameter("hoherBetrag");

			HashMap<String, String> suche = new HashMap<String, String>();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// werden als Failsafe initialisiert, falls Eingaben unbrauchbar
			long startZ = 0;
			long endeZ = System.currentTimeMillis();
			Timestamp jetzt = new Timestamp(System.currentTimeMillis());

			// Schauen ob in der Suchanfrage die Daten gesetzt wurden
			try {
				Date start = format.parse(zs);
				startZ = start.getTime();
				// Wurde ein Datum in der Zukunft ausgewählt, können wir damit nichts anfangen

				// Ein Fehler heißt nur, dass die Zeit nicht gesetzt wurde, kann man ignorieren
			} catch (ParseException ey) {
			}

			try {
				Date ende = format.parse(ze);
				endeZ = ende.getTime();
			} catch (ParseException ey) {
			}

			// Zeiteingaben zwischenspeichern und sortieren
			long[] zeitEingaben = new long[2];
			zeitEingaben[0] = startZ;
			zeitEingaben[1] = endeZ;

			// schiebt die "größte" Zeit ans Ende vom Arry

			if (zeitEingaben[0] > zeitEingaben[1]) {
				long tmp = zeitEingaben[0];
				zeitEingaben[0] = zeitEingaben[1];
				zeitEingaben[1] = tmp;

				// falls beide uralt (<=0) oder leer waren,
				// muss der erste 0 sein und der zweite Arrayeintrag
				// auf heute gesetzt werden, sonst sucht man im Zeitraum 0
				if (zeitEingaben[1] == 0) {
					zeitEingaben[1] = jetzt.getTime();
				}
			}

			Timestamp anfang = new Timestamp(zeitEingaben[0]);
			Timestamp ende = new Timestamp(zeitEingaben[1]);

			// liegt der größte Eintrag in der Zukunft gehen wir von Fehler aus
			// liefern aber trotzdem alle Überweisungen vong UXigkeit her

			if (zeitEingaben[1] > jetzt.getTime()) {
				ende = jetzt;
				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg",
						"Leider können wir nicht ermitteln welche Überweisungen die Zukunft für sie bereit hält, aber alle bisher bekannten.");
			}

			if (zeitEingaben[0] > jetzt.getTime()) {
				anfang = new Timestamp(0);
				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg",
						"Überweisungen für eine Zeitspanne in der Zukunft haben wir nicht, es gibt stattdessen alle bisherigen Überweisungen");
			}

			// werden keine Beträge mitgegeben, wird angenommen, alle Beträge sind gewünscht
			// - also alle mgl. Werte von int
			int btrgNdrg = Integer.MIN_VALUE;
			int btrgHoch = Integer.MAX_VALUE;

			// Damit später die Regex Überprüfung keine sinnvollen Beträge wegwirft, zu
			// erwartende Sonderzeichen löschen
			// Da unsere Datenbank ja praktisch mit Cent - Integer funktioniert müssen wir
			// alle Trennzeichen wegwerfen
			hb = hb.replace(",", "");
			hb = hb.replace(".", "");
			hb = hb.replace("€", "");
			//
			try {
				btrgHoch = Integer.parseInt(hb);
			} catch (Exception eI) {
			}

			nb = nb.replace(",", "");
			nb = nb.replace(".", "");
			nb = nb.replace("€", "");

			try {
				btrgNdrg = Integer.parseInt(nb);
			} catch (Exception oi) {

			}
			// sodass die IBAN mit Leerzeichen eingegeben werden kann und trotzdem gefunden
			// wird
			e = e.replace(" ", "");

			// Beträge in die richtige Reihenfolge bringen
			if (btrgHoch < btrgNdrg) {
				int tempBetr = btrgHoch;
				btrgHoch = btrgNdrg;
				btrgNdrg = tempBetr;
			}

			// die Sucheingaben werden in die Suche gepackt
			suche.put("ibanAnderePartei", e);
			suche.put("betreff", b);
			suche.put("hoherBetrag", String.valueOf(btrgHoch));
			suche.put("niedrigerBetrag", String.valueOf(btrgNdrg));

			// die Suchbegriffe werden gesäubert um sie zur SQL Abfrage weitergeben zu

			suche.forEach((k, v) -> {
				if (!sauber(v)) {
					// wir whitelisten die Sucheingaben, alles sonst wird ignoriert
					suche.replace(k, "");

					if (!v.equals("")) {
						request.setAttribute("msgArt", "error");
						request.setAttribute("msg",
								"Es kann nur nach Ziffern und Buchstaben sowie . , - gesucht werden. "
										+ "Bitte passen sie die Suchanfrage dementsprechend an. In Feld: " + k);

					}
				}
			});

			// Timestamps sollten unbedenklich sein, keine Regex überprüfung nötig
			suche.put("zeitBeginn", String.valueOf(anfang.getTime()));
			suche.put("zeitEnde", String.valueOf(ende.getTime()));

			if (TransaktionsDatabase.transaktionsSuche(user, suche)) {

				request.getRequestDispatcher("/suchergebnisse.jsp").forward(request, response);
			} else {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Etwas ist schiefgelaufen. Bitte probieren sie es erneut.");
				request.getRequestDispatcher("/suche.jsp").forward(request, response);

			}
		}

	}


	/**
	 * Lädt Überweisungen aus der DB ins Nutzerobjekt um auf der Seite angezeigt zu
	 * werden
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void manageUeberweisungen(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");

		// Änderungen betreffen das aktuell gewählte Konto
		int kId = user.getCurKonto();

		// Entscheidet wie viele Überweisungen nachgeladen werden sollen
		String wieViele = (String) request.getParameter("auszugsMenge");

		if (wieViele.equals("alle")) {

			ArrayList<Ueberweisung> alleUeberweisungen = TransaktionsDatabase.getAlleTransaktionen(kId);
			if (alleUeberweisungen == null) {
				request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
			}

			user.setCurKontoueberweisungen(alleUeberweisungen);
		}

		else if (wieViele.equals("15")) {
			ArrayList<Ueberweisung> aktuelleUeberweisungen = user.getCurKontoUeberweisungen();

			Timestamp aeltester = user.getAeltesteAktuelleUeberweisung();
			ArrayList<Ueberweisung> neueUeberweisungen = TransaktionsDatabase.getAeltereTransaktionen(15, kId,
					aeltester);
			if (neueUeberweisungen == null) {
				request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
			}
			for (Ueberweisung tmp : neueUeberweisungen) {
				aktuelleUeberweisungen.add(tmp);
			}
			user.setCurKontoueberweisungen(aktuelleUeberweisungen);

		}
		// Setzt die Überweisungszahl der restlichen Konten auf 15

		session.setAttribute("user", user);
		request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);

	}



	/**
	 * Wir lassen in unserem Input nur Ziffern und Buchstaben zu um Injections o.ä.
	 * zu vermeiden
	 * Auch wenn es bereits einen Filter gibt und ein Prepared-Statement verwendet
	 * wird, lieber
	 * ein bisschen zu vorsichtig als Nachsicht zu haben - Versuch des Whitelistens:
	 * 
	 * @param in
	 * @return
	 */
	static boolean sauber(String in) {
		boolean stringGut = false;
		if (in == null) {
			return stringGut;
		}
		// Umlaute und Komma, Punkt und Bindestrich + normale Zeichen und Ziffern und
		// Leerzeichen!
		String inputRegex = "([\\.\\,\\-]|[äÄüÜöÖa-zA-Z0-9])+";
		Pattern sauber = Pattern.compile(inputRegex);
		if (sauber.matcher(in).matches()) {
			stringGut = true;
		}
		return stringGut;
	}

	/**
	 * Legt eine Überweisung in der DB ab
	 * 
	 * @param user
	 * @param iban
	 * @param name
	 * @param betrag
	 * @param betreff
	 * @param datum
	 * @param zeit
	 * @return ob speichern erfolgreich
	 */
	public static boolean speicherEineUeberweisungFuer(Benutzer user, String iban, String name, String betrag,
			String betreff, String datum, String zeit) {
		boolean erfolg = false;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
		long datm = 0;
		Timestamp jetzt = new Timestamp(System.currentTimeMillis());
		try {
			Date ende = format.parse(datum + "-" + zeit);
			datm = ende.getTime();
		} catch (ParseException ey) {
		}

		Timestamp gegeben = new Timestamp(datm);

		if (jetzt.getTime() < datm) {
			gegeben = jetzt;
		}

		betrag = betrag.replace(",", "");
		betrag = betrag.replace(".", "");
		betrag = betrag.replace("€", "");
		int btrg = 0;
		try {
			btrg = Integer.valueOf(betrag);
		} catch (NumberFormatException ok) {
			System.err.println("Unbrauchbarer Input Betrag: " + betrag);
			return erfolg;
		}
		erfolg = TransaktionsDatabase.erstelleEineTransaktion(user, name, iban, betreff, btrg, gegeben);

		return erfolg;
	}

	/**
	 * Zufaellige Transaktionsdaten erstellen zum Testen.
	 * 
	 * @param transaktionsZahl
	 * @return die erstellten Transaktionsdaten
	 */
	public static String[][] machZufallsTransaktionsDaten(int transaktionsZahl) {

		String[][] rtrn = new String[transaktionsZahl][3];

		String[] versender = { "Edeka", "Aral", "PayPal", "McFit", "Rewe", "Netto", "Amazon", "Telekom", "O2",
				"Aldi", "Lidl", "Modellbaushop24" };
		String[] ibans = { "DE33200907001015982001", "DE33200907001015395729", "DE33200907009374518374",
				"DE33200907039728502847", "DE33200907001015371038", "DE33200907001015026403", "DE33200907001015741706",
				"DE33200907001015894621", "DE33200907001017809439", "DE33200907001019752696", "DE33200907001010985374",
		"DE33200907001015987243" };

		Random r = new Random();

		for (int i = 0; i < transaktionsZahl; i++) {

			int indx = r.nextInt(versender.length);

			// Beträge in einer normalverteilten Spanne von von ca.
			// kleinen positiven bis dreistellig negativen Beträgen
			String derBetrag = String.valueOf(-1 * r.nextInt(43270) + r.nextInt(5000));

			rtrn[i][0] = versender[indx];
			rtrn[i][1] = ibans[indx];
			rtrn[i][2] = derBetrag;

			// Simuliert mit komplett realistischer Wahrscheinlichkeit einen Lottogewinn
			if (r.nextInt(400) == 83) {

				rtrn[i][0] = "Loto Totto";
				rtrn[i][1] = "DE33200907001079864039";
				rtrn[i][2] = "130000000";

			}
		}
		return rtrn;
	}
}
