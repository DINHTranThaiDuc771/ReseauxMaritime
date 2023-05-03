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
public class WriteDGSFile {
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
        HashSet<EdgeNav> setEdge = new HashSet<EdgeNav>();
        HashSet<Navire> setNavire = new HashSet<Navire>();
        ArrayList<Date> lstDate = model.getLstStepVsDate();

        int step = 0;
        for (Date date : lstDate) {
            // writerTest.write("st " + (step++) + "\n");
            writer.write("st " + (step++) + "\n");
            writer.write("#Date " + date + "\n");
            /*-------------------------------------- */
            /*----------- Construire setEdgeNew-------*/
            /*-------------------------------------- */

            System.out.println("Construire setEdgeNew");
            HashSet<EdgeNav> setEdgeNew = new HashSet<EdgeNav>();
            HashMap<Integer, HashSet<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire(date.toString());
            for (Integer port : mapPorteAvecNavire.keySet()) {
                ArrayList<Navire> lstNavireDePorteTraite = new ArrayList<Navire>(mapPorteAvecNavire.get(port));
                if (port == -2)
                    continue;
                if (port == -1)
                    continue;

                for (int i = 0; i < lstNavireDePorteTraite.size(); i++) {
                    for (int j = i + 1; j < lstNavireDePorteTraite.size(); j++) {
                        EdgeNav edgeTraite = new EdgeNav(lstNavireDePorteTraite.get(i), lstNavireDePorteTraite.get(j));
                        setEdgeNew.add(edgeTraite);
                    }
                }
            }
            /*-------------------------------------- */
            /*----------- remove Edge-----------------*/
            /*-------------------------------------- */
            HashSet<EdgeNav> setEdgeToRemove = new HashSet<EdgeNav>(setEdge);
            setEdgeToRemove.removeAll(setEdgeNew);
            for (EdgeNav edge : setEdgeToRemove) {
                if (setEdge.remove(edge)) {
                    String idI = "n" + edge.getNavA().toString();
                    String idJ = "n" + edge.getNavB().toString();
                    //TODO ce idI idJ present="false"
                    writer.write("de " + idI + idJ + "\n");
                }
            }
            System.out.println("Finish Constructing setEdgeNew");
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
                int port = mapNavireVsPorte.get(nav)[0];
                if (setNavire.add(nav)) {
                    // writer.write("an " + "n" + nav +" x="+port+" "+"y="+port+" "+ "\n");
                    writer.write("an " + "n" + nav+"\n");
                    continue;
                }
                if ( ! (setNavire.add(nav)) ) {
                    // writer.write("cn " + "n" + nav +" x="+port+" "+"y="+port+" "+ "\n");
                    continue;
                }                
            }
            System.out.println("Finish Constructing setNavire");
            /*-------------------------------------- */
            /*----------- add Edge-----------------*/
            /*-------------------------------------- */
            HashSet<EdgeNav> setEdgeToAdd = new HashSet<EdgeNav>(setEdgeNew);
            setEdgeToAdd.removeAll(setEdge);
            for (EdgeNav edge : setEdgeToAdd) {
                if (setEdge.add(edge)) {
                    String idI = "n" + edge.getNavA().toString();
                    String idJ = "n" + edge.getNavB().toString();
                    //TODO ce ce idI idJ present="true"
                    writer.write("ae " + idI + idJ + " " + idI + " " + idJ + " present=\"true\"\n");
                }
            }

        }
        writer.close();
        System.out.println("finish");
    }

}

class EdgeNav {
    Navire navA;
    Navire navB;

    public Navire getNavA() {
        return navA;
    }

    public Navire getNavB() {
        return navB;
    }

    public EdgeNav(Navire navA, Navire navB) {
        this.navA = navA;
        this.navB = navB;
    }

    @Override
    public int hashCode() {
        return navA.hashCode() + navB.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof EdgeNav))
            return false;
        EdgeNav edgeNav = (EdgeNav) object;

        return (navA == edgeNav.navA && navB == edgeNav.navB) ||
                (navA == edgeNav.navB && navB == edgeNav.navA);
    }

    public String toString() {
        return navA + "-" + navB;
    }
}
