package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Date implements Comparable<Date>{
    private String value;

    // Format days 01/03/1977 00:00
    public Date(String value) {
        // Need to transform value into dd/mm/yyyy hh:mm before this.value = value
        // There are 3 format of data in database d/m/yyyy hh:mm and even m/d/yyyy hh:mm (1/13/1988
        // for exemple)
        // If value is already in dd/mm/yyyy, just valid it
        String[] dateFormatsPossible = {"d/M/yyyy HH:mm", "M/d/yyyy HH:mm","d/M/yyyy H:mm", "M/d/yyyy H:mm","dd/MM/yyyy HH:mm" };
        boolean isValidDateFormat = false;

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
        return this.year() == d.year() && this.month()==d.month() && this.day()==d.day();
    }

    public static void main(String[] args) {
        Date date1 = new Date("01/03/1977 00:00");
        Date date2 = new Date("03/03/1977 00:00");
        Date date3 = new Date("01/03/1977 00:00");
        Date date4 = new Date("01/03/1988 00:00");
        System.out.println(
                date1.isAfter(date2) + "\n" + // false
                        date1.isAfter(date3) + "\n" + // false
                        date4.isAfter(date1) + "\n" + // true
                        date1.equals(date3) // true
        );
    }

    public int compareTo(Date date) {
        LocalDate thisDate = LocalDate.of(this.year(), this.month(), this.day());
        LocalDate otherDate = LocalDate.of(date.year(), date.month(), date.day());
        return thisDate.compareTo(otherDate);
    }
}
