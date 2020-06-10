package itba.edu.ar;

import itba.edu.ar.api.LSB;
import itba.edu.ar.utils.bmp.Bmp;
import itba.edu.ar.utils.criptography.Encriptor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Steganographer {

    private LSB lsb;

    private Encriptor cipher;

    private String encriptionPassword;

    /*
        holder y out son archivos .bmp
    */
    public void embed(String inFilename, String holderFilename, String outFilename) throws Exception {
        Bmp holderBmp = Bmp.read(holderFilename);

        byte[] extensionBytes = getExtension(inFilename).getBytes();
        byte[] fileBytes = Files.readAllBytes(Paths.get(inFilename));
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.putInt(fileBytes.length);
        byte[] fileSizeBytes = b.array();

        // 4 (del tamaño) + longitud archivo + e(extensión)
        int messageLenght = 4 + fileBytes.length + extensionBytes.length;
        byte[] message = new byte[messageLenght];

        System.arraycopy(fileSizeBytes, 0, message, 0, 4);
        System.arraycopy(fileBytes, 0, message, 4, fileBytes.length);
        System.arraycopy(extensionBytes, 0, message, fileBytes.length + 4, extensionBytes.length);

        if (cipher != null) {
            message = cipher.simetricEncript(message, encriptionPassword);
        }

        // LSB
        byte[] outBmpPixelData = lsb.encrypt(message, holderBmp.getPixelData());

        // guardamos
        Bmp.write(holderBmp.getFileHeader(), holderBmp.getInfoHeader(), outBmpPixelData, outFilename);
    }

    public void extract(String holderFilename, String outFilename) throws IOException {
        Bmp holderBmp = Bmp.read(holderFilename);
        lsb.decrypt(holderBmp.getPixelData());

    }

    private String getExtension(String filename) {
        return filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
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
