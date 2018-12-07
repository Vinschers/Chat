import java.awt.*;
import java.awt.event.*;

import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;

import java.util.ArrayList;

public class JanelaDeEscolha extends JFrame {

	protected JPanel contentPane;
	protected JTextField txtNome;
	protected JComboBox cbxSalas;
	protected JButton btnEntrar;
	protected JLabel lblStatus;
	protected JLabel lblSelecioneUmaSala;
	protected JLabel lblDigiteSeuNome;
	
	protected boolean estaEmTesteSemConexao = false;
	
	protected Socket conexao;
	protected ObjectInputStream receptor;
	protected ObjectOutputStream transmissor;
	protected Chat chat = null;
	protected Receptor receptorClass;

	public final String ip = "localhost";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JanelaDeEscolha frame = new JanelaDeEscolha();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public JanelaDeEscolha() {
		setResizable(false);
		setFont(new Font("Century Gothic", Font.PLAIN, 12));
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 619, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		lblStatus = new JLabel("Escolha sua sala e seu nome");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Century Gothic", Font.BOLD, 16));
		contentPane.add(lblStatus, BorderLayout.NORTH);
		
		JanelaDeEscolha este = this;
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblSelecioneUmaSala = new JLabel("Selecione uma sala:");
		lblSelecioneUmaSala.setEnabled(false);
		lblSelecioneUmaSala.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_lblSelecioneUmaSala = new GridBagConstraints();
		gbc_lblSelecioneUmaSala.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelecioneUmaSala.anchor = GridBagConstraints.EAST;
		gbc_lblSelecioneUmaSala.gridx = 0;
		gbc_lblSelecioneUmaSala.gridy = 0;
		panel.add(lblSelecioneUmaSala, gbc_lblSelecioneUmaSala);
		
		cbxSalas = new JComboBox();
		cbxSalas.setEnabled(false);
		cbxSalas.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_cbxSalas = new GridBagConstraints();
		gbc_cbxSalas.insets = new Insets(0, 0, 5, 0);
		gbc_cbxSalas.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxSalas.gridx = 1;
		gbc_cbxSalas.gridy = 0;
		panel.add(cbxSalas, gbc_cbxSalas);
		
		lblDigiteSeuNome = new JLabel("Digite seu nome:");
		lblDigiteSeuNome.setEnabled(false);
		lblDigiteSeuNome.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDigiteSeuNome = new GridBagConstraints();
		gbc_lblDigiteSeuNome.anchor = GridBagConstraints.EAST;
		gbc_lblDigiteSeuNome.insets = new Insets(0, 0, 0, 5);
		gbc_lblDigiteSeuNome.gridx = 0;
		gbc_lblDigiteSeuNome.gridy = 1;
		panel.add(lblDigiteSeuNome, gbc_lblDigiteSeuNome);

		btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Object recebido = null;

					if (transmissor == null)
						transmissor = new ObjectOutputStream(conexao.getOutputStream());

					String nomeSala = cbxSalas.getSelectedItem().toString().split("\"")[1];

					transmissor.writeObject(txtNome.getText().trim());
					transmissor.writeObject(nomeSala);
					transmissor.flush();
					
					recebido = receptor.readObject();

					if (!recebido.equals("ok"))
						JOptionPane.showMessageDialog(null, recebido.toString());
					else
					{		
						chat = new Chat(este, nomeSala, txtNome.getText().trim(), transmissor);
						chat.setVisible(true);
						setVisible(false);
						receptorClass = new Receptor(chat, receptor);
						receptorClass.start();
					}
				}
				catch (Exception ex) 
				{
					if (ex.getMessage().equals("Connection reset by peer: socket write error"))
					{
						try
						{
							receptor.close();
							if (transmissor != null)
								transmissor.close();
							transmissor = null;
							conexao.close();
							txtNome.setText("");
							cbxSalas.removeAllItems();
	
							btnEntrar.setEnabled(false);
							lblDigiteSeuNome.setEnabled(false);
							txtNome.setEnabled(false);
							lblSelecioneUmaSala.setEnabled(false);
							cbxSalas.setEnabled(false);
	
							JOptionPane.showMessageDialog(null, "Servidor fechado. Tente novamente mais tarde!");
						}
						catch (Exception e) {JOptionPane.showMessageDialog(null, e.getMessage());}
					}
					else
						JOptionPane.showMessageDialog(null, "Deu esse erro: " + ex.getMessage());
				}
			}
		});
		btnEntrar.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		btnEntrar.setEnabled(false);
		contentPane.add(btnEntrar, BorderLayout.SOUTH);
		
		txtNome = new JTextField();
		((AbstractDocument) txtNome.getDocument()).setDocumentFilter(new LimitDocumentFilter(25));
		txtNome.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				btnEntrar.setEnabled(txtNome.getText().trim().length() > 0);
			}
			public void removeUpdate(DocumentEvent e) {
				btnEntrar.setEnabled(txtNome.getText().trim().length() > 0);
			}
			public void insertUpdate(DocumentEvent e) {
				btnEntrar.setEnabled(txtNome.getText().trim().length() > 0);
			}
		});

		txtNome.setEnabled(false);
		txtNome.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_txtNome = new GridBagConstraints();
		gbc_txtNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNome.gridx = 1;
		gbc_txtNome.gridy = 1;
		panel.add(txtNome, gbc_txtNome);
		txtNome.setColumns(10);

		try {
			conexao = new Socket(ip, 12345);
			receptor = new ObjectInputStream(conexao.getInputStream());

			SalasDisponiveis recebido = (SalasDisponiveis) receptor.readObject();
			ArrayList<SalaSerializable> salasDisponiveis = recebido.getSalas();

			for (int i = 0; i < salasDisponiveis.size(); i++)
				cbxSalas.addItem(salasDisponiveis.get(i).toString());
			lblStatus.setText("Servidor conectado. Selecione uma sala e digite seu nome para come\u00E7ar");
		
			lblSelecioneUmaSala.setEnabled(true);
			cbxSalas.setEnabled(true);
			lblDigiteSeuNome.setEnabled(true);
			txtNome.setEnabled(true);					
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Detalhes: " + e.getMessage(), "Erro de conex\00E3o", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void morra() throws Exception
	{
		receptorClass.morrer();
		receptor.close();
		conexao.close();
	}
	public void setDados(String nomeSala, String nome) throws Exception
	{
		for (int i = 0; i < cbxSalas.getItemCount(); i++)
			if (cbxSalas.getItemAt(i).toString().split("\"")[1].equals(nomeSala))
				cbxSalas.setSelectedIndex(i);
	
		lblSelecioneUmaSala.setEnabled(true);
		cbxSalas.setEnabled(true);
		lblDigiteSeuNome.setEnabled(true);
		txtNome.setEnabled(true);

		txtNome.setText(nome);

		btnEntrar.setEnabled(true);
	}

}
