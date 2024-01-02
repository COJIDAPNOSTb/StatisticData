import java.io.*;


public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\User\\Desktop\\Games\\Programming\\CODES\\JAVA\\Project13\\src\\static.txt";
        int[] numbers = CustomFileReader.readFromFile(fileName);

        int[] evenNumbersArray = new int[numbers.length];
        int[] oddNumbersArray = new int[numbers.length];
        int evenCount = 0;
        int oddCount = 0;

        for (int i = 0; i < numbers.length; i++) {
            if ((i + 1) % 2 == 0) {
                evenNumbersArray[evenCount++] = numbers[i];
            } else {
                oddNumbersArray[oddCount++] = numbers[i];
            }
        }

        writeToFile(evenNumbersArray, evenCount, "Y.txt", 5);
        writeToFile(oddNumbersArray, oddCount, "X.txt", 5);
        AdditionalDataWriter aDW= new AdditionalDataWriter();
        aDW.main(args);


    }

    public static void writeToFile(int[] numbers, int count, String fileName, int columns) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < count; i++) {
                writer.write(numbers[i] + " ");
                if ((i + 1) % columns == 0) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

