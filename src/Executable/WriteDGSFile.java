package Executable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        model.chargerModel("./testData/testMoves.csv");
        model.chargerListDateVsStep("./tmp/dates_vs_step");
        // Write 2 first line
        writer.write("DGS004\n");
        writer.write("null 0 0\n");
        HashSet<EdgeNav> setEdge = new HashSet<EdgeNav>();
        // HashSet<Navire> setNavire = model.getSetNavire();
        HashSet<Navire> setNavire = new HashSet<Navire>();
        ArrayList<Date> lstDate = model.getLstStepVsDate();
        /*-------------------------------------- */
        /*-----------------Add Node------------- */
        /*-------------------------------------- */
        // an A
        // for (Navire nav : setNavire) {
        //     // int[] positionNavire = model.positionNavire(nav, date);
        //     // int[] arraysAvantHistorique = { -1, -1 };
        //     // if (Arrays.equals(arraysAvantHistorique, positionNavire)) {
        //     //     // TODO Isolate navires by changing color in green;
        //     // }
        //     String idnav = "n" + nav.toString();
        //     writer.write("an " + idnav + "\n");
        // }
        int step = 0;
        for (Date date : lstDate) {
            // writerTest.write("st " + (step++) + "\n");
            writer.write("st " + (step++) + "\n");
            writer.write("#Date " + date + "\n");
            /*-------------------------------------- */
            /*----------- retirer les arêtes---------*/
            /*-------------------------------------- */
            if (!(setEdge.isEmpty())) {
                for (EdgeNav edge : setEdge) {
                    String idI = "n" + edge.getNavA().toString();
                    String idJ = "n" + edge.getNavB().toString();
                    writer.write("de " + idI + idJ + "\n");
                }
            }
            setEdge.clear();
            /*-------------------------------------- */
            /*----------- retirer les nodes---------*/
            /*-------------------------------------- */
            if (!(setNavire.isEmpty())) {
                for (Navire navire : setNavire) {
                    String idNavire = "n"+navire.toString();
                    writer.write("dn " + idNavire+"\n");
                }
            }
            setNavire.clear();
            /*-------------------------------------- */
            /*-----------------Add edges------------ */
            /*-------------------------------------- */
            // ae AB A B = add edge [id] nodeA nodeB
            HashMap<Integer, HashSet<Navire>> mapPorteNavire = model.getPorteAvecNavire(date.toString());
            for (Integer port : mapPorteNavire.keySet()) {
                writer.write("#Port " + port + "\n");
                for (int i = 0; i < mapPorteNavire.get(port).size(); i++) {
                    ArrayList<Navire> lstNavire = new ArrayList<>(mapPorteNavire.get(port));
                    for (int j = i + 1; j < mapPorteNavire.get(port).size(); j++) {
                        EdgeNav edgeTraite = new EdgeNav(lstNavire.get(i), lstNavire.get(j));
                        if (setEdge.add(edgeTraite)) 
                        {
                            String idI = "n" + edgeTraite.getNavA().toString();
                            String idJ = "n" + edgeTraite.getNavB().toString();
                            if (setNavire.add(edgeTraite.getNavA()))
                                writer.write("an "+"n"+edgeTraite.getNavA()+"\n");
                            if (setNavire.add(edgeTraite.getNavB()))
                                writer.write("an "+"n"+edgeTraite.getNavB()+"\n");

                            writer.write("ae " + idI + idJ + " " + idI + " " + idJ + "\n");
                            // writerTest.write(edgeTraite+"\n");
                        }
                    }
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
        return navA.hashCode() +navB.hashCode();
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
    public String toString()
    {
        return navA+"-"+navB;
    }
}
