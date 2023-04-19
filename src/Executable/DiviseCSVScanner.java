package Executable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DiviseCSVScanner {
    public static void main(String[] args) throws IOException {
        String              fichier     = "./testData/testMoves8000.csv";
        FileInputStream     inputStream = new FileInputStream(fichier);
        Scanner             scanner     = new Scanner(inputStream,"UTF-8");
        // Ignore the first line
        int currentYear = 1977;
        FileWriter writer = new FileWriter("data/"+currentYear+".csv", true);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String dataOfLine[] = line.split(";");
            if (dataOfLine[2].equals("year")) continue; //There are 'HEADERS' inside the allMoves.csv
            /*
            dataOfLine[2] Year 
            */
            int year = Integer.parseInt(dataOfLine[2]);
            if (currentYear!=year)
            {
                currentYear = year;
                writer.close(); // Have to close correctlly, or else there will be problem
                writer = new FileWriter("data/"+currentYear+".csv", true);
                writer.write(line+"\n");
            }
            writer.write(line+"\n");

        }
        writer.close();
        scanner.close();    
    }

}
