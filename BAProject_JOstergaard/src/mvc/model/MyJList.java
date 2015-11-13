package mvc.model;

import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;


public class MyJList<E> extends JList<E> {
	
	public MyJList(DefaultListModel<E> dLM){
		super(dLM);
	}
}
