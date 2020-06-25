package itba.edu.ar.api;


public class NotEnoughSpaceException extends Exception{

    public NotEnoughSpaceException(String m){
        super(m);
    }
}
