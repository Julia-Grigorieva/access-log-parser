import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int secondNumber = new Scanner(System.in).nextInt();
        int sum = firstNumber+secondNumber;
        int difference = firstNumber-secondNumber;
        int multiplication = firstNumber*secondNumber;
        double quotient = (double)firstNumber/secondNumber;
        System.out.println("Сумма чисел: " + sum);
        System.out.println("Разность чисел: " + difference);
        System.out.println("Произведение чисел: " + multiplication);
        System.out.println("Частное чисел: " + quotient);
    }
}
