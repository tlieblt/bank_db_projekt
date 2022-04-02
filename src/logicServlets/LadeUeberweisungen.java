package logicServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import manager.UeberweisungsManager;

/**
 * Lädt Überweisungen ins Benutzerobjekt nach
 * alle oder 15 (könnte man auch umschreiben, 
 * sodass eine beliebige Anzahl geladen wird)
 * 
 * Beachten: Im Nutzerobjekt liegen die Überweisungen
 * immer nach Datum absteigend sortiert
 * 
 * @author Tobias Liebl
 */
@WebServlet("/LadeUeberweisungen")
public class LadeUeberweisungen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Es wird gePOSTet wie viele Überweisungen gewünscht sind, deswegen GET
		// statisch
		request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");

		if (user == null) {
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
		// Ausgelagert um Servlet klein zu halten
		UeberweisungsManager.manageUeberweisungen(request, response);

	}
}
