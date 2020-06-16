package itba.edu.ar.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Message {

    private byte[] fileSize; // en big-endian
    private byte[] fileBytes; // bytes
    private byte[] fileExtension; // null terminated

    public byte[] toByteArray() {
        int messageLenght = fileSize.length + fileBytes.length + fileExtension.length;
        byte[] message = new byte[messageLenght];

        // array concatenation
        System.arraycopy(fileSize, 0, message, 0, fileSize.length);
        System.arraycopy(fileBytes, 0, message, fileSize.length, fileBytes.length);
        System.arraycopy(fileExtension, 0, message, fileSize.length + fileBytes.length, fileExtension.length);

        return message;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public int getFileSize() {
        return MessageUtils.fromBigEndianBytes(fileSize);
    }

    public String getFileExtension() {
        return MessageUtils.fromNullTerminatedBytes(fileExtension);
    }

    public static final class MessageBuilder {

        private byte[] fileSize;
        private byte[] fileBytes;
        private byte[] fileExtension;

        private MessageBuilder() {
        }

        public static MessageBuilder aMessage() {
            return new MessageBuilder();
        }

        public static MessageBuilder aMessage(String filename) throws IOException {
            MessageBuilder mb = new MessageBuilder();
            mb.fileExtension = MessageUtils.toNullTerminatedBytes(MessageUtils.getExtension(filename));
            mb.fileBytes = Files.readAllBytes(Paths.get(filename));
            mb.fileSize = MessageUtils.toBigEndianBytes(mb.fileBytes.length);
            return mb;
        }

        public MessageBuilder withFileSize(byte[] bigEndianFileSize) {
            this.fileSize = bigEndianFileSize;
            return this;
        }

        public MessageBuilder withFileBytes(byte[] fileBytes) {
            this.fileBytes = fileBytes;
            return this;
        }

        public MessageBuilder withFileExtension(byte[] nullTerminatedFileExtension) {
            this.fileExtension = nullTerminatedFileExtension;
            return this;
        }

        public MessageBuilder withFileSize(int fileSize) {
            this.fileSize = MessageUtils.toBigEndianBytes(fileSize);
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.fileExtension = this.fileExtension;
            message.fileSize = this.fileSize;
            message.fileBytes = this.fileBytes;
            return message;
        }
    }
}
