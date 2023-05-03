package Executable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import Model.Date;
import Model.Model;
import Model.Navire;

/*
 * Run getDate.sh before execute this
 */
public class WriteDGSFileVer2 {
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("graphDynamic.dgs", true);
        // FileWriter writerTest = new FileWriter("test.txt", true);
        Model model = new Model();
        model.chargerModel("./testData/testDGS.csv");
        model.chargerListDateVsStep("./tmp/dates_vs_step");
        // Write 2 first line
        writer.write("DGS004\n");
        writer.write("null 0 0\n");
        // Chaque Date (step) setEdge et setNavire vont être modifiés
        HashSet<Navire> setNavire = new HashSet<Navire>();
        HashSet<Integer> setPort = new HashSet<Integer>();
        ArrayList<Date> lstDate = model.getLstStepVsDate();

        int step = 0;
        for (Date date : lstDate) {
            // writerTest.write("st " + (step++) + "\n");
            writer.write("st " + (step++) + "\n");
            writer.write("#Date " + date + "\n");
            /*-------------------------------------- */
            /*----------- Construire setPort---------*/
            /*-------------------------------------- */
            HashMap<Integer, HashSet<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire(date.toString());
            for (Integer port : mapPorteAvecNavire.keySet()) {
                ArrayList<Navire> lstNavireDePorteTraite = new ArrayList<Navire>(mapPorteAvecNavire.get(port));
                if (port == -2)
                    continue;
                if (port == -1)
                    continue;
                if(setPort.add(port))
                {
                    writer.write("an p"+port+" ui.style=\"fill-color: rgb(0,100,255);\"\n");
                }
            }

            /*-------------------------------------- */
            /*----------- Construire setNavire-------*/
            /*-------------------------------------- */
            System.out.println("Construire setNavire");

            HashMap<Navire, int[]> mapNavireVsPorte = model.getNavireAvecPorte(date.toString());
            int[] avantHistorique = { -1, -1 };
            int[] apresHistorique = { -2, -2 };
            for (Navire nav : mapNavireVsPorte.keySet()) {
                int[] positionNavireTraite = mapNavireVsPorte.get(nav);
                if (Arrays.equals(positionNavireTraite, avantHistorique))
                    continue;

                if (Arrays.equals(positionNavireTraite, apresHistorique)) {
                    if (setNavire.remove(nav)) {
                        writer.write("dn " + "n" + nav + "\n");
                    }
                    continue;
                }
                //TODO change color depending on the port
                int port1,port2;
                port1 = mapNavireVsPorte.get(nav)[0];
                port2 = mapNavireVsPorte.get(nav)[1];

                if (setNavire.add(nav)) {
                    writer.write("an " + "n" + nav+"\n");
                    writer.write("ae "+ ("n"+nav) +("p"+port1)+" "+("n"+nav)+" "+("p"+port1)+"\n");
                    //TODO Port2
                    if (port2!= port1)
                        // writer.write("ae "+ ("n"+nav) +("p"+port2)+" "+("n"+nav)+" "+("p"+port2)+"\n");
                    continue;
                }
                if ( ! (setNavire.add(nav)) ) {
                    writer.write("dn " + "n" + nav+"\n");
                    writer.write("an " + "n" + nav+"\n");
                    writer.write("ae "+ ("n"+nav) +("p"+port1)+" "+("n"+nav)+" "+("p"+port1)+"\n");
                    //TODO Port2
                    if (port2!= port1)
                        // writer.write("ae "+ ("n"+nav) +("p"+port2)+" "+("n"+nav)+" "+("p"+port2)+"\n");
                    continue;
                }                
            }
            System.out.println("Finish Constructing setNavire");


        }
        writer.close();
        System.out.println("finish");
    }

}

