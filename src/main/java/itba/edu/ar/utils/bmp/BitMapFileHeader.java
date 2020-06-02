package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitMapFileHeader {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapfileheader
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader
    static final int SIZE = 14;

    /*WORD, The file type; must be BM.*/
    private final short bfType;

    /*DWORD, The size, in bytes, of the bitmap file.*/
    private final int bfSize;

    /*WORD, Reserved; must be zero.*/
    private final short bfReserved1;

    /*WORD, Reserved; must be zero.*/
    private final short bfReserved2;

    /*DWORD, The offset, in bytes, from the beginning of the BITMAPFILEHEADER structure to the bitmap bits.*/
    private final int bfOffBits;

    /*
        A BITMAPINFO or BITMAPCOREINFO structure immediately follows the BITMAPFILEHEADER structure in the DIB file.
    */

    private BitMapFileHeader(ByteBuffer buf) {
         /*
            Los archivos que les entregaremos tendrán el tamaño guardado en forma Big Endian.
            The initial order of a byte buffer is always BIG_ENDIAN
        */
        this.bfType = buf.getShort();
        this.bfSize = buf.getInt();
        this.bfReserved1 = buf.getShort();
        this.bfReserved2 = buf.getShort();
        this.bfOffBits = buf.getInt();
    }

    public static BitMapFileHeader read(ByteBuffer buf) {
        return new BitMapFileHeader(buf);
    }

    public static BitMapFileHeader read(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new InvalidBmpFile("The .bmp file contains an invalid BitMapFileHeader");
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN); // bmp usa little endian;
        return new BitMapFileHeader(buf);
    }

    public static void write(BitMapFileHeader header, ByteBuffer buf) {
        buf.putShort(header.bfType);
        buf.putInt(header.bfSize);
        buf.putShort(header.bfReserved1);
        buf.putShort(header.bfReserved2);
        buf.putInt(header.bfOffBits);
    }

    public short getBfType() {
        return bfType;
    }

    public int getBfSize() {
        return bfSize;
    }

    public short getBfReserved1() {
        return bfReserved1;
    }

    public short getBfReserved2() {
        return bfReserved2;
    }

    public int getBfOffBits() {
        return bfOffBits;
    }
}
