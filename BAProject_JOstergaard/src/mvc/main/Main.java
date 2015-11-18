package mvc.main;

import java.awt.Color;
import java.awt.event.MouseEvent;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import mvc.controller.MainframeController;
import mvc.controller.MapController;
import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;

public class Main {
	public static Color[] weekDayColors;
	public static Color
		mondayColor = Color.YELLOW,
		tuesdayColor = Color.CYAN,
		wednesdayColor = Color.MAGENTA,
		thursdayColor = new Color(220,90,0), //ORANGE
		fridayColor = Color.RED,
		saturdayColor = Color.GREEN,
		sundayColor = Color.blue;
	
	public static void main(String[] args) throws Exception {
		defineColors();
		CrimeCaseDatabase dataBase;
		dataBase = new CrimeCaseDatabase("dat\\Case_Data_from_San_Francisco_311__SF311_.csv");
		
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

	private static void defineColors() {
		Main.weekDayColors = new Color[7];
		Main.weekDayColors[0] = sundayColor;
		Main.weekDayColors[1] = mondayColor;
		Main.weekDayColors[2] = tuesdayColor; 
		Main.weekDayColors[3] = wednesdayColor;
		Main.weekDayColors[4] = thursdayColor;
		Main.weekDayColors[5] = fridayColor;
		Main.weekDayColors[6] = saturdayColor;
	}

}
