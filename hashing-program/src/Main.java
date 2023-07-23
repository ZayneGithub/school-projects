import javax.swing.*;

public class Main {

	public static void main(String[] args) {

		JFrame frame = new JFrame("Hashing Program");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Menu_Panel());
		frame.setResizable(false);
		frame.setVisible(true);

	}

	public static String convertByteArrayToHexString(byte[] hashedBytes) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashedBytes.length; i++) {
			sb.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();

	}

}
