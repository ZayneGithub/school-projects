import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class String_Hash_Panel extends JPanel implements ActionListener {

	ArrayList<String> hashes = new ArrayList<String>();
	JLabel messageLabel;
	JLabel algorithmLabel;
	JLabel resultLabel;
	JTextField messageText;
	JButton clear;
	JButton md5;
	JButton sha1;
	JButton sha256;
	JList<String> list;
	DefaultListModel<String> listModel;
	JScrollPane scrollPane;

	String_Hash_Panel() {

		setLayout(null);

		messageLabel = new JLabel("Enter a string:");
		messageLabel.setBounds(10, 10, 80, 25);
		add(messageLabel);

		messageText = new JTextField(50);
		messageText.setBounds(100, 10, 530, 25);
		add(messageText);

		clear = new JButton("Clear");
		clear.setBounds(655, 100, 70, 25);
		clear.addActionListener(this);
		add(clear);

		algorithmLabel = new JLabel("Select a hashing algorithm: ");
		algorithmLabel.setBounds(10, 50, 200, 25);
		add(algorithmLabel);

		md5 = new JButton("MD5");
		md5.setBounds(190, 50, 70, 25);
		md5.addActionListener(this);
		add(md5);
		sha1 = new JButton("SHA-1");
		sha1.setBounds(270, 50, 70, 25);
		sha1.addActionListener(this);
		add(sha1);
		sha256 = new JButton("SHA-256");
		sha256.setBounds(350, 50, 90, 25);
		sha256.addActionListener(this);
		add(sha256);

		resultLabel = new JLabel();
		resultLabel.setBounds(10, 100, 480, 25);
		add(resultLabel);

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 130, 725, 150);
		add(scrollPane);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == clear) {
			clearList();
			resetMessageOptions();
		}
		if (e.getSource() == md5) {
			hashString("MD5");
		}
		if (e.getSource() == sha1) {
			hashString("SHA-1");
		}
		if (e.getSource() == sha256) {
			hashString("SHA-256");
		}

	}

	void hashString(String algorithm) {

		try {
			String message = messageText.getText();
			if (message.isEmpty()) {
				resultLabel.setForeground(Color.red);
				resultLabel.setText("Enter a string.");
			} else {
				MessageDigest md = MessageDigest.getInstance(algorithm);
				byte[] hashedBytes = md.digest(message.getBytes("UTF-8"));
				String hash = Main.convertByteArrayToHexString(hashedBytes);
				if (hashes.contains(hash)) {
					resultLabel.setForeground(Color.red);
					resultLabel.setText("This string has already been hashed at index: " + hashes.indexOf(hash));
				} else {
					hashes.add(hash);
					resultLabel.setForeground(Color.green);
					resultLabel.setText("Hashed...");
					listModel.addElement(hashes.indexOf(hash) + " | " + message + " | " + algorithm + " | " + hash);
				}
			}
		} catch (Exception e) {
			resultLabel.setForeground(Color.red);
			resultLabel.setText("Invalid entry, try again.");
		}

	}

	void clearList() {

		resultLabel.setForeground(Color.green);
		resultLabel.setText("Cleared...");
		listModel.removeAllElements();
		hashes.clear();

	}
	
	void resetMessageOptions() {

		messageText.setText("");

	}

}