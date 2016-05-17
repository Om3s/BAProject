package mvc.model;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Gridmodel {
	private static Gridmodel instance;
	public Coordinate topLeftCornerDot,botRightCornerDot;
	private int xResolution,yResolution;
	private double xRange, yRange, gridWidth, gridHeight;
	private GridModelData data;
	private Rectangle2D[][] gridRectangleMatrix;
	
	public static Gridmodel getInstance(){
		if(Gridmodel.instance == null){
			Gridmodel.instance = new Gridmodel();
		}
		return Gridmodel.instance;
	}
	
	private Gridmodel(){
		
	}
	
	public void init(int xResolution, int yResolution, Coordinate topLeftCornerDot, Coordinate botRightCornerDot){
		this.xResolution = xResolution;
		this.yResolution = yResolution;
		this.topLeftCornerDot = topLeftCornerDot;
		this.botRightCornerDot = botRightCornerDot;
		this.data = new GridModelData(1.0, -1); //-1 means its the current dataInterval
		this.recalculateValues();
		this.createGridRectangles();
	}
	
	private void recalculateValues() {
		this.xRange = Math.abs(this.topLeftCornerDot.getLon() - this.botRightCornerDot.getLon());
		this.yRange = Math.abs(this.topLeftCornerDot.getLat() - this.botRightCornerDot.getLat());
		this.gridWidth = xRange / (double)xResolution;
		this.gridHeight = yRange / (double)yResolution;
		if(this.topLeftCornerDot.getLon() >= this.botRightCornerDot.getLon()){
			this.gridWidth *= (-1);
		}
		if(this.topLeftCornerDot.getLat() >= this.botRightCornerDot.getLat()){
			this.gridHeight *= (-1);
		}
	}

	public void createGridRectangles(){
		this.gridRectangleMatrix = new Rectangle2D[xResolution][yResolution];
		for(int y=0; y<this.yResolution; y++){ //rows
			for(int x=0; x<this.xResolution; x++){ //columns
				this.gridRectangleMatrix[x][y] = new Rectangle2D.Double(this.topLeftCornerDot.getLon() + this.gridWidth*x, this.topLeftCornerDot.getLat() + this.gridHeight*y, this.gridWidth, this.gridHeight);
			}
		}
	}

	public Rectangle2D[][] getGridRectangleMatrix() {
		return this.gridRectangleMatrix;
	}

	public GridModelData getData() {
		return data;
	}

	public void setData(GridModelData data) {
		this.data = data;
	}
	
	public int getXResolution(){
		return this.xResolution;
	}
	
	public int getYResolution(){
		return this.yResolution;
	}
}
