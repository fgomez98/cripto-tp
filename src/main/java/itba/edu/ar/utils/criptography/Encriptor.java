package itba.edu.ar.utils.criptography;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/*
    Me gusta mas el nombre Cipher pero se choca con javax.crypto.Cipher;
*/
public class Encriptor {

    private final EncriptionAlgorithm algorithm;
    private final EncriptionMode mode;
    private final EncriptionPadding padding;

    private Encriptor(EncriptionAlgorithm algorithm, EncriptionMode mode) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = mode.getPadding();
    }

    public static Encriptor from(EncriptionAlgorithm algorithm, EncriptionMode mode) {
        return new Encriptor(algorithm, mode);
    }

    public String getTransformation() {
        return algorithm.getRepresentation() + "/" + mode.getRepresentation() + "/" + padding.getRepresentation();
    }

    private byte[] cipher(int i, byte[] bytes, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(getTransformation());

        /*
            Para la generación de clave a partir de una password, asumir que la función de hash usada es sha256,
            y que no se usa SALT.
        */

        // TODO: Esto solo sirve para el modo ECB que no utiliza un IV, Para todos los demas falla. Implementar generador de (key, iv) a partir de una password

        byte[] key = (password).getBytes(StandardCharsets.UTF_8);
        key = Hashing.sha256(key);
        // achicamos el hash obtenido a la cantidad de bytes necesarios para la clave
        key = Arrays.copyOf(key, algorithm.getKeySize());

        SecretKey secretKey = new SecretKeySpec(key, algorithm.getRepresentation());

        cipher.init(i, secretKey);
        return cipher.doFinal(bytes);
    }

    public byte[] simetricEncript(byte[] bytes, String password) throws Exception {
        return cipher(Cipher.ENCRYPT_MODE, bytes, password);
    }

    public byte[] simetricDecript(byte[] bytes, String password) throws Exception {
        return cipher(Cipher.DECRYPT_MODE, bytes, password);
    }

}
