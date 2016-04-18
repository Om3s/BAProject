package mvc.model;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Gridmodel {
	private int xResolution,yResolution;
	private double xRange, yRange, gridWidth, gridHeight;
	public Point topLeftCornerDot,botRightCornerDot;
	private ArrayList<int[][]> pastGridDataList;
	private double[][] presentGridData;
	private Rectangle2D[][] gridRectangleMatrix;
	
	public Gridmodel(int xResolution, int yResolution, Point topLeftCornerDot, Point botRightCornerDot){
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
		this.xRange = this.topLeftCornerDot.x - this.botRightCornerDot.x;
		this.yRange = this.topLeftCornerDot.y - this.botRightCornerDot.y;
		this.gridWidth = xRange / xResolution;
		this.gridHeight = yRange / yResolution;
	}

	public void createGridRectangles(){
		this.gridRectangleMatrix = new Rectangle2D[xResolution][yResolution];
		for(int x=0; x<xResolution; x++){ //rows
			for(int y=0; y<yResolution; y++){ //columns
				this.gridRectangleMatrix[x][y] = new Rectangle2D.Double(this.topLeftCornerDot.x + this.gridWidth*x, this.topLeftCornerDot.y + this.gridHeight*y, this.topLeftCornerDot.x + this.gridWidth, this.topLeftCornerDot.y + this.gridHeight);
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

	public double[][] getPresentGridData() {
		return presentGridData;
	}

	public void setPresentGridData(double[][] presentGridData) {
		this.presentGridData = presentGridData;
	}
}
