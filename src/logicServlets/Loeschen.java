package logicServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import manager.FehlerManager;

/**
 * Relativ einfaches Servlet, das nur ueberprueft ob ein Nutzer eingeloggt ist
 * und diesen, falls gewuenscht, aus der Datenbank loescht.
 * 
 * @author Tobias Liebl
 */

@WebServlet("/Loeschen")
public class Loeschen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer gibtsMich = (Benutzer) session.getAttribute("user");
		if (gibtsMich != null) {
			doPost(request, response);
		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bevor sie ihren Account loeschen koennen, bitte einloggen!");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Benutzer gibtsMich = (Benutzer) session.getAttribute("user");
		if (gibtsMich != null) {
			boolean geloescht = FehlerManager.userLoeschen(gibtsMich);
			if (geloescht) {
				request.getSession().invalidate();
				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg", "User erfolgreich geloescht.");
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			} else {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "User konnte nicht geloescht werden.");
				request.getRequestDispatcher("/nutzer.jsp").forward(request, response);
			}

		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bevor sie ihr Profil loeschen koennen, bitte einloggen!");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
	}

}
