package mvc.model;

import java.awt.Color;
import java.awt.Point;

import mvc.controller.MapController;
import mvc.main.Main;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class GeoMapViewer extends JMapViewer {
	private MapController controller;
	
    @Override
    public void setZoom(int zoom, Point mapPoint) {
	    super.setZoom(zoom, mapPoint);
    	int alphaValue;
    	if(zoom > 17){
    		alphaValue = 180;
    	} else if (zoom > 16){
    		alphaValue = 130;
    	} else if (zoom > 15){
    		alphaValue = 100;
    	} else if (zoom > 14){
    		alphaValue = 75;
    	} else if (zoom > 13){
    		alphaValue = 50;
    	} else if (zoom > 12){
    		alphaValue = 30;
    	} else if (zoom > 11){
    		alphaValue = 15;
    	} else {
    		alphaValue = 5;
    	}
    	System.out.println("Zoomlevel: "+this.zoom);
    	System.out.println("AlphaValue: "+alphaValue);
    	Main.alphaColor = new Color(48,145,0,alphaValue);
    	if(this.controller != null){
    		this.controller.refreshDots();
    	}
    }
    
    public void setMapController(MapController mapController){
    	this.controller = mapController;
    }
}
