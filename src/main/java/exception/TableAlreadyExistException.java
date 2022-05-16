package exception;

public class TableAlreadyExistException extends RuntimeException {
    public TableAlreadyExistException(String message){
        super(message);
    }
}
