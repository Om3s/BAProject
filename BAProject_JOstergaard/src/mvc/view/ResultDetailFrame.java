package mvc.view;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import mvc.controller.MainframeController;
import mvc.controller.PictureWorker;
import mvc.main.Main;
import mvc.model.CaseReport;

public class ResultDetailFrame extends JFrame {
	private CaseReport cR;
	private String content;
	private JTextArea caseId_textArea, category_textArea, statusNotes_textArea, dateOpened_textArea, dateClosed_textArea, position_textArea, neighbourhood_textArea;
	private JLabel caseId_label, category_label, status_label, statusNotes_label, dateOpened_label, dateClosed_label, dateClosed_graphicText_label, position_label, neighbourh_label;
	private JPanel detailFrame_main_panel;
	private JLabel neighbourhood_graphic_label, image_label, status_graphic_label, dateOpened_graphic_label, dateClosed_graphic_label;
	private JButton center_to_point_button;
	
	public ResultDetailFrame(CaseReport cR) {
		this.cR = cR;
		this.initGUI();
		this.initGUILogic();
	}

	private void initGUILogic() {
		this.createDetailInformation();
		this.pack();
		this.setVisible(true);
	}
	
	private void createDetailInformation(){
		String result;
		result = cR.toString2();
		result = result.substring(11, result.length()-1);
		Pattern pattern = Pattern.compile(Pattern.quote("%$sepa&%$"));
		String[] tokens = pattern.split(result);
		result = "Selected Case:\n";
		for(String s : tokens){
			result += s+"\n";
		}
		this.content = result;
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
		
		this.caseId_label = new JLabel("CaseID:");
		GridBagConstraints gbc_caseId_label = new GridBagConstraints();
		gbc_caseId_label.anchor = GridBagConstraints.CENTER;
		gbc_caseId_label.fill = GridBagConstraints.BOTH;
		gbc_caseId_label.gridx = 0;
		gbc_caseId_label.gridy = 0;
		this.detailFrame_main_panel.add(this.caseId_label, gbc_caseId_label);
		
		this.caseId_textArea = new JTextArea("testID");
		GridBagConstraints gbc_caseId_textArea = new GridBagConstraints();
		gbc_caseId_textArea.anchor = GridBagConstraints.CENTER;
		gbc_caseId_textArea.fill = GridBagConstraints.BOTH;
		gbc_caseId_textArea.gridx = 1;
		gbc_caseId_textArea.gridy = 0;
		this.detailFrame_main_panel.add(this.caseId_textArea, gbc_caseId_textArea);
		
		this.category_label = new JLabel("Category:");
		GridBagConstraints gbc_category_label = new GridBagConstraints();
		gbc_category_label.anchor = GridBagConstraints.CENTER;
		gbc_category_label.fill = GridBagConstraints.BOTH;
		gbc_category_label.gridx = 0;
		gbc_category_label.gridy = 1;
		this.detailFrame_main_panel.add(this.category_label, gbc_category_label);
		
		this.category_textArea = new JTextArea("testCategory");
		GridBagConstraints gbc_category_textArea = new GridBagConstraints();
		gbc_category_textArea.anchor = GridBagConstraints.CENTER;
		gbc_category_textArea.fill = GridBagConstraints.BOTH;
		gbc_category_textArea.gridx = 1;
		gbc_category_textArea.gridy = 1;
		this.detailFrame_main_panel.add(this.category_textArea, gbc_category_textArea);
		
		this.status_label = new JLabel("Status:");
		GridBagConstraints gbc_status_label = new GridBagConstraints();
		gbc_status_label.anchor = GridBagConstraints.CENTER;
		gbc_status_label.fill = GridBagConstraints.BOTH;
		gbc_status_label.gridx = 0;
		gbc_status_label.gridy = 2;
		this.detailFrame_main_panel.add(this.status_label, gbc_status_label);
		
		this.status_graphic_label = new JLabel("<status graphic>", SwingConstants.CENTER);
		GridBagConstraints gbc_status_graphic_label = new GridBagConstraints();
		gbc_status_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_status_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_status_graphic_label.gridx = 1;
		gbc_status_graphic_label.gridy = 2;
		this.detailFrame_main_panel.add(this.status_graphic_label, gbc_status_graphic_label);
		
		this.statusNotes_label = new JLabel("StatusNotes:");
		GridBagConstraints gbc_statusNotes_label = new GridBagConstraints();
		gbc_statusNotes_label.anchor = GridBagConstraints.CENTER;
		gbc_statusNotes_label.fill = GridBagConstraints.BOTH;
		gbc_statusNotes_label.gridx = 0;
		gbc_statusNotes_label.gridy = 3;
		this.detailFrame_main_panel.add(this.statusNotes_label, gbc_statusNotes_label);
		
		this.statusNotes_textArea = new JTextArea("test StatusNotes Text");
		GridBagConstraints gbc_statusNotes_textArea = new GridBagConstraints();
		gbc_statusNotes_textArea.anchor = GridBagConstraints.CENTER;
		gbc_statusNotes_textArea.fill = GridBagConstraints.BOTH;
		gbc_statusNotes_textArea.gridx = 1;
		gbc_statusNotes_textArea.gridy = 3;
		this.detailFrame_main_panel.add(this.statusNotes_textArea, gbc_statusNotes_textArea);
		
		this.dateOpened_label = new JLabel("Opened:");
		GridBagConstraints gbc_dateOpened_label = new GridBagConstraints();
		gbc_dateOpened_label.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_label.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_label.gridx = 0;
		gbc_dateOpened_label.gridy = 4;
		this.detailFrame_main_panel.add(this.dateOpened_label, gbc_dateOpened_label);
		
		this.dateOpened_graphic_label = new JLabel("<dateOpened graphic>", SwingConstants.CENTER);
		GridBagConstraints gbc_dateOpened_graphic_label = new GridBagConstraints();
		gbc_dateOpened_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_graphic_label.gridx = 1;
		gbc_dateOpened_graphic_label.gridy = 4;
		this.detailFrame_main_panel.add(this.dateOpened_graphic_label, gbc_dateOpened_graphic_label);
		
		this.dateOpened_textArea = new JTextArea("dateOpened text");
		GridBagConstraints gbc_dateOpened_textArea = new GridBagConstraints();
		gbc_dateOpened_textArea.anchor = GridBagConstraints.CENTER;
		gbc_dateOpened_textArea.fill = GridBagConstraints.BOTH;
		gbc_dateOpened_textArea.gridx = 3;
		gbc_dateOpened_textArea.gridy = 4;
		this.detailFrame_main_panel.add(this.dateOpened_textArea, gbc_dateOpened_textArea);
		
		this.dateClosed_label = new JLabel("Closed:");
		GridBagConstraints gbc_dateClosed_label = new GridBagConstraints();
		gbc_dateClosed_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_label.gridx = 0;
		gbc_dateClosed_label.gridy = 5;
		this.detailFrame_main_panel.add(this.dateClosed_label, gbc_dateClosed_label);
		
		this.dateClosed_graphic_label = new JLabel("<dateClosed graphic>", SwingConstants.CENTER);
		GridBagConstraints gbc_dateClosed_graphic_label = new GridBagConstraints();
		gbc_dateClosed_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_graphic_label.gridx = 1;
		gbc_dateClosed_graphic_label.gridy = 5;
		this.detailFrame_main_panel.add(this.dateClosed_graphic_label, gbc_dateClosed_graphic_label);
		
		this.dateClosed_graphicText_label = new JLabel("+ x weeks");
		GridBagConstraints gbc_dateClosed_graphicText_label = new GridBagConstraints();
		gbc_dateClosed_graphicText_label.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_graphicText_label.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_graphicText_label.gridx = 2;
		gbc_dateClosed_graphicText_label.gridy = 5;
		this.detailFrame_main_panel.add(this.dateClosed_graphicText_label, gbc_dateClosed_graphicText_label);
		
		this.dateClosed_textArea = new JTextArea("dateClosed text");
		GridBagConstraints gbc_dateClosed_textArea = new GridBagConstraints();
		gbc_dateClosed_textArea.anchor = GridBagConstraints.CENTER;
		gbc_dateClosed_textArea.fill = GridBagConstraints.BOTH;
		gbc_dateClosed_textArea.gridx = 3;
		gbc_dateClosed_textArea.gridy = 5;
		this.detailFrame_main_panel.add(this.dateClosed_textArea, gbc_dateClosed_textArea);
		
		this.position_label = new JLabel("Position:");
		GridBagConstraints gbc_position_label = new GridBagConstraints();
		gbc_position_label.anchor = GridBagConstraints.CENTER;
		gbc_position_label.fill = GridBagConstraints.BOTH;
		gbc_position_label.gridx = 0;
		gbc_position_label.gridy = 6;
		this.detailFrame_main_panel.add(this.position_label, gbc_position_label);
		
		this.position_textArea = new JTextArea("coordinates");
		GridBagConstraints gbc_position_textArea = new GridBagConstraints();
		gbc_position_textArea.anchor = GridBagConstraints.CENTER;
		gbc_position_textArea.fill = GridBagConstraints.BOTH;
		gbc_position_textArea.gridx = 1;
		gbc_position_textArea.gridy = 6;
		this.detailFrame_main_panel.add(this.position_textArea, gbc_position_textArea);
		
		this.neighbourh_label = new JLabel("Neighbourhood:");
		GridBagConstraints gbc_neighbourh_label = new GridBagConstraints();
		gbc_neighbourh_label.anchor = GridBagConstraints.CENTER;
		gbc_neighbourh_label.fill = GridBagConstraints.BOTH;
		gbc_neighbourh_label.gridx = 0;
		gbc_neighbourh_label.gridy = 7;
		this.detailFrame_main_panel.add(this.neighbourh_label, gbc_neighbourh_label);
		
		this.neighbourhood_textArea = new JTextArea("neighbourhood text");
		GridBagConstraints gbc_neighbourhood_textArea = new GridBagConstraints();
		gbc_neighbourhood_textArea.anchor = GridBagConstraints.CENTER;
		gbc_neighbourhood_textArea.fill = GridBagConstraints.BOTH;
		gbc_neighbourhood_textArea.gridx = 1;
		gbc_neighbourhood_textArea.gridy = 7;
		this.detailFrame_main_panel.add(this.neighbourhood_textArea, gbc_neighbourhood_textArea);
		
		this.neighbourhood_graphic_label = new JLabel("<Neighbourhood graphic>", SwingConstants.CENTER);
		GridBagConstraints gbc_neighbourhood_graphic_label = new GridBagConstraints();
		gbc_neighbourhood_graphic_label.anchor = GridBagConstraints.CENTER;
		gbc_neighbourhood_graphic_label.fill = GridBagConstraints.BOTH;
		gbc_neighbourhood_graphic_label.gridwidth = 3;
		gbc_neighbourhood_graphic_label.gridx = 0;
		gbc_neighbourhood_graphic_label.gridy = 8;
		this.detailFrame_main_panel.add(this.neighbourhood_graphic_label, gbc_neighbourhood_graphic_label);
		
		this.center_to_point_button = new JButton("Center on Map");
		GridBagConstraints gbc_center_to_point_button = new GridBagConstraints();
		gbc_center_to_point_button.anchor = GridBagConstraints.CENTER;
		gbc_center_to_point_button.fill = GridBagConstraints.BOTH;
		gbc_center_to_point_button.gridx = 3;
		gbc_center_to_point_button.gridy = 8;
		this.detailFrame_main_panel.add(this.center_to_point_button, gbc_center_to_point_button);

		this.image_label = new JLabel("no image available", SwingConstants.CENTER);
		GridBagConstraints gbc_image_label = new GridBagConstraints();
		gbc_image_label.anchor = GridBagConstraints.CENTER;
		gbc_image_label.fill = GridBagConstraints.BOTH;
		gbc_image_label.gridwidth = 4;
		gbc_image_label.gridx = 0;
		gbc_image_label.gridy = 9;
		this.detailFrame_main_panel.add(this.image_label, gbc_image_label);
		
		if(cR.hasAPictureLink()){
			// load picture:
			this.image_label.setText("Loading image...");
			new PictureWorker(cR.getMediaURLPath(), this.detailFrame_main_panel, image_label, this).execute();
		}
		
		//EVENTHANDLERS:
		this.center_to_point_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// center on selected point on geomap (mainframe)
			}
		});
	}
}
