import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

public final class DirectedGraph {
    private final List<List<Integer>> edges;
    private final List<Integer> nodes;

    public DirectedGraph(int nodeCount) {
        this.edges = new ArrayList<>(nodeCount);
        this.nodes = new ArrayList<>();

        for (int i = 0; i < nodeCount; i++) {
            this.edges.add(new ArrayList<>());
            this.nodes.add(i);
        }

        this.generateRandomHamiltonian(nodeCount);
        //this.generateEdges();
    }

    private void generateRandomHamiltonian(int size) {
        List<Integer> nodes = this.getNodes();

        shuffle(nodes);

        for (int i = 0; i < nodes.size() - 1; i++){
            this.addEdge(nodes.get(i),  nodes.get(i + 1));
        }

        this.addEdge(nodes.get(nodes.size() - 1), nodes.get(0));
        Random random = new Random();

        for (int i = 0; i < size / 2; i++){
            int a = random.nextInt(size - 1);
            int b = random.nextInt(size - 1);

            this.addEdge(a, b);
        }
    }

    private void generateEdges() {
        List<Integer> nodes = this.getNodes();

        shuffle(nodes);

        this.addEdge(0,1);
        this.addEdge(1, 3);
    }

    public void addEdge(int a, int b) {
        // a - inbound, b - outbound
        this.edges.get(a).add(b);
    }

    public List<Integer> neighboursOf(int node) {
        return this.edges.get(node);
    }

    public List<Integer> getNodes(){
        return nodes;
    }

    public int size() {
        return this.nodes.size();
    }

    @Override
    public String toString() {
        return "Directed Graph {" +
                "nodes -> " + nodes +
                ", edges -> " + edges +
                '}';
    }
}