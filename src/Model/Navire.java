package Model;

public class Navire implements Comparable<Navire>{
    private int id;
    public Navire (int id) {
        this.id = id;
    }
    @Override
    public boolean equals (Object object){
        if (object == this ) return true;
        if (!(object instanceof Navire)) return false;
        Navire navire = (Navire) object;

        return this.id == navire.id;
    }
    /*
     * hashCode is a method defined in the Object class that returns an integer representation of the object's memory address or state.
     * Implement to use it with HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString () {
        return this.id +"";
    }

    @Override
    public int compareTo(Navire navire) {
        return this.id - navire.id;
    }
}
