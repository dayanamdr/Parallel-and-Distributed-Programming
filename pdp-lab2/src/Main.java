import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // random graph
        DirectedGraph graph = new DirectedGraph(15);
        System.out.println("Graph: " + graph.getNodes() + " " + graph.getEdges());
        HamiltonianCycleFinder finder = new HamiltonianCycleFinder(graph);
        //finder.beginSearch();

        // custom graphs
        // graph 1 - Hamiltonian cycle: 0 -> 1 -> 2 -> 4 -> 3 -> 0
        DirectedGraph graphWithHamiltonianCycle = new DirectedGraph(
                new ArrayList<>(List.of(0, 1, 2, 3, 4)),
                new ArrayList<>(
                        List.of(
                                new ArrayList<>(List.of(1)),
                                new ArrayList<>(List.of(2, 3)),
                                new ArrayList<>(List.of(4)),
                                new ArrayList<>(List.of(0)),
                                new ArrayList<>(List.of(1, 3, 4))
                        )
                )
        );


        // graph 2 - no Hamiltonian cycle, edge 4 -> 3 is removed from the previous one
        DirectedGraph graphWithoutHamiltonianCycle = new DirectedGraph(
                new ArrayList<>(List.of(0, 1, 2, 3, 4)),
                new ArrayList<>(
                        List.of(
                                new ArrayList<>(List.of(1)),
                                new ArrayList<>(List.of(2, 3)),
                                new ArrayList<>(List.of(4)),
                                new ArrayList<>(List.of(0)),
                                new ArrayList<>(List.of(1, 4))
                        )
                )
        );

        var startTime = System.nanoTime();
        HamiltonianCycleFinder findHamiltonianCycle = new HamiltonianCycleFinder(graph);
        findHamiltonianCycle.beginSearch();
        var endTime = System.nanoTime();
        System.out.println("Time: " + ((double) endTime - (double) startTime) / 1_000_000_000.0 + "s");

//        var startTime = System.nanoTime();
//        HamiltonianCycleFinder findHamiltonianCycle = new HamiltonianCycleFinder(graphWithHamiltonianCycle);
//        findHamiltonianCycle.beginSearch();
//        var endTime = System.nanoTime();
//        System.out.println("Time: " + ((double) endTime - (double) startTime) / 1_000_000_000.0 + "s");

//        var startTime = System.nanoTime();
//        HamiltonianCycleFinder findHamiltonianCycle = new HamiltonianCycleFinder(graphWithoutHamiltonianCycle);
//        findHamiltonianCycle.beginSearch();
//        var endTime = System.nanoTime();
//        System.out.println("Time: " + ((double) endTime - (double) startTime) / 1_000_000_000.0 + "s");


    }
}