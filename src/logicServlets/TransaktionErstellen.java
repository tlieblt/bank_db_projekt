package logicServlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.KategorieDB;
import database.TransaktionsDatabase;
import manager.UeberweisungsManager;

/**
 * Hier werden bei GET 30 Zufalls-transaktionen erstellt 
 * und dem aktuell ausgewählten Konto (CurKonto)
 * hinzugefügt und die Daten einer Überweisung 
 * werden gePOSTet und gespeichert
 * 
 * @author Tobias Liebl
 */
@WebServlet("/TransaktionErstellen")
public class TransaktionErstellen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Get für Erstellung von zufaelligen Ueberweisungen
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		if (user == null) {
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}
		boolean auszuegeErstellt = TransaktionsDatabase.erstelleNeueZufallsTransaktionen(user, 30);

		if (auszuegeErstellt) {
			ArrayList<Ueberweisung> neueUeberweisungen = TransaktionsDatabase.getAlleTransaktionen(user.getCurKonto());
			user.setCurKontoueberweisungen(neueUeberweisungen);
			session.setAttribute("user", user);

			request.setAttribute("msgArt", "erfolg");
			request.setAttribute("msg", "Ihrem Konto wurden 30 zufällige Überweisungen hinzugefügt!");
			request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);

		}
		//
		else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Kontoauszugserstellung nicht möglich!");
			request.getRequestDispatcher("/nutzer.jsp").forward(request, response);
		}
	}

	// Post zum Anlegen einer einzelnen Überweisung
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");
		String wasTun = (String) request.getParameter("eineUeberweisung");
		if (wasTun.equals("eineUeberweisung")) {

			String iban = request.getParameter("iban");
			String name = request.getParameter("name");
			String betreff = request.getParameter("betreff");
			String betrag = request.getParameter("betrag");
			String datum = request.getParameter("datum");
			String zeit = request.getParameter("zeit");


			if(UeberweisungsManager.speicherEineUeberweisungFuer(user, iban, name, betrag, betreff, datum, zeit)) {

				ArrayList<Ueberweisung> neueUeberweisungen = TransaktionsDatabase.getAlleTransaktionen(user.getCurKonto());
				user.setCurKontoueberweisungen(neueUeberweisungen);				
				

				String kategorie = request.getParameter("kategorieName");
				int kategorieInput = 2;
				if(betreff == null || betreff.equals("")) {
					kategorieInput--;
				}	
				if(name == null || name.equals("")) {

					kategorieInput--;
				}

				HashMap<String,String[]> echteKats = user.getKategorien();
				if(echteKats == null || ! echteKats.isEmpty()) {}
				else {
				if(kategorieInput > 0) {
					if(echteKats.containsKey(kategorie)) {
						String[] alteKatEls = echteKats.get(kategorie);
						
						String[] neueKatEls = new String[alteKatEls.length+2];
						int j = 0;
						for(String stri : alteKatEls) {

							neueKatEls[j] = stri;
							j++;
						}
						if(betreff != null && !betreff.equals("")) {

							neueKatEls[j] = betreff;
							j++;
						}	
						if(name != null && !name.equals("")) {
							neueKatEls[j] = name;
							//j++;				}
						}
						echteKats.replace(kategorie, neueKatEls);
						user.setKategorien(echteKats);
						
						KategorieDB.updateKategorie(kategorie, user);
					}}}
				
				session.setAttribute("user", user);


				request.setAttribute("msgArt", "erfolg");
				request.setAttribute("msg", "Die Überweisung wurde ihrem Konto hinzugefügt!");				
				request.getRequestDispatcher("kontouebersicht.jsp").forward(request, response);
			}	
		}
		//
		else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Überweisungserstellung nicht möglich!");
			request.getRequestDispatcher("/nutzer.jsp").forward(request, response);
		}
	}

}

