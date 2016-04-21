package mvc.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import mvc.main.Main;
import mvc.model.Gridmodel;

public class GridController {
	private Gridmodel gridModel;
	
	public GridController(){
		
	}
	
	public int[][] countGridOccurenciesFromTo(int xResolution, int yResolution, Date fromDate, Date toDate, Coordinate topLeft, Coordinate botRight) {
		int[][] result = null;
		this.gridModel = new Gridmodel(xResolution, yResolution, topLeft, botRight);
		try {
			result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, "All categories", this.gridModel.getGridRectangleMatrix());
		} catch (IOException e) {
			e.printStackTrace();
		}
		NumberFormat n = NumberFormat.getInstance();
		n.setMaximumFractionDigits(4);
		System.out.println("Test Rectangles:");
		for(int x=0; x<this.gridModel.getGridRectangleMatrix().length; x++){
			for(int y=0; y<this.gridModel.getGridRectangleMatrix()[0].length;y++){
				System.out.print(" [[P1:X:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getX())+",Y:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getY())+",P2:X:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getX()+this.gridModel.getGridRectangleMatrix()[x][y].getWidth())+",Y:"+n.format(this.gridModel.getGridRectangleMatrix()[x][y].getY()+this.gridModel.getGridRectangleMatrix()[x][y].getHeight())+"]] ");
			}
			System.out.println("");
		}
		System.out.println("OCCURENCIES:");
		for(int x=0; x<result.length; x++){
			for(int y=0; y<result[0].length;y++){
				System.out.print(" "+result[x][y]+" ");
			}
			System.out.println("");
		}
		return result;
	}
}
