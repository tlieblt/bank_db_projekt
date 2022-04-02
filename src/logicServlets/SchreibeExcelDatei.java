package logicServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.ExcelManager;

/**
 * Klasse (Servlet) was Excel Datei exportieren lässt
 * 
 * @author Sven de Haan
 *
 */
@WebServlet("/SchreibeExcelDatei")
public class SchreibeExcelDatei extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ExcelManager excelhelfer = new ExcelManager();
		excelhelfer.SchreibeExcel(request, response);
		request.setAttribute("msgArt", "erfolg");
		request.setAttribute("msg", "Export erfolgreich");
		request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);

	}
}
