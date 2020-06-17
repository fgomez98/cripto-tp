package itba.edu.ar.impl;

import itba.edu.ar.api.CipherMessage;
import itba.edu.ar.api.LSB;
import itba.edu.ar.api.Message;

public class Mirror implements LSB {
    @Override
    public byte[] encrypt(Message message, byte[] bmp) {
        System.arraycopy(message.getFileBytes(), 0, bmp, 0, message.getFileSize());
        return bmp;
    }

    @Override
    public Message decrypt(byte[] bmp) {
        return null;
    }

    @Override
    public byte[] encryptCiphered(CipherMessage cipherMessage, byte[] bmp){return null;}

    @Override
    public CipherMessage decryptCiphered(byte[] bmp){return null;}
}
