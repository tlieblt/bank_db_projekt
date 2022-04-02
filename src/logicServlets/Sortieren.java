package logicServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.UeberweisungsManager;

/**
 * 
 * Ueberweisungsmanager sortiert die aktuellen Such- oder Kontoeinträge 
 * des Nutzers aus der Session
 * und liefert diese auf die Seite "statisch" als Attribut ArrayList zurück
 * die auf sortieren.jsp angezeigt wird
 *  
 * @author Tobias Liebl 

 */

@WebServlet("/Sortieren")
public class Sortieren extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			UeberweisungsManager.sortiereMitDb(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UeberweisungsManager.sortiereMitDb(request, response);
		
	}

}
