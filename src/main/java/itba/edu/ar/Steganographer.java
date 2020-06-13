package itba.edu.ar;

import itba.edu.ar.api.LSB;
import itba.edu.ar.utils.bmp.Bmp;
import itba.edu.ar.utils.criptography.Encriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Steganographer {

    private LSB lsb;

    private Encriptor cipher;

    private String encriptionPassword;

    /*
        holder y out son archivos .bmp
    */
    public void embed(String inFilename, String holderFilename, String outFilename) throws Exception {
        Bmp holderBmp = Bmp.read(holderFilename);

        byte[] extensionBytes = toNullTerminatedBytes(getExtension(inFilename));
        byte[] fileBytes = Files.readAllBytes(Paths.get(inFilename));
        byte[] fileSizeBytes = toBigEndianBytes(fileBytes.length);

        // 4 (del tamaño) + longitud archivo + e(extensión)
        // tamaño real || datos archivo || extensión

        // array initialization
        int messageLenght = fileSizeBytes.length + fileBytes.length + extensionBytes.length;
        byte[] message = new byte[messageLenght];

        // array concatenation
        System.arraycopy(fileSizeBytes, 0, message, 0, fileSizeBytes.length);
        System.arraycopy(fileBytes, 0, message, fileSizeBytes.length, fileBytes.length);
        System.arraycopy(extensionBytes, 0, message, fileSizeBytes.length + fileBytes.length, extensionBytes.length);

        if (cipher != null) {
            byte[] encriptedMessageBytes = cipher.simetricEncript(message, encriptionPassword);
            byte[] encriptedMessageSizeBytes = toBigEndianBytes(encriptedMessageBytes.length);

            // Tamaño cifrado || encripcion(tamaño real || datos archivo || extensión)

            // array initialization
            messageLenght = encriptedMessageSizeBytes.length + encriptedMessageBytes.length;
            message = new byte[messageLenght];

            // array concatenation
            System.arraycopy(encriptedMessageSizeBytes, 0, message, 0, encriptedMessageSizeBytes.length);
            System.arraycopy(encriptedMessageBytes, 0, message, encriptedMessageSizeBytes.length, encriptedMessageBytes.length);
        }

        // LSB

        if (holderBmp.getInfoHeader().getBmiHeader().getBiSizeImage() < messageLenght) {
            throw new IllegalArgumentException("Holder .bmp file is to small to hide given message");
        }

        byte[] outBmpPixelData = lsb.encrypt(message, holderBmp.getPixelData());

        // guardamos
        Bmp.write(holderBmp.getFileHeader(), holderBmp.getInfoHeader(), outBmpPixelData, outFilename);
    }

    /*
        holder archivo .bmp con mensaje oculto, outFilename path a donde guardar el mensaje oculto
    */
    public void extract(String holderFilename, String outFilename) throws Exception {
        Bmp holderBmp = Bmp.read(holderFilename);

        // LSB

        byte[] message = lsb.decrypt(holderBmp.getPixelData());

        if (cipher != null) {
            // Tamaño cifrado || encripcion(tamaño real || datos archivo || extensión)
            byte[] encriptedMessageSizeBytes = Arrays.copyOf(message, 4);
            int encriptedMessageSize = fromBigEndianBytes(encriptedMessageSizeBytes);
            byte[] encriptedMessageBytes = Arrays.copyOfRange(message, 4, encriptedMessageSize + 4);

            message = cipher.simetricDecript(encriptedMessageBytes, encriptionPassword);
        }

        // tamaño real || datos archivo || extensión
        byte[] fileSizeBytes = Arrays.copyOf(message, 4);
        int fileSize = fromBigEndianBytes(fileSizeBytes);
        byte[] fileBytes = Arrays.copyOfRange(message, 4, fileSize + 4);
        byte[] extensionBytes = Arrays.copyOfRange(message, 4 + fileSize, message.length);

        String extension = fromNullTerminatedBytes(extensionBytes);

        // guardamos

        System.out.println(new String(message, StandardCharsets.ISO_8859_1));
        File outFile = new File(outFilename + extension);
        OutputStream os = new FileOutputStream(outFile);
        os.write(fileBytes);
        os.close();
    }

    private String getExtension(String filename) {
        return filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
    }

    private byte[] toNullTerminatedBytes(String str) {
        byte[] stringBytes = str.getBytes(StandardCharsets.ISO_8859_1);
        byte[] nullTerminatedBytes = new byte[stringBytes.length + 1];
        System.arraycopy(stringBytes, 0, nullTerminatedBytes, 0, stringBytes.length);
        return nullTerminatedBytes;
    }

    private String fromNullTerminatedBytes(byte[] nullTerminatedBytes) {
        int i = 0;
        while (i < nullTerminatedBytes.length && nullTerminatedBytes[i] != '\0') {
            i++;
        }
        return new String(nullTerminatedBytes, 0, i, StandardCharsets.ISO_8859_1);
    }

    private byte[] toBigEndianBytes(int aInt) {
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.putInt(aInt);
        return b.array();
    }

    private int fromBigEndianBytes(byte[] aInt) {
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.put(aInt);
        b.rewind();
        return b.getInt();
    }

    public static final class SteganographerBuilder {
        private LSB lsb;
        private Encriptor cipher;
        private String encriptionPassword;

        public SteganographerBuilder() {
        }

        public SteganographerBuilder withLsb(LSB lsb) {
            this.lsb = lsb;
            return this;
        }

        public SteganographerBuilder withCipher(Encriptor cipher, String encriptionPassword) {
            this.cipher = cipher;
            this.encriptionPassword = encriptionPassword;
            return this;
        }

        public Steganographer build() {
            return new Steganographer(this);
        }
    }

    private Steganographer(SteganographerBuilder builder) {
        this.lsb = builder.lsb;
        this.cipher = builder.cipher;
        this.encriptionPassword = builder.encriptionPassword;
    }
}
