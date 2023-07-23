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
public class Directory_Hash_Panel extends JPanel implements ActionListener {

	ArrayList<String> hashes = new ArrayList<String>();
	JLabel directoryPathLabel;
	JLabel directoryAlgorithmLabel;
	JLabel directoryResultLabel;
	JLabel directoryPath;
	JButton directoryBrowse;
	JButton directoryMD5;
	JButton directorySHA1;
	JButton directorySHA256;
	JButton clear;
	JFileChooser directoryJFC;
	JList<String> list;
	DefaultListModel<String> listModel;
	JScrollPane scrollPane;

	Directory_Hash_Panel() {

		setLayout(null);

		directoryPathLabel = new JLabel("Select a directory:");
		directoryPathLabel.setBounds(10, 10, 115, 25);
		add(directoryPathLabel);

		directoryPath = new JLabel();
		directoryPath.setBounds(245, 10, 485, 25);
		add(directoryPath);

		directoryBrowse = new JButton("Browse");
		directoryBrowse.setBounds(135, 10, 100, 25);
		directoryBrowse.addActionListener(this);
		add(directoryBrowse);

		clear = new JButton("Clear");
		clear.setBounds(655, 100, 70, 25);
		clear.addActionListener(this);
		add(clear);

		directoryAlgorithmLabel = new JLabel("Select a hashing algorithm: ");
		directoryAlgorithmLabel.setBounds(10, 50, 200, 25);
		add(directoryAlgorithmLabel);

		directoryMD5 = new JButton("MD5");
		directoryMD5.setBounds(190, 50, 70, 25);
		directoryMD5.addActionListener(this);
		add(directoryMD5);
		directorySHA1 = new JButton("SHA-1");
		directorySHA1.setBounds(270, 50, 70, 25);
		directorySHA1.addActionListener(this);
		add(directorySHA1);
		directorySHA256 = new JButton("SHA-256");
		directorySHA256.setBounds(350, 50, 90, 25);
		directorySHA256.addActionListener(this);
		add(directorySHA256);

		directoryResultLabel = new JLabel();
		directoryResultLabel.setBounds(10, 100, 730, 25);
		add(directoryResultLabel);

		directoryJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		directoryJFC.setDialogTitle("Select a directory: ");
		directoryJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		add(directoryJFC);

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

		if (e.getSource() == directoryBrowse) {
			getDirectory();
		}
		if (e.getSource() == clear) {
			clearList();
			resetDirectoryOptions();
		}
		if (e.getSource() == directoryMD5) {
			hashDirectory("MD5");
		}
		if (e.getSource() == directorySHA1) {
			hashDirectory("SHA-1");
		}
		if (e.getSource() == directorySHA256) {
			hashDirectory("SHA-256");
		}

	}

	void getDirectory() {

		if (directoryJFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			directoryPath.setText(directoryJFC.getSelectedFile().toString());
		} else {
			directoryResultLabel.setForeground(Color.red);
			directoryResultLabel.setText("Invalid directory selection/unreadable directory, try again. ");
		}

	}

	void hashDirectory(String algorithm) {

		try {
			clearList();
			String path = directoryPath.getText();
			if (path == "") {
				directoryResultLabel.setForeground(Color.red);
				directoryResultLabel.setText("Select a directory.");
			} else {
				File directory = new File(path);
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile() & files[i].canRead()) {
						FileInputStream is = new FileInputStream(files[i]);
						MessageDigest md = MessageDigest.getInstance(algorithm);
						byte[] bytesBuffer = new byte[1024];
						int bytesRead = -1;
						while ((bytesRead = is.read(bytesBuffer)) != -1) {
							md.update(bytesBuffer, 0, bytesRead);
						}
						byte[] hashedBytes = md.digest();
						is.close();
						String hash = Main.convertByteArrayToHexString(hashedBytes);
						directoryResultLabel.setForeground(Color.green);
						directoryResultLabel.setText("Hashed...");
						hashes.add(hash);
						listModel.addElement(i + " | " + files[i].getName() + " | " + algorithm + " | " + hash);
					}
				}
				if (listModel.isEmpty()) {
					directoryResultLabel.setForeground(Color.red);
					directoryResultLabel.setText("This directory contains no files, try again.");
				}
			}
		} catch (Exception e) {
			directoryResultLabel.setForeground(Color.red);
			directoryResultLabel.setText("Invalid entry/unreadable directory, try again. ");
		}

	}

	void clearList() {

		directoryResultLabel.setForeground(Color.green);
		directoryResultLabel.setText("Cleared...");
		listModel.removeAllElements();
		hashes.clear();

	}

	void resetDirectoryOptions() {

		directoryPath.setText("");

	}

}