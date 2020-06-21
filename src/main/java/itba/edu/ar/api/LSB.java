package itba.edu.ar.api;

public interface LSB {

    byte[] embedding(Message message, byte[] bmp);

    Message extract(byte[] bmp);

    byte[] embeddingCiphered(CipherMessage cipherMessage, byte[] bmp);

    CipherMessage extractCiphered(byte[] bmp);

}
