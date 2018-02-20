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
public interface FileReceiveListener {
    
    void connectedStatus(boolean connected);
    void receivingStarted(String imei, String file);
    void receivingEnded(String imei, String file);
    void receiveProgress(String imei, String file, long received, long total, int progress);
    void receivingError(String imei, String file, String message);
}
