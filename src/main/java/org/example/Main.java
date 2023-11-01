package org.example;

import org.example.controllers.Parser;
import org.example.controllers.Scanner;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        java.util.Scanner sentencia=new java.util.Scanner(System.in);
        Scanner scanner=new Scanner(sentencia.nextLine());
        Parser parser=new Parser(scanner);
        parser.run();
    }
}