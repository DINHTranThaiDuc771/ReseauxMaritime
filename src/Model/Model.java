package Model;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

import java.util.Collections;
import java.util.Comparator;

public class Model {
    private HashSet<Navire> setNavire ;
    private HashMap<Navire,LinkedList<Move>> mapNavireVsListmove;
    private HashMap<Navire,ArrayList<Date>> mapNavireVsListDate;//un hashmap qui stock des navires et leurs dates départ et arrival, 
    private TreeMap<Date,Integer>           mapDateVsStep;
    private ArrayList<Date>                 lstStepVsDate;
    /*---------------------------------------------------------------------------------------- */
    /*-------------------------------------Geter----------------------------------------------- */
    /*---------------------------------------------------------------------------------------- */
    public HashMap<Navire, LinkedList<Move>> getMapNavireVsListmove() {
        return mapNavireVsListmove;
    }
    public ArrayList<Date> getLstStepVsDate() {
        return lstStepVsDate;
    }
    public HashSet<Navire> getSetNavire() {
        return setNavire;
    }

    public Model () {
        setNavire           = new HashSet<>();
        mapNavireVsListmove = new HashMap<Navire,LinkedList<Move>>();
        mapNavireVsListDate = new HashMap<Navire,ArrayList<Date>>();
        mapDateVsStep       = new TreeMap<Date,Integer>();
        lstStepVsDate       = new ArrayList<Date>();
    }
    /*
     * Possible Output
     * {-1,-1} -> la date est avant l'historique
     * {-2,-2} -> la date est apres l'historique
     * { a, a} -> le navire est à la porte a
     * { a, b} -> le navire est à la porte b dans la même journée
     * { 0, 0} -> le naivre est en route (null part)
     */
    public int[] positionNavire(Navire navire,Date date)
    {
        int[]            tabPosition        = new int[2];
        int              currentPosition    = 0;
        Move             moveArrete         = null;
        LinkedList<Move> lstMoveNavire      = mapNavireVsListmove.get(navire);
        if (lstMoveNavire.getLast().getArrival().isBefore(date))
        {
            int[] array = {-2,-2};
            return array; //la date est apres l'historique            
        }
        if (lstMoveNavire.getFirst().getDepart().isAfter(date))
        {
            int[] array = {-1,-1};
            return array; //la date est avant l'historique
        }
        for (Move move : lstMoveNavire)
        {
            if (move.getArrival().isAfter(date)) // If two date are equal, isAfter returns false
            {
                moveArrete = move;
                break;
            }
            currentPosition = move.getTo_id();
            moveArrete = move;
        }
        if (date.isAfter(moveArrete.getDepart()) && date.isBefore(moveArrete.getArrival())) 
        {
            currentPosition = 0;//en route
            if (moveArrete.getFrom_id() == -3)
            {
                currentPosition = -3;
            }
        }
    
        if (moveArrete.getDepart().equals(date)  )                                          currentPosition = moveArrete.getFrom_id();
        
        // If le navire départ et arrive dans la même journée
        if (moveArrete.getDepart().equals(date) && moveArrete.getArrival().equals(date))
        {
            tabPosition[0] =    moveArrete.getFrom_id();
            tabPosition[1] =    moveArrete.getTo_id();
            return tabPosition;
        }
        
        tabPosition[0] = currentPosition;
        tabPosition[1] = currentPosition;
        return tabPosition;
    }
    public int[] positionNavire(Navire navire,String dateString)
    {
        return positionNavire(navire, new Date(dateString));
    }
    public int[] positionNavire(Navire navire,String dateString,boolean formatted)
    {
        return positionNavire(navire, new Date(dateString,formatted));
    }
    /*
     * Sort all the LinkedList of Move for all the Navires
     */
    private void sortMove ()
    {
        this.mapNavireVsListmove.forEach((key,value)->
        {
            Collections.sort(value);
        }
        );
    }
    private void sortDate()
    {
        this.mapNavireVsListDate.forEach((key,value)->
        {
            Collections.sort(value);
        });
    }
    public void coherentModel3()
    {
        ArrayList<Navire> lstNavire = new ArrayList<>(mapNavireVsListmove.keySet());
        Collections.sort(lstNavire);
        for (Navire navire : lstNavire)
        {
            LinkedList<Move> lstMoveNavireTraite = mapNavireVsListmove.get(navire); // L'Historique de navigation
            //Use iterator to improve performance
            ListIterator<Move> it=  lstMoveNavireTraite.listIterator(0);
            HistoryGraph historyGraph = new HistoryGraph(lstMoveNavireTraite);
            Move previousMove,currentMove;
            previousMove  = null;
            currentMove   = null;
            if (it.hasNext())
            {
                previousMove = it.next(); //Assigne the first move to previous and advance the cursor
            }             
            //Now the iterator is between the first and second element.
            while (it.hasNext())
            {
                currentMove = it.next();
                Move nextMove = null;
                if (it.hasNext())
                {
                    nextMove = it.next();
                    it.previous();
                }
                //Vérifier les données et le faire cohérent
                List<Move> potentialMoves;
                if (currentMove.getFrom_id() == Move.NULLE_PARTE)
                {


                    Date dateDeb, dateFin;
                    int porteDeb,porteFin;
                    dateDeb = currentMove.getDepart();
                    dateFin = currentMove.getArrival();
                    porteDeb = previousMove.getTo_id();
                    porteFin = nextMove.getFrom_id();
                    it.previous();
                    it.remove(); 
                    //Remove print     
                    System.out.println (String.format("%-30s","Navire current "+ navire +" ")+currentMove);
                    System.out.println (String.format("%-30s","Navire previous "+ navire +" ")+previousMove);
                    System.out.println (String.format("%-30s","Navire next "+ navire +" ")+nextMove);
                    potentialMoves = historyGraph.generateMoves(porteDeb, porteFin, dateDeb, dateFin);
                    System.out.println ("Finish generate");

                    for (Move move: potentialMoves)
                    {
                        //Iterator illustration
                        /*
                         * Previous  -3   Next
                         *              ^ 
                         * After delete
                         * Preious   Next
                         *         ^
                         * After add
                         * Prevous   moveAdd   Next
                         *                   ^
                         */
                        it.add(move);

                    }
                    // System.out.println( potentialMoves);

                }

                //Update previous var
                previousMove = currentMove;
            }
        }
    }
    public void coherentModel2()
    {
        for (Navire navire : mapNavireVsListmove.keySet())
        {
            LinkedList<Move> lstMoveNavireTraite = mapNavireVsListmove.get(navire); // L'Historique de navigation
            //Use iterator to improve performance
            ListIterator<Move> it=  lstMoveNavireTraite.listIterator(0);
            Move previousMove,currentMove;
            previousMove  = null;
            currentMove   = null;
            if (it.hasNext())
            {
                previousMove = it.next(); //Assigne the first move to previous and advance the cursor
            }             
            //Now the iterator is between the first and second element.
            while (it.hasNext())
            {
                currentMove = it.next();
                //Vérifier les données et le faire cohérent
                List<Move> potentialMoves = Move.checkCoherent2(previousMove,currentMove);
                if (potentialMoves != null  )
                {
                    for (Move move: potentialMoves)
                    {
                        it.previous();
                        it.add(move);
                        it.next();
                    }
                }
                //Update previous var
                previousMove = currentMove;
            }
        }
    }
    public void coherentModel(){
        for (Navire navire : mapNavireVsListmove.keySet())
        {
            LinkedList<Move> lstMoveNavireTraite = mapNavireVsListmove.get(navire); // L'Historique de navigation
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
    /*
     * return Navire-> liste des positions (ports) dans une date
     */
    public HashMap<Navire,int[]> getNavireAvecPorte(String date)
    {
        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(this.mapNavireVsListmove); // sort hashMap
        HashMap<Navire,int[]> mapNavireAvecPorte = new  HashMap<Navire,int[]>();
        for (Navire nav : sorted.keySet())
        {
            mapNavireAvecPorte.put(nav, positionNavire(nav, date,true));
        }
        return mapNavireAvecPorte;
        
    }
    /*
     * return Porte -> List des navire dans une date
     * Cet HashMap sert à construire un graph d'une date
     * 
     */
    public HashMap<Integer, HashSet<Navire>> getPorteAvecNavire(String date) {
        HashMap<Integer, HashSet<Navire>> mapPorteAvecNavire = new HashMap<>();
        HashMap<Navire, int[]> mapNavireAvecPorte = this.getNavireAvecPorte(date);
    
        for (Entry<Navire, int[]> entry : mapNavireAvecPorte.entrySet()) {
            Navire navire = entry.getKey();
            int porteA = entry.getValue()[0];
            int porteB = entry.getValue()[1];
            /*
             * computeIfAbsent(porteA, k -> new ArrayList<Navire>())
             * if absente porteA, then create a new key porteA and associate it with a new Array
             * If present porteA, it simply return the value of key porteA
             */
            mapPorteAvecNavire.computeIfAbsent(porteA, k -> new HashSet<Navire>()).add(navire);
            mapPorteAvecNavire.computeIfAbsent(porteB, k -> new HashSet<Navire>()).add(navire);

        }
    
        return mapPorteAvecNavire;
    }
    public void chargerListDateVsStep(String path) throws FileNotFoundException
    {
        String              fichier     = path;
        FileInputStream     inputStream = new FileInputStream(fichier);
        Scanner             scanner     = new Scanner(inputStream,"UTF-8");
        HashMap<Date,Integer> mapDateStepTemp = new HashMap<Date,Integer> ();
        //Ignore the first and second line
        scanner.nextLine();
        scanner.nextLine();

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String dataOfLine[]=line.split(" ");
            mapDateStepTemp.put(new Date(dataOfLine[0],true), Integer.parseInt(dataOfLine[1]));
            lstStepVsDate.add(new Date(dataOfLine[0],true));
        }
        this.mapDateVsStep = new TreeMap<Date,Integer>(mapDateStepTemp);
        Collections.sort(this.lstStepVsDate);
        scanner.close();
    }
    public void chargerModel (String path) throws IOException
    {
        System.out.println("Charging");
        String              fichier     = path;
        FileInputStream     inputStream = new FileInputStream(fichier);
        Scanner             scanner     = new Scanner(inputStream,"UTF-8");
        HashSet<Date> setDate = new HashSet<Date>();

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String dataOfLine[] = line.split(";");
            if (dataOfLine[2].equals("year")) continue; //There are 'HEADERS' inside the allMoves.csv
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
                this.mapNavireVsListDate        .put(navireTraite, new ArrayList<Date>());
                this.mapNavireVsListmove  .put(navireTraite,new LinkedList<Move>());
                Move moveTraite = new Move(dataOfLine[4], dataOfLine[5], Integer.parseInt(dataOfLine[6]), Integer.parseInt(dataOfLine[7]) );
                this.mapNavireVsListmove   .get(navireTraite).add(moveTraite);
                Date dateDepart = new Date(dataOfLine[4]);
                Date dateArrival= new Date(dataOfLine[5]);
                setDate.add(dateArrival);
                setDate.add(dateDepart);
                if (!(this.mapNavireVsListDate.get(navireTraite).contains(dateDepart)))this.mapNavireVsListDate.get(navireTraite).add(dateDepart);
                if (!(this.mapNavireVsListDate.get(navireTraite).contains(dateArrival)))this.mapNavireVsListDate.get(navireTraite).add(dateArrival);

            }
            else // if navire Traité is already in the set
            {
                Move moveTraite = new Move(dataOfLine[4], dataOfLine[5], Integer.parseInt(dataOfLine[6]), Integer.parseInt(dataOfLine[7]) );
                this.mapNavireVsListmove.get(navireTraite).add(moveTraite);
                Date dateDepart = new Date(dataOfLine[4]);
                Date dateArrival= new Date(dataOfLine[5]);
                setDate.add(dateArrival);
                setDate.add(dateDepart);
                if (!(this.mapNavireVsListDate.get(navireTraite).contains(dateDepart)))this.mapNavireVsListDate.get(navireTraite).add(dateDepart);
                if (!(this.mapNavireVsListDate.get(navireTraite).contains(dateArrival)))this.mapNavireVsListDate.get(navireTraite).add(dateArrival);

            }
        }
        System.out.println("Finish Charging");
        scanner.close();
        this.sortMove();
        System.out.println("Finish sortMove");
        this.sortDate();
        System.out.println("Finish sortDate");
        this.coherentModel2();
        System.out.println("Finish coherent2");

        this.coherentModel3();
        System.out.println("Finish coherent3");
        this.lstStepVsDate = new ArrayList<>(setDate);
        Collections.sort(lstStepVsDate);
        System.out.println("Finish charger listDate");

    }

    /*
     * Affichage
     * 
     */
    public void afficherPostionDesNavires(String date)
    {
        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(this.mapNavireVsListmove); // sort hashMap

        for (Navire nav : sorted.keySet())
        {
            System.out.println(String.format("%-20s","Navire "+nav)+" :"+String.format("%-20s",Arrays.toString(this.positionNavire(nav, date))));
        }
    }
    
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        if (args.length > 0 ) model.chargerModel(args[0]);
        else model.chargerModel("./dataUniq/1977.csv");
        model.chargerListDateVsStep("./tmp/dates_vs_step");
        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(model.mapNavireVsListmove); // sort hashMap
        System.out.println("Writing files");

        sorted.forEach(
            (key,value) -> {
                try {
                    FileWriter writer = new FileWriter("./dataHistorique/nav"+key+".txt",true);
                    writer.write("Navire: "+ key+"\n");
                    for (Move move : value) {
                        String line;
                        // line   = String.format("%6d",model.mapDateVsStep.get(move.getDepart()));
                        // line  += String.format("%6d",model.mapDateVsStep.get(move.getArrival()));
                        // line  += String.format("%8d",move.getFrom_id());
                        // line  += String.format("%8d",move.getTo_id());
                        line   = model.mapDateVsStep.get(move.getDepart()) + ";";
                        line  += model.mapDateVsStep.get(move.getArrival()) + ";";
                        line  += move.getFrom_id() + ";";
                        line  += move.getTo_id() +";";
                        line  += move.toString();
                        writer.write(line+"\n");
                    }
                    writer.write("\nPosition de Navire "+ key+"\n");
                    for (Date date : model.mapNavireVsListDate.get(key)){
                        writer.write(date.toString()+Arrays.toString(model.positionNavire(key,date))+"\n");                       
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        );


        
    }
    public static void testTerminal(Model model) {
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

        TreeMap<Navire, LinkedList<Move>> sorted            = new TreeMap<>(model.mapNavireVsListmove); // sort hashMap
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
        sorted            = new TreeMap<>(model.mapNavireVsListmove); // sort hashMap
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