package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import mvc.main.Main;
import mvc.model.GridModelData;
import mvc.model.Gridmodel;

public class GridController {
	private ArrayList<GridModelData> pastGridModelData;
	private ImageController imageController = new ImageController();
	private ArrayList<double[][]> normalizedRelativeMatrix,relativeDataDifference;
	private double minValue, maxValue;
	private int intervalAmount;
	private boolean intervalChanged;
	private double[] weightsOfData;
	
	public GridController(){
		this.pastGridModelData = new ArrayList<GridModelData>();
	}
	
	public void analyze(int xResolution, int yResolution, Date fromDate, Date toDate, Coordinate topLeft, Coordinate botRight, int newIntervalAmount){
		this.pastGridModelData = new ArrayList<GridModelData>();
		Gridmodel.getInstance().init(xResolution, yResolution, topLeft, botRight);
		long dateInterval = toDate.getTime() - fromDate.getTime(); //interval is current date selection
		Date fDate = fromDate;
		Date tDate = toDate;
		Gridmodel.getInstance().getData().setDataMatrix(this.countGridOccurenciesFromTo(fDate, tDate));
		if(this.intervalAmount != newIntervalAmount){
			this.intervalAmount = newIntervalAmount;
			this.intervalChanged = true;
			this.weightsOfData = new double[this.intervalAmount];
		} else {
			this.intervalChanged = false;
		}
		
		//TODO use threading over time
		double weight = 1.0; //TODO determine weight properly and implement analysis of past data:
		GridModelData newData;
		long startTime = System.currentTimeMillis(), queryStart, currentTime;
		System.out.println("Begin gridCountQuery...(0/"+this.intervalAmount+")");
		for(int i=0; i<this.intervalAmount; i++){
			queryStart = System.currentTimeMillis();
			fDate = new Date(fromDate.getTime() - dateInterval*(i+1));
			tDate = new Date(fDate.getTime() + dateInterval);
			if(this.intervalChanged){
				this.weightsOfData[i] = 1.0 - ((1.0 / this.intervalAmount) * i); //TODO weight calculation is linear atms
			}
			newData = new GridModelData(this.weightsOfData[i], this.countGridOccurenciesFromTo(fDate, tDate));
			this.pastGridModelData.add(newData);
			currentTime = System.currentTimeMillis();
			System.out.print("("+(i+1)+"/"+this.intervalAmount+") in "+(((double)currentTime-(double)queryStart)/1000)+"sec, ");
		}
		currentTime = System.currentTimeMillis();
		System.out.println("\nQuery finished in "+(((double)currentTime-(double)startTime)/1000)+"sec");
	}
	
	public void matrixCalculations() {
		double	minNegValue,maxNegValue,minPosValue,maxPosValue;
		minNegValue = -1.0 * Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.getText()) / 100.0;
		maxNegValue = -1.0 * Main.mainframeController.mainframe.filtermenu_analysis_panel_threshold_slider.getValue() / 100.0;
		minPosValue = Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.getText()) / 100.0;
		maxPosValue = Main.mainframeController.mainframe.filtermenu_analysis_panel_threshold_slider.getValue() / 100.0;
		this.minValue = Double.MAX_VALUE;
		this.maxValue = Double.MIN_VALUE;
		
		int[][] pastDataAverage = this.calculateWeightedAverage(this.pastGridModelData.toArray(new GridModelData[0]));
		int[][] absoluteDataDifference = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];

		this.normalizedRelativeMatrix = new ArrayList<double[][]>();
		this.relativeDataDifference = new ArrayList<double[][]>();
				
		for(int index=0;index<this.pastGridModelData.size();index++){
			this.relativeDataDifference.add(new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()]);
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){ //Fill absolute- and relativeDifferenceMatrices:
				for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
					absoluteDataDifference[x][y] = Gridmodel.getInstance().getData().getDataMatrix()[x][y] - pastDataAverage[x][y];
					if(absoluteDataDifference[x][y] != 0 && Gridmodel.getInstance().getData().getDataMatrix()[x][y] != 0){
						this.relativeDataDifference.get(index)[x][y] = (double)absoluteDataDifference[x][y] / (double)Gridmodel.getInstance().getData().getDataMatrix()[x][y];
					} else {
						this.relativeDataDifference.get(index)[x][y] = 0.0;	
					}
					if(Math.abs(this.relativeDataDifference.get(index)[x][y]) > this.maxValue){
						this.maxValue = Math.abs(this.relativeDataDifference.get(index)[x][y]);
					}
					if(Math.abs(this.relativeDataDifference.get(index)[x][y]) < this.minValue){
						this.minValue = Math.abs(this.relativeDataDifference.get(index)[x][y]);
					}
				}
			}
			this.normalizedRelativeMatrix.add(new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()]);
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){ //fill the normalized RelativeMatrix
				for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
					if(this.relativeDataDifference.get(index)[x][y] > maxPosValue){
						this.relativeDataDifference.get(index)[x][y] = maxPosValue;
					} else if (this.relativeDataDifference.get(index)[x][y] < maxNegValue) {
						this.relativeDataDifference.get(index)[x][y] = maxNegValue;
					} 
					if(this.relativeDataDifference.get(index)[x][y] >= 0){
						this.normalizedRelativeMatrix.get(index)[x][y] = (double)((double)this.relativeDataDifference.get(index)[x][y] - minPosValue)/(maxPosValue - minPosValue);
					} else {
						this.normalizedRelativeMatrix.get(index)[x][y] = (double)((double)this.relativeDataDifference.get(index)[x][y] - minNegValue)/(maxNegValue - minNegValue) * (-1.0);
					}
				}
			}
		}
	}
	
	public void recommendedSliderValues(){
		Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.setText(""+(int)(this.minValue*100));
		Main.mainframeController.mainframe.filtermenu_analysis_panel_upperPosThreshold_textfield.setText(""+(int)(this.maxValue*105));
		Main.mainframeController.refreshHeatMapSlider();
	}

	public void createHeatMap(int index) {
		try {
			this.imageController.drawHeatmap(this.normalizedRelativeMatrix.get(index), index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.imageController.loadAllHeatMaps();
	}

	private int[][] calculateWeightedAverage(GridModelData[] matrices){ //TODO weight is linear at the moment
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
			result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, "All categories", Gridmodel.getInstance().getGridRectangleMatrix(), Main.mainframeController.getIgnoredDayTimesAsList(), Main.mainframeController.getIgnoredWeekdaysAsList());
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

	public int[] getIntervalOverallAmounts() {
		int[] resultList = new int[Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_intervallAmount_textfield.getText())+1];
		resultList[0] = 0;
		for(int y=0;y<Gridmodel.getInstance().getYResolution();y++){
			for(int x=0;x<Gridmodel.getInstance().getXResolution();x++){
				resultList[0] += Gridmodel.getInstance().getData().getDataMatrix()[x][y];
			}
		}
		for(int i=1;i<resultList.length;i++){
			resultList[i] = 0;
			for(int y=0;y<Gridmodel.getInstance().getYResolution();y++){
				for(int x=0;x<Gridmodel.getInstance().getXResolution();x++){
					resultList[i] += this.pastGridModelData.get(i-1).getDataMatrix()[x][y];
				}
			}
		}
		return resultList;
	}
	
	public double[] getWeightsFromIntervals(){
		return this.weightsOfData;
	}

	public void setDataWeights(double[] newWeights) {
		this.weightsOfData = newWeights;
		for(int i=1;i<this.weightsOfData.length;i++){
			this.pastGridModelData.get(i).setWeight(this.weightsOfData[i]);
		}
	}
}
