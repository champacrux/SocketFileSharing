/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfiletest.fileexchange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Champ
 */
public class FileServer implements Runnable {

    private final Thread mThread;

    private volatile boolean keepRunning = false;

    public static final int RESULT_OK = 100;
    public static final int RESULT_ERROR = 110;

    private final int mPort;
    private ServerSocket mServerSocket;

    private FileReceiveListener mListener;

    public FileServer(int port) {
        mPort = port;
        mThread = new Thread(this);
    }

    public void setListener(FileReceiveListener listener) {
        mListener = listener;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting FileServer");
            
            FileHeader fh = null;
            mServerSocket = new ServerSocket(mPort);
            while (keepRunning) {
                try {
                    
                    System.out.println("Waiting for client to connect");
                    
                    Socket client = mServerSocket.accept();
                    
                    if (mListener != null) {
                        mListener.connectedStatus(true);
                    }
                    
                    InputStream is = client.getInputStream();
                    OutputStream os = client.getOutputStream();
                    
                    byte[] headers = readHeaderBytes(is);
                    
                    fh = new FileHeader(headers);
                    
                    if (mListener != null) {
                        mListener.receivingStarted(fh.imei, fh.fileName);
                    }
                    
                    FileOutputStream fos = new FileOutputStream(new File("files", fh.fileName));
                    
                    byte[] buffer = new byte[4096];
                    int read = 0;
                    long totalRead = 0;
                    while (totalRead < fh.fileSize) {
                        try {
                        read = is.read(buffer);
                        fos.write(buffer, 0, read);
                        totalRead += read;
                        fos.flush();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            break;
                        }
                        
                        if (mListener != null) {
                            int progress = Utils.calcProgress(totalRead, fh.fileSize);
                            mListener.receiveProgress(fh.imei, fh.fileName, totalRead, fh.fileSize, progress);
                        }
                    }
                    
                    if (mListener != null) {
                        mListener.receivingEnded(fh.imei, fh.fileName);
                    }
                    
                    fos.close();
                    
                    byte[] result = Utils.longToBytes(RESULT_OK);
                    
                    os.write(result);
                    
                    client.close();
                    
                    if (mListener != null) {
                        mListener.connectedStatus(false);
                    }
                    
                } catch (IOException ex) {
                    System.out.println("Exception Occurred");
                    Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
                    keepRunning = false;
                    if (mListener != null && fh != null) {
                        mListener.receivingError(fh.imei, fh.fileName, ex.getMessage());
                    }
                }

            }

            System.out.println("Stopping FileServer");
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] readHeaderBytes(InputStream is) throws IOException {
        byte[] headers = new byte[FileHeader.FILE_SIZE_HEADER_LENGTH
                + FileHeader.IMEI_HEADER_LENGTH
                + FileHeader.FILE_NAME_HEADER_LENGTH];
        is.read(headers);
        return headers;
    }

    public void startServer() {
        keepRunning = true;
        mThread.start();
    }

    public void stopServer() {
        keepRunning = false;
        mThread.interrupt();
        try {
            mServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long extractFileSize(byte[] array) {
        long fileSize = -1;

        return fileSize;
    }
}
