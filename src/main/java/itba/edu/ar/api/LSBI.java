package itba.edu.ar.api;

public class LSBI implements LSB{
    @Override
    public byte[] encrypt(Message message, byte[] bmp) {
        return new byte[0];
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
