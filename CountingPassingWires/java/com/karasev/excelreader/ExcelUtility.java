package com.karasev.excelreader;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

        List<String> groupNumber = new ArrayList<>();

        if (numberGroup.isEmpty()) {
            System.out.println("Адреса групп не введены! ");
            System.exit(0);
        } else {
            groupNumber = List.of(numberGroup.toUpperCase().split(" "));
        }

        int[] counterWires = readFileAndAnalysis(file, groupNumber);

        scannerSystem.close();

        System.out.println("Количество проводов в жгуте: " + counterWires[1] + " шт.");
        System.out.println("Количество проводов проходящих через уплотнитель КР: " + counterWires[0] + " шт.");
    }

    //метод для проверки формата файла по расширению
    public static boolean hasExcelFormat(File file) {
        return file.getName().endsWith(XLSX) || file.getName().endsWith(XLS);
    }

    //метод для анализа полученого файла
    private static int[] readFileAndAnalysis(File file, List<String> groupNumber) {
        String extension = FilenameUtils.getExtension(file.getName());
        char groupLetter = groupNumber.get(0).charAt(0);
        var cellFrom = 1;
        var cellTo = 2;
        int[] counterWires = new int[2];

        try {
            if (hasExcelFormat(file)) {
                FileInputStream fileInputStream = new FileInputStream(file);

                if ((extension).equals(TYPE.XLSX.getTitle())) {
                    XSSFWorkbook workbookXLSX = new XSSFWorkbook(fileInputStream);
                    XSSFSheet sheetXLSX = workbookXLSX.getSheetAt(0);

                    counterWires = getCounterWires(sheetXLSX, groupNumber, groupLetter, cellFrom, cellTo);

                    workbookXLSX.close();
                } else if (extension.equals(TYPE.XLS.getTitle())) {
                    HSSFWorkbook workbookXLS = new HSSFWorkbook(fileInputStream);
                    HSSFSheet sheetXLS = workbookXLS.getSheetAt(0);

                    counterWires = getCounterWires(sheetXLS, groupNumber, groupLetter, cellFrom, cellTo);

                    workbookXLS.close();
                }
            } else {
                System.out.println("Wrong format, b.b. Excel file with extension .xlsx or .xls");
                System.exit(11);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }

        return counterWires;
    }

    //метод для подсчета количества проводов в листе и проводов проходящих через чехол КР
    private static int[] getCounterWires(Sheet sheet,
                                         List<String> groupNumber,
                                         char groupLetter,
                                         int cellFrom, int cellTo) {
        int[] counterArrays = new int[2];

        for (Row currentRow : sheet) {
            var from = currentRow.getCell(cellFrom).getStringCellValue().trim().toUpperCase();
            var to = currentRow.getCell(cellTo).getStringCellValue().trim().toUpperCase();

            if (!(from.isEmpty() || to.isEmpty())) {
                if (groupNumber.contains(from) && (!groupNumber.contains(to) && to.charAt(0) == groupLetter)) {
                    counterArrays[0]++;
                }
                if (groupNumber.contains(to) && (!groupNumber.contains(from) && from.charAt(0) == groupLetter)) {
                    counterArrays[0]++;
                }
                if ((!groupNumber.contains(from) && from.charAt(0) == groupLetter) && to.charAt(0) != groupLetter) {
                    counterArrays[0]++;
                }
                if (from.charAt(0) != groupLetter && (!groupNumber.contains(to) && to.charAt(0) == groupLetter)) {
                    counterArrays[0]++;
                }
            }
            counterArrays[1]++;
        }

        return counterArrays;
    }
}
