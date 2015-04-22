package mvc.controller;

import java.awt.Color;

import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;

import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class MapController extends DefaultMapController {
	private CrimeCaseDatabase dataBase;
	
	public MapController(JMapViewer map, CrimeCaseDatabase db) {
		super(map);
		this.dataBase = db;
		this.init();
	}
	
	public void init(){
		for(CaseReport cR : dataBase.getCurrentData()){
			cR.getPoint().setVisible(true);
			this.map.addMapMarker(cR.getPoint());
		}
		this.map.updateUI();
	}
	
	public void toggleShowCurrentData(){
		for(CaseReport cR : dataBase.getCurrentData()){
			if(cR.getPoint().isVisible()){
				cR.getPoint().setVisible(false);
			} else {
				cR.getPoint().setVisible(true);
			}
		}
		this.map.updateUI();
	}
}
