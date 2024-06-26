package chartManagement.utils;

import java.io.Serializable;
import java.util.Date;

public class DataPoint implements Serializable
{


	private static final long serialVersionUID = -6585082144060930958L;
	private Date date;
    private String grouper1;
    private String grouper2;
    private double measure;

    public DataPoint(Date date, String grouper1, String grouper2, double measure) {
        this.date = date;
        this.grouper1 = grouper1;
        this.grouper2 = grouper2;
        this.measure = measure;
    }
	

    public Date getDate() {
        return date;
    }

    public String getGrouper1() {
        return grouper1;
    }

    public String getGrouper2() {
        return grouper2;
    }

    public double getMeasure() {
        return measure;
    }
    


	
}
