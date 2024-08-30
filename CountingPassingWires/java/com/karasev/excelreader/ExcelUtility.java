package com.karasev.excelreader;

import com.karasev.excelreader.entity.Wire;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ExcelUtility {
    public static String XLSX = TYPE.XLSX.getTitle();
    public static String XLS = TYPE.XLS.getTitle();

    public static void main(String[] args) {
        Scanner scannerSystem = new Scanner(System.in);

        System.out.print("Введите место хранения файла Excel начиная с корневой папки: ");
        String filePath = scannerSystem.nextLine();

        File file = new File(filePath);

        System.out.print("Введите адреса групп через пробел: ");

        String numberGroup = scannerSystem.nextLine();

        List<Wire> wireList = excelToWireTable(file);
        List<String> groupNumber = new ArrayList<>();

        if (numberGroup.isEmpty()) {
            System.out.println("Адреса групп не введены! ");
            System.exit(0);
        } else {
            groupNumber = List.of(numberGroup.split(" "));
        }

        int counterWires = 0;
        char groupLetter = groupNumber.get(0).charAt(0);

        for (Wire w : wireList) {
            if (!(w.getFrom().isEmpty() || w.getTo().isEmpty())) {
                if (groupNumber.contains(w.getFrom()) && (!groupNumber.contains(w.getTo()) && w.getTo().charAt(0) == groupLetter)) {
                    counterWires++;
                }
                if (groupNumber.contains(w.getTo()) && (!groupNumber.contains(w.getFrom()) && w.getFrom().charAt(0) == groupLetter)) {
                    counterWires++;
                }
                if ((!groupNumber.contains(w.getFrom()) && w.getFrom().charAt(0) == groupLetter) && w.getTo().charAt(0) != groupLetter) {
                    counterWires++;
                }
                if (w.getFrom().charAt(0) != groupLetter && (!groupNumber.contains(w.getTo()) && w.getTo().charAt(0) == groupLetter)) {
                    counterWires++;
                }
            }
        }

        scannerSystem.close();

        System.out.println("Количество проводов в жгуте: " + wireList.size() + " шт.");
        System.out.println("Количество проводов проходящих через уплотнитель КР: " + counterWires + " шт.");
    }

    public static boolean hasExcelFormat(File file) {
        return file.getName().endsWith(XLSX) || file.getName().endsWith(XLS);
    }

    public static List<Wire> excelToWireTable(File file) {
        List<Wire> wireList = new ArrayList<>();
        String extension = FilenameUtils.getExtension(file.getName());

        try {
            if (hasExcelFormat(file)) {
                FileInputStream fileInputStream = new FileInputStream(file);

                if ((extension).equals(TYPE.XLSX.getTitle())) {
                    XSSFWorkbook workbookXLSX = new XSSFWorkbook(fileInputStream);
                    XSSFSheet sheetXLSX = workbookXLSX.getSheetAt(0);

                    readExcelSheetAndSaveWireEntityToList(sheetXLSX, wireList);

                    workbookXLSX.close();
                } else if (extension.equals(TYPE.XLS.getTitle())) {
                    HSSFWorkbook workbookXLS = new HSSFWorkbook(fileInputStream);
                    HSSFSheet sheetXLS = workbookXLS.getSheetAt(0);

                    readExcelSheetAndSaveWireEntityToList(sheetXLS, wireList);

                    workbookXLS.close();
                }
            } else {
                System.out.println("Wrong format, b.b. Excel file with extension .xlsx or .xls");
                System.exit(11);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }

        return wireList;
    }

    private static void readExcelSheetAndSaveWireEntityToList(Sheet sheet, List<Wire> wireList) {
        for (Row currentRow : sheet) {
            Iterator<Cell> cellsInRow = currentRow.iterator();
            Wire wire = new Wire();

            int cellIndex = 0;

            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();

                switch (cellIndex) {
                    case 0 -> wire.setId((int) currentCell.getNumericCellValue());
                    case 1 -> wire.setFrom(currentCell.getStringCellValue().trim());
                    case 2 -> wire.setTo(currentCell.getStringCellValue().trim());
                    case 3 -> wire.setColor(currentCell.getStringCellValue().trim());
                    case 4 -> wire.setCrossSection(currentCell.getNumericCellValue());
                    case 5 -> wire.setLength((int) currentCell.getNumericCellValue());
                    default -> {
                    }
                }
                cellIndex++;
            }
            wireList.add(wire);
        }
    }
}
