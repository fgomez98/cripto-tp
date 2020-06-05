package itba.edu.ar.utils.criptography;

public enum EncriptionAlgorithm {

    /*
        AES
        Key sizes	128, 192 or 256 bits
        Block sizes	128 bits

        DES
        Key sizes‎: ‎56 bits (+8 parity bits)
        Block sizes‎: ‎64 bits
     */

    AES128(128 / 8, 128 / 8, "AES"),
    AES192(192 / 8, 128 / 8, "AES"),
    AES256(256 / 8, 128 / 8, "AES"),
    DES((56 + 8) / 8, 64 / 8, "DES");

    private final int keySize; // tamaño de clave a usar para el algoritmo en bytes
    private final int blockSize; // tamaño de bloque a usar para el algoritmo en bytes
    private final String representation; // codigo para la transformacion

    EncriptionAlgorithm(int keySize, int blockSize, String representation) {
        this.keySize = keySize;
        this.blockSize = blockSize;
        this.representation = representation;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public String getRepresentation() {
        return representation;
    }
}
