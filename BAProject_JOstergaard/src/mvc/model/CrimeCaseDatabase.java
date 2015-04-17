package mvc.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CrimeCaseDatabase {
	private CaseReport[] allData, currentData;
	
	
	public CrimeCaseDatabase(String path){
		this.importData(path);
		System.out.println(this.allData[this.allData.length-4]);
		System.out.println(this.allData[this.allData.length-3]);
		System.out.println(this.allData[this.allData.length-2]);
		System.out.println(this.allData[this.allData.length-1]);
		
//		ExampleData:
//		CaseReport exampleReport = new CaseReport(1, "12/08/1988 03:21:11 AM", "03/01/2057 01:43:53 AM", "Brugierstr. 1, 78464, Konstanz", "Life of Om3s", "(47.671804, 9.184545)");
	}
	
	private void importData(String path){
		System.out.println("Import Data...");
		long startTime = System.currentTimeMillis();
		ArrayList<CaseReport> tempData = new ArrayList<CaseReport>(1000000);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			br.readLine(); //skip head row
			ArrayList<String> tokens = new ArrayList<String>(15);
			String[] quoteSplits;
		    for(String line; (line = br.readLine()) != null; ) {
		        quoteSplits = line.split("(?<=\")|(?=\")");
		        for(int i=0; i<quoteSplits.length; i++){
		        	if(quoteSplits[i].equals("\"")){ //entering Quotefield
		        		i++;
		        		tokens.add("\"" + quoteSplits[i] + "\"");
				        i++;;
				        quoteSplits[i+1] = quoteSplits[i+1].substring(1, quoteSplits[i+1].length());
		        	} else {
		        		for(String s : quoteSplits[i].split(",")){
			        		tokens.add(s);
		        		}
		        	}
		        }
//		        Column fields in token:
//		        [0] = CaseID
//		        [1] = Opened
//		        [2] = Closed
//		        [3] = Updated
//		        [4] = Status
//		        [5] = Responsible Agency
//		        [6] = Category
//		        [7] = Request Type
//		        [8] = Request Details
//		        [9] = Address
//		        [10] = Supervisor District
//		        [11] = Neighborhood
//		        [12] = Point (first half, separator error)
//		        [14] = Source
		        
		        if(tokens.size() > 1){
		        	tempData.add(new CaseReport(Integer.valueOf(tokens.get(0)), tokens.get(1), tokens.get(2), tokens.get(9), tokens.get(6), tokens.get(12)));	
		        }
		        tokens.clear();
		    }
		    this.allData = tempData.toArray(new CaseReport[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Import done in "+((System.currentTimeMillis()-startTime)/1000f)+" sec");
		System.out.println(this.allData.length + " entries loaded.");
	}
}
