package itba.edu.ar.utils.bmp;

public class InvalidBmpFile extends RuntimeException {

    public InvalidBmpFile() {
    }

    public InvalidBmpFile(String message) {
        super(message);
    }
}
