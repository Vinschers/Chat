import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JanelaDeEscolha extends JFrame {

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtIP;
	
	protected boolean estaEmTesteSemConexao = false;
	
	protected Socket conexao;
	protected ObjectInputStream receptor;
	protected Chat chat = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JanelaDeEscolha frame = new JanelaDeEscolha();
			frame.setVisible(true);
			while (true)
			{
				frame.receber();
				Thread.sleep(100);
			}
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
		setBounds(100, 100, 599, 193);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStatus = new JLabel("Digite o IP do Servidor");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Century Gothic", Font.BOLD, 16));
		contentPane.add(lblStatus, BorderLayout.NORTH);
		
		JanelaDeEscolha este = this;
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblDigiteOIp = new JLabel("IP do Servidor:");
		lblDigiteOIp.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDigiteOIp = new GridBagConstraints();
		gbc_lblDigiteOIp.anchor = GridBagConstraints.EAST;
		gbc_lblDigiteOIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblDigiteOIp.gridx = 0;
		gbc_lblDigiteOIp.gridy = 0;
		panel.add(lblDigiteOIp, gbc_lblDigiteOIp);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		txtIP = new JTextField();
		txtIP.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		panel_1.add(txtIP, BorderLayout.CENTER);
		txtIP.setColumns(10);
		
		JLabel lblSelecioneUmaSala = new JLabel("Selecione uma sala:");
		lblSelecioneUmaSala.setEnabled(false);
		lblSelecioneUmaSala.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_lblSelecioneUmaSala = new GridBagConstraints();
		gbc_lblSelecioneUmaSala.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelecioneUmaSala.anchor = GridBagConstraints.EAST;
		gbc_lblSelecioneUmaSala.gridx = 0;
		gbc_lblSelecioneUmaSala.gridy = 1;
		panel.add(lblSelecioneUmaSala, gbc_lblSelecioneUmaSala);
		
		JComboBox cbxSalas = new JComboBox();
		cbxSalas.setEnabled(false);
		cbxSalas.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_cbxSalas = new GridBagConstraints();
		gbc_cbxSalas.insets = new Insets(0, 0, 5, 0);
		gbc_cbxSalas.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxSalas.gridx = 1;
		gbc_cbxSalas.gridy = 1;
		panel.add(cbxSalas, gbc_cbxSalas);
		
		JLabel lblDigiteSeuNome = new JLabel("Digite seu nome:");
		lblDigiteSeuNome.setEnabled(false);
		lblDigiteSeuNome.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDigiteSeuNome = new GridBagConstraints();
		gbc_lblDigiteSeuNome.anchor = GridBagConstraints.EAST;
		gbc_lblDigiteSeuNome.insets = new Insets(0, 0, 0, 5);
		gbc_lblDigiteSeuNome.gridx = 0;
		gbc_lblDigiteSeuNome.gridy = 2;
		panel.add(lblDigiteSeuNome, gbc_lblDigiteSeuNome);


		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Object recebido = null;

					ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());

					String nomeSala = cbxSalas.getSelectedItem().toString().split("\"")[1];

					transmissor.writeObject(txtNome.getText());
					transmissor.writeObject(nomeSala);
					transmissor.flush();
					
					recebido = receptor.readObject();

					if (!recebido.equals("ok"))
						JOptionPane.showMessageDialog(null, recebido.toString());
					else
					{		
						chat = new Chat(este, nomeSala, transmissor);
						chat.setVisible(true);
						setVisible(false);
					}
				}
				catch (Exception ex) {JOptionPane.showMessageDialog(null, "Deu esse erro: " + ex.getMessage());}
			}
		});
		btnEntrar.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		btnEntrar.setEnabled(false);
		contentPane.add(btnEntrar, BorderLayout.SOUTH);
		
		txtNome = new JTextField();
		txtNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				btnEntrar.setEnabled(txtNome.getText() != null && !txtNome.getText().equals(""));
			}
		});
		txtNome.setEnabled(false);
		txtNome.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GridBagConstraints gbc_txtNome = new GridBagConstraints();
		gbc_txtNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNome.gridx = 1;
		gbc_txtNome.gridy = 2;
		panel.add(txtNome, gbc_txtNome);
		txtNome.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!estaEmTesteSemConexao)
					{
						conexao = new Socket(txtIP.getText(), 12345);
						receptor = new ObjectInputStream(conexao.getInputStream());

						SalasDisponiveis recebido = (SalasDisponiveis) receptor.readObject();
						ArrayList<SalaSerializable> salasDisponiveis = recebido.getSalas();

						JOptionPane.showMessageDialog(null, "Salas disponiveis obtidas: " + salasDisponiveis.toString());
						for (int i = 0; i < salasDisponiveis.size(); i++)
							cbxSalas.addItem(salasDisponiveis.get(i).toString());
					}
					else
					{
						cbxSalas.addItem("Sala 1                            0/10 lugares preenchidos");
						cbxSalas.addItem("Sala 2                            0/10 lugares preenchidos");
						cbxSalas.addItem("Sala 3                            0/10 lugares preenchidos");
					}
					lblStatus.setText("Servidor conectado. Selecione uma sala e digite seu nome para come?ar");
					
					lblDigiteOIp.setEnabled(false);
					txtIP.setEnabled(false);
					btnConectar.setEnabled(false);
				
					lblSelecioneUmaSala.setEnabled(true);
					cbxSalas.setEnabled(true);
					lblDigiteSeuNome.setEnabled(true);
					txtNome.setEnabled(true);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Detalhes: " + e.getMessage(), "Erro de conexao", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnConectar.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		panel_1.add(btnConectar, BorderLayout.EAST);
	}
	public void receber() throws Exception
	{
		if (chat != null)
		{
			System.out.println("Chat nao e mais nulo");
			Enviavel recebido = (Enviavel) receptor.readObject();
			System.out.println("Recebeu");
			chat.receber(recebido);
		}
	}

}
