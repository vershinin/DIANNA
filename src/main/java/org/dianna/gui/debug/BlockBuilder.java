package org.dianna.gui.debug;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.dianna.core.Dianna;
import org.dianna.core.crypto.CryptoUtil;
import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;

public class BlockBuilder extends JPanel {
	private Dianna dianna;
	private JTextField domain;
	private JTextField value;
	private JTextField blockHash;

	private ECKey key = new ECKey();

	public BlockBuilder(Dianna dianna) {
		this.dianna = dianna;
		setLayout(new GridBagLayout());

		
		initComponents();
	}

	private void initComponents() {
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		
		domain = new JTextField();
		add(domain);

		value = new JTextField();
		add(value);

		blockHash = new JTextField();
		add(blockHash);

		JButton add = new JButton("Add transaction");
		add(add);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});

	}
}
