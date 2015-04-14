package mvc.view;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import java.awt.GridBagConstraints;

import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.MenuBar;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import java.awt.Font;

public class Mainframe extends JFrame {
	
	public Mainframe(){
		super();
		
		this.init();
	}
	
	private void init(){
		//Window settings:
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenWidth, screenHeight, frameWidth, frameHeight;
		screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frameWidth = (int) (screenWidth / 4);
		frameHeight = (int) (frameWidth);
		this.setSize(frameWidth, frameHeight);
		this.setLocation((int)(screenWidth / 2 - frameWidth / 2), (int)(screenHeight / 2 - frameHeight / 2 ));
		
		//Layout:
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0};
		gridBagLayout.columnWeights = new double[]{0.8, 0.2};
		gridBagLayout.rowWeights = new double[]{1.0};
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
		
		JPanel filtermenu_panel = new JPanel();
		filtermenu_panel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_filtermenu_panel = new GridBagConstraints();
		gbc_filtermenu_panel.insets = new Insets(0, 0, 5, 0);
		gbc_filtermenu_panel.fill = GridBagConstraints.BOTH;
		gbc_filtermenu_panel.gridx = 1;
		gbc_filtermenu_panel.gridy = 0;
		getContentPane().add(filtermenu_panel, gbc_filtermenu_panel);
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
	}
}
