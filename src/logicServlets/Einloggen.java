package logicServlets;

import manager.BenutzerManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login Servlet, das fuer alle Logins eine spezifische Nutzerinstanz erstellt,
 * deren Credentials dann von einem Fehlermanager ueberprueft werden.
 * Verschiedene Nutzer werden unabhaengig voneinander generiert und ueberprueft,
 * um Modifizierung zu vereinfachen.
 * 
 * @author Tobias Liebl
 */
@WebServlet("/Einloggen")
public class Einloggen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Dieses Servlet soll keine Get-Anfragen behandeln - Weiterleitung auf Einloggen jsp
		request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * LoginManager schreibt Userobjekt in die Session
		 */
		BenutzerManager.checkLogin(request, response);

	}
}
