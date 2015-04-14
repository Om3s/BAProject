package mvc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaseReport {
	private int caseID;
	private Date dateOpened, dateClosed;
	private String address, category, point;
	
	public CaseReport(int caseID, String dateOpened, String dateClosed, String address, String category, String point){
		this.caseID = caseID;
		this.address = address;
		this.category = category;
		this.point = point;
		try {
			this.dateOpened = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(dateOpened);
			this.dateClosed = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(dateClosed);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this.dateOpened);
		System.out.println(this.dateClosed);
	}
}
