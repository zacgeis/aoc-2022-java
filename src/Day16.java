import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    static class Tunnels {
        Map<String, Integer> flow_rates = new HashMap<>();
        Map<String, List<String>> tunnels = new HashMap<>();

        void setFlowRate(String tunnel, int flow_rate) {
            flow_rates.put(tunnel, flow_rate);
        }

        void addConnection(String from, String to) {
            List<String> tos = tunnels.putIfAbsent(from, new ArrayList<>());
            tos.add(to);
        }
    }

    static class Simulation {
        Tunnels tunnels;
        Set<String> open_valves = new HashSet<>();
        int time = 30;
        int pressure_released = 0;
        String current = "AA";

        Simulation(Tunnels tunnels) {
            this.tunnels = tunnels;
        }

        void move() {
            step();
        }

        void openValve() {
            step();
        }

        void step() {
            // TODO: adjust the time and the pressure released.
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
            tunnels.setFlowRate(from, flow_rate);
            do {
                tunnels.addConnection(from, parser.nextWord());
            } while(parser.match(", "));
        } while(parser.match("\n"));
        return tunnels;
    }

    static void part1(Tunnels tunnels) {
        System.out.println(tunnels.flow_rates.size());
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs/16.txt"));
        Parser parser = Parser.of(input);
        Tunnels tunnels = parseTunnels(parser);
        part1(tunnels);
    }
}