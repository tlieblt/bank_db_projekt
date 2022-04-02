package manager;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.BenutzerDatabase;
import database.KategorieDB;
import database.TransaktionsDatabase;

/**
 * Klasse die Benutzer Funktionen verwaltet
 * 
 * @author Tobias Liebl
 *
 */
public class BenutzerManager {

	/**
	 * Kontrolliert die Eingaben eines Nutzers und legt einen Eintrag in der
	 * Konto-DB für ihn an, wenn alles passt. Fehlermeldungen werden dem Nutzer über
	 * den MessageTag kommuniziert. Es wird zB das Passwort ueberprueft
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Benutzer neuerBenutzer = null;
		String email = request.getParameter("EmailInput");
		String passwort = request.getParameter("Passwort");
		String passwort2 = request.getParameter("PasswortRepeat");
		String vorname = request.getParameter("Vorname");
		String nachname = request.getParameter("Nachname");
		String rolle = request.getParameter("userart");
		int alter = 0;
		try {
			alter = (int) Integer.decode(request.getParameter("Alter"));
		} catch (Exception no) {
		}
		request.setAttribute("msgArt", "error");
		// Passwort Anforderung wird geprueft
		if (FehlerManager.passwortGut(passwort)) {
			// E-Mail echt wird geprueft
			if (FehlerManager.emailEchtheitscheck(email)) {
				// E-Mail schon vorhanden? wird geprueft
				if (!BenutzerDatabase.emailBekannt(email)) {

					if (passwort.equals(passwort2)) {

						String sha3HexedPW = new DigestUtils("SHA3-512").digestAsHex(passwort);
						// Nutzer wird aus eingegebenen Daten erzeugt und an DB weitergegeben
						neuerBenutzer = new Benutzer(email, sha3HexedPW, vorname, nachname, rolle, alter);
						Benutzer registrierterUser = BenutzerDatabase.insertUser(neuerBenutzer);

						if (registrierterUser != null) {

							registrierterUser.setMeiEidi(BenutzerDatabase.getBenutzerId(email));

							// Beim Registrieren auch gleich Erstellung eines Kontos damit der Nutzer gleich
							// loslegen kann

							boolean neuesKonto = KontoManager.machNeuesKontoFuer(registrierterUser);

							if (neuesKonto) {

								registrierterUser = KontoManager.updateBenutzerKontenInfo(registrierterUser);

								if (registrierterUser != null) {

									request.setAttribute("msgArt", "erfolg");
									request.setAttribute("msg", "Erfolgreich registriert.");

									HttpSession session = request.getSession();
									session.setAttribute("user", registrierterUser);
									request.getRequestDispatcher("index.jsp").forward(request, response);
								} else {
									request.setAttribute("msg",
											"Bitte versuchen Sie es erneut, es konnte kein Konto erstellt werden.");
									request.getRequestDispatcher("registrieren.jsp").forward(request, response);
								}
							} else {
								request.setAttribute("msg",
										"Bitte versuchen Sie es erneut, es konnte kein Konto erstellt werden.");
								request.getRequestDispatcher("registrieren.jsp").forward(request, response);
							}
						} else {
							request.setAttribute("msg",
									"Bitte versuchen Sie es erneut, es konnte kein Nutzer erstellt werden.");
							request.getRequestDispatcher("registrieren.jsp").forward(request, response);
						}
					} else {
						request.setAttribute("msg",
								"Die Passwörter stimmen nicht überein. Bitte versuchen Sie es erneut!");
						request.getRequestDispatcher("registrieren.jsp").forward(request, response);
					}
				} else {
					request.setAttribute("msg", "Für diese Email wurde bereits ein Account registriert.");
					request.getRequestDispatcher("registrieren.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("msg", "Ihre Email scheint nicht echt zu sein.");
				request.getRequestDispatcher("registrieren.jsp").forward(request, response);
			}

		} else {
			request.setAttribute("msg",
					"Ihr Passwort sollte aus mindestens acht Zeichen, einem Großbuchstaben, einem Kleinbuchstaben, "
							+ "sowie einem Sonderzeichen bestehen. Dabei darf das Sonderzeichen nicht am Anfang oder am Ende des Passworts stehen.");
			request.getRequestDispatcher("registrieren.jsp").forward(request, response);
		}

	}



	/**
	 * Manager nimmt sich die Logindaten aus dem Request und nutzt die checkLogin
	 * Methode der DB um zu pruefen ob diese stimmen.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void checkLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Benutzer user = null;
		String email = request.getParameter("mailadresse");
		String password = request.getParameter("passwort");

		// Das PW wird verschlüsselt und dann mit dem DB-Eintrag abgeglichen
		String sha3HexedPW = new DigestUtils("SHA3-512").digestAsHex(password);

		if (BenutzerDatabase.checkLogin(email, sha3HexedPW)) {
			user = BenutzerDatabase.updateBenutzer(email);
		}

		if (user != null) {

			HttpSession session = request.getSession();

			user = KontoManager.updateBenutzerKontenInfo(user);
			user = KategorieDB.aktualisierNutzerKategorien(user);
			if (user != null) {
				// legt die 5 neuesten Ueberweisungen im Nutzerobjekt ab - falls verfügbar (per
				// Spezifikation)
				ArrayList<Ueberweisung> ersteUeberweisungen = TransaktionsDatabase.getTransaktionen(5,
						user.getCurKonto());

				if (ersteUeberweisungen != null) {

					user.setCurKontoueberweisungen(ersteUeberweisungen);

					session.setAttribute("user", user);

					request.setAttribute("msgArt", "erfolg");
					request.setAttribute("msg", "Erfolgreich eingeloggt.");
					request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);

				}

			}

		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Einloggen nicht möglich.");
			request.getRequestDispatcher("/einloggen.jsp").forward(request, response);
		}

	}


	/**
	 * Werden Profildaten geändert, wird hier eine Verbindung zur Datenbank
	 * aufgebaut, die Daten aus dem Request genommen und versucht diese in die DB zu
	 * schreiben - die meisten Fehlermeldungen werden hierbei an den User
	 * zurückgegeben
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		request.setAttribute("msgArt", "error");
		Benutzer vorher = (Benutzer) session.getAttribute("user");
		// input sollte keinen HTML Code mehr enthalten können (Filter)
		Benutzer benutzer = null;
		String email = request.getParameter("EmailInput");
		String passwort = request.getParameter("Passwort");
		String vorname = request.getParameter("Vorname");
		String nachname = request.getParameter("Nachname");
		String rolle = request.getParameter("userart");
		int alter;
		String fallsAlter = request.getParameter("Alter");

		if (!vorher.getEmail().equals(email) && BenutzerDatabase.emailBekannt(email)) {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Bitte wählen sie eine andere Email-Adresse.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} else {

			if (fallsAlter.isEmpty() || fallsAlter == null) {
				alter = 2;
			} else {
				alter = (int) Integer.decode(request.getParameter("Alter"));

			}

			String sha3HexedPW = new DigestUtils("SHA3-512").digestAsHex(passwort);

			if (FehlerManager.emailEchtheitscheck(email)) {
				if (BenutzerDatabase.checkLogin(vorher.getEmail(), sha3HexedPW)) {

					benutzer = new Benutzer(email, sha3HexedPW, vorname, nachname, rolle, alter);
					benutzer.setMeiEidi(vorher.getMeiEidi());
					boolean updateErfolg = BenutzerDatabase.updateUser(benutzer);

					if (updateErfolg) {

						benutzer = BenutzerDatabase.updateBenutzer(email);
						benutzer = KontoManager.updateBenutzerKontenInfo(benutzer);
						benutzer = KategorieDB.aktualisierNutzerKategorien(benutzer);
						session.setAttribute("user", benutzer);
						request.setAttribute("msgArt", "erfolg");
						request.setAttribute("msg", "Die Daten wurden erfolgreich geändert.");
						request.getRequestDispatcher("index.jsp").forward(request, response);
					}
				} else {
					request.setAttribute("msg", "Falsches Passwort.");
					request.getRequestDispatcher("index.jsp").forward(request, response);

				}
			} else {
				request.setAttribute("msg", "Die eingegebene Email " + email + " scheint nicht echt zu sein.");
				request.getRequestDispatcher("index.jsp").forward(request, response);

			}
		}

	}
	
	
	/**
	 * Legt für einen Nutzer entweder eine neue Kategorie an oder das erste
	 * KategorieElement gleich mit
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void neueKategorie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Benutzer user = (Benutzer) session.getAttribute("user");

		String katN = String.valueOf(request.getParameter("kategorieName"));
		String katE = String.valueOf(request.getParameter("kategorieElement"));

		if (katE == null || katE.equals("")) {
			if (KategorieDB.erstelleNeueKategorie(katN, user)) {
				user = KategorieDB.aktualisierNutzerKategorien(user);
			}

			session.setAttribute("user", user);

			request.setAttribute("msgArt", "erfolg");
			request.setAttribute("msg", "Die Kategorie " + katN + " wurde erfolgreich hinzugefügt.");
			request.getRequestDispatcher("kategorien.jsp").forward(request, response);
		}

		else if (!katE.equals("")) {
			if (KategorieDB.erstelleNeueKategorie(katN, katE, user)) {
				user = KategorieDB.aktualisierNutzerKategorien(user);

			}

			request.setAttribute("msgArt", "erfolg");
			request.setAttribute("msg", "Die Kategorie " + katN + " wurde erfolgreich hinzugefügt.");
			request.getRequestDispatcher("kategorien.jsp").forward(request, response);
		} else {
			request.setAttribute("msgArt", "error");
			request.setAttribute("msg", "Die Kategorie " + katN + " konnte nicht hinzugefügt werden.");
			request.getRequestDispatcher("kategorien.jsp").forward(request, response);
		}

	}

}
