import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;

public class Day12 {
    record Loc(int x, int y) {
        boolean inBounds(int width, int height) {
            return x >= 0 && x < width && y >= 0 && y < height;
        }
        Loc[] neighbors() {
            Loc left = new Loc(x - 1, y);
            Loc right = new Loc(x + 1, y);
            Loc up = new Loc(x, y - 1);
            Loc down = new Loc(x, y + 1);
            return new Loc[] {left, right, up, down};
        }
    };

    public static void part1(String input) {
        String[] lines = input.split("\n");
        int height = lines.length;
        int width = lines[0].length();
        int[][] grid = new int[height][width];
        int[][] visited = new int[height][width];
        int x = 0;
        int y = 0;
        Loc start_loc = null;
        Loc end_loc = null;
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                if (c == 'S') {
                    c = 'a';
                    start_loc = new Loc(x, y);
                } else if (c == 'E') {
                    c = 'z';
                    end_loc = new Loc(x, y);
                }
                visited[y][x] = 0;
                grid[y][x++] = c - 'a';
            }
            x = 0;
            y += 1;
        }
        assert start_loc != null;
        assert end_loc != null;

        Queue<Loc> frontier = new ArrayDeque<>();
        frontier.add(start_loc);
        while (!frontier.isEmpty()) {
            Loc current = frontier.remove();
            if (current.equals(end_loc)) {
                System.out.println("part1: " + visited[current.y][current.x]);
                break;
            }
            int current_height = grid[current.y][current.x];
            for (Loc neighbor : current.neighbors()) {
                if (neighbor.inBounds(width, height) && visited[neighbor.y][neighbor.x] == 0) {
                    int neighbor_height = grid[neighbor.y][neighbor.x];
                    if (neighbor_height - current_height <= 1) {
                        visited[neighbor.y][neighbor.x] = visited[current.y][current.x] + 1;
                        frontier.add(neighbor);
                    }
                }
            }
        }
    }

    public static void part2(String input) {
        String[] lines = input.split("\n");
        int height = lines.length;
        int width = lines[0].length();
        int[][] grid = new int[height][width];
        int[][] visited = new int[height][width];
        int x = 0;
        int y = 0;
        Loc start_loc = null;
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                if (c == 'E') {
                    c = 'z';
                    start_loc = new Loc(x, y);
                }
                visited[y][x] = 0;
                grid[y][x++] = c - 'a';
            }
            x = 0;
            y += 1;
        }
        assert start_loc != null;

        Queue<Loc> frontier = new ArrayDeque<>();
        frontier.add(start_loc);
        while (!frontier.isEmpty()) {
            Loc current = frontier.remove();
            if (grid[current.y][current.x] == 0) {
                System.out.println("part2: " + visited[current.y][current.x]);
                break;
            }
            int current_height = grid[current.y][current.x];
            for (Loc neighbor : current.neighbors()) {
                if (neighbor.inBounds(width, height) && visited[neighbor.y][neighbor.x] == 0) {
                    int neighbor_height = grid[neighbor.y][neighbor.x];
                    if (current_height - neighbor_height <= 1) {
                        visited[neighbor.y][neighbor.x] = visited[current.y][current.x] + 1;
                        frontier.add(neighbor);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/12.txt"));
        part1(input);
        part2(input);
    }
}