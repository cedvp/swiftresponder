package nl.bluetrails.swiftresponder;

public class MissingTagException extends RuntimeException {
    public MissingTagException(String message) {
        super(message); // Call the constructor of the parent Exception class
    }
}