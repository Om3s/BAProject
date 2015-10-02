package mvc.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;
import mvc.model.GeoPoint;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

public class MapController extends DefaultMapController {
	private CrimeCaseDatabase dataBase;
	private ArrayList<GeoPoint> currentPoints;
	private MapMarker selectedMapMarker;
	
	public MapController(JMapViewer map, CrimeCaseDatabase db) {
		super(map);
		this.dataBase = db;
		this.currentPoints = new ArrayList<GeoPoint>();
		this.selectedMapMarker = null;
		this.init();
	}
	
	public void init(){
		for(CaseReport cR : dataBase.getCurrentData()){
			this.map.addMapMarker(cR.getPoint());
		}
		this.map.updateUI();
	}
	
	public void loadPoints(ArrayList<CaseReport> reports){
		this.setShowCurrentPoints(false);
		this.map.removeAllMapMarkers();
		this.currentPoints.clear();
		
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = 0;
		for(CaseReport cR : reports){
			cal.setTime(cR.getDateOpened());
			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
				case 1: cR.getPoint().setBackColor(Color.BLUE); break; //sunday
				case 2: cR.getPoint().setBackColor(Color.YELLOW); break; //monday
				case 3: cR.getPoint().setBackColor(Color.CYAN); break; //tuesday
				case 4: cR.getPoint().setBackColor(Color.MAGENTA); break; //wednesday
				case 5: cR.getPoint().setBackColor(Color.ORANGE); break; //thursday
				case 6: cR.getPoint().setBackColor(Color.RED); break; //friday
				case 7: cR.getPoint().setBackColor(Color.GREEN); break; //saturday
			}
			this.currentPoints.add(cR.getPoint());
		}
		for(GeoPoint p : this.currentPoints){
			p.setVisible(false);
			this.map.addMapMarker(p);
		}
		System.out.println("Map loaded "+this.currentPoints.size()+" points.");
		this.map.updateUI();
	}
	
	public void setShowCurrentPoints(boolean showThem){
		for(GeoPoint point : this.currentPoints){
			if(!showThem){
				point.setVisible(false);
			} else {
				point.setVisible(true);
			}
		}
		this.map.updateUI();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			Point p = e.getPoint();
			double X = p.x;
			double Y = p.y;
	  		System.out.println("Left-Click at X="+X+", Y="+Y);
	  		List<MapMarker> ar = map.getMapMarkerList();
	  	    Iterator<MapMarker> i = ar.iterator();
	  	    double centerX, centerY,shortestDistance=Double.MAX_VALUE;
	  	    Point markerPosition = null;
	  	    int y=0;
	  	    this.clearSelectedMapMarker();
	  	    while (i.hasNext()) {

	  	        MapMarkerDot mapMarker = (MapMarkerDot) i.next();

	  	        if(mapMarker != null){
	  	        	
	  	        	markerPosition = map.getMapPosition(mapMarker.getLat(), mapMarker.getLon());
	  	        	if(markerPosition == null){
	  	        		System.out.println("MarkerPosition OUT OF SIGHT");
	  	        	}
	  	            centerX =  markerPosition.getX();
	  	            centerY = markerPosition.getY();

	  	            // calculate the radius from the touch to the center of the dot
	  	            double radCircle  = Math.sqrt( (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));

	  	            // if the radius is smaller then 23 (radius of a ball is 5), then it must be on the dot
	  	            if (radCircle < 8){
	  	            	if(radCircle<shortestDistance){
	  	            		this.clearSelectedMapMarker();
	  	            		this.selectedMapMarker = mapMarker;
	  	            		mapMarker.setBackColor(Color.PINK);
	  	            	}
	  	            }
		  	        y++;
	  	        }
	  	    }
	  	    System.out.println(y);
	  	    this.map.repaint();
		} else if(e.getButton() == MouseEvent.BUTTON2){
			Point p = e.getPoint();
			int X = p.x;
			int Y = p.y;
	  		System.out.println("Mid-Click at X="+X+", Y="+Y);
		} else if(e.getButton() == MouseEvent.BUTTON3){
			Point p = e.getPoint();
			int X = p.x;
			int Y = p.y;
	  		System.out.println("Right-Click at X="+X+", Y="+Y);
		}
	}

	private void clearSelectedMapMarker() {
		this.setDefaultColor(this.selectedMapMarker);
		this.selectedMapMarker = null;
	}

	private void setDefaultColor(MapMarker m) {
		if(m != null){
			for(CaseReport cR : this.dataBase.getCurrentData()){
				if(cR.getPoint().getLat() == m.getLat() && cR.getPoint().getLon() == m.getLon()){
					int dayOfWeek;
					Calendar cal = Calendar.getInstance();
					cal.setTime(cR.getDateOpened());
					dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
					switch (dayOfWeek) {
						case 1: cR.getPoint().setBackColor(Color.BLUE); break; //sunday
						case 2: cR.getPoint().setBackColor(Color.YELLOW); break; //monday
						case 3: cR.getPoint().setBackColor(Color.CYAN); break; //tuesday
						case 4: cR.getPoint().setBackColor(Color.MAGENTA); break; //wednesday
						case 5: cR.getPoint().setBackColor(Color.ORANGE); break; //thursday
						case 6: cR.getPoint().setBackColor(Color.RED); break; //friday
						case 7: cR.getPoint().setBackColor(Color.GREEN); break; //saturday
					}
				}
			}
		}
	}
}
