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
        int lowest = Integer.MAX_VALUE;

        void set(Loc l, State s) {
            grid.put(l, s);
        }

        State get(Loc l) {
            if (l.y < lowest) {
                return State.ABYSS;
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
                        return true;
                    }
                }
            }
            set(current, State.SAND);

            return false;
        }

        void addWall(Loc a, Loc b) {
            if (a.x == b.x) {
                int min = Math.min(a.y, b.y);
                int max = Math.max(a.y, b.y);
                for (int y = min; y <= max; ++y) {
                    set(new Loc(a.x, y), State.WALL);
                }
            } else if (a.y == b.y) {
                int min = Math.min(a.x, b.x);
                int max = Math.max(a.x, b.x);
                if (min < lowest) lowest = min;
                for (int x = min; x <= max; ++x) {
                    set(new Loc(x, a.y), State.WALL);
                }
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
        Cave cave = new Cave();
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

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/14.txt"));
        part1(input);
    }
}

// TODO: ga code action, gc comment, gf & gF format, gh hover, ctrl-p finder. something to open find in file too.