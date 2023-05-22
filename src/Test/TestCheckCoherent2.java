package Test;

import Model.Move;

public class TestCheckCoherent2
{
    public static void main(String[] args) {
        Move m1,m2,m3,m4;
        System.out.println("Ecart > 10");
        
        m1 = new Move ("01/03/1977 00:00","01/03/1977 00:00",1,2);
        m2 = new Move ("14/03/1977 00:00","15/03/1977 00:00",3,4);
        System.out.println (m1);
        System.out.println (m2);
        System.out.println("------------Moves à ajouter------------");

        for (Move m :Move.checkCoherent2(m1,m2))
        {
            System.out.println(m);
        }
        System.out.println("Ecart <= 10 and Ecart >5");
        m3 = new Move ("01/03/1977 00:00","01/03/1977 00:00",1,2);
        m4 = new Move ("8/03/1977 00:00","15/03/1977 00:00",3,4);
        System.out.println (m3);
        System.out.println (m4);
        System.out.println("------------Moves à ajouter------------");
        for (Move m :Move.checkCoherent2(m3,m4))
        {
            System.out.println(m);
        }
        System.out.println("Ecart <= 5");
        m3 = new Move ("01/03/1977 00:00","01/03/1977 00:00",1,2);
        m4 = new Move ("07/03/1977 00:00","15/03/1977 00:00",3,4);
        System.out.println (m3);
        System.out.println (m4);
        System.out.println("------------Moves à ajouter------------");

        for (Move m :Move.checkCoherent2(m3,m4))
        {
            System.out.println(m);
        }

        System.out.println("Ecart >= 30");
        m3 = new Move ("01/03/1977 00:00","11/03/1977 00:00",1,2);
        m4 = new Move ("31/04/1977 00:00","15/05/1977 00:00",2,3);
        System.out.println (m3);
        System.out.println (m4);
        System.out.println("------------Moves à ajouter------------");

        for (Move m :Move.checkCoherent2(m3,m4))
        {
            System.out.println(m);
        }
    }
}