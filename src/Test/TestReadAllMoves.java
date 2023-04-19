package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.csv.*;

public class TestReadAllMoves {

    public static void main(String[] args) throws IOException {
        // Read a file
        String filePath = "./testData/testDivide.csv";
        
        String[] HEADERS =  {"move_pkid","move_id","year","month","departure","arrival","from_id","to_id","from_name","to_name","from_type","to_type","vessel_id","vessel_name","vessel_type","dwt","time"};
        // Parese
        Reader in = new FileReader(filePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT .builder()
                                                .setHeader()
                                                .setDelimiter(";")
                                                .build();


        CSVParser csvParser = new CSVParser(in, csvFormat);
    
        //à àméliorer
        int currentYear = 1977;
        CSVPrinter printer = new CSVPrinter(new FileWriter("data/1977.csv",false), csvFormat);
        int cpt = 0;
        for (CSVRecord record : csvParser)
        {
            cpt++;
            int year;
            
            try {
                year = Integer.parseInt(record.get("year").trim());
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                // skip header record
                System.out.println("Problematic Line: "+cpt);
                e.printStackTrace();
                continue;
            }
            if (year != currentYear)
            {
                currentYear = year;
                if (printer != null) {
                    printer.close();
                }
                printer     = new CSVPrinter(new FileWriter("data/"+currentYear+".csv",true), csvFormat);

            }
            printer.printRecord(record);
        }
    }
}
