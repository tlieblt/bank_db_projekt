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

/**
 * Filtert HTML aus Benutzereingaben zur Vorbeugung von HTML Injection und bpsw.
 * damit verbundenem Ausführen/Nachladen von JS
 * 
 * Alle Routen werden dementsprechend überprüft
 * 
 * @author Tobias Liebl
 */
@WebFilter(urlPatterns = { "/*" })
public class HTMLFilter extends BaseFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// Wrapper der sich wie eine warme, sichere Decke um den Request legt
		HtmlRequestWrapper wrapper = new HtmlRequestWrapper((HttpServletRequest) req);
		chain.doFilter(wrapper, res);
	}
}

/**
 * Erweitert Funktionen des HttpServletResponseWrappers *
 */
class HtmlRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest {

	/**
	 * Konstruktor
	 * 
	 * @param request
	 */
	public HtmlRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 
	 * @param str
	 * @return String ohne die wichtigsten HTML-Zeichen
	 */
	public String getParameter(String str) {
		/*
		 * Zeichen die für HTML Code benutzt werden können werden gefilter inklusive &
		 * als Escape zeichen z.B. &lt; könnte als < encoded in der DB landen und danach
		 * auf der Seite des Nutzers von einem Tag dann trotzdem als "lebendiger" HTML
		 * Code die Seite verändern. An sich kann den zwar erst ein eingelogger Nutzer
		 * schreiben, aber sicher is sicher.
		 */
		return super.getParameter(str) == null ? "" : super.getParameter(str).replaceAll("<(.|\n&%)*?>", "");

	}

}
