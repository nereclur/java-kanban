package exceptions;

import java.io.IOException;

public class FileWriterSaveException extends RuntimeException {
    public FileWriterSaveException(String message, IOException e) {
    }
}
