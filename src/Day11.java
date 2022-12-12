import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Day11 {
    static class Monkey {
        Monkey(int number) {
            this.number = number;
        }

        int number = 0;
        List<Integer> items = new ArrayList<>();
    }

    Monkey parseMonkey(Iterator<String> iter) {
        String line = iter.next();
        int number = Integer.parseInt(line.replace(":", "").split(" ")[1]);
        String starting_items = iter.next();

        Monkey monkey = new Monkey(number);
        return monkey;
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/11.txt"));
        Iterator<String> iter = input.lines().iterator();
    }
}