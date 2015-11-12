package mvc.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;
import mvc.model.GeoPoint;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

public class MapController extends DefaultMapController {
	private CrimeCaseDatabase dataBase;
	private ArrayList<GeoPoint> currentPoints;
	private GeoPoint selectedMapMarker;
	private MainframeController mainFrameController;
	
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
	  		Coordinate geoClickPos = this.map.getPosition(e.getPoint());
			Point p = this.map.getMapPosition(geoClickPos);
			double X = p.x;
			double Y = p.y;
	  		System.out.println("Left-Click at X="+X+", Y="+Y);
	  		System.out.println("Left-Click at X="+geoClickPos.getLat()+", Y="+geoClickPos.getLon());
	  	    Iterator<GeoPoint> i = this.currentPoints.iterator();
	  	    double centerX, centerY,shortestDistance=Double.MAX_VALUE;
	  	    Point markerPosition = null;
	  	    this.clearSelectedMapMarker();
	  	    GeoPoint mapMarker = null;
	  	    double newDistance;
	  	    while (i.hasNext()) {

	  	        mapMarker = (GeoPoint) i.next();

	  	        if(mapMarker != null){
	  	        	
	  	        	newDistance = this.distance(mapMarker.getCoordinate(), geoClickPos);
	  	        	
	  	        	if (newDistance < shortestDistance){
	  	        		shortestDistance = newDistance;
  	            		this.clearSelectedMapMarker();
  	            		this.selectedMapMarker = mapMarker;
  	            		this.selectedMapMarker.setBackColor(Color.PINK);
	  	        	}
	  	        }
	  	    }
	  	    markerPosition = this.map.getMapPosition(this.selectedMapMarker.getCoordinate());
	  	    centerX =  markerPosition.getX();
	  	    centerY = markerPosition.getY();
	  	    double radCircle  = Math.sqrt( (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));
	  	    if (radCircle > 8){
	  	    	this.clearSelectedMapMarker();
	  	    } else {
	  	    	this.mainFrameController.mainframe.reportList.setSelectedValue(this.selectedMapMarker.getRelatedCaseReport(), true);
	  	    }
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
  	    this.map.repaint();
	}
	
	private double distance(Coordinate c1, Coordinate c2){
		return (c1.getLat()-c2.getLat())*(c1.getLat()-c2.getLat())+(c1.getLon()-c2.getLon())*(c1.getLon()-c2.getLon());
	}

	private void clearSelectedMapMarker() {
		this.setDefaultColor(this.selectedMapMarker);
		this.mainFrameController.mainframe.reportList.clearSelection();
		this.selectedMapMarker = null;
	}
	
	public void setSelectedMarker(GeoPoint mapMarker){
		this.setDefaultColor(this.selectedMapMarker);
		this.selectedMapMarker = mapMarker;
		this.selectedMapMarker.setBackColor(Color.PINK);
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
	
	public void setMainFrameController(MainframeController mFC){
		this.mainFrameController = mFC;
	}
}
