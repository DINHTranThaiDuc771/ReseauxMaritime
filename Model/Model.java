package Model;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class Model {
    private HashSet<Navire> setNavire ;
    private HashMap<Navire,LinkedList<Move>> mapHistoireNavire;
    
    public Model () {
        setNavire           = new HashSet<>();
        mapHistoireNavire   = new HashMap<Navire,LinkedList<Move>>();
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
    
    public static void main(String[] args) throws IOException {
        Model               model       = new Model();
        model.chargerModel("testMoves.csv");
        ArrayList<Navire> listNavire = new ArrayList<Navire>(model.setNavire);
        Collections.sort(listNavire, new Comparator<Navire>() {
            @Override
            public int compare (Navire navireA, Navire navireB) {
                return navireA.compareTo(navireB);
            }
        });
        System.out.println(listNavire);
        System.out.println(model.setNavire.add (new Navire(37516)));
        model.mapHistoireNavire.forEach(
            (key,value) -> {
                System.out.println("Navire: "+ key);
                for (Move move : value) {
                    System.out.println(move);
                }
            }
        );

        System.out.println("--------------------------Test Cohérent-----------------------------------------------");

        model.coherentModel();
        model.mapHistoireNavire.forEach(
            (key,value) -> {
                System.out.println("Navire: "+ key);
                for (Move move : value) {
                    System.out.println(move);
                }
            }
        );        
    }
}