package mvc.controller;

import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;
import mvc.model.GeoPoint;
import mvc.view.Mainframe;
import mvc.view.ResultDetailFrame;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class MainframeController {
	public Mainframe mainframe;
	private CrimeCaseDatabase cCaseDatabase;
	private Date globalFromDate, globalToDate;
	private SimpleDateFormat standardGUIDateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat standardGUIDateTimeOutputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private int timelineDateSteps;
	private int[] selectedWeekdays = {1,2,3,4,5,6,7};
	private ArrayList<Integer> selectedDayTimesAsList;
	private final int dayTimesAmount = 6;
	private String currentCategory;
	private MapController mapController;

	//Chart:
	private String intervalName = null;
	private int[][] currentDataMatrix;
	private ArrayList<Integer> selectedWeekdaysAsList;

	//DEPRECATED CONTENT:
//	//Timeline:
//	private Date currentFromDate, currentToDate;
//	private int timelineSmallestTimeInterval = 1;
//	private int timelineMaxValue, timelineLowerValue, timelineHigherValue;
	
	
	/**
	 * 
	 * @param frame is the reference to the mainframe
	 * @param dataBase is the reference to the main database
	 * @param mC is the reference to the MapController of the GeoMap
	 * @throws ParseException
	 */
	public MainframeController(Mainframe frame, CrimeCaseDatabase dataBase, MapController mC) throws ParseException{
		this.mainframe = frame;
		this.cCaseDatabase = dataBase;
		this.mapController = mC;
		this.mapController.setMainFrameController(this);
		this.init();
	}
	
	/**
	 * 
	 * initial settings for the GUI components
	 * 
	 * @throws ParseException
	 */
	private void init() throws ParseException{
		//Globaldates
		this.globalFromDate = new SimpleDateFormat("dd/MM/yyyy KK:mm:ss a").parse("10/11/2015 12:00:01 AM");
		this.globalToDate = new SimpleDateFormat("dd/MM/yyyy KK:mm:ss a").parse("08/12/2015 11:59:59 PM");
		//Radiobutton
		this.mainframe.filtermenu_interval_radioButtonWeeks.setSelected(true);
		this.mainframe.filtermenu_interval_radioButtonDays.setEnabled(false);
		this.mainframe.filtermenu_interval_radioButtonHours.setEnabled(false);
		this.mainframe.filtermenu_interval_radioButtonMonths.setEnabled(false);
		this.timelineDateSteps = 2; // weeks
		//Day of Week Checkboxes:
		this.mainframe.checkBox_Mon.setSelected(true);
		this.mainframe.checkBox_Mon.setBackground(Main.mondayColor);
		this.mainframe.checkBox_Tue.setSelected(true);
		this.mainframe.checkBox_Tue.setBackground(Main.tuesdayColor);
		this.mainframe.checkBox_Wed.setSelected(true);
		this.mainframe.checkBox_Wed.setBackground(Main.wednesdayColor);
		this.mainframe.checkBox_Thu.setSelected(true);
		this.mainframe.checkBox_Thu.setBackground(Main.thursdayColor);
		this.mainframe.checkBox_Fri.setSelected(true);
		this.mainframe.checkBox_Fri.setBackground(Main.fridayColor);
		this.mainframe.checkBox_Sat.setSelected(true);
		this.mainframe.checkBox_Sat.setBackground(Main.saturdayColor);
		this.mainframe.checkBox_Sun.setSelected(true);
		this.mainframe.checkBox_Sun.setBackground(Main.sundayColor);
		//calendarbuttons:
		this.mainframe.filtermenu_dates_leftCalendarButton.setTargetDate(globalFromDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(globalToDate);
		String fromDateString = this.standardGUIDateOutputFormat.format(this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate());
		String toDateString = this.standardGUIDateOutputFormat.format(this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate());
		this.mainframe.filtermenu_dates_leftCalendarButton.setText(fromDateString);
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(toDateString);
		//Category
		this.currentCategory = "All categories";
		//reportListArea
		this.mainframe.reportList_panel_filter_checkBoxOpen.setSelected(true);
		this.mainframe.reportList_panel_filter_checkBoxClosed.setSelected(true);
		((DefaultListCellRenderer)this.mainframe.reportList.getCellRenderer()).setHorizontalAlignment(JLabel.LEFT);
		this.mainframe.reportList.setLayoutOrientation(this.mainframe.reportList.HORIZONTAL_WRAP);
		//daytime checkboxes
		this.mainframe.checkBox_daytime_morning.setSelected(true);
		this.mainframe.checkBox_daytime_noon.setSelected(true);
		this.mainframe.checkBox_daytime_afternoon.setSelected(true);
		this.mainframe.checkBox_daytime_evening.setSelected(true);
		this.mainframe.checkBox_daytime_evening2.setSelected(true);
		this.mainframe.checkBox_daytime_midnight.setSelected(true);
		//Weekdays:
		this.refreshWeekdays();
		//Chart:
		this.refreshChart();
		this.mainframe.matrix_chart_panel.setTransformationMode(2);
		//Map:
		this.refreshMapData();
		
		//DEPRECATED CONTENT:
//		//TimeLine:
//		this.mainframe.timeline_panel_fromDate_label.setText(fromDateString);
//		this.mainframe.timeline_panel_toDate_label.setText(toDateString);
//		this.refreshTimelineAttributes();
//		this.mainframe.timeLineBiSlider.setPrecise(true);
//		this.mainframe.timeLineBiSlider.setUniformSegment(true);
//		this.timelineLowerValue = -1; //definetly change HACK
//		this.timeLineChanged(0, 1);
	}
	/**
	 * Refreshes the TimeLine with the actual Filtersettings
	 */
	private void refreshTimelineAttributes(){
		//DEPRECATED CONTENT
//		Date fromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
//		Date toDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
//		long diffInMillis = toDate.getTime() - fromDate.getTime();
//		int timeUnitDiffs;
//		//WEEK-Interval selected:
//		timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 7);
//		if(timeUnitDiffs == 0){
//			timeUnitDiffs = 1;
//		}
		
		this.timelineDateSteps = 2;
		this.intervalName = "Weeks";
		
		//DEPRECATED CONTENT
//		if(this.mainframe.filtermenu_interval_radioButtonWeeks.isSelected()){
//			timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 7);
//			if(timeUnitDiffs == 0){
//				timeUnitDiffs = 1;
//			}
//			this.timelineDateSteps = 2;
//			this.intervalName = "Weeks";
//		} else if (this.mainframe.filtermenu_interval_radioButtonDays.isSelected()) {
//			timeUnitDiffs = (int)TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
//			if(timeUnitDiffs == 0){
//				timeUnitDiffs = 1;
//			}
//			this.timelineDateSteps = 1;
//			this.intervalName = "Days";
//		} else if (this.mainframe.filtermenu_interval_radioButtonHours.isSelected()) {
//			timeUnitDiffs = (int)TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
//			if(timeUnitDiffs == 0){
//				timeUnitDiffs = 1;
//			}
//			this.timelineDateSteps = 0;
//			this.intervalName = "Hours";
//		} else {
//			Calendar startCalendar = new GregorianCalendar();
//			Calendar endCalendar = new GregorianCalendar();
//			startCalendar.setTime(fromDate);
//			endCalendar.setTime(toDate);
//			timeUnitDiffs = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
//			System.out.println(timeUnitDiffs);
//			timeUnitDiffs = (timeUnitDiffs * 12) + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
//			if(timeUnitDiffs == 0){
//				timeUnitDiffs = 1;
//			}
//			System.out.println(timeUnitDiffs);
//			this.timelineDateSteps = 3;
//			this.intervalName = "Months";
//		}
//		this.timelineMaxValue = timeUnitDiffs;
//		this.timelineSmallestTimeInterval = 1;
//		this.timelineHigherValue = this.timelineMaxValue;
//		this.timelineLowerValue = 0;
//		this.mainframe.timeLineBiSlider.setValues(0, this.timelineMaxValue);
//		this.mainframe.timeLineBiSlider.setMaximumColoredValue(this.timelineHigherValue);
//		this.mainframe.timeLineBiSlider.setMinimumColoredValue(this.timelineLowerValue);
//		this.mainframe.timeLineBiSlider.setSegmentSize(this.timelineSmallestTimeInterval);
//		this.refreshCurrentDates(0, timeUnitDiffs);
	}
	
	/**
	 * is called when the apply button of the settings is clicked
	 * it calls all functions to refresh the values and attributes
	 * of the analysis part.
	 */
	public void applySettings(){
		this.refreshGlobalDates();
		this.refreshTimelineAttributes();
		this.refreshWeekdays();
		this.refreshCurrentCategory();
		this.refreshChart();
		this.refreshMapData();
		this.showOpenedCases(this.mainframe.reportList_panel_filter_checkBoxOpen.isSelected());
		this.showClosedCases(this.mainframe.reportList_panel_filter_checkBoxClosed.isSelected());
		
		//DEPRECATED CONTENT:
//		this.timelineLowerValue = -1; //definetly change HACK
//		this.timeLineChanged(0, 1);
	}
	
	/**
	 * writes the date values of the filter buttons into global variables
	 */
	private void refreshGlobalDates() {
		this.globalFromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
		this.globalToDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
	}
	
	/**
	 * reads the checkboxes of the filtermenu and creates the new weekday array
	 */
	private void refreshWeekdays() {
		this.selectedWeekdaysAsList = new ArrayList<Integer>();
		if(this.mainframe.checkBox_Sun.isSelected()){
			selectedWeekdaysAsList.add(1);
		}
		if(this.mainframe.checkBox_Mon.isSelected()){
			selectedWeekdaysAsList.add(2);
		}
		if(this.mainframe.checkBox_Tue.isSelected()){
			selectedWeekdaysAsList.add(3);
		}
		if(this.mainframe.checkBox_Wed.isSelected()){
			selectedWeekdaysAsList.add(4);
		}
		if(this.mainframe.checkBox_Thu.isSelected()){
			selectedWeekdaysAsList.add(5);
		}
		if(this.mainframe.checkBox_Fri.isSelected()){
			selectedWeekdaysAsList.add(6);
		}
		if(this.mainframe.checkBox_Sat.isSelected()){
			selectedWeekdaysAsList.add(7);
		}
		Integer[] tempArray = ((Integer[]) selectedWeekdaysAsList.toArray(new Integer[0]));
		this.selectedWeekdays = new int[tempArray.length];
		for(int i = 0; i<tempArray.length; i++){
			this.selectedWeekdays[i] = tempArray[i];
		}
	}
	
	/**
	 * is called by clicking the "Default" Button, at the moment it just calls the init() function.
	 */
	public void defaultSettings(){
		try {
			this.init();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is called when the load menuitem is clicked.
	 * It will call the reindexCSV() function of the dataBase with a given path as parameter.
	 * @param path where the new CSV file is.
	 */
	public void loadData(String path){
		this.cCaseDatabase.reindexCSV(path);
	}
	
	/**
	 * This method is called if the value of the fromDate of the filtermenu
	 * has been changed. And it will change the toDate properly if needed.
	 * 
	 * @param newDate the new Date the CalendarButton fromDate has to be set to.
	 */
	public void fromDateAction(Date newDate){
		System.out.println("fromDate changed");
		Date newDateMinusOneWeek = new Date(newDate.getTime() + 604799999); //+7 Days
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		if(this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate().before(newDateMinusOneWeek)) {
			this.setToDate(newDateMinusOneWeek);
			this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
			//DEPRECATED CONTENT
//			this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
		} else {
			this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
			//DEPRECATED CONTENT
//			this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
		}
		this.mainframe.repaint();
	}

	/**
	 * This method is called if the value of the toDate of the filtermenu
	 * has been changed. And it will change the toDate properly if needed.
	 * 
	 * @param newDate the new Date the CalendarButton toDate has to be set to.
	 */
	public void toDateAction(Date newDate){
		System.out.println("toDate changed");
		Date newDateMinusOneWeek = new Date(newDate.getTime() - 604799999); //-7 Days
		String newDateText;
		if(this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate().after(newDateMinusOneWeek)) {
			this.setFromDate(newDateMinusOneWeek);
			newDateText = this.standardGUIDateOutputFormat.format(newDate);
			this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
			//DEPRECATED CONTENT
//			this.mainframe.timeline_panel_toDate_label.setText(newDateText);
		} else {
			newDateText = this.standardGUIDateOutputFormat.format(newDate);
			this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
			//DEPRECATED CONTENT
//			this.mainframe.timeline_panel_toDate_label.setText(newDateText);
		}
	}
	
	/**
	 * This method sets the value of the Calendarbutton fromDate.
	 * 
	 * @param newDate
	 */
	private void setFromDate(Date newDate){
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		this.mainframe.filtermenu_dates_leftCalendarButton.setTargetDate(newDate);
		this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
	}
	
	/**
	 * This method sets the value of the Calendarbutton toDate.
	 * 
	 * @param newDate
	 */
	private void setToDate(Date newDate){
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
	}

	//DEPRECATED CONTENT:
//	/**
//	 * This method is always called, if the timeLine has been changed
//	 * 
//	 * @param minColorValue the left curser position
//	 * @param maxColorValue the right cursor position
//	 */
//	public void timeLineChanged(double minColorValue, double maxColorValue){
//		if(this.hasTimeLineChanged((int) (minColorValue+0.5), (int) (maxColorValue+0.5))){
//			this.timelineLowerValue = (int) (minColorValue+0.5);
//			this.timelineHigherValue = (int) (maxColorValue+0.5);
//			this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
//			try {
//				System.out.println("fromDate: "+this.currentFromDate+" , toDate: "+this.currentToDate);
//				this.cCaseDatabase.selectWeekdaysCasesBetweenDatesToCurrentData(this.currentFromDate, this.currentToDate, this.weekdays, this.currentCategory);
//				this.geoMapController.loadPoints(this.cCaseDatabase.getCurrentData());
//				this.fillReportListWith(this.cCaseDatabase.getCurrentData());
//				this.geoMapController.setShowCurrentPoints(true);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	private void refreshMapData(){
		//DEPRECATED CONTENT:
//		this.timelineLowerValue = (int) (minColorValue+0.5);
//		this.timelineHigherValue = (int) (maxColorValue+0.5);
//		this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
		try {
			ArrayList<Integer> notSelectedDayTimeList = new ArrayList<Integer>();
			for(int i = 0; i<this.dayTimesAmount; i++){
				if(!this.selectedDayTimesAsList.contains(i)){
					notSelectedDayTimeList.add(i);
					System.out.println("NOT SELECTED "+i);
				}
			}
			int[] notSelectedDayTimes = new int[notSelectedDayTimeList.size()];
			for(int i = 0; i<notSelectedDayTimeList.size(); i++){
				notSelectedDayTimes[i] = notSelectedDayTimeList.get(i);
			}
			this.cCaseDatabase.selectWeekdaysCasesBetweenDatesToCurrentData(this.globalFromDate, this.globalToDate, this.selectedWeekdays, this.currentCategory, notSelectedDayTimes);
			System.out.println("currentData#: "+this.cCaseDatabase.getCurrentData().size());
			this.mapController.loadPoints(this.cCaseDatabase.getCurrentData());
			this.fillReportListWith(this.mainframe.reportList_panel_filter_checkBoxOpen.isSelected(), this.mainframe.reportList_panel_filter_checkBoxClosed.isSelected());
			this.mapController.setShowCurrentPoints(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method fills the List for the CaseReportList.
	 * 
	 * @param currentData contains the data thas should be listed.
	 */
	private void fillReportListWith(boolean openedCases, boolean closedCases) {
		this.mainframe.reportListModel.clear();
		for(CaseReport cR : this.cCaseDatabase.getCurrentData()){
			if(cR.isClosed() && closedCases){
				this.mainframe.reportListModel.addElement(cR);
			} else if (!cR.isClosed() && openedCases){
				this.mainframe.reportListModel.addElement(cR);
			}
		}
	}
	
	//DEPRECATED CONTENT:
//	/**
//	 * This method refreshes the currentDates, currentDate Labels depending on time interval setting,
//	 * minValue = globalMinValue and maxValue is minValue + selected timeintervall. 
//	 * 
//	 * @param minValue
//	 * @param maxValue
//	 */
//	private void refreshCurrentDates(int minValue, int maxValue){
//		Calendar temp = Calendar.getInstance();
//		SimpleDateFormat tempDateFormat = this.standardGUIDateOutputFormat;
//		if(this.timelineDateSteps == 0){ //hourly
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.HOUR_OF_DAY, (minValue));
//			this.currentFromDate = new Date(temp.getTimeInMillis());
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.HOUR_OF_DAY, (maxValue));
//			tempDateFormat = this.standardGUIDateTimeOutputFormat;
//			this.currentToDate = new Date(temp.getTimeInMillis());
//		} else if(this.timelineDateSteps == 1){ //daily
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.DATE, (minValue));
//			this.currentFromDate = new Date(temp.getTimeInMillis());
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.DATE, (maxValue));
//			this.currentToDate = new Date(temp.getTimeInMillis());
//		} else if(this.timelineDateSteps == 2){ //weekly
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.DATE, ((minValue)*7));
//			this.currentFromDate = new Date(temp.getTimeInMillis());
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.DATE, ((maxValue)*7));
//			this.currentToDate = new Date(temp.getTimeInMillis());
//		} else { //monthly
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.MONTH, (minValue));
//			this.currentFromDate = new Date(temp.getTimeInMillis());
//			temp.setTime(globalFromDate);
//			temp.add(Calendar.MONTH, (maxValue));
//			this.currentToDate = new Date(temp.getTimeInMillis());
//		}
//		//refresh TimeLine GUI elements:
//		String newDateText = tempDateFormat.format(this.currentFromDate);
//		this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
//		newDateText = tempDateFormat.format(this.currentToDate);
//		this.mainframe.timeline_panel_toDate_label.setText(newDateText);
//	}

	//DEPRECATED CONTENT:
//	/**
//	 * This method checks, if the timeLineBiSlider has been changed.
//	 * The parameters are values of the gui element are getting compared to
//	 * the saved values.
//	 * 
//	 * @param minColorValue
//	 * @param maxColorValue
//	 * @return
//	 */
//	private boolean hasTimeLineChanged(int minColorValue, int maxColorValue){
//		if(this.timelineLowerValue == minColorValue && this.timelineHigherValue == maxColorValue){
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	/**
	 * This method sets the currentCategory variable with the current selected one from the GUI DropList.
	 */
	public void refreshCurrentCategory() {
		this.currentCategory = (String) this.mainframe.filtermenu_comboBox_category.getSelectedItem();
	}
	
	/**
	 * This method deletes the old chart and creates a new chart
	 * based on the selected filter values.
	 */
	public void refreshChart(){
		this.createDataMatrix();
		this.mainframe.matrix_chart_panel.setData(this.currentDataMatrix);
	}
	
	//DEPRECATED CONTENT:
//	/**
//	 * This method creates the current dataset by getting all cases
//	 * from the database which are in the timespan from the currentFromDate
//	 * and the currentToDate of the BiSlider.
//	 * 
//	 * @return the CategoryDataset for the Chart
//	 */
//	private CategoryDataset createDataset(){
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//		int count = 0;
//		for(int i=1; i <= this.timelineMaxValue; i++){
//			this.refreshCurrentDates(i-1, i);
//			try {
//				count = this.cCaseDatabase.countCaseReportsFromTo(this.currentFromDate, this.currentToDate, this.weekdays, this.currentCategory);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			dataset.addValue(count, String.valueOf(i), "");
//		}
//		this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
//		
//		return dataset;
//	}
	
	private void createDataMatrix(){
		this.currentDataMatrix = new int[7][this.dayTimesAmount];
		this.determineSelectedDayTimesList();
		for(int x = 1; x<this.currentDataMatrix.length; x++){
			if(this.selectedWeekdaysAsList.contains(x+1)){
				for(int y = 0; y<this.currentDataMatrix[0].length; y++){
					if(this.selectedDayTimesAsList.contains(y)){
						try {
							this.currentDataMatrix[x-1][y] = this.cCaseDatabase.countCaseReportsFromToWithDayTimes(this.globalFromDate, this.globalToDate, this.currentCategory, y, x);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						this.currentDataMatrix[x-1][y] = -1;
					}
				}
			} else {
				for(int y=0; y<this.currentDataMatrix[0].length; y++){
					this.currentDataMatrix[x-1][y] = -1;
				}
			}
		}
		//special case, Sunday:
		if(this.selectedWeekdaysAsList.contains(1)){
			for(int y = 0; y<this.currentDataMatrix[0].length; y++){
				if(this.selectedDayTimesAsList.contains(y)){
					try {
						this.currentDataMatrix[6][y] = this.cCaseDatabase.countCaseReportsFromToWithDayTimes(this.globalFromDate, this.globalToDate, this.currentCategory, y, 0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					this.currentDataMatrix[6][y] = -1;
				}
			}
		} else {
			for(int y=0; y<this.currentDataMatrix[0].length; y++){
				this.currentDataMatrix[6][y] = -1;
			}
		}
	}

	private void determineSelectedDayTimesList() {
		this.selectedDayTimesAsList = new ArrayList<Integer>();
		if (this.mainframe.checkBox_daytime_midnight.isSelected()){
			this.selectedDayTimesAsList.add(0);
		}
		if (this.mainframe.checkBox_daytime_morning.isSelected()){
			this.selectedDayTimesAsList.add(1);
		}
		if (this.mainframe.checkBox_daytime_noon.isSelected()){
			this.selectedDayTimesAsList.add(2);
		}
		if (this.mainframe.checkBox_daytime_afternoon.isSelected()){
			this.selectedDayTimesAsList.add(3);
		}
		if (this.mainframe.checkBox_daytime_evening.isSelected()){
			this.selectedDayTimesAsList.add(4);
		}
		if (this.mainframe.checkBox_daytime_evening2.isSelected()){
			this.selectedDayTimesAsList.add(5);
		}
	}

	/**
	 * This is called when a new Item in the reportList is selected.
	 */
	public void onNewCaseSelection() {
		CaseReport cR = this.mainframe.reportList.getSelectedValue();
		String result;
		if(cR == null){
		} else {
			this.mapController.setSelectedMarker(cR.getPoint());
		}
		this.mainframe.repaint();
	}
	
	private void createDetailFrame(CaseReport cR) {
		new ResultDetailFrame(cR, this);
//		this.mainframe.setVisible(false);
//		this.mainframe.setVisible(true);
	}

	/**
	 * 
	 * @return the current timeLineDateSteps value
	 */
	public int getTimeLineDateSteps(){
		return this.timelineDateSteps;
	}
	
	/**
	 * 
	 * @return the globalFromDate value
	 */
	public Date getGlobalFromDate(){
		return this.globalFromDate;
	}
	
	/**
	 * This method is called when the details button is clicked.
	 * It will open a new Window containing detailed information of the selected report.
	 */
	public void detailsButtonClicked() {
		CaseReport cR = this.mainframe.reportList.getSelectedValue();
		
		if(cR == null){
		} else {
			this.mapController.setSelectedMarker(cR.getPoint());
			this.createDetailFrame(cR);
		}
		this.mainframe.repaint();
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary){
		int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}
	
	/**
	 * This method is called by the center_to_point button of the ResultDetailFrame.
	 * It will focus the GeoMap (center and zoom) on the selected CaseReport's Point.
	 * 
	 * @param point
	 */
	public void center_to_point(CaseReport cR) {
		this.mapController.setMapLocation(cR.getPoint().getLat(), cR.getPoint().getLon(), 17);
	}

	public void showOpenedCases(boolean yes) {
		this.mapController.setShowOpenedClosedCurrentPoints(yes, true);
		this.fillReportListWith(yes, this.mainframe.reportList_panel_filter_checkBoxClosed.isSelected());
	}

	public void showClosedCases(boolean yes) {
		this.mapController.setShowOpenedClosedCurrentPoints(yes, false);
		this.fillReportListWith(this.mainframe.reportList_panel_filter_checkBoxOpen.isSelected(), yes);
	}
	
	public void filterForCaseCountMatrixSelection(boolean isSelected,int selectedWeekDay,int selectedDayTime){
		if(isSelected){
			this.selectedWeekdays = new int[1];
			if(selectedWeekDay != 6){
				this.selectedWeekdays[0] = selectedWeekDay+2;
			} else {
				this.selectedWeekdays[0] = 1;
			}
			if(selectedDayTime != -1){
				this.selectedDayTimesAsList = new ArrayList<Integer>();
				this.selectedDayTimesAsList.add(selectedDayTime);
			} else {
				this.selectedDayTimesAsList = new ArrayList<Integer>();
				for(int i=0; i<this.dayTimesAmount; i++){
					this.selectedDayTimesAsList.add(i);
				}
			}
			this.refreshMapData();
			this.showOpenedCases(this.mainframe.reportList_panel_filter_checkBoxOpen.isSelected());
			this.showClosedCases(this.mainframe.reportList_panel_filter_checkBoxClosed.isSelected());
		} else {
			this.applySettings();
		}
	}
}
