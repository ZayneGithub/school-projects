import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Menu_Panel extends JPanel implements ActionListener {

	JLabel label;
	JButton stringHashBtn;
	JButton fileHashBtn;
	JButton dirHashBtn;
	JButton hashMatchBtn;

	Menu_Panel() {

		setLayout(null);

		label = new JLabel("Hashing Program", SwingConstants.CENTER);
		label.setBounds(0, 40, 500, 25);
		label.setFont(new Font("Verdana", Font.PLAIN, 20));
		add(label);

		stringHashBtn = new JButton("String Hashing");
		stringHashBtn.setBounds(0, 100, 500, 75);
		stringHashBtn.setFont(new Font("Verdana", Font.PLAIN, 15));
		stringHashBtn.addActionListener(this);
		add(stringHashBtn);

		fileHashBtn = new JButton("File Hashing");
		fileHashBtn.setBounds(0, 180, 500, 75);
		fileHashBtn.setFont(new Font("Verdana", Font.PLAIN, 15));
		fileHashBtn.addActionListener(this);
		add(fileHashBtn);

		dirHashBtn = new JButton("Directory Hashing");
		dirHashBtn.setBounds(0, 260, 500, 75);
		dirHashBtn.setFont(new Font("Verdana", Font.PLAIN, 15));
		dirHashBtn.addActionListener(this);
		add(dirHashBtn);

		hashMatchBtn = new JButton("Hash Matching");
		hashMatchBtn.setBounds(0, 340, 500, 75);
		hashMatchBtn.setFont(new Font("Verdana", Font.PLAIN, 15));
		hashMatchBtn.addActionListener(this);
		add(hashMatchBtn);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == stringHashBtn) {
			JFrame frame = new JFrame("String Hashing");
			frame.setSize(750, 325);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.add(new String_Hash_Panel());
			frame.setResizable(false);
			frame.setVisible(true);
		}
		if (e.getSource() == fileHashBtn) {
			JFrame frame = new JFrame("File Hashing");
			frame.setSize(750, 325);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.add(new File_Hash_Panel());
			frame.setResizable(false);
			frame.setVisible(true);
		}
		if (e.getSource() == dirHashBtn) {
			JFrame frame = new JFrame("Directory Hashing");
			frame.setSize(750, 325);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.add(new Directory_Hash_Panel());
			frame.setResizable(false);
			frame.setVisible(true);
		}
		if (e.getSource() == hashMatchBtn) {
			JFrame frame = new JFrame("Hash Matching");
			frame.setSize(750, 500);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.add(new Hash_Matching_Panel());
			frame.setResizable(false);
			frame.setVisible(true);
		}

	}

}
