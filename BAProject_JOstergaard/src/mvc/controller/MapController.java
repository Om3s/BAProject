package mvc.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;

import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;
import mvc.model.GeoPoint;

import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class MapController extends DefaultMapController {
	private CrimeCaseDatabase dataBase;
	private ArrayList<GeoPoint> currentPoints;
	
	public MapController(JMapViewer map, CrimeCaseDatabase db) {
		super(map);
		this.dataBase = db;
		this.currentPoints = new ArrayList<GeoPoint>();
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
}
