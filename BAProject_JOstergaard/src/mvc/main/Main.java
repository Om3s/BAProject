package mvc.main;

import mvc.model.CaseReport;
import mvc.model.CrimeCaseCollection;
import mvc.view.Mainframe;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		Mainframe mFrame = new Mainframe();
		mFrame.setVisible(true);
		
		CrimeCaseCollection dataBase = new CrimeCaseCollection("dat\\Case_Data_from_San_Francisco_311__SF311_.csv");
	}

}
