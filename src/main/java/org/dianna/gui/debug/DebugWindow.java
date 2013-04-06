package org.dianna.gui.debug;

import java.awt.FlowLayout;
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
		setLayout(new FlowLayout());
		add(createText());
		add(createPanel());
	}

	private JPanel createText() {
		JPanel panel = new JPanel();
		panel.add(new JTextArea(10, 10));
		return panel;
	}

	private JPanel createPanel() {
		JPanel p = new JPanel();
		JButton connect = new JButton("Connect");
		JButton addTransaction = new JButton("Get hash");
		JButton cancel = new JButton("Add transaction");
		p.add(connect);
		p.add(addTransaction);
		p.add(cancel);

		getRootPane().setDefaultButton(connect);
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dianna.connect();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		return p;
	}
}
