import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       int count = 0;

       Statistics statistics = new Statistics();

       while(true) {
           System.out.println("Введите путь к файлу:");
           String path = new Scanner(System.in).nextLine();
           File file = new File(path);
           boolean fileExists = file.exists();
           boolean isDirectory = file.isDirectory();
           if (!fileExists || isDirectory) {
               System.out.println("указанный файл не существует или указанный путь является путём к папке");
               continue;
           }
           if (fileExists) {
               System.out.println("Путь указан верно");
               count++;
               System.out.println("Это файл номер " + count);
           }
           int lineCount = 0;
           int googleBotRequests = 0;
           int yandexBotRequests = 0;

           try {
               FileReader fileReader = new FileReader(path);
               BufferedReader reader =
                       new BufferedReader(fileReader);
               String line;
               while ((line = reader.readLine()) != null) {
                   lineCount++;
                   int length = line.length();
                   if (length > 1024) {
                       throw new LineTooLongException("Строка превышает 1024 символа: " + length);
                   }
                   LogEntry logEntry = new LogEntry(line);
                   statistics.addEntry(logEntry);
                   System.out.println(logEntry.getDataSize());
                  String userAgent = extractUserAgent(line);
                   if (userAgent != null) {
                       String botName = getBotName(userAgent);
                       if ("Googlebot".equals(botName)) {
                           googleBotRequests++;
                       } else if ("YandexBot".equals(botName)) {
                           yandexBotRequests++;
                       }
                   }
               }
           } catch (LineTooLongException e) {
               System.err.println("Ошибка: " + e.getMessage());
               return;
           } catch (IOException e) {
               e.printStackTrace();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           System.out.println("Общее количество строк: " + lineCount);
           double googleBotShare = (double) googleBotRequests / lineCount * 100;
           double yandexBotShare = (double) yandexBotRequests / lineCount * 100;
           System.out.printf("Запросы от Googlebot: %d (%.2f%%)%n", googleBotRequests, googleBotShare);
           System.out.printf("Запросы от YandexBot: %d (%.2f%%)%n", yandexBotRequests, yandexBotShare);
           System.out.printf("Средний объем трафика за час: %.2f байт/час%n", statistics.getTotalTraffic());
           System.out.printf("Средний объем трафика за час: %.2f байт/час%n", statistics.getTrafficRate());
       }
    }
    private static String extractUserAgent(String line) {
        String[] parts = line.split("\"");
        if (parts.length > 5) {
            return parts[5];
        }
        return null;
    }

    private static String getBotName(String userAgent) {

        int compatibleIndex = userAgent.indexOf("compatible;");
        if (compatibleIndex != -1) {

            String subUserAgent = userAgent.substring(compatibleIndex);

            String[] parts = subUserAgent.split(" ");
            for (String part : parts) {

                if (part.contains("/")) {

                    return part.split("/")[0].trim();
                }
            }
        }
        return null;
    }
}
