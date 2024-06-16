package utils.com.gabriel.orders.core;

import java.util.Random;

public class UtilsMock {

    public static String generateRandomString() {
        return new Random().ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(10)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
