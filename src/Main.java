import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       int count = 0;
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
        int start = userAgent.indexOf('(');
        int end = userAgent.indexOf(')');
        if (start != -1 && end != -1 && start < end) {
            String firstBrackets = userAgent.substring(start + 1, end);
            String[] parts = firstBrackets.split(";");
            if (parts.length >= 2) {
                String fragment = parts[1].trim();
                return fragment.split("/")[0].trim();
            }
        }
        return null;
    }
}
