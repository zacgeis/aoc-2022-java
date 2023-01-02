import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 {
    enum Dir {
        LEFT,
        RIGHT,
    }

    record Point(int x, int y) {
        Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
    }

    static class Board {
        static final int WIDTH = 7;
        Map<Point, Boolean> grid = new HashMap<>();
        List<Dir> dirs;

        Board(List<Dir> dirs) {
            this.dirs = dirs;
        }

        void placePiece(Piece piece) {
            // TODO
        }

        void addPiece(Point point, Piece piece) {
            for (Point piece_point : piece.points) {
                grid.put(point.add(piece_point), true);
            }
        }

        boolean collisionTest(Point point, Piece piece) {
            // TODO
            return false;
        }
    }

    static class Piece {
        static final Piece one = new Piece("""
            ####""");
        static final Piece two = new Piece("""
            .#.
            ###
            .#.""");
        static final Piece three = new Piece("""
            ..#
            ..#
            ###""");
        static final Piece four = new Piece("""
            #
            #
            #
            #""");
        static final Piece five = new Piece("""
            ##
            ##""");
        static final Piece[] all = new Piece[]{one, two, three, four, five};

        List<Point> points = new ArrayList<>();

        Piece(String input) {
            String[] lines = input.split("\n");
            for (int y = 0; y < lines.length; ++y) {
                char[] chars = lines[y].toCharArray();
                for (int x = 0; x < chars.length; ++x) {
                    if (chars[x] == '#') {
                        points.add(new Point(x, y));
                    }
                }
            }
        }
    }

    static void part1(List<Dir> dirs) {
        System.out.printf("part1: %d\n", 0);
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/17.txt"));
        Parser parser = Parser.of(input);
        List<Dir> dirs = new ArrayList<>();
        while (parser.hasNext()) {
            dirs.add(switch (parser.next()) {
                case '<' -> Dir.LEFT;
                case '>' -> Dir.RIGHT;
                default -> throw new RuntimeException("Unexpected char.");
            });
        }

        part1(dirs);
    }
}