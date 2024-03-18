package ogt.wireCounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("d:\\��\\���\\025\\FOTON\\A32R32.3724 025-160;-170\\FilesForComparingWireTables\\A32R32.3724025-170 2024.12.03.txt");
        Scanner scannerFile = new Scanner(file);
        Scanner scannerSystem = new Scanner(System.in);

        Map<Integer, Wire> wireMap = new HashMap<>();
        List<String> groupNumber = new ArrayList<>();

        Wire wire;

        System.out.print("������� ������ ����� ����� ������: ");

        String string = scannerSystem.nextLine();

        if (string.isEmpty()) {
            System.out.println("������ ����� �� �������! ");
            System.exit(0);
        } else {
            groupNumber = List.of(string.split(" "));
        }

        while (scannerFile.hasNextLine()) {
            String line = scannerFile.nextLine();
            String[] splitLine = line.split("[;\t]");
            wire = new Wire(Integer.parseInt(splitLine[0]), splitLine[1].trim(), splitLine[2].trim());

            wireMap.put(wire.number(), wire);
        }

        int counterWires = 0;
        char groupLetter = groupNumber.get(0).charAt(0);

        for (Wire w : wireMap.values()) {
            if (!(w.from().isEmpty() || w.to().isEmpty())) {
                if (groupNumber.contains(w.from()) && (!groupNumber.contains(w.to()) && w.to().charAt(0) == groupLetter)) {
                    counterWires++;
                }
                if (groupNumber.contains(w.to()) && (!groupNumber.contains(w.from()) && w.from().charAt(0) == groupLetter)) {
                    counterWires++;
                }

                if ((!groupNumber.contains(w.from()) && w.from().charAt(0) == groupLetter) && w.to().charAt(0) != groupLetter) {
                    counterWires++;
                }
                if (w.from().charAt(0) != groupLetter && (!groupNumber.contains(w.to()) && w.to().charAt(0) == groupLetter)) {
                    counterWires++;
                }
            }
        }

        scannerSystem.close();
        scannerFile.close();

        System.out.println("���������� �������� � �����: " + wireMap.size() + " ��.");
        System.out.println("���������� �������� ���������� ����� ����������� ��: " + counterWires + " ��.");
    }
}