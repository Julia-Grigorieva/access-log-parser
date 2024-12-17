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
           int maxLength = 0;
           int minLength = Integer.MAX_VALUE;
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

                   if (length > maxLength) {
                       maxLength = length;
                   }
                   if (length < minLength) {
                       minLength = length;
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
           System.out.println("Длина самой длинной строки: " + maxLength);
           System.out.println("Длина самой короткой строки: " + (minLength == Integer.MAX_VALUE ? 0 : minLength));

       }
    }
}
