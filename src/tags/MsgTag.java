package tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import benutzer.Benutzer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Ein Tag fuer Mitteilungen, koennte auch erweitert werden um 
 * Informationen weiterzugeben, aber bisher war dafuer noch kein Bedarf.
 * Im Tag Body kann bei Bedarf zusaetzlicher HTML Code abgelegt werden,
 * die Attribute werden direkt aus der Request ausgelesen, was sinnvoller erscheint, 
 * als diese ueber den Umweg der ${  } Formatierung extra auf der Seite aus der Request auszulesen
 * @param msgArt : Art der Message
 * @param msgArt : Konkrete Message
 * 
 * @author Tobias Liebl
 */

public class MsgTag extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;

	@SuppressWarnings("unused")
	private String msgArt;
	@SuppressWarnings("unused")
	private String msg;

	public String getMsgArt(HttpServletRequest request) {
		return (String) request.getAttribute("msgArt");
	}

	public String getMsg(HttpServletRequest request) {
		return (String) request.getAttribute("msg");
	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		JspWriter out = pageContext.getOut();
		String msgArt = "";
		String msg = "";
		try {
			msgArt = getMsgArt(request);
			msg = getMsg(request);
//Für Fehlermeldungen

			if (msgArt.equals("error")) {
				out.println("<div class='alert alert-danger' role='alert'>");
				out.println(msg);
				out.println("</div>");
				return EVAL_BODY_INCLUDE;
			}
			/*
			 * Statt einem eigenen Tag nur um den Nutzer zu grueßen, wird die Funktionalität
			 * gleich hier mit abgearbeitet - nur nach erfolgreichem Login wird der Nutzer
			 * begrüßt, sonst einfach Erfolgsmeldung
			 */

			else if (msgArt.equals("erfolg")) {
				out.println("<div class='alert alert-success' role='alert'>");
				if (msg.equals("Erfolgreich eingeloggt.")) {
					HttpSession session = request.getSession();
					Benutzer user = (Benutzer) session.getAttribute("user");
					out.println("Willkommen " + user.getVorname() + " " + user.getNachname() + "!</br>");
					out.println("Eingeloggt als " + user.getRolle() + " mit der Email " + user.getEmail() + "</br>");
					out.println("Man sieht Ihnen die " + user.getAlter() + " Jahre gar nicht an.</br>");
					out.println("</div>");
				} else {
					out.println(msg);
					out.println("</div>");
				}
				return EVAL_BODY_INCLUDE;
			}

			// Für neutrale Meldungen

			else if (msgArt.equals("info")) {
				out.println("<div class='alert alert-info' role='alert'>");
				out.println(msg);
				out.println("</div>");
				return EVAL_BODY_INCLUDE;

			}

		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return SKIP_BODY;

	}
}