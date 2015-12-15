package mvc.view;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JTextArea;

public class ResultDetailFrame extends JFrame {
	private String content;
	private JTextArea detailTextArea;
	private JPanel detailText_panel;
	
	public ResultDetailFrame(String result) {
		this.content = result;
		this.initGUI();
		this.initGUILogic();
	}

	private void initGUILogic() {
		this.detailTextArea.setText(this.content);
		this.pack();
		this.setVisible(true);
//		this.testURLImage();
	}

	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {0};
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
	}
	
	private void testURLImage(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                try {
                    String path = "http://mobile311.sfgov.org/media/san_francisco/report/photos/551b5193df861c2f469d40d2/report.jpg";
                    System.out.println("Get Image from " + path);
                    URL url = new URL(path);
                    BufferedImage image = ImageIO.read(url);
                    System.out.println("Load image into frame...");
                    JLabel label = new JLabel(new ImageIcon(image));
                    JFrame f = new JFrame();
                    f.getContentPane().add(label);
                    f.pack();
                    f.setLocation(200, 200);
                    f.setVisible(true);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }
        });
    }
}
