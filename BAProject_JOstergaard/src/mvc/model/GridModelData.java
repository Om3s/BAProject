package mvc.model;

public class GridModelData {
	private int[][] dataMatrix;
	private double weight;

	public GridModelData(double weight){
		this.dataMatrix = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		this.weight = weight;
	}

	public GridModelData(double weight, int[][] data){
		this.dataMatrix = new int[Gridmodel.getInstance().getXResolution()][Gridmodel.getInstance().getYResolution()];
		this.weight = weight;
		this.dataMatrix = data;
	}
	public int[][] getDataMatrix() {
		return dataMatrix;
	}
	public void setDataMatrix(int[][] dataMatrix) {
		this.dataMatrix = dataMatrix;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
