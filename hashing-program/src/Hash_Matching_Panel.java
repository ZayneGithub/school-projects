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
public class Hash_Matching_Panel extends JPanel implements ActionListener {

	ArrayList<String> hashes = new ArrayList<String>();
	ArrayList<Integer> matches = new ArrayList<Integer>();
	JLabel directoryPathLabel;
	JLabel directoryAlgorithmLabel;
	JLabel directoryResultLabel;
	JLabel directoryPath;
	JLabel filePathLabel;
	JLabel fileAlgorithmLabel;
	JLabel fileResultLabel;
	JLabel fileHash;
	JLabel filePath;
	JButton directoryBrowse;
	JButton directoryMD5;
	JButton directorySHA1;
	JButton directorySHA256;
	JButton fileBrowse;
	JButton fileMD5;
	JButton fileSHA1;
	JButton fileSHA256;
	JButton clear;
	JFileChooser directoryJFC;
	JFileChooser fileJFC;
	JList<String> list;
	DefaultListModel<String> listModel;
	JScrollPane scrollPane;

	Hash_Matching_Panel() {

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

		filePathLabel = new JLabel("Select a file:");
		filePathLabel.setBounds(10, 300, 95, 25);
		add(filePathLabel);
		filePathLabel.setVisible(false);

		filePath = new JLabel();
		filePath.setBounds(220, 300, 500, 25);
		add(filePath);
		filePath.setVisible(false);

		fileBrowse = new JButton("Browse");
		fileBrowse.setBounds(100, 300, 100, 25);
		fileBrowse.addActionListener(this);
		add(fileBrowse);
		fileBrowse.setVisible(false);

		clear = new JButton("Clear");
		clear.setBounds(655, 100, 70, 25);
		clear.addActionListener(this);
		add(clear);

		directoryAlgorithmLabel = new JLabel("Select a hashing algorithm: ");
		directoryAlgorithmLabel.setBounds(10, 50, 200, 25);
		add(directoryAlgorithmLabel);

		fileAlgorithmLabel = new JLabel("Select a hashing algorithm: ");
		fileAlgorithmLabel.setBounds(10, 340, 200, 25);
		add(fileAlgorithmLabel);
		fileAlgorithmLabel.setVisible(false);

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

		fileMD5 = new JButton("MD5");
		fileMD5.setBounds(190, 340, 70, 25);
		fileMD5.addActionListener(this);
		add(fileMD5);
		fileMD5.setVisible(false);
		fileSHA1 = new JButton("SHA-1");
		fileSHA1.setBounds(270, 340, 70, 25);
		fileSHA1.addActionListener(this);
		add(fileSHA1);
		fileSHA1.setVisible(false);
		fileSHA256 = new JButton("SHA-256");
		fileSHA256.setBounds(350, 340, 90, 25);
		fileSHA256.addActionListener(this);
		add(fileSHA256);
		fileSHA256.setVisible(false);

		directoryResultLabel = new JLabel();
		directoryResultLabel.setBounds(10, 100, 730, 25);
		add(directoryResultLabel);

		fileResultLabel = new JLabel();
		fileResultLabel.setBounds(10, 405, 730, 25);
		add(fileResultLabel);
		fileResultLabel.setVisible(false);

		fileHash = new JLabel();
		fileHash.setBounds(10, 390, 730, 25);
		add(fileHash);
		fileHash.setVisible(false);

		directoryJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		directoryJFC.setDialogTitle("Select a directory: ");
		directoryJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		add(directoryJFC);

		fileJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileJFC.setDialogTitle("Select a file: ");
		fileJFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
		add(fileJFC);
		fileJFC.setVisible(false);

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
		if (e.getSource() == fileBrowse) {
			getFile();
		}
		if (e.getSource() == clear) {
			clearList();
			resetDirectoryOptions();
			hideFileOptions();
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
		if (e.getSource() == fileMD5) {
			hashFile("MD5");
		}
		if (e.getSource() == fileSHA1) {
			hashFile("SHA-1");
		}
		if (e.getSource() == fileSHA256) {
			hashFile("SHA-256");
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

	void getFile() {

		if (fileJFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			filePath.setText(fileJFC.getSelectedFile().toString());
		} else {
			fileResultLabel.setForeground(Color.red);
			fileResultLabel.setText("Invalid file selection/unreadable file, try again. ");
		}

	}

	void hashDirectory(String algorithm) {

		try {
			clearList();
			resetFileOptions();
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
				} else {
					displayFileOptions();
				}
			}
		} catch (Exception e) {
			directoryResultLabel.setForeground(Color.red);
			directoryResultLabel.setText("Invalid entry/unreadable directory, try again. ");
		}

	}

	void hashFile(String algorithm) {

		try {
			matches.clear();
			String path = filePath.getText();
			if (path == "") {
				fileResultLabel.setForeground(Color.red);
				fileResultLabel.setText("Select a file.");
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
				fileHash.setText(hash);
				matchHashes(hash);
			}
		} catch (Exception e) {
			fileResultLabel.setForeground(Color.red);
			fileResultLabel.setText("Invalid file selection/unreadable file, try again. ");
		}

	}

	void matchHashes(String hash) {

		for (int i = 0; i < hashes.size(); i++) {
			if (hashes.get(i).contains(hash)) {
				if (!matches.contains(i)) {
					matches.add(i);
				}
			}
		}
		if (!matches.isEmpty()) {
			fileResultLabel.setForeground(Color.green);
			fileResultLabel.setText("This file hash matches at indicies: " + matches);
		} else {
			fileResultLabel.setForeground(Color.red);
			fileResultLabel.setText("This file hash has no matches.");
		}

	}

	void clearList() {

		directoryResultLabel.setForeground(Color.green);
		directoryResultLabel.setText("Cleared...");
		listModel.removeAllElements();
		matches.clear();
		hashes.clear();

	}

	void displayFileOptions() {

		filePathLabel.setVisible(true);
		filePath.setVisible(true);
		fileBrowse.setVisible(true);
		fileAlgorithmLabel.setVisible(true);
		fileMD5.setVisible(true);
		fileSHA1.setVisible(true);
		fileSHA256.setVisible(true);
		fileJFC.setVisible(true);
		fileResultLabel.setVisible(true);
		fileHash.setVisible(true);

	}

	void hideFileOptions() {

		filePathLabel.setVisible(false);
		filePath.setVisible(false);
		fileBrowse.setVisible(false);
		fileAlgorithmLabel.setVisible(false);
		fileMD5.setVisible(false);
		fileSHA1.setVisible(false);
		fileSHA256.setVisible(false);
		fileJFC.setVisible(false);
		fileResultLabel.setVisible(false);
		fileHash.setVisible(false);
		resetFileOptions();

	}
	
	void resetDirectoryOptions() {

		directoryPath.setText("");

	}
	
	void resetFileOptions() {

		filePath.setText("");
		fileResultLabel.setText("");
		fileHash.setText("");

	}

}