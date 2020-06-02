package itba.edu.ar.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BMP {
    /*
        Links copados:
        https://itnext.io/bits-to-bitmaps-a-simple-walkthrough-of-bmp-image-format-765dc6857393
        https://docs.microsoft.com/en-us/windows/win32/gdi/bitmap-storage
    */
    /*
        Ayuda memoria:
        WORD => 2 bytes =(java)=> short
        WORD => 4 bytes =(java)=> int
     */

    BitMapFileHeader fileHeader;
    BitMapInfo infoHeader;
    byte[] pixelData;

    public static BMP from(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new BMP(bytes);
    }

    public static BMP from(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return new BMP(bytes);
    }

    public static BMP from(String pathToFile) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(pathToFile));
        return new BMP(bytes);
    }

    private BMP(byte[] bytes) {
        /*
            Se esta asumiendo que toda la data que entra esta OK y podemos hacer get del buffer a medida que necesito.
            TODO: chequeos de los tamaños para todos los casos, validaciones. Al primer probelam IllegalArgumentException()
        */
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        this.fileHeader = new BitMapFileHeader(buf);
        this.infoHeader = new BitMapInfo(buf);
        /*
            Estamos guardando lo que queda en el ByteBuffer en pixelData.
            Se podria calcular de manera pre fijada ya que sabemos los tamaños de los headers
            Se podria calcular usando las variables offset y size en los headers
        */
        buf.get(this.pixelData);
    }

    public static class BitMapFileHeader {
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

        private void fill(ByteBuffer buf) {
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

        private BitMapFileHeader(ByteBuffer buf) {
            fill(buf);
        }

        private BitMapFileHeader(byte[] bytes) {
            if (bytes.length != SIZE) {
                throw new IllegalArgumentException();
            }
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            fill(buf);
        }
    }

    public static class BitMapInfo {
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

        private BitMapInfo(ByteBuffer buf) {
            this.bmiHeader = new BitMapInfoHeader(buf);
            this.bmiColors = new RGBQuad(buf);
        }

        private BitMapInfo(byte[] bytes) {
            if (bytes.length != SIZE) {
                throw new IllegalArgumentException();
            }
            this.bmiHeader = new BitMapInfoHeader(bytes);
            this.bmiColors = new RGBQuad(bytes);
        }
    }

    public static class BitMapInfoHeader {
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

        private void fill(ByteBuffer buf) {
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

        private BitMapInfoHeader(ByteBuffer buf) {
            fill(buf);
        }

        private BitMapInfoHeader(byte[] bytes) {
            if (bytes.length != SIZE) {
                throw new IllegalArgumentException();
            }
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            fill(buf);
        }
    }

    public static class RGBQuad {
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

        private void fill(ByteBuffer buf) {
            this.rgbBlue = buf.get();
            this.rgbGreen = buf.get();
            this.rgbRed = buf.get();
            this.rgbReserved = buf.get();
        }

        private RGBQuad(byte[] bytes) {
            if (bytes.length != SIZE) {
                throw new IllegalArgumentException();
            }
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            fill(buf);
        }

        private RGBQuad(ByteBuffer buf) {
            fill(buf);
        }
    }
}
