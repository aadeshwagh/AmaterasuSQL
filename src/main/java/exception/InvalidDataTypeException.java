package exception;

public class InvalidDataTypeException extends RuntimeException {
    public InvalidDataTypeException(String message){
        super(message);
    }
}
