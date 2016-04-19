package mvc.model;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Gridmodel {
	private int xResolution,yResolution;
	private double xRange, yRange, gridWidth, gridHeight;
	public double[] topLeftCornerDot,botRightCornerDot;
	private ArrayList<int[][]> pastGridDataList;
	private double[][] presentGridData;
	private Rectangle2D[][] gridRectangleMatrix;
	
	public Gridmodel(int xResolution, int yResolution, double[] topLeftCornerDot, double[] botRightCornerDot){
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
		this.xRange = this.topLeftCornerDot[0] - this.botRightCornerDot[0];
		this.yRange = this.topLeftCornerDot[1] - this.botRightCornerDot[1];
		System.out.println("xRange: "+this.xRange);
		System.out.println("yRange: "+this.yRange);
		this.gridWidth = xRange / xResolution;
		this.gridHeight = yRange / yResolution;
	}

	public void createGridRectangles(){
		this.gridRectangleMatrix = new Rectangle2D[xResolution][yResolution];
		for(int x=0; x<xResolution; x++){ //rows
			for(int y=0; y<yResolution; y++){ //columns
				this.gridRectangleMatrix[x][y] = new Rectangle2D.Double(this.topLeftCornerDot[0] + this.gridWidth*x, this.topLeftCornerDot[1] + this.gridHeight*y, this.gridWidth, this.gridHeight);
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
