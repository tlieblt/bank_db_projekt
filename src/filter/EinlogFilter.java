
package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filtert den Zugriff auf den Server - ohne eingeloggt zu sein gibt es nur
 * Zugriff auf Registrieren/Einloggen/Index
 * 
 * @author Tobias Liebl
 */
@WebFilter(urlPatterns = { "/*" })
public class EinlogFilter extends BaseFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);

		// Wahr falls eine Session mit Nutzer existiert
		boolean loggedIn = (session != null) && (session.getAttribute("user") != null);

		// Prüft welche Ressource angefragt wurde
		String url = String.valueOf(request.getRequestURL());
		String urlEnde = url;
		String[] zerteilteURL = urlEnde.split("/");
		urlEnde = zerteilteURL[zerteilteURL.length - 1];

		// Nicht eingeloggte Nutzer sollen sich nur Einloggen oder Registrieren können:
		boolean login = urlEnde.equals("Einloggen");
		boolean registrieren = urlEnde.equals("Registrieren");
		boolean main = urlEnde.equals("index.jsp");
		boolean loginRequest = urlEnde.equals("einloggen.jsp");
		boolean registerRequest = urlEnde.equals("registrieren.jsp");

		// Nutzer darf nur auf Login bzw. Registrierung zugreifen
		if (loggedIn || login || loginRequest || registrieren || registerRequest || main) {
			chain.doFilter(request, response);
		}
		// um auf andere Ressourcen zuzugreifen, muss sich erst eingeloggt werden
		else {
			request.setAttribute("msgArt", "info");
			request.setAttribute("msg",
					"Bitte loggen Sie sich ein oder <a href='registrieren.jsp'>Registrieren</a> sie sich.");
			// unerlaubte Zugriffsversuche loggen:
			// System.out.println(request.getRemoteAddr() +" hat versucht auf
			// Nutzerfunktionen zuzugreifen - Redirect zu Login");
			request.getRequestDispatcher("einloggen.jsp").forward(request, response);
		}

	}
}

/**
 * Erweitert Funktionen des HttpServletResponseWrappers
 * 
 *
 */
class AutorisierungsRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest {

	/**
	 * Konstruktor
	 * 
	 * @param request
	 */
	public AutorisierungsRequestWrapper(HttpServletRequest request) {
		super(request);
	}

}
