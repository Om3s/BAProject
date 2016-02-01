package mvc.model;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class GeoPoint extends MapMarkerDot {
	private CaseReport relatedCaseReport;
	
	public GeoPoint(Coordinate coord) {
		this(coord, null);
	}
	
	public GeoPoint(Coordinate coord, CaseReport cR) {
		super(coord);
		this.setVisible(false);
		this.relatedCaseReport = cR;
	}
	
	/**
	 * 
	 * @return the related CaseReport
	 */
	public CaseReport getRelatedCaseReport() {
		return relatedCaseReport;
	}
	
	/**
	 * Set the related CaseReport
	 * @param relatedCaseReport this will be the unique related CaseReport for this GeoPoint Object
	 */
	public void setRelatedCaseReport(CaseReport relatedCaseReport) {
		this.relatedCaseReport = relatedCaseReport;
	}
	
	@Override
	public void paint(Graphics g, Point position, int radius){
        int sizeH;
        int size;

		sizeH = radius;
		size = sizeH * 2;
		
        if (g instanceof Graphics2D && getBackColor() != null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.setPaint(getBackColor());
            
        	if(this.relatedCaseReport.isCurrentSelected()){
        		sizeH = radius * 2;
        		size = sizeH * 2;
                g.fillOval(position.x - sizeH, position.y - sizeH, size, size);
    		} else {
    			g.fillOval(position.x - sizeH, position.y - sizeH, size, size);
    		}
            g2.setComposite(oldComposite);
        }
        g.setColor(getColor());
        if(this.relatedCaseReport.isCurrentSelected()){
    		sizeH = radius * 2;
    		size = sizeH * 2;
            g.drawOval(position.x - sizeH, position.y - sizeH, size, size);
		} else {
	        g.drawOval(position.x - sizeH, position.y - sizeH, size, size);
		}

        if (getLayer() == null || getLayer().isVisibleTexts()) paintText(g, position);
		
	}
}
