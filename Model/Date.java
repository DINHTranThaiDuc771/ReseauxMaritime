package Model;

public class Date implements Comparable<Date>{
    private String value;
    // Format days 01/03/1977 00:00
    public Date (String value)
    {
        this.value  = value;
    }    
    public boolean isAfter (Date date)
    {
        // Compare year -> month -> day -> hour ->minute
        if (this.year()  > date.year()  ) return true;
        if (this.month() > date.month() ) return true;
        if (this.day()   > date.day()   ) return true;
        return false;
    }
    public int year(){
        return Integer.parseInt(this.value.substring(6,10));
    }
    public int month(){
        return Integer.parseInt(this.value.substring(3, 5));
    }
    public int day() {
        return Integer.parseInt(this.value.substring(0, 2));
    }
    public String toString () {
        return this.value;
    }

    @Override
    public int compareTo(Date o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    @Override
    public boolean equals (Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Date)) return false;
        Date d = (Date) o;
        return this.value.equals(d.value);
    }
    public static void main(String[] args) {
        Date date1 = new Date  ("01/03/1977 00:00");
        Date date2 = new Date ("03/03/1977 00:00");
        Date date3 = new Date ("01/03/1977 00:00");
        Date date4 = new Date ("01/03/1988 00:00");
        System.out.println(
                date1.isAfter(date2) + "\n" +       //false
                date1.isAfter(date3) + "\n" +       //false
                date4.isAfter(date1) + "\n" +       //true
                date1.equals(date3)                 //true
        );
    }
}
