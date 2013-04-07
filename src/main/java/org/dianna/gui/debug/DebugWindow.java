package org.dianna.gui.debug;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.dianna.core.Dianna;

public class DebugWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private Dianna dianna;

	public DebugWindow(Dianna dianna) {
		super("Dianna debug");
		this.dianna = dianna;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new TransactionBuilder(dianna));
	}
}
