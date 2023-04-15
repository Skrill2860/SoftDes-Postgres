package com.Skrill2860.utils;

import java.time.LocalDate;

/**
 * Class with validation utils
 */
public class Validation {

    /**
     * Checks if a string is integer
     * @param value - value to parse
     * @return true if value is Integer, else false
     */
    public static boolean tryIntParse(String value){
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    /**
     * Checks if a string is long
     * @param value - value to parse
     * @return true if value is long, else false
     */
    public static boolean tryLongParse(String value){
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    /**
     * Checks the date for correctness.
     * @param year
     * @param month
     * @param day
     * @return true if date is correct, else false
     */
    public static boolean checkDate(String year, String month, String day){
        return tryIntParse(year) && tryIntParse(month) && tryIntParse(day)
                && Integer.parseInt(year) <= LocalDate.now().getYear()
                && Integer.parseInt(year) >= 0
                && Integer.parseInt(month) <= 12
                && Integer.parseInt(day) <= 31;
    }
}
