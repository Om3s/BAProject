package mvc.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
		long dateInterval = toDate.getTime() - fromDate.getTime(); //interval is current date selection
		int intervalAmount = 3; //TODO intervalAmount hardcoded
		Date fDate = fromDate;
		Date tDate = toDate;
		Gridmodel.getInstance().getData().setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
		
		
		
		//TODO use threading over time
		double weight = 1.0; //TODO determine weight properly and implement analysis of past data:
		GridModelData newData;
		for(int i=0; i<intervalAmount; i++){
			fDate = new Date(fromDate.getTime() - dateInterval*i);
			tDate = new Date(fDate.getTime() + dateInterval);
			newData = new GridModelData(1.0 - ((1.0 / intervalAmount) * i)); //TODO Weight calculation is linear at the moment
			newData.setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
			this.pastGridModelData.add(newData);
		}
		int minNegValue = Integer.MIN_VALUE, maxNegValue = Integer.MAX_VALUE, minPosValue = Integer.MAX_VALUE,maxPosValue = Integer.MIN_VALUE;
		int[][] pastDataAverage = this.calculateWeightedAverage(this.pastGridModelData.toArray(new GridModelData[0]));
		int[][] absoluteDataDifference = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		double[][] relativeDataDifference = new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				absoluteDataDifference[x][y] = Gridmodel.getInstance().getData().getDataMatrix()[x][y] - pastDataAverage[x][y];
				if(absoluteDataDifference[x][y] >= 0){ //Positive:
					if(absoluteDataDifference[x][y] > maxPosValue){
						maxPosValue = absoluteDataDifference[x][y];
					}
					if(absoluteDataDifference[x][y] < minPosValue){
						minPosValue = absoluteDataDifference[x][y];
					}
				} else { //Negative:
					if(absoluteDataDifference[x][y] > minNegValue){
						minNegValue = absoluteDataDifference[x][y];
					}
					if(absoluteDataDifference[x][y] < maxNegValue){
						maxNegValue = absoluteDataDifference[x][y];
					}
				}
				if(absoluteDataDifference[x][y] == 0){
					relativeDataDifference[x][y] = 0.0;		
				} else {
					relativeDataDifference[x][y] = (double)absoluteDataDifference[x][y] / (double)Gridmodel.getInstance().getData().getDataMatrix()[x][y];
				}
			}
			System.out.println("");
		}
		double[][] normalizedRelativeMatrix = new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				if(absoluteDataDifference[x][y] >= 0){
					normalizedRelativeMatrix[x][y] = (double)((double)absoluteDataDifference[x][y] - (double)minPosValue)/((double)maxPosValue - (double)minPosValue);	
				} else {
					normalizedRelativeMatrix[x][y] = (double)((double)absoluteDataDifference[x][y] - (double)minNegValue)/((double)maxNegValue - (double)minNegValue) * (-1.0);
				}
			}
		}
		//OUTPUT:
		DecimalFormat dFormat = new DecimalFormat("0.00");
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
		System.out.println("Absolute Difference Occurences:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+absoluteDataDifference[x][y]+" ");
			}
			System.out.println("");
		}
		System.out.println("Relative Difference Occurences:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+Math.round(relativeDataDifference[x][y]*100)+"% ");
			}
			System.out.println("");
		}
		System.out.println("Normalized Relative Differences:");
		for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
				System.out.print(" "+normalizedRelativeMatrix[x][y]+" ");
			}
			System.out.println("");
		}
		System.out.println("maxNeg="+maxNegValue+", minNeg="+minNegValue+", maxPos="+maxPosValue+", minPos="+minPosValue);
		//Create Image:
		ImageController imageController = new ImageController();
		try {
			File imageFile = new File("dat/img/rawMap.png");
			imageFile.createNewFile();
			imageController.drawHeatmap(normalizedRelativeMatrix);
			imageController.saveImage(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int[][] calculateWeightedAverage(GridModelData[] matrices){ //TODO weight is not considered yet
		double[][] tempResult = new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		int[][] result = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		double divider = 0.0;
		//sum all weights for the divider:
		for(GridModelData gMD : matrices){
			divider += gMD.getWeight();
		}
		//sum all matrices up modified by each weight:
		for(int i=0; i<matrices.length; i++){
			for(int x=0; x<Gridmodel.getInstance().getXResolution(); x++){
				for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
					tempResult[x][y] += (double)matrices[i].getDataMatrix()[x][y] * matrices[i].getWeight();
				}
			}
		}
		//divide by the amount of matrices:
		for(int x=0; x<Gridmodel.getInstance().getXResolution(); x++){
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){
				result[x][y] = (int)(tempResult[x][y] / divider);
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
