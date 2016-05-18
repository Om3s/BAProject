package mvc.controller;

import mvc.main.Main;

public class AnalyzeButtonThreadController extends Thread {
	private String threadName;
	private Thread t;
	
	public AnalyzeButtonThreadController(String name){
		System.out.println("Created "+this.threadName);
		this.threadName = name;
	}
	
	public void run(){
		Main.mainframeController.analyzeButtonIsPressed();
		System.out.println("Exiting "+this.threadName);
	}
	
	public void start(){
		System.out.println("Starting "+this.threadName);
		if (t == null){
			t = new Thread(this);
			t.start ();
		}
	}
}
