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
public class FileReceiver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileReceiveListener listener = new FileReceiveListener() {
            @Override
            public void connectedStatus(boolean connected) {
                System.out.println("Connected: " + connected);
            }

            @Override
            public void receivingStarted(String imei, String file) {

            }

            @Override
            public void receivingEnded(String imei, String file) {

            }

            @Override
            public void receiveProgress(String imei, String file, long received, long total, int progress) {
                System.out.println("Progress: " + progress);
            }

            @Override
            public void receivingError(String imei, String file, String message) {

            }
        };

        FileServer fs = new FileServer(8080);
        fs.setListener(listener);
        fs.startServer();

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
