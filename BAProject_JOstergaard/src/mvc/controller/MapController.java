package mvc.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.GeoMapViewer;
import mvc.model.GeoPoint;
import mvc.model.GrayOSMTileSource;
import mvc.model.ImageMapMarker;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * 
 * @author Jonas Ostergaard
 * 
 * This class is the custom controller for the JMapViewer GeoMap element.
 *
 */
public class MapController extends DefaultMapController {
	private ArrayList<GeoPoint> currentPoints;
	private GeoPoint selectedMapMarker;
	private ArrayList<BufferedImage> heatMapImages = new ArrayList<BufferedImage>();
	private ImageMapMarker currentHeatMapImageMarker;
	
	public MapController(JMapViewer map) {
		super(map);
		this.currentPoints = new ArrayList<GeoPoint>();
		this.selectedMapMarker = null;
		this.init();
	}
	
	/**
	 * Simply draws all current data initially to the map.
	 */
	public void init(){
		this.map.setTileSource(new GrayOSMTileSource.BWMapnik());
		for(CaseReport cR : Main.dataBase.getCurrentData()){
			this.map.addMapMarker(cR.getPoint());
		}
		this.map.updateUI();
	}
	
	/**
	 * This method creates GeoPoint mapMarkers for all CaseReport objects of
	 * the given reports array.
	 * 
	 * @param reports the list of Casereport's you want to load to the map.
	 */
	public void loadPoints(ArrayList<CaseReport> reports){
		this.setShowCurrentPoints(false);
		this.map.removeAllMapMarkers();
		this.currentPoints.clear();
		
		Calendar cal = Calendar.getInstance();
		int dayOfWeek;
		for(CaseReport cR : reports){
			if(cR.getPoint() != null){
				cal.setTime(cR.getDateOpened());
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 3){
					cR.getPoint().setColor(Main.alphaColor);
					cR.getPoint().setBackColor(Main.alphaColor);
				} else {
					cR.getPoint().setColor(Color.BLACK);
					cR.getPoint().setBackColor(Main.weekDayColors[dayOfWeek-1]);
				}
				this.currentPoints.add(cR.getPoint());
			}
		}
		for(GeoPoint p : this.currentPoints){
			p.setVisible(false);
			this.map.addMapMarker(p);
		}
		System.out.println("Map loaded "+this.currentPoints.size()+" points.");
		this.map.updateUI();
	}
	
	public void refreshDots(){
		for(GeoPoint p : this.currentPoints){
			if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 3){
				p.setColor(Main.alphaColor);
				p.setBackColor(Main.alphaColor);
			}
		}
	}
	
	/**
	 * Shows or hides all currentPoints on the map
	 * 
	 * @param showThem true for visible, false for invisible.
	 */
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
	
	/**
	 * Shows or hides all closed/opened currentPoints on the map
	 * 
	 * @param showThem true for visible, false for invisible.
	 */
	public void setShowOpenedClosedCurrentPoints(boolean showThem, boolean opened){
		for(GeoPoint point : this.currentPoints){
			if(showThem && opened && !point.getRelatedCaseReport().isClosed()){
				point.setVisible(true);
			} else if(showThem && !opened && point.getRelatedCaseReport().isClosed()) {
				point.setVisible(true);
			} else if(!showThem && opened && !point.getRelatedCaseReport().isClosed()){
				point.setVisible(false);
			} else if(!showThem && !opened && point.getRelatedCaseReport().isClosed()){
				point.setVisible(false);
			}
		}
		this.map.updateUI();
	}
	
	@Override
	/**
	 * This method overwrites the default mouseClicked method to
	 * add a "select" function. Basically this method checks if your
	 * mouse position was on a mapMarker and if it was, it will be
	 * visible and logically selected.
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
	  		Coordinate geoClickPos = (Coordinate) this.map.getPosition(e.getPoint());
			Point p = this.map.getMapPosition(geoClickPos);
			double X = p.x;
			double Y = p.y;
	  		System.out.println("Left-Click at X="+X+", Y="+Y);
	  		System.out.println("Left-Click at X="+geoClickPos.getLon()+", Y="+geoClickPos.getLat());
	  	    Iterator<GeoPoint> i = this.currentPoints.iterator();
	  	    double centerX, centerY,shortestDistance=Double.MAX_VALUE;
	  	    Point markerPosition = null;
	  	    GeoPoint mapMarker = null;
	  	    double newDistance;
	  	    while (i.hasNext()) {

	  	        mapMarker = (GeoPoint) i.next();

	  	        if(mapMarker != null){
	  	        	
	  	        	newDistance = this.distance(mapMarker.getCoordinate(), geoClickPos);
	  	        	
	  	        	if (newDistance < shortestDistance){
	  	        		shortestDistance = newDistance;
  	            		this.setSelectedMarker(mapMarker);
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
	  	    	Main.mainframeController.mainframe.reportList.setSelectedValue(this.selectedMapMarker.getRelatedCaseReport(), true);
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
	
	/**
	 * calculates the euclidean distance
	 * 
	 * @param c1 start coordinate
	 * @param c2 target coordinate
	 * @return the euclidean distance of two coordinates
	 */
	private double distance(Coordinate c1, Coordinate c2){
		return (c1.getLat()-c2.getLat())*(c1.getLat()-c2.getLat())+(c1.getLon()-c2.getLon())*(c1.getLon()-c2.getLon());
	}
	
	/**
	 * This method will deselect the currentSelected mapMarker
	 * visually and logically.
	 */
	private void clearSelectedMapMarker() {
		this.setDefaultColor(this.selectedMapMarker);
		Main.mainframeController.mainframe.reportList.clearSelection();
		Main.dataBase.getCurrentSelected().setCurrentSelected(false);
		Main.dataBase.setCurrentSelected(null);
		this.selectedMapMarker = null;
	}
	
	/**
	 * This method will select the given mapMarker visually
	 * and logically 
	 * 
	 * @param mapMarker
	 */
	public void setSelectedMarker(GeoPoint mapMarker){
		if(Main.dataBase.getCurrentSelected()!= null){
			Main.dataBase.getCurrentSelected().setCurrentSelected(false);
		}
		mapMarker.getRelatedCaseReport().setCurrentSelected(true);
		Main.dataBase.setCurrentSelected(mapMarker.getRelatedCaseReport());
//		this.setDefaultColor(this.selectedMapMarker);
		this.selectedMapMarker = mapMarker;
		this.setDefaultColor(mapMarker);
//		this.selectedMapMarker.setBackColor(Color.PINK);
  		this.map.removeMapMarker(this.selectedMapMarker);
  		this.map.addMapMarker(this.selectedMapMarker);
  		this.map.repaint();
	}
	
	/**
	 * This method will determine and set the original color
	 * of a given mapMarker.
	 * 
	 * @param m this mapMarker will be colored in its original color.
	 */
	private void setDefaultColor(MapMarker m) {
		if(m != null){
			Calendar cal = Calendar.getInstance();
			for(CaseReport cR : Main.dataBase.getCurrentData()){
				if(cR.getPoint() != null){
					if(cR.getPoint().getLat() == m.getLat() && cR.getPoint().getLon() == m.getLon()){
						int dayOfWeek;
						cal.setTime(cR.getDateOpened());
						dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
						if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 3){
							cR.getPoint().setColor(Main.alphaColor);
							cR.getPoint().setBackColor(Main.alphaColor);
						} else {
							cR.getPoint().setColor(Color.BLACK);
							cR.getPoint().setBackColor(Main.weekDayColors[dayOfWeek-1]);
						}
					}
				}
			}
		}
	}

	public void setMapLocation(double lat, double lon, int zoom) {
		this.map.setDisplayPosition(new Coordinate(lat, lon), zoom);
	}
	
	public void setZoom(boolean isActivated){
		((GeoMapViewer)this.map).setZoomActivated(isActivated);
	}
	
	public void createCellMatrix(int xResolution, int yResolution, Date fromDate, Date toDate) {
		GridController gridController = new GridController();
		//Viewport:
		Coordinate coords1 = (Coordinate)this.map.getPosition(0,0);
		Coordinate coords2 = (Coordinate)map.getPosition(map.getWidth(),map.getHeight());
		System.out.println("Grid Boundary Points:\nP1:("+coords1.getLon()+"x,"+coords1.getLat()+"y)\nP2:("+coords2.getLon()+"x,"+coords2.getLat()+"y)");
		gridController.analyze(xResolution, yResolution, fromDate, toDate, coords1, coords2);
	}
	
	public int getMapWidth(){
		return this.map.getWidth();
	}
	
	public int getMapHeight(){
		return this.map.getHeight();
	}
		
	public void setheatMapImages(ArrayList<BufferedImage> imageList){
		this.heatMapImages = imageList;
	}

	public void loadHeatMapImage(int index) {
		this.map.removeMapMarker(this.currentHeatMapImageMarker);
		this.currentHeatMapImageMarker = new ImageMapMarker((Coordinate)this.map.getPosition(0, 0), this.heatMapImages.get(index));
		this.map.addMapMarker(this.currentHeatMapImageMarker);
	}

	public void setShowHeatMap(boolean isVisible) {
		this.currentHeatMapImageMarker.setVisible(isVisible);
	}
}
