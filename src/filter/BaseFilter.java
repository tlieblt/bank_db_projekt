package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet Filter implementation class BaseFilter Grundlage für spezifischere
 * Filter
 * 
 * @author Tobias Liebl
 */
public class BaseFilter implements Filter {
	protected FilterConfig config;

	/**
	 * Konstruktor.
	 */
	public BaseFilter() {
	}

	/**
	 * FilterConfig auf null setzen um Speicher frei zu geben.
	 * 
	 * @see Filter#destroy()
	 */
	public void destroy() {
		config = null;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * Filter initialisieren
	 * 
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.config = fConfig;
	}

}
