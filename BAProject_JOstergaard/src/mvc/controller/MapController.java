package mvc.controller;

import java.awt.Color;
import java.util.ArrayList;

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
			cR.getPoint().setVisible(true);
			this.map.addMapMarker(cR.getPoint());
		}
		this.map.updateUI();
	}
	
	public void loadPoints(ArrayList<CaseReport> reports){
		this.setShowCurrentPoints(false);
		this.map.removeAllMapMarkers();
		this.currentPoints.clear();
		for(CaseReport cR : reports){
			this.currentPoints.add(cR.getPoint());
		}
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
