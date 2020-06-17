package itba.edu.ar.api;

public interface LSB {

    byte[] encrypt(Message message, byte[] bmp);

    Message decrypt(byte[] bmp);

    byte[] encryptCiphered(CipherMessage cipherMessage, byte[] bmp);

    CipherMessage decryptCiphered(byte[] bmp);

}
