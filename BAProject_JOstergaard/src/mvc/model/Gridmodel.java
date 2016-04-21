package mvc.model;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Gridmodel {
	private int xResolution,yResolution;
	private double xRange, yRange, gridWidth, gridHeight;
	public Coordinate topLeftCornerDot,botRightCornerDot;
	private ArrayList<int[][]> pastGridDataList;
	private int[][] presentGridData;
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
		this.pastGridDataList = new ArrayList<int[][]>();
		this.createGridRectangles();
	}
	
	private void recalculateValues() {
		this.xRange = this.topLeftCornerDot.getLat() - this.botRightCornerDot.getLat();
		this.yRange = this.topLeftCornerDot.getLon() - this.botRightCornerDot.getLon();
		System.out.println("xRange: "+this.xRange);
		System.out.println("yRange: "+this.yRange);
		this.gridWidth = xRange / (double)xResolution;
		this.gridHeight = yRange / (double)yResolution;
	}

	public void createGridRectangles(){
		this.gridRectangleMatrix = new Rectangle2D[xResolution][yResolution];
		for(int x=0; x<xResolution; x++){ //rows
			for(int y=0; y<yResolution; y++){ //columns
				if(this.topLeftCornerDot.getLat() >= this.botRightCornerDot.getLat()){
					this.gridWidth *= (-1);
				}
				if(this.topLeftCornerDot.getLon() <= this.botRightCornerDot.getLon()){
					this.gridHeight *= (-1);
				}
				this.gridRectangleMatrix[x][y] = new Rectangle2D.Double(this.topLeftCornerDot.getLat() + this.gridWidth*x, this.topLeftCornerDot.getLon() + this.gridHeight*y, this.gridWidth, this.gridHeight);
			}
		}
	}

	public Rectangle2D[][] getGridRectangleMatrix() {
		return this.gridRectangleMatrix;
	}

	public ArrayList<int[][]> getPastGridDataList() {
		return pastGridDataList;
	}

	public void setPastGridDataList(ArrayList<int[][]> pastGridDataList) {
		this.pastGridDataList = pastGridDataList;
	}
	
	public void addGridData(int[][] gridData){
		this.pastGridDataList.add(gridData);
	}

	public int[][] getPresentGridData() {
		return presentGridData;
	}

	public void setPresentGridData(int[][] presentGridData) {
		this.presentGridData = presentGridData;
	}
}
