package Model;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;

public class Model {
    private HashSet<Navire> setNavire ;
    private HashMap<Navire,LinkedList<Move>> mapHistoireNavire;
    
    public Model () {
        setNavire           = new HashSet<>();
        mapHistoireNavire   = new HashMap<Navire,LinkedList<Move>>();
    }

    public int[] positionNavire(Navire navire,String dateString)
    {
        int[]            tabPosition        = new int[2];
        int              currentPosition    = 0;
        Move             moveArrete         = null;
        Date             date               = new Date(dateString);
        LinkedList<Move> lstMoveNavire      = mapHistoireNavire.get(navire);

        if (lstMoveNavire.getFirst().getDepart().isAfter(date))
        {
            int[] array = {-1,-1};
            return array; //la date est avant l'historique
        }
        for (Move move : lstMoveNavire)
        {
            moveArrete = move;
            if (move.getArrival().isAfter(date)) // If two date are equal, isAfter returns false
            {
                break;
            }
            currentPosition = moveArrete.getTo_id();
        }
        if (date.isAfter(moveArrete.getDepart()) ) currentPosition = 0;//en route
        if (moveArrete.getDepart().equals(date)  ) currentPosition = moveArrete.getFrom_id();
        if (moveArrete.getDepart().equals(date) && moveArrete.getArrival().equals(date))
        {
            tabPosition[0] =  moveArrete.getFrom_id();
            tabPosition[1] = moveArrete.getTo_id();
            return tabPosition;
        }
        tabPosition[0] = currentPosition;
        tabPosition[1] = -2; // Non défini
        return tabPosition;
    }

    public void coherentModel(){
        for (Navire navire : mapHistoireNavire.keySet())
        {
            LinkedList<Move> lstMoveNavireTraite = mapHistoireNavire.get(navire); // L'Historique de navigation
            //Use iterator to improve performance
            ListIterator<Move> it=  lstMoveNavireTraite.listIterator(0);
            Move previousMove,currentMove;
            previousMove = currentMove = null;
            if (it.hasNext())
            {
                previousMove = it.next(); //Assigne the first move to previous
            }             
            //Now the iterator is between the first and second element.
            while (it.hasNext())
            {
                currentMove = it.next();
                //Vérifier les données et le faire cohérent
                Move potentialMove = Move.checkCoherent(previousMove,currentMove);
                if (potentialMove != null)
                {
                    it.previous();
                    it.add(potentialMove);
                    it.next();
                }
                //Update previous var
                previousMove = currentMove;
            }
        }
    }
    public void chargerModel (String path) throws IOException
    {
        String              fichier     = path;
        FileInputStream     inputStream = new FileInputStream(fichier);
        Scanner             scanner     = new Scanner(inputStream,"UTF-8");
        // Ignore the first line
        scanner.nextLine(); 
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String dataOfLine[] = line.split(";");
            /*
            dataOfLine[4] = departure : date de départ du port (from_id)
            dataOfLine[5] = arrival : date d'arrivée au port (to_id)
            dataOfLine[6] = from_id : port de départ
            dataOfLine[7] = to_id : port d'arrivée
            dataOfLine[12] = vessel_id : identifiant du navire
            */
            Navire navireTraite = new Navire (Integer.parseInt(dataOfLine[12]));
            if (this.setNavire.add (navireTraite) ) // if navire Traité is not in the set
            {
                this.mapHistoireNavire.put(navireTraite,new LinkedList<Move>());
                Move moveTraite = new Move(dataOfLine[4], dataOfLine[5], Integer.parseInt(dataOfLine[6]), Integer.parseInt(dataOfLine[7]) );
                this.mapHistoireNavire.get(navireTraite).add(moveTraite);
            }
            else // if navire Traité is already in the set
            {
                Move moveTraite = new Move(dataOfLine[4], dataOfLine[5], Integer.parseInt(dataOfLine[6]), Integer.parseInt(dataOfLine[7]) );
                this.mapHistoireNavire.get(navireTraite).add(moveTraite);
            }
        }
        scanner.close();
    }

    /*
     * Affichage
     * 
     *
     */
    public void afficherPostionDesNavires(String date)
    {
        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(this.mapHistoireNavire); // sort hashMap

        for (Navire nav : sorted.keySet())
        {
            System.out.println(String.format("%-20s","Navire "+nav)+" :"+String.format("%-20s",Arrays.toString(this.positionNavire(nav, date))));
        }
    }
    
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("testMoves.csv");

        System.out.println("---------------------------------------Test l'ensemble Navires-----------------------------------------------");
        ArrayList<Navire> listNavire = new ArrayList<Navire>(model.setNavire);
        Collections.sort(listNavire, new Comparator<Navire>() {
            @Override
            public int compare (Navire navireA, Navire navireB) {
                return navireA.compareTo(navireB);
            }
        });
        System.out.println(listNavire);
        System.out.println(model.setNavire.add (new Navire(37516)));
        
        System.out.println("---------------------------------------Test Map Histoire-----------------------------------------------");

        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(model.mapHistoireNavire); // sort hashMap
        sorted.forEach(
            (key,value) -> {
                System.out.println("Navire: "+ key);
                for (Move move : value) {
                    System.out.println(move);
                }
            }
        );
        System.out.println("---------------------------------------Test Cohérent-----------------------------------------------");
        model.coherentModel();
        sorted            = new TreeMap<>(model.mapHistoireNavire); // sort hashMap
        sorted.forEach(
            (key,value) -> {
                System.out.println("Navire: "+ key);
                for (Move move : value) {
                    System.out.println(move);
                }
            }
        );
        System.out.println("---------------------------------------Test Position-----------------------------------------------");
        System.out.println("Date 05/03/1977 00:00");
        model.afficherPostionDesNavires("05/03/1977 00:00");
    }
}