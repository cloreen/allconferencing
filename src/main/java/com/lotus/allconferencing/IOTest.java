package com.lotus.allconferencing;

import java.util.Scanner;

public class IOTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String question = "How old are you?";
        System.out.println(question);
        String answer = scanner.nextLine();
    }
}
