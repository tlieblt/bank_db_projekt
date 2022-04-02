package manager;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Klasse die benoetigte Spalten nach Vorlage kontouebersicht.jsp sortiert
 * 
 * @author Sven de Haan
 *
 */
public class ExcelSortierManager {
	/**
	 * Methode die Versender Spalte an erste Stelle sortiert
	 * 
	 * @param sheet
	 * @param file
	 */
	public void sortiereVersenderSpalte(XSSFSheet sheet, File file) {
		DataFormatter formatter = new DataFormatter();

		String spaltenUeberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
		String spaltenUeberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
		String spaltenUeberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
		String spaltenUeberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
		String spaltenUeberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
		// starte Methode wenn Versender nicht an erster Stelle ist
		if (!spaltenUeberschrift1.equalsIgnoreCase("Versender")) {
			Iterator<Row> sortiereIterator = sheet.iterator();
			while (sortiereIterator.hasNext()) {
				Row row = sortiereIterator.next();
				// wenn Spalte nicht an erster Stelle, setze Zielzelle auf erste Stelle
				if (spaltenUeberschrift2.equalsIgnoreCase("Versender")
						|| spaltenUeberschrift3.equalsIgnoreCase("Versender")
						|| spaltenUeberschrift4.equalsIgnoreCase("Versender")
						|| spaltenUeberschrift5.equalsIgnoreCase("Versender")) {
					Cell zielZelle = row.getCell(0);
					// If Bedingungen die Spalten Werte tauschen (ziemlich statisch)
					if (spaltenUeberschrift2.equalsIgnoreCase("Versender")) {
						Cell falscheZelle = row.getCell(1);
						zielZelle = row.getCell(0);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift3.equalsIgnoreCase("Versender")) {
						Cell falscheZelle = row.getCell(2);
						zielZelle = row.getCell(0);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift4.equalsIgnoreCase("Versender")) {
						Cell falscheZelle = row.getCell(3);
						zielZelle = row.getCell(0);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift5.equalsIgnoreCase("Versender")) {
						Cell falscheZelle = row.getCell(4);
						zielZelle = row.getCell(0);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

				}
			}
		}
	}

	/**
	 * Methode die IBAN Spalte an zweite Stelle sortiert
	 * 
	 * @param sheet
	 * @param file
	 */
	public void sortiereIBANSpalte(XSSFSheet sheet, File file) {
		DataFormatter formatter = new DataFormatter();
		String spaltenUeberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
		String spaltenUeberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
		String spaltenUeberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
		String spaltenUeberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
		String spaltenUeberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
		if (!spaltenUeberschrift2.equalsIgnoreCase("Versender")) {
			Iterator<Row> rowIterator1 = sheet.iterator();
			while (rowIterator1.hasNext()) {
				Row row = rowIterator1.next();
				if (spaltenUeberschrift1.equalsIgnoreCase("IBAN") || spaltenUeberschrift3.equalsIgnoreCase("IBAN")
						|| spaltenUeberschrift4.equalsIgnoreCase("IBAN")
						|| spaltenUeberschrift5.equalsIgnoreCase("IBAN")) {
					Cell zielZelle = row.getCell(1);

					if (spaltenUeberschrift1.equalsIgnoreCase("IBAN")) {
						Cell falscheZelle = row.getCell(0);
						zielZelle = row.getCell(1);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift3.equalsIgnoreCase("IBAN")) {
						Cell falscheZelle = row.getCell(2);
						zielZelle = row.getCell(1);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift4.equalsIgnoreCase("IBAN")) {
						Cell falscheZelle = row.getCell(3);
						zielZelle = row.getCell(1);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift5.equalsIgnoreCase("IBAN")) {
						Cell falscheZelle = row.getCell(4);
						zielZelle = row.getCell(1);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

				}
			}
		}
	}

	/**
	 * Methode die Beschreibungs Spalte an dritte Stelle sortiert
	 * 
	 * @param sheet
	 * @param file
	 */
	public void sortiereBeschreibungSpalte(XSSFSheet sheet, File file) {

		DataFormatter formatter = new DataFormatter();
		String spaltenUeberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
		String spaltenUeberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
		String spaltenUeberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
		String spaltenUeberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
		String spaltenUeberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
		if (!spaltenUeberschrift3.equalsIgnoreCase("Beschreibung")) {
			Iterator<Row> rowIterator1 = sheet.iterator();
			while (rowIterator1.hasNext()) {
				Row row = rowIterator1.next();
				if (spaltenUeberschrift1.equalsIgnoreCase("Beschreibung")
						|| spaltenUeberschrift2.equalsIgnoreCase("Beschreibung")
						|| spaltenUeberschrift4.equalsIgnoreCase("Beschreibung")
						|| spaltenUeberschrift5.equalsIgnoreCase("Beschreibung")) {
					Cell zielZelle = row.getCell(2);

					if (spaltenUeberschrift1.equalsIgnoreCase("Beschreibung")) {
						Cell falscheZelle = row.getCell(0);
						zielZelle = row.getCell(2);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift2.equalsIgnoreCase("Beschreibung")) {
						Cell falscheZelle = row.getCell(1);
						zielZelle = row.getCell(2);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift4.equalsIgnoreCase("Beschreibung")) {
						Cell falscheZelle = row.getCell(3);
						zielZelle = row.getCell(2);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift5.equalsIgnoreCase("Beschreibung")) {
						Cell falscheZelle = row.getCell(4);
						zielZelle = row.getCell(2);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

				}
			}
		}
	}

	/**
	 * Methode die Betrags Spalte an vierte Stelle sortiert
	 * 
	 * @param sheet
	 * @param file
	 */
	public void sortiereBetragSpalte(XSSFSheet sheet, File file) {

		DataFormatter formatter = new DataFormatter();
		String spaltenUeberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
		String spaltenUeberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
		String spaltenUeberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
		String spaltenUeberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
		String spaltenUeberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
		if (!spaltenUeberschrift4.equalsIgnoreCase("Betrag")) {
			Iterator<Row> rowIterator1 = sheet.iterator();
			while (rowIterator1.hasNext()) {
				Row row = rowIterator1.next();
				if (spaltenUeberschrift1.equalsIgnoreCase("Betrag") || spaltenUeberschrift2.equalsIgnoreCase("Betrag")
						|| spaltenUeberschrift3.equalsIgnoreCase("Betrag")
						|| spaltenUeberschrift5.equalsIgnoreCase("Betrag")) {
					Cell zielZelle = row.getCell(3);

					if (spaltenUeberschrift1.equalsIgnoreCase("Betrag")) {
						Cell falscheZelle = row.getCell(0);
						zielZelle = row.getCell(3);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift2.equalsIgnoreCase("Betrag")) {
						Cell falscheZelle = row.getCell(1);
						zielZelle = row.getCell(3);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift3.equalsIgnoreCase("Betrag")) {
						Cell falscheZelle = row.getCell(2);
						zielZelle = row.getCell(3);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift5.equalsIgnoreCase("Betrag")) {
						Cell falscheZelle = row.getCell(4);
						zielZelle = row.getCell(3);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

				}
			}
		}
	}

	/**
	 * Methode die Zeit Spalte an fünfter Stelle sortiert
	 * 
	 * @param sheet
	 * @param file
	 */
	public void sortiereZeitSpalte(XSSFSheet sheet, File file) {

		DataFormatter formatter = new DataFormatter();
		String spaltenUeberschrift1 = sheet.getRow(0).getCell(0).getStringCellValue();
		String spaltenUeberschrift2 = sheet.getRow(0).getCell(1).getStringCellValue();
		String spaltenUeberschrift3 = sheet.getRow(0).getCell(2).getStringCellValue();
		String spaltenUeberschrift4 = sheet.getRow(0).getCell(3).getStringCellValue();
		String spaltenUeberschrift5 = sheet.getRow(0).getCell(4).getStringCellValue();
		if (!spaltenUeberschrift5.equalsIgnoreCase("Zeit")) {
			Iterator<Row> rowIterator1 = sheet.iterator();
			while (rowIterator1.hasNext()) {
				Row row = rowIterator1.next();
				if (spaltenUeberschrift1.equalsIgnoreCase("Zeit") || spaltenUeberschrift2.equalsIgnoreCase("Zeit")
						|| spaltenUeberschrift3.equalsIgnoreCase("Zeit")
						|| spaltenUeberschrift4.equalsIgnoreCase("Zeit")) {
					Cell zielZelle = row.getCell(4);

					if (spaltenUeberschrift1.equalsIgnoreCase("Zeit")) {
						Cell falscheZelle = row.getCell(0);
						zielZelle = row.getCell(4);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift2.equalsIgnoreCase("Zeit")) {
						Cell falscheZelle = row.getCell(1);
						zielZelle = row.getCell(4);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift3.equalsIgnoreCase("Zeit")) {
						Cell falscheZelle = row.getCell(2);
						zielZelle = row.getCell(4);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}

					if (spaltenUeberschrift4.equalsIgnoreCase("Zeit")) {
						Cell falscheZelle = row.getCell(3);
						zielZelle = row.getCell(4);
						String temp = new String(formatter.formatCellValue(falscheZelle));
						String temp2 = new String(formatter.formatCellValue(zielZelle));
						zielZelle.setCellValue(temp);
						falscheZelle.setCellValue(temp2);
					}
				}
			}
		}
	}
}
