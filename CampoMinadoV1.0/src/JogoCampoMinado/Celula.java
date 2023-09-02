package JogoCampoMinado;

import java.util.ArrayList;

import javax.swing.JButton;

public class Celula extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<JButton> vizinhas;
	
	public Celula() {
		vizinhas = new ArrayList<JButton>();
	}	

	public ArrayList<JButton> getVizinhas() {
		return vizinhas;
	}

	public void setVizinhas(ArrayList<JButton> vizinhas) {
		this.vizinhas = vizinhas;
	}
	
	public void addVizinha(JButton vizinha) {
		this.vizinhas.add(vizinha);
	}

}
