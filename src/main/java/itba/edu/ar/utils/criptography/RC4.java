package itba.edu.ar.utils.criptography;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class RC4 {

    private byte[] key;
    private final int sSize = 256;
    private byte[] S;

    public RC4(byte[] key) {
        this.key = key;
        this.S = stateVectorInit();
    }

    public byte[] encrypt(byte[] message) {
        return generateCypherFlux(message,message.length,false);
    }

    public byte[] decrypt(byte[] message) {
        return this.encrypt(message);
    }

    public byte[] encrypt(byte[] message, boolean restart) {
        return generateCypherFlux(message,message.length,restart);
    }

    public byte[] decrypt(byte[] message, boolean restart) {
        return this.encrypt(message,restart);
    }

    private byte[] stateVectorInit() {
        byte[] S = new byte[sSize];
        for (int i = 0; i < sSize; i++) {
            S[i] = (byte)i;
        }
        int j = 0;
        for (int i = 0; i < sSize; i++) {
            j = (j + S[i] + key[i % key.length]) & 0xff;
            byte aux = S[i];
            S[i] = S[j];
            S[j] = aux;
        }
        return S;
    }

    private byte[] generateCypherFlux(byte[] message, int generatingOutput, boolean restart) {
        if(restart){
            // con esto reinicio la matriz y puedo en el mismo codigo encriptar y desencriptar
            this.S = stateVectorInit();
        }
        int i = 0;
        int j = 0;
        byte[] retMessage = new byte[generatingOutput];
        byte[] K = new byte[generatingOutput];
        for (int k = 0; k < generatingOutput; k++) {
            i = (i + 1) & 0xff;
            j = (j + S[i]) & 0xff;
            byte aux = S[i];
            S[i] = S[j];
            S[j] = aux;
            K[k] = S[(S[i] + S[j]) & 0xff];
            retMessage[k] = (byte)(0xff & (K[k] ^ message[k]));
        }
        return retMessage;
    }

    public static void main(String[] args) {
        String testString = "The quick brown fox jumps over the lazy dog.";
        String encrypted = "2ac2fecdd8fbb84638e3a4820eb205cc8e29c28b9d5d6b2ef974f311964971c90e8b9ca16467ef2dc6fc3520";

        int[] key = {63, 72, 79, 70, 74, 69, 69};
        ByteBuffer bb = ByteBuffer.allocate(key.length * 4);
        IntBuffer ib = bb.asIntBuffer();
        ib.put(key);
        byte[] key2 = bb.array();
        RC4 arc4 = new RC4(key2);


        byte[] ans = arc4.encrypt(testString.getBytes(),true);
        for (byte an : ans) {
            System.out.println(an);
        }
        String back = new String(arc4.decrypt(ans,true));
        System.out.println(back);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
