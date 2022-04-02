package logicServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import benutzer.Benutzer;
import manager.KontoManager;

/**
 * Für einen eingeloggten Benutzer wird ein Konto erstellt 
 * @author Tobias Liebl
 */

@WebServlet("/KontoErstellen")
public class KontoErstellen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Get requests sollen nur auf die Nutzeränderung weiterleiten
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
		Benutzer user = (Benutzer) session.getAttribute("user");
		// Ist kein Nutzer eingeloggt, sollen keine Daten geaendert werden duerfen
		if (user != null) {

			if (KontoManager.machNeuesKontoFuer(user)) {
				user = KontoManager.updateBenutzerKontenInfo(user);
				session.setAttribute("user", user);

				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg", "Ihr neues Konto wurde erstellt! Die IBAN lautet: " + user.getCurIban());
				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			} else {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Die Kontoerstellung war leider nicht möglich!");
				request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
			}

		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bitte einloggen!");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
	}

}
