package mvc.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class CrimeCaseDatabase {
	private CaseReport[] allData, currentData;
	
	
	public CrimeCaseDatabase(String path){
		this.importData(path);
	}
	
	private void importData(String path){
		System.out.println("Import Data...");
		long startTime = System.currentTimeMillis();
		ArrayList<CaseReport> tempData = new ArrayList<CaseReport>();
		int emptyEntries = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(path), ',' , '\"', 1);
			String[] nextLine;
			
			while((nextLine = reader.readNext()) != null){
				try{
					tempData.add(new CaseReport(Integer.valueOf(nextLine[0]), nextLine[1], nextLine[2], nextLine[9], nextLine[6], nextLine[12]));
				} catch (NumberFormatException e){
					emptyEntries++;
				}
				
			}
			this.allData = tempData.toArray(new CaseReport[0]);
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Import done in "+((System.currentTimeMillis()-startTime)/1000f)+" sec");
		System.out.println(this.allData.length + " entries loaded.");
		System.out.println(emptyEntries+" empty entries ignored.");
	}
}
