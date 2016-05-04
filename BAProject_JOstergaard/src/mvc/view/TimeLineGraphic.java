package mvc.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import mvc.main.Main;

public class TimeLineGraphic extends JPanel {
	//Offset space on each side
	private Point mousePos;
	private Color[] colorMaps = {
			new Color(255,255,204),
			new Color(255,247,160),
			new Color(254,217,118),
			new Color(254,178,76),
			new Color(253,151,60),
			new Color(252,78,42),
			new Color(227,26,28),
			new Color(189,0,38),
			new Color(128,0,38),
			};
	private double xOuterOffsetLeft, xOuterOffsetRight;
	private double yOuterOffsetTop, yOuterOffsetBot;
	private double xInnerOffset, yInnerOffset;
	private double xDrawRange, yDrawRange;

	public TimeLineGraphic(){
		super();
		this.init();
	}
	
	private void init() {
		this.setupMouseListener();
		this.refreshLayoutSettings();
	}

	private void setupMouseListener(){
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mousePos = e.getPoint();
				//TODO if(item got dragged)set new position for dragged item
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				//TODO set maybe-drag-state, save hovered object
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//TODO maybe error handling
			}
			
			@Override
			public void mouseEntered(MouseEvent e){} //ignore
			
			@Override
			public void mouseClicked(MouseEvent e){
				
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				anyMouseMovement(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mousePos = e.getPoint();
				//TODO if(dragged-state has not begun) begin dragged state
			}
			
			private void anyMouseMovement(MouseEvent e){
				mousePos = e.getPoint();
				//TODO
			}
		});
	}
	
	private void refreshLayoutSettings() {
		this.xOuterOffsetLeft = this.getWidth()*0.05;
		this.xOuterOffsetRight = this.getWidth()*0.05;
		this.yOuterOffsetTop = this.getHeight()*0.05;
		this.yOuterOffsetBot = this.getHeight()*0.05;
		this.xInnerOffset = 0;
		this.yInnerOffset = 0;
		this.xDrawRange = this.getWidth()*(1.0-(this.xOuterOffsetLeft + this.xOuterOffsetRight)/this.getWidth());
		this.yDrawRange = this.getHeight()*(1.0-(this.yOuterOffsetTop+this.yOuterOffsetBot)/this.getHeight());
	}
	public void paint(Graphics g){
		//INIT:
		Graphics2D g2d = (Graphics2D)g;
		//DRAW BACKGROUND:
		//DRAW BARS:
		//DRAW WEIGHTLINES(include drag-state):
		g2d.dispose();
	}
}