package mvc.controller;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeListener;

import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;

public class MainframeController {
	private Mainframe mainframe;
	private CrimeCaseDatabase cCaseDatabase;
	private Date defaultFromDate;
	private Date defaultToDate;
	private SimpleDateFormat standardGUIDateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	//Timeline:
	private int timelineSmallestTimeInterval = 1;
	private int timelineMaxValue = 100;
	private int timelineLowerValue = 0;
	private int timelineHigherValue = 5;
	
	
	public MainframeController(Mainframe frame, CrimeCaseDatabase dataBase) throws ParseException{
		this.mainframe = frame;
		this.cCaseDatabase = dataBase;
		this.defaultFromDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("01/01/2011 12:00:01 AM");
		this.defaultToDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("02/01/2011 23:59:99 PM");
		
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
		//Day of Week Checkboxes:
		this.mainframe.checkBox_Mon.setSelected(true);
		this.mainframe.checkBox_Tue.setSelected(true);
		this.mainframe.checkBox_Wed.setSelected(true);
		this.mainframe.checkBox_Thu.setSelected(true);
		this.mainframe.checkBox_Fri.setSelected(true);
		this.mainframe.checkBox_Sat.setSelected(true);
		this.mainframe.checkBox_Sun.setSelected(true);
		//calendarbuttons:
		this.mainframe.filtermenu_dates_leftCalendarButton.setTargetDate(defaultFromDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(defaultToDate);
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
	}
	
	private void refreshTimelineAttributes(){
		Date fromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
		Date toDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
		long diffInMillis = toDate.getTime() - fromDate.getTime();
		int timeUnitDiffs;
		if(this.mainframe.filtermenu_interval_radioButtonHours.isSelected()){
			timeUnitDiffs = (int)TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			System.out.println("Hours difference: "+ timeUnitDiffs);
		} else if (this.mainframe.filtermenu_interval_radioButtonDays.isSelected()) {
			timeUnitDiffs = (int)TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			System.out.println("Days difference: "+ timeUnitDiffs);
		} else if (this.mainframe.filtermenu_interval_radioButtonWeeks.isSelected()) {
			timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 7) + 1;
			System.out.println("Weeks difference: "+ timeUnitDiffs);
		} else {
			timeUnitDiffs = -1;
		}
		this.timelineMaxValue = timeUnitDiffs;
		this.timelineSmallestTimeInterval = 1;
		this.timelineHigherValue = 2;
		this.timelineLowerValue = 1;
		this.mainframe.timeLineBiSlider.setValues(1, this.timelineMaxValue);
		this.mainframe.timeLineBiSlider.setMaximumColoredValue(this.timelineHigherValue);
		this.mainframe.timeLineBiSlider.setMinimumColoredValue(this.timelineLowerValue);
		this.mainframe.timeLineBiSlider.setSegmentSize(this.timelineSmallestTimeInterval);
	}
	
	public void applySettings(){
		this.refreshTimelineAttributes();
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
			System.out.println("Min: "+this.timelineLowerValue+", Max: "+this.timelineHigherValue);
		}
		
	}
	
	private boolean hasTimeLineChanged(int minColorValue, int maxColorValue){
		if(this.timelineLowerValue == minColorValue && this.timelineHigherValue == maxColorValue){
			return false;
		} else {
			return true;
		}
	}
}
