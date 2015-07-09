package mvc.controller;

import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;

public class MainframeController {
	private Mainframe mainframe;
	private CrimeCaseDatabase cCaseDatabase;
	private Date globalFromDate, globalToDate, currentFromDate, currentToDate;
	private SimpleDateFormat standardGUIDateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
	private byte timelineDateSteps;
	private MapController geoMapController;
	private int[] weekdays;
	private String currentCategory;
	
	//Timeline:
	private int timelineSmallestTimeInterval = 1;
	private int timelineMaxValue, timelineLowerValue, timelineHigherValue;
	
	
	public MainframeController(Mainframe frame, CrimeCaseDatabase dataBase) throws ParseException{
		this.mainframe = frame;
		this.cCaseDatabase = dataBase;
		this.globalFromDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("01/01/2011 12:00:01 AM");
		this.globalToDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("02/01/2011 23:59:99 PM");
		this.geoMapController = this.mainframe.getGeoMapController();
		
		this.init();
	}
	
	/**
	 * 
	 * initial settings for the GUI components
	 * 
	 * @throws ParseException
	 */
	private void init() throws ParseException{
		//Radiobutton
		this.mainframe.filtermenu_interval_radioButtonHours.setSelected(true);
		this.timelineDateSteps = 0; // hours
		//Day of Week Checkboxes:
		this.mainframe.checkBox_Mon.setSelected(true);
		this.mainframe.checkBox_Tue.setSelected(true);
		this.mainframe.checkBox_Wed.setSelected(true);
		this.mainframe.checkBox_Thu.setSelected(true);
		this.mainframe.checkBox_Fri.setSelected(true);
		this.mainframe.checkBox_Sat.setSelected(true);
		this.mainframe.checkBox_Sun.setSelected(true);
		//calendarbuttons:
		this.mainframe.filtermenu_dates_leftCalendarButton.setTargetDate(globalFromDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(globalToDate);
		String fromDateString = this.standardGUIDateOutputFormat.format(this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate());
		String toDateString = this.standardGUIDateOutputFormat.format(this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate());
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(toDateString);
		this.mainframe.filtermenu_dates_leftCalendarButton.setText(fromDateString);
		this.mainframe.timeline_panel_fromDate_label.setText(fromDateString);
		this.mainframe.timeline_panel_toDate_label.setText(toDateString);
		//TimeLine:
		this.refreshTimelineAttributes();
		this.mainframe.timeLineBiSlider.setPrecise(true);
		this.mainframe.timeLineBiSlider.setUniformSegment(true);
		//Category
		this.currentCategory = "All categories";
	}
	
	private void refreshTimelineAttributes(){
		Date fromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
		Date toDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
		long diffInMillis = toDate.getTime() - fromDate.getTime();
		int timeUnitDiffs;
		if(this.mainframe.filtermenu_interval_radioButtonHours.isSelected()){
			timeUnitDiffs = (int)TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			this.timelineDateSteps = 0;
			System.out.println("Hours difference: "+ timeUnitDiffs);
		} else if (this.mainframe.filtermenu_interval_radioButtonDays.isSelected()) {
			timeUnitDiffs = (int)TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			this.timelineDateSteps = 1;
			System.out.println("Days difference: "+ timeUnitDiffs);
		} else if (this.mainframe.filtermenu_interval_radioButtonWeeks.isSelected()) {
			timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 7) + 1;
			this.timelineDateSteps = 2;
			System.out.println("Weeks difference: "+ timeUnitDiffs);
		} else {
			timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 30);
			this.timelineDateSteps = 3;
		}
		this.timelineMaxValue = timeUnitDiffs;
		this.timelineSmallestTimeInterval = 1;
		this.timelineHigherValue = 2;
		this.timelineLowerValue = 1;
		this.mainframe.timeLineBiSlider.setValues(1, this.timelineMaxValue);
		this.mainframe.timeLineBiSlider.setMaximumColoredValue(this.timelineHigherValue);
		this.mainframe.timeLineBiSlider.setMinimumColoredValue(this.timelineLowerValue);
		this.mainframe.timeLineBiSlider.setSegmentSize(this.timelineSmallestTimeInterval);
		this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
	}
	
	public void applySettings(){
		this.refreshGlobalDates();
		this.refreshTimelineAttributes();
		this.refreshWeekdays();
		this.refreshCurrentCategory();
		
		//Test:
//		try {
//			this.cCaseDatabase.selectCategory(this.currentCategory);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void refreshGlobalDates() {
		this.globalFromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
		this.globalToDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
	}

	private void refreshWeekdays() {
		ArrayList<Integer> tempWeekdayList = new ArrayList<Integer>();
		if(this.mainframe.checkBox_Sun.isSelected()){
			tempWeekdayList.add(1);
		}
		if(this.mainframe.checkBox_Mon.isSelected()){
			tempWeekdayList.add(2);
		}
		if(this.mainframe.checkBox_Tue.isSelected()){
			tempWeekdayList.add(3);
		}
		if(this.mainframe.checkBox_Wed.isSelected()){
			tempWeekdayList.add(4);
		}
		if(this.mainframe.checkBox_Thu.isSelected()){
			tempWeekdayList.add(5);
		}
		if(this.mainframe.checkBox_Fri.isSelected()){
			tempWeekdayList.add(6);
		}
		if(this.mainframe.checkBox_Sat.isSelected()){
			tempWeekdayList.add(7);
		}
		Integer[] tempArray = ((Integer[]) tempWeekdayList.toArray(new Integer[0]));
		this.weekdays = new int[tempArray.length];
		for(int i = 0; i<tempArray.length-1; i++){
			this.weekdays[i] = tempArray[i];
		}
	}

	public void defaultSettings(){
		try {
			this.init();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void loadData(String path){
		this.cCaseDatabase.reindexCSV(path);
	}
	
	public void fromDateAction(Date newDate){
		System.out.println("fromDate changed");
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		if(this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate().before(newDate)) {
			this.setToDate(new Date(newDate.getTime() + 86400000)); // + 1 Day from newDate
			this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
			this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
		} else {
			this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
			this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
		}
		this.mainframe.repaint();
	}
	
	public void toDateAction(Date newDate){
		System.out.println("toDate changed");
		String newDateText;
		if(this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate().after(newDate)) {
			this.setFromDate(new Date(newDate.getTime() - 86400000)); // -1 Day from newDate
			newDateText = this.standardGUIDateOutputFormat.format(newDate);
			this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
			this.mainframe.timeline_panel_toDate_label.setText(newDateText);
		} else {
			newDateText = this.standardGUIDateOutputFormat.format(newDate);
			this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
			this.mainframe.timeline_panel_toDate_label.setText(newDateText);
		}
	}
	
	private void setFromDate(Date newDate){
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		this.mainframe.filtermenu_dates_leftCalendarButton.setTargetDate(newDate);
		this.mainframe.filtermenu_dates_leftCalendarButton.setText(newDateText);
		this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
	}
	
	private void setToDate(Date newDate){
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
		this.mainframe.timeline_panel_toDate_label.setText(newDateText);
	}
	
	public void timeLineChanged(double minColorValue, double maxColorValue){
		if(this.hasTimeLineChanged((int) (minColorValue+0.5), (int) (maxColorValue+0.5))){
			this.timelineLowerValue = (int) (minColorValue+0.5);
			this.timelineHigherValue = (int) (maxColorValue+0.5);
//			System.out.println("Min: "+this.timelineLowerValue+", Max: "+this.timelineHigherValue);
			this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
			try {
				System.out.println("fromDate: "+this.currentFromDate+" , toDate: "+this.currentToDate);
				if(this.mainframe.filtermenu_interval_radioButtonHours.isSelected() || this.mainframe.filtermenu_interval_radioButtonDays.isSelected() || (this.mainframe.checkBox_Mon.isSelected() && this.mainframe.checkBox_Tue.isSelected() && this.mainframe.checkBox_Wed.isSelected() && this.mainframe.checkBox_Thu.isSelected() && this.mainframe.checkBox_Fri.isSelected() && this.mainframe.checkBox_Sat.isSelected() && this.mainframe.checkBox_Sun.isSelected())){
					this.cCaseDatabase.selectAllCasesBetweenTwoDates(this.currentFromDate, this.currentToDate, this.currentCategory);
				} else { // UNDER CONSTRUCTION - NOT TESTED
					this.cCaseDatabase.selectWeekdaysCasesBetweenDatesToCurrentData(this.currentFromDate, this.currentToDate, this.weekdays, this.currentCategory);
				}
				this.geoMapController.loadPoints(this.cCaseDatabase.getCurrentData());
				this.geoMapController.setShowCurrentPoints(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void refreshCurrentDates(int minValue, int maxValue){
		Calendar temp = Calendar.getInstance();
		if(this.timelineDateSteps == 0){ //hourly
			temp.setTime(globalFromDate);
			temp.add(Calendar.HOUR_OF_DAY, (minValue-1));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.HOUR_OF_DAY, (maxValue-1));
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else if(this.timelineDateSteps == 1){ //daily
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, (minValue-1));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, (maxValue-1));
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else if(this.timelineDateSteps == 2){ //weekly
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, ((minValue-1)*7));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, ((maxValue-1)*7));
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else { //monthly
			temp.setTime(globalFromDate);
			temp.add(Calendar.MONTH, (minValue-1));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.MONTH, (maxValue-1));
			this.currentToDate = new Date(temp.getTimeInMillis());
		}
	}
	
	private boolean hasTimeLineChanged(int minColorValue, int maxColorValue){
		if(this.timelineLowerValue == minColorValue && this.timelineHigherValue == maxColorValue){
			return false;
		} else {
			return true;
		}
	}

	public void refreshCurrentCategory() {
		this.currentCategory = (String) this.mainframe.filtermenu_comboBox_category.getSelectedItem();
	}
}
