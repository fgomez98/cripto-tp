package itba.edu.ar.api;

public interface LSB {

    byte[] embedding(Message message, byte[] bmp) throws NotEnoughSpaceException;

    Message extract(byte[] bmp) throws WrongLSBStegException;

    byte[] embeddingCiphered(CipherMessage cipherMessage, byte[] bmp) throws NotEnoughSpaceException;

    CipherMessage extractCiphered(byte[] bmp) throws WrongLSBStegException;

}
