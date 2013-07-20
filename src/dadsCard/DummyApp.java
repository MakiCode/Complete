package src.dadsCard;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DummyApp extends JFrame{
	

	public static void main(String[] args) {
		DummyApp t = new DummyApp();
		
		t.buildUI();
		t.pack();
		t.setVisible(true);
	}

	public void buildUI() {
		// default layout manager is flow layout
		JPanel bottom = new JPanel();
		JButton first = new JButton();
		bottom.add(first);
		add(bottom, BorderLayout.SOUTH);
	}
}
