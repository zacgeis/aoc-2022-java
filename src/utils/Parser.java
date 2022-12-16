package utils;

public class Parser {
    public static Parser of(String value) {
        return new Parser(value);
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private Parser(String value) {
        this.value = value;
    }

    public boolean hasNext() {
        return i < value.length();
    }

    public char next() {
        return value.charAt(i++);
    }

    public int nextInt() {
        int start = i;
        while (hasNext()) {
            if (isDigit(peek())) {
                next();
            } else {
                break;
            }
        }
        return Integer.parseInt(value.substring(start, i));
    }

    public void skipNewLines() {
        while (hasNext()) {
            if (peek() == '\n') {
                next();
            } else {
                break;
            }
        }
    }

    public char peek() {
        return value.charAt(i);
    }

    public void match(char expected) {
        char actual = next();
        if (actual != expected) {
            throw new RuntimeException("Expected " + expected + ", but found " + actual);
        }
    }

    int i = 0;
    String value;
}
