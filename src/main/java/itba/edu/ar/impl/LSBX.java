package itba.edu.ar.impl;

import itba.edu.ar.api.LSB;

import java.nio.ByteBuffer;


public class LSBX implements LSB {

    // Cantidad de bits reservados al tamaño
    private static final int SIZE_LENGTH = 32;
    // Cantidad de bits que se usan en cada byte para guardar el mensaje
    private int shiftingSize;

    public LSBX(int shiftingSize) {
        if (shiftingSize < 1 || shiftingSize > 8) {
            shiftingSize = 1;
            System.out.println("Invalid shifting, must be between 1 and 8. Using 1 insted.");
        }
        this.shiftingSize = shiftingSize;
    }

    @Override
    public byte[] encrypt(byte[] message, byte[] bmp) {
        if (!canEncrypt(message, bmp)) {
            System.out.println("BMP file is too small for the message");
            return null;
        }

        int bmpIndex = 0;
        byte[] editedBmp = bmp.clone();
        for (int messageIndex = 0; messageIndex < message.length * 8; bmpIndex++) {
            for (int i = shiftingSize - 1; i >= 0; i--) {
                int bitValue = getBitValueFromArray(message, messageIndex);
                setBitValue(editedBmp, bmpIndex, i, bitValue);
                messageIndex++;
            }
        }
        return editedBmp;
    }

    @Override
    public byte[] decrypt(byte[] bmp) {
        if (bmp == null)
            return null;

        int messageLength = getMessageLength(bmp);

        return decrypt(bmp, messageLength * 8 / shiftingSize, SIZE_LENGTH / shiftingSize);
    }

    // Size en bytes
    private byte[] decrypt(byte[] toDecrypt, int size, int startByte) {
        byte[] reader = new byte[size];
        int readerIndex = 0;
        for (int decryptIndex = startByte; decryptIndex < size; decryptIndex++) {
            readerIndex = setValuesToMessage(readerIndex, toDecrypt[decryptIndex], reader);
        }
        return reader;
    }

    private byte[] decrypt(byte[] toDecrypt, int size) {
        return decrypt(toDecrypt,size,0);
    }

    private int getMessageLength(byte[] toDecypt) {
        byte[] size = decrypt(toDecypt, SIZE_LENGTH / shiftingSize);
        return ByteBuffer.wrap(size).getInt();
    }

    private int setValuesToMessage(int messageIndex, byte toDecrypt, byte[] message) {
        for (int i = shiftingSize - 1; i >= 0; i--) {
            if (getBitValue(toDecrypt, i) > 0) {
                turnBitOn(message, messageIndex);
            }
            messageIndex++;
        }
        return messageIndex;
    }

    private int getBitValue(byte b, int position) {
        return (b >> position & 1);
    }

    private int getBitValueFromArray(byte[] arr, int bit) {
        int index = bit / 8;
        int bitPosition = 7 - (bit % 8);

        return getBitValue(arr[index], bitPosition);
    }

    private void turnBitOn(byte[] arr, int pos) {
        int index = pos / 8;
        int bitPosition = 7 - (pos % 8);

        arr[index] |= (1 << bitPosition);
    }

    private void setBitValue(byte[] arr, int pos, int shifting, int value) {
        if (value == 1)
            arr[pos] |= 1 << shifting;
        else
            arr[pos] &= (255 - (1 << shifting));
    }

    private boolean canEncrypt(byte[] message, byte[] bmp) {
        int bitsToWrite = message.length * 8;
        return bmp.length * shiftingSize >= bitsToWrite;
    }
}
