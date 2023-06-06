package Executable;

import java.io.*;
import java.util.*;

public class WriteDGSSommetPort {
    public static void main(String[] args) throws IOException {
        File folder = new File("./dataHistorique");
        File folderNavHistorique = new File("./dgs/navHistorique");

        File[] files = folder.listFiles();
        File[] dgsFiles = folderNavHistorique.listFiles();
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
            FileWriter writer = new FileWriter("./dgs/navHistorique/" + baseFileName + ".dgs", false);
            writer.write(""); // Delete content of the file
            writer.close();
            writer = new FileWriter("./dgs/navHistorique/" + baseFileName + ".dgs", true);
            writer.write("DGS004\n");
            writer.write("null 0 0\n");

            FileInputStream inputStream = new FileInputStream("./dataHistorique/" + filename);
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            /*
             * Navire: 46
             * 19;23;1600;219;02/03/1977;06/03/1977;1600;219;
             * 28;38;-3;-3;11/03/1977;21/03/1977;-3;-3;
             * 45;45;4246;1697;28/03/1977;28/03/1977;4246;1697;
             * 46;47;1697;4492;29/03/1977;30/03/1977;1697;4492;
             * 52;75;-3;-3;04/04/1977;31/05/1977;-3;-3;
             * 82;83;4246;4492;07/06/1977;08/06/1977;4246;4492;
             * 88;89;4492;2993;13/06/1977;14/06/1977;4492;2993;
             * 89;90;2993;830;14/06/1977;15/06/1977;2993;830;
             * 91;95;830;889;16/06/1977;20/06/1977;830;889;
             * 100;130;-3;-3;25/06/1977;24/08/1977;-3;-3;
             * 137;141;830;889;31/08/1977;04/09/1977;830;889;
             * 141;142;889;5521;04/09/1977;05/09/1977;889;5521;
             * 147;155;-3;-3;10/09/1977;18/09/1977;-3;-3;
             * 162;166;219;1600;25/09/1977;29/09/1977;219;1600;
             * 171;194;-3;-3;04/10/1977;25/11/1977;-3;-3;
             * 201;202;889;5521;02/12/1977;03/12/1977;889;5521;
             * 207;216;-3;-3;08/12/1977;17/12/1977;-3;-3;
             * 223;228;219;2849;24/12/1977;29/12/1977;219;2849;
             * 
             */
            HashSet<Integer> setPorte = new HashSet<>();
            HashSet<EdgePorte> setEdge = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String dataOfLine[] = line.split(";");
                if (dataOfLine.length == 1)//Ignore lines that is not a data Line
                    continue;
                int from, to;
                from = Integer.parseInt(dataOfLine[2]);
                to = Integer.parseInt(dataOfLine[3]);
                if (from == -3)
                    continue;
                setPorte.add(from);
                setPorte.add(to);
                setEdge.add(new EdgePorte(from, to));
            }
            for (int porte : setPorte) {
                writer.write("an " + porte +" label="+porte+ "\n");
            }
            for (EdgePorte edge : setEdge) {
                int from, to;
                from = edge.getFrom();
                to = edge.getTo();
                writer.write("ae " + from + "-" + to + " " + from + " > " + to + "\n");
            }
            writer.close();
            scanner.close();

        }
        System.out.println("Finish");
    }

}

class EdgePorte {
    private int from;
    private int to;

    public EdgePorte(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        return from + 9 * to;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof EdgePorte))
            return false;
        EdgePorte edge = (EdgePorte) object;

        return (from == edge.from && to == edge.to);
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
