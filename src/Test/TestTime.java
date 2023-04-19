package Test;

import java.io.IOException;

import Model.Model;

public class TestTime {
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        long startTime = System.nanoTime();
        model.chargerModel("testMoves.csv");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // duration in nanoseconds
        System.out.println("Duration: " + (double)duration/1000000000  + " s"); //0.0368558 s 100 first line

        Model model2 = new Model();
        startTime = System.nanoTime();
        model2.chargerModel("testMoves8000.csv");
        endTime = System.nanoTime();
        duration = (endTime - startTime);  // duration in nanoseconds
        System.out.println("Duration: " + (double)duration/1000000000  + " s"); //0.1173023 s 8000 first line 

        Model model3 = new Model();
        startTime = System.nanoTime();
        model3.chargerModel("allMoves.csv");
        endTime = System.nanoTime();
        duration = (endTime - startTime);  // duration in nanoseconds
        System.out.println("Duration: " + (double)duration/1000000000  + " s"); //0.1173023 s 8000 first line 
    }
}
