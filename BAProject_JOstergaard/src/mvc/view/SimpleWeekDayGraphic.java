package mvc.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import mvc.main.Main;
import mvc.model.CaseReport;

public class SimpleWeekDayGraphic extends JPanel {
	private double xOuterOffset,yOuterOffset,
	xInnerOffset,yInnerOffset,xDrawRange,yDrawRange;
	private boolean fromDate;
	private CaseReport cR;
	private int plusWeeks;
		
	public SimpleWeekDayGraphic(CaseReport cR, boolean fromDate, Dimension size){
		super();
		this.setGraphicSize(size);
		this.fromDate = fromDate;
		this.cR = cR;
		if(this.cR.getDateClosed() != null){
			this.determinePlusWeeks();
		}
	}
	
	private void determinePlusWeeks() {
		long dayClosed,dayOpened,dayDifference,restOfWeek;
		restOfWeek = 7 - this.cR.getDayOfWeek();
		dayClosed = this.cR.getDateClosed().getTime()/86400000;
		dayOpened = this.cR.getDateOpened().getTime()/86400000;
		dayDifference = dayClosed - dayOpened;
		if(restOfWeek - dayDifference > 0){
			this.plusWeeks = 0;
		} else {
			this.plusWeeks = 1+(int)(dayDifference-restOfWeek)/7;
		}
	}

	private void setGraphicSize(Dimension size){
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	private void refreshLayoutSettings() {
		this.yOuterOffset = this.getHeight()*0.10;
		this.xOuterOffset = this.yOuterOffset;
		this.xInnerOffset = this.getWidth()*0.02;
		this.yInnerOffset = this.getHeight()*0.02;
		this.xDrawRange = this.getWidth()*(1.0-(this.xOuterOffset*2)/this.getWidth());
		this.yDrawRange = this.getHeight()*(1.0-(this.yOuterOffset*2)/this.getHeight());
	}
	
	public void paint(Graphics g){
		this.refreshLayoutSettings();
		Graphics2D g2d = (Graphics2D)g;
		double rectWidth = this.determineRecWidth(7);
		double rectHeight = this.determineRecHeight(1);
		double posX,posY;
		posY = this.yOuterOffset;
		//Draw background:
		if(this.fromDate){
			g2d.setColor(Color.WHITE);
		} else {
			g2d.setColor(new Color(220,220,220));
		}
		
		g2d.fill(new Rectangle2D.Double(0.0, 0.0, this.getWidth(), this.getHeight()));
		//Draw rectangles:
		for(int x=0; x<7; x++){
			posX = this.xOuterOffset+x*(rectWidth+this.xInnerOffset);
			g2d.setColor(Main.weekDayColors[x]);
			g2d.drawRect((int)posX, (int)posY, (int)rectWidth, (int)rectHeight);
//			g2d.draw(new Rectangle2D.Double(posX,posY,rectWidth,rectHeight));
		}
		int dayOfWeek = this.determineDayOfWeek();
		posX = this.xOuterOffset+dayOfWeek*(rectWidth+this.xInnerOffset);
		g2d.setColor(Main.weekDayColors[dayOfWeek]);
		g2d.fill(new Rectangle2D.Double(posX,posY,rectWidth,rectHeight));
	}
	
	/**
	 * the dayOfWeek from the dateClosed is not stored in the CaseReport
	 * so this method will determine it for the case that this is the
	 * dateClosed graphic.
	 * 
	 * @return the dayOfWeek value for either dateOpened or dateClosed
	 * depends on what is needed.
	 */
	private int determineDayOfWeek() {
		if(this.fromDate){
			return this.cR.getDayOfWeek()-1;
		} else {
			Date dateClosed = this.cR.getDateClosed();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dateClosed.getTime());
			return (c.get(Calendar.DAY_OF_WEEK)-1);
		}
		
	}

	private double determineRecWidth(int amount) {
		double recWidth = ((this.xDrawRange-((amount-1)*this.xInnerOffset))/amount);
		return recWidth;
	}

	private double determineRecHeight(int amount) {
		double recHeight = ((this.yDrawRange-((amount-1)*this.yInnerOffset))/amount);
		return recHeight;
	}

	public int getPlusWeeks() {
		return this.plusWeeks;
	}
}
