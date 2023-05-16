package Model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
/*
 * From 31/12/2013, date format is MM/DD/YYYY
 * 
 * 
 */
public class Date implements Comparable<Date>{
    private String value;
    public Date (Date date)
    {
        this.value = date.value;
    }
    // Format days 01/03/1977 00:00
    public Date(String value,boolean formatted) {
        // Need to transform value into dd/mm/yyyy hh:mm before this.value = value
        // There are 3 format of data in database d/m/yyyy hh:mm and even m/d/yyyy hh:mm (1/13/1988
        // for exemple)
        // If value is already in dd/mm/yyyy, just valid it
        String[] dateFormatsPossible = {"d/M/yyyy HH:mm","d/M/yyyy H:mm", "dd/MM/yyyy HH:mm","dd/MM/yyyy","d/M/yyyy" };
        boolean isValidDateFormat = false;

        String[] dateData    = value.split(" ");
        String dateSansHeure = dateData[0];
        String[] lstArg      = dateSansHeure.split("/");
        int arg3 = Integer.parseInt(lstArg[2]);
        int arg2 = Integer.parseInt(lstArg[1]);
        int arg1 = Integer.parseInt(lstArg[0]);
        if ((arg3 > 2013 || (arg1 == 12 && arg2 == 31 && arg3 ==2013)) && !formatted)
        {
            dateFormatsPossible = new String[]{"M/d/yyyy HH:mm","M/d/yyyy H:mm","M/d/yyyy","MM/dd/yyyy","MM/dd/yyyy HH:mm"};
        }

        for (String dateFormat : dateFormatsPossible) {
            try {
                LocalDate date = LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(dateFormat));
                this.value = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                isValidDateFormat = true;
                break;
            } catch (DateTimeParseException e) {
                // do nothing and continue trying other date formats
            }
        }

        if (!isValidDateFormat) {
            throw new IllegalArgumentException("Invalid date format: " + value);
        }

    }
    public Date(String value) {
        // Need to transform value into dd/mm/yyyy hh:mm before this.value = value
        // There are 3 format of data in database d/m/yyyy hh:mm and even m/d/yyyy hh:mm (1/13/1988
        // for exemple)
        // If value is already in dd/mm/yyyy, just valid it
        String[] dateFormatsPossible = {"d/M/yyyy HH:mm","d/M/yyyy H:mm", "dd/MM/yyyy HH:mm","dd/MM/yyyy","d/M/yyyy" };
        boolean isValidDateFormat = false;

        String[] dateData    = value.split(" ");
        String dateSansHeure = dateData[0];
        String[] lstArg      = dateSansHeure.split("/");
        int arg3 = Integer.parseInt(lstArg[2]);
        int arg2 = Integer.parseInt(lstArg[1]);
        int arg1 = Integer.parseInt(lstArg[0]); 
        if (arg3 > 2013 || (arg1 == 12 && arg2 == 31 && arg3 ==2013))
        {
            dateFormatsPossible = new String[]{"M/d/yyyy HH:mm","M/d/yyyy H:mm","M/d/yyyy","MM/dd/yyyy","MM/dd/yyyy HH:mm"};
        }
        for (String dateFormat : dateFormatsPossible) {
            try {
                LocalDate date = LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(dateFormat));
                this.value = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                isValidDateFormat = true;
                break;
            } catch (DateTimeParseException e) {
                // do nothing and continue trying other date formats
            }
        }

        if (!isValidDateFormat) {
            throw new IllegalArgumentException("Invalid date format: " + value);
        }

    }

    public boolean isAfter(Date date) {
        return this.compareTo(date) > 0;
    }

    public boolean isBefore(Date date) {
        return this.compareTo(date) < 0;
    }

    public int year() {
        return Integer.parseInt(this.value.substring(6, 10).trim());
    }

    public int month() {
        return Integer.parseInt(this.value.substring(3, 5).trim());
    }

    public int day() {
        return Integer.parseInt(this.value.substring(0, 2).trim());
    }

    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Date))
            return false;
        Date d = (Date) o;
        return this.compareTo(d) == 0 || this.value.equals(d.value);
    }

    public static void main(String[] args) {
        Date date1 = new Date("01/03/1977 00:00");
        Date date2 = new Date("03/03/1977 00:00");
        Date date3 = new Date("01/03/1977 00:00");
        Date date4 = new Date("01/03/1988 00:00");
        Date date5 = new Date("31/12/1988 00:00");

        System.out.println(
                date1.isAfter(date2) + "\n" + // false
                        date1.isAfter(date3) + "\n" + // false
                        date4.isAfter(date1) + "\n" + // true
                        date1.equals(date3) // true
        );
        //Test NextDate
        System.out.println(Date.getNextDate(date5,1)); //01/01/1989
        //Test Between
        Date date6 = new Date("31/03/1977");
        Date date7 = new Date("31/05/1977");

        System.out.println (Date.between(date6, date7));
    }

    public int compareTo(Date date) {
        LocalDate thisDate = LocalDate.of(this.year(), this.month(), this.day());
        LocalDate otherDate = LocalDate.of(date.year(), date.month(), date.day());
        return thisDate.compareTo(otherDate);
    }
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static Date getNextDate(Date date,long daysToAdd) {
        LocalDate localDate = LocalDate.of(date.year(), date.month(), date.day()).plusDays(daysToAdd);
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new Date(formattedDate + " 00:00");
    }
    public static Date getPreviousDate (Date date, long daysToSubtract)
    {
        LocalDate localDate = LocalDate.of(date.year(), date.month(), date.day()).minusDays(daysToSubtract);
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new Date(formattedDate + " 00:00");
    }
    public static long between(Date date1, Date date2) {
        LocalDate localDate1 = LocalDate.of (date1.year(), date1.month(), date1.day());
        LocalDate localDate2 = LocalDate.of (date2.year(), date2.month(), date2.day());
        return ChronoUnit.DAYS.between(localDate1,localDate2);
    }
}
