package logicServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.ExcelManager;

/**
 * Klasse (Servlet) was Excel Datei importieren lässt
 * 
 * @author Sven de Haan
 *
 */
@WebServlet("/LeseExcelDatei")
public class LeseExcelDatei extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ExcelManager excelhelfer = new ExcelManager();
		// wenn Excel Import nicht nach Richtlinie -> Error
		excelhelfer.LeseExcel(request, response);


	}
}
