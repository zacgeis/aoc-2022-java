package utils;

// TODO: ga code action, gc comment, gf & gF format, gh hover, ctrl-p finder. something to open find in file too.

public class Parser {
    public static Parser of(String value) {
        return new Parser(value);
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isNumeric(char c) {
        return isDigit(c) || c == '-';
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
            if (isNumeric(peek())) {
                next();
            } else {
                break;
            }
        }
        return Integer.parseInt(value.substring(start, i));
    }

    public String nextWord() {
        int start = i;
        while (hasNext()) {
            char c = peek();
            if (c == '\n' || c == ' ') {
                break;
            } else {
                next();
            }
        }
        return value.substring(start, i);
    }

    public boolean match(char c) {
        if (hasNext() && peek() == c) {
            next();
            return true;
        }
        return false;
    }

    public boolean match(String s) {
        if (!hasNext()) return false;
        int end = Math.min(i + s.length(), value.length() - 1);
        if (value.substring(i, end).equals(s)) {
            i = end;
            return true;
        }
        return false;
    }

    public char peek() {
        return value.charAt(i);
    }

    public String peek(int len) {
        if (!hasNext()) return "";
        return value.substring(i, Math.min(i + len, value.length() - 1));
    }

    public void expect(char expected) {
        if (!hasNext()) {
            throw new RuntimeException("Expected '" + expected + "', but found ''");
        }
        char actual = next();
        if (actual != expected) {
            throw new RuntimeException("Expected '" + expected + "', but found '" + actual + "'");
        }
    }

    public void expect(String expected) {
        String actual = "";
        int end = Math.min(i + expected.length(), value.length() - 1);
        if (end < value.length()) {
            actual = value.substring(i, end);
        }
        if (actual.equals(expected)) {
            i = end;
        } else {
            throw new RuntimeException("Expected '" + expected + "', but found '" + actual + "'");
        }
    }

    int i = 0;
    String value;
}
