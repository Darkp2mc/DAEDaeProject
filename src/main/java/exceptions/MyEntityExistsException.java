package exceptions;

public class MyEntityExistsException extends Exception {

    public MyEntityExistsException(String message) {
        super(message);
    }
}
