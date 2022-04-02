package logicServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;

/**
 * Loescht die Session und verabschiedet Nutzer
 * 
 * @author Tobias Liebl
 */

@WebServlet("/Ausloggen")
public class Ausloggen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		String name = user.getVorname() + " " + user.getNachname();
		if (session != null) {
			session.invalidate();
		}
		request.setAttribute("msgArt", "erfolg");
		request.setAttribute("msg", "Erfolgreich ausgeloggt. Auf morgen, bis Wiedersehen " + name + "!");
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}
}
