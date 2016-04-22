package mvc.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import mvc.main.Main;
import mvc.model.Gridmodel;

public class GridController {
	private Gridmodel presentGridModel;
	private ArrayList<Gridmodel> pastGridModels;
	
	public GridController(){
		this.pastGridModels = new ArrayList<Gridmodel>();
	}
	
	public void analyze(int xResolution, int yResolution, Date fromDate, Date toDate, Coordinate topLeft, Coordinate botRight){
		this.presentGridModel =  new Gridmodel(xResolution, yResolution, topLeft, botRight);
		
		Date fDate = fromDate;
		Date tDate = toDate;
		this.countGridOccurenciesFromTo(this.presentGridModel, fDate, tDate);
	}
	
	private int[][] countGridOccurenciesFromTo(Gridmodel model, Date fromDate, Date toDate) {
		int[][] result = null;
		try {
			result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, "All categories", model.getGridRectangleMatrix());
		} catch (IOException e) {
			e.printStackTrace();
		}
//		NumberFormat n = NumberFormat.getInstance();
//		n.setMaximumFractionDigits(4);
//		System.out.println("Test Rectangles:");
//		for(int y=0; y<this.gridModel.getGridRectangleMatrix().length; y++){
//			for(int x=0; x<this.gridModel.getGridRectangleMatrix()[0].length;x++){
//				System.out.print(" [[P1:X:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getX())+",Y:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getY())+",P2:X:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getMaxX())+",Y:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getMaxY())+"]] ");
//			}
//			System.out.println("");
//		}
		System.out.println("OCCURENCIES:");
		for(int y=0; y<result.length; y++){
			for(int x=0; x<result[0].length;x++){
				System.out.print(" "+result[x][y]+" ");
			}
			System.out.println("");
		}
		return result;
	}

	public ArrayList<Gridmodel> getPastGridModels() {
		return pastGridModels;
	}

	public void setPastGridModels(ArrayList<Gridmodel> pastGridModels) {
		this.pastGridModels = pastGridModels;
	}
}
