/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfiletest.fileexchange;

/**
 *
 * @author Champ
 */
public class Utils {

    public static final int BYTE_TO_LONG_SIZE = 8;

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[BYTE_TO_LONG_SIZE];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < BYTE_TO_LONG_SIZE; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static int calcProgress(long processed, long total) {
        int progress = (int)(processed * 100.0 / total + 0.5);
        return progress;
    }
}
