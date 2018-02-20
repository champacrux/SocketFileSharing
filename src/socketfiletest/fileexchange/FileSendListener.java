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
public interface FileSendListener {

    void connectedStatus(boolean connected);
    void sendingStarted(String imei, String file);
    void sendingEnded(String imei, String file);
    void sendingError(String imei, String file, String message);
    void sendProgress(String imei, String file, long sent, long total, int progress);
}
