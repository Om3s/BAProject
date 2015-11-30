package mvc.model;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * 
 * @author Jonas Ostergaard
 *
 * @param <E>
 */
public class MyJList<E> extends JList<E> {
	
	public MyJList(DefaultListModel<E> dLM){
		super(dLM);
	}
}
