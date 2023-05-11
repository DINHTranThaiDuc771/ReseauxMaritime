package Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import Model.Date;
import Model.Model;
import Model.Navire;

public class TestNumberOfPort 
{
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.chargerModel("./testData/testDGS.csv");
        model.chargerListDateVsStep("./tmp/dates_vs_step");
        for (Date date : model.getLstStepVsDate())
        {
            HashMap<Integer, HashSet<Navire>> mapPorteAvecNavire = model.getPorteAvecNavire(date.toString());
            System.out.println(mapPorteAvecNavire.keySet().size());
        }
    }    
}
