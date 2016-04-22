package mvc.model;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import mvc.main.Main;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class CrimeCaseDatabase {
	private ArrayList<CaseReport> currentData;
	private IndexSearcher indexSearcher = null;
	private StandardAnalyzer standardAnalyzer = null;
	private ArrayList<String> categories;
	private CaseReport currentSelected;
	
	public CrimeCaseDatabase(String path, boolean reIndex) throws IOException {
		
		this.currentData = new ArrayList<CaseReport>();
		this.standardAnalyzer = new StandardAnalyzer();
		this.categories = new ArrayList<String>();
		
		if(reIndex){
			this.reindexCSV(path); //testing
		}
		
		try{
			if(!Main.isChicago){
				this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index_sanfrancisco").toPath())));
			} else {
				this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index_chicago").toPath())));
			}
			System.out.println("Index found and loaded.");
		} catch(org.apache.lucene.index.IndexNotFoundException e){
			this.reindexCSV(path);
			if(!Main.isChicago){
				this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index_sanfrancisco").toPath())));
			} else {
				this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index_chicago").toPath())));
			}
		}
		
		// Testing:
//		this.readCategoriesFromIndex();
//		this.countNonGeoPoints();
	}
	
	@SuppressWarnings("unused")
	private void readCategoriesFromIndex() throws IOException{
		System.out.println("Read Categories: ");
		MatchAllDocsQuery query = new MatchAllDocsQuery();
		TopDocs docs = this.indexSearcher.search(query, 1200000);
		System.out.println(docs.scoreDocs.length);
		boolean alreadyExist = false;
		String tempString;
		for(ScoreDoc sDoc : docs.scoreDocs){
			tempString = indexSearcher.doc(sDoc.doc).get("category");
			for(String category : this.categories){
				if(tempString.equals(category)){
					alreadyExist = true;
				}
			}
			if(!alreadyExist){
				this.categories.add(tempString);
			}
			alreadyExist = false;
		}
		System.out.println("\nCategories: "+this.categories.size());
		for(String s : this.categories){
			System.out.println(s);
		}
		System.out.println("------\n");
	}
	
	@SuppressWarnings("unused")
	private void countNonGeoPoints() throws IOException{
		System.out.println("Count missing GeoPoints: ");
		double percentageOfNonGeo, allData = 0, nonGeoData = 0;
		MatchAllDocsQuery query = new MatchAllDocsQuery();
		TopDocs docs = this.indexSearcher.search(query, 1000000);
		String tempString;
		for(ScoreDoc sDoc : docs.scoreDocs){
			tempString = indexSearcher.doc(sDoc.doc).get("lat");
			if(tempString == null){
				nonGeoData++;
			}
		}
		percentageOfNonGeo = nonGeoData / docs.scoreDocs.length;;
		System.out.println("AllData: "+docs.scoreDocs.length);
		System.out.println("Non-GeoData: "+nonGeoData);
		System.out.println("Percent without Geo-Data: "+((double)((int)(percentageOfNonGeo*100*100))/100)+"%");
	}
	
	/**
	 * deletes the old index of the lucene representation of the data
	 * and loads the csv-file from the path folder to create a new index.
	 *  
	 * @param path contains the path to the csv file which should be the new indexed database.
	 */
	public void reindexCSV(String path){
		System.out.println("Begin indexing...");
		long startTime = System.currentTimeMillis(), tempTime = System.currentTimeMillis();
		int emptyEntries = 0, i = 1;
		try {
			if(!Main.isChicago){
				System.out.println("Index San Francisco Data...");
				CsvParserSettings settings = new CsvParserSettings();
				settings.getFormat().setLineSeparator("\r\n");
				settings.setLineSeparatorDetectionEnabled(true);
				settings.setHeaderExtractionEnabled(true);
				CsvParser parser  = new CsvParser(settings);
				parser.beginParsing(new FileReader(path));
				
				String[] nextLine;
				
				Directory indexDirectory;
				indexDirectory = FSDirectory.open(new File("dat/lucene_index_sanfrancisco").toPath());
				IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
				IndexWriter indexWriter = new IndexWriter(indexDirectory, config);
				indexWriter.deleteAll();
				
				Document doc;
				long dateOpenedAsLong = -1, dateClosedAsLong = -1;
				int dayOfWeek = -1;
				while((nextLine = parser.parseNext()) != null){
					i++;
					if(nextLine[13] != null){
						try{
							doc = new Document();
							doc.add(new IntField("id", Integer.valueOf(nextLine[0]), Field.Store.YES));
							try{
								dateOpenedAsLong = (long)(new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").parse(nextLine[1]).getTime());
								dateClosedAsLong = (long)(new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").parse(nextLine[2]).getTime());
							} catch (ParseException e) {
								
							} catch (NullPointerException e){
								dateClosedAsLong = -1;
							}
							if(dateOpenedAsLong != -1){
								Calendar c = Calendar.getInstance();
								c.setTimeInMillis(dateOpenedAsLong);
								dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
							}
							doc.add(new LongField("dateOpened", dateOpenedAsLong, Field.Store.YES));
							doc.add(new LongField("dateClosed", dateClosedAsLong, Field.Store.YES));
							doc.add(new TextField("status", nextLine[4], Field.Store.YES));
							if(nextLine[5] == null){
								doc.add(new StringField("statusNotes", "n/A", Field.Store.YES));
							} else {
								doc.add(new StringField("statusNotes", nextLine[5], Field.Store.YES));
							}
							doc.add(new StringField("category", nextLine[7], Field.Store.YES));
							if(nextLine[10] == null){
								doc.add(new TextField("address", "n/A", Field.Store.YES));
							} else {
								doc.add(new TextField("address", nextLine[10], Field.Store.YES));
							}
							if(nextLine[12] == null){
								doc.add(new StringField("neighbourhood", "n/A", Field.Store.YES));
							} else {
								doc.add(new StringField("neighbourhood", nextLine[12], Field.Store.YES));
							}
							String[] coordinateStrings = nextLine[13].substring(1, nextLine[13].length()-1).split(",");
							doc.add(new DoubleField("lat", Double.valueOf(coordinateStrings[0]), Field.Store.YES));
							doc.add(new DoubleField("lon", Double.valueOf(coordinateStrings[1]), Field.Store.YES));
							if(nextLine[15] == null){
								doc.add(new StringField("mediaUrl", "n/A", Field.Store.YES));
							} else {
								doc.add(new StringField("mediaUrl", nextLine[15], Field.Store.YES));
							}
							doc.add(new IntField("dayOfWeek", dayOfWeek, Field.Store.YES));
							doc.add(new IntField("dayTimeValue", this.determineDayTimeInMillis(dateOpenedAsLong), Field.Store.YES));
							indexWriter.addDocument(doc);
						} catch (NumberFormatException e){
							emptyEntries++;
						}
					}
				}
				indexWriter.close();
				parser.stopParsing();
			} else {
				System.out.println("Index Chicago Data...");
				CsvParserSettings settings = new CsvParserSettings();
				settings.getFormat().setLineSeparator("\r\n");
				settings.setLineSeparatorDetectionEnabled(true);
				settings.setHeaderExtractionEnabled(true);
				CsvParser parser  = new CsvParser(settings);
				parser.beginParsing(new FileReader(path));
				
				String[] nextLine;
				
				Directory indexDirectory;
				indexDirectory = FSDirectory.open(new File("dat/lucene_index_chicago").toPath());
				IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
				IndexWriter indexWriter = new IndexWriter(indexDirectory, config);
				indexWriter.deleteAll();
				
				Document doc;
				long dateOpenedAsLong = -1, dateClosedAsLong = -1;
				int dayOfWeek = -1;
				while((nextLine = parser.parseNext()) != null){
					i++;
					if(nextLine[13] != null){
						try{
							doc = new Document();
							doc.add(new IntField("id", Integer.valueOf(nextLine[0]), Field.Store.YES));
							try{
								dateOpenedAsLong = (long)(new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").parse(nextLine[2]).getTime());
								dateClosedAsLong = (long)(new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").parse(nextLine[18]).getTime());
							} catch (ParseException e) {
								
							} catch (NullPointerException e){
								dateClosedAsLong = -1;
							}
							if(dateOpenedAsLong != -1){
								Calendar c = Calendar.getInstance();
								c.setTimeInMillis(dateOpenedAsLong);
								dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
							}
							doc.add(new LongField("dateOpened", dateOpenedAsLong, Field.Store.YES));
							doc.add(new LongField("dateClosed", dateClosedAsLong, Field.Store.YES));
							doc.add(new TextField("status", "n/A", Field.Store.YES));
							doc.add(new StringField("statusNotes", "n/A", Field.Store.YES));
							doc.add(new StringField("category", nextLine[5], Field.Store.YES));
							if(nextLine[3] == null){
								doc.add(new TextField("address", "n/A", Field.Store.YES));
							} else {
								doc.add(new TextField("address", nextLine[3], Field.Store.YES));
							}
							if(nextLine[3] == null){
								doc.add(new StringField("neighbourhood", "n/A", Field.Store.YES));
							} else {
								doc.add(new StringField("neighbourhood", nextLine[3], Field.Store.YES));
							}
							if(nextLine[19] != null){
								doc.add(new DoubleField("lat", Double.valueOf(nextLine[19]), Field.Store.YES));
								doc.add(new DoubleField("lon", Double.valueOf(nextLine[20]), Field.Store.YES));
							}
							doc.add(new StringField("mediaUrl", "n/A", Field.Store.YES));
							doc.add(new IntField("dayOfWeek", dayOfWeek, Field.Store.YES));
							doc.add(new IntField("dayTimeValue", this.determineDayTimeInMillis(dateOpenedAsLong), Field.Store.YES));
							indexWriter.addDocument(doc);
						} catch (NumberFormatException e){
							emptyEntries++;
						}
					}
				}
				indexWriter.close();
				parser.stopParsing();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempTime = System.currentTimeMillis()-startTime;
		System.out.println("Indexing done ("+(tempTime/1000f)+" sec)");
		System.out.println(i+" entries indexed");;
	}
	
	private int determineDayTimeInMillis(long dateOpen) {
		GregorianCalendar dateOpened = new GregorianCalendar();
		dateOpened.setTimeInMillis(dateOpen);
		long timeDifference = dateOpened.getTimeInMillis() - (new GregorianCalendar(dateOpened.get(Calendar.YEAR),dateOpened.get(Calendar.MONTH),dateOpened.get(Calendar.DAY_OF_MONTH))).getTimeInMillis();
		int dayTimeValue;
		if(timeDifference <= 21600000){
			dayTimeValue = 0; // Midnight (0:00 - 6:00)
		} else if(timeDifference <= 36000000){
			dayTimeValue = 1; // Morning (6:00 - 10:00)
		} else if(timeDifference <= 50400000){
			dayTimeValue = 2; // Noon (10:00 - 14:00)
		} else if(timeDifference <= 64800000){
			dayTimeValue = 3; // Aternoon (14:00 - 18:00)
		} else if(timeDifference <= 79200000){
			dayTimeValue = 4; // Evening (18:00 - 22:00)
		} else {
			dayTimeValue = 5; // Evening2 (22:00 - 0:00)
		}
		return dayTimeValue;
	}

	/**
	 * deletes all entries in the currentData list
	 */
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
	 * @throws IOException 
	 */
	public void selectWeekdaysCasesBetweenDatesToCurrentData(Date fromDate, Date toDate, int[] weekdays, String category, int[] dayTimeValueIgnoreList) throws IOException {
		this.clearCurrentData();
		for(int day : weekdays){
			BooleanQuery boolQuery = new BooleanQuery();
			Query query1 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
			Query query2 = NumericRangeQuery.newIntRange("dayOfWeek", day, day, true, true);
			boolQuery.add(query1, BooleanClause.Occur.MUST);
			boolQuery.add(query2, BooleanClause.Occur.MUST);
			if(!category.equals("All categories")){
				Query query3 = new TermQuery(new Term("category", category));
				boolQuery.add(query3, BooleanClause.Occur.MUST);
			}
			for(int dayTimeValue : dayTimeValueIgnoreList){
				Query query4 = NumericRangeQuery.newIntRange("dayTimeValue", dayTimeValue, dayTimeValue, true, true);
				boolQuery.add(query4, BooleanClause.Occur.MUST_NOT);
			}
			TopDocs docs = this.indexSearcher.search(boolQuery, 10000);
			System.out.println(boolQuery);
			System.out.println("Total hits: " + docs.totalHits);
			Document doc;
			for(ScoreDoc sDoc : docs.scoreDocs){
				 doc = indexSearcher.doc(sDoc.doc);
				 if(doc.get("lat") != null){
					 this.currentData.add(new CaseReport(Integer.valueOf(doc.get("id")), doc.get("dateOpened"), doc.get("dateClosed"), doc.get("dayTimeValue"), doc.get("dayOfWeek"), doc.get("status"), doc.get("statusNotes"), doc.get("category"), doc.get("address"), doc.get("neighbourhood"), "(" + doc.get("lat") + ", " + doc.get("lon") + ")", doc.get("mediaUrl"))); 
				 }
			}
		}
		this.currentData.sort(new Comparator<CaseReport>() {

			@Override
			public int compare(CaseReport cR1, CaseReport cR2) {
				return cR1.compareTo(cR2);
			}
		});
	}
	
	/**
	 * 
	 * @return the CaseReport ArrayList with the currently loaded data.
	 */
	public ArrayList<CaseReport> getCurrentData() {
		return this.currentData;
	}
	
	/**
	 * This method counts all CaseReport in a given timespan
	 * and only includes the selected weekdays and the selected category
	 * 
	 * @param fromDate begin of the timespan
	 * @param toDate end of the timespan
	 * @param checkedWeekdays array of all selected weekdays
	 * @param category String with the selected category
	 * @return the amount of all CaseReports filtered by the parameters
	 * @throws IOException
	 */
	public int countCaseReportsFromTo(Date fromDate, Date toDate, int[] checkedWeekdays, String category) throws IOException{
		int totalHits = 0;
		for(int day : checkedWeekdays){
			TotalHitCountCollector hitCountCollector = new TotalHitCountCollector();
			BooleanQuery boolQuery = new BooleanQuery();
			Query query1 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
			Query query2 = NumericRangeQuery.newIntRange("dayOfWeek", day, day, true, true);
			boolQuery.add(query1, BooleanClause.Occur.MUST);
			boolQuery.add(query2, BooleanClause.Occur.MUST);
			if(!category.equals("All categories")){
				Query query3 = new TermQuery(new Term("category", category));
				boolQuery.add(query3, BooleanClause.Occur.MUST);
			}
			this.indexSearcher.search(boolQuery, hitCountCollector);
			totalHits += hitCountCollector.getTotalHits();
		}
		return totalHits;
	}
	
	public int countCaseReportsFromToWithDayTimes(Date fromDate, Date toDate, String category, int dayTimeValue, int actualWeekday) throws IOException{
		int totalHits = 0;
		TotalHitCountCollector hitCountCollector = new TotalHitCountCollector();
		BooleanQuery boolQuery = new BooleanQuery();
		Query query1 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
		boolQuery.add(query1, BooleanClause.Occur.MUST);
		if(!category.equals("All categories")){
			Query query2 = new TermQuery(new Term("category", category));
			boolQuery.add(query2, BooleanClause.Occur.MUST);
		}
		Query query3 = NumericRangeQuery.newIntRange("dayTimeValue", dayTimeValue, dayTimeValue, true, true);
		boolQuery.add(query3, BooleanClause.Occur.MUST);
		Query query4 = NumericRangeQuery.newIntRange("dayOfWeek", actualWeekday+1, actualWeekday+1, true, true);
		boolQuery.add(query4, BooleanClause.Occur.MUST);
		this.indexSearcher.search(boolQuery, hitCountCollector);
		totalHits += hitCountCollector.getTotalHits();
		return totalHits;
	}

	public int[][] countGridOccurenciesFromTo(Date fromDate, Date toDate, String category, Rectangle2D[][] gridRectangleMatrix) throws IOException{
		long startTime = System.currentTimeMillis(), currentTime;
		System.out.println("Begin gridCountQuery...");
		int[][] resultMatrix = new int[gridRectangleMatrix.length][gridRectangleMatrix[0].length];
		TotalHitCountCollector hitCountCollector;
		Query query1 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
		Query query2 = null;
		if(!category.equals("All categories")){
			query2 = new TermQuery(new Term("category", category));
		}
		BooleanQuery boolQueryBuilder = new BooleanQuery();
		boolQueryBuilder.add(query1, BooleanClause.Occur.MUST);
		if(query2 != null){
			boolQueryBuilder.add(query2, BooleanClause.Occur.MUST);
		}
		BooleanQuery actualQuery = null;
		Query query5;
		Query query6;
		for(int x=0; x<gridRectangleMatrix.length; x++){
			for(int y=0; y<gridRectangleMatrix[0].length; y++){
				hitCountCollector = new TotalHitCountCollector();
				actualQuery = boolQueryBuilder.clone();
				if(gridRectangleMatrix[x][y].getX() <= gridRectangleMatrix[x][y].getMaxX()){
					query5 = NumericRangeQuery.newDoubleRange("lon", gridRectangleMatrix[x][y].getX(), gridRectangleMatrix[x][y].getMaxX(), true, true);
				} else {
					query5 = NumericRangeQuery.newDoubleRange("lon", gridRectangleMatrix[x][y].getMaxX(), gridRectangleMatrix[x][y].getX(), true, true);
				}
				actualQuery.add(query5, BooleanClause.Occur.MUST);
				if(gridRectangleMatrix[x][y].getY() <= gridRectangleMatrix[x][y].getMaxY()){
					query6 = NumericRangeQuery.newDoubleRange("lat", gridRectangleMatrix[x][y].getY(), gridRectangleMatrix[x][y].getMaxY(), true, true);
				} else {
					query6 = NumericRangeQuery.newDoubleRange("lat", gridRectangleMatrix[x][y].getMaxY(),gridRectangleMatrix[x][y].getY(), true, true);
				}
				actualQuery.add(query6, BooleanClause.Occur.MUST);
				this.indexSearcher.search(actualQuery, hitCountCollector);
				resultMatrix[x][y] = hitCountCollector.getTotalHits();
			}
		}
		currentTime = System.currentTimeMillis();
		System.out.println("Query finished in "+(((double)currentTime-(double)startTime)/1000)+"sec");
		return resultMatrix;
	}

	public CaseReport getCurrentSelected() {
		return currentSelected;
	}

	public void setCurrentSelected(CaseReport currentSelected) {
		this.currentSelected = currentSelected;
	}
}
