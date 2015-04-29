package mvc.main;

import java.awt.event.MouseEvent;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import mvc.controller.MapController;
import mvc.model.CrimeCaseDatabase;
import mvc.view.Mainframe;

public class Main {
	
	public static void main(String[] args) throws IOException {
		CrimeCaseDatabase dataBase;
		dataBase = new CrimeCaseDatabase("dat\\Case_Data_from_San_Francisco_311__SF311_.csv");
		
		System.out.println("Create Map...");
		JMapViewer map = new JMapViewer();
		map.setDisplayPositionByLatLon(37.7, -122, 7);
		
		System.out.println("Create Controller...");
		DefaultMapController controller = new MapController(map, dataBase);
		controller.setMovementMouseButton(MouseEvent.BUTTON2);
		controller.setDoubleClickZoomEnabled(false);
		
		System.out.println("Create UI...");
		Mainframe mFrame = new Mainframe(map);
		mFrame.setVisible(true);
		
		mFrame.repaint();
		map.updateUI();
		System.out.println("All Done, program is running.");
		
		System.exit(0); //testing
	}

}