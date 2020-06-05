package itba.edu.ar.utils.criptography;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class EncriptorTest {

    private final byte[] message = "Mi mensaje oculto".getBytes();
    private final String password = "Titi Henry";
    private List<Encriptor> encriptors;

    @Before
    public void setUp() {
        encriptors = new LinkedList<>();
        for (EncriptionAlgorithm algorithm : EncriptionAlgorithm.values()) {
            for (EncriptionMode mode : EncriptionMode.values()) {
                Encriptor encriptor = Encriptor.from(algorithm, mode);
                encriptors.add(encriptor);
            }
        }
    }

    @Test
    public void testAll() {
        try {
            for (Encriptor enc : encriptors) {
                System.out.println("Usando: " + enc.getTransformation());
                byte[] cipherText = enc.simetricEncript(message, password);
                System.out.println("El mensaje es: " + hexStringFromBytes(message));
                System.out.println("El cifrado es: " + hexStringFromBytes(cipherText));
                byte[] desCipherText = enc.simetricDecript(cipherText, password);
                System.out.println("El descifrado es: " + new String(desCipherText, StandardCharsets.UTF_8) + "\n");
                Assert.assertArrayEquals(message, desCipherText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String hexStringFromBytes(byte[] b) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder hex = new StringBuilder();
        int msb;
        int lsb;
        int j;
        for (j = 0; j < b.length; j++) {
            msb = ((int) b[j] & 0x000000FF) / 16;
            lsb = ((int) b[j] & 0x000000FF) % 16;
            hex.append(hexChars[msb]).append(hexChars[lsb]);
        }
        return (hex.toString());
    }
}