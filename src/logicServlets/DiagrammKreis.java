package logicServlets;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.DiagrammManager;

/**
 * Servlet fuer Kreis Diagramm
 * 
 * @author Sven de Haan
 *
 */
@WebServlet("/DiagrammKreis")
public class DiagrammKreis extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Ziehe Werte die fuer Visualisierung des Diagramms notwendig sind
		DiagrammManager diagramm = new DiagrammManager();
		diagramm.diagrammWerte(request, response);
	}
}
