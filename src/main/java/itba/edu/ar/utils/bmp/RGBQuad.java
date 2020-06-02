package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class RGBQuad {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-rgbquad
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader

    static final int SIZE = 4;

    /* The intensity of blue in the color. */
    private final byte rgbBlue;

    /* The intensity of green in the color. */
    private final byte rgbGreen;

    /* The intensity of red in the color. */
    private final byte rgbRed;

    /* This member is reserved and must be zero. */
    private final byte rgbReserved;

    private RGBQuad(ByteBuffer buf) {
        this.rgbBlue = buf.get();
        this.rgbGreen = buf.get();
        this.rgbRed = buf.get();
        this.rgbReserved = buf.get();
    }

    public static RGBQuad read(ByteBuffer buf) {
        return new RGBQuad(buf);
    }

    public static RGBQuad read(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        return new RGBQuad(buf);
    }

    public static void write(RGBQuad header, ByteBuffer buf) {
        buf.put(header.rgbBlue);
        buf.put(header.rgbGreen);
        buf.put(header.rgbRed);
        buf.put(header.rgbReserved);
    }

    public byte getRgbBlue() {
        return rgbBlue;
    }

    public byte getRgbGreen() {
        return rgbGreen;
    }

    public byte getRgbRed() {
        return rgbRed;
    }

    public byte getRgbReserved() {
        return rgbReserved;
    }
}
