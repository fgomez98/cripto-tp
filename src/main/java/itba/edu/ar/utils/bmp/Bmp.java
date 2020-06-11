package itba.edu.ar.utils.bmp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Bmp {
    /*
        Links copados:
        https://itnext.io/bits-to-bitmaps-a-simple-walkthrough-of-bmp-image-format-765dc6857393
        https://docs.microsoft.com/en-us/windows/win32/gdi/bitmap-storage
    */
    /*
        Ayuda memoria:
        WORD => 2 bytes =(java)=> short
        DWORD => 4 bytes =(java)=> int
     */
    /*
        Consideraciones:
            One thing to remember is that BMP uses the little-endian system to store a number
            (integer or float) when a number is larger than 1-byte.
            Los bmps utilizados deben ser de 24 bits (8bits para rojo, 8bits para verde y 8bits para azul).
            Los bmps utilizados no deben tener compresión.
     */

    private final BitMapFileHeader fileHeader;
    private final BitMapInfo infoHeader;
    private byte[] pixelData;

    private Bmp(byte[] bytes) {
        /*
            Se esta asumiendo que toda la data que entra esta OK y podemos hacer get del buffer a medida que necesito.
            TODO: chequeos de los tamaños para todos los casos, validaciones. Al primer probelam IllegalArgumentException()
        */
        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN); // bmp usa little endian
        this.fileHeader = BitMapFileHeader.read(buf);
        this.infoHeader = BitMapInfo.read(buf);

        if (fileHeader.getBfType() != 19778) {
            throw new InvalidBmpFile("The file type must be BM");
        }
        if (fileHeader.getBfSize() != bytes.length) {
            throw new InvalidBmpFile("The size, in bytes, of the .bmp file does not match the size specified in fileHeader");
        }
        if (fileHeader.getBfSize() - fileHeader.getBfOffBits() != infoHeader.getBmiHeader().getBiSizeImage()) {
            throw new InvalidBmpFile("The header offset specified in fileHeader of the .bmp file is invalid");
        }
        if (infoHeader.getBmiHeader().getBiBitCount() != 24) {
            throw new InvalidBmpFile("The .bmp file must match 24 bits-per-pixel.");
        }
        if (infoHeader.getBmiHeader().getBiCompression() != 0) {
            throw new InvalidBmpFile("The .bmp file should not be compressed");
        }
        // chequeos de que el tamaño de la imagen corresponda con lo especificado en el header
        int pixelDataSize = bytes.length - BitMapFileHeader.SIZE - BitMapInfoHeader.SIZE;
        this.pixelData = new byte[pixelDataSize];
        buf.get(this.pixelData);
    }

    private Bmp(BitMapFileHeader fileHeader, BitMapInfo infoHeader, byte[] pixelData) {
        this.fileHeader = fileHeader;
        this.infoHeader = infoHeader;
        this.pixelData = pixelData;
    }

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

    public static void write(Bmp bmp, File file) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bmp.fileHeader.getBfSize()).order(ByteOrder.LITTLE_ENDIAN);

        BitMapFileHeader.write(bmp.fileHeader, buffer);
        BitMapInfo.write(bmp.infoHeader, buffer);
        buffer.put(bmp.pixelData);

        // Flips this buffer.  The limit is set to the current position and then
        // the position is set to zero.  If the mark is defined then it is discarded.
        buffer.flip();

        FileChannel channel = new FileOutputStream(file).getChannel();

        // Writes a sequence of bytes to this channel from the given buffer.
        channel.write(buffer);
        // close the channel
        channel.close();
    }

    public static void write(Bmp bmp, Path path) throws IOException {
        write(bmp, path.toFile());
    }

    public static void write(Bmp bmp, String pathToFile) throws IOException {
        write(bmp, new File(pathToFile));
    }

    public static Bmp write(BitMapFileHeader fileHeader, BitMapInfo infoHeader, byte[] image, File file) throws IOException {
        Bmp bmp = new Bmp(fileHeader, infoHeader, image);
        write(bmp, file);
        return bmp;
    }

    public static Bmp write(BitMapFileHeader fileHeader, BitMapInfo infoHeader, byte[] image, String pathToFile) throws IOException {
        return write(fileHeader, infoHeader, image, new File(pathToFile));
    }

    public static Bmp write(BitMapFileHeader fileHeader, BitMapInfo infoHeader, byte[] image, Path path) throws IOException {
        return write(fileHeader, infoHeader, image, path.toFile());
    }

    public BitMapFileHeader getFileHeader() {
        return fileHeader;
    }

    public BitMapInfo getInfoHeader() {
        return infoHeader;
    }

    public byte[] getPixelData() {
        return Arrays.copyOf(pixelData, pixelData.length);
    }

    /*
        Cualquier dato util del archivo BMP que este en los headers agregar getter aca para evitar tener que abrir todos
        los campos
     */

    public static void main(String[] args) throws IOException {
        String pathToFile = "src/main/resources/lima.bmp";
        Bmp bmp = Bmp.read(pathToFile);
        Bmp.write(bmp, "src/main/resources/outBMP.bmp");
    }
}
