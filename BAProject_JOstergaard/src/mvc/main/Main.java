package mvc.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import mvc.controller.MainframeController;
import mvc.controller.MapController;
import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;
import mvc.view.MatrixVisualization;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class Main {
	public static Color[] weekDayColors,weekDayColorsWithAlpha;
	public static int colorAlpha = 50, screenWidth, screenHeight;
	public static Color
		mondayColor = new Color(213,207,11), //YELLOW
		tuesdayColor = new Color(0,200,240), //CYAN
		wednesdayColor = new Color(160,15,200), //MAGENTA
		thursdayColor = new Color(220,90,0), //ORANGE
		fridayColor = new Color(200,0,0), //RED
		saturdayColor = new Color(0,200,100), //GREEN
		sundayColor = new Color(0,0,200), // BLUE
		mondayColorWithAlpha = new Color(213,207,11,colorAlpha), //YELLOW
		tuesdayColorWithAlpha = new Color(0,200,240,colorAlpha), //CYAN
		wednesdayColorWithAlpha = new Color(160,15,200,colorAlpha), //MAGENTA
		thursdayColorWithAlpha = new Color(220,90,0,colorAlpha), //ORANGE
		fridayColorWithAlpha = new Color(200,0,0,colorAlpha), //RED
		saturdayColorWithAlpha = new Color(0,200,100,colorAlpha), //GREEN
		sundayColorWithAlpha = new Color(0,0,200,colorAlpha); //BLUE
	
	public static void main(String[] args) throws Exception {
		defineColors();
		determineScreenSize();
		CrimeCaseDatabase dataBase;
		boolean testing = false;
		boolean reIndex = false;
		if(testing){
			dataBase = new CrimeCaseDatabase("dat\\testData.csv", reIndex);	
		} else {
			dataBase = new CrimeCaseDatabase("dat\\Case_Data_from_San_Francisco_311__SF311_.csv", reIndex);
			
		}
		
		System.out.println("Create Map...");
		JMapViewer map = new JMapViewer();
		map.setDisplayPositionByLatLon(37.73, -122.45, 11);
		
		System.out.println("Create Controller...");
		MapController geoMapController = new MapController(map, dataBase);
		geoMapController.setMovementMouseButton(MouseEvent.BUTTON2);
		geoMapController.setDoubleClickZoomEnabled(false);
		
		System.out.println("Create UI...");
		Mainframe mFrame = new Mainframe(map, (MapController)geoMapController);
		MainframeController mFrameController = new MainframeController(mFrame, dataBase, geoMapController);
		mFrame.setController(mFrameController);
		mFrame.setVisible(true);
		
		mFrame.repaint();
		map.updateUI();
		System.out.println("All Done, program is running.");
		
//		System.exit(0); //testing
	}
	
	private static void determineScreenSize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Main.screenWidth = gd.getDisplayMode().getWidth(); 
		Main.screenHeight = gd.getDisplayMode().getHeight();
	}

	/**
	 * This method sets up the colorarray with the static defined
	 * colors for each day. The index of the array is the same index
	 * as the weekday Number of the Calendar.WEEKDAY standard.
	 */
	private static void defineColors() {
		Main.weekDayColors = new Color[7];
		Main.weekDayColors[0] = sundayColor;
		Main.weekDayColors[1] = mondayColor;
		Main.weekDayColors[2] = tuesdayColor; 
		Main.weekDayColors[3] = wednesdayColor;
		Main.weekDayColors[4] = thursdayColor;
		Main.weekDayColors[5] = fridayColor;
		Main.weekDayColors[6] = saturdayColor;
		Main.weekDayColorsWithAlpha = new Color[7];
		Main.weekDayColorsWithAlpha[0] = sundayColorWithAlpha;
		Main.weekDayColorsWithAlpha[1] = mondayColorWithAlpha;
		Main.weekDayColorsWithAlpha[2] = tuesdayColorWithAlpha;
		Main.weekDayColorsWithAlpha[3] = wednesdayColorWithAlpha;
		Main.weekDayColorsWithAlpha[4] = thursdayColorWithAlpha;
		Main.weekDayColorsWithAlpha[5] = fridayColorWithAlpha;
		Main.weekDayColorsWithAlpha[6] = saturdayColorWithAlpha;
	}

}
