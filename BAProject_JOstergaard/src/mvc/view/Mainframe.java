package mvc.view;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mainframe extends JFrame {
	private JMapViewer geoMap;
	
	public Mainframe(JMapViewer map){
		super();
		this.geoMap = map;
		this.init();
	}
	
	private void init(){
		// =================== WINDOW SETTINGS: =================== 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenWidth, screenHeight, frameWidth, frameHeight;
		screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frameWidth = (int) (screenWidth / 3);
		frameHeight = (int) (frameWidth * 0.75);
		this.setSize(frameWidth, frameHeight);
		this.setLocation((int)((screenWidth / 2) - (frameWidth / 2)), (int)((screenHeight / 2) - (frameHeight / 2 )));
		this.setMinimumSize(new Dimension(640, 480));
		
		
		
		// =================== GUI LAYOUT: =================== 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.8, 0.2};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel analysis_panel = new JPanel();
		GridBagConstraints gbc_analysis_panel = new GridBagConstraints();
		gbc_analysis_panel.insets = new Insets(0, 0, 5, 5);
		gbc_analysis_panel.fill = GridBagConstraints.BOTH;
		gbc_analysis_panel.gridx = 0;
		gbc_analysis_panel.gridy = 0;
		getContentPane().add(analysis_panel, gbc_analysis_panel);
		GridBagLayout gbl_analysis_panel = new GridBagLayout();
		gbl_analysis_panel.columnWidths = new int[]{0};
		gbl_analysis_panel.rowHeights = new int[]{0, 0, 0};
		gbl_analysis_panel.columnWeights = new double[]{1.0};
		gbl_analysis_panel.rowWeights = new double[]{0.6, 0.35, 0.05};
		analysis_panel.setLayout(gbl_analysis_panel);
		
		JPanel geomap_panel = new JPanel();
		geomap_panel.setBackground(Color.GREEN);
		geomap_panel.setForeground(Color.BLACK);
		geomap_panel.setToolTipText("GeoMap for Hotspot visualization");
		GridBagConstraints gbc_geomap_panel = new GridBagConstraints();
		gbc_geomap_panel.insets = new Insets(0, 0, 5, 0);
		gbc_geomap_panel.fill = GridBagConstraints.BOTH;
		gbc_geomap_panel.gridx = 0;
		gbc_geomap_panel.gridy = 0;
		analysis_panel.add(geomap_panel, gbc_geomap_panel);
		geomap_panel.add(this.geoMap);
		GridLayout gl_geomap_panel = new GridLayout();
		geomap_panel.setLayout(gl_geomap_panel);
		
		JPanel trend_panel = new JPanel();
		trend_panel.setBackground(Color.CYAN);
		GridBagConstraints gbc_trend_panel = new GridBagConstraints();
		gbc_trend_panel.insets = new Insets(0, 0, 5, 0);
		gbc_trend_panel.fill = GridBagConstraints.BOTH;
		gbc_trend_panel.gridx = 0;
		gbc_trend_panel.gridy = 1;
		analysis_panel.add(trend_panel, gbc_trend_panel);
		GridBagLayout gbl_trend_panel = new GridBagLayout();
		gbl_trend_panel.columnWidths = new int[]{0};
		gbl_trend_panel.rowHeights = new int[]{0};
		gbl_trend_panel.columnWeights = new double[]{1.0};
		gbl_trend_panel.rowWeights = new double[]{1.0};
		trend_panel.setLayout(gbl_trend_panel);
		
		//Placeholder Textfield for scrollpane
		JTextArea testTextfield = new JTextArea();
		testTextfield.setForeground(new Color(165, 42, 42));
		testTextfield.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		testTextfield.setBackground(new Color(220, 220, 220));
		testTextfield.setText("Test_Textfield, a placeholder for the scrollpane.");
		
		JScrollPane trend_panel_scrollpane = new JScrollPane(testTextfield);
		trend_panel_scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTH;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		trend_panel.add(trend_panel_scrollpane, gbc_scrollPane);
		
		JPanel timeline_panel = new JPanel();
		timeline_panel.setBackground(Color.BLUE);
		GridBagConstraints gbc_timeline_panel = new GridBagConstraints();
		gbc_timeline_panel.fill = GridBagConstraints.BOTH;
		gbc_timeline_panel.gridx = 0;
		gbc_timeline_panel.gridy = 2;
		analysis_panel.add(timeline_panel, gbc_timeline_panel);
		timeline_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		timeline_panel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollBar scrollBar = new JScrollBar();
		panel.add(scrollBar);
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		
		JLabel timeline_panel_fromDate_label = new JLabel("fromDate");
		panel.add(timeline_panel_fromDate_label, BorderLayout.WEST);
		
		JLabel timeline_panel_toDate_label = new JLabel("toDate");
		panel.add(timeline_panel_toDate_label, BorderLayout.EAST);
		
		JPanel filtermenu_panel = new JPanel();
		filtermenu_panel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_filtermenu_panel = new GridBagConstraints();
		gbc_filtermenu_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_panel.gridx = 1;
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
		gbc_filtermenu_interval_panel.gridy = 1;
		filtermenu_panel.add(filtermenu_interval_panel, gbc_filtermenu_interval_panel);
		GridBagLayout gbl_filtermenu_interval_panel = new GridBagLayout();
		gbl_filtermenu_interval_panel.columnWidths = new int[] {0, 0, 0};
		gbl_filtermenu_interval_panel.rowHeights = new int[] {0, 0};
		gbl_filtermenu_interval_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_filtermenu_interval_panel.rowWeights = new double[]{0.0, 0.0};
		filtermenu_interval_panel.setLayout(gbl_filtermenu_interval_panel);
		
		JLabel filtermenu_intervall_label = new JLabel("Choose time interval:");
		GridBagConstraints gbc_filtermenu_intervall_label = new GridBagConstraints();
		gbc_filtermenu_intervall_label.gridwidth = 3;
		gbc_filtermenu_intervall_label.insets = new Insets(0, 0, 5, 5);
		gbc_filtermenu_intervall_label.gridx = 0;
		gbc_filtermenu_intervall_label.gridy = 0;
		filtermenu_interval_panel.add(filtermenu_intervall_label, gbc_filtermenu_intervall_label);
		
		JRadioButton filtermenu_interval_radioButtonMonthly = new JRadioButton("Monthly");
		GridBagConstraints gbc_filtermenu_interval_radioButtonMonthly = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonMonthly.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonMonthly.insets = new Insets(0, 0, 0, 5);
		gbc_filtermenu_interval_radioButtonMonthly.gridx = 2;
		gbc_filtermenu_interval_radioButtonMonthly.gridy = 1;
		filtermenu_interval_panel.add(filtermenu_interval_radioButtonMonthly, gbc_filtermenu_interval_radioButtonMonthly);
		
		JRadioButton filtermenu_interval_radioButtonWeekly = new JRadioButton("Weekly");
		GridBagConstraints gbc_filtermenu_interval_radioButtonWeekly = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonWeekly.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonWeekly.insets = new Insets(0, 0, 0, 5);
		gbc_filtermenu_interval_radioButtonWeekly.gridx = 1;
		gbc_filtermenu_interval_radioButtonWeekly.gridy = 1;
		filtermenu_interval_panel.add(filtermenu_interval_radioButtonWeekly, gbc_filtermenu_interval_radioButtonWeekly);
		
		JRadioButton filtermenu_interval_radioButtonDaily = new JRadioButton("Daily");
		GridBagConstraints gbc_filtermenu_interval_radioButtonDaily = new GridBagConstraints();
		gbc_filtermenu_interval_radioButtonDaily.anchor = GridBagConstraints.WEST;
		gbc_filtermenu_interval_radioButtonDaily.gridx = 0;
		gbc_filtermenu_interval_radioButtonDaily.gridy = 1;
		filtermenu_interval_radioButtonDaily.setSelected(true);;
		filtermenu_interval_panel.add(filtermenu_interval_radioButtonDaily, gbc_filtermenu_interval_radioButtonDaily);
		
		ButtonGroup filtermenu_interval_buttongroup = new ButtonGroup();
		filtermenu_interval_buttongroup.add(filtermenu_interval_radioButtonDaily);
		filtermenu_interval_buttongroup.add(filtermenu_interval_radioButtonWeekly);
		filtermenu_interval_buttongroup.add(filtermenu_interval_radioButtonMonthly);
		
		JPanel filtermenu_specific_dow_panel = new JPanel();
		GridBagConstraints gbc_filtermenu_specific_dow_panel = new GridBagConstraints();
		gbc_filtermenu_specific_dow_panel.anchor = GridBagConstraints.NORTH;
		gbc_filtermenu_specific_dow_panel.fill = GridBagConstraints.BOTH;
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
		
		JCheckBox checkBox_Mon = new JCheckBox("Mon");
		checkBox_Mon.setSelected(true);
		GridBagConstraints gbc_checkBox_Mon = new GridBagConstraints();
		gbc_checkBox_Mon.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Mon.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Mon.gridx = 0;
		gbc_checkBox_Mon.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Mon, gbc_checkBox_Mon);
		
		JCheckBox checkBox_Tue = new JCheckBox("Tue");
		checkBox_Tue.setSelected(true);
		GridBagConstraints gbc_checkBox_Tue = new GridBagConstraints();
		gbc_checkBox_Tue.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Tue.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Tue.gridx = 1;
		gbc_checkBox_Tue.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Tue, gbc_checkBox_Tue);
		
		JCheckBox checkBox_Wed = new JCheckBox("Wed");
		checkBox_Wed.setSelected(true);
		GridBagConstraints gbc_checkBox_Wed = new GridBagConstraints();
		gbc_checkBox_Wed.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Wed.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_Wed.gridx = 2;
		gbc_checkBox_Wed.gridy = 1;
		filtermenu_specific_dow_panel.add(checkBox_Wed, gbc_checkBox_Wed);
		
		JCheckBox checkBox_Thu_ = new JCheckBox("Thu");
		checkBox_Thu_.setSelected(true);
		GridBagConstraints gbc_checkBox_Thu_ = new GridBagConstraints();
		gbc_checkBox_Thu_.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Thu_.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Thu_.gridx = 0;
		gbc_checkBox_Thu_.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Thu_, gbc_checkBox_Thu_);
		
		JCheckBox checkBox_Fri = new JCheckBox("Fri");
		checkBox_Fri.setSelected(true);
		GridBagConstraints gbc_checkBox_Fri = new GridBagConstraints();
		gbc_checkBox_Fri.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Fri.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_Fri.gridx = 1;
		gbc_checkBox_Fri.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Fri, gbc_checkBox_Fri);
		
		JCheckBox checkBox_Sat = new JCheckBox("Sat");
		checkBox_Sat.setSelected(true);
		GridBagConstraints gbc_checkBox_Sat = new GridBagConstraints();
		gbc_checkBox_Sat.anchor = GridBagConstraints.WEST;
		gbc_checkBox_Sat.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_Sat.gridx = 2;
		gbc_checkBox_Sat.gridy = 2;
		filtermenu_specific_dow_panel.add(checkBox_Sat, gbc_checkBox_Sat);
		
		JCheckBox checkBox_Sun = new JCheckBox("Sun");
		checkBox_Sun.setSelected(true);
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
		
		JButton filtermenu_buttons_applyButton = new JButton("Apply");
		filtermenu_buttons_panel.add(filtermenu_buttons_applyButton);
		
		JButton filtermenu_buttons_defaultButton = new JButton("Default");
		filtermenu_buttons_panel.add(filtermenu_buttons_defaultButton);
		
		JComboBox filtermenu_comboBox_category = new JComboBox();
		((JLabel)filtermenu_comboBox_category.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		filtermenu_comboBox_category.addItem("- All Categories -");
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
		gbc_filtermenu_dates_panel.gridy = 3;
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
		
				JCalendarButton filtermenu_dates_leftCalendarButton = new JCalendarButton();
				filtermenu_dates_leftCalendarButton.setText("From");
				GridBagConstraints gbc_filtermenu_dates_leftCalendarButton = new GridBagConstraints();
				gbc_filtermenu_dates_leftCalendarButton.fill = GridBagConstraints.BOTH;
				gbc_filtermenu_dates_leftCalendarButton.insets = new Insets(0, 3, 3, 5);
				gbc_filtermenu_dates_leftCalendarButton.gridx = 0;
				gbc_filtermenu_dates_leftCalendarButton.gridy = 1;
				filtermenu_dates_panel.add(filtermenu_dates_leftCalendarButton, gbc_filtermenu_dates_leftCalendarButton);
		
				JCalendarButton filtermenu_dates_rightCalendarButton = new JCalendarButton();
				filtermenu_dates_rightCalendarButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
				filtermenu_dates_rightCalendarButton.setText("To");
				GridBagConstraints gbc_filtermenu_dates_rightCalendarButton = new GridBagConstraints();
				gbc_filtermenu_dates_rightCalendarButton.insets = new Insets(0, 3, 3, 3);
				gbc_filtermenu_dates_rightCalendarButton.fill = GridBagConstraints.BOTH;
				gbc_filtermenu_dates_rightCalendarButton.gridx = 1;
				gbc_filtermenu_dates_rightCalendarButton.gridy = 1;
				filtermenu_dates_panel.add(filtermenu_dates_rightCalendarButton, gbc_filtermenu_dates_rightCalendarButton);
		
		JMenuBar menuBar = new JMenuBar();
		GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.insets = new Insets(0, 0, 0, 5);
		gbc_menuBar.gridx = 1;
		gbc_menuBar.gridy = 0;
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem fileMenu_item_load = new JMenuItem("Load");
		fileMenu.add(fileMenu_item_load);
		
		JMenuItem fileMenu_item_exit = new JMenuItem("Exit");
		fileMenu.add(fileMenu_item_exit);
		
		this.setJMenuBar(menuBar);
		
		
		
		// =================== EVENTHANDLER: =================== 
		fileMenu_item_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenu_item_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_comboBox_category.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_interval_radioButtonDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_interval_radioButtonWeekly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_interval_radioButtonMonthly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_dates_leftCalendarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_dates_rightCalendarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		filtermenu_buttons_applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Options applied.");
			}
		});
		
		filtermenu_buttons_defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("default setup.");
				
				//loadDefaul
			}
		});
	}
}
