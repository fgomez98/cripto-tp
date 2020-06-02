package itba.edu.ar.utils.bmp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bmp {
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

    public static Bmp read(File file) throws IOException {
        return Bmp.read(file.toPath());
    }

    public static Bmp read(String pathToFile) throws IOException {
        return Bmp.read(Paths.get(pathToFile));
    }

    public static Bmp read(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return new Bmp(bytes);
    }

    public static Bmp write(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return new Bmp(bytes);
    }

    private Bmp(byte[] bytes) {
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
}
