package itba.edu.ar.api;

import com.google.common.primitives.Bytes;
import itba.edu.ar.utils.criptography.RC4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class LSBI implements LSB {

    private static final int SIZE_LENGTH = 32;
    private int hope;
    private RC4 rc4;
    private byte[] rc4Password;
    private int extractIndex;

    /**
     * parameter hope has to be in the list [2, 4, 8, 12, 16, 24, 32]
     *
     * @param rc4Password Importante: Como los primeros 6 bytes de la portadora se utilizarán como clave para RC4, esos bytes no se usarán para LSB
     */
    public LSBI(byte[] rc4Password) {
        this.rc4Password = rc4Password;
        this.rc4 = new RC4(rc4Password);
        this.hope = getHope(rc4Password[0]);
    }

    /**
     * parameter hope has to be in the list [2, 4, 8, 12, 16, 24, 32]
     *
     * @param hope hope power of 2 value
     */
    public LSBI(int hope) {
        this.hope = hope;

        byte[] rc4Password = new byte[6];
        Random rd = new Random();
        rd.nextBytes(rc4Password);
        rc4Password[0] = (byte) (hope & 0xFF);
        this.rc4 = new RC4(rc4Password);
        this.rc4Password = rc4Password;
    }

    public LSBI() {
        Random rd = new Random();
        this.rc4Password = new byte[6];
        rd.nextBytes(this.rc4Password);
        this.rc4 = new RC4(this.rc4Password);
        this.hope = getHope(this.rc4Password[0]);
    }

    public void setHope(int hope) {
        this.hope = hope;

        byte[] rc4Password = new byte[6];
        Random rd = new Random();
        rd.nextBytes(rc4Password);
        rc4Password[0] = (byte) (hope & 0xFF);
        this.rc4 = new RC4(rc4Password);
    }

    public void setRc4Password(byte[] rc4Password) {
        this.rc4Password = rc4Password;
        this.rc4 = new RC4(rc4Password);
        this.hope = getHope(rc4Password[0]);
    }

    /**
     * Orden de los pasos:
     * 1) preparo msj = Tamaño real || datos archivo || extensión
     * 2) RC4K(msj)
     * 3) oculto RC4K(msj) en la foto
     *
     * @param message msj
     * @param bmp     foto para esconder el msj
     * @return foto estanografada
     */
    @Override
    public byte[] embedding(Message message, byte[] bmp) throws NotEnoughSpaceException {

        byte[] msg = message.toByteArray();
        return embedding(msg, bmp);

    }

    private byte[] embedding(byte[] msg, byte[] bmp) throws NotEnoughSpaceException {
        byte[] rc4Encrypt = rc4.encrypt(msg);
        int maxSize = bmp.length * 8 - 6;
        if (rc4Encrypt.length > maxSize) {
            throw new NotEnoughSpaceException("BMP file is too small for the message. " +
                    "Use a message smaller than " + maxSize + " bytes");
        }
        byte[] ret = bmp.clone();
        byte[] password = this.rc4Password;
        System.arraycopy(password, 0, ret, 0, password.length);
        int j = 0;
        int bmpI = 6;
        for (int i = 0; i < rc4Encrypt.length * 8; i++) { //asi paso por todos los length*8 bits
            if (bmpI > ret.length) {
                j += 1;
                bmpI = 6 + j; // pues siempre salto los 6 primeros bytes
            }
            int bit = getBitValueFromArray(rc4Encrypt, i);

            setBitValue(ret, bmpI, bit);
            bmpI += this.hope;
        }

        return ret;

    }

    /**
     * 1) busco RC4K(msj) en la foto
     * 2) RC4K(msj)
     * 3) preparo msj = Tamaño real || datos archivo || extensión
     *
     * @param bmp foto
     * @return mesaje
     */
    @Override
    public Message extract(byte[] bmp) throws WrongLSBStegException {
        if (bmp == null) {
            throw new WrongLSBStegException("BMP is empty");
        }
        this.extractIndex = 6;

        this.hope = getHope(bmp[0]);
        this.rc4Password = Arrays.copyOfRange(bmp, 0, 6);
        this.rc4 = new RC4(this.rc4Password);

        byte[] size = decrypt(SIZE_LENGTH, bmp);
        byte[] decSize = this.rc4.decrypt(size);

        int msgSize = MessageUtils.fromBigEndianBytes(decSize);

        if (msgSize < 0) {
            throw new WrongLSBStegException("Wrong LSB");
        }

        byte[] rc4Encrypted = decrypt(8 * msgSize, bmp);
        byte[] message = this.rc4.decrypt(rc4Encrypted);

        byte[] fileExtension = decryptExtension(bmp);

        return new Message.MessageBuilder()
                .withFileBytes(message)
                .withFileSize(size)
                .withFileExtension(fileExtension)
                .build();
    }

    private byte[] decryptExtension(byte[] bmp) {

        List<Byte> decryption = new ArrayList<>();
        int j = 0;

        int counterBit = 0;
        byte b = 0;
        boolean flag = true;

        while (flag) {
            if (this.extractIndex > bmp.length) {
                j++;
                this.extractIndex = 6 + j;
            }

            b = getLSBit(b, bmp, counterBit % 8);
            counterBit++;

            if (counterBit % 8 == 0) { //entonces junte 1 byte
                byte a = this.rc4.decrypt(new byte[]{b})[0];
                if (a == 0) {
                    flag = false;
                }
                decryption.add(a);
                b = 0;
                counterBit = 0;
            }

            this.extractIndex += this.hope;
        }
        return Bytes.toArray(decryption);
    }

    /**
     * funcion privada asi al menos puedo encontrar el tamaño del archivo
     *
     * @param size tamaño de la extracción
     * @param bmp  foto
     * @return el array de bytes en la estanografia
     */
    private byte[] decrypt(int size, byte[] bmp) {

        byte[] decryption = new byte[size / 8];
        int j = 0;
        int decIndex = 0;

        int counterBit = 0;
        byte b = 0;

        for (int i = 0; i < size; i++) {
            if (this.extractIndex > bmp.length) {
                j++;
                this.extractIndex = 6 + j;
            }

            b = getLSBit(b, bmp, counterBit % 8);
            counterBit++;

            if (counterBit % 8 == 0) { //entonces junte 1 byte
                decryption[decIndex] = b;
                b = 0;
                counterBit = 0;
                decIndex++;
            }

            this.extractIndex += this.hope;
        }


        return decryption;
    }


    private byte getLSBit(byte b, byte[] bmp, int bitPosition) {
        byte bmpByte = bmp[this.extractIndex];
        return (byte) ((b << 1) | (bmpByte & 1)); //todo verificar
    }

    @Override
    public byte[] embeddingCiphered(CipherMessage cipherMessage, byte[] bmp) throws NotEnoughSpaceException {
        return embedding(cipherMessage.toByteArray(), bmp);
    }

    @Override
    public CipherMessage extractCiphered(byte[] bmp) throws WrongLSBStegException {

        if (bmp == null) {
            throw new WrongLSBStegException("BMP is empty");
        }

        this.extractIndex = 6;

        this.hope = getHope(bmp[0]);
        this.rc4Password = Arrays.copyOfRange(bmp, 0, 6);
        this.rc4 = new RC4(this.rc4Password);

        byte[] size = decrypt(SIZE_LENGTH, bmp);
        byte[] decSize = this.rc4.decrypt(size);

        int msgSize = MessageUtils.fromBigEndianBytes(decSize);

        if (msgSize < 0) {
            throw new WrongLSBStegException("Wrong LSB. Size read is negative");
        }

        byte[] encMessage = decrypt(msgSize, bmp);
        byte[] messageParts = this.rc4.decrypt(encMessage);

        return new CipherMessage.CipherMessageBuilder()
                .withCipherSize(msgSize)
                .withCipherBytes(messageParts).build();

    }

    @Override
    public int getMaxSize(byte[] bmp) {
        return (bmp.length / 8) - 6;
    }

    private int getHope(byte firstSigByte) {
        if (firstSigByte == 0) {
            return 256;
        }
        int msv = firstSigByte & 0xff;
        msv |= msv >> 1;
        msv |= msv >> 2;
        msv |= msv >> 4;
        msv |= msv >> 8;
        msv |= msv >> 16;
        msv = msv + 1;
        return msv >> 1;
    }

    //todo: mandar a un utils si lo voy a re utilizar aca
    private void setBitValue(byte[] arr, int pos, int value) {
        if (value == 1)
            arr[pos] |= 1;
        else
            arr[pos] &= 254;
    }

    private int getBitValue(byte b, int position) {
        return (b >> position & 1);
    }

    private int getBitValueFromArray(byte[] arr, int bit) {
        int index = bit / 8;
        int bitPosition = 7 - (bit % 8);

        return getBitValue(arr[index], bitPosition);
    }

}
