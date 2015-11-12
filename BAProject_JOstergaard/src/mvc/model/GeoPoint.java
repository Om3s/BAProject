package mvc.model;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;


public class GeoPoint extends MapMarkerDot {
	private CaseReport relatedCaseReport;
	
	public GeoPoint(Coordinate coord) {
		super(coord);
		this.setVisible(false);
	}
	
	public GeoPoint(Coordinate coord, CaseReport cR) {
		super(coord);
		this.setVisible(false);
		this.relatedCaseReport = cR;
	}

	public CaseReport getRelatedCaseReport() {
		return relatedCaseReport;
	}

	public void setRelatedCaseReport(CaseReport relatedCaseReport) {
		this.relatedCaseReport = relatedCaseReport;
	}

}
