package Model;
public class Move implements Comparable<Move>{
    private Date depart;
    private Date arrival;
    private int    from_id;
    private int    to_id;
    public Move(String depart, String arrival, int from_id, int to_id) {
        this.depart = new Date (depart);
        this.arrival = new Date (arrival);
        this.from_id = from_id;
        this.to_id = to_id;
    }
    public Move(Date depart, Date arrival, int from_id, int to_id) {
        this.depart =  depart;
        this.arrival = arrival;
        this.from_id = from_id;
        this.to_id = to_id;       
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
    public static Move checkCoherent(Move previousMove, Move currentMove) {
        if (previousMove.to_id == currentMove.from_id)
            return null;
        return new Move(previousMove.arrival, currentMove.depart, previousMove.to_id, currentMove.from_id);
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
