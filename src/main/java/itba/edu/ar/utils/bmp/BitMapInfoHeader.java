package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class BitMapInfoHeader {
    // Para mas info: https://docs.microsoft.com/en-us/previous-versions/dd183376(v=vs.85)
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader

    static final int SIZE = 40;

    /* The number of bytes required by the structure. */
    int biSize;

    /* The width of the bitmap, in pixels. */
    long biWidth;

    /* The height of the bitmap, in pixels. */
    long biHeight;

    /* The number of planes for the target device. This value must be set to 1. */
    short biPlanes;

    /* The number of bits-per-pixel.  */
    short biBitCount;

    /* biCompression */
    int biCompression;

    /* The size, in bytes, of the image. This may be set to zero for BI_RGB bitmaps. */
    int biSizeImage;

    /*
        The horizontal resolution, in pixels-per-meter, of the target device for the bitmap.
        An application can use this value to select a bitmap from a resource group that best matches the
        characteristics of the current device.
    */
    long biXPelsPerMeter;

    /* The vertical resolution, in pixels-per-meter, of the target device for the bitmap. */
    long biYPelsPerMeter;

    /* The number of color indexes in the color table that are actually used by the bitmap. */
    int biClrUsed;

    /*
        The number of color indexes that are required for displaying the bitmap.
        If this value is zero, all colors are required.
    */
    int biClrImportant;

    private void read(ByteBuffer buf) {
        this.biSize = buf.getInt();
        this.biWidth = buf.getLong();
        this.biHeight = buf.getLong();
        this.biPlanes = buf.getShort();
        this.biBitCount = buf.getShort();
        this.biCompression = buf.getInt();
        this.biSizeImage = buf.getInt();
        this.biXPelsPerMeter = buf.getLong();
        this.biYPelsPerMeter = buf.getLong();
        this.biClrUsed = buf.getInt();
        this.biClrImportant = buf.getInt();
    }

    BitMapInfoHeader(ByteBuffer buf) {
        read(buf);
    }

    BitMapInfoHeader(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        read(buf);
    }
}
