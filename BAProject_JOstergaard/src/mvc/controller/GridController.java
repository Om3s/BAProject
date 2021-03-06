package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import mvc.main.Main;
import mvc.model.GridModelData;
import mvc.model.Gridmodel;

public class GridController {
	private ArrayList<GridModelData> pastGridModelData;
	private ImageController imageController = new ImageController();
	private ArrayList<double[][]> normalizedRelativeMatrix,relativeDataDifference;
	private double minRelativeValue, maxRelativeValue,recommendedSliderValue;
	private int intervalAmount;
	private boolean intervalChanged;
	private double[] weightsOfData;
	
	public GridController(){
		this.pastGridModelData = new ArrayList<GridModelData>();
	}
	
	class AnalyzeThread extends Thread { // TODO analyzeThreads not working!!!
		private Thread t;
		private int threadInterval;
		private GridModelData newData;
		private Date fDate,tDate;
		private boolean isFinished;
		
		AnalyzeThread(Date fDate, Date tDate){
			this.threadInterval = -1;
			this.fDate = fDate;
			this.tDate = tDate;
			this.isFinished = false;
			System.out.println("Thread "+this.threadInterval+" created");
		}
		
		AnalyzeThread(Date fDate, Date tDate, int interval){
			this.threadInterval = interval;
			this.fDate = fDate;
			this.tDate = tDate;
			this.isFinished = false;
			System.out.println("Thread "+this.threadInterval+" created");
		}
		
		public void run() {
			System.out.println("run thread "+this.threadInterval );
			if(intervalChanged && this.threadInterval != -1){
				weightsOfData[this.threadInterval] = 1.0 - ((1.0 / intervalAmount) * this.threadInterval); //TODO weight calculation is linear atms
//				this.newData = new GridModelData(weightsOfData[this.threadInterval], countGridOccurenciesFromTo(fDate, tDate), this.threadInterval);
			}
			if(this.threadInterval == -1){
				Gridmodel.getInstance().setData(new GridModelData(1.0, countGridOccurenciesFromTo(fDate, tDate), this.threadInterval));
			} else {
				pastGridModelData.add(new GridModelData(weightsOfData[this.threadInterval], countGridOccurenciesFromTo(fDate, tDate), this.threadInterval));
				System.out.println("GridModelData added to pastGridModelData from "+this.threadInterval);
			}
			this.isFinished = true;
			System.out.println("Thread "+this.threadInterval+" exiting.");
		}
		
		public void start (){
			System.out.println("Starting "+this.threadInterval );
			if (t == null){
				t = new Thread(this);
				t.start ();
			}
		}
		
		public boolean isFinished() {
			return isFinished;
		}

		public GridModelData getNewData() {
			return newData;
		}
	}
	public void analyze(int xResolution, int yResolution, Date fromDate, Date toDate, Coordinate topLeft, Coordinate botRight, int newIntervalAmount){
		this.pastGridModelData = new ArrayList<GridModelData>();
		Gridmodel.getInstance().init(xResolution, yResolution, topLeft, botRight);
		long dateInterval = toDate.getTime() - fromDate.getTime(); //interval is current date selection
		Date fDate = fromDate;
		Date tDate = toDate;
		if(this.intervalAmount != newIntervalAmount){
			this.intervalAmount = newIntervalAmount;
			this.intervalChanged = true;
			this.weightsOfData = new double[this.intervalAmount];
		} else {
			this.intervalChanged = false;
		}
		
		//TODO use threading over time
		double weight = 1.0; //TODO determine weight properly and implement analysis of past data:
		long startTime = System.currentTimeMillis(), queryStart, currentTime;
		System.out.println("Begin gridCountQuery...(0/"+this.intervalAmount+")");
		String progressString;
		ArrayList<AnalyzeThread> workers = new ArrayList<AnalyzeThread>();
		workers.add(new AnalyzeThread(fDate, tDate));
		queryStart = System.currentTimeMillis();
		for(int i=0; i<this.intervalAmount; i++){
			fDate = new Date(fromDate.getTime() - dateInterval*(i+1));
			tDate = new Date(fDate.getTime() + dateInterval);
			workers.add(new AnalyzeThread(fDate, tDate, i));
		}
		for(AnalyzeThread worker : workers){
			worker.start();
		}
		boolean threadsRunning = true;
		int progressCounter = -1, tempProgressCounter;
		while(threadsRunning){ //Wait while threads are running
			threadsRunning = false;
			tempProgressCounter = this.intervalAmount+1;
			for(AnalyzeThread worker : workers){
				if(!worker.isFinished()){
					threadsRunning = true;
					tempProgressCounter--;
				}
			}
			if(progressCounter != tempProgressCounter){
				progressCounter = tempProgressCounter;
				progressString = "("+progressCounter+"/"+(this.intervalAmount+1)+") ";
				System.out.println(progressString);
				Main.mainframeController.mainframe.filtermenu_analysis_panel_progress_label.setText(progressString);
			}
		}
		//Sort pastGridModelData:
		this.pastGridModelData.sort(new Comparator<GridModelData>() {

			@Override
			public int compare(GridModelData o1, GridModelData o2) {
				return o1.compareTo(o2);
			}
		});
		
		currentTime = System.currentTimeMillis();
		System.out.println("\nQuery finished in "+(((double)currentTime-(double)startTime)/1000)+"sec");
//		Main.mainframeController.mainframe.filtermenu_analysis_panel_progress_label.setText(" ");
		this.matrixCalculations();
		this.recommendedSliderValues();
	}
	
	public void matrixCalculations() {
		double	minNegValue,maxNegValue,minPosValue,maxPosValue;
		minNegValue = -1.0 * Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.getText()) / 100.0;
		maxNegValue = -1.0 * Main.mainframeController.mainframe.filtermenu_analysis_panel_threshold_slider.getValue() / 100.0;
		minPosValue = Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.getText()) / 100.0;
		maxPosValue = Main.mainframeController.mainframe.filtermenu_analysis_panel_threshold_slider.getValue() / 100.0;
		this.minRelativeValue = Double.MAX_VALUE;
		this.maxRelativeValue = Double.MIN_VALUE;
		this.recommendedSliderValue = Double.MIN_VALUE;
		
		int[][] pastDataAverage = this.calculateWeightedAverage(this.pastGridModelData.toArray(new GridModelData[0]));
		int[][] absoluteDataDifference = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];

		this.normalizedRelativeMatrix = new ArrayList<double[][]>();
		this.relativeDataDifference = new ArrayList<double[][]>();
				
		for(int index=0;index<this.pastGridModelData.size();index++){
			this.relativeDataDifference.add(new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()]);
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){ //Fill absolute- and relativeDifferenceMatrices:
				for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
					absoluteDataDifference[x][y] = Gridmodel.getInstance().getData().getDataMatrix()[x][y] - pastDataAverage[x][y];
					if(absoluteDataDifference[x][y] != 0 && pastDataAverage[x][y] != 0){
						//TODO Maybe this part is wrong:
						this.relativeDataDifference.get(index)[x][y] = (double)absoluteDataDifference[x][y] / (double)pastDataAverage[x][y];
					} else {
						this.relativeDataDifference.get(index)[x][y] = 0.0;	
					}
					if(Math.abs(this.relativeDataDifference.get(index)[x][y]) > this.maxRelativeValue){
						this.maxRelativeValue = Math.abs(this.relativeDataDifference.get(index)[x][y]);
					}
					if(Math.abs(this.relativeDataDifference.get(index)[x][y]) < this.minRelativeValue){
						this.minRelativeValue = Math.abs(this.relativeDataDifference.get(index)[x][y]);
					}
					if(this.recommendedSliderValue < relativeDataDifference.get(index)[x][y]){
						this.recommendedSliderValue = relativeDataDifference.get(index)[x][y];
					}
				}
			}
			System.out.println("DEBUG max pos relative Difference: "+this.recommendedSliderValue);
			System.out.println("DEBUG max abs relative Difference: "+this.maxRelativeValue);
			this.normalizedRelativeMatrix.add(new double[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()]);
			for(int y=0; y<Gridmodel.getInstance().getYResolution(); y++){ //fill the normalized RelativeMatrix
				for(int x=0; x<Gridmodel.getInstance().getXResolution();x++){
					if(this.relativeDataDifference.get(index)[x][y] > maxPosValue){
						this.relativeDataDifference.get(index)[x][y] = maxPosValue;
					} else if (this.relativeDataDifference.get(index)[x][y] < maxNegValue) {
						this.relativeDataDifference.get(index)[x][y] = maxNegValue;
					} 
					if(this.relativeDataDifference.get(index)[x][y] >= 0){
						this.normalizedRelativeMatrix.get(index)[x][y] = ((double)this.relativeDataDifference.get(index)[x][y] - minPosValue)/(maxPosValue - minPosValue);
					} else {
						this.normalizedRelativeMatrix.get(index)[x][y] = ((double)this.relativeDataDifference.get(index)[x][y] - minNegValue)/(maxNegValue - minNegValue) * (-1.0);
					}
				}
			}
			//TODO recommended is not working properly absolute and relative difference has to be considered properly
//			this.recommendedSliderValue = ((double)(this.recommendedSliderValue - this.minAbsoluteValue) / (double)(this.maxAbsoluteValue - this.minAbsoluteValue));
		}
	}
	
	public void recommendedSliderValues(){
		Main.mainframeController.mainframe.filtermenu_analysis_panel_lowPosThreshold_textfield.setText(""+(int)(this.minRelativeValue*100.0));
		Main.mainframeController.mainframe.filtermenu_analysis_panel_upperPosThreshold_textfield.setText(""+(int)(this.maxRelativeValue*100.0));
		System.out.println("Recommended Slider Values: "+this.recommendedSliderValue+", "+this.maxRelativeValue);
//		Main.mainframeController.mainframe.filtermenu_analysis_panel_threshold_slider.setValue((int)(this.recommendedSliderValue*this.maxRelativeValue*100.0)); //Somehow bugged
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
			result = Main.dataBase.countGridOccurenciesFromTo(fromDate, toDate, Main.mainframeController.mainframe.filtermenu_comboBox_category.getSelectedItem().toString(), Gridmodel.getInstance().getGridRectangleMatrix(), Main.mainframeController.getIgnoredDayTimesAsList(), Main.mainframeController.getIgnoredWeekdaysAsList());
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
