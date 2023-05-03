package Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestRemoveAll {
    public static void main(String[] args) {
        Set<Integer> setA = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> setB = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        Set<Integer> setC = new HashSet<>(setA);
        setC.removeAll(setB);

        System.out.println("SetA \\ SetB : " + setC);
    }
}
