package itba.edu.ar.utils.criptography;

public enum EncriptionPadding {

    /*
       No se agregan bytes de padding.
    */
    NO("NoPadding"),

    /*
        The number of bytes to be padded equals to "8 - numberOfBytes(clearText) mod 8".
        So 1 to 8 bytes will be padded to the clear text data depending on the length of the clear text data.
        All padded bytes have the same value - the number of bytes padded.
    */
    PKCS5("PKCS5Padding");

    private final String representation; // codigo para la transformacion

    EncriptionPadding(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

}
