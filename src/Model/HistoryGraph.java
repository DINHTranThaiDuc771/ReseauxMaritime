package Model;

import java.io.IOException;
import java.util.*;

public class HistoryGraph {
    private static final int UNKNOWN_VALUE = Move.NULLE_PARTE;
    private static final int NB_OF_DAYS = 5;
    private static final long NB_MAX_PROBABILITY = 100000;
    private ArrayList<Integer> lstPorte;
    private HashSet<EdgePorte> setEdge;
    private Map<Integer, Integer> mapPorteAndIndexOfMatrice;
    private HashSet<Integer> listDeadEnd;
    private Record[][] matrixGraph;

    public HistoryGraph(List<Move> lstMoves) {
        this.listDeadEnd = new HashSet<>();
        this.setEdge = new HashSet<>();

        HashSet<Integer> setPorte = new HashSet<>();
        // setPorte does not add -3 (UNKNOWN_VALUE)
        for (Move m : lstMoves) {
            if (m.getFrom_id() == UNKNOWN_VALUE)
                continue;
            if (m.getTo_id() == UNKNOWN_VALUE)
                continue;

            setPorte.add(m.getFrom_id());
            setPorte.add(m.getTo_id());
        }
        lstPorte = new ArrayList<>(setPorte);
        Collections.sort(lstPorte);
        this.matrixGraph = new Record[lstPorte.size()][lstPorte.size()];
        for (int lig = 0; lig < matrixGraph.length; lig++) {
            for (int col = 0; col < matrixGraph.length; col++) {
                matrixGraph[lig][col] = new Record();
            }
        }
        int indexMatrix = 0;
        mapPorteAndIndexOfMatrice = new HashMap<Integer, Integer>();
        for (int porte : lstPorte) {
            mapPorteAndIndexOfMatrice.put(porte, indexMatrix++);
        }
        for (Move m : lstMoves) {
            if (m.getFrom_id() == UNKNOWN_VALUE)
                continue;
            if (m.getTo_id() == UNKNOWN_VALUE)
                continue;
            int from = mapPorteAndIndexOfMatrice.get(m.getFrom_id());
            int to = mapPorteAndIndexOfMatrice.get(m.getTo_id());
            if (from == to)
                continue;
            setEdge.add(new EdgePorte(from, to));
            matrixGraph[from][to].nbOfMove++;
            matrixGraph[from][to].nbOfDay += Date.between(m.getDepart(), m.getArrival());

        }

    }

    public ArrayList<Move> generateNbMoves(int nbMove, int porteDeb, int porteFin, Date dateDeb, Date dateFin) {
        ArrayList<Move> lstRet = new ArrayList<>();
        try {
            do {
                lstRet = generateMoves(porteDeb, porteFin, dateDeb, dateFin);
            } while (lstRet.size() != nbMove);
        } catch (java.lang.NullPointerException e) {
            System.out.println("La porte " + porteDeb + " ou " + porteFin + " n'existe pas dans le graph");
        }
        return lstRet;
    }

    public ArrayList<Move> generateMoves(int porteDeb, int porteFin, Date dateDeb, Date dateFin) {
        ArrayList<Integer> chemin = generateCheminWithPorteFin(porteDeb, porteFin, Date.between(dateDeb, dateFin));
        ArrayList<Move> lstMove = new ArrayList<>();
        // The final porte is the porteFin
        Date currentDate = dateDeb;
        for (int i = 0; i < chemin.size() - 1; i++) {
            int portA, portB;
            int averageDuration;
            Move move;
            portA = chemin.get(i);
            portB = chemin.get(i + 1);
            averageDuration = matrixGraph[mapPorteAndIndexOfMatrice.get(portA)][mapPorteAndIndexOfMatrice.get(portB)]
                    .getAverageDuration();
            move = new Move(currentDate, Date.getNextDate(currentDate, averageDuration), portA, portB);
            lstMove.add(move);
            currentDate = Date.getNextDate(currentDate, averageDuration + 1);// averageDuration +1 because the navire
                                                                             // only do a movement in a day
        }

        return lstMove;
    }

    private ArrayList<Integer> generateCheminWithPorteFin(int porteDeb, int porteFin, long periode) {
        // An array including porteDeb

        ArrayList<Integer> lstRet = new ArrayList<>();
        boolean valide = false;
        int nbProbability = 0;
        long currentPeriode = 0;

        while (!valide) {

            lstRet.clear();
            lstRet.add(porteDeb);
            currentPeriode = 0;
            int currentPorte = porteDeb;
            if (porteDeb == porteFin) {
                currentPorte = nextPorte(porteDeb);
                lstRet.add(currentPorte);
            }
            while (currentPorte != porteFin || currentPeriode < periode - NB_OF_DAYS) {
                int porteNext = nextPorte(currentPorte);
                // Check if it is a deadEnd
                if (listDeadEnd.contains(currentPorte)) {
                    System.out.println("DeadEnd " + currentPorte);
                    System.out.println(lstRet);
                    return lstRet;
                }
                if (currentPeriode >= periode)
                    break;
                currentPeriode += matrixGraph[mapPorteAndIndexOfMatrice.get(currentPorte)][mapPorteAndIndexOfMatrice
                        .get(porteNext)].getAverageDuration();
                currentPeriode++;// TODO explains more
                currentPorte = porteNext;
                lstRet.add(currentPorte);
            }
            if (nbProbability < HistoryGraph.NB_MAX_PROBABILITY) {
                valide = currentPeriode < periode && currentPeriode >= periode - NB_OF_DAYS;
            }
            if (nbProbability >= HistoryGraph.NB_MAX_PROBABILITY) {
                System.out.println(this);
                valide = true;
            }
            nbProbability++;
        }
        System.out.println(lstRet);
        return lstRet;
    }

    private ArrayList<Integer> generateChemin(int nbOfStep, int porteDeb) {
        // generateChemin (a,b) will return an array of a elements INCLUDING porte b
        ArrayList<Integer> lstRet = new ArrayList<>(nbOfStep);
        int currentPorte = porteDeb;
        lstRet.add(currentPorte);

        for (int i = 0; i < nbOfStep - 1; i++) {
            currentPorte = nextPorte(currentPorte);
            lstRet.add(currentPorte);
        }
        return lstRet;
    }

    private int nextPorte(int porteDeb) {
        int porteRet = UNKNOWN_VALUE;

        try {
            Record[] lstProbability = matrixGraph[mapPorteAndIndexOfMatrice.get(porteDeb)]; // time complexity is 1,
                                                                                            // better than O(n) of
                                                                                            // indexOf

            int totalProb = 0;
            for (Record rec : lstProbability) {
                totalProb += rec.nbOfMove;
            }
            if (totalProb == 0) {
                this.listDeadEnd.add(porteDeb);
                return porteDeb;
            }
            Random random = new Random();
            int randomNumber = random.nextInt(totalProb); // 0 <= the randomNumber < a
            int sum = 0;
            for (int i = 0; i < lstProbability.length; i++) {
                if (sum <= randomNumber && randomNumber < sum + lstProbability[i].nbOfMove) {
                    porteRet = lstPorte.get(i);
                    break;
                }
                sum += lstProbability[i].nbOfMove;
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("La porte " + porteDeb + " n'existe pas dans le graph");
        }
        return porteRet;
    }

    @Override
    public String toString() {
        String ret = "";
        for (int porte : lstPorte) {
            ret += String.format("%10d", porte);
        }
        ret += "\n";
        int lig = 0;
        for (int porte : lstPorte) {
            ret += String.format("%5d", porte);
            if (matrixGraph[lig][0].nbOfMove != 0)
                ret += String.format("\u001B[31m%5s\u001B[0m",
                        matrixGraph[lig][0].nbOfMove + "," + matrixGraph[lig][0].getAverageDuration());
            else
                ret += String.format("%5s",
                        matrixGraph[lig][0].nbOfMove + "," + matrixGraph[lig][0].getAverageDuration());

            for (int col = 1; col < matrixGraph[lig].length; col++) {
                if (matrixGraph[lig][col].nbOfMove != 0)
                    ret += String.format("\u001B[31m%10s\u001B[0m",
                            matrixGraph[lig][col].nbOfMove + "," + matrixGraph[lig][col].getAverageDuration());
                else
                    ret += String.format("%10s",
                            matrixGraph[lig][col].nbOfMove + "," + matrixGraph[lig][col].getAverageDuration());
            }
            ret += "\n";
            lig++;
        }
        return ret;
    }

    public boolean isConnected() {
        int numVertices = matrixGraph.length;
        Stack<Integer> stack = new Stack<>(); // Using DFS
        Set<Integer> visited = new HashSet<>();

        stack.push(0); // Start with vertex 0

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            visited.add(vertex);

            // Check adjacent vertices
            for (int i = 0; i < numVertices; i++) {
                if (matrixGraph[vertex][i].nbOfMove != 0 && !visited.contains(i)) {
                    stack.push(i);
                }
            }
        }

        return visited.size() == numVertices;
    }
    /*
     * First, check the graph if it is connected
     * Then, check the transpose graph if it is connected
     */
    public boolean isStronglyConnected() {
        int numVertices = matrixGraph.length;
        boolean[] visited = new boolean[numVertices];
        Stack<Integer> stack = new Stack<>();

        stack.push(0); // Start with vertex 0

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            visited[vertex] = true;

            for (int i = 0; i < numVertices; i++) {
                if (matrixGraph[vertex][i].nbOfMove != 0 && !visited[i]) {
                    stack.push(i);
                }
            }
        }

        for (boolean v : visited) {
            if (!v) {
                return false; // Not all vertices are visited
            }
        }

        // Transpose the adjacency matrix
        int[][] transposeMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                transposeMatrix[i][j] = matrixGraph[j][i].nbOfMove;
            }
        }

        // Reset visited array
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        stack.push(0); // Start with vertex 0 in the transposed graph

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            visited[vertex] = true;

            for (int i = 0; i < numVertices; i++) {
                if (transposeMatrix[vertex][i] != 0 && !visited[i]) {
                    stack.push(i);
                }
            }
        }

        for (boolean v : visited) {
            if (!v) {
                return false; // Not all vertices are visited in the transposed graph
            }
        }

        return true; // Graph is strongly connected 
    }

    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("./dataUniq/1977.csv");
        HashMap<Navire, LinkedList<Move>> mapNavireVsListmove;
        mapNavireVsListmove = model.getMapNavireVsListmove();
        ArrayList<Navire> lstNavire = new ArrayList<>(mapNavireVsListmove.keySet());
        Collections.sort(lstNavire);
        Navire navExample = null;

        for (Navire nav : lstNavire) {
            if (nav.getId() == 2102) {
                navExample = nav;
                break;
            }
        }
        HistoryGraph historyGraph = new HistoryGraph(mapNavireVsListmove.get(navExample));
        System.out.println("Graph of Navire:" + navExample + " in 1977.csv");
        System.out.println(historyGraph);
        for (Move m : historyGraph.generateMoves(2151, 2151, new Date("2/10/1977"), new Date("26/11/1977"))) {
            System.out.println(m);
        }

    }

    class Record {

        int nbOfMove;
        int nbOfDay;
        int averageDuration;

        public Record() {
            this.nbOfMove = this.nbOfDay = this.averageDuration = 0;
        }

        public int getAverageDuration() {
            if (nbOfMove == 0)
                return 0;
            return this.nbOfDay / this.nbOfMove;
        }

    }

    class EdgePorte {
        public EdgePorte(int porteDeb, int porteFin) {
            this.porteDeb = porteDeb;
            this.porteFin = porteFin;
        }

        @Override
        public int hashCode() {
            int x, y;
            x = porteDeb;
            y = porteFin;
            return (x + y) * (x + y + 1) / 2 + y; // pairing function
        }

        private int porteDeb;
        private int porteFin;
    }
}
