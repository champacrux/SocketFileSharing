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
public class FileHeader {

    public static final int FILE_SIZE_HEADER_LENGTH = 8;
    public static final int IMEI_HEADER_LENGTH = 20;
    public static final int FILE_NAME_HEADER_LENGTH = 100;

    public String imei;
    public long fileSize;
    public String fileName;

    public byte headerBytes[];

    public FileHeader() {

    }

    public FileHeader(byte headerBytes[]) {
        this.headerBytes = headerBytes;
        processByteHeader();
    }

    private void processByteHeader() {

        System.out.println("Total Header Size: " + headerBytes.length);

        byte[] fileSizeBytes = new byte[FILE_SIZE_HEADER_LENGTH];
        System.arraycopy(headerBytes, 0, fileSizeBytes, 0, FILE_SIZE_HEADER_LENGTH);
        fileSize = Utils.bytesToLong(fileSizeBytes);

        byte[] imeiBytes = new byte[IMEI_HEADER_LENGTH];
        System.arraycopy(headerBytes, FILE_SIZE_HEADER_LENGTH, imeiBytes, 0, IMEI_HEADER_LENGTH);
        imei = new String(imeiBytes);

        byte[] nameBytes = new byte[FILE_NAME_HEADER_LENGTH];
        System.arraycopy(headerBytes, FILE_SIZE_HEADER_LENGTH + IMEI_HEADER_LENGTH, nameBytes, 0, FILE_NAME_HEADER_LENGTH);
        fileName = new String(nameBytes);
        System.out.println("Processed name: " + fileName);
        int index = fileName.indexOf('0');
        if (index > 0) {
            // strip the padded characters
            fileName = fileName.substring(0, fileName.indexOf('0'));
        }
    }

    public byte[] getHeaderBytes() {
        byte[] headerBytes = new byte[getTotalHeaderLength()];

        byte[] fileSizeBytes = Utils.longToBytes(fileSize);
        System.out.println("File Size Bytes Length: " + fileSizeBytes.length);
        byte[] imeiBytes = getIMEIBytesWithPadding();
        System.out.println("IMEI Bytes Length: " + imeiBytes.length);
        byte[] nameBytes = getNameBytesWithPadding();
        System.out.println("Name Bytes Length: " + nameBytes.length);

        System.arraycopy(fileSizeBytes, 0, headerBytes, 0, fileSizeBytes.length);
        System.arraycopy(imeiBytes, 0, headerBytes, FILE_SIZE_HEADER_LENGTH, imeiBytes.length);
        System.arraycopy(nameBytes, 0, headerBytes, FILE_SIZE_HEADER_LENGTH + IMEI_HEADER_LENGTH, nameBytes.length);

        return headerBytes;
    }

    /**
     * Total header size of IMEI is 20. If IMEI is less then 20, then we have to
     * add leading zeros in it. This function does all this for us
     *
     * @return
     */
    private byte[] getIMEIBytesWithPadding() {
        // main imeiHeader which will be returned
        byte[] imeiHeader = new byte[IMEI_HEADER_LENGTH];
        // imeiBytes of the IMEI
        byte[] imeiBytes = imei.getBytes();
        // lenght of imeiBytes
        int imeiBytesLength = imeiBytes.length;
        // no of leading zeros we have to add
        int noOfPaddedZeros = 0;
        // if imeiBytes is less then the header
        if (imeiBytesLength < IMEI_HEADER_LENGTH) {
            noOfPaddedZeros = IMEI_HEADER_LENGTH - imeiBytesLength;
            for (int i = 0; i < noOfPaddedZeros; i++) {
                imeiHeader[i] = 0;
            }
        }
        System.arraycopy(imeiBytes, 0, imeiHeader, noOfPaddedZeros, imeiBytesLength);
        return imeiHeader;
    }

    private byte[] getNameBytesWithPadding() {
        byte[] nameHeader = new byte[FILE_NAME_HEADER_LENGTH];

        byte[] nameBytes = fileName.getBytes();

        int nameBytesLength = nameBytes.length;
        /**
         * write name bytes into header, if name length is less then name header
         * length, then write name length header, otherwise skip last one
         */
        System.arraycopy(nameBytes, 0, nameHeader, 0,
                nameBytesLength < FILE_NAME_HEADER_LENGTH ? nameBytesLength : FILE_NAME_HEADER_LENGTH);
        for (int i = nameBytesLength; i < FILE_NAME_HEADER_LENGTH; i++) {
            nameHeader[i] = '0';
        }

        return nameHeader;
    }

    /**
     * get the total length of header
     *
     * @return size in integer
     */
    public int getTotalHeaderLength() {
        return FILE_SIZE_HEADER_LENGTH
                + IMEI_HEADER_LENGTH
                + FILE_NAME_HEADER_LENGTH;
    }
}
