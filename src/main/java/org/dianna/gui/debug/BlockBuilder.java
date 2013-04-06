package org.dianna.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.dianna.core.Dianna;

public class BlockBuilder extends JPanel {
	private Dianna dianna;

	public BlockBuilder() {
	}

	private void initComponents() {
		JButton connect = new JButton("Add transaction");
		add(connect);
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// dianna.addTransaction(jsonTransaction);
			}
		});
	}
}
