package active.since93.ancount.model;

/**
 * Created by myzupp on 04-12-2016.
 *
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */

public class StringAndInteger {
    String day;
    int count;

    public StringAndInteger(String day, int count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
