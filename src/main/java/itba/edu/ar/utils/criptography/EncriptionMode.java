package itba.edu.ar.utils.criptography;

public enum EncriptionMode {

    // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher

    ECB("ECB", EncriptionPadding.PKCS5),

    CFB("CFB8", EncriptionPadding.NO),
    OFB("OFB", EncriptionPadding.NO),
    CBC("CBC", EncriptionPadding.PKCS5);

    /*
        codigo para la transformacion
    */
    private final String representation;
    /*
        Tipo de padding a usar segun el modo:
        -   Si el archivo a ocultar requiriera padding usar el padding por defecto de openssl que es PKCS5.
        -   Para modos de feedback considerar una cantidad de 8 bits si es CFB y 128 si es OFB.
                "__/CFB8/NoPadding" (corresponde a 8 bits de feedback)
                “__/OFB/NoPadding” (corresponde a 128 bits de feedback)
    */
    private final EncriptionPadding padding;

    EncriptionMode(String representation, EncriptionPadding padding) {
        this.representation = representation;
        this.padding = padding;
    }

    public String getRepresentation() {
        return representation;
    }

    public EncriptionPadding getPadding() {
        return padding;
    }
}
