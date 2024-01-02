// FileReader.java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomFileReader {
    public static int[] readFromFile(String fileName) {
        List<Integer> numbers = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                numbers.add(num);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
            e.printStackTrace();
        }

        return numbers.stream().mapToInt(Integer::intValue).toArray();
    }
}

