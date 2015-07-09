package mvc.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.opencsv.CSVReader;

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
	
	public CrimeCaseDatabase(String path) throws IOException {
		
		this.currentData = new ArrayList<CaseReport>();
		this.standardAnalyzer = new StandardAnalyzer();
		this.categories = new ArrayList<String>();
		
//		this.reindexCSV(path); //testing
		
		try{
			this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index").toPath())));
			System.out.println("Index found and loaded.");
		} catch(org.apache.lucene.index.IndexNotFoundException e){
			this.reindexCSV(path);
			this.indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("dat/lucene_index").toPath())));
		}
		
		// Testing:
//		this.readCategoriesFromIndex();
		String fromDateTest = "01/01/2008 12:00:01 AM";
		String toDateTest = "03/01/2008 23:59:99 PM";
//		try {
//			this.selectAllCasesBetweenTwoDates(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(fromDateTest), new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(toDateTest));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		try {
			int[] weekdays = {1,2,3,4,5,6,7};
			this.selectWeekdaysCasesBetweenDatesToCurrentData(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(fromDateTest), new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(toDateTest), weekdays, "all");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CurrentData#: "+this.currentData.size());
//		for(CaseReport cR : this.currentData){
//			System.out.println(cR);
//		}
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
	
	public void reindexCSV(String path){
		System.out.println("Begin indexing...");
		long startTime = System.currentTimeMillis(), tempTime = System.currentTimeMillis();
		int emptyEntries = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(path), ',' , '\"', 1);
			String[] nextLine;

			Directory indexDirectory = FSDirectory.open(new File("dat/lucene_index").toPath());
			IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
			IndexWriter indexWriter = new IndexWriter(indexDirectory, config);
			indexWriter.deleteAll();
			
			Document doc;
			long dateOpenedAsLong = -1, dateClosedAsLong = -1;
			int dayOfWeek = -1;
			while((nextLine = reader.readNext()) != null){
				try{
					doc = new Document();
					doc.add(new IntField("id", Integer.valueOf(nextLine[0]), Field.Store.YES));
					try{
						dateOpenedAsLong = (long)(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(nextLine[1]).getTime());
						dateClosedAsLong = (long)(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse(nextLine[2]).getTime());
					} catch (ParseException e) {
						
					}
					if(dateOpenedAsLong != -1){
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(dateOpenedAsLong);
						dayOfWeek = c.get(Calendar.DAY_OF_WEEK); 
					}
					doc.add(new LongField("dateOpened", dateOpenedAsLong, Field.Store.YES));
					doc.add(new LongField("dateClosed", dateClosedAsLong, Field.Store.YES));
					doc.add(new TextField("address", nextLine[9], Field.Store.YES));
					doc.add(new StringField("category", nextLine[6], Field.Store.YES));
					String[] coordinateStrings = nextLine[12].substring(1, nextLine[12].length()-1).split(",");
					doc.add(new DoubleField("lat", Double.valueOf(coordinateStrings[0]), Field.Store.YES));
					doc.add(new DoubleField("lon", Double.valueOf(coordinateStrings[1]), Field.Store.YES));
					doc.add(new IntField("dayOfWeek", dayOfWeek, Field.Store.YES));
					indexWriter.addDocument(doc);
				} catch (NumberFormatException e){
					emptyEntries++;
				}
			}
			indexWriter.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempTime = System.currentTimeMillis()-startTime;
		System.out.println("Indexing done ("+(tempTime/1000f)+" sec)");
		System.out.println(emptyEntries+" empty entries in CSV found and ignored");
	}
	
	// For testing purposes
	public void selectCategory(String category) throws IOException{
		this.clearCurrentData();
		Query query = new TermQuery(new Term("category", category));
		TopDocs docs = this.indexSearcher.search(query, 1500);
		System.out.println(query);
		System.out.println("Total hits: " + docs.totalHits);
		Document doc;
		for(ScoreDoc sDoc : docs.scoreDocs){
			 doc = indexSearcher.doc(sDoc.doc);
			 this.currentData.add(new CaseReport(Integer.valueOf(doc.get("id")), doc.get("dateOpened"), doc.get("dateClosed"), doc.get("address"), doc.get("category"), "(" + doc.get("lat") + ", " + doc.get("lon") + ")"));
		}
	}
	
	/**
	 * 
	 * Selects all CrimeReports and puts them to the currentData list
	 * between the fromDate and the toDate.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws IOException 
	 */
	public void selectAllCasesBetweenTwoDates(Date fromDate, Date toDate, String category) throws IOException{
		this.clearCurrentData();
		BooleanQuery boolQuery = new BooleanQuery();
		Query query1 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
		if(!category.equals("All categories")){
			Query query2 = new TermQuery(new Term("category", category));
			boolQuery.add(query2, BooleanClause.Occur.MUST);
		}
		boolQuery.add(query1, BooleanClause.Occur.MUST);
		TopDocs docs = this.indexSearcher.search(boolQuery, 1500);
		System.out.println(boolQuery);
		System.out.println("Total hits: " + docs.totalHits);
		Document doc;
		for(ScoreDoc sDoc : docs.scoreDocs){
			 doc = indexSearcher.doc(sDoc.doc);
			 this.currentData.add(new CaseReport(Integer.valueOf(doc.get("id")), doc.get("dateOpened"), doc.get("dateClosed"), doc.get("address"), doc.get("category"), "(" + doc.get("lat") + ", " + doc.get("lon") + ")"));
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
	 * @throws IOException 
	 */
	public void selectWeekdaysCasesBetweenDatesToCurrentData(Date fromDate, Date toDate, int[] weekdays, String category) throws IOException {
		this.clearCurrentData();
		for(int day : weekdays){
			BooleanQuery boolQuery = new BooleanQuery();
			Query query1 = NumericRangeQuery.newIntRange("dayOfWeek", day, day, true, true);
			Query query2 = NumericRangeQuery.newLongRange("dateOpened", fromDate.getTime(), toDate.getTime(), true, true);
			if(!category.equals("All categories")){
				Query query3 = new TermQuery(new Term("category", category));
				boolQuery.add(query3, BooleanClause.Occur.MUST);
			}
			boolQuery.add(query1, BooleanClause.Occur.MUST);
			boolQuery.add(query2, BooleanClause.Occur.MUST);
			TopDocs docs = this.indexSearcher.search(boolQuery, 100);
			switch (day) {
            case 1:  System.out.println("Sunday hits: " + docs.totalHits);
                     break;
            case 2:  System.out.println("Monday hits: " + docs.totalHits);
                     break;
            case 3:  System.out.println("Tuesday hits: " + docs.totalHits);
                     break;
            case 4:  System.out.println("Wednesday hits: " + docs.totalHits);
                     break;
            case 5:  System.out.println("Thursday hits: " + docs.totalHits);
                     break;
            case 6:  System.out.println("Friday hits: " + docs.totalHits);
                     break;
            case 7:  System.out.println("Saturday hits: " + docs.totalHits);
                     break;
			}
			Document doc;
			for(ScoreDoc sDoc : docs.scoreDocs){
				 doc = indexSearcher.doc(sDoc.doc);
				 this.currentData.add(new CaseReport(Integer.valueOf(doc.get("id")), doc.get("dateOpened"), doc.get("dateClosed"), doc.get("address"), doc.get("category"), "(" + doc.get("lat") + ", " + doc.get("lon") + ")"));
			}
		}
	}

	public ArrayList<CaseReport> getCurrentData() {
		return this.currentData;
	}
}
