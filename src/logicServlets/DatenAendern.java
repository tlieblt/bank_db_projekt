package logicServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import manager.BenutzerManager;

/**
 * Servlet das geaenderte Profildaten an die Datenbank weitergibt bzw. dadurch
 * entstehende Fehlermeldungen direkt ausgibt
 * 
 * @author Tobias Liebl
 */

@WebServlet("/DatenAendern")
public class DatenAendern extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Um Daten zu Ändern müssen natürlich welche per POST geliefert werden -> Get
	// nur Umleitung auf Seite
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer gibtsMich = (Benutzer) session.getAttribute("user");
		if (gibtsMich != null) {
			request.getRequestDispatcher("/nutzer.jsp").forward(request, response);
		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bevor Daten geaendert werden koennen bitte einloggen!");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Benutzer gibtsMich = (Benutzer) session.getAttribute("user");
		// Ist kein Nutzer eingeloggt, sollen keine Daten geaendert werden duerfen
		if (gibtsMich != null) {

			// sonst macht das der BenutzerManager
			BenutzerManager.updateUser(request, response);

		}

		else {
			request.getRequestDispatcher("einloggen.jsp").forward(request, response);
		}
	}
}
