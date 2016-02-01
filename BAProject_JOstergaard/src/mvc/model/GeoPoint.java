package mvc.model;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class GeoPoint extends MapMarkerDot {
	private CaseReport relatedCaseReport;
	
	public GeoPoint(Coordinate coord) {
		this(coord, null);
	}
	
	public GeoPoint(Coordinate coord, CaseReport cR) {
		super(coord);
		this.setVisible(false);
		this.relatedCaseReport = cR;
	}
	
	/**
	 * 
	 * @return the related CaseReport
	 */
	public CaseReport getRelatedCaseReport() {
		return relatedCaseReport;
	}
	
	/**
	 * Set the related CaseReport
	 * @param relatedCaseReport this will be the unique related CaseReport for this GeoPoint Object
	 */
	public void setRelatedCaseReport(CaseReport relatedCaseReport) {
		this.relatedCaseReport = relatedCaseReport;
	}
}
