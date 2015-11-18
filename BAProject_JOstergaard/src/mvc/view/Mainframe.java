package mvc.view;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import mvc.controller.MainframeController;
import mvc.controller.MapController;
import mvc.model.CaseReport;
import mvc.model.MyJList;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import com.visutools.nav.bislider.BiSlider;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;

public class Mainframe extends JFrame {
	public JMapViewer geoMap;
	private MainframeController controller;
	private final JFileChooser fileChooser;
	private final JMenuItem fileMenu_item_load;
	private final JMenuItem fileMenu_item_exit;
	private MapController geoMapController;
	public final ButtonGroup filtermenu_interval_buttongroup;
	public final JRadioButton filtermenu_interval_radioButtonMonths;
	public final JRadioButton filtermenu_interval_radioButtonWeeks;
	public final JRadioButton filtermenu_interval_radioButtonDays;
	public final JRadioButton filtermenu_interval_radioButtonHours;
	public final JCheckBox checkBox_Mon;
	public final JCheckBox checkBox_Tue;
	public final JCheckBox checkBox_Wed;
	public final JCheckBox checkBox_Thu;
	public final JCheckBox checkBox_Fri;
	public final JCheckBox checkBox_Sat;
	public final JCheckBox checkBox_Sun;
	public final JCalendarButton filtermenu_dates_leftCalendarButton, filtermenu_dates_rightCalendarButton;
	public final JButton filtermenu_buttons_applyButton;
	public final JButton filtermenu_buttons_defaultButton;
	public final JComboBox<String> filtermenu_comboBox_category;
	public ChartPanel innerChartPanel;
	public JFreeChart barChart;
	public final JPanel gui_chart_panel;
	private JPanel reportList_panel;
	public MyJList<CaseReport> reportList;
	public DefaultListModel<CaseReport> reportListModel;
	public JTextArea selectedCaseDetails_textArea;
	public JLabel timeline_panel_fromDate_label;
	public JLabel timeline_panel_toDate_label;
	public BiSlider timeLineBiSlider;
	private JLabel timeLine_range_label;
	
	
	public Mainframe(JMapViewer map, MapController geoMapController){
		super();
		this.geoMap = map;
		this.geoMapController = geoMapController;
		//radiobuttons:
		this.filtermenu_interval_buttongroup = new ButtonGroup();
		this.filtermenu_interval_radioButtonMonths = new JRadioButton("Months");
		this.filtermenu_interval_radioButtonWeeks = new JRadioButton("Weeks");
		this.filtermenu_interval_radioButtonDays = new JRadioButton("Days");
		this.filtermenu_interval_radioButtonHours = new JRadioButton("Hours");
		//Day of Week Checkboxes:
		this.checkBox_Mon = new JCheckBox("Mon");
		this.checkBox_Tue = new JCheckBox("Tue");
		this.checkBox_Wed = new JCheckBox("Wed");
		this.checkBox_Thu = new JCheckBox("Thu");
		this.checkBox_Fri = new JCheckBox("Fri");
		this.checkBox_Sat = new JCheckBox("Sat");
		this.checkBox_Sun = new JCheckBox("Sun");
		//calendarbuttons:
		this.filtermenu_dates_leftCalendarButton = new JCalendarButton();
		this.filtermenu_dates_rightCalendarButton = new JCalendarButton();
		//Filechooser:
		this.fileChooser = new JFileChooser();
		//MenuItems:
		this.fileMenu_item_load = new JMenuItem("Load");
		this.fileMenu_item_exit = new JMenuItem("Exit");
		//Apply/Default Buttons:
		this.filtermenu_buttons_applyButton = new JButton("Apply");
		this.filtermenu_buttons_defaultButton = new JButton("Default");
		//Combobox Categories:
		this.filtermenu_comboBox_category = new JComboBox();
		//ChartPanel
		this.gui_chart_panel = new JPanel();
		
		this.init();
	}
	
	public void setController(MainframeController mfC){
		this.controller = mfC;
	}
	
	private void init(){
		// =================== WINDOW SETTINGS: =================== 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenWidth, screenHeight, frameWidth, frameHeight;
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		screenWidth = gd.getDisplayMode().getWidth(); 
		screenHeight = gd.getDisplayMode().getHeight();
		frameWidth = (int) (screenWidth / 1.75);
		frameHeight = (int) (frameWidth * 0.75);
		this.setSize(frameWidth, frameHeight);
		this.setLocation((int)((screenWidth / 2) - (frameWidth / 2)), (int)((screenHeight / 2) - (frameHeight / 2 )));
		this.setMinimumSize(new Dimension(800, 450));
		
		
		
		// =================== GUI LAYOUT: =================== 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {100, 200, 200, 100};
		gridBagLayout.rowHeights = new int[] {0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.2, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{1.0};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel analysis_panel = new JPanel();
		GridBagConstraints gbc_analysis_panel = new GridBagConstraints();
		gbc_analysis_panel.gridwidth = 2;
		gbc_analysis_panel.weightx = 1.0;
		gbc_analysis_panel.fill = GridBagConstraints.BOTH;
		gbc_analysis_panel.gridx = 1;
		gbc_analysis_panel.gridy = 0;
		getContentPane().add(analysis_panel, gbc_analysis_panel);
		GridBagLayout gbl_analysis_panel = new GridBagLayout();
		gbl_analysis_panel.columnWidths = new int[]{0};
		gbl_analysis_panel.rowHeights = new int[] {0, 0, 0};
		gbl_analysis_panel.columnWeights = new double[]{1.0};
		gbl_analysis_panel.rowWeights = new double[]{0.6, 0.35, 1.0};
		analysis_panel.setLayout(gbl_analysis_panel);
		
		JPanel geomap_panel = new JPanel();
		geomap_panel.setBackground(Color.GREEN);
		geomap_panel.setForeground(Color.BLACK);
		geomap_panel.setToolTipText("GeoMap for Hotspot visualization");
		GridBagConstraints gbc_geomap_panel = new GridBagConstraints();
		gbc_geomap_panel.weighty = 0.85;
		gbc_geomap_panel.insets = new Insets(0, 0, 0, 0);
		gbc_geomap_panel.fill = GridBagConstraints.BOTH;
		gbc_geomap_panel.gridx = 0;
		gbc_geomap_panel.gridy = 0;
		analysis_panel.add(geomap_panel, gbc_geomap_panel);
		geomap_panel.add(this.geoMap);
		GridLayout gl_geomap_panel = new GridLayout();
		geomap_panel.setLayout(gl_geomap_panel);
		
		JPanel timeline_panel = new JPanel();
		GridBagConstraints gbc_timeline_panel = new GridBagConstraints();
		gbc_timeline_panel.fill = GridBagConstraints.BOTH;
		gbc_timeline_panel.gridx = 0;
		gbc_timeline_panel.gridy = 1;
		analysis_panel.add(timeline_panel, gbc_timeline_panel);
		GridBagLayout gbl_timeline_panel = new GridBagLayout();
		gbl_timeline_panel.columnWidths = new int[] {30, 30, 30};
		gbl_timeline_panel.rowHeights = new int[] {10, 30};
		gbl_timeline_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_timeline_panel.rowWeights = new double[]{0.0, 0.0};
		timeline_panel.setLayout(gbl_timeline_panel);
		
		timeline_panel_toDate_label = new JLabel("toDate");
		timeline_panel_toDate_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeline_panel_toDate_label = new GridBagConstraints();
		gbc_timeline_panel_toDate_label.weighty = 0.01;
		gbc_timeline_panel_toDate_label.weightx = 1.0;
		gbc_timeline_panel_toDate_label.anchor = GridBagConstraints.NORTHEAST;
		gbc_timeline_panel_toDate_label.insets = new Insets(0, 0, 5, 0);
		gbc_timeline_panel_toDate_label.gridx = 2;
		gbc_timeline_panel_toDate_label.gridy = 0;
		timeline_panel.add(timeline_panel_toDate_label, gbc_timeline_panel_toDate_label);
		
		this.timeLineBiSlider = new BiSlider();
		this.timeLineBiSlider.setMinimumSize(new Dimension(350, 50));
		this.timeLineBiSlider.setMinimumColor(Color.GRAY);
		this.timeLineBiSlider.setMaximumColor(Color.LIGHT_GRAY);
		GridBagConstraints gbc_timeLineBiSlider = new GridBagConstraints();
		gbc_timeLineBiSlider.weighty = 1.0;
		gbc_timeLineBiSlider.insets = new Insets(0, 13, 5, 0);
		gbc_timeLineBiSlider.weightx = 1.0;
		gbc_timeLineBiSlider.gridwidth = 3;
		gbc_timeLineBiSlider.fill = GridBagConstraints.BOTH;
		gbc_timeLineBiSlider.gridx = 0;
		gbc_timeLineBiSlider.gridy = 1;
		timeline_panel.add(timeLineBiSlider, gbc_timeLineBiSlider);
		
		timeline_panel_fromDate_label = new JLabel("fromDate");
		timeline_panel_fromDate_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeline_panel_fromDate_label = new GridBagConstraints();
		gbc_timeline_panel_fromDate_label.insets = new Insets(0, 0, 5, 0);
		gbc_timeline_panel_fromDate_label.weighty = 0.01;
		gbc_timeline_panel_fromDate_label.weightx = 1.0;
		gbc_timeline_panel_fromDate_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_timeline_panel_fromDate_label.gridx = 0;
		gbc_timeline_panel_fromDate_label.gridy = 0;
		timeline_panel.add(timeline_panel_fromDate_label, gbc_timeline_panel_fromDate_label);
		
		timeLine_range_label = new JLabel("Range");
		timeLine_range_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeLine_range_label = new GridBagConstraints();
		gbc_timeLine_range_label.weighty = 0.01;
		gbc_timeLine_range_label.weightx = 1.0;
		gbc_timeLine_range_label.anchor = GridBagConstraints.NORTH;
		gbc_timeLine_range_label.gridx = 1;
		gbc_timeLine_range_label.gridy = 0;
		timeline_panel.add(timeLine_range_label, gbc_timeLine_range_label);
//		this.rangeSlider = new JRangeSlider(0, 100, 0, 50, 1);
//		panel.add(this.rangeSlider, BorderLayout.PAGE_END);
		
		GridBagConstraints gbc_chart_panel = new GridBagConstraints();
		gbc_chart_panel.weighty = 0.1;
		gbc_chart_panel.fill = GridBagConstraints.BOTH;
		gbc_chart_panel.gridx = 0;
		gbc_chart_panel.gridy = 2;
		analysis_panel.add(this.gui_chart_panel, gbc_chart_panel);
						
		JPanel filtermenu_panel = new JPanel();
		filtermenu_panel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_filtermenu_panel = new GridBagConstraints();
		gbc_filtermenu_panel.insets = new Insets(0, 0, 0, 0);
		gbc_filtermenu_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_panel.gridx = 3;
		gbc_filtermenu_panel.gridy = 0;
		getContentPane().add(filtermenu_panel, gbc_filtermenu_panel);
		GridBagLayout gbl_filtermenu_panel = new GridBagLayout();
		gbl_filtermenu_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_panel.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_filtermenu_panel.columnWeights = new double[]{1.0, 1.0};
		gbl_filtermenu_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0};
		filtermenu_panel.setLayout(gbl_filtermenu_panel);
		
		JPanel filtermenu_interval_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_interval_panel = new GridBagConstraints();
		gbc_filtermenu_interval_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_interval_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_interval_panel.gridwidth = 2;
		gbc_filtermenu_interval_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_interval_panel.gridx = 0;
		gbc_filtermenu_interval_panel.gridy = 2;
		filtermenu_panel.add(filtermenu_interval_panel, gbc_filtermenu_interval_panel);
		GridBagLayout gbl_filtermenu_interval_panel = new GridBagLayout();
		gbl_filtermenu_interval_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_interval_panel.rowHeights = new int[] {0, 0, 0};
		gbl_filtermenu_interval_panel.columnWeights = new double[]{0.0, 0.0};
		gbl_filtermenu_interval_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
		filtermenu_interval_panel.setLayout(gbl_filtermenu_interval_panel);
		
		JLabel filtermenu_interval_label = new JLabel("Choose time interval:");
		GridBagConstraints gbc_filtermenu_intervall_label = new GridBagConstraints();
		gbc_filtermenu_intervall_label.gridwidth = 2;
		gbc_filtermenu_intervall_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_intervall_label.gridx = 0;
		gbc_filtermenu_intervall_label.gridy = 0;
		filtermenu_interval_panel.add(filtermenu_interval_label, gbc_filtermenu_intervall_label);
		
		GridBagConstraints gbc_filtermenu_interval_radioButtonMonths = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonMonths.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_interval_radioButtonMonths.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonMonths.gridx = 0;
		gbc_filtermenu_interval_radioButtonMonths.gridy = 1;
		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonMonths, gbc_filtermenu_interval_radioButtonMonths);
		
		GridBagConstraints gbc_filtermenu_interval_radioButtonWeeks = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonWeeks.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonWeeks.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_interval_radioButtonWeeks.gridx = 1;
		gbc_filtermenu_interval_radioButtonWeeks.gridy = 1;
		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonWeeks, gbc_filtermenu_interval_radioButtonWeeks);
		
		GridBagConstraints gbc_filtermenu_interval_radioButtonDays = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonDays.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_interval_radioButtonDays.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonDays.gridx = 0;
		gbc_filtermenu_interval_radioButtonDays.gridy = 2;
		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonDays, gbc_filtermenu_interval_radioButtonDays);
		
		GridBagConstraints gbc_filtermenu_interval_radioButtonHours = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonHours.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_interval_radioButtonHours.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonHours.gridx = 1;
		gbc_filtermenu_interval_radioButtonHours.gridy = 2;
		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonHours, gbc_filtermenu_interval_radioButtonHours);
		
		this.filtermenu_interval_buttongroup.add(this.filtermenu_interval_radioButtonDays);
		this.filtermenu_interval_buttongroup.add(this.filtermenu_interval_radioButtonWeeks);
		this.filtermenu_interval_buttongroup.add(this.filtermenu_interval_radioButtonMonths);
		this.filtermenu_interval_buttongroup.add(this.filtermenu_interval_radioButtonHours);
		
		JPanel filtermenu_specific_dow_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_specific_dow_panel = new GridBagConstraints();
		gbc_filtermenu_specific_dow_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_specific_dow_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_specific_dow_panel.gridwidth = 2;
		gbc_filtermenu_specific_dow_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_specific_dow_panel.gridx = 0;
		gbc_filtermenu_specific_dow_panel.gridy = 3;
		filtermenu_panel.add(filtermenu_specific_dow_panel, gbc_filtermenu_specific_dow_panel);
		GridBagLayout gbl_filtermenu_specific_dow_panel = new GridBagLayout();
		gbl_filtermenu_specific_dow_panel.columnWidths = new int[] {0, 0, 0};
		gbl_filtermenu_specific_dow_panel.rowHeights = new int[] {0, 0, 0, 0};
		gbl_filtermenu_specific_dow_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_filtermenu_specific_dow_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		filtermenu_specific_dow_panel.setLayout(gbl_filtermenu_specific_dow_panel);
				
		GridBagConstraints gbc_checkBox_Mon = new GridBagConstraints();
		gbc_checkBox_Mon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Mon.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Mon.gridx = 0;
		gbc_checkBox_Mon.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Mon, gbc_checkBox_Mon);

		GridBagConstraints gbc_checkBox_Tue = new GridBagConstraints();
		gbc_checkBox_Tue.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Tue.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Tue.gridx = 1;
		gbc_checkBox_Tue.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Tue, gbc_checkBox_Tue);

		GridBagConstraints gbc_checkBox_Wed = new GridBagConstraints();
		gbc_checkBox_Wed.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Wed.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_Wed.gridx = 2;
		gbc_checkBox_Wed.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Wed, gbc_checkBox_Wed);

		GridBagConstraints gbc_checkBox_Thu_ = new GridBagConstraints();
		gbc_checkBox_Thu_.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Thu_.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Thu_.gridx = 0;
		gbc_checkBox_Thu_.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Thu, gbc_checkBox_Thu_);

		GridBagConstraints gbc_checkBox_Fri = new GridBagConstraints();
		gbc_checkBox_Fri.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Fri.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Fri.gridx = 1;
		gbc_checkBox_Fri.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Fri, gbc_checkBox_Fri);

		GridBagConstraints gbc_checkBox_Sat = new GridBagConstraints();
		gbc_checkBox_Sat.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sat.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_Sat.gridx = 2;
		gbc_checkBox_Sat.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Sat, gbc_checkBox_Sat);

		GridBagConstraints gbc_checkBox_Sun = new GridBagConstraints();
		gbc_checkBox_Sun.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sun.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_Sun.gridx = 0;
		gbc_checkBox_Sun.gridy = 3;
		filtermenu_specific_dow_panel.add(checkBox_Sun, gbc_checkBox_Sun);
		
		JLabel filtermenu_specific_dow_label = new JLabel("Choose specific weekdays:");
		GridBagConstraints gbc_filtermenu_specific_dow_label = new GridBagConstraints();
		gbc_filtermenu_specific_dow_label.gridwidth = 3;
		gbc_filtermenu_specific_dow_label.insets = new Insets(0, 0, 0, 5);
		gbc_filtermenu_specific_dow_label.gridx = 0;
		gbc_filtermenu_specific_dow_label.gridy = 0;
		filtermenu_specific_dow_panel.add(filtermenu_specific_dow_label, gbc_filtermenu_specific_dow_label);
		
		JPanel filtermenu_buttons_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_buttons_panel = new GridBagConstraints();
		gbc_filtermenu_buttons_panel.anchor = GridBagConstraints.SOUTH;
		gbc_filtermenu_buttons_panel.gridwidth = 2;
		gbc_filtermenu_buttons_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_buttons_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_buttons_panel.gridx = 0;
		gbc_filtermenu_buttons_panel.gridy = 4;
		filtermenu_panel.add(filtermenu_buttons_panel, gbc_filtermenu_buttons_panel);
		filtermenu_buttons_panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		
		filtermenu_buttons_panel.add(filtermenu_buttons_applyButton);
		filtermenu_buttons_panel.add(filtermenu_buttons_defaultButton);
		
		((JLabel)filtermenu_comboBox_category.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		filtermenu_comboBox_category.addItem("All categories");
		filtermenu_comboBox_category.addItem("SFHA Requests");
		filtermenu_comboBox_category.addItem("Abandoned Vehicle");
		filtermenu_comboBox_category.addItem("Street and Sidewalk Cleaning");
		filtermenu_comboBox_category.addItem("Litter Receptacles");
		filtermenu_comboBox_category.addItem("Sign Repair");
		filtermenu_comboBox_category.addItem("Residential Building Request");
		filtermenu_comboBox_category.addItem("Graffiti Public Property");
		filtermenu_comboBox_category.addItem("Streetlights");
		filtermenu_comboBox_category.addItem("Damaged Property");
		filtermenu_comboBox_category.addItem("Street Defects");
		filtermenu_comboBox_category.addItem("Blocked Street or SideWalk");
		filtermenu_comboBox_category.addItem("Tree Maintenance");
		filtermenu_comboBox_category.addItem("MUNI Feedback");
		filtermenu_comboBox_category.addItem("Illegal Postings");
		filtermenu_comboBox_category.addItem("Color Curb");
		filtermenu_comboBox_category.addItem("General Requests");
		filtermenu_comboBox_category.addItem("Sewer Issues");
		filtermenu_comboBox_category.addItem("Rec and Park Requests");
		filtermenu_comboBox_category.addItem("Temporary Sign Request");
		filtermenu_comboBox_category.addItem("Graffiti Private Property");
		filtermenu_comboBox_category.addItem("311 External Request");
		filtermenu_comboBox_category.addItem("Sidewalk or Curb");
		filtermenu_comboBox_category.addItem("Catch Basin Maintenance");
		filtermenu_comboBox_category.addItem("Interdepartmental Request");
		filtermenu_comboBox_category.addItem("DPW Volunteer Programs");
		filtermenu_comboBox_category.addItem("Unpermitted Cab Complaint");
		filtermenu_comboBox_category.addItem("Construction Zone Permits");
		GridBagConstraints gbc_filtermenu_comboBox_category = new GridBagConstraints();
		gbc_filtermenu_comboBox_category.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_comboBox_category.gridwidth = 2;
		gbc_filtermenu_comboBox_category.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_comboBox_category.gridx = 0;
		gbc_filtermenu_comboBox_category.gridy = 0;
		filtermenu_panel.add(filtermenu_comboBox_category, gbc_filtermenu_comboBox_category);
		
		JPanel filtermenu_dates_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_dates_panel = new GridBagConstraints();
		gbc_filtermenu_dates_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_dates_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_dates_panel.gridwidth = 2;
		gbc_filtermenu_dates_panel.insets = new Insets(0, 3, 3, 3);
		gbc_filtermenu_dates_panel.gridx = 0;
		gbc_filtermenu_dates_panel.gridy = 1;
		filtermenu_panel.add(filtermenu_dates_panel, gbc_filtermenu_dates_panel);
		GridBagLayout gbl_filtermenu_dates_panel = new GridBagLayout();
		gbl_filtermenu_dates_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_dates_panel.rowHeights = new int[] {0, 0, 0};
		gbl_filtermenu_dates_panel.columnWeights = new double[]{0.0, 0.0};
		gbl_filtermenu_dates_panel.rowWeights = new double[]{0.0, 0.0};
		filtermenu_dates_panel.setLayout(gbl_filtermenu_dates_panel);
				
				JLabel filtermenu_dates_label = new JLabel("Choose time span:");
				GridBagConstraints gbc_filtermenu_dates_label = new GridBagConstraints();
				gbc_filtermenu_dates_label.gridwidth = 2;
				gbc_filtermenu_dates_label.insets = new Insets(0, 0, 5, 5);
				gbc_filtermenu_dates_label.gridx = 0;
				gbc_filtermenu_dates_label.gridy = 0;
				filtermenu_dates_panel.add(filtermenu_dates_label, gbc_filtermenu_dates_label);
		
				this.filtermenu_dates_leftCalendarButton.setText("From");
				GridBagConstraints gbc_filtermenu_dates_leftCalendarButton = new GridBagConstraints();
				gbc_filtermenu_dates_leftCalendarButton.fill = GridBagConstraints.BOTH;
				gbc_filtermenu_dates_leftCalendarButton.insets = new Insets(0, 3, 3, 5);
				gbc_filtermenu_dates_leftCalendarButton.gridx = 0;
				gbc_filtermenu_dates_leftCalendarButton.gridy = 1;
				filtermenu_dates_panel.add(this.filtermenu_dates_leftCalendarButton, gbc_filtermenu_dates_leftCalendarButton);
				
				this.filtermenu_dates_rightCalendarButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
				this.filtermenu_dates_rightCalendarButton.setText("To");
				GridBagConstraints gbc_filtermenu_dates_rightCalendarButton = new GridBagConstraints();
				gbc_filtermenu_dates_rightCalendarButton.insets = new Insets(0, 3, 3, 3);
				gbc_filtermenu_dates_rightCalendarButton.fill = GridBagConstraints.BOTH;
				gbc_filtermenu_dates_rightCalendarButton.gridx = 1;
				gbc_filtermenu_dates_rightCalendarButton.gridy = 1;
				filtermenu_dates_panel.add(this.filtermenu_dates_rightCalendarButton, gbc_filtermenu_dates_rightCalendarButton);
				
				this.reportList_panel = new JPanel();
				GridBagConstraints gbc_reportList_panel = new GridBagConstraints();
				gbc_reportList_panel.anchor = GridBagConstraints.WEST;
				gbc_reportList_panel.fill = GridBagConstraints.BOTH;
				gbc_reportList_panel.weightx = 0.5;
				gbc_reportList_panel.insets = new Insets(0, 0, 0, 5);
				gbc_reportList_panel.gridx = 0;
				gbc_reportList_panel.gridy = 0;
				getContentPane().add(reportList_panel, gbc_reportList_panel);
				
				this.reportListModel = new DefaultListModel<CaseReport>();
				this.reportList_panel.setLayout(new BoxLayout(reportList_panel, BoxLayout.Y_AXIS));
				this.reportList = new MyJList<CaseReport>(this.reportListModel);
				this.reportList.setToolTipText("");
				this.reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.reportList.setLayoutOrientation(JList.VERTICAL);
				this.reportList.setVisibleRowCount(-1);
				JScrollPane reportList_scrollPane = new JScrollPane(reportList);
				this.reportList_panel.add(reportList_scrollPane);
				
				this.selectedCaseDetails_textArea = new JTextArea();
				this.selectedCaseDetails_textArea.setMaximumSize(new Dimension(2000, 400));
				this.selectedCaseDetails_textArea.setMinimumSize(new Dimension(300, 10));
				this.reportList_panel.add(selectedCaseDetails_textArea);
		
		JMenuBar menuBar = new JMenuBar();
		GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.insets = new Insets(0, 0, 0, 5);
		gbc_menuBar.gridx = 1;
		gbc_menuBar.gridy = 0;
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(fileMenu_item_load);

		fileMenu.add(fileMenu_item_exit);
		
		this.setJMenuBar(menuBar);
		
		
		
		// =================== EVENTHANDLER: =================== 
this.timeLineBiSlider.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				BiSlider src = (BiSlider) e.getSource();
				Mainframe.this.controller.timeLineChanged(src.getMinimumColoredValue(), src.getMaximumColoredValue());
			}
		});
		
		this.timeLineBiSlider.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {

			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				BiSlider src = (BiSlider) e.getSource();
				Mainframe.this.controller.timeLineChanged(src.getMinimumColoredValue(), src.getMaximumColoredValue());
			}
		});
		
		this.fileMenu_item_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		this.fileMenu_item_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == Mainframe.this.fileMenu_item_load) {
					int returnVal = Mainframe.this.fileChooser.showOpenDialog(Mainframe.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = Mainframe.this.fileChooser.getSelectedFile();
						//This is where a real application would open the file.
//						log.append("Opening: " + file.getName() + "." + newline);
					} else {
//						log.append("Open command cancelled by user." + newline);
					}
			    }
				Mainframe.this.controller.loadData(Mainframe.this.fileChooser.getSelectedFile().getPath());
			}
		});
		
		this.filtermenu_comboBox_category.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		this.filtermenu_interval_radioButtonDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		this.filtermenu_interval_radioButtonWeeks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		this.filtermenu_interval_radioButtonMonths.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		this.filtermenu_dates_leftCalendarButton.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					Mainframe.this.controller.fromDateAction((Date)evt.getNewValue());
				} 
			}
		});
		
		this.filtermenu_dates_rightCalendarButton.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					Mainframe.this.controller.toDateAction((Date)evt.getNewValue());
				}
			}
		});
		
		this.filtermenu_buttons_applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Apply button.");
				Mainframe.this.controller.applySettings();
			}
		});
		
		this.filtermenu_buttons_defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Default button.");
				Mainframe.this.controller.defaultSettings();
			}
		});
		
		this.reportList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){
					Mainframe.this.controller.onNewCaseSelection();
				}
			}
		});
	}

	public MapController getGeoMapController() {
		return geoMapController;
	}
}
