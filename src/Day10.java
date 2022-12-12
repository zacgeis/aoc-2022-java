import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10 {
    interface Op {}
    record Noop() implements Op {}
    record AddX(int value) implements Op {}

    static class Vm {
        static final int cycle_limit = 240;
        int cycle = 0;
        int x = 1;
        int total = 0;
        char[] buffer = new char[cycle_limit];

        void run(Op[] ops) {
            for (int i = 0; i < buffer.length; ++i) {
                buffer[i] = ' ';
            }
            for (Op op : ops) {
                if (cycle > cycle_limit) {
                    break;
                }
                if (op instanceof Noop) {
                    tick();
                } else if (op instanceof AddX addx) {
                    tick();
                    tick();
                    x += addx.value;
                }
            }
            System.out.println("part1: " + total);
            System.out.println("part2: ");
            for (int i = 0; i < 6; ++i) {
                for (int j = 0; j < 40; ++j) {
                    System.out.print(buffer[i * 40 + j]);
                }
                System.out.println();
            }
        }

        void tick() {
            if (x - 1 <= (cycle % 40) && x + 1 >= (cycle % 40)) {
                buffer[cycle] = 'X';
            }
            cycle += 1;
            if (cycle == 20 || (cycle > 20 && (cycle - 20) % 40 == 0)) {
                total += x * cycle;
            }
        }
    }

    static Op parse(String s) {
        if (s.startsWith("noop")) {
            return new Noop();
        } else if (s.startsWith("addx")) {
            String[] parts = s.split(" ");
            int value = Integer.parseInt(parts[1]);
            return new AddX(value);
        } else {
            throw new RuntimeException("invalid op.");
        }
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/10.txt"));
        Op[] ops = input.lines().map(line -> parse(line)).toArray(Op[]::new);
        Vm vm = new Vm();
        vm.run(ops);
    }
}