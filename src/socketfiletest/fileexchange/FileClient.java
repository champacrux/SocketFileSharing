/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfiletest.fileexchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Champ
 * 
 * This is a simple FileClient that sends file to server
 * it first send some header, and after header, it starts sending the actual file bytes
 */
public class FileClient implements Runnable {

    private final Thread mThread;

    private final String mServerAddress;
    private final int mPort;
    private final String mFile;

    private Socket mClient;

    private FileSendListener mListener;

    public FileClient(String serverAddress, int port, String file) {
        mServerAddress = serverAddress;
        mPort = port;
        mFile = file;
        mThread = new Thread(this);
    }

    public void setListener(FileSendListener listener) {
        mListener = listener;
    }

    @Override
    public void run() {
        System.out.println("Starting client");
        FileHeader fh = null;
        try {
            File file = new File(mFile);
            FileInputStream fis = new FileInputStream(file);
            fh = new FileHeader();
            fh.fileName = file.getName();
            fh.fileSize = file.length();
            fh.imei = "5521698452365";

            byte[] header = fh.getHeaderBytes();

            mClient = new Socket(mServerAddress, mPort);

            if (mListener != null) {
                mListener.connectedStatus(true);
            }

            InputStream is = mClient.getInputStream();
            OutputStream os = mClient.getOutputStream();

            if (mListener != null) {
                mListener.sendingStarted(fh.imei, fh.fileName);
            }

            os.write(header);

            byte[] buffer = new byte[4096];
            int read = -1;
            long totalSent = 0;

            while ((read = fis.read(buffer)) > 0) {
                os.write(buffer, 0, read);
                totalSent += read;

                if (mListener != null) {
                    int progress = Utils.calcProgress(totalSent, fh.fileSize);
                    mListener.sendProgress(fh.imei, fh.fileName, totalSent, fh.fileSize, progress);
                }
            }

            if (mListener != null) {
                mListener.sendingEnded(fh.imei, fh.fileName);
            }

            byte[] responseBytes = new byte[Utils.BYTE_TO_LONG_SIZE];
            is.read(responseBytes);

            int response = (int) Utils.bytesToLong(responseBytes);

            System.out.println("File sent: " + (response == FileServer.RESULT_OK ? "OK" : "ERROR"));

            mClient.close();

            fis.close();

            if (mListener != null) {
                mListener.connectedStatus(false);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
            if (mListener != null && fh != null) {
                mListener.sendingError(fh.imei, fh.fileName, ex.getMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
             if (mListener != null && fh != null) {
                mListener.sendingError(fh.imei, fh.fileName, ex.getMessage());
            }
        }

        System.out.println("Stopping client");
    }

    public void startSend() {
        mThread.start();
    }

    public void stopSend() {
        mThread.interrupt();
        try {
            mClient.close();
        } catch (IOException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
