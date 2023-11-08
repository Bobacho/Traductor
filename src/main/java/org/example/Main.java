package org.example;

import org.example.controllers.Parser;
import org.example.controllers.Scanner;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        java.util.Scanner sentencia=new java.util.Scanner(System.in);
        System.out.println("Ingrese la cadena a reconocer:");
        Scanner scanner=new Scanner(sentencia.nextLine());
        Parser parser=new Parser(scanner);
        parser.run();
    }
}