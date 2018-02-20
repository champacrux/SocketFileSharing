/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfiletest.fileexchange;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Champ
 */
public class FileSender {

    private static volatile boolean wait = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        FileSendListener listener = new FileSendListener() {
            @Override
            public void connectedStatus(boolean connected) {
                System.out.println("Connected: " + connected);
            }

            @Override
            public void sendingStarted(String imei, String file) {

            }

            @Override
            public void sendingEnded(String imei, String file) {
                wait = false;
            }

            @Override
            public void sendProgress(String imei, String file, long sent, long total, int progress) {
                System.out.println("Progress: " + progress);
            }

            @Override
            public void sendingError(String imei, String file, String message) {
                wait = false;
            }
        };

        FileClient fileClient = new FileClient("localhost", 8080, "client/video.mp4");
        fileClient.setListener(listener);
        fileClient.startSend();

        while (wait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Stopped");
    }

}
