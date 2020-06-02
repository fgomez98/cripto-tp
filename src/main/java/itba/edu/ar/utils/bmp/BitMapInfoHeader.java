package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class BitMapInfoHeader {
    // Para mas info: https://docs.microsoft.com/en-us/previous-versions/dd183376(v=vs.85)
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader

    static final int SIZE = 40;

    /* The number of bytes required by the structure. */
    private final int biSize;

    /* The width of the bitmap, in pixels. */
    private final long biWidth;

    /* The height of the bitmap, in pixels. */
    private final long biHeight;

    /* The number of planes for the target device. This value must be set to 1. */
    private final short biPlanes;

    /* The number of bits-per-pixel.  */
    private final short biBitCount;

    /* biCompression */
    private final int biCompression;

    /* The size, in bytes, of the image. This may be set to zero for BI_RGB bitmaps. */
    private final int biSizeImage;

    /*
        The horizontal resolution, in pixels-per-meter, of the target device for the bitmap.
        An application can use this value to select a bitmap from a resource group that best matches the
        characteristics of the current device.
    */
    private final long biXPelsPerMeter;

    /* The vertical resolution, in pixels-per-meter, of the target device for the bitmap. */
    private final long biYPelsPerMeter;

    /* The number of color indexes in the color table that are actually used by the bitmap. */
    private final int biClrUsed;

    /*
        The number of color indexes that are required for displaying the bitmap.
        If this value is zero, all colors are required.
    */
    private final int biClrImportant;

    private BitMapInfoHeader(ByteBuffer buf) {
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

    public static BitMapInfoHeader read(ByteBuffer buf) {
        return new BitMapInfoHeader(buf);
    }

    public static BitMapInfoHeader read(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        return new BitMapInfoHeader(buf);
    }

    public static void write(BitMapInfoHeader header, ByteBuffer buf) {
        buf.putInt(header.biSize);
        buf.putLong(header.biWidth);
        buf.putLong(header.biHeight);
        buf.putShort(header.biPlanes);
        buf.putShort(header.biBitCount);
        buf.putInt(header.biCompression);
        buf.putInt(header.biSizeImage);
        buf.putLong(header.biXPelsPerMeter);
        buf.putLong(header.biYPelsPerMeter);
        buf.putInt(header.biClrUsed);
        buf.putInt(header.biClrImportant);
    }

    public int getBiSize() {
        return biSize;
    }

    public long getBiWidth() {
        return biWidth;
    }

    public long getBiHeight() {
        return biHeight;
    }

    public short getBiPlanes() {
        return biPlanes;
    }

    public short getBiBitCount() {
        return biBitCount;
    }

    public int getBiCompression() {
        return biCompression;
    }

    public int getBiSizeImage() {
        return biSizeImage;
    }

    public long getBiXPelsPerMeter() {
        return biXPelsPerMeter;
    }

    public long getBiYPelsPerMeter() {
        return biYPelsPerMeter;
    }

    public int getBiClrUsed() {
        return biClrUsed;
    }

    public int getBiClrImportant() {
        return biClrImportant;
    }
}
