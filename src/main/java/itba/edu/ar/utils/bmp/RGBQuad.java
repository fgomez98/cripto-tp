package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class RGBQuad {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-rgbquad
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader

    static final int SIZE = 4;

    /* The intensity of blue in the color. */
    byte rgbBlue;

    /* The intensity of green in the color. */
    byte rgbGreen;

    /* The intensity of red in the color. */
    byte rgbRed;

    /* This member is reserved and must be zero. */
    byte rgbReserved;

    private void read(ByteBuffer buf) {
        this.rgbBlue = buf.get();
        this.rgbGreen = buf.get();
        this.rgbRed = buf.get();
        this.rgbReserved = buf.get();
    }

    RGBQuad(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        read(buf);
    }

    RGBQuad(ByteBuffer buf) {
        read(buf);
    }
}
