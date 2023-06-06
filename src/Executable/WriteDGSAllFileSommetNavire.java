package Executable;

import Model.*;
import Model.Date;

import java.io.*;
import java.util.*;

public class WriteDGSAllFileSommetNavire {
    public static void main(String[] args) throws IOException {
        File folder = new File("./dataUniq");
        File folderAnnee = new File ("./dgs/annee");
        File[] files = folder.listFiles();

        File[] dgsFiles = folderAnnee.listFiles();
        if (dgsFiles != null) {
            for (File file : dgsFiles) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
        if (files == null) {
            System.out.println("No files found in the folder.");
            return;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            String filename = file.getName();
            String baseFileName;
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex == -1) {
                baseFileName = filename;
            }
            baseFileName = filename.substring(0, dotIndex);

            FileWriter writer = new FileWriter("./dgs/annee/" + baseFileName + ".dgs", false);
            writer.write(""); // Delete content of the file
            writer.close();
            writer = new FileWriter("./dgs/annee/" + baseFileName + ".dgs", true);
            // FileWriter writerTest = new FileWriter("test.txt", true);
            Model model = new Model();
            model.chargerModel("./dataUniq/" + filename);

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

                HashSet<EdgeNav> setEdgeNew = new HashSet<EdgeNav>();
                HashMap<Integer, HashSet<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire(date.toString());
                for (Integer port : mapPorteAvecNavire.keySet()) {
                    ArrayList<Navire> lstNavireDePorteTraite = new ArrayList<Navire>(mapPorteAvecNavire.get(port));
                    if (port == -2) // Apres la date
                        continue;
                    if (port == -1) // Avant la date
                        continue;
                    if (port == 0) // En route
                        continue;
                    if (port == -3) // Nulle Parte
                        continue;
                    for (int i = 0; i < lstNavireDePorteTraite.size(); i++) {
                        for (int j = i + 1; j < lstNavireDePorteTraite.size(); j++) {
                            EdgeNav edgeTraite = new EdgeNav(lstNavireDePorteTraite.get(i),
                                    lstNavireDePorteTraite.get(j));
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
                        // TODO ce idI idJ present="false"
                        writer.write("de " + idI + idJ + "\n");
                    }
                }
                /*-------------------------------------- */
                /*----------- Construire setNavire-------*/
                /*-------------------------------------- */

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
                    // TODO change color depending on the port
                    int port = mapNavireVsPorte.get(nav)[0];
                    if (setNavire.add(nav)) {
                        // writer.write("an " + "n" + nav +" x="+port+" "+"y="+port+" "+ "\n");
                        writer.write("an " + "n" + nav + " label=" + nav + " \n");
                        continue;
                    }
                    if (!(setNavire.add(nav))) {
                        // writer.write("cn " + "n" + nav +" x="+port+" "+"y="+port+" "+ "\n");
                        if (port == -3) {
                            // BLUE
                            writer.write("cn " + "n" + nav + " ui.style=\"fill-color: rgb(0,100,255);\"\n");
                            continue;
                        }
                        if (port == 0) {
                            // Green
                            writer.write("cn " + "n" + nav + " ui.style=\"fill-color: rgb(124,252,0);\"\n");
                            continue;
                        }
                        // BLACK, back to normal
                        writer.write("cn " + "n" + nav + " ui.style=\"fill-color: rgb(0,0,0);\"\n");

                        continue;
                    }
                }
                /*-------------------------------------- */
                /*----------- add Edge-----------------*/
                /*-------------------------------------- */
                HashSet<EdgeNav> setEdgeToAdd = new HashSet<EdgeNav>(setEdgeNew);
                setEdgeToAdd.removeAll(setEdge);
                for (EdgeNav edge : setEdgeToAdd) {
                    if (setEdge.add(edge)) {
                        String idI = "n" + edge.getNavA().toString();
                        String idJ = "n" + edge.getNavB().toString();
                        writer.write("ae " + idI + idJ + " " + idI + " " + idJ + "\n");
                    }
                }

            }
            writer.close();
            System.out.println("finish");

        }

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
