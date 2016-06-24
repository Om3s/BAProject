package mvc.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.GeoMapViewer;
import mvc.model.GeoPoint;
import mvc.model.GrayOSMTileSource;
import mvc.model.ImageMapMarker;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

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
	private GridController gridController;
	private Coordinate upperLeftFixPoint,lowerRightFixPoint;
	//selection variables:
	private boolean isDraggingSelection,isPotentialDrag;
	public boolean wasPreviouslyHeatmap,isInSelectionMode;
	private Coordinate dragModeStartMousePosition,dragModeCurrentMousePosition,dragModeEndMousePosition;
	private ArrayList<Coordinate> selectionRectanglePath;
	private Point topLeftCorner,botRightCorner,topRightCorner,botLeftCorner;
	private MapPolygon selectionRectanglePolygon;
	
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
		this.isDraggingSelection = false;
		this.isPotentialDrag = false;
		this.isInSelectionMode = false;
		this.wasPreviouslyHeatmap = false;
		this.map.setTileSource(new GrayOSMTileSource.BWMapnik());
		for(CaseReport cR : Main.dataBase.getCurrentData()){
			this.map.addMapMarker(cR.getPoint());
		}
		this.setUpperLeftFixPoint(new Coordinate(42.181723498412396,-88.0828857421875));
		this.setLowerRightFixPoint(new Coordinate(41.48389104267176,-87.25341796875));
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
				if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 7){
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
			if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 7){
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
		if(Main.mainframeController.mainframe.filtermenu_analysis_panel_chckbxHeatmap.isSelected()){
			//not clear what clicks should do in heatMap Mode
		} else {
			if(e.getButton() == MouseEvent.BUTTON1){
		  		Coordinate geoClickPos = (Coordinate) this.map.getPosition(e.getPoint());
				Point p = this.map.getMapPosition(geoClickPos);
				double X = p.x;
				double Y = p.y;
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
			}
		}
		if(e.getButton() == MouseEvent.BUTTON1){
			//DEBUG OUTPUT:
	  		Coordinate geoClickPos = (Coordinate) this.map.getPosition(e.getPoint());
			Point p = this.map.getMapPosition(geoClickPos);
			double X = p.x;
			double Y = p.y;
	  		System.out.println("Left-Click at X="+X+", Y="+Y);
	  		System.out.println("Left-Click at X="+geoClickPos.getLon()+", Y="+geoClickPos.getLat()+", Zoomlevel: "+this.map.getZoom());
		} else if(e.getButton() == MouseEvent.BUTTON2){
			//DEBUG OUTPUT:
			Point p = e.getPoint();
			int X = p.x;
			int Y = p.y;
	  		System.out.println("Mid-Click at X="+X+", Y="+Y);
		} else if(e.getButton() == MouseEvent.BUTTON3){
			System.out.println("isInSelectionMode "+this.isInSelectionMode);
			if(this.isInSelectionMode){
				if(this.selectionRectanglePolygon != null){
					this.map.removeMapPolygon(this.selectionRectanglePolygon);
					this.selectionRectanglePolygon = null;
				}
				System.out.println("wasPreviouslyHeatmap: "+this.wasPreviouslyHeatmap);
				this.isInSelectionMode = false;
				if(this.wasPreviouslyHeatmap){
					Main.mainframeController.mainframe.filtermenu_analysis_panel_chckbxHeatmap.setSelected(true);
				} else {
					Main.mainframeController.refreshMapData();
				}
				Main.mainframeController.mainframe.filtermenu_analysis_panel_chckbxHeatmap.setEnabled(true);
			}
			Main.mainframeController.refreshChart();
			//DEBUG OUTPUT:
			Point p = e.getPoint();
			int X = p.x;
			int Y = p.y;
	  		System.out.println("Right-Click at X="+X+", Y="+Y);
		}
  	    this.map.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		if(SwingUtilities.isLeftMouseButton(e)){
			if(!this.isDraggingSelection && this.isPotentialDrag){
				this.isDraggingSelection = true;
			}
			if(this.isDraggingSelection){
				this.dragModeCurrentMousePosition = (Coordinate)this.map.getPosition(e.getPoint());
				this.topLeftCorner = this.map.getMapPosition(this.dragModeStartMousePosition);
				this.botRightCorner = this.map.getMapPosition(this.dragModeCurrentMousePosition);
				this.topRightCorner = new Point(botRightCorner.x,topLeftCorner.y);
				this.botLeftCorner = new Point(topLeftCorner.x,botRightCorner.y);
				if(this.selectionRectanglePolygon != null){
					this.map.removeMapPolygon(this.selectionRectanglePolygon);
				}
				this.selectionRectanglePath = new ArrayList<Coordinate>();
				this.selectionRectanglePath.add((Coordinate)this.map.getPosition(topLeftCorner));
				this.selectionRectanglePath.add((Coordinate)this.map.getPosition(topRightCorner));
				this.selectionRectanglePath.add((Coordinate)this.map.getPosition(botRightCorner));
				this.selectionRectanglePath.add((Coordinate)this.map.getPosition(botLeftCorner));
				this.selectionRectanglePath.add((Coordinate)this.map.getPosition(topLeftCorner));
				this.selectionRectanglePolygon = new MapPolygonImpl(this.selectionRectanglePath);
				this.map.addMapPolygon(this.selectionRectanglePolygon);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(SwingUtilities.isLeftMouseButton(e)){
			System.out.println("left pressed");
			this.isPotentialDrag = true;
			this.dragModeStartMousePosition = (Coordinate)this.map.getPosition(e.getPoint());
		} else if (SwingUtilities.isRightMouseButton(e)){
			System.out.println("right pressed");
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		if(this.isDraggingSelection){
			this.isInSelectionMode = true;
			this.dragModeEndMousePosition = (Coordinate)this.map.getPosition(e.getPoint());
			if(!this.wasPreviouslyHeatmap){
				this.wasPreviouslyHeatmap = Main.mainframeController.mainframe.filtermenu_analysis_panel_chckbxHeatmap.isSelected();
			}
			Main.mainframeController.wasInSelectionMode = true;
			if(this.wasPreviouslyHeatmap){
				Main.mainframeController.mainframe.filtermenu_analysis_panel_chckbxHeatmap.setSelected(false);
			}
			try {
				Main.dataBase.selectSpatialWeekdaysCasesBetweenDatesToCurrentData(Main.mainframeController.getGlobalFromDate(), Main.mainframeController.getGlobalToDate(), Main.mainframeController.getIgnoredWeekdaysAsList(), Main.mainframeController.getCurrentCategory(), Main.mainframeController.getIgnoredDayTimesAsList(), this.dragModeStartMousePosition, this.dragModeEndMousePosition);
				System.out.println("currentData#: "+Main.dataBase.getCurrentData().size());
				Main.mapController.loadPoints(Main.dataBase.getCurrentData());
				Main.mainframeController.fillReportListWith(Main.mainframeController.mainframe.reportList_panel_filter_checkBoxOpen.isSelected(), Main.mainframeController.mainframe.reportList_panel_filter_checkBoxClosed.isSelected());
				Main.mapController.setShowCurrentPoints(true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Main.mainframeController.refreshChart();
		this.isPotentialDrag = false;
		this.isDraggingSelection = false;
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
						if(Main.mainframeController.getSelectedWeekdaysAsList().size() > 7){
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
	
	public void createCellMatrix(int xResolution, int yResolution, Date fromDate, Date toDate, int intervalAmount) {
		if(this.gridController == null){
			this.gridController = new GridController();
		}
		System.out.println("Grid Boundary Points:\nP1:("+this.upperLeftFixPoint.getLon()+"x,"+this.upperLeftFixPoint.getLat()+"y)\nP2:("+this.lowerRightFixPoint.getLon()+"x,"+this.lowerRightFixPoint.getLat()+"y)");
		this.gridController.analyze(xResolution, yResolution, fromDate, toDate, this.upperLeftFixPoint, this.lowerRightFixPoint, intervalAmount);
		this.loadHeatMapImage(0);
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
		if(this.gridController != null){
			if(index == 0){
				this.gridController.matrixCalculations();
			}
			this.gridController.createHeatMap(index);
		}
		if(this.heatMapImages != null && this.heatMapImages.size() > 0){
			if(this.heatMapImages.get(index) != null){
				this.currentHeatMapImageMarker = new ImageMapMarker(this.upperLeftFixPoint, this.heatMapImages.get(index));
				this.map.addMapMarker(this.currentHeatMapImageMarker);
			}
		}
	}

	public void setShowHeatMap(boolean isVisible) {
		if(this.currentHeatMapImageMarker != null){
			this.currentHeatMapImageMarker.setVisible(isVisible);
			if(isVisible){
				//TODO draw Legend on Map ALSO consider update the graphic on panning
			} else {
				
			}
		}
	}

	public int[] getIntervalData() {
		return this.gridController.getIntervalOverallAmounts();
	}

	public double[] getWeightsOfIntervals() {
		return this.gridController.getWeightsFromIntervals();
	}

	public void setIntervalWeight(int index, double newWeight) {
		this.gridController.getPastGridModelData().get(index).setWeight(newWeight);
	}

	public void updateTimelineWeights(double[] weightsOfData) {
		this.gridController.setDataWeights(weightsOfData);
		
	}

	public Coordinate getUpperLeftFixPoint() {
		return upperLeftFixPoint;
	}

	public void setUpperLeftFixPoint(Coordinate upperLeftFixPoint) {
		this.upperLeftFixPoint = upperLeftFixPoint;
	}

	public Coordinate getLowerRightFixPoint() {
		return lowerRightFixPoint;
	}

	public void setLowerRightFixPoint(Coordinate lowerRightFixPoint) {
		this.lowerRightFixPoint = lowerRightFixPoint;
	}
	
	public Point getMapPosition(Coordinate coord){
		return this.map.getMapPosition(coord,false);
	}
	
	public Coordinate getPosition(Point p){
		return (Coordinate)this.map.getPosition(p);
	}
	
	public Coordinate getCurrentUpperLeftPoint() {
		return this.selectionRectanglePath.get(0);
	}
	
	public Coordinate getCurrentLowerRightPoint() {
		return this.selectionRectanglePath.get(2);
	}
}
