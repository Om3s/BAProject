package mvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;

public class MainframeController {
	private Mainframe mainframe;
	private CrimeCaseDatabase cCaseDatabase;
	private Date defaultFromDate;
	private Date defaultToDate;
	
	public MainframeController(Mainframe frame, CrimeCaseDatabase dataBase) throws ParseException{
		this.mainframe = frame;
		this.cCaseDatabase = dataBase;
		this.defaultFromDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("01/01/2011 12:00:01 AM");
		this.defaultToDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").parse("03/01/2011 23:59:99 PM");
		
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
		SimpleDateFormat dateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fromDateString = dateOutputFormat.format(this.mainframe.filtermenu_dates_leftCalendarButton.getTargetDate());
		String toDateString = dateOutputFormat.format(this.mainframe.filtermenu_dates_rightCalendarButton.getTargetDate());
		this.mainframe.filtermenu_dates_rightCalendarButton.setText(toDateString);
		this.mainframe.filtermenu_dates_leftCalendarButton.setText(fromDateString);
		this.mainframe.timeline_panel_fromDate_label.setText(fromDateString);
		this.mainframe.timeline_panel_toDate_label.setText(toDateString);
	}
	
	public void applySettings(){
		
	}
	
	public void defaultSettings(){
		try {
			this.init();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void loadData(String path){
		System.out.println("DATA LOADED NOT FROM: "+path);
//		this.cCaseDatabase.reindexCSV(path);
	}
}
