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
import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Chat extends JFrame {

	protected JPanel contentPane;
	protected JTextField txtMensagem;
	protected JComboBox cbxDestino;
	protected JTextPane painelMensagens;
	protected DefaultListModel modelo;
	protected String nomeUsuario;

	/**
	 * Create the frame.
	 */
	public Chat(JanelaDeEscolha escolha, String nomeSala, String nomeUsuario, ObjectOutputStream transmissor) {
		setTitle("Chat - Sala conectada: " + nomeSala);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 525);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.nomeUsuario = nomeUsuario;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				try
				{
					transmissor.writeObject(new PedidoParaSairDaSala());
					transmissor.flush();
					escolha.setVisible(true);
				}
				catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
			}
		});
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		cbxDestino = new JComboBox();
		cbxDestino.setModel(new DefaultComboBoxModel(new String[] {"Mensagem Geral                           "}));
		cbxDestino.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		panel.add(cbxDestino, BorderLayout.WEST);
		
		txtMensagem = new JTextField();
		panel.add(txtMensagem, BorderLayout.CENTER);
		txtMensagem.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					ArrayList<String> destino = new ArrayList<String>();
					if (cbxDestino.getSelectedIndex() == 0)				
						for (int i = 1; i < cbxDestino.getItemCount(); i++)
							destino.add(cbxDestino.getItemAt(i).toString());
					else
						destino.add(cbxDestino.getSelectedItem().toString());

					destino.add(nomeUsuario);

					transmissor.writeObject(new Mensagem(txtMensagem.getText(), destino));
					transmissor.flush();

					txtMensagem.setText("");
				}
				catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
			}
		});
		btnEnviar.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel.add(btnEnviar, BorderLayout.EAST);

		txtMensagem.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					btnEnviar.doClick();
            }
        });
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUsuriosConectados = new JLabel("   Usu\u00E1rios Conectados   ");
		lblUsuriosConectados.setForeground(Color.WHITE);
		lblUsuriosConectados.setFont(new Font("Century Gothic", Font.BOLD, 23));
		panel_1.add(lblUsuriosConectados, BorderLayout.NORTH);
		
		modelo = new DefaultListModel();

		JList listUsuarios = new JList(modelo);
		listUsuarios.setFont(new Font("Century Gothic", Font.PLAIN, 19));
		listUsuarios.setVisibleRowCount(15);
		listUsuarios.setBackground(Color.LIGHT_GRAY);
		panel_1.add(listUsuarios, BorderLayout.CENTER);

		modelo.addElement(nomeUsuario);
		
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
		
		painelMensagens = new JTextPane();
		painelMensagens.setEditable(false);
		painelMensagens.setContentType("text/html");
		painelMensagens.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		painelMensagens.setText("<html><body></body></html>");
		panel_2.add(painelMensagens, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnSairDaSala = new JButton("Sair da sala");
		btnSairDaSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					transmissor.writeObject(new PedidoParaSairDaSala());
					transmissor.flush();
					escolha.setVisible(true);
					dispose();
				}
				catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
			}
		});
		btnSairDaSala.setBackground(Color.LIGHT_GRAY);
		btnSairDaSala.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel_3.add(btnSairDaSala, BorderLayout.EAST);
	}

	protected String ultimoUsuario = null;
	public void receber(Enviavel recebido)
	{
		String texto = recebido.toString();
		if (recebido instanceof Mensagem)
		{
			if (ultimoUsuario != null && ultimoUsuario.equals(recebido.getUsuario()))
				texto = ((Mensagem)recebido).getMensagem();
			if (recebido.getUsuario().equals(this.nomeUsuario))
				texto =  "<span style=\"text-align: right\">" + texto + "</span>";
			ultimoUsuario = recebido.getUsuario();
		}
		painelMensagens.setText("<html><body>" + painelMensagens.getText().substring(15, painelMensagens.getText().length() - 17) + recebido.toString() + "</body></html><br>");
		if (recebido instanceof AvisoDeEntradaNaSala && !recebido.getUsuario().equals(this.nomeUsuario))
		{
			modelo.addElement(recebido.getUsuario());
			cbxDestino.addItem(recebido.getUsuario());
		}
		else if (recebido instanceof AvisoDeSaidaDaSala)
		{
			for (int i = 0; i < cbxDestino.getItemCount(); i++)
				if (cbxDestino.getItemAt(i).equals(recebido.getUsuario()))
				{
					cbxDestino.removeItemAt(i);
					modelo.remove(i);
				}
		}
	}
}