package tags;

import benutzer.Benutzer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Sorgt dafür, das uneingeloggte Nutzer nur Einloggen und Registrieren sehen
 * eingeloggte Nutzer bekommen alle Funktionen im Header angezeigt
 * 
 * @author Sven de Haan
 */

public class ErlaubteFunktionen extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;

	public int doStartTag() {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		Benutzer user = null;
		JspWriter out = pageContext.getOut();

		try {
			user = (Benutzer) session.getAttribute("user");

			if (user == null) {
				out.println("<a class='nav-link active' href='einloggen.jsp'>Einloggen</a>");
				out.println("<a class='nav-link active' href='registrieren.jsp'>Registrieren</a>");
			}

			else if (user != null) {
				return EVAL_BODY_INCLUDE;
			}

		} catch (NullPointerException e) {
			System.err.println("[benutzertag] Macht Nullprobleme " + e);
		} catch (IOException oi) {

		}

		return SKIP_BODY;

	}

}
