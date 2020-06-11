package itba.edu.ar.impl;

import itba.edu.ar.api.LSB;

public class Mirror implements LSB {
    @Override
    public byte[] encrypt(byte[] message, byte[] bmp) {
        System.arraycopy(message, 0, bmp, 0, message.length);
        return bmp;
    }

    @Override
    public byte[] decrypt(byte[] bmp) {
        return bmp;
    }
}
