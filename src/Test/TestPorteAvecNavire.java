package Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import Model.*;

public class TestPorteAvecNavire {
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("./testData/testMoves.csv");
        HashMap<Integer,ArrayList<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire("01/03/1977 00:00");
        TreeMap<Integer,ArrayList<Navire>> sorted = new TreeMap<Integer,ArrayList<Navire>>(mapPorteAvecNavire);
        sorted.forEach(
            (k,v) -> 
            {
                System.out.println("Porte"+ String.format("%-10d", k));
                for (Navire nav :v)
                {
                    System.out.println(nav);
                }
                System.out.println("--------------------------");
            }
        );
    }
}
