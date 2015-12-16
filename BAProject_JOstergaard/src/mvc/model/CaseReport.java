package mvc.model;

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
	private String address, category, mediaURLPath, neighbourhood, status, statusNotes;
	private GeoPoint point;
	private boolean currentSelected = false, isClosed, hasAPictureLink;
	
	public CaseReport(){
		
	}
	
	public CaseReport(int caseID, String dateOpened, String dateClosed, String status, String statusNotes, String category, String address, String neighbourhood, String point, String mediaUrl){
		this.caseID = caseID;
		this.status = status;
		this.statusNotes = statusNotes;
		this.category = category;
		this.address = address;
		this.neighbourhood = neighbourhood;
		this.point = fromStringToGeoPoint(point);
		this.dateOpened = new Date(Long.valueOf(dateOpened));
		if(!dateClosed.equals("-1")){
			this.dateClosed = new Date(Long.valueOf(dateClosed));
		} else {
			this.dateClosed = null;
		}
		if(!mediaUrl.equals("n/A")){
			this.mediaURLPath = mediaUrl;
			this.hasAPictureLink = true;
		} else {
			this.mediaURLPath = "Not Available";
			this.hasAPictureLink = false;
		}
		if(status.equals("Open")){
			this.isClosed = false;
		} else if(status.equals("Closed")) {
			this.isClosed = true;
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
		return this.dateOpened.toString();
	}
	
	/**
	 * A second toString method for the detailed view of a caseReport. It uses "%$sepa$%" as
	 * separator String of the different values.
	 * @return the representative String for the details window
	 */
	public String toString2(){
		return "CaseReport[CaseID: "+this.caseID+"%$sepa&%$Category: "+this.category+"%$sepa&%$Opened: "+this.dateOpened+"%$sepa&%$Closed: "+this.dateClosed+"%$sepa&%$Status: "+this.status+"%$sepa&%$StatusNotes: "+this.statusNotes+"Address: "+this.address+"%$sepa&%$Neighbourhood: "+this.neighbourhood+"%$sepa&%$Point: ("+this.point.getLat()+", "+this.point.getLon()+")%$sepa&%$Image: "+this.mediaURLPath+"]";
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

	public String getMediaURLPath() {
		return mediaURLPath;
	}

	public void setMediaURLPath(String mediaURL) {
		this.mediaURLPath = mediaURL;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public boolean hasAPictureLink() {
		return hasAPictureLink;
	}
}
