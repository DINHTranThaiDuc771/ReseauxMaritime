import java.util.HashSet;

import Model.Navire;

public class Test {
    public static void main(String[] args) {
        Navire n1 = new Navire(0);
        Navire n2 = new Navire(0);
        HashSet<Navire> set  = new HashSet<Navire>();
        System.out.println(set.add(n1));
        System.out.println(set.add(n2));

    }
}
