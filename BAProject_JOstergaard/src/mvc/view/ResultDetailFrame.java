package mvc.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import mvc.controller.MainframeController;
import mvc.controller.PictureWorker;
import mvc.model.CaseReport;

public class ResultDetailFrame extends JFrame {
	private CaseReport cR;
	private MainframeController mainFrameController;
	private JTextArea caseId_textArea, category_textArea, statusNotes_textArea, dateOpened_textArea, dateClosed_textArea, position_textArea, neighbourhood_textArea;
	private JLabel caseId_label, category_label, status_label, statusNotes_label, dateOpened_label, dateClosed_label, dateClosed_graphicText_label, position_label, neighbourhood_label;
	private JPanel detailFrame_main_panel;
	private JLabel neighbourhood_graphic_label, image_label, status_graphic_label;
	private SimpleWeekDayGraphic dateOpened_graphic_label, dateClosed_graphic_label;
	private JButton center_to_point_button;
	private Dimension dateGraphicSize;
	
	public ResultDetailFrame(CaseReport cR, MainframeController mainFrameController) {
		this.cR = cR;
		this.mainFrameController = mainFrameController;
		this.dateGraphicSize = new Dimension(210,30);
		this.initGUI();
		this.initGUILogic();
	}

	private void initGUILogic() {
		this.statusNotes_textArea.setLineWrap(true);
		this.statusNotes_textArea.setWrapStyleWord(true);
		this.statusNotes_textArea.setMaximumSize(new Dimension(100,500));
		this.createDetailInformation();
		this.repaint();
		this.pack();
		this.setResizable(false);
		this.setLocation(this.getWidth()/4, this.getHeight()/4);
		this.setVisible(true);
	}
	
	/**
	 * this method processes the information of the CaseReport and fills the fields
	 * of the frame with the right format.
	 */
	private void createDetailInformation(){
		String[] tokens = Pattern.compile(Pattern.quote("%$sepa&%$")).split(cR.toString2().substring(11, cR.toString2().length()-1));
		int i=0;
		for(String s : tokens){
			System.out.println("["+(i++)+"]"+s);
		}
		this.caseId_textArea.setText(tokens[0].substring(8, tokens[0].length()));
		this.category_textArea.setText(tokens[1].substring(10, tokens[1].length()));
		SimpleDateFormat dateOutputFormat = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm");
		this.dateOpened_textArea.setText(dateOutputFormat.format(this.cR.getDateOpened()));
		this.statusNotes_textArea.setText(tokens[5].substring(13, tokens[5].length()));
		this.neighbourhood_textArea.setText(tokens[6].substring(15, tokens[6].length()));
		this.position_textArea.setText(tokens[7].substring(7, tokens[7].length()));

		//Correcting GUI Components based on the information:
		if(this.cR.isClosed()){
			String dateClosed_graphicText_string;
			if(this.dateClosed_graphic_label.getPlusWeeks()>0){
				dateClosed_graphicText_string = " +"+this.dateClosed_graphic_label.getPlusWeeks()+" week";
				if(this.dateClosed_graphic_label.getPlusWeeks() != 1){
					dateClosed_graphicText_string += "s ";
				} else {
					dateClosed_graphicText_string += " ";
				}
			} else {
				dateClosed_graphicText_string = "";
			}
			this.dateClosed_graphicText_label.setText(dateClosed_graphicText_string);
			this.status_graphic_label.setForeground(Color.GREEN);
			this.status_graphic_label.setText("Closed");
			this.dateClosed_textArea.setText(dateOutputFormat.format(this.cR.getDateClosed()));
		} else {
			//Status
			this.status_graphic_label.setForeground(Color.ORANGE);
			this.status_graphic_label.setText("Open");
			//Remove ClosedDate Line:
			this.detailFrame_main_panel.remove(this.dateClosed_graphic_label);
			this.detailFrame_main_panel.remove(this.dateClosed_graphicText_label);
			this.detailFrame_main_panel.remove(this.dateClosed_label);
			this.detailFrame_main_panel.remove(this.dateClosed_textArea);
		}
		this.colorizeBackgroundLines();
		this.repaint();
		this.pack();
	}
	
	/**
	 * colorize the lines differently for easier reading of the Details
	 */
	private void colorizeBackgroundLines() {
		Color lightGrayBckgrnd = new Color(220,220,220);
		this.category_label.setOpaque(true);
		this.category_label.setBackground(lightGrayBckgrnd);
		this.category_textArea.setBackground(lightGrayBckgrnd);
		this.statusNotes_label.setOpaque(true);
		this.statusNotes_label.setBackground(lightGrayBckgrnd);
		this.statusNotes_textArea.setBackground(lightGrayBckgrnd);
		if(this.cR.isClosed()){
			this.dateClosed_label.setOpaque(true);
			this.dateClosed_label.setBackground(lightGrayBckgrnd);
			this.dateClosed_graphicText_label.setOpaque(true);
			this.dateClosed_graphicText_label.setBackground(lightGrayBckgrnd);
			this.dateClosed_textArea.setBackground(lightGrayBckgrnd);
			this.neighbourhood_label.setOpaque(true);
			this.neighbourhood_label.setBackground(lightGrayBckgrnd);
			this.neighbourhood_textArea.setBackground(lightGrayBckgrnd);
		} else {
			this.position_label.setOpaque(true);
			this.position_label.setBackground(lightGrayBckgrnd);
			this.position_textArea.setBackground(lightGrayBckgrnd);
		}
	}

	private void initGUI() {
		GridBagLayout contentPane_gridBagLayout = new GridBagLayout();
		contentPane_gridBagLayout.columnWidths = new int[] {0};
		contentPane_gridBagLayout.rowHeights = new int[] {0};
		contentPane_gridBagLayout.columnWeights = new double[]{0.0};
		contentPane_gridBagLayout.rowWeights = new double[]{0.0};
		getContentPane().setLayout(contentPane_gridBagLayout);
		
		GridBagLayout detailFrame_main_label_gridBagLayout = new GridBagLayout();
		detailFrame_main_label_gridBagLayout.columnWidths = new int[] {0,0,0,0};
		detailFrame_main_label_gridBagLayout.rowHeights = new int[] {0,0,0,0,0,0,0,0,0,0};
		detailFrame_main_label_gridBagLayout.columnWeights = new double[]{0.0,0.0,0.0,0.0};
		detailFrame_main_label_gridBagLayout.rowWeights = new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		this.detailFrame_main_panel = new JPanel(detailFrame_main_label_gridBagLayout);
		GridBagConstraints gbc_detailFrame_main_panel = new GridBagConstraints();
		gbc_detailFrame_main_panel.anchor = GridBagConstraints.CENTER;
		gbc_detailFrame_main_panel.fill = GridBagConstraints.BOTH;
		gbc_detailFrame_main_panel.gridx = 0;
		gbc_detailFrame_main_panel.gridy = 0;
		getContentPane().add(detailFrame_main_panel, gbc_detailFrame_main_panel);
		Insets standardInsets = new Insets (2,0,2,0);
		
		this.caseId_label = new JLabel("CaseID:");
		GridBagConstraints gbc_caseId_label = new GridBagConstraints();
		gbc_caseId_label.anchor = GridBagConstraints.CENTER;
		gbc_caseId_label.fill = GridBagConstraints.BOTH;
		gbc_caseId_label.gridx = 0;
		gbc_caseId_label.gridy = 0;
		gbc_caseId_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.caseId_label, gbc_caseId_label);
		
		this.caseId_textArea = new JTextArea("testID");
		GridBagConstraints gbc_caseId_textArea = new GridBagConstraints();
		gbc_caseId_textArea.anchor = GridBagConstraints.CENTER;
		gbc_caseId_textArea.fill = GridBagConstraints.BOTH;
		gbc_caseId_textArea.gridwidth = 3;
		gbc_caseId_textArea.gridx = 1;
		gbc_caseId_textArea.gridy = 0;
		gbc_caseId_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.caseId_textArea, gbc_caseId_textArea);
		
		this.category_label = new JLabel("Category:");
		GridBagConstraints gbc_category_label = new GridBagConstraints();
		gbc_category_label.anchor = GridBagConstraints.CENTER;
		gbc_category_label.fill = GridBagConstraints.BOTH;
		gbc_category_label.gridx = 0;
		gbc_category_label.gridy = 1;
		gbc_category_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.category_label, gbc_category_label);
		
		this.category_textArea = new JTextArea("testCategory");
		GridBagConstraints gbc_category_textArea = new GridBagConstraints();
		gbc_category_textArea.anchor = GridBagConstraints.CENTER;
		gbc_category_textArea.fill = GridBagConstraints.BOTH;
		gbc_category_textArea.gridwidth = 3;
		gbc_category_textArea.gridx = 1;
		gbc_category_textArea.gridy = 1;
		gbc_category_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.category_textArea, gbc_category_textArea);
		
		this.status_label = new JLabel("Status:");
		GridBagConstraints gbc_status_label = new GridBagConstraints();
		gbc_status_label.anchor = GridBagConstraints.CENTER;
		gbc_status_label.fill = GridBagConstraints.BOTH;
		gbc_status_label.gridx = 0;
		gbc_status_label.gridy = 2;
		gbc_status_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.status_label, gbc_status_label);
		
		this.status_graphic_label = new JLabel("<status_graphic>", SwingConstants.LEFT);
		GridBagConstraints gbc_status_graphic_label = new GridBagConstraints();
		gbc_status_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_status_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_status_graphic_label.gridwidth = 3;
		gbc_status_graphic_label.gridx = 1;
		gbc_status_graphic_label.gridy = 2;
		gbc_status_graphic_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.status_graphic_label, gbc_status_graphic_label);
		
		this.statusNotes_label = new JLabel("StatusNotes:");
		GridBagConstraints gbc_statusNotes_label = new GridBagConstraints();
		gbc_statusNotes_label.anchor = GridBagConstraints.CENTER;
		gbc_statusNotes_label.fill = GridBagConstraints.BOTH;
		gbc_statusNotes_label.gridx = 0;
		gbc_statusNotes_label.gridy = 3;
		gbc_statusNotes_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.statusNotes_label, gbc_statusNotes_label);
		
		this.statusNotes_textArea = new JTextArea("test StatusNotes Text");
		GridBagConstraints gbc_statusNotes_textArea = new GridBagConstraints();
		gbc_statusNotes_textArea.anchor = GridBagConstraints.CENTER;
		gbc_statusNotes_textArea.fill = GridBagConstraints.BOTH;
		gbc_statusNotes_textArea.gridwidth = 3;
		gbc_statusNotes_textArea.gridx = 1;
		gbc_statusNotes_textArea.gridy = 3;
		gbc_statusNotes_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.statusNotes_textArea, gbc_statusNotes_textArea);
		
		this.dateOpened_label = new JLabel("Opened:");
		GridBagConstraints gbc_dateOpened_label = new GridBagConstraints();
		gbc_dateOpened_label.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_label.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_label.gridx = 0;
		gbc_dateOpened_label.gridy = 4;
		gbc_dateOpened_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateOpened_label, gbc_dateOpened_label);
		
		this.dateOpened_graphic_label = new SimpleWeekDayGraphic(this.cR, true, this.dateGraphicSize);
		GridBagConstraints gbc_dateOpened_graphic_label = new GridBagConstraints();
		gbc_dateOpened_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_graphic_label.gridx = 1;
		gbc_dateOpened_graphic_label.gridy = 4;
		gbc_dateOpened_graphic_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateOpened_graphic_label, gbc_dateOpened_graphic_label);
		
		this.dateOpened_textArea = new JTextArea("dateOpened text");
		GridBagConstraints gbc_dateOpened_textArea = new GridBagConstraints();
		gbc_dateOpened_textArea.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_textArea.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_textArea.gridx = 3;
		gbc_dateOpened_textArea.gridy = 4;
		gbc_dateOpened_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateOpened_textArea, gbc_dateOpened_textArea);
		

		this.dateClosed_label = new JLabel("Closed:");
		GridBagConstraints gbc_dateClosed_label = new GridBagConstraints();
		gbc_dateClosed_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_label.gridx = 0;
		gbc_dateClosed_label.gridy = 5;
		gbc_dateClosed_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateClosed_label, gbc_dateClosed_label);
		
		this.dateClosed_graphic_label = new SimpleWeekDayGraphic(this.cR, false, this.dateGraphicSize);
		GridBagConstraints gbc_dateClosed_graphic_label = new GridBagConstraints();
		gbc_dateClosed_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_graphic_label.gridx = 1;
		gbc_dateClosed_graphic_label.gridy = 5;
		gbc_dateClosed_graphic_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateClosed_graphic_label, gbc_dateClosed_graphic_label);
		
		this.dateClosed_graphicText_label = new JLabel("+ x weeks");
		GridBagConstraints gbc_dateClosed_graphicText_label = new GridBagConstraints();
		gbc_dateClosed_graphicText_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_graphicText_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_graphicText_label.gridx = 2;
		gbc_dateClosed_graphicText_label.gridy = 5;
		gbc_dateClosed_graphicText_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateClosed_graphicText_label, gbc_dateClosed_graphicText_label);
		
		this.dateClosed_textArea = new JTextArea("dateClosed text");
		GridBagConstraints gbc_dateClosed_textArea = new GridBagConstraints();
		gbc_dateClosed_textArea.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_textArea.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_textArea.gridx = 3;
		gbc_dateClosed_textArea.gridy = 5;
		gbc_dateClosed_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.dateClosed_textArea, gbc_dateClosed_textArea);
				
		this.position_label = new JLabel("Position:");
		GridBagConstraints gbc_position_label = new GridBagConstraints();
		gbc_position_label.anchor = GridBagConstraints.CENTER;
		gbc_position_label.fill = GridBagConstraints.BOTH;
		gbc_position_label.gridx = 0;
		gbc_position_label.gridy = 6;
		gbc_position_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.position_label, gbc_position_label);
		
		this.position_textArea = new JTextArea("coordinates");
		GridBagConstraints gbc_position_textArea = new GridBagConstraints();
		gbc_position_textArea.anchor = GridBagConstraints.CENTER;
		gbc_position_textArea.fill = GridBagConstraints.BOTH;
		gbc_position_textArea.gridwidth = 2;
		gbc_position_textArea.gridx = 1;
		gbc_position_textArea.gridy = 6;
		gbc_position_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.position_textArea, gbc_position_textArea);
		
		this.neighbourhood_label = new JLabel("Neighbourhood:");
		GridBagConstraints gbc_neighbourh_label = new GridBagConstraints();
		gbc_neighbourh_label.anchor = GridBagConstraints.CENTER;
		gbc_neighbourh_label.fill = GridBagConstraints.BOTH;
		gbc_neighbourh_label.gridx = 0;
		gbc_neighbourh_label.gridy = 7;
		gbc_neighbourh_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.neighbourhood_label, gbc_neighbourh_label);
		
		this.neighbourhood_textArea = new JTextArea("neighbourhood text");
		GridBagConstraints gbc_neighbourhood_textArea = new GridBagConstraints();
		gbc_neighbourhood_textArea.anchor = GridBagConstraints.CENTER;
		gbc_neighbourhood_textArea.fill = GridBagConstraints.BOTH;
		gbc_neighbourhood_textArea.gridwidth = 3;
		gbc_neighbourhood_textArea.gridx = 1;
		gbc_neighbourhood_textArea.gridy = 7;
		gbc_neighbourhood_textArea.insets = standardInsets;
		this.detailFrame_main_panel.add(this.neighbourhood_textArea, gbc_neighbourhood_textArea);
		
		this.neighbourhood_graphic_label = new JLabel("<Neighbourhood graphic>", SwingConstants.CENTER);
		GridBagConstraints gbc_neighbourhood_graphic_label = new GridBagConstraints();
		gbc_neighbourhood_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_neighbourhood_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_neighbourhood_graphic_label.gridwidth = 4;
		gbc_neighbourhood_graphic_label.gridx = 0;
		gbc_neighbourhood_graphic_label.gridy = 8;
		gbc_neighbourhood_graphic_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.neighbourhood_graphic_label, gbc_neighbourhood_graphic_label);
		
		this.center_to_point_button = new JButton("Center on Map");
		GridBagConstraints gbc_center_to_point_button = new GridBagConstraints();
		gbc_center_to_point_button.anchor = GridBagConstraints.CENTER;
		gbc_center_to_point_button.fill = GridBagConstraints.BOTH;
		gbc_center_to_point_button.gridx = 3;
		gbc_center_to_point_button.gridy = 6;
		gbc_center_to_point_button.insets = standardInsets;
		this.detailFrame_main_panel.add(this.center_to_point_button, gbc_center_to_point_button);

		this.image_label = new JLabel("no image available", SwingConstants.CENTER);
		this.image_label.setForeground(Color.RED);
		GridBagConstraints gbc_image_label = new GridBagConstraints();
		gbc_image_label.anchor = GridBagConstraints.CENTER;
		gbc_image_label.fill = GridBagConstraints.BOTH;
		gbc_image_label.gridwidth = 4;
		gbc_image_label.gridx = 0;
		gbc_image_label.gridy = 9;
		gbc_image_label.insets = standardInsets;
		this.detailFrame_main_panel.add(this.image_label, gbc_image_label);
		
		if(cR.hasAPictureLink()){
			// load picture:
			this.image_label.setText("Loading image...");
			this.image_label.setForeground(Color.GREEN);
			new PictureWorker(cR.getMediaURLPath(), this.detailFrame_main_panel, image_label, this).execute();
		}
		
		//EVENTHANDLERS:
		this.center_to_point_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ResultDetailFrame.this.mainFrameController.center_to_point(ResultDetailFrame.this.cR);
			}
		});
	}
}
