package itba.edu.ar;

import itba.edu.ar.api.CipherMessage;
import itba.edu.ar.api.LSB;
import itba.edu.ar.api.Message;
import itba.edu.ar.utils.bmp.Bmp;
import itba.edu.ar.utils.criptography.Encriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Steganographer {

    private LSB lsb;

    private Encriptor cipher;

    private String encriptionPassword;

    /*
        holder y out son archivos .bmp
    */
    public void embed(String inFilename, String holderFilename, String outFilename) throws Exception {
        Bmp holderBmp = Bmp.read(holderFilename);

        Message message = Message.MessageBuilder.aMessage(inFilename).build();

        byte[] outBmpPixelData;

        if (cipher != null) {
            CipherMessage cipherMessage = CipherMessage.CipherMessageBuilder.aCipherMessage(message, cipher, encriptionPassword).build();
            outBmpPixelData = lsb.encryptCiphered(cipherMessage, holderBmp.getPixelData());
        } else {
            outBmpPixelData = lsb.encrypt(message, holderBmp.getPixelData());
        }

        Bmp.write(holderBmp.getFileHeader(), holderBmp.getInfoHeader(), outBmpPixelData, outFilename);
    }

    /*
        holder archivo .bmp con mensaje oculto, outFilename path a donde guardar el mensaje oculto
    */
    public void extract(String holderFilename, String outFilename) throws Exception {
        Bmp holderBmp = Bmp.read(holderFilename);

        Message message;

        if (cipher != null) {
            CipherMessage cipherMessage = lsb.decryptCiphered(holderBmp.getPixelData());
            message = cipherMessage.getMessage(cipher, encriptionPassword);
        } else {
            message = lsb.decrypt(holderBmp.getPixelData());
        }

        File outFile = new File(outFilename + message.getFileExtension());
        OutputStream os = new FileOutputStream(outFile);
        os.write(message.getFileBytes());
        os.close();
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
