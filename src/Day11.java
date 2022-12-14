import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    interface Value {}
    record Old() implements Value {}
    record Num(long value) implements Value {}
    enum Op {
        ADD,
        MUL,
    }
    static class Operation {
        Op op;
        Value left;
        Value right;
    }

    static class Monkey {
        Monkey(int number) {
            this.number = number;
        }

        long apply(long old) {
            long left = old;
            if (operation.left instanceof Num op_value) {
                left = op_value.value;
            }

            long right = old;
            if (operation.right instanceof Num op_value) {
                right = op_value.value;
            }

            if (operation.op == Op.ADD) {
                return left + right;
            }
            return left * right;
        }

        int number = 0;
        List<Long> items = new ArrayList<>();
        Operation operation;
        long test = 0;
        int if_true = 0;
        int if_false = 0;
        long count = 0;
    }

    static Operation parseOperation(String s) {
        Operation operation = new Operation();
        String[] parts = s.split(" ");
        if (parts[0].equals("old")) {
            operation.left = new Old();
        } else {
            operation.left = new Num(Integer.parseInt(parts[0]));
        }
        if (parts[1].equals("*")) {
            operation.op = Op.MUL;
        } else {
            operation.op = Op.ADD;
        }
        if (parts[2].equals("old")) {
            operation.right = new Old();
        } else {
            operation.right = new Num(Integer.parseInt(parts[2]));
        }
        return operation;
    }

    static Monkey parseMonkey(Iterator<String> iter) {
        String line = iter.next();
        int number = Integer.parseInt(line.replace(":", "").split(" ")[1]);
        Monkey monkey = new Monkey(number);

        for (String item : iter.next().split(": ")[1].split(", ")) {
            monkey.items.add(Long.parseLong(item));
        }
        monkey.operation = parseOperation(iter.next().split("new = ")[1]);
        monkey.test = Long.parseLong(iter.next().split(" by ")[1]);
        monkey.if_true = Integer.parseInt(iter.next().split(" monkey ")[1]);
        monkey.if_false = Integer.parseInt(iter.next().split(" monkey ")[1]);
        if (iter.hasNext()) {
            iter.next();
        }

        return monkey;
    }

    static List<Monkey> parseMonkeys(String input) {
        Iterator<String> iter = input.lines().iterator();
        List<Monkey> monkeys = new ArrayList<>();
        while (iter.hasNext()) {
            Monkey monkey = parseMonkey(iter);
            monkeys.add(monkey);
        }
        return monkeys;
    }

    static void part1(String input) {
        List<Monkey> monkeys = parseMonkeys(input);

        for (int i = 0; i < 20; ++i) {
            for (Monkey monkey : monkeys) {
                for (long item : monkey.items) {
                    long new_value = monkey.apply(item) / 3;
                    if (new_value % monkey.test == 0) {
                        monkeys.get(monkey.if_true).items.add(new_value);
                    } else {
                        monkeys.get(monkey.if_false).items.add(new_value);
                    }
                    monkey.count++;
                }
                monkey.items.clear();
            }
        }
        Long[] counts = monkeys.stream().map(monkey -> monkey.count)
                .sorted(Comparator.reverseOrder()).toArray(Long[]::new);

        System.out.println("part1: " + counts[0] * counts[1]);
    }

    static void part2(String input) {
        List<Monkey> monkeys = parseMonkeys(input);

        long multiple = 1;
        for (Monkey monkey : monkeys) {
            multiple *= monkey.test;
        }

        for (int i = 0; i < 10000; ++i) {
            for (Monkey monkey : monkeys) {
                for (long item : monkey.items) {
                    long new_value = monkey.apply(item) % multiple;
                    System.out.println(new_value);
                    if (new_value % monkey.test == 0) {
                        monkeys.get(monkey.if_true).items.add(new_value);
                    } else {
                        monkeys.get(monkey.if_false).items.add(new_value);
                    }
                    monkey.count++;
                }
                monkey.items.clear();
            }
        }
        Long[] counts = monkeys.stream().map(monkey -> monkey.count)
                .sorted(Comparator.reverseOrder()).toArray(Long[]::new);

        System.out.println("part2: " + counts[0] * counts[1]);
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/11.txt"));
        part1(input);
        part2(input);
    }
}