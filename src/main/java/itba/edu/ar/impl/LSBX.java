package itba.edu.ar.impl;

import itba.edu.ar.api.LSB;

import java.util.BitSet;

public class LSBX implements LSB {

    // Cantidad de bits que se usan en cada byte para guardar el mensaje
    private int shiftingSize;

    public LSBX(int shiftingSize) {
        if(shiftingSize < 1 || shiftingSize > 8) {
            shiftingSize = 1;
            System.out.println("Invalid shifting, must be between 1 and 8. Using 1 insted.");
        }
        this.shiftingSize = shiftingSize;
    }

    @Override
    public byte[] encrypt(byte[] message, byte[] bmp) {
        if(!canEncrypt(message, bmp)) {
            System.out.println("BMP file is too small for the message");
            return null;
        }

        int shiftingValue = shiftingSize - 1;
        int bmpIndex = 0;
        byte[] editedBmp = bmp.clone();
        for (int messageIndex = 0; messageIndex < message.length * 8; messageIndex++) {
            if(shiftingValue < 0) {
                shiftingValue = this.shiftingSize - 1;
                bmpIndex++;
            }

            int bitValue = getBitValueFromArray(message, messageIndex);
            setBitValue(editedBmp, bmpIndex, shiftingValue, bitValue);
            shiftingValue--;
        }
        return editedBmp;
    }

    @Override
    public byte[] decrypt(byte[] bmp) {
        if(bmp == null)
            return null;

        BitSet message = new BitSet(bmp.length * shiftingSize);
        int messageIndex = 0;
        int shiftingValue = shiftingSize - 1;
        for (int bmpIndex = 0; bmpIndex < bmp.length; messageIndex++) {
            int bitValue = getBitValue(bmp[bmpIndex], shiftingValue);
            message.set(messageIndex, bitValue == 1);

            shiftingValue--;
            if(shiftingValue < 0) {
                shiftingValue = this.shiftingSize - 1;
                bmpIndex++;
            }
        }
        return message.toByteArray();
    }

    private int getBitValue(byte b, int position) {
        return (b >> position & 1);
    }

    private int getBitValueFromArray(byte[] arr, int bit) {
        int index = bit / 8;
        int bitPosition = (bit % 8);

        return getBitValue(arr[index], bitPosition);
    }

    private void setBitValue(byte[] arr, int pos, int shifting, int value) {
        if (value == 1)
            arr[pos] |= 1 << shifting;
        else
            arr[pos] &= 255 - (1 << shifting);
    }

    private boolean canEncrypt(byte[] message, byte[] bmp) {
        int bitsToWrite = message.length * 8;
        return bmp.length * shiftingSize >= bitsToWrite;
    }
}
