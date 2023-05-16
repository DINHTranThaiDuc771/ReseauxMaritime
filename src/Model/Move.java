package Model;

import java.util.ArrayList;
import java.util.List;

public class Move implements Comparable<Move>{
    private final static int NULLE_PARTE = -3;
    private final static int AVERAGE_SEA_SHIPPING_DURATION = 45; 
    private Date depart;
    private Date arrival;
    private int    from_id;
    private int    to_id;

    public Move(String depart, String arrival, int from_id, int to_id) {
        this (new Date(depart), new Date(arrival),from_id,to_id);
    }
    public Move(Date depart, Date arrival, int from_id, int to_id) {
        this.depart =  depart;
        this.arrival = arrival;
        this.from_id = from_id;
        this.to_id = to_id;
        if (Date.between(depart, arrival)> Move.AVERAGE_SEA_SHIPPING_DURATION && from_id != Move.NULLE_PARTE)
        {
            this.arrival = Date.getNextDate(this.depart, 10);
        }       
    }
    @Override
    public String toString () {
        return
        String.format("%20s", depart.toString()) +
        String.format("%20s", arrival.toString())+
        String.format("%8d" , from_id)+
        String.format("%8d" , to_id);
    }
    // Format days 01/03/1977 00:00
    public Date getDepart() {
        return depart;
    }
    public Date getArrival() {
        return arrival;
    }
    public int getFrom_id() {
        return from_id;
    }
    public int getTo_id() {
        return to_id;
    }
    public static Move checkCoherent(Move moveBefore, Move moveAfter) {
        if (moveBefore.to_id == moveAfter.from_id)
            return null;
        return new Move(moveBefore.arrival, moveAfter.depart, moveBefore.to_id, moveAfter.from_id);
    }
    public static List<Move> checkCoherent2(Move moveBefore, Move moveAfter) {
        //TODO Test method
        if (moveBefore.to_id == moveAfter.from_id)
        {
            if (Date.between(moveBefore.arrival, moveAfter.depart) > 30 )
            {
                List<Move> movesRet = new ArrayList<>();
                movesRet.add(new Move(Date.getNextDate(moveBefore.arrival,1), Date.getPreviousDate(moveAfter.depart, 1), Move.NULLE_PARTE, Move.NULLE_PARTE));
                return movesRet;
            }
            return new ArrayList<Move>();
        }

        List<Move> movesRet = new ArrayList<>();
        //For example, the 3/3 and 1/3 will return 2 by the method Date.between, but 
        //The date that can be used to add more Move is only the 2/3, which is 2-1
        long ecart = Date.between(moveBefore.getArrival(),moveAfter.getDepart()) -1;
        if (ecart > 10)
        {
            //Imagine if ecart =11 , between the 1/3 and 13/3

            //Add 5 moves to indicate that the navire stay at the same port in 5 days
            Move moveCurrentToAdd = new Move(Date.getNextDate(moveBefore.getArrival(),1), Date.getNextDate(moveBefore.getArrival(),1), moveBefore.to_id, moveBefore.to_id); 
            movesRet.add (moveCurrentToAdd);
            for (int cpt = 0; cpt < 4; cpt++)
            {
                // stay in port moveBefore.to_id for 5 days : from 2/3 to 6/3
                moveCurrentToAdd = new Move (Date.getNextDate(moveCurrentToAdd.getArrival(),1),Date.getNextDate(moveCurrentToAdd.getArrival(),1),moveCurrentToAdd.to_id,moveCurrentToAdd.to_id);
                movesRet.add (moveCurrentToAdd);
            }
            //Nulle part for 11-10 days : 7/3
            Date datetmp = Date.getNextDate(moveCurrentToAdd.getArrival(),1);
            moveCurrentToAdd = new Move (datetmp,Date.getNextDate(datetmp, ecart-11),Move.NULLE_PARTE,Move.NULLE_PARTE);
            movesRet.add(moveCurrentToAdd);
            //Add move that it moves from old port to the new : from 8/3 to 12/3
            datetmp = Date.getNextDate(moveCurrentToAdd.getArrival(),1);
            moveCurrentToAdd = new Move (datetmp,Date.getPreviousDate(moveAfter.getDepart(), 1),moveBefore.to_id,moveAfter.from_id);
            movesRet.add(moveCurrentToAdd);
            return movesRet;

        }
        if (ecart <= 10 && ecart > 5)
        {
            //Add 5 moves to say that the navire stay at the same port in 5 days
            Move moveCurrentToAdd = new Move(Date.getNextDate(moveBefore.getArrival(),1), Date.getNextDate(moveBefore.getArrival(),1), moveBefore.to_id, moveBefore.to_id); 
            movesRet.add (moveCurrentToAdd);
            for (int cpt = 0; cpt < 4; cpt++)
            {
                // stay in port moveBefore.to_id for 5 days : from 2/3 to 6/3
                moveCurrentToAdd = new Move (Date.getNextDate(moveCurrentToAdd.getArrival(),1),Date.getNextDate(moveCurrentToAdd.getArrival(),1),moveCurrentToAdd.to_id,moveCurrentToAdd.to_id);
                movesRet.add (moveCurrentToAdd);
            }

             //Add move that it moves from old port to the new
            Date datetmp = Date.getNextDate(moveCurrentToAdd.getArrival(),1);
            moveCurrentToAdd = new Move (datetmp,Date.getPreviousDate(moveAfter.getDepart(), 1),moveBefore.to_id,moveAfter.from_id);
            movesRet.add(moveCurrentToAdd);
            return movesRet;           
        }
        if (ecart <= 5)
        {
            movesRet.add (Move.checkCoherent(moveBefore, moveAfter));
            return movesRet;
        }
        return movesRet;
    }
    @Override
    public int compareTo(Move m) {
        // System.out.println("Comparing " + this + " and " + m);
        return this.depart.compareTo(m.depart);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        Move other = (Move) obj;
        return this.depart.equals(other.depart);
    }
}
