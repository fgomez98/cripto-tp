package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class BitMapFileHeader {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapfileheader
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader
    static final int SIZE = 14;

    /*WORD, The file type; must be BM.*/
    short bfType;

    /*DWORD, The size, in bytes, of the bitmap file.*/
    int bfSize;

    /*WORD, Reserved; must be zero.*/
    short bfReserved1;

    /*WORD, Reserved; must be zero.*/
    short bfReserved2;

    /*DWORD, The offset, in bytes, from the beginning of the BITMAPFILEHEADER structure to the bitmap bits.*/
    int bfOffBits;

        /*
            A BITMAPINFO or BITMAPCOREINFO structure immediately follows the BITMAPFILEHEADER structure in the DIB file.
        */

    private void read(ByteBuffer buf) {
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

    BitMapFileHeader(ByteBuffer buf) {
        read(buf);
    }

    BitMapFileHeader(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        read(buf);
    }
}
