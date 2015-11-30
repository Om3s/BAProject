package mvc.model;

import java.awt.Color;
import java.awt.Paint;
import java.util.Calendar;

import mvc.controller.MainframeController;

import org.jfree.chart.renderer.category.BarRenderer;

/**
 * 
 * @author Jonas Ostergaard
 *
 */
public class CustomBarRenderer extends BarRenderer {
	private final MainframeController mFController;
	private int dayOfWeek, firstDay, timeLineDateSteps;;
	Calendar cal;
	public CustomBarRenderer(MainframeController mFC)
	{
		super();
		this.mFController = mFC;
		this.timeLineDateSteps = mFC.getTimeLineDateSteps();
		cal = Calendar.getInstance();
		cal.setTime(mFC.getGlobalFromDate());
		firstDay = cal.get(Calendar.DAY_OF_WEEK);
		System.out.println("FirstDay: "+ this.firstDay);
	}
	
	@Override
	/**
	 * This overwrites the original getItemPaint method, to colorize
	 * the bars into the specific weekday colors defined in the main class
	 */
	public Paint getItemPaint(int row,int column)
	{
		this.dayOfWeek = ((this.firstDay + row - 1) % 7)+1;
		switch (this.dayOfWeek) {
			case 1: return Color.BLUE; //sunday
			case 2: return Color.YELLOW; //monday
			case 3: return Color.CYAN; //tuesday
			case 4: return Color.MAGENTA; //wednesday
			case 5: return new Color(220,90,0); //thursday
			case 6: return Color.RED; //friday
			case 7: return Color.GREEN; //saturday
			default: return null;
		}
	}
}
