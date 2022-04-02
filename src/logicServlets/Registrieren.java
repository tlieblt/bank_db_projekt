package logicServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.BenutzerManager;

/**
 * Registriert einen neuen Nutzer und legt diesen in der Datenbank ab Gibt ueber
 * Messages Informationen ueber (Miss-)Erfolg weiter
 * 
 * @author Tobias Liebl
 */

@WebServlet("/Registrieren")
public class Registrieren extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BenutzerManager.register(request, response);

	}
}