import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat(new JanelaDeEscolha());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chat(JanelaDeEscolha escolha) {
		setTitle("Chat - Sala conectada: ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 525);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Mensagem Geral                           ", "DM: Usu\u00E1rio 1", "DM: Usu\u00E1rio 2", "DM: Usu\u00E1rio 3", "DM: Usu\u00E1rio 4", "DM: Usu\u00E1rio 5"}));
		comboBox.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		panel.add(comboBox, BorderLayout.WEST);
		
		textField = new JTextField();
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel.add(btnNewButton, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUsuriosConectados = new JLabel("   Usu\u00E1rios Conectados   ");
		lblUsuriosConectados.setForeground(Color.WHITE);
		lblUsuriosConectados.setFont(new Font("Century Gothic", Font.BOLD, 23));
		panel_1.add(lblUsuriosConectados, BorderLayout.NORTH);
		
		JList list = new JList();
		list.setFont(new Font("Century Gothic", Font.PLAIN, 19));
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Usu\u00E1rio 1", "Usu\u00E1rio 2", "Usu\u00E1rio 3", "Usu\u00E1rio 4"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBackground(Color.LIGHT_GRAY);
		panel_1.add(list, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel(" ");
		panel_2.add(label, BorderLayout.SOUTH);
		
		JLabel label_1 = new JLabel("     ");
		panel_2.add(label_1, BorderLayout.WEST);
		
		JLabel label_2 = new JLabel("     ");
		panel_2.add(label_2, BorderLayout.EAST);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		panel_2.add(textArea, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnSairDaSala = new JButton("Sair da sala");
		btnSairDaSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				escolha.setVisible(true);
				dispose();
			}
		});
		btnSairDaSala.setBackground(Color.LIGHT_GRAY);
		btnSairDaSala.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel_3.add(btnSairDaSala, BorderLayout.EAST);
	}

}
