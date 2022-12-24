import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day15 {
    record Point(int x, int y) {
        int distance(Point other) {
            return Math.abs(other.x - x) + Math.abs(other.y - y);
        }
    }

    record Range(int start, int end) {
        int length() {
            return end - start;
        }

        boolean overlaps(Range other) {
            return other.start >= start && other.start <= end || other.end >= start && other.end <= end;
        }

        static Range merge(Range a, Range b) {
            if (!a.overlaps(b)) {
                throw new RuntimeException("Cannot merge ranges that don't overlap.");
            }

            return new Range(Math.min(a.start, b.start), Math.max(a.end, b.end));
        }

        static List<Range> compress(List<Range> ranges) {
            if (ranges.size() < 2) {
                return ranges;
            }
            List<Range> new_ranges = new ArrayList<>();
            ranges.sort(Comparator.comparingInt(r -> r.start));
            Range a = ranges.get(0);
            for (int i = 1; i < ranges.size(); ++i) {
                Range b = ranges.get(i);
                if (a.overlaps(b)) {
                    a = Range.merge(a, b);
                } else {
                    new_ranges.add(a);
                    a = b;
                }
            }
            new_ranges.add(a);
            return new_ranges;
        }
    }

    static class Sensor {
        Point location;
        Point beacon;
        int radius;

        Sensor(Point location, Point beacon) {
            this.location = location;
            this.beacon = beacon;
            this.radius = location.distance(beacon);
        }

        Optional<Range> getHorizontalRange(int y) {
            int dy = Math.abs(y - location.y);
            int dx = radius - dy;
            if (dx <= 0) return Optional.empty();
            return Optional.of(new Range(location.x - dx, location.x + dx));
        }
    }

    static Point parsePoint(Parser parser) {
        parser.expect("x=");
        int x = parser.nextInt();
        parser.expect(", y=");
        int y = parser.nextInt();
        return new Point(x, y);
    }

    static Sensor parseSensor(Parser parser) {
        parser.expect("Sensor at ");
        Point location = parsePoint(parser);
        parser.expect(": closest beacon is at ");
        Point beacon = parsePoint(parser);
        return new Sensor(location, beacon);
    }

    static List<Sensor> parseSensors(Parser parser) {
        List<Sensor> sensors = new ArrayList<>();

        do {
            sensors.add(parseSensor(parser));
        } while(parser.match("\n"));

        return sensors;
    }

    static List<Range> getCompressedHorizontalRange(List<Sensor> sensors, int y) {
        List<Range> ranges = new ArrayList<>();
        for (Sensor sensor : sensors) {
            Optional<Range> range = sensor.getHorizontalRange(y);
            if (range.isPresent()) {
                ranges.add(range.get());
            }
        }
        List<Range> compressed = Range.compress(ranges);
        return compressed;
    }

    static void part1(List<Sensor> sensors) {
        List<Range> compressed_range = getCompressedHorizontalRange(sensors, 2000000);
        System.out.printf("part1: %d\n", compressed_range.get(0).length());
    }

    static void part2(List<Sensor> sensors) {
        for (int i = 0; i <= 4000000; ++i) {
            List<Range> compressed_range = getCompressedHorizontalRange(sensors, i);
            if (compressed_range.size() > 1) {
                long signal = (compressed_range.get(0).end + 1) * 4000000L + i;
                System.out.printf("part2: %d\n", signal);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/15.txt"));
        Parser parser = Parser.of(input);
        List<Sensor> sensors = parseSensors(parser);
        part1(sensors);
        part2(sensors);
    }
}