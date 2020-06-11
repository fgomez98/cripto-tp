package itba.edu.ar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DividedFile {

    private String path;
    private String extension;
    private byte[] data;
    private long size;


    public DividedFile(String path) throws IOException {
        this.path = path;
        this.extension = path.contains(".") ? path.substring(path.lastIndexOf(".") + 1) : "";
        this.data = Files.readAllBytes(Paths.get(path));
        this.size = this.data.length;
    }

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getData() {
        return data;
    }

    public long getSize() {
        return size;
    }
}
