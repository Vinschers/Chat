import java.awt.*;

import javax.swing.*; 
import javax.swing.text.*;
import javax.swing.text.html.*;

import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.*;
import java.util.Timer;

import java.lang.*;

public class Chat extends JFrame {

	protected JPanel contentPane;
	protected JTextField txtMensagem;
	protected JComboBox cbxDestino;
	protected JTextPane painelMensagens;
	protected JScrollPane scrollPane;
	protected JLabel lblEstaDigitando;
	protected JCheckBox chkApenasDMs;

	protected StyleSheet folhaDeEstilo;
	protected HTMLDocument documento;
	protected HTMLEditorKit editor;
	protected Element elementoBody;
  
	protected DefaultListModel modelo;
	protected String nomeUsuario;
	protected ObjectOutputStream transmissor;
	protected JanelaDeEscolha escolha;
	protected String nomeSala;

	protected ArrayList<Enviavel> recebidos;

	/**
	 * Create the frame.
	 */
	public Chat(JanelaDeEscolha escolha, String nomeSala, String nomeUsuario, ObjectOutputStream transmissor) {
		setTitle("Chat - Sala conectada: " + nomeSala);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 525);
		setMinimumSize(new Dimension(900, 300));
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.nomeUsuario = nomeUsuario;
		this.nomeSala = nomeSala;
		this.transmissor = transmissor;
		this.escolha = escolha;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				fechar(false, false);
			}
		});
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(new Color(30, 30, 30));
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		cbxDestino = new JComboBox();
		cbxDestino.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					chkApenasDMs.setVisible(cbxDestino.getSelectedIndex() != 0);
			}
		});
		cbxDestino.setModel(new DefaultComboBoxModel(new String[] {"Mensagem Geral                           "}));
		cbxDestino.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		panel.add(cbxDestino, BorderLayout.WEST);
		
		txtMensagem = new JTextField();
		panel.add(txtMensagem, BorderLayout.CENTER);
		txtMensagem.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtMensagem.getText().equals(""))
				{
					try
					{
						ArrayList<String> destino = new ArrayList<String>();
						if (cbxDestino.getSelectedIndex() == 0)
							for (int i = 0; i < cbxDestino.getItemCount(); i++)
								destino.add(cbxDestino.getItemAt(i).toString());
						else
						{
							destino.add("dm");
							destino.add(cbxDestino.getSelectedItem().toString());
						}
						destino.add(nomeUsuario);
	
						transmissor.writeObject(new Mensagem(txtMensagem.getText(), destino));
						transmissor.flush();
	
						txtMensagem.setText("");
					}
					catch (Exception ex) 
					{
						if (ex.getMessage().equals("Connection reset by peer: socket write error"))
						{

							JOptionPane.showMessageDialog(null, "Servidor fechado. Voltando ao menu...");
							fechar(true, true);
						}
						else
							JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			}
		});
		btnEnviar.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		btnEnviar.setEnabled(false);
		panel.add(btnEnviar, BorderLayout.EAST);

		txtMensagem.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				btnEnviar.setEnabled(txtMensagem.getText().trim().length() > 0);
			}
			public void removeUpdate(DocumentEvent e) {
				enviarDigitando();
				btnEnviar.setEnabled(txtMensagem.getText().trim().length() > 0);
			}
			public void insertUpdate(DocumentEvent e) {
				enviarDigitando();
				btnEnviar.setEnabled(txtMensagem.getText().trim().length() > 0);
			}
		});

		txtMensagem.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
				if (btnEnviar.isEnabled() && evt.getKeyCode() == KeyEvent.VK_ENTER)
					btnEnviar.doClick();			
            }
		});
		
		new Timer().scheduleAtFixedRate(new TimerTask(){
			public void run() {
				if (quandoRecebeuUltimoDigitando != null && (new Date().getTime() - quandoRecebeuUltimoDigitando.getTime()) > 2500)
					lblEstaDigitando.setText("   ");
			}
		}, 0, 500);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBackground(new Color(30, 30, 30));
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
		listUsuarios.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 2 && listUsuarios.getSelectedIndex() != 0) {
					cbxDestino.setSelectedIndex(listUsuarios.getSelectedIndex());			
				}
			}
		});
		panel_1.add(listUsuarios, BorderLayout.CENTER);

		modelo.addElement(nomeUsuario);

		chkApenasDMs = new JCheckBox("Exibir apenas DMs", false);
		chkApenasDMs.setForeground(Color.WHITE);
		chkApenasDMs.setBackground(new Color(30, 30, 30));
		chkApenasDMs.setVisible(false);
		chkApenasDMs.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (chkApenasDMs.isSelected())
					exibirDMs((String) cbxDestino.getSelectedItem());
				else
					exibirTodos();
			}
		});
		panel_1.add(chkApenasDMs, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(30, 30, 30));
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		lblEstaDigitando = new JLabel("    ");
		lblEstaDigitando.setForeground(Color.LIGHT_GRAY);
		panel_2.add(lblEstaDigitando, BorderLayout.SOUTH);
		
		JLabel label_1 = new JLabel("     ");
		panel_2.add(label_1, BorderLayout.WEST);
		
		JLabel label_2 = new JLabel("     ");
		panel_2.add(label_2, BorderLayout.EAST);
		
		this.folhaDeEstilo = new StyleSheet();
		this.editor = new HTMLEditorKit();

		this.folhaDeEstilo.addRule("body {background-color: #004b66; font-size: 16pt; font-family: Century Gothic; color: white; max-width: 500px; overflow-x: hidden;}");
		this.folhaDeEstilo.addRule("p {padding: 2px;} ");
		this.folhaDeEstilo.addRule(".geral {background-color: #00384c; color: white;} ");
		this.folhaDeEstilo.addRule(".dm {background-color: #262626; color: #bcbcbc;} ");
		this.folhaDeEstilo.addRule(".espaco {font-size: 3pt;}");
		this.folhaDeEstilo.addRule("center {text-align: center; font-weight: bold; font-size: 21pt; margin-bottom: 5px; margin-top: 5px;}");
		this.editor.setStyleSheet(this.folhaDeEstilo);
		this.documento = (HTMLDocument) this.editor.createDefaultDocument();
		this.elementoBody = documento.getRootElements()[0].getElement(0);

		painelMensagens = new JTextPane();
		painelMensagens.setEditable(false);
		painelMensagens.setEditorKit(this.editor);
		//painelMensagens.setDocument(this.documento);
		painelMensagens.setContentType("text/html");
		painelMensagens.setBackground(Color.getColor("#004b66"));
		painelMensagens.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		painelMensagens.setText("<html><body bgcolor=\"#004b66\"></body></html>");
		scrollPane = new JScrollPane(painelMensagens);
		panel_2.add(scrollPane, BorderLayout.CENTER);

		recebidos = new ArrayList<Enviavel>();
			
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(30, 30, 30));
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnSairDaSala = new JButton("Sair da sala");
		btnSairDaSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fechar(true, false);
			}
		});
		btnSairDaSala.setBackground(Color.LIGHT_GRAY);
		btnSairDaSala.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel_3.add(btnSairDaSala, BorderLayout.EAST);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				exibir();
			}
		});
	}
	protected void enviarDigitando()
	{
		try
		{
			ArrayList<String> destino = new ArrayList<String>();
			if (cbxDestino.getSelectedIndex() == 0)
				for (int i = 0; i < cbxDestino.getItemCount(); i++)
					destino.add(cbxDestino.getItemAt(i).toString());
			else
			{
				destino.add("dm");
				destino.add(cbxDestino.getSelectedItem().toString());
			}
			transmissor.writeObject(new Aviso(4, destino));
		}
		catch(Exception ex){System.out.println(ex.getMessage());};
	}

	protected void fechar(boolean abrirNovaJanela, boolean servidorFechou)
	{
		try
		{
			if (!servidorFechou)
			{
				transmissor.writeObject(new PedidoParaSairDaSala());
				transmissor.flush();
				transmissor.close();
			}
			else
				escolha.morra();

			dispose();

			if (abrirNovaJanela)
			{
				JanelaDeEscolha novaJanela = new JanelaDeEscolha();
				if (!servidorFechou)
					novaJanela.setDados(nomeSala, nomeUsuario);
				novaJanela.setVisible(true);
			}
		}
		catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
	}

	protected Mensagem ultimaMensagem = null;
	protected String ultimoUsuario = null;
	protected Date quandoRecebeuUltimoDigitando = null;

	public void receber(Enviavel recebido)
	{
		if (recebido instanceof Aviso && !recebido.getUsuario().equals(this.nomeUsuario))
		{
			Aviso aux = (Aviso)recebido;
			if (aux.getTipo() == 1)
			{
				modelo.addElement(recebido.getUsuario());
				cbxDestino.addItem(recebido.getUsuario());
			}
			else if(aux.getTipo() == 2)
			{
				for (int i = 0; i < cbxDestino.getItemCount(); i++)
					if (cbxDestino.getItemAt(i).equals(recebido.getUsuario()))
					{
						cbxDestino.removeItemAt(i);
						modelo.remove(i);
					}
			}
			else
			{
				lblEstaDigitando.setText(" " + aux.getUsuario() + " est\u00E1 digitando... ");
				quandoRecebeuUltimoDigitando = new Date();
			}
		}

		if ((!(recebido instanceof Aviso) || ((Aviso)recebido).getTipo() != 4))
		{
			recebidos.add(recebido);

			exibir();

			if (!isFocused())	
				requestFocus(); // Faz a janela piscar se o usuário recebeu uma mensagem, um aviso de entrada ou um aviso de saída
		}
	}
	protected void exibirTodos()
	{
		String texto = "<html><body bgcolor=\"#004b66\">" + Formatador.formatar(recebidos, nomeUsuario, painelMensagens.getWidth()) + "</body></html>";
		painelMensagens.setText(texto);
		painelMensagens.setCaretPosition(painelMensagens.getDocument().getLength());
	}
	protected void exibirDMs(String usuario)
	{
		ArrayList<Enviavel> dmsComUsuario = new ArrayList<Enviavel>();

		for (int i = 0; i < recebidos.size(); i++)
		{
			if (recebidos.get(i) instanceof Mensagem)
			{
				Mensagem msg = (Mensagem) recebidos.get(i);
				if (msg.getDestinatarios().get(0).equals("dm") && (msg.getDestinatarios().get(1).equals(usuario) || msg.getUsuario().equals(usuario)))
					dmsComUsuario.add(msg);
			}
		}

		String texto = "<html><body bgcolor=\"#004b66\">" + Formatador.formatar(dmsComUsuario, nomeUsuario, painelMensagens.getWidth()) + "</body></html>";
		painelMensagens.setText(texto);
		painelMensagens.setCaretPosition(painelMensagens.getDocument().getLength());
	}
	protected void exibir()
	{
		if (chkApenasDMs.isSelected())
			exibirDMs((String) cbxDestino.getSelectedItem());
		else
			exibirTodos();
	}
}