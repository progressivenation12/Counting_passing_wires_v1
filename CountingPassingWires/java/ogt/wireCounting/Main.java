package ogt.wireCounting;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<Integer, Wire> wireMap = new HashMap<>();
        List<String> groupNumber = new ArrayList<>();
        Wire wire;

        Scanner scannerSystem = new Scanner(System.in);

        System.out.print("Введите место хранения файла .TXT начиная с корневой папки: ");
        String scannerString = scannerSystem.nextLine();

        Path path = Path.of(scannerString);
        Scanner scannerFile = new Scanner(path);

        System.out.print("Введите адреса групп через пробел: ");

        String string = scannerSystem.nextLine();

        if (string.isEmpty()) {
            System.out.println("Адреса групп не введены! ");
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

        System.out.println("Количество проводов в жгуте: " + wireMap.size() + " шт.");
        System.out.println("Количество проводов проходящих через уплотнитель КР: " + counterWires + " шт.");
    }
}
