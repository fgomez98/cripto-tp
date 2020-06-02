package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitMapInfo {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapinfo
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader


    /* Para el tamaño de este bloque se esta tomnado al RGBQUAD no se lo considera */
    static final int SIZE = BitMapInfoHeader.SIZE;

    private final BitMapInfoHeader bmiHeader;

    /*
        this block is mandatory when BitsPerPixel is less than or equal to 8, hence this block is semi-optional.
        When the BitsPerPixel is 16, 24 or 32, the color value of a pixel is calculated from the combination of
        individual Blue, Green and Red values defined by the pixel.
        las imagenes a considerar consisten de 24 bits por píxel

        Creo que este bloque lo podemos no considerar
     */

    private final RGBQuad bmiColors = null;

    private BitMapInfo(ByteBuffer buf) {
        this.bmiHeader = BitMapInfoHeader.read(buf);
        /* opcional, no lo usamos para nuestro tipo de imagenes */
//        this.bmiColors = RGBQuad.read(buf);
    }

    public static BitMapInfo read(ByteBuffer buf) {
        return new BitMapInfo(buf);
    }

    public static BitMapInfo read(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new InvalidBmpFile("The .bmp file contains an invalid BitMapInfoHeader");
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN); // bmp usa little endian;
        return new BitMapInfo(buf);
    }

    public static void write(BitMapInfo header, ByteBuffer buf) {
        BitMapInfoHeader.write(header.bmiHeader, buf);
        /* opcional, no lo usamos para nuestro tipo de imagenes */
        // RGBQuad.write(header.bmiColors, buf);
    }

    public BitMapInfoHeader getBmiHeader() {
        return bmiHeader;
    }

    public RGBQuad getBmiColors() {
        return bmiColors;
    }
}
