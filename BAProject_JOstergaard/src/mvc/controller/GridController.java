package mvc.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mvc.main.Main;
import mvc.model.Gridmodel;

public class GridController {
	private Gridmodel gridModel;
	
	public GridController(){
		
	}
	
	public int[][] countGridOccurenciesFromTo() throws ParseException, IOException{
		int[][] result = null;
		double[] topLeft = {42.218347726793304,-88.25042724609375};
		double[] botRight = {41.50240661583931,-87.04742431640625};
		this.gridModel = new Gridmodel(1, 1, topLeft, botRight);
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy KK:mm:ss a").parse("01/01/2016 12:00:01 AM");
		Date toDate = new SimpleDateFormat("dd/MM/yyyy KK:mm:ss a").parse("30/01/2016 12:00:01 AM");
		result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, "All categories", this.gridModel.getGridRectangleMatrix());
		System.out.println("Test Rectangles:");
		for(int x=0; x<this.gridModel.getGridRectangleMatrix().length; x++){
			for(int y=0; y<this.gridModel.getGridRectangleMatrix()[0].length;y++){
				System.out.print(" [[X:"+this.gridModel.getGridRectangleMatrix()[x][y].getX()+", Y:"+this.gridModel.getGridRectangleMatrix()[x][y].getY()+", W:"+this.gridModel.getGridRectangleMatrix()[x][y].getWidth()+", H:"+this.gridModel.getGridRectangleMatrix()[x][y].getHeight()+"]] ");
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
