package mvc.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.opencsv.CSVReader;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class CrimeCaseDatabase {
	private ArrayList<CaseReport> allData, currentData, listOfMondays, listOfTuesdays, listOfWednesdays, listOfThursdays, listOfFridays, listOfSaturdays, listOfSundays;
	
	
	public CrimeCaseDatabase(String path){
		this.currentData = new ArrayList<CaseReport>();
		this.listOfMondays = new ArrayList<CaseReport>();
		this.listOfTuesdays = new ArrayList<CaseReport>();
		this.listOfWednesdays = new ArrayList<CaseReport>();
		this.listOfThursdays = new ArrayList<CaseReport>();
		this.listOfFridays = new ArrayList<CaseReport>();
		this.listOfSaturdays = new ArrayList<CaseReport>();
		this.listOfSundays = new ArrayList<CaseReport>();
		this.importData(path);
		
		// Testing:
		try {
			this.selectAllCasesBetweenDatesToCurrentData(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("07/01/2008 12:00:01 AM"), new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("07/01/2008 02:00:00 AM"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(CaseReport cR : this.currentData){
			System.out.println(cR);
		}
	}
	
	private void importData(String path){
		System.out.println("Import Data...");
		long startTime = System.currentTimeMillis(), tempTime = System.currentTimeMillis();
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
			this.allData = tempData;
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempTime = System.currentTimeMillis()-startTime;
		System.out.println("Import done in "+(tempTime / 1000f)+" sec");
		System.out.println(this.allData.size() + " entries loaded.");
		System.out.println(emptyEntries+" empty entries ignored.");
		System.out.println("Sorting by Datetime...");
		sortAllDataByDateTime();
		System.out.println("Sorting done in "+((System.currentTimeMillis() - tempTime) / 1000f)+" sec");
		tempTime = System.currentTimeMillis()-startTime;
		System.out.println("Indexing...");
		this.indexing();
		System.out.println("Indexing done in "+((System.currentTimeMillis() - tempTime) / 1000f)+" sec");
	}
	
	private void indexing(){
		Calendar cal = Calendar.getInstance();
		for(CaseReport cR : this.allData){
			cal.setTime(cR.getDateOpened());
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				this.listOfMondays.add(cR);
			} else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
				this.listOfTuesdays.add(cR);
			} else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
				this.listOfWednesdays.add(cR);
			} else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
				this.listOfThursdays.add(cR);
			} else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
				this.listOfFridays.add(cR);
			} else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
				this.listOfSaturdays.add(cR);
			} else {
				this.listOfSundays.add(cR);
			}
		}
	}
	
	private void sortAllDataByDateTime(){
		Collections.sort(this.allData, new Comparator<CaseReport>(){
			@Override
			public int compare(CaseReport cR1, CaseReport cR2){
				return cR1.compareTo(cR2);
			}
		});
	}
	
	/**
	 * 
	 * Selects all CrimeReports and puts them to the currentData list
	 * between the fromDate and the toDate.
	 * 
	 * @param fromDate
	 * @param toDate
	 */
	public void selectAllCasesBetweenDatesToCurrentData(Date fromDate, Date toDate){
		this.clearCurrentData();
		for(CaseReport cR : this.allData){
			if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
				this.currentData.add(cR);
			}
		}
	}
	
	public void clearCurrentData(){
		this.currentData.clear();
	}
	
	/**
	 * 
	 * Selects the CrimeReports of the specified weekdays and
	 * puts them to the currentData list between the fromDate and the toDate.
	 * 
	 * @param fromDate the beginning date
	 * @param toDate the ending date
	 * @param weekdays specifies which weekdays we want to look at (Sunday[1],Monday[2],...,Saturday[7])
	 */
	public void selectWeekdaysCasesBetweenDatesToCurrentData(Date fromDate, Date toDate, int[] weekdays) {
		this.clearCurrentData();
		for(int i=1; i<8; i++){
			if(weekdays[i] == 1){
				for(CaseReport cR : this.listOfSundays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else if(weekdays[i] == 2){
				for(CaseReport cR : this.listOfMondays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else if(weekdays[i] == 3){
				for(CaseReport cR : this.listOfTuesdays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else if(weekdays[i] == 4){
				for(CaseReport cR : this.listOfWednesdays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else if(weekdays[i] == 5){
				for(CaseReport cR : this.listOfThursdays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else if(weekdays[i] == 6){
				for(CaseReport cR : this.listOfFridays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			} else {
				for(CaseReport cR : this.listOfSaturdays){
					if(fromDate.before(cR.getDateOpened()) && toDate.after(cR.getDateOpened())){
						this.currentData.add(cR);
					}
				}
			}
		}
	}

	public ArrayList<CaseReport> getCurrentData() {
		return this.currentData;
	}
	
	public ArrayList<CaseReport> getAllData() {
		return this.allData;
	}
}
