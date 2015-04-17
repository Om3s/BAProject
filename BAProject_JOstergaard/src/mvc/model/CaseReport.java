package mvc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaseReport {
	private int caseID;
	private Date dateOpened, dateClosed;
	private String address, category, point;
	
	public CaseReport(){
		
	}
	
	public CaseReport(int caseID, String dateOpened, String dateClosed, String address, String category, String point){
		this.caseID = caseID;
		this.address = address;
		this.category = category;
		this.point = point;
		try {
			this.dateOpened = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(dateOpened);
			if(!dateClosed.isEmpty()){
				this.dateClosed = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(dateClosed);
			} else {
				this.dateClosed = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return "CaseReport[CaseID: "+this.caseID+", Opened: "+this.dateOpened+", Closed: "+this.dateClosed+", Address: "+this.address+", Category: "+this.category+", Point: "+this.point+"]";
	}
}
