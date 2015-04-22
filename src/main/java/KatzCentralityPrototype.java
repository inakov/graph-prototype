import java.util.ArrayList;
import java.util.List;

/**
 * Created by inakov on 4/22/15.
 */
public class KatzCentralityPrototype {
    private static final double KARMA_SPREAD = 0.15;

    public static void main(String[] args) {
        Graph g = new Graph(6, true);
        g.addEdge(1, 0);
        g.addEdge(1, 4);
        g.addEdge(1, 2);

        g.addEdge(0, 3);
        g.addEdge(0, 4);

        g.addEdge(3, 4);
        g.addEdge(3, 5);

        g.addEdge(2, 4);

//        g.addEdge(6, 7);
//        g.addEdge(7, 6);


        System.out.println(g);
        katzCentrality(g);
    }

    private static void katzCentrality(Graph graph) {
        final double DEFAULT_ALPHA = 1.0;
        final double DEFAULT_BETA = 1.0;

        int MAX_ITERATIONS = 100;
        double EPSILON = 1e-6;

        int size = graph.V();
        double centrality[];
        double old[];
        double tmp[];
        double change;
        double sum2;
        double norm;
        int iteration;
        double alpha = DEFAULT_ALPHA;
        double beta = DEFAULT_BETA;

        // Initialization: 1/N

        centrality = new double[size];
        old = new double[size];

        for (int i = 0; i < size; i++) {
            centrality[i] = 1.0 / size;
            old[i] = centrality[i];
        }

        // Power iteration: O(k(n+m))
        // The value of norm converges to the dominant eigenvalue, and the vector 'centrality' to an associated eigenvector
        // ref. http://en.wikipedia.org/wiki/Power_iteration
        change = Double.MAX_VALUE;

        for (iteration = 0; (iteration < MAX_ITERATIONS) && (change > EPSILON); iteration++) {
            tmp = old;         // Swap old-centrality
            old = centrality;
            centrality = tmp;

            sum2 = 0;

            for (int v = 0; v < size; v++) {

                centrality[v] = 0.0;

                // Right eigenvector
                for (Integer i : graph.adj(v)) {
                    centrality[v] += old[i];
                }

                // Katz centrality
                centrality[v] = alpha * centrality[v] + beta / size;
                sum2 += centrality[v] * centrality[v];
            }

            // Normalization
            norm = Math.sqrt(sum2);
            change = 0;

            for (int v = 0; v < size; v++) {
                centrality[v] /= norm;

                if (Math.abs(centrality[v] - old[v]) > change)
                    change = Math.abs(centrality[v] - old[v]);
            }
        }

        List<Double> centralityList = new ArrayList<Double>();
        for (double i : centrality) {
            System.out.println(i);
            centralityList.add(i);
        }

        System.out.println("Normalized: " + normalised(centralityList));
    }

    private static List<Double> normalised(List<Double> principalEigenvector) {
        double total = sum(principalEigenvector);
        List<Double> normalisedValues = new ArrayList<Double>();
        for (Double aDouble : principalEigenvector) {
            normalisedValues.add(aDouble / total);
        }
        return normalisedValues;
    }

    private static double sum(List<Double> principalEigenvector) {
        double total = 0;
        for (Double aDouble : principalEigenvector) {
            total += aDouble;
        }
        return total;
    }
}
