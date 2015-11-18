package mvc.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.CrimeCaseDatabase;
import mvc.model.CustomBarRenderer;
import mvc.view.Mainframe;

public class MainframeController {
	public Mainframe mainframe;
	private CrimeCaseDatabase cCaseDatabase;
	private Date globalFromDate, globalToDate, currentFromDate, currentToDate;
	private SimpleDateFormat standardGUIDateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat standardGUIDateTimeOutputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private byte timelineDateSteps;
	private MapController geoMapController;
	private int[] weekdays = {1,2,3,4,5,6,7};
	private String currentCategory;
	private MapController mapController;
	
	//Timeline:
	private int timelineSmallestTimeInterval = 1;
	private int timelineMaxValue, timelineLowerValue, timelineHigherValue;
	
	//Chart:
	private String intervalName = null;
	
	
	public MainframeController(Mainframe frame, CrimeCaseDatabase dataBase, MapController mC) throws ParseException{
		this.mainframe = frame;
		this.cCaseDatabase = dataBase;
		this.globalFromDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("01/01/2011 12:00:01 AM");
		this.globalToDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("02/01/2011 23:59:99 PM");
		this.geoMapController = this.mainframe.getGeoMapController();
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
		//Radiobutton
		this.mainframe.filtermenu_interval_radioButtonHours.setSelected(true);
		this.timelineDateSteps = 0; // hours
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
		//Weekdays:
		this.refreshWeekdays();
		this.timelineLowerValue = -1; //definetly change HACK
		this.timeLineChanged(0, 1);
		//Chart:
		this.refreshChart();
		//reportListArea
		this.mainframe.selectedCaseDetails_textArea.setEditable(false);
		this.mainframe.selectedCaseDetails_textArea.setLineWrap(true);
	}
	
	private void refreshTimelineAttributes(){
		Date fromDate = this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate();
		Date toDate = this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate();
		long diffInMillis = toDate.getTime() - fromDate.getTime();
		int timeUnitDiffs;
		if(this.mainframe.filtermenu_interval_radioButtonHours.isSelected()){
			timeUnitDiffs = (int)TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			this.timelineDateSteps = 0;
			this.intervalName = "Hours";
		} else if (this.mainframe.filtermenu_interval_radioButtonDays.isSelected()) {
			timeUnitDiffs = (int)TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
			this.timelineDateSteps = 1;
			this.intervalName = "Days";
		} else if (this.mainframe.filtermenu_interval_radioButtonWeeks.isSelected()) {
			timeUnitDiffs = (int)((TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)) / 7) + 1;
			this.timelineDateSteps = 2;
			this.intervalName = "Weeks";
		} else {
			Calendar startCalendar = new GregorianCalendar();
			Calendar endCalendar = new GregorianCalendar();
			startCalendar.setTime(fromDate);
			endCalendar.setTime(toDate);
			timeUnitDiffs = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			System.out.println(timeUnitDiffs);
			timeUnitDiffs = (timeUnitDiffs * 12) + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH) + 1;
			System.out.println(timeUnitDiffs);
			this.timelineDateSteps = 3;
			this.intervalName = "Months";
		}
		this.timelineMaxValue = timeUnitDiffs;
		this.timelineSmallestTimeInterval = 1;
		this.timelineHigherValue = 1;
		this.timelineLowerValue = 0;
		this.mainframe.timeLineBiSlider.setValues(0, this.timelineMaxValue);
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
		this.timelineLowerValue = -1; //definetly change HACK
		this.timeLineChanged(0, 1);
		this.refreshChart();
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
		System.out.println(this.weekdays.length);
		for(int i = 0; i<tempArray.length; i++){
			this.weekdays[i] = tempArray[i];
			System.out.println(i + " : " + this.weekdays[i]);
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
	}
	
	private void setToDate(Date newDate){
		String newDateText = this.standardGUIDateOutputFormat.format(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setTargetDate(newDate);
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(newDateText);
	}
	
	public void timeLineChanged(double minColorValue, double maxColorValue){
		if(this.hasTimeLineChanged((int) (minColorValue+0.5), (int) (maxColorValue+0.5))){
			this.timelineLowerValue = (int) (minColorValue+0.5);
			this.timelineHigherValue = (int) (maxColorValue+0.5);
			this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
			try {
				System.out.println("fromDate: "+this.currentFromDate+" , toDate: "+this.currentToDate);
				this.cCaseDatabase.selectWeekdaysCasesBetweenDatesToCurrentData(this.currentFromDate, this.currentToDate, this.weekdays, this.currentCategory);
				this.geoMapController.loadPoints(this.cCaseDatabase.getCurrentData());
				this.fillReportListWith(this.cCaseDatabase.getCurrentData());
				this.geoMapController.setShowCurrentPoints(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void fillReportListWith(ArrayList<CaseReport> currentData) {
		this.mainframe.reportListModel.clear();
		for(CaseReport cR : this.cCaseDatabase.getCurrentData()){
			this.mainframe.reportListModel.addElement(cR);
		}
	}

	private void refreshCurrentDates(int minValue, int maxValue){
		Calendar temp = Calendar.getInstance();
		SimpleDateFormat tempDateFormat = this.standardGUIDateOutputFormat;
		if(this.timelineDateSteps == 0){ //hourly
			temp.setTime(globalFromDate);
			temp.add(Calendar.HOUR_OF_DAY, (minValue));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.HOUR_OF_DAY, (maxValue));
			tempDateFormat = this.standardGUIDateTimeOutputFormat;
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else if(this.timelineDateSteps == 1){ //daily
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, (minValue));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, (maxValue));
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else if(this.timelineDateSteps == 2){ //weekly
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, ((minValue)*7));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.DATE, ((maxValue)*7));
			this.currentToDate = new Date(temp.getTimeInMillis());
		} else { //monthly
			temp.setTime(globalFromDate);
			temp.add(Calendar.MONTH, (minValue));
			this.currentFromDate = new Date(temp.getTimeInMillis());
			temp.setTime(globalFromDate);
			temp.add(Calendar.MONTH, (maxValue));
			this.currentToDate = new Date(temp.getTimeInMillis());
		}
		//refresh TimeLine GUI elements:
		String newDateText = tempDateFormat.format(this.currentFromDate);
		this.mainframe.timeline_panel_fromDate_label.setText(newDateText);
		newDateText = tempDateFormat.format(this.currentToDate);
		this.mainframe.timeline_panel_toDate_label.setText(newDateText);
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
	
	public void refreshChart(){
		this.mainframe.gui_chart_panel.removeAll();
//		this.mainframe.gui_chart_panel.revalidate();
		this.mainframe.getContentPane().revalidate();
		//Create Chart:
		this.mainframe.barChart = ChartFactory.createBarChart(null, this.intervalName, null, this.createDataset(), PlotOrientation.VERTICAL, false, true, false);
		//Choose Renderer
		final CategoryPlot plot;
		if(this.mainframe.barChart != null){
			plot = this.mainframe.barChart.getCategoryPlot();
			if(this.timelineDateSteps == 0) { //hours
				
			} else if(this.timelineDateSteps == 1){ //days
				CategoryItemRenderer customBarRenderer;
				customBarRenderer = new CustomBarRenderer(this);
				plot.setRenderer(customBarRenderer);
			} else if(this.timelineDateSteps == 2){ //weeks
				
			} else { //months
				
			}
		}
		
		this.mainframe.innerChartPanel = new ChartPanel(this.mainframe.barChart);
		this.mainframe.gui_chart_panel.setLayout(new BorderLayout());
		this.mainframe.gui_chart_panel.add(this.mainframe.innerChartPanel);
		this.mainframe.gui_chart_panel.repaint();
		//Remove Mouselisteners:
		MouseListener[] mListeners = this.mainframe.innerChartPanel.getMouseListeners();
		for(MouseListener mL : mListeners){
			this.mainframe.innerChartPanel.removeMouseListener(mL);
		}
	}
	
	private CategoryDataset createDataset(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int count = 0;
		for(int i=1; i <= this.timelineMaxValue; i++){
			this.refreshCurrentDates(i-1, i);
			try {
				count = this.cCaseDatabase.countCaseReportsFromTo(this.currentFromDate, this.currentToDate, this.weekdays, this.currentCategory);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataset.addValue(count, String.valueOf(i), "");
		}
		this.refreshCurrentDates(this.timelineLowerValue, this.timelineHigherValue);
		
		return dataset;
	}

	/**
	 * This is called when a new Item in the reportList is selected.
	 */
	public void onNewCaseSelection() {
		CaseReport cR = this.mainframe.reportList.getSelectedValue();
		String result;
		if(cR == null){
			result = "";
		} else {
			this.mapController.setSelectedMarker(cR.getPoint());
			result = cR.toString2();
			result = result.substring(11, result.length()-1);
			Pattern pattern = Pattern.compile(Pattern.quote("%$sepa&%$"));
			String[] tokens = pattern.split(result);
			result = "Selected Case:\n";
			for(String s : tokens){
				result += s+"\n";
			}
		}
		this.mainframe.selectedCaseDetails_textArea.setText(result);
		this.mainframe.repaint();
	}
	
	public byte getTimeLineDateSteps(){
		return this.timelineDateSteps;
	}
	
	public Date getGlobalFromDate(){
		return this.globalFromDate;
	}
}
