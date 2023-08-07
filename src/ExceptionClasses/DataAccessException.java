package ExceptionClasses;

import java.util.logging.Logger;

public class DataAccessException extends Exception {

    public DataAccessException(String message) {
        System.out.println(message);
        Logger.global.info(message);
    }
}
