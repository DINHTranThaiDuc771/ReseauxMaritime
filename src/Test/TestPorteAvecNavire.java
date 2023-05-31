package Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import Model.*;
public class TestPorteAvecNavire {
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("./testData/testDGS.csv");
        HashMap<Integer,HashSet<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire("01/07/2016 00:00");
        TreeMap<Integer,HashSet<Navire>> sorted = new TreeMap<Integer,HashSet<Navire>>(mapPorteAvecNavire);
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
