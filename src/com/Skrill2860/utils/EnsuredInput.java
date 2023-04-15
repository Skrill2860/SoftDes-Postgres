package com.Skrill2860.utils;

import java.util.Scanner;

import static com.Skrill2860.utils.Validation.tryIntParse;
import static com.Skrill2860.utils.Validation.tryLongParse;

public class EnsuredInput {
    public static int getIntEnsured(Scanner scanner) {
        return getIntEnsured(scanner, "");
    }

    public static int getIntEnsured(Scanner scanner, String hint) {
        System.out.print(hint);
        String input;
        while (!tryIntParse(input = scanner.nextLine())) {
            System.out.println("Введите целое число");
        }
        return Integer.parseInt(input);
    }

    public static Long getLongEnsured(Scanner scanner) {
        return getLongEnsured(scanner, "");
    }

    public static Long getLongEnsured(Scanner scanner, String hint) {
        System.out.print(hint);
        String input;
        while (!tryLongParse(input = scanner.nextLine())) {
            System.out.println("Введите целое число");
        }
        return Long.parseLong(input);
    }
}
