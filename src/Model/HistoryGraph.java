package Model;

import java.io.IOException;
import java.util.*;

public class HistoryGraph 
{
    private static final int UNKNOWN_VALUE = -3;
    private static final int NB_OF_DAYS    = 10;
    private ArrayList<Integer> lstPorte;
    private Map<Integer,Integer> mapPorteAndIndexOfMatrice;
    private Record[][]          matrixGraph;
    public HistoryGraph(List<Move> lstMoves)
    {
        HashSet<Integer> setPorte = new HashSet<>();
        for (Move m : lstMoves)
        {
            if (m.getFrom_id() == -3) continue;
            if (m.getTo_id()   == -3) continue;

            setPorte.add(m.getFrom_id());
            setPorte.add(m.getTo_id  ());
        }
        lstPorte = new ArrayList<>(setPorte);
        Collections.sort(lstPorte);
        this.matrixGraph = new Record[lstPorte.size()][lstPorte.size()];
        for (int lig = 0 ; lig < matrixGraph.length ; lig ++)
        {
            for (int col = 0; col < matrixGraph.length ; col ++)
            {
                matrixGraph[lig][col] = new Record();
            }
        }
        int indexMatrix = 0;
        mapPorteAndIndexOfMatrice = new HashMap<Integer, Integer>();
        for (Integer porte : lstPorte)
        {
            mapPorteAndIndexOfMatrice.put(porte,indexMatrix++);
        }
        for (Move m : lstMoves)
        {
            if (m.getFrom_id() == -3) continue;
            if (m.getTo_id()   == -3) continue;
            int portA = mapPorteAndIndexOfMatrice.get(m.getFrom_id());
            int portB = mapPorteAndIndexOfMatrice.get(m.getTo_id());
            if ( portA ==  portB) continue;

            matrixGraph[portA][portB].nbOfMove ++;
            matrixGraph[portA][portB].nbOfDay += Date.between(m.getDepart(), m.getArrival());

            matrixGraph[portB][portA].nbOfMove ++;
            matrixGraph[portB][portA].nbOfDay += Date.between(m.getDepart(), m.getArrival());

        }
    }
    public ArrayList<Move> generateNbMoves (int nbMove,Integer porteDeb,Integer porteFin, Date dateDeb,Date dateFin)
    {
        ArrayList<Move> lstRet = new ArrayList<>();
        do 
        {
            lstRet = generateMoves(porteDeb, porteFin, dateDeb, dateFin);
        }
        while (lstRet.size() != nbMove);
        return lstRet;
    }
    public ArrayList<Move> generateMoves (Integer porteDeb,Integer porteFin, Date dateDeb,Date dateFin)
    {
        //TODO list Moves
        ArrayList<Integer> chemin = generateCheminWithPorteFin(porteDeb, porteFin,Date.between(dateDeb, dateFin));
        ArrayList<Move>    lstMove= new ArrayList<>();
        //The final porte is  the porteFin
        Date currentDate = dateDeb;
        for (int i = 0; i< chemin.size() -1; i ++)
        {
            int portA, portB;
            int averageDuration;
            Move move;
            portA = chemin.get(i);
            portB = chemin.get(i+1);
            averageDuration = matrixGraph[mapPorteAndIndexOfMatrice.get(portA)][mapPorteAndIndexOfMatrice.get(portB)].getAverageDuration();
            move = new Move (currentDate , Date.getNextDate(currentDate, averageDuration),portA,portB);
            lstMove.add(move);
            currentDate = Date.getNextDate(currentDate, averageDuration);
        }

        return lstMove;
    }
    private ArrayList<Integer> generateCheminWithPorteFin ( Integer porteDeb,Integer porteFin,long periode)
    {
        //An array including porteDeb
        
        ArrayList<Integer> lstRet = new ArrayList<>();
        boolean valide = false;
        long currentPeriode = 0;
        while (!valide)
        {
            lstRet.clear();
            currentPeriode = 0;
            int currentPorte = porteDeb;
            lstRet.add (currentPorte);

            while (currentPorte != porteFin)
            {
                int porteNext = nextPorte (currentPorte);
                currentPeriode += matrixGraph[mapPorteAndIndexOfMatrice.get(currentPorte)][mapPorteAndIndexOfMatrice.get(porteNext)].getAverageDuration();
                currentPorte = porteNext;
                lstRet.add (currentPorte);
            }
            if (currentPeriode <periode && currentPeriode >= periode - NB_OF_DAYS) valide = true;
        }
        System.out.println("Periode :" + periode);
        System.out.println("Current Periode :" + currentPeriode);

        return lstRet;
    }
    private ArrayList<Integer> generateChemin (int nbOfStep, Integer porteDeb)
    {
        // generateChemin (a,b) will return an array of a elements INCLUDING porte b
        ArrayList<Integer> lstRet = new ArrayList<>(nbOfStep);
        int currentPorte = porteDeb;
        lstRet.add (currentPorte);

        for (int i = 0; i< nbOfStep -1; i++)
        {
            currentPorte = nextPorte(currentPorte);
            lstRet.add (currentPorte);
        }
        return lstRet;
    }
    private int nextPorte (Integer porteDeb)
    {
        Record[] lstProbability = matrixGraph[mapPorteAndIndexOfMatrice.get(porteDeb)]; // time complexity is 1, better than O(n) of indexOf

        int totalProb = 0;
        int porteRet = UNKNOWN_VALUE;
        for (Record rec : lstProbability )
        {
            totalProb += rec.nbOfMove;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(totalProb); // 0 <= the randomNumber < a
        int sum = 0;
        for (int i = 0; i< lstProbability.length ; i ++)
        {
            if (sum<= randomNumber && randomNumber < sum + lstProbability[i].nbOfMove)
            {
                porteRet =  lstPorte.get(i);
                break;
            }
            sum += lstProbability[i].nbOfMove;
        }
        return porteRet;
    }
    @Override
    public String toString() {
        String ret = "";
        for (int porte : lstPorte)
        {
            ret += String.format("%10d",porte);
        }
        ret += "\n";
        int lig = 0;
        for (int porte : lstPorte)
        {
            ret += String.format("%5d",porte);
            if (matrixGraph[lig][0].nbOfMove != 0)
                ret += String.format("\u001B[31m%5s\u001B[0m",matrixGraph[lig][0].nbOfMove+","+matrixGraph[lig][0].getAverageDuration());
            else
                ret += String.format("%5s",matrixGraph[lig][0].nbOfMove+","+matrixGraph[lig][0].getAverageDuration());

            for (int col = 1;col < matrixGraph[lig].length;col ++)
            {
                if (matrixGraph[lig][col].nbOfMove != 0)
                    ret += String.format ("\u001B[31m%10s\u001B[0m",matrixGraph[lig][col].nbOfMove+","+matrixGraph[lig][col].getAverageDuration());
                else 
                    ret += String.format ("%10s",matrixGraph[lig][col].nbOfMove+","+matrixGraph[lig][col].getAverageDuration());
            }
            ret += "\n";
            lig ++;
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("./dataUniq/1977.csv");
        HashMap<Navire,LinkedList<Move>> mapNavireVsListmove;
        mapNavireVsListmove = model.getMapNavireVsListmove();
        ArrayList<Navire> lstNavire = new ArrayList<>(mapNavireVsListmove.keySet());
        Navire navExample = lstNavire.get(0);
        HistoryGraph historyGraph = new HistoryGraph(mapNavireVsListmove.get(navExample));
        System.out.println("Graph of Navire:" +navExample + " in 1977.csv");
        System.out.println (historyGraph);
        for (Move m : historyGraph.generateNbMoves(17,2907, 369, new Date("22/9/1977"), new Date("17/11/1977")))
        {
            System.out.println(m);
        }
    }
    class Record {
        int nbOfMove;
        int nbOfDay;
        int averageDuration;
        public Record()
        {
            this.nbOfMove =this.nbOfDay= this.averageDuration = 0;
        }
        public int getAverageDuration ()
        {
            if (nbOfMove == 0) return 0;
            return this.nbOfDay/this.nbOfMove;
        }
        
    }
}
