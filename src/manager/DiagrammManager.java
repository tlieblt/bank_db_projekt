package manager;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.TransaktionsDatabase;

/**
 * Manager der bei Erstellen der Diagramme hilft
 * 
 * @author Sven de Haan
 *
 */
public class DiagrammManager {

	/**
	 * Methode die benoetigte Werte weiterleitet
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void diagrammWerte(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Initialiere benötige Werte
		HttpSession session = request.getSession();
		HashMap<String, String[]> kategorien;
		ArrayList<Ueberweisung> ueberweisungen;

		Benutzer user = (Benutzer) session.getAttribute("user");
		// User Daten in Variablen speichern
		int kId = user.getCurKonto();
		String iban = user.getCurIban();
		kategorien = user.getKategorien();
		ueberweisungen = TransaktionsDatabase.getAlleTransaktionen(kId);

		// Zeit Werte
		long zeit[] = zeit(request);
		Timestamp anfang = new Timestamp(zeit[0]);
		Timestamp ende = new Timestamp(zeit[1]);

		String istKreis = request.getParameter("kreis");
		String istBalken = request.getParameter("balken");

		String auswahlNutzer[] = new String[5];
		auswahlNutzer[0] = request.getParameter("auswahl1");
		auswahlNutzer[1] = request.getParameter("auswahl2");
		auswahlNutzer[2] = request.getParameter("auswahl3");
		auswahlNutzer[3] = request.getParameter("auswahl4");
		auswahlNutzer[4] = request.getParameter("auswahl5");

		ueberpruefungEingaben(auswahlNutzer, request, response, istKreis, istBalken);

		response.setContentType("image/jpeg");

		double summeAuswahl1 = 0;
		double summeAuswahl2 = 0;
		double summeAuswahl3 = 0;
		double summeAuswahl4 = 0;
		double summeAuswahl5 = 0;
		double summeNichtKate = 0;

		double betragAuswahl1 = 0;
		double betragAuswahl2 = 0;
		double betragAuswahl3 = 0;
		double betragAuswahl4 = 0;
		double betragAuswahl5 = 0;

		String nameAuswahl1 = "";
		String nameAuswahl2 = "";
		String nameAuswahl3 = "";
		String nameAuswahl4 = "";
		String nameAuswahl5 = "";
		// get Kategorie fuer jede Ueberweisung
		for (int i = 0; i < ueberweisungen.size(); i++) {
			Ueberweisung aktuelleUeberweisung = ueberweisungen.get(i);
			ArrayList<String> treffer = new ArrayList<String>();
			for (var entry : kategorien.entrySet()) {

				for (String katEl : entry.getValue()) {
					if (aktuelleUeberweisung.getBetreff().contains(katEl)
							|| aktuelleUeberweisung.getVersender().contains(katEl)) {
						treffer.add(entry.getKey());
						break;
					}
				}
			}
			String kategorieTreffer = "";
			for (String trf : treffer) {
				kategorieTreffer += "Kategorie: " + trf;

				// Ueberpruefe jede Auswahl des Nutzers. Weise auszugebene Werte (Name, Summe,
				// Betrag) Veriable zu
				if (kategorieTreffer.equals(auswahlNutzer[0]) && aktuelleUeberweisung.getTimestamp().after(anfang)
						&& aktuelleUeberweisung.getTimestamp().before(ende)) {
					summeAuswahl1 += 1;
					nameAuswahl1 = kategorieTreffer;
					betragAuswahl1 += aktuelleUeberweisung.getBetrag() / 100;
				}
				if (kategorieTreffer.equals(auswahlNutzer[1]) && aktuelleUeberweisung.getTimestamp().after(anfang)
						&& aktuelleUeberweisung.getTimestamp().before(ende)) {
					summeAuswahl2 += 1;
					nameAuswahl2 = kategorieTreffer;
					betragAuswahl2 += aktuelleUeberweisung.getBetrag() / 100;
				}
				if (kategorieTreffer.equals(auswahlNutzer[2]) && aktuelleUeberweisung.getTimestamp().after(anfang)
						&& aktuelleUeberweisung.getTimestamp().before(ende)) {
					summeAuswahl3 += 1;
					nameAuswahl3 = kategorieTreffer;
					betragAuswahl3 += aktuelleUeberweisung.getBetrag() / 100;
				}
				if (kategorieTreffer.equals(auswahlNutzer[3]) && aktuelleUeberweisung.getTimestamp().after(anfang)
						&& aktuelleUeberweisung.getTimestamp().before(ende)) {
					summeAuswahl4 += 1;
					nameAuswahl4 = kategorieTreffer;
					betragAuswahl4 += aktuelleUeberweisung.getBetrag() / 100;
				}
				if (kategorieTreffer.equals(auswahlNutzer[4]) && aktuelleUeberweisung.getTimestamp().after(anfang)
						&& aktuelleUeberweisung.getTimestamp().before(ende)) {
					summeAuswahl5 += 1;
					nameAuswahl5 = kategorieTreffer;
					betragAuswahl5 += aktuelleUeberweisung.getBetrag() / 100;
				}

			}
			if (treffer.isEmpty()) {
				summeNichtKate += 1;
			}
		}

		request.setAttribute("auswahl1", nameAuswahl1);
		request.setAttribute("auswahl2", nameAuswahl2);
		request.setAttribute("auswahl3", nameAuswahl3);
		request.setAttribute("auswahl4", nameAuswahl4);
		request.setAttribute("auswahl5", nameAuswahl5);
		request.setAttribute("summeauswahl1", summeAuswahl1);
		request.setAttribute("summeauswahl2", summeAuswahl2);
		request.setAttribute("summeauswahl3", summeAuswahl3);
		request.setAttribute("summeauswahl4", summeAuswahl4);
		request.setAttribute("summeauswahl5", summeAuswahl5);
		request.setAttribute("summenichtkategorisiert", summeNichtKate);
		request.setAttribute("betragauswahl1", betragAuswahl1);
		request.setAttribute("betragauswahl2", betragAuswahl2);
		request.setAttribute("betragauswahl3", betragAuswahl3);
		request.setAttribute("betragauswahl4", betragAuswahl4);
		request.setAttribute("betragauswahl5", betragAuswahl5);
		request.setAttribute("anfangszeit", anfang);
		request.setAttribute("endzeit", ende);
		request.setAttribute("kontonummer", iban);

		if (istKreis.equals("kreis")) {
			request.getRequestDispatcher("/KategorieDropDownKreisDiagramm").forward(request, response);
		}
		if (istBalken.equals("balken")) {
			request.getRequestDispatcher("/KategorieDropDownBalkenDiagramm").forward(request, response);
		}

	}

	/**
	 * Methode die Zeiteingaben des Nutzers prueft (analog wie bei Suche)
	 * 
	 * @param request
	 * @return zeitEingaben die gemacht wurden
	 */
	public long[] zeit(HttpServletRequest request) {
		String zs = request.getParameter("start");
		String ze = request.getParameter("ende");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		long startZ = 0;
		long endeZ = System.currentTimeMillis();
		Timestamp jetzt = new Timestamp(System.currentTimeMillis());
		try {
			Date start = format.parse(zs);
			startZ = start.getTime();
		} catch (ParseException ey) {
		}

		try {
			Date ende = format.parse(ze);
			endeZ = ende.getTime();
		} catch (ParseException ey) {
		}

		long[] zeitEingaben = new long[2];
		zeitEingaben[0] = startZ;
		zeitEingaben[1] = endeZ;

		if (zeitEingaben[0] > zeitEingaben[1]) {
			long tmp = zeitEingaben[0];
			zeitEingaben[0] = zeitEingaben[1];
			zeitEingaben[1] = tmp;
			if (zeitEingaben[1] == 0) {
				zeitEingaben[1] = jetzt.getTime();
			}
		}
		return zeitEingaben;
	}

	/**
	 * Methode die Kategorien ohne Dopplungen kriegen für das Auswahlmenue in den
	 * diagramm jsp's
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public ArrayList<String> getKategorienOhneDopplungen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		HashMap<String, String[]> kategorien;
		ArrayList<Ueberweisung> ueberweisungen;

		response.setContentType("image/jpeg");

		Benutzer user = (Benutzer) session.getAttribute("user");
		kategorien = user.getKategorien();

			if (kategorien.size() < 1) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Sie benötigen Kategorien um Diagramme darzustellen");
				request.getRequestDispatcher("/kategorien.jsp").forward(request, response);

			}
		

		// Änderungen betreffen das aktuell gewählte Konto
		int kId = user.getCurKonto();
		ueberweisungen = TransaktionsDatabase.getAlleTransaktionen(kId);
		if (ueberweisungen.size() < 1) {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Sie benötigen Überweisungen um Diagramme darzustellen. Damit das Balkendiagramm was sinnvolles anzeigt, brauchen Sie mind. eine Überweisung, die einer Kategorie zugeordnet ist.");
			request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);

		}
		ArrayList<String> kategorienOhneDopplungen = new ArrayList<String>();
		// leeren Wert einfuegen damit Drop Down Menue nicht direkt befuellt ist.
		kategorienOhneDopplungen.add("");
		// gehe Kategorie je Ueberweisung durch
		for (int i = 0; i < ueberweisungen.size(); i++) {
			Ueberweisung aktuelleUeberweisung = ueberweisungen.get(i);

			ArrayList<String> treffer = new ArrayList<String>();
			for (var entry : kategorien.entrySet()) {

				for (String katEl : entry.getValue()) {
					if (aktuelleUeberweisung.getBetreff().contains(katEl)
							|| aktuelleUeberweisung.getVersender().contains(katEl)) {
						treffer.add(entry.getKey());
						break;
					}
				}
			}
			treffer.forEach((e) -> {
				if (kategorienOhneDopplungen.contains("Kategorie: " + e)) {
					// ueberspringe Wert weil schon enthalten
				} else {
					kategorienOhneDopplungen.add("Kategorie: " + e);

				}

			});
		}
		return kategorienOhneDopplungen;
	}

	/**
	 * Ueberpruefung ob Nutzer keine Kategorie ausgewaehlt hat oder Kategorien
	 * doppelt ausgewaehlt hat
	 * 
	 * @param auswahlNutzer
	 * @param request
	 * @param response
	 * @param istKreis
	 * @param istBalken
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ueberpruefungEingaben(String[] auswahlNutzer, HttpServletRequest request, HttpServletResponse response,
			String istKreis, String istBalken) throws ServletException, IOException {
		if (auswahlNutzer[0] == "" && auswahlNutzer[1] == "" && auswahlNutzer[2] == "" && auswahlNutzer[3] == "") {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Fehler. Ein Diagramm kann ohne Auswahl nicht erstellt werden");
			pruefeWelchesDiagramm(istKreis, istBalken, request, response);

		}
		if (auswahlNutzer[0] != "") {
			if (auswahlNutzer[0].equals(auswahlNutzer[1]) || auswahlNutzer[0].equals(auswahlNutzer[2])
					|| auswahlNutzer[0].equals(auswahlNutzer[3]) || auswahlNutzer[0].equals(auswahlNutzer[4])) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Fehler. Bitte Kategorien nicht mehrfach auswählen");
				pruefeWelchesDiagramm(istKreis, istBalken, request, response);
			}
		}
		if (auswahlNutzer[1] != "") {
			if (auswahlNutzer[1].equals(auswahlNutzer[2]) || auswahlNutzer[1].equals(auswahlNutzer[3])
					|| auswahlNutzer[1].equals(auswahlNutzer[4])) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Fehler. Bitte Kategorien nicht mehrfach auswählen");
				pruefeWelchesDiagramm(istKreis, istBalken, request, response);
			}
		}
		if (auswahlNutzer[2] != "") {
			if (auswahlNutzer[2].equals(auswahlNutzer[3]) || auswahlNutzer[2].equals(auswahlNutzer[4])) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Fehler. Bitte Kategorien nicht mehrfach auswählen");
				pruefeWelchesDiagramm(istKreis, istBalken, request, response);
			}
		}
		if (auswahlNutzer[3] != "") {
			if (auswahlNutzer[3].equals(auswahlNutzer[4])) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Fehler. Bitte Kategorien nicht mehrfach auswählen");
				pruefeWelchesDiagramm(istKreis, istBalken, request, response);
			}
		}

	}

	/**
	 * Methode die ueberprueft welches Diagramm erstellt wird und an das jeweilige
	 * DropDown Servlet weiterleitet
	 * 
	 * @param istKreis
	 * @param istBalken
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void pruefeWelchesDiagramm(String istKreis, String istBalken, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (istKreis.equals("kreis")) {
			request.getRequestDispatcher("/KategorieDropDownKreisDiagramm").forward(request, response);
		}
		if (istBalken.equals("balken")) {
			request.getRequestDispatcher("/KategorieDropDownBalkenDiagramm").forward(request, response);
		}
	}
}
