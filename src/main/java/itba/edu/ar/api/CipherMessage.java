package itba.edu.ar.api;

import itba.edu.ar.utils.criptography.Encriptor;

import java.util.Arrays;

public class CipherMessage {

    private byte[] cipherSize; // en big-endian
    private byte[] cipherBytes; // bytes

    public byte[] getCipherSizeBytes() {
        return cipherSize;
    }

    public byte[] getCipherBytes() {
        return cipherBytes;
    }

    public int getCipherSize() {
        return MessageUtils.fromBigEndianBytes(cipherSize);
    }

    public byte[] toByteArray() {
        int messageLenght = cipherSize.length + cipherBytes.length;
        byte[] message = new byte[messageLenght];

        // array concatenation
        System.arraycopy(cipherSize, 0, message, 0, cipherSize.length);
        System.arraycopy(cipherBytes, 0, message, cipherSize.length, cipherBytes.length);
        return message;
    }

    public Message getMessage(Encriptor cipher, String password) throws Exception {
        byte[] cipheredMessage = cipher.simetricDecript(cipherBytes, password);

        byte[] messageSizeBytes = Arrays.copyOf(cipheredMessage, 4);
        int messageSize = MessageUtils.fromBigEndianBytes(messageSizeBytes);
        byte[] messageBytes = Arrays.copyOfRange(cipheredMessage, 4, messageSize + 4);
        byte[] messageExtensionBytes = Arrays.copyOfRange(cipheredMessage, messageSize + 4, cipheredMessage.length);

        return Message.MessageBuilder.aMessage()
                .withFileSize(messageSize)
                .withFileBytes(messageBytes)
                .withFileExtension(messageExtensionBytes)
                .build();
    }

    public static final class CipherMessageBuilder {
        private byte[] cipherSize; // en big-endian
        private byte[] cipherBytes; // bytes

        public CipherMessageBuilder() {
        }

        public static CipherMessageBuilder aCipherMessage() {
            return new CipherMessageBuilder();
        }

        public static CipherMessageBuilder aCipherMessage(Message message, Encriptor cipher, String password) throws Exception {
            CipherMessageBuilder cm = aCipherMessage();
            cm.cipherBytes = cipher.simetricEncript(message.toByteArray(), password);
            cm.cipherSize = MessageUtils.toBigEndianBytes(cm.cipherBytes.length);
            return cm;
        }

        public CipherMessageBuilder withCipherSize(byte[] bigEndianCipherSize) {
            this.cipherSize = bigEndianCipherSize;
            return this;
        }

        public CipherMessageBuilder withCipherSize(int cipherSize) {
            this.cipherSize = MessageUtils.toBigEndianBytes(cipherSize);
            return this;
        }

        public CipherMessageBuilder withCipherBytes(byte[] cipherBytes) {
            this.cipherBytes = cipherBytes;
            return this;
        }

        public CipherMessage build() {
            CipherMessage cipherMessage = new CipherMessage();
            cipherMessage.cipherSize = this.cipherSize;
            cipherMessage.cipherBytes = this.cipherBytes;
            return cipherMessage;
        }
    }
}
