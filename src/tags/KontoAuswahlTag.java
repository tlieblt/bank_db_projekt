package tags;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import benutzer.Benutzer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Simpler Tag der alle Konten des Nutzers in einem Dropdown-Menü anzeigt
 * 
 * @author Tobias Liebl
 *
 */
public class KontoAuswahlTag extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;

	ArrayList<String> dieKontoIbans;

	public ArrayList<String> getKontenInSession(HttpSession session) {

		Benutzer user = (Benutzer) session.getAttribute("user");

		ArrayList<String> ibans = user.getKontoIbans();

		if (ibans.isEmpty()) {
			ArrayList<String> fehler = new ArrayList<String>();
			fehler.add("Anscheinend keine Konten in der Session!");
			return fehler;
		}

		return ibans;
	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		JspWriter out = pageContext.getOut();
		Benutzer user = (Benutzer) session.getAttribute("user");

		try {

			dieKontoIbans = getKontenInSession(session);

			out.println("<select id='konto' name='konto'>");

			for (int i = 0; i < dieKontoIbans.size(); i++) {
				String selected = "";
				if (dieKontoIbans.get(i).equals(user.getCurIban())) {
					selected = "selected";
				}
				out.println("<option value='" + dieKontoIbans.get(i) + "' " + selected + ">");
				out.println(dieKontoIbans.get(i) + "</option>");
			}

		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;

	}
}