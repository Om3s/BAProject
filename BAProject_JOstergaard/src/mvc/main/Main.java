package mvc.main;

import mvc.model.CaseReport;
import mvc.view.Mainframe;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		CaseReport report = new CaseReport();
		Mainframe mFrame = new Mainframe();
		mFrame.setVisible(true);
	}

}
