package mvc.view;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
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
import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.MyJList;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;
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

/**
 * 
 * @author Jonas Ostergaard
 *
 */
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
	private JPanel reportList_panel;
	public MyJList<CaseReport> reportList;
	public DefaultListModel<CaseReport> reportListModel;
	public JLabel timeline_panel_fromDate_label;
	public JLabel timeline_panel_toDate_label;
	public BiSlider timeLineBiSlider;
	private JLabel timeLine_range_label;
	private JLabel filtermenu_specific_dow_daytime_label;
	public JCheckBox checkBox_daytime_morning;
	public JCheckBox checkBox_daytime_noon;
	public JCheckBox checkBox_daytime_afternoon;
	public JCheckBox checkBox_daytime_evening;
	public JCheckBox checkBox_daytime_midnight;
	public JCheckBox checkBox_daytime_latenight;
	private JPanel filtermenu_daytime_panel;
	private JButton reportList_details_button;
	public MatrixVisualization matrix_chart_panel;
	
	
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
		this.matrix_chart_panel = new MatrixVisualization(new int[7][6]);
		
		this.init();
	}
	
	/**
	 * 
	 * @param mfC the MainframeController which controls the functions of this frame
	 */
	public void setController(MainframeController mfC){
		this.controller = mfC;
	}
	
	/**
	 * setting up the GUI and its eventHandlers
	 */
	private void init(){
		// =================== WINDOW SETTINGS: =================== 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int frameWidth, frameHeight;
		frameWidth = (int) (Main.screenWidth / 1.75);
		frameHeight = (int) (frameWidth * 0.75);
		this.setSize(frameWidth, frameHeight);
		this.setLocation((int)((Main.screenWidth / 2) - (frameWidth / 2)), (int)((Main.screenHeight / 2) - (frameHeight / 2 )));
		this.setMinimumSize(new Dimension(800, 450));
		
		
		
		// =================== GUI LAYOUT: =================== 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0};
		gridBagLayout.columnWeights = new double[]{0,0,0};
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
		
		this.timeline_panel_toDate_label = new JLabel("toDate");
		this.timeline_panel_toDate_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeline_panel_toDate_label = new GridBagConstraints();
		gbc_timeline_panel_toDate_label.weighty = 0.01;
		gbc_timeline_panel_toDate_label.weightx = 1.0;
		gbc_timeline_panel_toDate_label.anchor = GridBagConstraints.NORTHEAST;
		gbc_timeline_panel_toDate_label.insets = new Insets(0, 0, 5, 0);
		gbc_timeline_panel_toDate_label.gridx = 2;
		gbc_timeline_panel_toDate_label.gridy = 0;
		timeline_panel.add(this.timeline_panel_toDate_label, gbc_timeline_panel_toDate_label);
		
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
		timeline_panel.add(this.timeLineBiSlider, gbc_timeLineBiSlider);
		
		this.timeline_panel_fromDate_label = new JLabel("fromDate");
		this.timeline_panel_fromDate_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeline_panel_fromDate_label = new GridBagConstraints();
		gbc_timeline_panel_fromDate_label.insets = new Insets(0, 0, 5, 0);
		gbc_timeline_panel_fromDate_label.weighty = 0.01;
		gbc_timeline_panel_fromDate_label.weightx = 1.0;
		gbc_timeline_panel_fromDate_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_timeline_panel_fromDate_label.gridx = 0;
		gbc_timeline_panel_fromDate_label.gridy = 0;
		timeline_panel.add(this.timeline_panel_fromDate_label, gbc_timeline_panel_fromDate_label);
		
		this.timeLine_range_label = new JLabel("Range");
		this.timeLine_range_label.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_timeLine_range_label = new GridBagConstraints();
		gbc_timeLine_range_label.weighty = 0.01;
		gbc_timeLine_range_label.weightx = 1.0;
		gbc_timeLine_range_label.anchor = GridBagConstraints.NORTH;
		gbc_timeLine_range_label.gridx = 1;
		gbc_timeLine_range_label.gridy = 0;
		timeline_panel.add(this.timeLine_range_label, gbc_timeLine_range_label);

		GridBagConstraints gbc_matrix_chart_panel = new GridBagConstraints();
		gbc_matrix_chart_panel.gridwidth = 2;
		gbc_matrix_chart_panel.weightx = 1.0;
		gbc_matrix_chart_panel.fill = GridBagConstraints.BOTH;
		gbc_matrix_chart_panel.gridx = 0;
		gbc_matrix_chart_panel.gridy = 2;
		analysis_panel.add(this.matrix_chart_panel, gbc_matrix_chart_panel);
						
		JPanel filtermenu_panel = new JPanel();
		filtermenu_panel.setBackground(new Color(230,230,230));
		GridBagConstraints gbc_filtermenu_panel = new GridBagConstraints();
		gbc_filtermenu_panel.insets = new Insets(0, 0, 0, 0);
		gbc_filtermenu_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_panel.gridx = 3;
		gbc_filtermenu_panel.gridy = 0;
		getContentPane().add(filtermenu_panel, gbc_filtermenu_panel);
		GridBagLayout gbl_filtermenu_panel = new GridBagLayout();
		gbl_filtermenu_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_filtermenu_panel.columnWeights = new double[]{1.0, 1.0};
		gbl_filtermenu_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0};
		filtermenu_panel.setLayout(gbl_filtermenu_panel);
		
		JPanel filtermenu_interval_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_interval_panel = new GridBagConstraints();
		gbc_filtermenu_interval_panel.weighty = 0.1;
		gbc_filtermenu_interval_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_interval_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_interval_panel.gridwidth = 2;
		gbc_filtermenu_interval_panel.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_filtermenu_specific_dow_panel.weighty = 0.1;
		gbc_filtermenu_specific_dow_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_specific_dow_panel.fill = GridBagConstraints.HORIZONTAL;
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
		gbc_checkBox_Mon.gridx = 0;
		gbc_checkBox_Mon.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Mon, gbc_checkBox_Mon);

		GridBagConstraints gbc_checkBox_Tue = new GridBagConstraints();
		gbc_checkBox_Tue.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Tue.gridx = 1;
		gbc_checkBox_Tue.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Tue, gbc_checkBox_Tue);

		GridBagConstraints gbc_checkBox_Wed = new GridBagConstraints();
		gbc_checkBox_Wed.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Wed.gridx = 2;
		gbc_checkBox_Wed.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Wed, gbc_checkBox_Wed);

		GridBagConstraints gbc_checkBox_Thu_ = new GridBagConstraints();
		gbc_checkBox_Thu_.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Thu_.gridx = 0;
		gbc_checkBox_Thu_.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Thu, gbc_checkBox_Thu_);

		GridBagConstraints gbc_checkBox_Fri = new GridBagConstraints();
		gbc_checkBox_Fri.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Fri.gridx = 1;
		gbc_checkBox_Fri.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Fri, gbc_checkBox_Fri);

		GridBagConstraints gbc_checkBox_Sat = new GridBagConstraints();
		gbc_checkBox_Sat.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sat.gridx = 2;
		gbc_checkBox_Sat.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Sat, gbc_checkBox_Sat);

		GridBagConstraints gbc_checkBox_Sun = new GridBagConstraints();
		gbc_checkBox_Sun.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sun.gridx = 0;
		gbc_checkBox_Sun.gridy = 3;
		filtermenu_specific_dow_panel.add(this.checkBox_Sun, gbc_checkBox_Sun);
		
		JLabel filtermenu_specific_dow_label = new JLabel("Weekdays:");
		GridBagConstraints gbc_filtermenu_specific_dow_label = new GridBagConstraints();
		gbc_filtermenu_specific_dow_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_specific_dow_label.gridwidth = 3;
		gbc_filtermenu_specific_dow_label.gridx = 0;
		gbc_filtermenu_specific_dow_label.gridy = 0;
		filtermenu_specific_dow_panel.add(filtermenu_specific_dow_label, gbc_filtermenu_specific_dow_label);

		this.filtermenu_daytime_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_daytime_panel = new GridBagConstraints();
		gbc_filtermenu_daytime_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_daytime_panel.weighty = 0.1;
		gbc_filtermenu_daytime_panel.gridwidth = 2;
		gbc_filtermenu_daytime_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_daytime_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_daytime_panel.gridx = 0;
		gbc_filtermenu_daytime_panel.gridy = 4;
		
		filtermenu_panel.add(this.filtermenu_daytime_panel, gbc_filtermenu_daytime_panel);
		GridBagLayout gbl_filtermenu_daytime_panel = new GridBagLayout();
		gbl_filtermenu_daytime_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_daytime_panel.rowHeights = new int[] {0, 0, 0, 0};
		gbl_filtermenu_daytime_panel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_filtermenu_daytime_panel.rowWeights = new double[]{Double.MIN_VALUE, 0.0, 0.0, 0.0};
		this.filtermenu_daytime_panel.setLayout(gbl_filtermenu_daytime_panel);
		
		this.checkBox_daytime_latenight = new JCheckBox("Early Morning");
		this.checkBox_daytime_latenight.setToolTipText("2 AM to 6 AM");
		GridBagConstraints gbc_checkBox_daytime_latenight = new GridBagConstraints();
		gbc_checkBox_daytime_latenight.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_latenight.gridx = 1;
		gbc_checkBox_daytime_latenight.gridy = 3;
		filtermenu_daytime_panel.add(this.checkBox_daytime_latenight, gbc_checkBox_daytime_latenight);
		
		this.checkBox_daytime_midnight = new JCheckBox("Midnight");
		this.checkBox_daytime_midnight.setToolTipText("10 PM to 2 AM");
		GridBagConstraints gbc_checkBox_daytime_midnight = new GridBagConstraints();
		gbc_checkBox_daytime_midnight.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_midnight.gridx = 0;
		gbc_checkBox_daytime_midnight.gridy = 3;
		filtermenu_daytime_panel.add(this.checkBox_daytime_midnight, gbc_checkBox_daytime_midnight);
		
		this.checkBox_daytime_evening = new JCheckBox("Evening");
		this.checkBox_daytime_evening.setToolTipText("6 PM to 10 PM");
		GridBagConstraints gbc_checkBox_daytime_evening = new GridBagConstraints();
		gbc_checkBox_daytime_evening.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_evening.gridx = 1;
		gbc_checkBox_daytime_evening.gridy = 2;
		filtermenu_daytime_panel.add(this.checkBox_daytime_evening, gbc_checkBox_daytime_evening);
		
		this.checkBox_daytime_afternoon = new JCheckBox("Afternoon");
		this.checkBox_daytime_afternoon.setToolTipText("2:00 PM to 6 PM");
		GridBagConstraints gbc_checkBox_daytime_afternoon = new GridBagConstraints();
		gbc_checkBox_daytime_afternoon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_afternoon.gridx = 0;
		gbc_checkBox_daytime_afternoon.gridy = 2;
		filtermenu_daytime_panel.add(this.checkBox_daytime_afternoon, gbc_checkBox_daytime_afternoon);
		
		this.checkBox_daytime_noon = new JCheckBox("Noon");
		this.checkBox_daytime_noon.setToolTipText("10:00 AM to 2:00 PM");
		GridBagConstraints gbc_checkBox_daytime_noon = new GridBagConstraints();
		gbc_checkBox_daytime_noon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_noon.gridx = 1;
		gbc_checkBox_daytime_noon.gridy = 1;
		filtermenu_daytime_panel.add(this.checkBox_daytime_noon, gbc_checkBox_daytime_noon);
		
		this.checkBox_daytime_morning = new JCheckBox("Morning");
		this.checkBox_daytime_morning.setToolTipText("6:00 AM to 10:00 AM");
		GridBagConstraints gbc_checkBox_daytime_morning = new GridBagConstraints();
		gbc_checkBox_daytime_morning.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_morning.gridx = 0;
		gbc_checkBox_daytime_morning.gridy = 1;
		this.filtermenu_daytime_panel.add(this.checkBox_daytime_morning, gbc_checkBox_daytime_morning);
		
		this.filtermenu_specific_dow_daytime_label = new JLabel("Daytime:");
		GridBagConstraints gbc_filtermenu_specific_dow_daytime_label = new GridBagConstraints();
		gbc_filtermenu_specific_dow_daytime_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_specific_dow_daytime_label.gridwidth = 2;
		gbc_filtermenu_specific_dow_daytime_label.gridx = 0;
		gbc_filtermenu_specific_dow_daytime_label.gridy = 0;
		this.filtermenu_daytime_panel.add(this.filtermenu_specific_dow_daytime_label, gbc_filtermenu_specific_dow_daytime_label);
		
		JPanel filtermenu_buttons_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_buttons_panel = new GridBagConstraints();
		gbc_filtermenu_buttons_panel.weighty = 1.0;
		gbc_filtermenu_buttons_panel.anchor = GridBagConstraints.SOUTH;
		gbc_filtermenu_buttons_panel.gridwidth = 2;
		gbc_filtermenu_buttons_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_buttons_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_buttons_panel.gridx = 0;
		gbc_filtermenu_buttons_panel.gridy = 5;
		filtermenu_panel.add(filtermenu_buttons_panel, gbc_filtermenu_buttons_panel);
		filtermenu_buttons_panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		
		filtermenu_buttons_panel.add(filtermenu_buttons_applyButton);
		filtermenu_buttons_panel.add(filtermenu_buttons_defaultButton);
		
		((JLabel)this.filtermenu_comboBox_category.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		this.filtermenu_comboBox_category.addItem("All categories");
		this.filtermenu_comboBox_category.addItem("SFHA Requests");
		this.filtermenu_comboBox_category.addItem("Abandoned Vehicle");
		this.filtermenu_comboBox_category.addItem("Street and Sidewalk Cleaning");
		this.filtermenu_comboBox_category.addItem("Litter Receptacles");
		this.filtermenu_comboBox_category.addItem("Sign Repair");
		this.filtermenu_comboBox_category.addItem("Residential Building Request");
		this.filtermenu_comboBox_category.addItem("Graffiti Public Property");
		this.filtermenu_comboBox_category.addItem("Streetlights");
		this.filtermenu_comboBox_category.addItem("Damaged Property");
		this.filtermenu_comboBox_category.addItem("Street Defects");
		this.filtermenu_comboBox_category.addItem("Blocked Street or SideWalk");
		this.filtermenu_comboBox_category.addItem("Tree Maintenance");
		this.filtermenu_comboBox_category.addItem("MUNI Feedback");
		this.filtermenu_comboBox_category.addItem("Illegal Postings");
		this.filtermenu_comboBox_category.addItem("Color Curb");
		this.filtermenu_comboBox_category.addItem("General Requests");
		this.filtermenu_comboBox_category.addItem("Sewer Issues");
		this.filtermenu_comboBox_category.addItem("Rec and Park Requests");
		this.filtermenu_comboBox_category.addItem("Temporary Sign Request");
		this.filtermenu_comboBox_category.addItem("Graffiti Private Property");
		this.filtermenu_comboBox_category.addItem("311 External Request");
		this.filtermenu_comboBox_category.addItem("Sidewalk or Curb");
		this.filtermenu_comboBox_category.addItem("Catch Basin Maintenance");
		this.filtermenu_comboBox_category.addItem("Interdepartmental Request");
		this.filtermenu_comboBox_category.addItem("DPW Volunteer Programs");
		this.filtermenu_comboBox_category.addItem("Unpermitted Cab Complaint");
		this.filtermenu_comboBox_category.addItem("Construction Zone Permits");
		GridBagConstraints gbc_filtermenu_comboBox_category = new GridBagConstraints();
		gbc_filtermenu_comboBox_category.weighty = 0.1;
		gbc_filtermenu_comboBox_category.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_comboBox_category.gridwidth = 2;
		gbc_filtermenu_comboBox_category.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_comboBox_category.gridx = 0;
		gbc_filtermenu_comboBox_category.gridy = 0;
		filtermenu_panel.add(this.filtermenu_comboBox_category, gbc_filtermenu_comboBox_category);
		
		JPanel filtermenu_dates_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_dates_panel = new GridBagConstraints();
		gbc_filtermenu_dates_panel.weighty = 0.1;
		gbc_filtermenu_dates_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_dates_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_dates_panel.gridwidth = 2;
		gbc_filtermenu_dates_panel.insets = new Insets(0, 3, 5, 3);
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
		gbc_reportList_panel.weightx = 0.1;
		gbc_reportList_panel.gridx = 0;
		gbc_reportList_panel.gridy = 0;
		getContentPane().add(reportList_panel, gbc_reportList_panel);
		
		this.reportListModel = new DefaultListModel<CaseReport>();
		GridBagLayout gbl_reportList_panel = new GridBagLayout();
		gbl_reportList_panel.columnWidths = new int[] {0};
		gbl_reportList_panel.rowHeights = new int[] {0, 0};
		gbl_reportList_panel.columnWeights = new double[]{0.0};
		gbl_reportList_panel.rowWeights = new double[]{0.0, 0.0};
		reportList_panel.setLayout(gbl_reportList_panel);
		this.reportList = new MyJList<CaseReport>(this.reportListModel);
		this.reportList.setToolTipText("");
		this.reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.reportList.setLayoutOrientation(JList.VERTICAL);
		this.reportList.setVisibleRowCount(-1);
		((DefaultListCellRenderer)this.reportList.getCellRenderer()).setHorizontalTextPosition(SwingConstants.LEFT);
		JScrollPane reportList_scrollPane = new JScrollPane(reportList);
		GridBagConstraints gbc_reportList_scrollPane = new GridBagConstraints();
		gbc_reportList_scrollPane.anchor = GridBagConstraints.WEST;
		gbc_reportList_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_reportList_scrollPane.weighty = 0.9;
		gbc_reportList_scrollPane.weightx = 1.0;
		gbc_reportList_scrollPane.gridx = 0;
		gbc_reportList_scrollPane.gridy = 0;
		this.reportList_panel.add(reportList_scrollPane, gbc_reportList_scrollPane);
		this.reportList_details_button = new JButton("Details");
		reportList_details_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_reportList_details_button = new GridBagConstraints();
		gbc_reportList_details_button.fill = GridBagConstraints.BOTH;
		gbc_reportList_details_button.weighty = 0.1;
		gbc_reportList_details_button.weightx = 1.0;
		gbc_reportList_details_button.gridx = 0;
		gbc_reportList_details_button.gridy = 1;
		reportList_panel.add(reportList_details_button, gbc_reportList_details_button);
		
		// =================== EVENTHANDLER: =================== 
		this.reportList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){
					Mainframe.this.controller.onNewCaseSelection();
				}
			}
		});
		
		this.reportList_details_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mainframe.this.controller.detailsButtonClicked();
			}
		});
		
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
	}
	
	/**
	 * 
	 * @return the mapController of the GeoMap
	 */
	public MapController getGeoMapController() {
		return geoMapController;
	}
}
