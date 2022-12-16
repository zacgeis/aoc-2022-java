import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 {
    sealed interface ListOrNum permits Num, _List {}
    record _List(List<ListOrNum> list) implements ListOrNum {}
    record Num(int value) implements ListOrNum {}

    static ListOrNum createDividerPacket(int value) {
        return new _List(List.of(new _List(List.of(new Num(value)))));
    }

    static List<ListOrNum> parseLists(Parser parser) {
        List<ListOrNum> lists = new ArrayList<>();
        while (parser.hasNext()) {
            parser.skipNewLines();
            lists.add(parseList(parser));
        }
        return lists;
    }

    static ListOrNum parseList(Parser parser) {
        parser.match('[');
        List<ListOrNum> values = new ArrayList<>();
        while (parser.hasNext()) {
            char c = parser.peek();
            if (c == ']') {
                break;
            } else if (c == '[') {
                values.add(parseList(parser));
            } else if (c == ',') {
                parser.next();
            } else {
                values.add(new Num(parser.nextInt()));
            }
        }
        parser.match(']');
        return new _List(values);
    }

    static int compare(ListOrNum left, ListOrNum right) {
        if (left instanceof Num left_num && right instanceof Num right_num) {
            if (left_num.value < right_num.value) {
                return 1;
            } else if (left_num.value == right_num.value) {
                return 0;
            } else {
                return -1;
            }
        } else if (left instanceof _List left_list && right instanceof _List right_list) {
            int i = 0;
            for (; i < left_list.list.size(); ++i) {
                if (i < right_list.list.size()) {
                    int result = compare(left_list.list.get(i), right_list.list.get(i));
                    if (result != 0) {
                        return result;
                    }
                } else {
                    return -1;
                }
            }
            if (i < right_list.list.size()) {
                return 1;
            } else {
                return 0;
            }
        } else if (left instanceof _List && right instanceof Num right_num) {
            return compare(left, new _List(List.of(right_num)));
        } else if (left instanceof Num left_num && right instanceof _List) {
            return compare(new _List(List.of(left_num)), right);
        }
        throw new RuntimeException("invalid state.");
    }

    static void part1(String input) {
        List<ListOrNum> lists = parseLists(Parser.of(input));
        int total = 0;
        for (int i = 0; i < lists.size() / 2; ++i) {
            ListOrNum left = lists.get(i * 2);
            ListOrNum right = lists.get(i * 2 + 1);
            int results = compare(left, right);
            if (results == 1) {
                total += i + 1;
            }
        }
        System.out.printf("part1: %d\n", total);
    }

    static void part2(String input) {
        List<ListOrNum> lists = parseLists(Parser.of(input));
        ListOrNum divider_packet_1 = createDividerPacket(2);
        ListOrNum divider_packet_2 = createDividerPacket(6);
        lists.add(divider_packet_1);
        lists.add(divider_packet_2);

        lists.sort((a, b) -> compare(a, b) * -1);
        int result = 1;
        for (int i = 0; i < lists.size(); ++i) {
            if (lists.get(i) == divider_packet_1 || lists.get(i) == divider_packet_2) {
                result *= i + 1;
            }
        }
        System.out.printf("part2: %d\n", result);
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/13.txt"));
        part1(input);
        part2(input);
    }
}