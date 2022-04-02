package logicServlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import benutzer.Benutzer;
import database.KategorieDB;
import manager.BenutzerManager;

/**
 * Legt neue Kategorien an und gibt Kategorieänderungen an die DB weiter Bei
 * Änderungen werden die Kategorien im Nutzer aktualisiert und dann das
 * komplette Kategorieelemente-Array in der DB mit dem aktualisierten Inhalt des
 * Kategorieobjektes im Nutzer überschrieben 
 * -> einfacher als einzelne Einträge im DB-Array zu ändern
 *
 * @author Tobias Liebl
 *
 */
@WebServlet("/KategorienAendern")
public class KategorienAendern extends HttpServlet {
	private static final long serialVersionUID = 1L;



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("kategorien.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		Benutzer user = (Benutzer) session.getAttribute("user");
		HashMap<String, String[]> ktgrn = user.getKategorien();

		String katN = String.valueOf(request.getParameter("kategorieName"));
		String aenderungsArt = String.valueOf(request.getParameter("aenderungsArt"));

		/* Leerzeichen im Kategorienamen führen bei HTML-Namen in der Form
		zu Fehlern - statt Leerzeichen zu verbieten, durch den String s.u. ersetzen
		und jetzt wieder zurückersetzen */

		String katNamEsc = katN.replace("89hjafoiuslkfj", " ");

		if (aenderungsArt.equals("kategorieDazu")) {
			if (katN == null || katN.equals("")) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Leere Kategorienamen können nicht angelegt werden.");
				request.getRequestDispatcher("kategorien.jsp").forward(request, response);
			}

			else if (user.getKategorien() != null && user.getKategorien().containsKey(katN)) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Sie haben bereits eine Kategorie " + katN + " angelegt.");
				request.getRequestDispatcher("kategorien.jsp").forward(request, response);
			} else {
				BenutzerManager.neueKategorie(request, response);
			}

		}

		else if (aenderungsArt.equals("kategorieUpdate")) {
			if (ktgrn.containsKey(katNamEsc)) {

				String[] katEls = ktgrn.get(katNamEsc);

				String neuesElement = String.valueOf(request.getParameter("neuesElement"));

				int groesse = katEls.length;

				int geloeschteElemente = 0;
				for (int i = 0; i < groesse; i++) {

					String s = katEls[i];
					String tmp = String.valueOf(request.getParameter(s));
					if (tmp == null || tmp.equals("")) {
						katEls[i] = "";
						geloeschteElemente++;
					} else if (!tmp.equals(s)) {
						katEls[i] = tmp;
					}
				}
				String[] uebrig;
				if (geloeschteElemente == groesse) {
					uebrig = new String[0];
				} else if (geloeschteElemente < groesse) {
					uebrig = new String[groesse - geloeschteElemente];
				} else {
					uebrig = new String[0];
				}

				for (int j = 0; j < uebrig.length; j++) {
					int z = j;

					if (!katEls[z].equals("")) {
						uebrig[j] = katEls[z];
					} else {
						z++;
						String korrKatEl = katEls[z];
						while (korrKatEl.equals("")) {
							z++;
							korrKatEl = katEls[z];
						}
						uebrig[j] = korrKatEl;
					}
				}
				// ist nix zu tun, machen wir auch nix
				if (neuesElement == null || neuesElement.equals("")) {
				}

				else {

					String[] uebrigMitNeu = Arrays.copyOf(uebrig, uebrig.length + 1);
					uebrigMitNeu[uebrig.length] = neuesElement;
					uebrig = uebrigMitNeu;

				}

				ktgrn.replace(katNamEsc, uebrig);
				user.setKategorien(ktgrn);
				if (KategorieDB.updateKategorie(katNamEsc, user)) {
					request.setAttribute("msgArt", "erfolg");
					request.setAttribute("msg", "Ihre Kategorie " + katNamEsc + " wurde erfolgreich geändert.");
					request.getRequestDispatcher("kategorien.jsp").forward(request, response);
				}
			}
		}

		else if (aenderungsArt.equals("kategorieLoeschen")) {
			if (ktgrn.containsKey(katNamEsc)) {

				if (KategorieDB.loescheKategorie(katNamEsc, user)) {
					ktgrn.remove(katNamEsc);
					user.setKategorien(ktgrn);
					request.setAttribute("msgArt", "erfolg");
					request.setAttribute("msg", "Ihre Kategorie " + katNamEsc + " wurde erfolgreich gelöscht.");
					request.getRequestDispatcher("kategorien.jsp").forward(request, response);
				}
			}
		}

		else {
			request.getRequestDispatcher("kategorien.jsp").forward(request, response);
		}
	}

}
