package mvc.model;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Gridmodel {
	public Coordinate topLeftCornerDot,botRightCornerDot;
	private int xResolution,yResolution;
	private double xRange, yRange, gridWidth, gridHeight, weight;
	private int[][] data;
	private Rectangle2D[][] gridRectangleMatrix;
	
	public Gridmodel(int xResolution, int yResolution, Coordinate topLeftCornerDot, Coordinate botRightCornerDot){
		this.xResolution = xResolution;
		this.yResolution = yResolution;
		this.topLeftCornerDot = topLeftCornerDot;
		this.botRightCornerDot = botRightCornerDot;
		this.init();
	}
	
	private void init(){
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
		for(int y=0; y<xResolution; y++){ //rows
			for(int x=0; x<yResolution; x++){ //columns
				this.gridRectangleMatrix[x][y] = new Rectangle2D.Double(this.topLeftCornerDot.getLon() + this.gridWidth*x, this.topLeftCornerDot.getLat() + this.gridHeight*y, this.gridWidth, this.gridHeight);
			}
		}
	}

	public Rectangle2D[][] getGridRectangleMatrix() {
		return this.gridRectangleMatrix;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int[][] getData() {
		return data;
	}

	public void setData(int[][] data) {
		this.data = data;
	}
}
