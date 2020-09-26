package org.testaufgabe;


import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class Reader {
    private Scanner scan;
    private Random ran;
    public Reader(){
        scan = new Scanner(System.in);
        ran = new Random();
    }

    /**
     * Scannt und returnt einen String-Wert
     * @return Scannt und returnt einen String-Wert
     */
    public String scans(){
        return scan.next();
    }

    /**
     * Scannt und returnt einen Integer-Wert
     * @return Scannt und returnt einen Integer-Wert
     */
    public int scani() {

        try {
            return scan.nextInt();

        }
        catch (InputMismatchException ex){
            System.out.println("Please type in a number!");
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        }

    }

    /**
     * Gibt Random 0 oder 1 zurück
     * @return Gibt Random 0 oder 1 zurück
     */
    public int randomint(){
        return ran.nextInt(2);
    }
}