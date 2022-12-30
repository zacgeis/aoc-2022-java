import utils.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    record SearchNode(String tunnel, int count) {}
    static class Tunnels {
        Map<String, Integer> flow_rates = new HashMap<>();
        Map<String, List<String>> tunnels = new HashMap<>();

        Map<String, List<SearchNode>> cache = new HashMap<>();
        List<SearchNode> shortestPaths(String from) {
            if (cache.containsKey(from)) return cache.get(from);
            Map<String, SearchNode> visited = new HashMap<>();
            Queue<SearchNode> frontier = new ArrayDeque<>();
            frontier.add(new SearchNode(from, 0));
            while (!frontier.isEmpty()) {
                SearchNode node = frontier.poll();
                if (visited.containsKey(node.tunnel)) continue;
                visited.put(node.tunnel, node);
                for (String to : getConnections(node.tunnel)) {
                    frontier.add(new SearchNode(to, node.count + 1));
                }
            }
            cache.put(from, visited.values().stream().toList());
            return cache.get(from);
        }

        void setFlowRate(String tunnel, int flow_rate) {
            flow_rates.put(tunnel, flow_rate);
        }

        int getFlowRate(String tunnel) {
            return flow_rates.get(tunnel);
        }

        void addConnection(String from, String to) {
            if (!tunnels.containsKey(from)) {
                tunnels.put(from, new ArrayList<>());
            }
            tunnels.get(from).add(to);
        }

        List<String> getConnections(String from) {
            return tunnels.get(from);
        }
    }

    static class State {
        record Player(String tunnel, int time) {}

        Tunnels tunnels;
        Set<String> open_valves = new HashSet<>();
        int pressure_released = 0;
        Player current_player;
        Player other_player;
        boolean two_player;

        State(Tunnels tunnels, boolean two_player) {
            this.tunnels = tunnels;
            final int time = two_player ? 26 : 30;
            this.current_player = new Player("AA", time);
            this.other_player = new Player("AA", time);
            this.two_player = two_player;
        }

        State(State other) {
            this.tunnels = other.tunnels;
            this.two_player = other.two_player;
        }

        List<State> nextStates() {
            List<State> next_states = new ArrayList<>();
            for (SearchNode node : tunnels.shortestPaths(current_player.tunnel)) {
                int flow_rate = tunnels.getFlowRate(node.tunnel);
                if (!open_valves.contains(node.tunnel) && flow_rate != 0 && node.count <= current_player.time) {
                    State new_state = new State(this);
                    Player new_player = new Player(node.tunnel, current_player.time - (node.count + 1));
                    new_state.open_valves = new HashSet<>(open_valves);
                    new_state.open_valves.add(node.tunnel);
                    new_state.pressure_released += pressure_released + new_player.time * flow_rate;
                    if (two_player) {
                        new_state.current_player = other_player;
                        new_state.other_player = new_player;
                    } else {
                        new_state.current_player = new_player;
                        new_state.other_player = other_player;
                    }
                    next_states.add(new_state);
                }
            }

            return next_states;
        }
    }

    static Tunnels parseTunnels(Parser parser) {
        Tunnels tunnels = new Tunnels();
        do {
            parser.match("Valve ");
            String from = parser.nextWord();
            parser.match(" has flow rate=");
            int flow_rate = parser.nextInt();
            parser.match("; tunnels lead to valves ");
            parser.match("; tunnel leads to valve ");
            tunnels.setFlowRate(from, flow_rate);
            do {
                tunnels.addConnection(from, parser.nextWord());
            } while (parser.match(", "));
        } while (parser.match("\n"));
        return tunnels;
    }

    static int findMax(State state) {
        int max = state.pressure_released;
        for (State next_state : state.nextStates()) {
            int possible_max = findMax(next_state);
            if (possible_max > max) {
                max = possible_max;
            }
        }
        return max;
    }

    static void part1(Tunnels tunnels) {
        State state = new State(tunnels, false);
        System.out.printf("part1: %d\n", findMax(state));
    }

    static void part2(Tunnels tunnels) {
        State state = new State(tunnels, true);
        System.out.printf("part2: %d\n", findMax(state));
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/16.txt"));
        Parser parser = Parser.of(input);
        Tunnels tunnels = parseTunnels(parser);
        part1(tunnels);
        part2(tunnels);
    }
}