package itba.edu.ar.api;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class MessageUtils {

    public static byte[] toBigEndianBytes(int aInt) {
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.putInt(aInt);
        return b.array();
    }

    public static int fromBigEndianBytes(byte[] aInt) {
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.put(aInt);
        b.rewind();
        return b.getInt();
    }

    public static String getExtension(String filename) {
        return filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
    }

    public static byte[] toNullTerminatedBytes(String str) {
        byte[] stringBytes = str.getBytes(StandardCharsets.ISO_8859_1);
        byte[] nullTerminatedBytes = new byte[stringBytes.length + 1];
        System.arraycopy(stringBytes, 0, nullTerminatedBytes, 0, stringBytes.length);
        return nullTerminatedBytes;
    }

    public static String fromNullTerminatedBytes(byte[] nullTerminatedBytes) {
        int i = 0;
        while (i < nullTerminatedBytes.length && nullTerminatedBytes[i] != '\0') {
            i++;
        }
        return new String(nullTerminatedBytes, 0, i, StandardCharsets.ISO_8859_1);
    }
}
