package com.kewal.acute;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ValidateDetails {
    static int[][] d  = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                    {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                    {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                    {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                    {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                    {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                    {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                    {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                    {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
            };
    static int[][] p = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                    {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                    {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                    {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                    {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                    {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                    {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
            };
    static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    public static boolean validateVerhoeff(String num){
        int c = 0;
        int[] myArray = StringToReversedIntArray(num);
        for (int i = 0; i < myArray.length; i++){
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
    }

    public static boolean isAadharValid (String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = ValidateDetails.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
    private static int[] StringToReversedIntArray(String num){
        int[] myArray = new int[num.length()];
        for(int i = 0; i < num.length(); i++){
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        myArray = Reverse(myArray);
        return myArray;
    }
    private static int[] Reverse(int[] myArray){
        int[] reversed = new int[myArray.length];
        for(int i = 0; i < myArray.length ; i++){
            reversed[i] = myArray[myArray.length - (i + 1)];
        }
        return reversed;
    }

    public static boolean isPanValid (String s) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(s);

        // Check if pattern matches
        return (matcher.matches());
    }

    public static boolean isVoterValid (String s) {
        if(s.length() == 10) {
            return true;
        }
        return false;
    }

    public static boolean isDriverValid (String s) {
        if(s.length() == 15) {
            if(Character.isLetter(s.charAt(0)) && Character.isLetter(s.charAt(1))) {
                for (int i = 2; i < 12; ++i) {
                    if (!Character.isDigit(s.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isPassportValid (String s) {
        Pattern pattern = Pattern.compile("(([a-zA-Z]{1})\\d{7})");

        Matcher matcher = pattern.matcher(s);

        // Check if pattern matches
        return (matcher.matches());
    }

    public static boolean isNameValid (String s) {
        Pattern pattern = Pattern.compile("^[\\p{L} .'-]+$");

        Matcher matcher = pattern.matcher(s);

        // Check if pattern matches
        return matcher.matches();
    }

    public static boolean isNumberValid (String s) {
        Pattern pattern = Pattern.compile("^[6-9]\\d{9}$");

        Matcher matcher = pattern.matcher(s);

        // Check if pattern matches
        return matcher.matches();
    }

    public static boolean isPinCodeValid (String s) {
        Pattern pattern = Pattern.compile("^[1-9][0-9]{5}$");

        Matcher matcher = pattern.matcher(s);

        // Check if pattern matches
        return matcher.matches();
    }
}
