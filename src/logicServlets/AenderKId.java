package logicServlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import database.TransaktionsDatabase;
import manager.KontoManager;

/**
 * Servlet das einem Nutzer erlaubt sein aktuelles Konto 
 * zu ändern
 * 
 * Auf Wunsch wird hier auch das aktuelle Konto gelöscht
 * 
 * @author Tobias Liebl
 */
@WebServlet("/AenderKId")
public class AenderKId extends HttpServlet {
	private static final long serialVersionUID = 1L;



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String wunschKonto = request.getParameter("konto");
		String aenderungsArt = request.getParameter("aenderungsArt");

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		if (user == null) {
			request.getRequestDispatcher("einloggen.jsp").forward(request, response);
		}

		ArrayList<String> kIbs = user.getKontoIbans();

		// Ordnet der ausgewählten IBAN die "interne" KontoID der DB zu
		if (kIbs.contains(wunschKonto)) {
			int welchesKonto = kIbs.indexOf(wunschKonto);
			user.setCurKonto(user.getKonten().get(welchesKonto));
			user.setCurIban(wunschKonto);

		}

		if (aenderungsArt.equals("kontoLoeschen") && kIbs.contains(wunschKonto)) {

			if (KontoManager.loescheKonto(user, user.getCurKonto())) {

				Benutzer usa = KontoManager.updateBenutzerKontenInfo(user);
				if (usa == null) {
					user.setCurIban(null);
					user.setCurKonto(0);
					user.setKonten(null);
					user.setKontoIbans(null);

				} else {
					user = usa;
				}

				session.setAttribute("user", user);
				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg", "Ihr Konto " + wunschKonto
						+ " und die damit verknüpften Überweisungen wurden erfolgreich gelöscht.");
				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			}

			else {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Leider hat etwas nicht richtig funktioniert.");
				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			}
		}

		else {
			user.setCurKontoueberweisungen(TransaktionsDatabase.getTransaktionen(15, user.getCurKonto()));

			session.setAttribute("user", user);

			String ursprungsSeite = String.valueOf(request.getParameter("ursprungsseite"));

			/*
			 * wenn noch mehr Seiten hinzukommen für die der KontoAuswahlTag gebraucht wird,
			 * müssen diese ihren Ressourcennamen mitgeben - sonst Weiterleitung auf
			 * Übersicht
			 */
			if (ursprungsSeite != null && !ursprungsSeite.equals("")) {
				request.getRequestDispatcher(ursprungsSeite).forward(request, response);
			} else {
				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			}
		}
	}
}
