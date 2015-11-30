package mvc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class CaseReport {
	private int caseID;
	private Date dateOpened, dateClosed;
	private String address, category;
	private GeoPoint point;
	private boolean currentSelected = false;
	
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
		this.point.setRelatedCaseReport(this);
	}
	
	/**
	 * This method will convert the given String(lat and lon from the CSV) into an GeoPoint object.
	 * 
	 * @param pointString the String in form of the indexed text representation of a Point coordinate.
	 * @return the created GeoPoint object.
	 */
	private GeoPoint fromStringToGeoPoint(String pointString){
		String[] coordinatesAsString = pointString.substring(1, pointString.length()-1).split(",");
		Coordinate coord = new Coordinate(Double.valueOf(coordinatesAsString[0]), Double.valueOf(coordinatesAsString[1]));
		return new GeoPoint(coord);
	}
	
	/**
	 * Overwrites the toString() method, this method is used for the representation
	 * in the CaseReport List element of the mainFrame.
	 * 
	 * @return the representative String for the CaseReport List elements
	 */
	public String toString(){
		return "CaseReport[CaseID: "+this.caseID+"]";
	}
	
	/**
	 * A second toString method for the detailed view of a caseReport. It uses "%$sepa$%" as
	 * separator String of the different values.
	 * @return the representative String for the details window
	 */
	public String toString2(){
		return "CaseReport[CaseID: "+this.caseID+"%$sepa&%$Opened: "+this.dateOpened+"%$sepa&%$Closed: "+this.dateClosed+"%$sepa&%$Address: "+this.address+"%$sepa&%$Category: "+this.category+"%$sepa&%$Point: ("+this.point.getLat()+", "+this.point.getLon()+")]";
	}
	
	/**
	 * 
	 * @return returns the position of this CaseReport by its GeoPoint object
	 */
	public GeoPoint getPoint(){
		return this.point;
	}
	
	/**
	 * compareTo method to set a order by the time the case has been
	 * opened from older to younger
	 * 
	 * @param anotherCaseReport
	 * @return
	 */
	public int compareTo(CaseReport anotherCaseReport){
		if(this.dateOpened.after(anotherCaseReport.dateOpened)){
			return 1;
		} else if(this.dateOpened.equals(anotherCaseReport.dateOpened)){
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * 
	 * @return true if this CaseReport is the selected one, false if not
	 */
	public boolean isCurrentSelected() {
		return currentSelected;
	}
	
	/**
	 * 
	 * @param currentSelected true to select this CaseReport, false to deselect this CaseReport
	 */
	public void setCurrentSelected(boolean currentSelected) {
		this.currentSelected = currentSelected;
	}
	
	/**
	 * 
	 * @return the date when this CaseReport has been opened
	 */
	public Date getDateOpened(){
		return this.dateOpened;
	}
	
	/**
	 * 
	 * @return the unique indexed ID of this Case
	 */
	public int getCaseID(){
		return this.caseID;
	}
}
