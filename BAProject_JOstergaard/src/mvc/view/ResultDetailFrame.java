package mvc.view;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import mvc.main.Main;
import mvc.model.CaseReport;

public class ResultDetailFrame extends JFrame {
	private CaseReport cR;
	private String content;
	private JTextArea detailTextArea;
	private JPanel detailText_panel;
	private JButton loadPictureButton;
	
	public ResultDetailFrame(CaseReport cR) {
		this.cR = cR;
		this.initGUI();
		this.initGUILogic();
	}

	private void initGUILogic() {
		this.createDetailTextContent();
		this.detailTextArea.setText(this.content);
		this.pack();
		this.setVisible(true);
	}
	
	private void createDetailTextContent(){
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
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0};
		getContentPane().setLayout(gridBagLayout);
		
		this.detailText_panel = new JPanel();
		GridBagConstraints gbc_detailText_panel = new GridBagConstraints();
		gbc_detailText_panel.anchor = GridBagConstraints.NORTH;
		gbc_detailText_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_detailText_panel.gridx = 0;
		gbc_detailText_panel.gridy = 0;
		getContentPane().add(detailText_panel, gbc_detailText_panel);
		GridBagLayout gbl_detailText_panel = new GridBagLayout();
		gbl_detailText_panel.columnWidths = new int[]{0, 0};
		gbl_detailText_panel.rowHeights = new int[]{0, 0};
		gbl_detailText_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_detailText_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		detailText_panel.setLayout(gbl_detailText_panel);
		
		this.detailTextArea = new JTextArea();
		GridBagConstraints gbc_detailTextArea = new GridBagConstraints();
		gbc_detailTextArea.weighty = 1.0;
		gbc_detailTextArea.weightx = 1.0;
		gbc_detailTextArea.fill = GridBagConstraints.BOTH;
		gbc_detailTextArea.gridx = 0;
		gbc_detailTextArea.gridy = 0;
		detailText_panel.add(detailTextArea, gbc_detailTextArea);
		
		this.loadPictureButton = new JButton("No Picture Available");
		this.loadPictureButton.setEnabled(false);
		GridBagConstraints gbc_loadPictureButton = new GridBagConstraints();
		gbc_loadPictureButton.weighty = 1.0;
		gbc_loadPictureButton.weightx = 0.1;
		gbc_loadPictureButton.fill = GridBagConstraints.BOTH;
		gbc_loadPictureButton.gridx = 0;
		gbc_loadPictureButton.gridy = 1;
		this.detailText_panel.add(this.loadPictureButton, gbc_loadPictureButton);
		if(cR.hasAPictureLink()){
			this.loadPictureButton.setText("Show Picture");
			this.loadPictureButton.setEnabled(true);
		}
		//EVENTHANDLERS:
		this.loadPictureButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                ResultDetailFrame.this.loadPictureButton.setText("Loading Picture...");
                ResultDetailFrame.this.loadPictureButton.setEnabled(false);
				ResultDetailFrame.this.createWindowWithPicture();
			}
		});
	}
	
	private void createWindowWithPicture(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                try {
                	String path = ResultDetailFrame.this.cR.getMediaURLPath();
                    System.out.println("Get Image from " + path);
                    URL url = new URL(path);
                    BufferedImage image = ImageIO.read(url);
                    System.out.println("Load image into frame...");
                    JLabel label = new JLabel(new ImageIcon(image));
                    JFrame f = new JFrame();
                    f.getContentPane().add(label);
                    f.pack();
                    Rectangle r = f.getBounds();
                    f.setLocation((int)((Main.screenWidth / 2)-(r.getWidth()/2)), (int)((Main.screenHeight / 2)-(r.getHeight()/2)));
                    f.setVisible(true);
                    ResultDetailFrame.this.loadPictureButton.setText("Show Picture");
                    ResultDetailFrame.this.loadPictureButton.setEnabled(true);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }
        });
    }
}
