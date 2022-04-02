package logicServlets;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.DiagrammManager;

/**
 * Servlet um Kategorien ohne Dopplungen, fuer das DropDown Menue in der jsp,
 * als ArrayList zu übermitteln. Ablauf: Nutzer klickt auf Nutzerfunktion
 * Balkendiagramm erstellen -> dieses Servlet gibt Kategorien an jsp -> jsp
 * öffnet sich -> man laesst Balkendiagramm erstellen -> dieses Servlet wird
 * wieder angerufen -> wieder auf jsp mit Ausgabe Diagramm -> Wiederholung der
 * Schritte
 * 
 * @author Sven de Haan
 *
 */
@WebServlet("/KategorieDropDownBalkenDiagramm")
public class KategorieDropDownBalkenDiagramm extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DiagrammManager dropdown = new DiagrammManager();
		ArrayList<String> kategorienOhneDopplungen = new ArrayList<String>();
		kategorienOhneDopplungen = dropdown.getKategorienOhneDopplungen(request, response);
		request.setAttribute("kategorien", kategorienOhneDopplungen);
		request.getRequestDispatcher("/diagrammbalken.jsp").forward(request, response);

	}

}
