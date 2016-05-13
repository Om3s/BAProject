package mvc.view;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.Dimension;
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
import javax.swing.ListSelectionModel;

import mvc.controller.MainframeController;
import mvc.controller.MapController;
import mvc.main.Main;
import mvc.model.CaseReport;
import mvc.model.GeoMapViewer;
import mvc.model.MyJList;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

import com.visutools.nav.bislider.BiSlider;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class Mainframe extends JFrame {
	public GeoMapViewer geoMap;
	private final JFileChooser fileChooser;
	private final JMenuItem fileMenu_item_load;
	private final JMenuItem fileMenu_item_exit;
//	public final ButtonGroup filtermenu_interval_RadioButtongroupInterval;
//	public final JRadioButton filtermenu_interval_radioButtonMonths;
//	public final JRadioButton filtermenu_interval_radioButtonWeeks;
//	public final JRadioButton filtermenu_interval_radioButtonDays;
//	public final JRadioButton filtermenu_interval_radioButtonHours;
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
	public JCheckBox checkBox_daytime_evening2;
	public JCheckBox checkBox_daytime_midnight;
	private JPanel filtermenu_daytime_panel;
	private JButton reportList_details_button;
	public CaseCountMatrix matrix_chart_panel;
	public JCheckBox reportList_panel_filter_checkBoxOpen;
	public JCheckBox reportList_panel_filter_checkBoxClosed;
	public JScrollPane reportList_scrollPane;
	private JLabel filtermenu_analysis_panel_label;
	private JLabel filtermenu_analysis_panel_slider_lowValue_label;
	public JCheckBox filtermenu_analysis_panel_chckbxHeatmap;
	public JSlider filtermenu_analysis_panel_threshold_slider;
	public JButton filtermenu_analysis_panel_analyze_button;
	public JTextField filtermenu_analysis_panel_lowPosThreshold_textfield;
	public JTextField filtermenu_analysis_panel_upperPosThreshold_textfield;
	public JTextField filtermenu_analysis_panel_intervallAmount_textfield;
	private JLabel filtermenu_analysis_panel_interactive_label;
	private JLabel filtermenu_analysis_panel_intervalAmount_label;
	private JLabel filtermenu_analysis_panel_resolution_label;
	public JTextField filtermenu_analysis_panel_resolution_textfield;
	private JLabel filtermenu_analysis_panel_slider_highValue_label;
	private JLabel filtermenu_analysis_panel_currentSliderValue_label;
	public TimeLineGraphic filtermenu_timeline_panel;
	public JLabel filtermenu_analysis_panel_progress_label;
	
	
	public Mainframe(GeoMapViewer map){
		super();
		this.geoMap = map;
		//radiobuttons:
//		this.filtermenu_interval_RadioButtongroupInterval = new ButtonGroup();
//		this.filtermenu_interval_radioButtonMonths = new JRadioButton("Months");
//		this.filtermenu_interval_radioButtonWeeks = new JRadioButton("Weeks");
//		this.filtermenu_interval_radioButtonDays = new JRadioButton("Days");
//		this.filtermenu_interval_radioButtonHours = new JRadioButton("Hours");
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
		this.matrix_chart_panel = new CaseCountMatrix(new int[7][4], this);
		//filtermenu_analysis:
		this.filtermenu_analysis_panel_chckbxHeatmap = new JCheckBox("Heatmap Mode");
		this.filtermenu_analysis_panel_threshold_slider = new JSlider(JSlider.HORIZONTAL);
		this.filtermenu_analysis_panel_intervallAmount_textfield = new JTextField();
		this.filtermenu_analysis_panel_lowPosThreshold_textfield = new JTextField();
		this.filtermenu_analysis_panel_slider_lowValue_label = new JLabel("min:");
		this.filtermenu_analysis_panel_upperPosThreshold_textfield = new JTextField();
		this.filtermenu_analysis_panel_analyze_button = new JButton("Analyze");
		this.filtermenu_analysis_panel_resolution_textfield = new JTextField();
		
		this.init();
		this.pack();
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
		gridBagLayout.columnWeights = new double[]{0.1,1,0.1};
		gridBagLayout.rowWeights = new double[]{1.0};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel analysis_panel = new JPanel();
		GridBagConstraints gbc_analysis_panel = new GridBagConstraints();
		gbc_analysis_panel.gridwidth = 1;
		gbc_analysis_panel.weightx = 1.0;
		gbc_analysis_panel.fill = GridBagConstraints.BOTH;
		gbc_analysis_panel.gridx = 1;
		gbc_analysis_panel.gridy = 0;
		getContentPane().add(analysis_panel, gbc_analysis_panel);
		GridBagLayout gbl_analysis_panel = new GridBagLayout();
		gbl_analysis_panel.columnWidths = new int[]{0};
		gbl_analysis_panel.rowHeights = new int[] {0, 0, 0};
		gbl_analysis_panel.columnWeights = new double[]{1.0};
		gbl_analysis_panel.rowWeights = new double[]{0.0, 0.0};
		analysis_panel.setLayout(gbl_analysis_panel);
		
		JPanel geomap_panel = new JPanel();
		GridBagConstraints gbc_geomap_panel = new GridBagConstraints();
		gbc_geomap_panel.weighty = 1.0;
		gbc_geomap_panel.insets = new Insets(0, 0, 0, 0);
		gbc_geomap_panel.fill = GridBagConstraints.BOTH;
		gbc_geomap_panel.gridx = 0;
		gbc_geomap_panel.gridy = 0;
		analysis_panel.add(geomap_panel, gbc_geomap_panel);
		geomap_panel.add(this.geoMap);
		GridLayout gl_geomap_panel = new GridLayout();
		geomap_panel.setLayout(gl_geomap_panel);
		
//		JPanel timeline_panel = new JPanel();
//		GridBagConstraints gbc_timeline_panel = new GridBagConstraints();
//		gbc_timeline_panel.fill = GridBagConstraints.BOTH;
//		gbc_timeline_panel.gridx = 0;
//		gbc_timeline_panel.gridy = 1;
//		analysis_panel.add(timeline_panel, gbc_timeline_panel);
//		GridBagLayout gbl_timeline_panel = new GridBagLayout();
//		gbl_timeline_panel.columnWidths = new int[] {30, 30, 30};
//		gbl_timeline_panel.rowHeights = new int[] {10, 30};
//		gbl_timeline_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
//		gbl_timeline_panel.rowWeights = new double[]{0.0, 0.0};
//		timeline_panel.setLayout(gbl_timeline_panel);
		
//		this.timeline_panel_toDate_label = new JLabel("toDate");
//		this.timeline_panel_toDate_label.setVerticalAlignment(SwingConstants.TOP);
//		GridBagConstraints gbc_timeline_panel_toDate_label = new GridBagConstraints();
//		gbc_timeline_panel_toDate_label.weighty = 0.01;
//		gbc_timeline_panel_toDate_label.weightx = 1.0;
//		gbc_timeline_panel_toDate_label.anchor = GridBagConstraints.NORTHEAST;
//		gbc_timeline_panel_toDate_label.insets = new Insets(0, 0, 5, 0);
//		gbc_timeline_panel_toDate_label.gridx = 2;
//		gbc_timeline_panel_toDate_label.gridy = 0;
//		timeline_panel.add(this.timeline_panel_toDate_label, gbc_timeline_panel_toDate_label);
		
//		this.timeLineBiSlider = new BiSlider();
//		this.timeLineBiSlider.setMinimumSize(new Dimension(350, 50));
//		this.timeLineBiSlider.setMinimumColor(Color.GRAY);
//		this.timeLineBiSlider.setMaximumColor(Color.LIGHT_GRAY);
//		GridBagConstraints gbc_timeLineBiSlider = new GridBagConstraints();
//		gbc_timeLineBiSlider.weighty = 1.0;
//		gbc_timeLineBiSlider.insets = new Insets(0, 13, 5, 0);
//		gbc_timeLineBiSlider.weightx = 1.0;
//		gbc_timeLineBiSlider.gridwidth = 3;
//		gbc_timeLineBiSlider.fill = GridBagConstraints.BOTH;
//		gbc_timeLineBiSlider.gridx = 0;
//		gbc_timeLineBiSlider.gridy = 1;
//		timeline_panel.add(this.timeLineBiSlider, gbc_timeLineBiSlider);
		
//		this.timeline_panel_fromDate_label = new JLabel("fromDate");
//		this.timeline_panel_fromDate_label.setVerticalAlignment(SwingConstants.TOP);
//		GridBagConstraints gbc_timeline_panel_fromDate_label = new GridBagConstraints();
//		gbc_timeline_panel_fromDate_label.insets = new Insets(0, 0, 5, 0);
//		gbc_timeline_panel_fromDate_label.weighty = 0.01;
//		gbc_timeline_panel_fromDate_label.weightx = 1.0;
//		gbc_timeline_panel_fromDate_label.anchor = GridBagConstraints.NORTHWEST;
//		gbc_timeline_panel_fromDate_label.gridx = 0;
//		gbc_timeline_panel_fromDate_label.gridy = 0;
//		timeline_panel.add(this.timeline_panel_fromDate_label, gbc_timeline_panel_fromDate_label);
		
//		this.timeLine_range_label = new JLabel("Range");
//		this.timeLine_range_label.setVerticalAlignment(SwingConstants.TOP);
//		GridBagConstraints gbc_timeLine_range_label = new GridBagConstraints();
//		gbc_timeLine_range_label.weighty = 0.01;
//		gbc_timeLine_range_label.weightx = 1.0;
//		gbc_timeLine_range_label.anchor = GridBagConstraints.NORTH;
//		gbc_timeLine_range_label.gridx = 1;
//		gbc_timeLine_range_label.gridy = 0;
//		timeline_panel.add(this.timeLine_range_label, gbc_timeLine_range_label);

		GridBagConstraints gbc_matrix_chart_panel = new GridBagConstraints();
		gbc_matrix_chart_panel.gridwidth = 1;
		gbc_matrix_chart_panel.weightx = 1.0;
		gbc_matrix_chart_panel.fill = GridBagConstraints.BOTH;
		gbc_matrix_chart_panel.gridx = 0;
		gbc_matrix_chart_panel.gridy = 1;
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
		gbl_filtermenu_panel.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_filtermenu_panel.columnWeights = new double[]{1.0, 1.0};
		gbl_filtermenu_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0};
		filtermenu_panel.setLayout(gbl_filtermenu_panel);
		
//		JPanel filtermenu_interval_panel = new JPanel();
//		GridBagConstraints gbc_filtermenu_interval_panel = new GridBagConstraints();
//		gbc_filtermenu_interval_panel.weighty = 0.1;
//		gbc_filtermenu_interval_panel.anchor = GridBagConstraints.NORTH;
//		gbc_filtermenu_interval_panel.insets = new Insets(0, 3, 5, 3);
//		gbc_filtermenu_interval_panel.gridwidth = 2;
//		gbc_filtermenu_interval_panel.fill = GridBagConstraints.HORIZONTAL;
//		gbc_filtermenu_interval_panel.gridx = 0;
//		gbc_filtermenu_interval_panel.gridy = 4;
//		filtermenu_panel.add(filtermenu_interval_panel, gbc_filtermenu_interval_panel);
//		GridBagLayout gbl_filtermenu_interval_panel = new GridBagLayout();
//		gbl_filtermenu_interval_panel.columnWidths = new int[] {0, 0};
//		gbl_filtermenu_interval_panel.rowHeights = new int[] {0, 0, 0};
//		gbl_filtermenu_interval_panel.columnWeights = new double[]{0.0, 0.0};
//		gbl_filtermenu_interval_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
//		filtermenu_interval_panel.setLayout(gbl_filtermenu_interval_panel);
//		
//		JLabel filtermenu_interval_label = new JLabel("Choose time interval:");
//		GridBagConstraints gbc_filtermenu_intervall_label = new GridBagConstraints();
//		gbc_filtermenu_intervall_label.gridwidth = 2;
//		gbc_filtermenu_intervall_label.insets = new Insets(0, 0, 5, 0);
//		gbc_filtermenu_intervall_label.gridx = 0;
//		gbc_filtermenu_intervall_label.gridy = 0;
//		filtermenu_interval_panel.add(filtermenu_interval_label, gbc_filtermenu_intervall_label);
//		
//		GridBagConstraints gbc_filtermenu_interval_radioButtonMonths = new GridBagConstraints();
//		gbc_filtermenu_interval_radioButtonMonths.insets = new Insets(0, 0, 5, 0);
//		gbc_filtermenu_interval_radioButtonMonths.anchor = GridBagConstraints.WEST;
//		gbc_filtermenu_interval_radioButtonMonths.gridx = 0;
//		gbc_filtermenu_interval_radioButtonMonths.gridy = 1;
//		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonMonths, gbc_filtermenu_interval_radioButtonMonths);
//		
//		GridBagConstraints gbc_filtermenu_interval_radioButtonWeeks = new GridBagConstraints();
//		gbc_filtermenu_interval_radioButtonWeeks.anchor = GridBagConstraints.WEST;
//		gbc_filtermenu_interval_radioButtonWeeks.insets = new Insets(0, 0, 5, 5);
//		gbc_filtermenu_interval_radioButtonWeeks.gridx = 1;
//		gbc_filtermenu_interval_radioButtonWeeks.gridy = 1;
//		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonWeeks, gbc_filtermenu_interval_radioButtonWeeks);
//		
//		GridBagConstraints gbc_filtermenu_interval_radioButtonDays = new GridBagConstraints();
//		gbc_filtermenu_interval_radioButtonDays.insets = new Insets(0, 0, 5, 5);
//		gbc_filtermenu_interval_radioButtonDays.anchor = GridBagConstraints.WEST;
//		gbc_filtermenu_interval_radioButtonDays.gridx = 0;
//		gbc_filtermenu_interval_radioButtonDays.gridy = 2;
//		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonDays, gbc_filtermenu_interval_radioButtonDays);
//		
//		GridBagConstraints gbc_filtermenu_interval_radioButtonHours = new GridBagConstraints();
//		gbc_filtermenu_interval_radioButtonHours.insets = new Insets(0, 0, 5, 5);
//		gbc_filtermenu_interval_radioButtonHours.anchor = GridBagConstraints.WEST;
//		gbc_filtermenu_interval_radioButtonHours.gridx = 1;
//		gbc_filtermenu_interval_radioButtonHours.gridy = 2;
//		filtermenu_interval_panel.add(this.filtermenu_interval_radioButtonHours, gbc_filtermenu_interval_radioButtonHours);
//		
//		this.filtermenu_interval_RadioButtongroupInterval.add(this.filtermenu_interval_radioButtonDays);
//		this.filtermenu_interval_RadioButtongroupInterval.add(this.filtermenu_interval_radioButtonWeeks);
//		this.filtermenu_interval_RadioButtongroupInterval.add(this.filtermenu_interval_radioButtonMonths);
//		this.filtermenu_interval_RadioButtongroupInterval.add(this.filtermenu_interval_radioButtonHours);
		
		JPanel filtermenu_specific_dow_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_specific_dow_panel = new GridBagConstraints();
		gbc_filtermenu_specific_dow_panel.weighty = 0.1;
		gbc_filtermenu_specific_dow_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_specific_dow_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_specific_dow_panel.gridwidth = 2;
		gbc_filtermenu_specific_dow_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_specific_dow_panel.gridx = 0;
		gbc_filtermenu_specific_dow_panel.gridy = 2;
		filtermenu_panel.add(filtermenu_specific_dow_panel, gbc_filtermenu_specific_dow_panel);
		GridBagLayout gbl_filtermenu_specific_dow_panel = new GridBagLayout();
		gbl_filtermenu_specific_dow_panel.columnWidths = new int[] {0, 0, 0};
		gbl_filtermenu_specific_dow_panel.rowHeights = new int[] {0, 0, 0, 0};
		gbl_filtermenu_specific_dow_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_filtermenu_specific_dow_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		filtermenu_specific_dow_panel.setLayout(gbl_filtermenu_specific_dow_panel);
				
		GridBagConstraints gbc_checkBox_Mon = new GridBagConstraints();
		gbc_checkBox_Mon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Mon.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Mon.gridx = 0;
		gbc_checkBox_Mon.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Mon, gbc_checkBox_Mon);

		GridBagConstraints gbc_checkBox_Tue = new GridBagConstraints();
		gbc_checkBox_Tue.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Tue.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Tue.gridx = 1;
		gbc_checkBox_Tue.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Tue, gbc_checkBox_Tue);

		GridBagConstraints gbc_checkBox_Wed = new GridBagConstraints();
		gbc_checkBox_Wed.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Wed.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Wed.gridx = 2;
		gbc_checkBox_Wed.gridy = 1;
		filtermenu_specific_dow_panel.add(this.checkBox_Wed, gbc_checkBox_Wed);

		GridBagConstraints gbc_checkBox_Thu = new GridBagConstraints();
		gbc_checkBox_Thu.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Thu.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Thu.gridx = 0;
		gbc_checkBox_Thu.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Thu, gbc_checkBox_Thu);

		GridBagConstraints gbc_checkBox_Fri = new GridBagConstraints();
		gbc_checkBox_Fri.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Fri.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Fri.gridx = 1;
		gbc_checkBox_Fri.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Fri, gbc_checkBox_Fri);

		GridBagConstraints gbc_checkBox_Sat = new GridBagConstraints();
		gbc_checkBox_Sat.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sat.fill = GridBagConstraints.BOTH;
		gbc_checkBox_Sat.gridx = 2;
		gbc_checkBox_Sat.gridy = 2;
		filtermenu_specific_dow_panel.add(this.checkBox_Sat, gbc_checkBox_Sat);

		GridBagConstraints gbc_checkBox_Sun = new GridBagConstraints();
		gbc_checkBox_Sun.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sun.fill = GridBagConstraints.BOTH;
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
		gbc_filtermenu_daytime_panel.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_daytime_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_daytime_panel.gridx = 0;
		gbc_filtermenu_daytime_panel.gridy = 3;
		
		filtermenu_panel.add(this.filtermenu_daytime_panel, gbc_filtermenu_daytime_panel);
		GridBagLayout gbl_filtermenu_daytime_panel = new GridBagLayout();
		gbl_filtermenu_daytime_panel.columnWidths = new int[] {0, 0};
		gbl_filtermenu_daytime_panel.rowHeights = new int[] {0, 0, 0};
		gbl_filtermenu_daytime_panel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_filtermenu_daytime_panel.rowWeights = new double[]{Double.MIN_VALUE, 0.0, 0.0, 0.0};
		this.filtermenu_daytime_panel.setLayout(gbl_filtermenu_daytime_panel);
		
		this.checkBox_daytime_midnight = new JCheckBox("00:00 - 06:00");
		GridBagConstraints gbc_checkBox_daytime_midnight = new GridBagConstraints();
		gbc_checkBox_daytime_midnight.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_midnight.gridx = 0;
		gbc_checkBox_daytime_midnight.gridy = 1;
		filtermenu_daytime_panel.add(this.checkBox_daytime_midnight, gbc_checkBox_daytime_midnight);

		this.checkBox_daytime_morning = new JCheckBox("06:00 - 10:00");
		GridBagConstraints gbc_checkBox_daytime_morning = new GridBagConstraints();
		gbc_checkBox_daytime_morning.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_morning.gridx = 1;
		gbc_checkBox_daytime_morning.gridy = 1;
		this.filtermenu_daytime_panel.add(this.checkBox_daytime_morning, gbc_checkBox_daytime_morning);

		this.checkBox_daytime_noon = new JCheckBox("10:00 - 14:00");
		GridBagConstraints gbc_checkBox_daytime_noon = new GridBagConstraints();
		gbc_checkBox_daytime_noon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_noon.gridx = 0;
		gbc_checkBox_daytime_noon.gridy = 2;
		this.filtermenu_daytime_panel.add(this.checkBox_daytime_noon, gbc_checkBox_daytime_noon);

		this.checkBox_daytime_afternoon = new JCheckBox("14:00 - 18:00");
		GridBagConstraints gbc_checkBox_daytime_afternoon = new GridBagConstraints();
		gbc_checkBox_daytime_afternoon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_afternoon.gridx = 1;
		gbc_checkBox_daytime_afternoon.gridy = 2;
		filtermenu_daytime_panel.add(this.checkBox_daytime_afternoon, gbc_checkBox_daytime_afternoon);
		
		this.checkBox_daytime_evening = new JCheckBox("18:00 - 22:00");
		GridBagConstraints gbc_checkBox_daytime_evening = new GridBagConstraints();
		gbc_checkBox_daytime_evening.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_evening.gridx = 0;
		gbc_checkBox_daytime_evening.gridy = 3;
		filtermenu_daytime_panel.add(this.checkBox_daytime_evening, gbc_checkBox_daytime_evening);

		this.checkBox_daytime_evening2 = new JCheckBox("22:00 - 24:00");
		GridBagConstraints gbc_checkBox_daytime_evening2 = new GridBagConstraints();
		gbc_checkBox_daytime_evening2.anchor = GridBagConstraints.WEST;
		gbc_checkBox_daytime_evening2.gridx = 1;
		gbc_checkBox_daytime_evening2.gridy = 3;
		filtermenu_daytime_panel.add(this.checkBox_daytime_evening2, gbc_checkBox_daytime_evening2);
			
		this.filtermenu_specific_dow_daytime_label = new JLabel("Daytime:");
		GridBagConstraints gbc_filtermenu_specific_dow_daytime_label = new GridBagConstraints();
		gbc_filtermenu_specific_dow_daytime_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_specific_dow_daytime_label.gridwidth = 2;
		gbc_filtermenu_specific_dow_daytime_label.gridx = 0;
		gbc_filtermenu_specific_dow_daytime_label.gridy = 0;
		this.filtermenu_daytime_panel.add(this.filtermenu_specific_dow_daytime_label, gbc_filtermenu_specific_dow_daytime_label);
		
		JPanel filtermenu_analyzis_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_analyzis_panel = new GridBagConstraints();
		gbc_filtermenu_analyzis_panel.weighty = 1.0;
		gbc_filtermenu_analyzis_panel.anchor = GridBagConstraints.SOUTH;
		gbc_filtermenu_analyzis_panel.gridwidth = 2;
		gbc_filtermenu_analyzis_panel.insets = new Insets(0, 3, 5, 3);
		gbc_filtermenu_analyzis_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_analyzis_panel.gridx = 0;
		gbc_filtermenu_analyzis_panel.gridy = 4;
		filtermenu_panel.add(filtermenu_analyzis_panel, gbc_filtermenu_analyzis_panel);
		GridBagLayout gbl_filtermenu_analyzis_panel = new GridBagLayout();
		gbl_filtermenu_analyzis_panel.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_filtermenu_analyzis_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_filtermenu_analyzis_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
		gbl_filtermenu_analyzis_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		filtermenu_analyzis_panel.setLayout(gbl_filtermenu_analyzis_panel);
		
		filtermenu_analysis_panel_label = new JLabel("analysis attributes:");
		GridBagConstraints gbc_filtermenu_analysis_panel_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_analysis_panel_label.gridwidth = 6;
		gbc_filtermenu_analysis_panel_label.gridx = 0;
		gbc_filtermenu_analysis_panel_label.gridy = 1;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_label, gbc_filtermenu_analysis_panel_label);
		
		
		GridBagConstraints gbc_filtermenu_analysis_panel_chckbxHeatmap = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_chckbxHeatmap.gridwidth = 6;
		gbc_filtermenu_analysis_panel_chckbxHeatmap.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_analysis_panel_chckbxHeatmap.gridx = 0;
		gbc_filtermenu_analysis_panel_chckbxHeatmap.gridy = 0;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_chckbxHeatmap, gbc_filtermenu_analysis_panel_chckbxHeatmap);
		
		this.filtermenu_analysis_panel_progress_label = new JLabel(" ");
		GridBagConstraints gbc_filtermenu_analysis_panel_progress_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_progress_label.gridwidth = 2;
		gbc_filtermenu_analysis_panel_progress_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_progress_label.gridx = 4;
		gbc_filtermenu_analysis_panel_progress_label.gridy = 3;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_progress_label, gbc_filtermenu_analysis_panel_progress_label);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_analyze_button = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_analyze_button.gridwidth = 2;
		gbc_filtermenu_analysis_panel_analyze_button.weightx = 0.01;
		gbc_filtermenu_analysis_panel_analyze_button.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_analysis_panel_analyze_button.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_analysis_panel_analyze_button.gridx = 4;
		gbc_filtermenu_analysis_panel_analyze_button.gridy = 4;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_analyze_button, gbc_filtermenu_analysis_panel_analyze_button);
		
		filtermenu_analysis_panel_interactive_label = new JLabel("realtime intensity adjustments:");
		GridBagConstraints gbc_filtermenu_analysis_panel_interactive_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_interactive_label.gridwidth = 6;
		gbc_filtermenu_analysis_panel_interactive_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_analysis_panel_interactive_label.gridx = 0;
		gbc_filtermenu_analysis_panel_interactive_label.gridy = 5;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_interactive_label, gbc_filtermenu_analysis_panel_interactive_label);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_upperPosThreshold_textfield = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_upperPosThreshold_textfield.weightx = 0.01;
		gbc_filtermenu_analysis_panel_upperPosThreshold_textfield.gridx = 5;
		gbc_filtermenu_analysis_panel_upperPosThreshold_textfield.gridy = 7;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_upperPosThreshold_textfield, gbc_filtermenu_analysis_panel_upperPosThreshold_textfield);
		filtermenu_analysis_panel_upperPosThreshold_textfield.setColumns(4);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_slider_lowValue_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_slider_lowValue_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_slider_lowValue_label.gridx = 0;
		gbc_filtermenu_analysis_panel_slider_lowValue_label.gridy = 6;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_slider_lowValue_label, gbc_filtermenu_analysis_panel_slider_lowValue_label);
		
		this.filtermenu_analysis_panel_threshold_slider.setPreferredSize(new Dimension(120,0));
		this.filtermenu_analysis_panel_threshold_slider.setMinimumSize(new Dimension(85,0));
		GridBagConstraints gbc_filtermenu_analysis_panel_upperThreshold_slider = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_upperThreshold_slider.gridwidth = 4;
		gbc_filtermenu_analysis_panel_upperThreshold_slider.insets = new Insets(0, 0, 0, 5);
		gbc_filtermenu_analysis_panel_upperThreshold_slider.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_analysis_panel_upperThreshold_slider.gridx = 1;
		gbc_filtermenu_analysis_panel_upperThreshold_slider.gridy = 7;
		filtermenu_analyzis_panel.add(this.filtermenu_analysis_panel_threshold_slider, gbc_filtermenu_analysis_panel_upperThreshold_slider);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_lowPosThreshold_textfield = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_lowPosThreshold_textfield.weightx = 0.01;
		gbc_filtermenu_analysis_panel_lowPosThreshold_textfield.insets = new Insets(0, 0, 0, 5);
		gbc_filtermenu_analysis_panel_lowPosThreshold_textfield.gridx = 0;
		gbc_filtermenu_analysis_panel_lowPosThreshold_textfield.gridy = 7;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_lowPosThreshold_textfield, gbc_filtermenu_analysis_panel_lowPosThreshold_textfield);
		filtermenu_analysis_panel_lowPosThreshold_textfield.setColumns(4);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_intervallAmount_textfield = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_intervallAmount_textfield.gridwidth = 2;
		gbc_filtermenu_analysis_panel_intervallAmount_textfield.weightx = 0.01;
		gbc_filtermenu_analysis_panel_intervallAmount_textfield.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_intervallAmount_textfield.gridx = 2;
		gbc_filtermenu_analysis_panel_intervallAmount_textfield.gridy = 4;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_intervallAmount_textfield, gbc_filtermenu_analysis_panel_intervallAmount_textfield);
		filtermenu_analysis_panel_intervallAmount_textfield.setColumns(4);
		
		filtermenu_analysis_panel_intervalAmount_label = new JLabel("#intervals:");
		GridBagConstraints gbc_filtermenu_analysis_panel_intervalAmount_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_intervalAmount_label.gridwidth = 2;
		gbc_filtermenu_analysis_panel_intervalAmount_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_intervalAmount_label.gridx = 2;
		gbc_filtermenu_analysis_panel_intervalAmount_label.gridy = 3;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_intervalAmount_label, gbc_filtermenu_analysis_panel_intervalAmount_label);
		
		filtermenu_analysis_panel_resolution_label = new JLabel("grid(n\u00B2):");
		GridBagConstraints gbc_filtermenu_analysis_panel_resolution_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_resolution_label.gridwidth = 2;
		gbc_filtermenu_analysis_panel_resolution_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_resolution_label.gridx = 0;
		gbc_filtermenu_analysis_panel_resolution_label.gridy = 3;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_resolution_label, gbc_filtermenu_analysis_panel_resolution_label);
		
		GridBagConstraints gbc_filtermenu_analysis_panel_resolution_textfield = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_resolution_textfield.gridwidth = 2;
		gbc_filtermenu_analysis_panel_resolution_textfield.weightx = 0.01;
		gbc_filtermenu_analysis_panel_resolution_textfield.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_resolution_textfield.gridx = 0;
		gbc_filtermenu_analysis_panel_resolution_textfield.gridy = 4;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_resolution_textfield, gbc_filtermenu_analysis_panel_resolution_textfield);
		filtermenu_analysis_panel_resolution_textfield.setColumns(4);
		
		filtermenu_analysis_panel_slider_highValue_label = new JLabel("max:");
		GridBagConstraints gbc_filtermenu_analysis_panel_slider_highValue_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_slider_highValue_label.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_analysis_panel_slider_highValue_label.gridx = 5;
		gbc_filtermenu_analysis_panel_slider_highValue_label.gridy = 6;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_slider_highValue_label, gbc_filtermenu_analysis_panel_slider_highValue_label);
		
		this.filtermenu_analysis_panel_currentSliderValue_label = new JLabel("threshold: 50 %");
		GridBagConstraints gbc_filtermenu_analysis_panel_currentSliderValue_label = new GridBagConstraints();
		gbc_filtermenu_analysis_panel_currentSliderValue_label.gridwidth = 4;
		gbc_filtermenu_analysis_panel_currentSliderValue_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_analysis_panel_currentSliderValue_label.gridx = 1;
		gbc_filtermenu_analysis_panel_currentSliderValue_label.gridy = 6;
		filtermenu_analyzis_panel.add(filtermenu_analysis_panel_currentSliderValue_label, gbc_filtermenu_analysis_panel_currentSliderValue_label);
		
		this.filtermenu_timeline_panel = new TimeLineGraphic();
		GridBagConstraints gbc_filtermenu_timeline_panel = new GridBagConstraints();
		gbc_filtermenu_timeline_panel.gridwidth = 6;
		gbc_filtermenu_timeline_panel.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_timeline_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_timeline_panel.gridx = 0;
		gbc_filtermenu_timeline_panel.gridy = 2;
		filtermenu_analyzis_panel.add(filtermenu_timeline_panel, gbc_filtermenu_timeline_panel);
		this.filtermenu_timeline_panel.setPreferredSize(new Dimension(300, 100));
		
		JPanel filtermenu_buttons_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_buttons_panel = new GridBagConstraints();
		gbc_filtermenu_buttons_panel.weighty = 1.0;
		gbc_filtermenu_buttons_panel.anchor = GridBagConstraints.SOUTH;
		gbc_filtermenu_buttons_panel.gridwidth = 2;
		gbc_filtermenu_buttons_panel.insets = new Insets(0, 3, 0, 3);
		gbc_filtermenu_buttons_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_filtermenu_buttons_panel.gridx = 0;
		gbc_filtermenu_buttons_panel.gridy = 5;
		filtermenu_panel.add(filtermenu_buttons_panel, gbc_filtermenu_buttons_panel);
		filtermenu_buttons_panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		
		filtermenu_buttons_panel.add(filtermenu_buttons_applyButton);
		filtermenu_buttons_panel.add(filtermenu_buttons_defaultButton);
		
		((JLabel)this.filtermenu_comboBox_category.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		this.filtermenu_comboBox_category.addItem("All categories");
		if(Main.isChicago){
			this.filtermenu_comboBox_category.addItem("ASSAULT");
			this.filtermenu_comboBox_category.addItem("BATTERY");
			this.filtermenu_comboBox_category.addItem("THEFT");
			this.filtermenu_comboBox_category.addItem("NARCOTICS");
			this.filtermenu_comboBox_category.addItem("ROBBERY");
			this.filtermenu_comboBox_category.addItem("HOMICIDE");
			this.filtermenu_comboBox_category.addItem("PROSTITUTION");
			this.filtermenu_comboBox_category.addItem("CRIMINAL DAMAGE");
			this.filtermenu_comboBox_category.addItem("KIDNAPPING");
			this.filtermenu_comboBox_category.addItem("OTHER OFFENSE");
			this.filtermenu_comboBox_category.addItem("INTERFERENCE WITH PUBLIC OFFICER");
			this.filtermenu_comboBox_category.addItem("MOTOR VEHICLE THEFT");
			this.filtermenu_comboBox_category.addItem("WEAPONS VIOLATION");
			this.filtermenu_comboBox_category.addItem("CRIMINAL TRESPASS");
			this.filtermenu_comboBox_category.addItem("BURGLARY");
			this.filtermenu_comboBox_category.addItem("DECEPTIVE PRACTICE");
			this.filtermenu_comboBox_category.addItem("SEX OFFENSE");
			this.filtermenu_comboBox_category.addItem("OFFENSE INVOLVING CHILDREN");
			this.filtermenu_comboBox_category.addItem("CRIM SEXUAL ASSAULT");
			this.filtermenu_comboBox_category.addItem("PUBLIC PEACE VIOLATION");
			this.filtermenu_comboBox_category.addItem("ARSON");
			this.filtermenu_comboBox_category.addItem("LIQUOR LAW VIOLATION");
			this.filtermenu_comboBox_category.addItem("GAMBLING");
			this.filtermenu_comboBox_category.addItem("STALKING");
			this.filtermenu_comboBox_category.addItem("OBSCENITY");
			this.filtermenu_comboBox_category.addItem("INTIMIDATION");
			this.filtermenu_comboBox_category.addItem("NON-CRIMINAL");
			this.filtermenu_comboBox_category.addItem("CONCEALED CARRY LICENSE VIOLATION");
			this.filtermenu_comboBox_category.addItem("HUMAN TRAFFICKING");
			this.filtermenu_comboBox_category.addItem("PUBLIC INDECENCY");
			this.filtermenu_comboBox_category.addItem("NON - CRIMINAL");
			this.filtermenu_comboBox_category.addItem("OTHER NARCOTIC VIOLATION");
			this.filtermenu_comboBox_category.addItem("NON-CRIMINAL (SUBJECT SPECIFIED)");
		} else {
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
		}
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
				
		JLabel filtermenu_dates_label = new JLabel("Choose interval time:");
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
		GridBagLayout gbl_reportList_panel = new GridBagLayout();
		gbl_reportList_panel.columnWidths = new int[] {0, 0};
		gbl_reportList_panel.rowHeights = new int[] {0, 0, 0};
		gbl_reportList_panel.columnWeights = new double[]{1.0, 1.0};
		gbl_reportList_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
		reportList_panel.setLayout(gbl_reportList_panel);
		this.reportListModel = new DefaultListModel<CaseReport>();
		this.reportList = new MyJList<CaseReport>(this.reportListModel);
		this.reportList.setToolTipText("");
		this.reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.reportList.setLayoutOrientation(JList.VERTICAL);
		this.reportList.setVisibleRowCount(-1);
		((DefaultListCellRenderer)this.reportList.getCellRenderer()).setHorizontalTextPosition(SwingConstants.LEFT);
				
		this.reportList_panel_filter_checkBoxOpen = new JCheckBox("Open");
		GridBagConstraints gbc_reportList_panel_filter_radioButtonOpen = new GridBagConstraints();
		gbc_reportList_panel_filter_radioButtonOpen.anchor = GridBagConstraints.CENTER;
		gbc_reportList_panel_filter_radioButtonOpen.fill = GridBagConstraints.BOTH;
		gbc_reportList_panel_filter_radioButtonOpen.gridx = 0;
		gbc_reportList_panel_filter_radioButtonOpen.gridy = 0;
		this.reportList_panel.add(this.reportList_panel_filter_checkBoxOpen ,gbc_reportList_panel_filter_radioButtonOpen);
		
		this.reportList_panel_filter_checkBoxClosed = new JCheckBox("Closed");
		GridBagConstraints gbc_reportList_panel_filter_radioButtonClosed = new GridBagConstraints();
		gbc_reportList_panel_filter_radioButtonClosed.anchor = GridBagConstraints.CENTER;
		gbc_reportList_panel_filter_radioButtonClosed.fill = GridBagConstraints.BOTH;
		gbc_reportList_panel_filter_radioButtonClosed.gridx = 1;
		gbc_reportList_panel_filter_radioButtonClosed.gridy = 0;
		this.reportList_panel.add(this.reportList_panel_filter_checkBoxClosed ,gbc_reportList_panel_filter_radioButtonClosed);
		
		reportList_scrollPane = new JScrollPane(reportList);
		reportList_scrollPane.setMinimumSize(new Dimension(210,0));
		reportList_scrollPane.setMaximumSize(new Dimension(210,Main.screenHeight));
		reportList_scrollPane.setPreferredSize(new Dimension(210,500));
		GridBagConstraints gbc_reportList_scrollPane = new GridBagConstraints();
		gbc_reportList_scrollPane.anchor = GridBagConstraints.WEST;
		gbc_reportList_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_reportList_scrollPane.weighty = 0.9;
		gbc_reportList_scrollPane.weightx = 1.0;
		gbc_reportList_scrollPane.gridwidth = 2;
		gbc_reportList_scrollPane.gridx = 0;
		gbc_reportList_scrollPane.gridy = 1;
		this.reportList_panel.add(reportList_scrollPane, gbc_reportList_scrollPane);
		this.reportList_details_button = new JButton("Details");
		reportList_details_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_reportList_details_button = new GridBagConstraints();
		gbc_reportList_details_button.fill = GridBagConstraints.BOTH;
		gbc_reportList_details_button.weighty = 0.1;
		gbc_reportList_details_button.weightx = 1.0;
		gbc_reportList_details_button.gridwidth = 2;
		gbc_reportList_details_button.gridx = 0;
		gbc_reportList_details_button.gridy = 2;
		reportList_panel.add(reportList_details_button, gbc_reportList_details_button);
		
		// =================== EVENTHANDLER: =================== 
		this.reportList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){
					Main.mainframeController.onNewCaseSelection();
				}
			}
		});
		
		this.reportList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2){
					Main.mainframeController.detailsButtonClicked();
				}
			}
		});
		
		this.reportList_details_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.mainframeController.detailsButtonClicked();
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
//		this.timeLineBiSlider.addMouseListener(new MouseListener() {
//			
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				BiSlider src = (BiSlider) e.getSource();
//				Mainframe.this.controller.timeLineChanged(src.getMinimumColoredValue(), src.getMaximumColoredValue());
//			}
//		});
		
//		this.timeLineBiSlider.addMouseMotionListener(new MouseMotionListener() {
//			
//			@Override
//			public void mouseMoved(MouseEvent e) {
//
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				BiSlider src = (BiSlider) e.getSource();
//				Mainframe.this.controller.timeLineChanged(src.getMinimumColoredValue(), src.getMaximumColoredValue());
//			}
//		});
		
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
				Main.mainframeController.loadData(Mainframe.this.fileChooser.getSelectedFile().getPath());
			}
		});
				
		this.filtermenu_dates_leftCalendarButton.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					Main.mainframeController.fromDateAction((Date)evt.getNewValue());
				} 
			}
		});
		
		this.filtermenu_dates_rightCalendarButton.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date) {
					Main.mainframeController.toDateAction((Date)evt.getNewValue());
				}
			}
		});
		
		this.filtermenu_buttons_applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Apply button.");
				Main.mainframeController.applySettings();
			}
		});
		
		this.filtermenu_buttons_defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Default button.");
				Main.mainframeController.defaultSettings();
			}
		});
		
		this.reportList_panel_filter_checkBoxOpen.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(Main.mainframeController != null){
					Main.mainframeController.showOpenedCases(Mainframe.this.reportList_panel_filter_checkBoxOpen.isSelected());
				}
			}
		});;
		
		this.reportList_panel_filter_checkBoxClosed.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(Main.mainframeController != null){
					Main.mainframeController.showClosedCases(Mainframe.this.reportList_panel_filter_checkBoxClosed.isSelected());
				}
			}
		});;
		
		this.filtermenu_analysis_panel_analyze_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.mainframeController.analyzeButtonIsPressed();
			}
		});
		
		this.filtermenu_analysis_panel_chckbxHeatmap.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Main.mainframeController.checkBoxHeatmapChanged();
			}
		});
		
		this.filtermenu_analysis_panel_threshold_slider.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				Main.mainframeController.analyzeHeatMapSliderChanged();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.filtermenu_analysis_panel_threshold_slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider src = (JSlider)e.getSource();
				String str = String.valueOf(src.getValue());
				str += " %";
				if(str.length() == 1){
					str += "  ";
				} else if(str.length() == 2){
					str += " ";
				}
				Mainframe.this.filtermenu_analysis_panel_currentSliderValue_label.setText("threshold: "+str);
			}
		});
		
		this.filtermenu_analysis_panel_upperPosThreshold_textfield.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Mainframe.this.filtermenu_analysis_panel_threshold_slider.requestFocus();
			}
		});
		
		this.filtermenu_analysis_panel_upperPosThreshold_textfield.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO
				Main.mainframeController.analyzeUpperHeatMapValueChanged(Integer.valueOf(Mainframe.this.filtermenu_analysis_panel_upperPosThreshold_textfield.getText()));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		
		this.filtermenu_analysis_panel_lowPosThreshold_textfield.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Mainframe.this.filtermenu_analysis_panel_threshold_slider.requestFocus();
			}
		});
		
		this.filtermenu_analysis_panel_lowPosThreshold_textfield.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO
				Main.mainframeController.analyzeLowerHeatMapValueChanged(Integer.valueOf(Mainframe.this.filtermenu_analysis_panel_upperPosThreshold_textfield.getText()));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}
}
