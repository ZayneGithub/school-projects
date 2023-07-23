import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class File_Hash_Panel extends JPanel implements ActionListener {
	
	ArrayList<String> hashes = new ArrayList<String>();
	JLabel filePathLabel;
	JLabel algorithmLabel;
	JLabel resultLabel;
	JLabel filePath;
	JButton browse;
	JButton md5;
	JButton sha1;
	JButton sha256;
	JButton clear;
	JFileChooser jfc;
	JList<String> list;
	DefaultListModel<String> listModel;
	JScrollPane scrollPane;

	File_Hash_Panel() {

		setLayout(null);

		filePathLabel = new JLabel("Select a file:");
		filePathLabel.setBounds(10, 10, 95, 25);
		add(filePathLabel);

		filePath = new JLabel();
		filePath.setBounds(220, 10, 500, 25);
		add(filePath);

		browse = new JButton("Browse");
		browse.setBounds(100, 10, 100, 25);
		browse.addActionListener(this);
		add(browse);

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
		resultLabel.setBounds(10, 100, 730, 25);
		add(resultLabel);

		jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select a file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		add(jfc);

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

		if (e.getSource() == browse) {
			getFile();
		}
		if (e.getSource() == clear) {
			clearList();
			resetFileOptions();
		}
		if (e.getSource() == md5) {
			hashFile("MD5");
		}
		if (e.getSource() == sha1) {
			hashFile("SHA-1");
		}
		if (e.getSource() == sha256) {
			hashFile("SHA-256");
		}

	}

	void getFile() {

		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			filePath.setText(jfc.getSelectedFile().toString());
		} else {
			resultLabel.setForeground(Color.red);
			resultLabel.setText("Invalid file selection/unreadable file, try again. ");
		}

	}

	void hashFile(String algorithm) {

		try {
			String path = filePath.getText();
			if (path == "") {
				resultLabel.setForeground(Color.red);
				resultLabel.setText("Select a file.");
			} else {
				File file = new File(path);
				FileInputStream is = new FileInputStream(file);
				MessageDigest md = MessageDigest.getInstance(algorithm);
				byte[] bytesBuffer = new byte[1024];
				int bytesRead = -1;
				while ((bytesRead = is.read(bytesBuffer)) != -1) {
					md.update(bytesBuffer, 0, bytesRead);
				}
				byte[] hashedBytes = md.digest();
				is.close();
				String hash = Main.convertByteArrayToHexString(hashedBytes);
				if (hashes.contains(hash)) {
					resultLabel.setForeground(Color.red);
					resultLabel.setText("This file has already been hashed at index: " + hashes.indexOf(hash));
				} else {
					hashes.add(hash);
					resultLabel.setForeground(Color.green);
					resultLabel.setText("Hashed...");
					listModel.addElement(hashes.indexOf(hash) + " | " + file.getName() + " | " + algorithm + " | " + hash);
				}
			}
		} catch (Exception e) {
			resultLabel.setForeground(Color.red);
			resultLabel.setText("Invalid file selection/unreadable file, try again. ");
		}

	}

	void clearList() {
		
		resultLabel.setForeground(Color.green);
		resultLabel.setText("Cleared...");
		listModel.removeAllElements();
		hashes.clear();

	}
	
	void resetFileOptions() {

		filePath.setText("");

	}

}