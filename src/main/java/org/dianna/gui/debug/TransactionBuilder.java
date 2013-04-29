package org.dianna.gui.debug;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.dianna.core.Dianna;
import org.dianna.core.entity.AuxData;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.utils.CryptoUtil;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;

public class TransactionBuilder extends JPanel {
	private JTextField txtDomain;
	private JTextField txtValue;
	private JTextField txtBlockHash;
	private JTextField txtBitcoinHash;

	
	private Dianna dianna;
	private ECKey key = new ECKey();

	/**
	 * Create the panel.
	 */
	public TransactionBuilder(final Dianna dianna) {
		this.dianna = dianna;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblDomain = new JLabel("Domain");
		GridBagConstraints gbc_lblDomain = new GridBagConstraints();
		gbc_lblDomain.insets = new Insets(0, 0, 5, 5);
		gbc_lblDomain.anchor = GridBagConstraints.EAST;
		gbc_lblDomain.gridx = 1;
		gbc_lblDomain.gridy = 1;
		add(lblDomain, gbc_lblDomain);

		txtDomain = new JTextField();
		txtDomain.setText("domain");
		GridBagConstraints gbc_txtDomain = new GridBagConstraints();
		gbc_txtDomain.insets = new Insets(0, 0, 5, 0);
		gbc_txtDomain.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDomain.gridx = 2;
		gbc_txtDomain.gridy = 1;
		add(txtDomain, gbc_txtDomain);
		txtDomain.setColumns(10);

		JLabel lblValue = new JLabel("Value");
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.anchor = GridBagConstraints.EAST;
		gbc_lblValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblValue.gridx = 1;
		gbc_lblValue.gridy = 2;
		add(lblValue, gbc_lblValue);

		txtValue = new JTextField();
		txtValue.setText("value");
		GridBagConstraints gbc_txtValue = new GridBagConstraints();
		gbc_txtValue.insets = new Insets(0, 0, 5, 0);
		gbc_txtValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValue.gridx = 2;
		gbc_txtValue.gridy = 2;
		add(txtValue, gbc_txtValue);
		txtValue.setColumns(10);

		JLabel lblBlockHash = new JLabel("Block hash");
		GridBagConstraints gbc_lblBlockHash = new GridBagConstraints();
		gbc_lblBlockHash.insets = new Insets(0, 0, 5, 5);
		gbc_lblBlockHash.gridx = 1;
		gbc_lblBlockHash.gridy = 3;
		add(lblBlockHash, gbc_lblBlockHash);

		txtBlockHash = new JTextField();
		txtBlockHash.setText("Block hash");
		GridBagConstraints gbc_txtBlockHash = new GridBagConstraints();
		gbc_txtBlockHash.insets = new Insets(0, 0, 5, 0);
		gbc_txtBlockHash.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBlockHash.gridx = 2;
		gbc_txtBlockHash.gridy = 4;
		add(txtBlockHash, gbc_txtBlockHash);
		txtBlockHash.setColumns(10);

		JButton btnNewButton = new JButton("Add transaction");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DomainTransaction t = new DomainTransaction();
				t.setDomain(txtDomain.getText());
				t.setValue(txtValue.getText());
				t.setFeeTransaction(Sha256Hash.ZERO_HASH);
				t.setNextPubkey(key);
				t.setSignature(CryptoUtil.signTransaction(t, key));
				dianna.addTransaction(t);
				txtBlockHash.setText(dianna.getBlockHash());

			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 5;
		add(btnNewButton, gbc_btnNewButton);

		txtBitcoinHash = new JTextField();
		txtBitcoinHash.setText("Bitcoin hash");
		GridBagConstraints gbc_txtBitcoinHash = new GridBagConstraints();
		gbc_txtBitcoinHash.insets = new Insets(0, 0, 5, 0);
		gbc_txtBitcoinHash.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBitcoinHash.gridx = 2;
		gbc_txtBitcoinHash.gridy = 6;
		add(txtBitcoinHash, gbc_txtBitcoinHash);
		txtBitcoinHash.setColumns(10);

		JButton btnSendBlock = new JButton("Send block");
		btnSendBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AuxData auxData = new AuxData();
				auxData.setParentBlockHash(new Sha256Hash(txtBitcoinHash.getText()));
				dianna.broadcastBlock(auxData);
			}
		});
		GridBagConstraints gbc_btnSendBlock = new GridBagConstraints();
		gbc_btnSendBlock.gridx = 2;
		gbc_btnSendBlock.gridy = 7;
		add(btnSendBlock, gbc_btnSendBlock);

	}

}
