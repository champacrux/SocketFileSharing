/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfiletest;

import socketfiletest.fileexchange.Utils;

/**
 *
 * @author Champ
 */
public class SocketFileTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        long number = 99999999999999999L;
        System.out.println("Number: " + number);
        byte[] bytes = Utils.longToBytes(number);
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            System.out.print(aByte + " ");
        }

        long convertedNumber = Utils.bytesToLong(bytes);
        System.out.println("");
        System.out.println("Converted Number: " + convertedNumber);

        long total = 5564652L;
        long processed = 0;

        for (processed = 0; processed < total; processed += 2564) {
            int progress = Utils.calcProgress(processed, total);
            System.out.println("Progress: " + progress);
        }
    }

}
