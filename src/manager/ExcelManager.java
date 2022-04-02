package manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import benutzer.Benutzer;
import benutzer.Ueberweisung;
import database.TransaktionsDatabase;

/**
 * @author Sven de Haan
 * 
 * Klasse die Excel Funktionen verwaltet
 *
 */
public class ExcelManager {
	static String fileDictName = "";

	/**
	 * Methode die Werte aus kontouebersicht.jsp(Datenbank) ausliest und dann in
	 * eine Excel Datei schreibt
	 * 
	 * @param request
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServletException
	 */
	public void SchreibeExcel(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, ServletException {
		HashMap<String, String[]> kategorien;
		ArrayList<Ueberweisung> ueberweisungen;
		HttpSession session = request.getSession();

		// aktuelle Ueberweisungen in kontouebersicht.jsp getten
		Benutzer user = (Benutzer) session.getAttribute("user");
		String url = request.getHeader("referer");
		kategorien = user.getKategorien();
		if (url.contains("Suche")) {
			ueberweisungen = user.getSuchErgebnisse();
			request.getRequestDispatcher("/suchergebnisse.jsp").forward(request, response);
		} else {
			ueberweisungen = user.getCurKontoUeberweisungen();
		}

		// Lass Speicherort auswaehlen und die Datei als .xlsx speichern
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle("Wählen Sie den Speicherort der Datei:");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", ".xlsx");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setSelectedFile(new File(fileDictName));
		fileChooser.setVisible(true);
		// setze Dateiname
		int ausgewaehlt = fileChooser.showOpenDialog(fileChooser);

		if (ausgewaehlt == JFileChooser.APPROVE_OPTION) {
			fileDictName = fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			return;
		}
		// erstelle neue Excel Datei mit eingegebenen Dateiname
		File datei = new File(fileDictName);
		if (datei.exists() == false) {
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("Exportierte Überweisungen");

			// Befülle Map mit Objekten
			Map<Integer, Object[]> daten = new TreeMap<Integer, Object[]>();
			// jede Ueberweisung durchgehen
			for (int i = 0; i < ueberweisungen.size(); i++) {
				Ueberweisung aktuelleUeberweisung = ueberweisungen.get(i);
				String betrag = String.valueOf(aktuelleUeberweisung.getBetrag());
				String saubererBetrag = "";
				if (betrag.length() < 3) {
					saubererBetrag = "0," + betrag + "€";
				} else {
					saubererBetrag = betrag.substring(0, betrag.length() - 2) + "."
							+ betrag.substring(betrag.length() - 2) + " €";
				}
				// Kategorien ermitteln
				ArrayList<String> treffer = new ArrayList<String>();
				for (var entry : kategorien.entrySet()) {

					for (String katEl : entry.getValue()) {
						if (aktuelleUeberweisung.getBetreff().contains(katEl)
								|| aktuelleUeberweisung.getVersender().contains(katEl)) {
							treffer.add(entry.getKey());
							break;
						}
					}
				}
				String kategorieTreffer = "";
				String kategorieTrefferFinal = "";
				for (String trf : treffer) {
					kategorieTreffer += trf+", ";

				}
				if (kategorieTreffer == "") {
					kategorieTrefferFinal = "nicht kategorisiert";
				} else {
					kategorieTrefferFinal = "Kategorie: " + kategorieTreffer ;
				}

				// erstelle Objekte. Jedes Objekt = eine Ueberweisung
				daten.put(0, new Object[] { "ID", "Versender", "IBAN", "Beschreibung", "Betrag", "Zeit", "Kategorie" });

				daten.put(i + 1, new Object[] { i + 1, aktuelleUeberweisung.getVersender(),
						aktuelleUeberweisung.getVersender_iban(), aktuelleUeberweisung.getBetreff(), saubererBetrag,
						aktuelleUeberweisung.getTimestamp().toString().substring(0, 19), kategorieTrefferFinal });
			}
			// Iteriere über die Map
			Set<Integer> keyset = daten.keySet();

			int reihennummer = 0;
			for (Integer key : keyset) {
				// Excel Reihe erstellen
				Row row = sheet.createRow(reihennummer++);

				// gette Daten
				Object[] objekte = daten.get(key);

				int zellnummer = 0;

				for (Object objekt : objekte) {
					Cell cell = row.createCell(zellnummer++);
					if (objekt instanceof String) {
						cell.setCellValue((String) objekt);
					} else if (objekt instanceof Integer) {
						cell.setCellValue((Integer) objekt);
					}
				}
			}
			try {

				// Schreibe Datei
				FileOutputStream out = new FileOutputStream(datei + ".xlsx");
				workbook.write(out);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Methode die Excel Datei importiert, die Spalten an die Datenbank anpasst und
	 * dann die Überweisungen in die Datenbank einfügt
	 * 
	 * @param request
	 * @param response
	 * @return false wenn Spaltenueberschriften nicht mit Datenbank übereinstimmt
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@SuppressWarnings("deprecation")
	public void LeseExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		// ArrayList für Excel Zellenwerte später
		ArrayList<Object> ueberweisungen = new ArrayList<>();
		JOptionPane.showMessageDialog(null,
				"Hinweis: Damit der Import funktioniert, muss Ihre Excel Datei die Spaltenüberschriften Versender, IBAN, Beschreibung, Betrag und Zeit beinhalten. Unnötige Spalten werden ignoriert. \r\nAchten Sie auch darauf, dass die richtigen Werte (Formate) in den jeweiligen Spalten stehen.");
		try {
			// Lasse Datei auswaehlen fuer Import ueber FileChooser
			File datei = new File("");
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setDialogTitle("Wählen Sie die Datei, die Sie öffnen wollen:");
			int klick = fileChooser.showOpenDialog(fileChooser);
			FileInputStream input = null;
			if (klick == JFileChooser.APPROVE_OPTION) {
				datei = fileChooser.getSelectedFile();
				input = new FileInputStream(datei);
			} else if (klick == JFileChooser.CANCEL_OPTION) {
				request.setAttribute("msgArt", "error");
				request.setAttribute("msg", "Sie haben den Import abgebrochen.");
				request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
			}

			try (
			    // Erstellt ein Excel Workbook mit ausgewaehlter Datei
				XSSFWorkbook wb = new XSSFWorkbook(input)) {
				XSSFSheet sheet = wb.getSheetAt(0);
				// Ueber Excel Datei iterieren
				Iterator<Row> loeschIterator = sheet.iterator();
				while (loeschIterator.hasNext()) {

					Row row = loeschIterator.next();
					// Schleife die Spalten loescht solange 6 oder mehr Spalten vorhanden sind
					for (int i = row.getLastCellNum(); i > 5; i--) {
						int loeschbareSpalte = ermittelUnnoetigeSpalte(sheet);
						deleteColumn(sheet, loeschbareSpalte);
					}
					// Weniger als 5 Spalten = Abbruch
					if (row.getLastCellNum() < 5) {
						request.setAttribute("msgArt", "error");
						request.setAttribute("msg", "Ihre Excel Datei enthält nich die vorgegebenen Spalten.");
						request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
					}
				}
				// Ueberpruefung ob Spaltenueberschriften aus Datenbank vorhanden sind
				String spaltenueberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
				String spaltenueberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
				String spaltenueberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
				String spaltenueberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
				String spaltenueberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
				ArrayList<String> spaltenUeberschriften = new ArrayList<String>();
				spaltenUeberschriften.add(spaltenueberschrift1);
				spaltenUeberschriften.add(spaltenueberschrift2);
				spaltenUeberschriften.add(spaltenueberschrift3);
				spaltenUeberschriften.add(spaltenueberschrift4);
				spaltenUeberschriften.add(spaltenueberschrift5);

				if (!spaltenUeberschriften
						.containsAll(Arrays.asList("Versender", "IBAN", "Beschreibung", "Betrag", "Zeit"))) {
					request.setAttribute("msgArt", "error");
					request.setAttribute("msg", "Ihre Excel Datei enthält nich die vorgegebenen Spalten.");
					request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
				}
				// Sortiere Spalten nach Datenbankvorlage
				ExcelSortierManager sortiere = new ExcelSortierManager();
				sortiere.sortiereVersenderSpalte(sheet, datei);

				sortiere.sortiereIBANSpalte(sheet, datei);

				sortiere.sortiereBeschreibungSpalte(sheet, datei);

				sortiere.sortiereBetragSpalte(sheet, datei);

				sortiere.sortiereZeitSpalte(sheet, datei);
				// Excel Zellenwerte zu ArrayList hinzufuegen
				Iterator<Row> hinzufuegenIterator = sheet.iterator();
				while (hinzufuegenIterator.hasNext()) {

					Row row = hinzufuegenIterator.next();

					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {

						Cell cell = cellIterator.next();
						// Erste Zeile ueberspringen da Spaltenueberschriften nicht benoetigt werden
						if (row.getRowNum() == 0) {
							continue;
						}
						// Pruefe Zellen Typen fuer richtige Aufnahme in ArrayList
						switch (cell.getCellType()) {
						case STRING:
							ueberweisungen.add(cell.getStringCellValue());

							break;
						case NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								ueberweisungen.add(cell.getDateCellValue());
							} else if (cell.getCellType() == CellType.NUMERIC) {
								ueberweisungen.add(cell.getNumericCellValue());
							}

							break;
						default:
							break;

						}

					}

				}

				// aktueller Nutzer
				Benutzer user = (Benutzer) session.getAttribute("user");
				// gehe Excel Werte in ArrayList durch
				for (int i = 0; i <= ueberweisungen.size(); i++) {
					// Alle 5 Schleifendurchgaenge, inserte Ueberweisung in Datenbank
					if (i % 5 == 0 && i > 1) {
						// wandle Datum aus Excel in Timestamp um
						Date altesDateFormat = new Date();
						String altesDatum = ueberweisungen.get(i - 1).toString();
						// Prüfe anhand der String Länge welches Datumsformat geneommen werden soll.
						// Wenn nicht im Bereich, nutze aktuelles Datum als Richtwert
						if (ueberweisungen.get(i - 1).toString().length() <= 3) {
							altesDateFormat = new Date();
						}
						if (ueberweisungen.get(i - 1).toString().length() > 3
								&& ueberweisungen.get(i - 1).toString().length() <= 8) {
							altesDateFormat = new SimpleDateFormat("MM/dd/yy")
									.parse(altesDatum);
						}
						if (ueberweisungen.get(i - 1).toString().length() > 8
								&& ueberweisungen.get(i - 1).toString().length() <= 15) {
							altesDateFormat = new SimpleDateFormat("MM/dd/yy hh:mm")
									.parse(altesDatum);
						}
						if (ueberweisungen.get(i - 1).toString().length() > 15 && ueberweisungen.get(i - 1).toString().length() <= 20) {

								altesDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
										.parse(altesDatum);
							

						}
						if (ueberweisungen.get(i - 1).toString().length() > 20) {

							altesDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
									.parse(altesDatum);
						

					}
						String neuesDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(altesDateFormat);
						Timestamp endDatum = Timestamp.valueOf(neuesDateFormat);

						String betrag = ueberweisungen.get(i - 2).toString();
			
							if (betrag == "") {
								betrag = "0,00 €";
							}
						

						// Wenn Betrag in Excel Währungsform hat, entferne € Zeichen, Leerzeichen und
						// Kommazeichen
						if (betrag.contains("€")) {
							int stelleWoEuroist = betrag.indexOf("€");
							String derBetragOhneEuro = betrag.substring(0, stelleWoEuroist);
							int stelleWoLeerzeichenIst = derBetragOhneEuro.indexOf(" ");
							String derBetragOhneLeerzeichen = betrag.substring(0, stelleWoLeerzeichenIst);
							String derBetragOhneKomma = derBetragOhneLeerzeichen.replace(",", "");
							String derBetragOhnePunkt = derBetragOhneKomma.replace(".", "");
							int endBetrag = Integer.parseInt(derBetragOhnePunkt);
							TransaktionsDatabase.erstelleEineTransaktion(user, ueberweisungen.get(i - 5).toString(),
									ueberweisungen.get(i - 4).toString(), ueberweisungen.get(i - 3).toString(),
									endBetrag, endDatum);
							// Wenn Betrag in Excel nur normales Zahlenformat, entferne das Komma Zeichen
						} else if (betrag.contains(",")) {
							String derBetragOhneKomma = betrag.replace(",", "");
							String derBetragOhnePunkt = derBetragOhneKomma.replace(".", "");
							int endBetrag = Integer.parseInt(derBetragOhnePunkt);
							TransaktionsDatabase.erstelleEineTransaktion(user, ueberweisungen.get(i - 5).toString(),
									ueberweisungen.get(i - 4).toString(), ueberweisungen.get(i - 3).toString(),
									endBetrag, endDatum);
						} else {
							String derBetragOhnePunkt = betrag.replace(".", "");
							int endBetrag = Integer.parseInt(derBetragOhnePunkt) * 10;
							TransaktionsDatabase.erstelleEineTransaktion(user, ueberweisungen.get(i - 5).toString(),
									ueberweisungen.get(i - 4).toString(), ueberweisungen.get(i - 3).toString(),
									endBetrag, endDatum);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("msgArt", "erfolg");
		request.setAttribute("msg", "Import erfolgreich");
		request.getRequestDispatcher("/kontouebersicht.jsp").forward(request, response);
	}

	/**
	 * Methode die Spalten eines Excel Sheets loescht
	 * 
	 * @param sheet
	 * @param columnToDelete
	 */
	private void deleteColumn(XSSFSheet sheet, int columnToDelete) {
		for (int rowID = 0; rowID <= sheet.getLastRowNum(); rowID++) {
			Row row = sheet.getRow(rowID);
			// entferne Zellen in Spalte die geloescht werden soll
			for (int cellID = columnToDelete; cellID < row.getLastCellNum(); cellID++) {
				Cell alteZelle = row.getCell(cellID);
				if (alteZelle != null) {
					row.removeCell(alteZelle);
				}
				// kopiere Zellen von naechster Spalte zur alten Spalte
				Cell naechsteZelle = row.getCell(cellID + 1);
				if (naechsteZelle != null) {
					Cell neueZelle = row.createCell(cellID, naechsteZelle.getCellType());
					cloneCell(neueZelle, naechsteZelle);
					// setze Spaltenbreite
					if (rowID == 0) {
						sheet.setColumnWidth(cellID, sheet.getColumnWidth(cellID + 1));

					}
				}
			}
		}
	}

	/**
	 * Methode die ZellTypen einer Exceldatei kopiert
	 * 
	 * @param cNew
	 * @param cOld
	 */
	private void cloneCell(Cell cNew, Cell cOld) {
		cNew.setCellComment(cOld.getCellComment());
		cNew.setCellStyle(cOld.getCellStyle());

		if (CellType.BOOLEAN == cNew.getCellType()) {
			cNew.setCellValue(cOld.getBooleanCellValue());
		} else if (CellType.NUMERIC == cNew.getCellType()) {
			cNew.setCellValue(cOld.getNumericCellValue());
		} else if (CellType.STRING == cNew.getCellType()) {
			cNew.setCellValue(cOld.getStringCellValue());
		} else if (CellType.ERROR == cNew.getCellType()) {
			cNew.setCellValue(cOld.getErrorCellValue());
		} else if (CellType.FORMULA == cNew.getCellType()) {
			cNew.setCellValue(cOld.getCellFormula());
		}
	}

	/**
	 * Methode die prueft ob eine Spaltenueberschrift nicht dem kontouebersicht.jsp
	 * Schema entspricht
	 * 
	 * @param sheet
	 * @return Spalte die geloescht werden kann
	 */
	public int ermittelUnnoetigeSpalte(XSSFSheet sheet) {
		int temp = 0;

		Iterator<Row> unnoetigeSpalteIterator = sheet.iterator();
		while (unnoetigeSpalteIterator.hasNext()) {

			Row row = unnoetigeSpalteIterator.next();

			Iterator<Cell> cellunnoetigeSpalteIterator = row.cellIterator();
			while (cellunnoetigeSpalteIterator.hasNext()) {

				Cell cell = cellunnoetigeSpalteIterator.next();
				// erste Zeile wird ueberprueft nach benoetigen Spaltenueberschriften
				if (row.getRowNum() == 0) {
					if (cell.getCellType() == CellType.STRING) {
						if (cell.getStringCellValue().equals("Versender")) {
							continue;
						} else if (cell.getStringCellValue().equals("Betrag")) {
							continue;
						} else if (cell.getStringCellValue().equals("Beschreibung")) {
							continue;
						} else if (cell.getStringCellValue().equals("Zeit")) {
							continue;
						} else if (cell.getStringCellValue().equals("IBAN")) {
							continue;
						} else {

							temp = cell.getColumnIndex();

						}

					}

				}

			}

		}
		return temp;

	}

}
