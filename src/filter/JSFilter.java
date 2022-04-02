package filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Filtert einige JavaScript relevante Begriffe aus allen Daten die auf den
 * Server geleitet werden
 * 
 * @author Tobias Liebl
 */

@WebFilter(urlPatterns = { "/*" })
public class JSFilter extends BaseFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// Wrapper der sich wie eine warme, sichere Decke um den Request legt
		XSSRequestWrapper wrapper = new XSSRequestWrapper((HttpServletRequest) req);
		chain.doFilter(wrapper, res);
	}
}

/**
 * Erweitert Funktionen des HttpServletResponseWrappers
 * 
 *
 */
class XSSRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest {
	//
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 
	 * @param str
	 * @return String ohne bestimmte JS Funktionen
	 */
	public String getParameter(String str) {
		str = super.getParameter(str);
		if (str != null) {
			Pattern scriptPattern = null;
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			str = scriptPattern.matcher(str).replaceAll(" ");
			scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE);
			str = scriptPattern.matcher(str).replaceAll(" ");
			scriptPattern = Pattern.compile("onmouseover=", Pattern.CASE_INSENSITIVE);
			str = scriptPattern.matcher(str).replaceAll(" ");

		}
		return str;
	}

}
