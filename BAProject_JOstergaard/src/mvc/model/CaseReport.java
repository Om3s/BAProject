package mvc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class CaseReport {
	private int caseID;
	private Date dateOpened, dateClosed;
	private String address, category;
	private GeoPoint point;
	private boolean currentSelected = false;
	public String testString = "unchanged";
	
	public CaseReport(){
		
	}
	
	public CaseReport(int caseID, String dateOpened, String dateClosed, String address, String category, String point){
		this.caseID = caseID;
		this.address = address;
		this.category = category;
		this.point = fromStringToGeoPoint(point);
		this.dateOpened = new Date(Long.valueOf(dateOpened));
		if(!dateClosed.isEmpty()){
			this.dateClosed = new Date(Long.valueOf(dateClosed));
		} else {
			this.dateClosed = null;
		}
	}
	
	private GeoPoint fromStringToGeoPoint(String pointString){
		String[] coordinatesAsString = pointString.substring(1, pointString.length()-1).split(",");
		Coordinate coord = new Coordinate(Double.valueOf(coordinatesAsString[0]), Double.valueOf(coordinatesAsString[1]));
		return new GeoPoint(coord);
	}
	
	public String toString(){
		return "CaseReport[CaseID: "+this.caseID+", Opened: "+this.dateOpened+", Closed: "+this.dateClosed+", Address: "+this.address+", Category: "+this.category+", Point: ("+this.point.getLat()+", "+this.point.getLon()+")]";
	}
	
	public GeoPoint getPoint(){
		return this.point;
	}
	
	public int compareTo(CaseReport anotherCaseReport){
		if(this.dateOpened.after(anotherCaseReport.dateOpened)){
			return 1;
		} else if(this.dateOpened.equals(anotherCaseReport.dateOpened)){
			return 0;
		} else {
			return -1;
		}
	}

	public boolean isCurrentSelected() {
		return currentSelected;
	}

	public void setCurrentSelected(boolean currentSelected) {
		this.currentSelected = currentSelected;
	}
	
	public Date getDateOpened(){
		return this.dateOpened;
	}
	
	public int getCaseID(){
		return this.caseID;
	}
}
