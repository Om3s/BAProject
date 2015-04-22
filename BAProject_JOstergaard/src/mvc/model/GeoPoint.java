package mvc.model;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;


public class GeoPoint extends MapMarkerDot {

	public GeoPoint(Coordinate coord) {
		super(coord);
		this.setVisible(false);
	}

}
