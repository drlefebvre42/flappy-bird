package flappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Render extends JPanel {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		//this is called from the jpanel
		super.paintComponent(g);
		
		//to repaint our flappybird bird program..
		flappyBird.flappyBird.repaint(g);
	}


}
