package Executable;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import Model.Date;
import Model.Model;

public class ConstruireEtape 
{
    private static ArrayList<Date> lstDate = new ArrayList<Date>();
    public static void main(String[] args) throws IOException {
        String path = "./testData/allMoves.csv";
        FileInputStream  inputStream  = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream,"UTF-8");
        FileWriter writer = new FileWriter("./tmp/dateOrdonne", true);
        System.out.println("Reading file");
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String dataOfLine[] = line.split(";");
            if (dataOfLine[2].equals("year")) continue; //There are 'HEADERS' inside the allMoves.csv
            Date dateDepart = new Date(dataOfLine[4]);
            //dataOfLine[4] = departure : date de départ du port (from_id)*
            if (!(lstDate.contains(dateDepart))) lstDate.add(dateDepart);
        }

        System.out.println("Finish reading file,start Sorting");
        Collections.sort(lstDate);
        System.out.println("Finish sorting, start Writing");

        for (Date date : lstDate)
        {
            writer.write(date+"\n");
        }
        System.out.println("Finish Writing");

    }    
}
