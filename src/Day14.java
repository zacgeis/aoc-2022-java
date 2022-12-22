import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day14 {
    record Loc(int x, int y) {
        Loc move(int dx, int dy) {
            return new Loc(x + dx, y + dy);
        }
    }
    enum State {
        SAND,
        WALL,
        AIR,
        ABYSS,
    }

    static class Cave {
        HashMap<Loc, State> grid = new HashMap<>();
        int lowest = 0;
        boolean part2;

        Cave(boolean part2) {
            this.part2 = part2;
        }

        void set(Loc l, State s) {
            grid.put(l, s);
        }

        State get(Loc l) {
            if (part2) {
                if (l.y >= lowest + 2) {
                    return State.WALL;
                }
            } else {
                if (l.y > lowest) {
                    return State.ABYSS;
                }
            }
            return grid.getOrDefault(l, State.AIR);
        }

        boolean dropSand(Loc source) {
            Loc current = source;

            boolean settled = false;
            while (!settled) {
                settled = true;
                Loc[] options = {
                        current.move(0, 1),
                        current.move(-1, 1),
                        current.move(1, 1)
                };
                for (Loc next : options) {
                    State next_state = get(next);
                    if (next_state == State.AIR) {
                        settled = false;
                        current = next;
                        break;
                    } else if (next_state == State.ABYSS) {
                        return false;
                    }
                }
            }

            if (current.equals(source)) {
                return false;
            }

            set(current, State.SAND);
            return true;
        }

        void addWall(Loc a, Loc b) {
            if (a.x == b.x) {
                int min = Math.min(a.y, b.y);
                int max = Math.max(a.y, b.y);
                for (int y = min; y <= max; ++y) {
                    set(new Loc(a.x, y), State.WALL);
                }
                if (max > lowest) lowest = max;
            } else if (a.y == b.y) {
                int min = Math.min(a.x, b.x);
                int max = Math.max(a.x, b.x);
                for (int x = min; x <= max; ++x) {
                    set(new Loc(x, a.y), State.WALL);
                }
                if (a.y > lowest) lowest = a.y;
            } else {
                throw new RuntimeException("Invalid wall.");
            }
        }

        void addWalls(List<Loc> traces) {
            for (int i = 0; i < traces.size() - 1; ++i) {
                addWall(traces.get(i), traces.get(i + 1));
            }
        }
    }

    static List<Loc> parseTrace(Parser parser) {
        List<Loc> locs = new ArrayList<>();
        do {
            int x = parser.nextInt();
            parser.expect(',');
            int y = parser.nextInt();
            locs.add(new Loc(x, y));
        } while (parser.match(" -> "));
        return locs;
    }

    static List<List<Loc>> parseTraces(Parser parser) {
        List<List<Loc>> results = new ArrayList<>();
        do {
            results.add(parseTrace(parser));
        } while (parser.match('\n'));
        return results;
    }

    static void part1(String input) {
        List<List<Loc>> traces = parseTraces(Parser.of(input));
        Cave cave = new Cave(false);
        for (List<Loc> trace : traces) {
            cave.addWalls(trace);
        }
        Loc source = new Loc(500, 0);
        int count = 0;
        while (cave.dropSand(source)) {
            ++count;
        }
        System.out.printf("part1: %d\n", count);
    }

    static void part2(String input) {
        List<List<Loc>> traces = parseTraces(Parser.of(input));
        Cave cave = new Cave(true);
        for (List<Loc> trace : traces) {
            cave.addWalls(trace);
        }
        Loc source = new Loc(500, 0);
        int count = 0;
        do {
            ++count;
        } while (cave.dropSand(source));
        System.out.printf("part2: %d\n", count);
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/14.txt"));
        part1(input);
        part2(input);
    }
}