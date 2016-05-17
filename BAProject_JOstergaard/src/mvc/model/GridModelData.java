package mvc.model;

public class GridModelData {
	private int[][] dataMatrix;
	private double weight;
	private int overallAmount, index;

	public GridModelData(double weight, int index){
		this.dataMatrix = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		this.weight = weight;
	}

	public GridModelData(double weight, int[][] data, int index){
		this.dataMatrix = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		this.weight = weight;
		this.dataMatrix = data;
	}
	public int[][] getDataMatrix() {
		return dataMatrix;
	}
	public void setDataMatrix(int[][] dataMatrix) {
		this.dataMatrix = dataMatrix;
		//calculate overall
		this.overallAmount = 0;
		for(int i=0;i<this.dataMatrix.length;i++){
			for(int j=0;j<this.dataMatrix[0].length;j++){
				this.overallAmount += this.dataMatrix[i][j];
			}
		}
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getOverallAmount() {
		return overallAmount;
	}

	public int getIndex() {
		return index;
	}
}
