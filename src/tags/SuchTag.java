package tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import benutzer.Benutzer;
import benutzer.Ueberweisung;
import manager.FehlerManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Zeigt die Suchergebnisse in Tabelleform an und prüft 
 * eventuelle Kategorieübereinstimmungen
 * - alternativ könnte man zwar auch die aktuellen Überweisungen
 * des Nutzers überschreiben, aber dann würden die Suchergebnisse
 * an anderer Stelle z.B. nach dem ältesten Ergebnis durchsucht
 * um ältere Überweisungen zu laden und es werden keine mehr nachgeladen o.ä.
 * Zudem spart man sich einen DB-Zugriff / Serverarbeit 
 * 
 * Ansonsten praktisch analog zu KontoUbersichtTag
 * 
 * @author Tobias Liebl
 */

public class SuchTag extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;
	HashMap<String, String[]> kategorien;

	ArrayList<Ueberweisung> ueberweisungen;

	/**
	 * holt die Kategorien aus der Session um abgleichen zu können
	 * 
	 * @param session
	 * @return die Kategorien
	 */
	public HashMap<String, String[]> getKategorien(HttpSession session) {
		Benutzer usi = (Benutzer) session.getAttribute("user");

		return usi.getKategorien();

	}

	/**
	 *  holt die Suchergebnisse aus der Session um sie anzeigen zu können
	 * 
	 * @param session
	 * @return die Ueberweisungen
	 */
	public ArrayList<Ueberweisung> getSuchergebnisseInSession(HttpSession session) {

		ArrayList<Ueberweisung> fallsLeer = new ArrayList<Ueberweisung>();
		fallsLeer.add(new Ueberweisung("Keine Überweisungsdaten verfügbar."));

		Benutzer currUser = (Benutzer) session.getAttribute("user");

		ArrayList<Ueberweisung> suchergebnisse = currUser.getSuchErgebnisse();

		if (suchergebnisse.isEmpty()) {
			return fallsLeer;

		} else {
			return suchergebnisse;

		}
	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		JspWriter out = pageContext.getOut();

		String tabellenKopf = "<div class='m-3'><table class='table table-striped table-bordered m-2'  style='table-layout: fixed;'>"
				+ "<thead><tr><th scope='col'>#</th><th scope='col'>Versender</th>"
				+ "<th scope='col'>IBAN</th><th scope='col'>Beschreibung</th><th scope='col'>Betrag</th>"
				+ "<th scope='col'>Zeit</th><th scope='col'>Kategorie</th></tr></thead><tbody>";

		try {
			ueberweisungen = getSuchergebnisseInSession(session);

			kategorien = getKategorien(session);

			if (ueberweisungen.get(0).getBetreff().equals("Keine Überweisungsdaten verfügbar.")) {

				return EVAL_BODY_INCLUDE;
			}

			// hier wird die Gesamtsumme schon oben ausgegeben, braucht mehr Rechenaufwand
			// wurde deswegen nur einmal gemacht, wegen Klimawandel
			int gesamtSumme = 0;
			for (Ueberweisung ue : ueberweisungen) {
				gesamtSumme += ue.getBetrag();
			}

			int i = 1;
			out.println("<h5 class='m-3'>Die Gesamtsumme über die Beträge der Suchergebnisse ist: "
					+ FehlerManager.machBetragAusInt(gesamtSumme) + 
					"<br/> Sie können ihre Suchergebnisse als Excel Datei unter Funktionen exportieren!</h5>");
			out.println(tabellenKopf);

			for (Ueberweisung tmpUeber : ueberweisungen) {

				// Trennpunkt Euro/Cent einfuegen
				String btrg = FehlerManager.machBetragAusInt(tmpUeber.getBetrag());

				out.println("<tr><th scope='row'>" + i + "</th>");
				out.println("<td>" + tmpUeber.getVersender() + "</td>");
				out.println("<td>" + tmpUeber.getVersender_iban() + "</td>");
				out.println("<td>" + tmpUeber.getBetreff() + "</td>");
				out.println("<td>" + btrg + "</td>");
				out.println("<td>" + tmpUeber.getTimestamp().toString().substring(0, 19) + "</td>");
				// Gibt es noch keine Kategorien, will der Nutzer ja vielleicht welche machen

				if (kategorien == null || kategorien.size() == 0) {
					out.println("<td><a href='kategorien.jsp' class='link-success'>Kategorien anlegen</a></td>");

				} else {
					ArrayList<String> treffer = new ArrayList<String>();

					// Für alle Kategorieelemente prüfen ob die Überweisung dazu passt
					for (var entry : kategorien.entrySet()) {

						for (String katEl : entry.getValue()) {
							if (tmpUeber.getBetreff().contains(katEl) || tmpUeber.getVersender().contains(katEl)) {
								// sobald eine Kategorie passt, merken wir uns die in treffer und stoppen die
								// weitere Suche in dieser Kategorie -> es wird weiter nach den anderen Kat.
								// gesucht
								treffer.add(entry.getKey());
								break;
							}
						}
					}
					String kategorieTreffer = "";
					for (String trf : treffer) {
						kategorieTreffer += "Kategorie: " + trf + "; ";
					}

					// hat bisher kein KategorieElement zu der Überweisung gepasst
					// teilen wir das dem Benutzer mit

					if (treffer.isEmpty()) {

						kategorieTreffer = "<a href='kategorien.jsp'>Nicht kategorisiert.  </a>";
					}

					out.println("<td>" + kategorieTreffer + "</td>");
				}
				i++;

			}

			out.println("</tbody></table></div>");

		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return SKIP_BODY;

	}
}