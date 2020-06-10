package itba.edu.ar.api;

public interface LSB {

    byte[] encrypt(byte[] message, byte[] bmp);

    byte[] decrypt(byte[] message);

}
