import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day14 {
    record Loc(int x, int y) {}

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

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/14.txt"));
        List<List<Loc>> locs = parseTraces(Parser.of(input));
        System.out.println(locs);
        HashMap<Loc, Integer> grid = new HashMap<>();
    }
}

// TODO: ga code action, gc comment, gf & gF format, gh hover, ctrl-p finder. something to open find in file too.