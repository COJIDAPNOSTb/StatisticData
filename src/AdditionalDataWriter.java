import java.io.*;
import java.util.*;

public class AdditionalDataWriter {
    private static int step;
    private static int min;
    private static int max;
    private static double sum;
    private static double sigma = 0;

    private static List<Double> pointNumbers=new ArrayList<>();
    private static List<Integer> middleNumbers= new ArrayList<>();
    public static void main(String[] args) {
        String xFileName = "X.txt";
        String yFileName = "Y.txt";
        String sortedXFileName = "sortedX.txt";
        String sortedYFileName = "sortedY.txt";
        String xTableFileName="tableX.txt";
        String yTableFileName = "tableY.txt";
        String midXFileName="midX.txt";
        String midYFileName="midY.txt";
        String pointXFileName="pointX.txt";
        String pointYFileName="pointY.txt";
        List<String> xLines = new ArrayList<>();
        List<String> yLines = new ArrayList<>();

        List<Integer> xNumbers = readNumbersFromFile(xFileName);
        List<Integer> yNumbers = readNumbersFromFile(yFileName);

        Collections.sort(xNumbers);
        Collections.sort(yNumbers);

        writeCountedNumbersToFile(xNumbers, sortedXFileName, xLines);
        table(xLines, "X",xTableFileName);
        writeMiddleNumbersToFile(middleNumbers,midXFileName);
        writePointNumbersToFile(pointNumbers,pointXFileName);
        writeCountedNumbersToFile(yNumbers, sortedYFileName, yLines);
        System.out.println("\n");
        table(yLines, "Y",yTableFileName);
        writeMiddleNumbersToFile(middleNumbers,midYFileName);
        writePointNumbersToFile(pointNumbers,pointYFileName);
        System.out.println("\n");
        summa(xLines, "X");
        summa(yLines, "Y");


    }

    public static List<Integer> readNumbersFromFile(String fileName) {
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numberStrings = line.trim().split("\\s+");
                for (String numStr : numberStrings) {
                    if (!numStr.isEmpty()) {
                        try {
                            int num = Integer.parseInt(numStr);
                            numbers.add(num);
                        } catch (NumberFormatException e) {
                            System.out.println("IncorrectNumber: " + numStr);

                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + fileName);
            e.printStackTrace();
        }
        return numbers;
    }


    public static void writeCountedNumbersToFile(List<Integer> numbers, String fileName, List<String> lines) {
        Map<Integer, Integer> countMap = new LinkedHashMap<>();

        for (Integer num : numbers) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            String line = entry.getKey() + " " + entry.getValue();
            lines.add(line);
        }
        max = Collections.max(numbers);
        min = Collections.min(numbers);
        step = (int) ((max - min) / 7.667);
        if (step % 2 != 0)
            step += 1;


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error with File: " + fileName);
            e.printStackTrace();
        }
    }

    public static void table(List<String> XLines, String name, String xFilename) {

        int middle=0;
        double sumW = 0;
        min = min - (step / 2);
        try (BufferedWriter xWriter = new BufferedWriter(new FileWriter(xFilename))) {
            xWriter.write("Шаг = "+step+"\n");
            xWriter.write("Начало" + name + "\tКонец" + name + "\tСередина\tЧастота\tОтносЧастота\tПлотность\tНакопление\n");
            for (int i = min; i < max; i += step) {
                int v = cSSIR(XLines, i, i + step);
                double w = (double) v / 100;
                sumW += w;
                String line = i + "\t\t" + (i + step) + "\t\t" + (i + (step / 2)) + "\t\t\t" + v + "\t\t" + w + "\t\t\t" + ((double) v / (100 * step)) + "\t" + sumW + "\n";
                middle=i+step/2;
                middleNumbers.add(middle);
                pointNumbers.add(sumW);
                xWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int cSSIR(List<String> data, int start, int end) {
        List<Integer> numbers = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();


        for (String str : data) {
            String[] parts = str.split("\\s+");

            if (parts.length >= 2) {
                try {
                    int number = Integer.parseInt(parts[0]);
                    int order = Integer.parseInt(parts[1]);
                    numbers.add(number);
                    orders.add(order);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        int sum = 0;


        for (int i = 0; i < numbers.size(); i++) {
            int currentNumber = numbers.get(i);

            if (currentNumber >= start && currentNumber < end) {
                sum += orders.get(i);
            }
        }


        return sum;
    }


    public static void summa(List<String> lines, String name) {

        for (String line : lines) {
            String[] parts = line.split("\\s+");

            if (parts.length == 2) {
                try {
                    int num = Integer.parseInt(parts[0]);
                    int freq = Integer.parseInt(parts[1]);
                    sum += num * freq;

                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format of number in line: " + line);
                }
            }
        }
        double mean = sum / 100;

        for (String line : lines) {
            String[] parts = line.split("\\s+");

            if (parts.length == 2) {
                try {
                    int num = Integer.parseInt(parts[0]);
                    int freq = Integer.parseInt(parts[1]);
                    sigma += Math.pow(num - mean, 2) * freq;
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect format of number in line: " + line);
                }
            }
        }

        sigma = Math.sqrt(sigma / 100);

        System.out.println("Сумма для " + name + ": " + sum / 100);
        System.out.println("Сигма для " + name + ": " + sigma);
    }

    public static void writeMiddleNumbersToFile(List<Integer> middleNumbers, String filename) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Integer number : middleNumbers) {
                writer.write(number.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        middleNumbers.clear();
    }
    public static void writePointNumbersToFile(List<Double> pointNumbers, String filename) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Double number : pointNumbers) {
                writer.write(number.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      pointNumbers.clear();
    }


}