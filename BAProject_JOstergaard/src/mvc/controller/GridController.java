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
		long dateInterval = toDate.getTime() - fromDate.getTime(); //TODO intervall hardcoded
		int intervalAmount = 6; //TODO hardcoded
		Date fDate = fromDate;
		Date tDate = toDate;
		Gridmodel.getInstance().getData().setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
		
		
		
		//TODO use threading over time
		double weight = 1.0; //TODO determine weight properly and implement analysis of past data:
		GridModelData newData;
		for(int i=0; i<intervalAmount; i++){
			fDate = new Date(fromDate.getTime() - dateInterval*i);
			tDate = new Date(fDate.getTime() + dateInterval);
			newData = new GridModelData(1.0 - ((1.0 / intervalAmount) * (i+1)));
			newData.setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
			this.pastGridModelData.add(newData);
		}
		int[][] pastDataAverage = this.calculateWeightedAverage(this.pastGridModelData.toArray(new GridModelData[0]));
		int[][] dataDifference = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				dataDifference[x][y] = Gridmodel.getInstance().getData().getDataMatrix()[x][y] - pastDataAverage[x][y];
			}
			System.out.println("");
		}
		//OUTPUT:
		System.out.println("Present Occurencies:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+Gridmodel.getInstance().getData().getDataMatrix()[x][y]+" ");
			}
			System.out.println("");
		}
		System.out.println("Average Past Occurences:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+pastDataAverage[x][y]+" ");
			}
			System.out.println("");
		}
		System.out.println("Difference Occurences:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+dataDifference[x][y]+" ");
			}
			System.out.println("");
		}
	}
	
	private int[][] calculateWeightedAverage(GridModelData[] matrices){ //TODO weight is not considered yet
		double[][] tempResult = new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		int[][] result = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		//sum all matrices up modified by each weight:
		for(int i=0; i<matrices.length; i++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution(); x++){
				for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
					tempResult[x][y] += (double)matrices[i].getDataMatrix()[x][y];
				}
			}
		}
		//divide by the amount of matrices:
		for(int x=0; x<Gridmodel.getInstance().getXResolution(); x++){
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
				result[x][y] = (int)(tempResult[x][y] / matrices.length);
			}
		}
		return result;
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
