package itba.edu.ar.utils.bmp;

import java.nio.ByteBuffer;

public class BitMapInfo {
    // Para mas info: https://docs.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapinfo
    // El orden de cada campo esta dado de la misma forma que en el BitMapFileHeader


    /* Para el tamaño de este bloque se esta tomnado al RGBQUAD no se lo considera */
    static final int SIZE = BitMapInfoHeader.SIZE;

    BitMapInfoHeader bmiHeader;

    /*
        this block is mandatory when BitsPerPixel is less than or equal to 8, hence this block is semi-optional.
        When the BitsPerPixel is 16, 24 or 32, the color value of a pixel is calculated from the combination of
        individual Blue, Green and Red values defined by the pixel.
        las imagenes a considerar consisten de 24 bits por píxel

        Creo que este bloque lo podemos no considerar
     */
    RGBQuad bmiColors;

    BitMapInfo(ByteBuffer buf) {
        this.bmiHeader = new BitMapInfoHeader(buf);
        this.bmiColors = new RGBQuad(buf);
    }

    BitMapInfo(byte[] bytes) {
        if (bytes.length != SIZE) {
            throw new IllegalArgumentException();
        }
        this.bmiHeader = new BitMapInfoHeader(bytes);
        this.bmiColors = new RGBQuad(bytes);
    }
}
