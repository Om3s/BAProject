package mvc.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import mvc.main.Main;
import mvc.model.GridModelData;
import mvc.model.Gridmodel;

public class GridController {
	private ArrayList<GridModelData> pastGridModelData;
	
	public GridController(){
		this.pastGridModelData = new ArrayList<GridModelData>();
	}
	
	public void analyze(int xResolution, int yResolution, Date fromDate, Date toDate, Coordinate topLeft, Coordinate botRight){
		Gridmodel.getInstance().init(xResolution, yResolution, topLeft, botRight);
		
		Date fDate = fromDate;
		Date tDate = toDate;
		Gridmodel.getInstance().getData().setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
		//Write currentMatrix to console:
		System.out.println("OCCURENCIES:");
		for(int y=0; y<Gridmodel.getInstance().getData().getDataMatrix().length; y++){
			for(int x=0; x<Gridmodel.getInstance().getData().getDataMatrix()[0].length;x++){
				System.out.print(" "+Gridmodel.getInstance().getData().getDataMatrix()[x][y]+" ");
			}
			System.out.println("");
		}
		
		
		
//		double weight = 1.0; //TODO determine weight properly and implement analysis of past data:
//		GridModelData newData = new GridModelData(weight);
//		newData.setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
//		this.pastGridModelData.add(new GridModelData(weight));
	}
	
	private int[][] countGridOccurenciesFromTo(Date fromDate, Date toDate) {
		int[][] result = null;
		try {
			result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, "All categories", Gridmodel.getInstance().getGridRectangleMatrix());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<GridModelData> getPastGridModelData() {
		return pastGridModelData;
	}

	public void setPastGridModelData(ArrayList<GridModelData> pastGridModelData) {
		this.pastGridModelData = pastGridModelData;
	}
}
