import java.awt.*;

import javax.swing.*; 
import javax.swing.text.Element; 
import javax.swing.text.View; 
import javax.swing.text.ViewFactory; 
import javax.swing.text.html.HTMLEditorKit; 
import javax.swing.text.html.InlineView; 
import javax.swing.text.html.ParagraphView;

import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.*;
import javax.swing.event.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
					btnEnviar.setEnabled(txtMensagem.getText().trim().length() > 0);
				}
				catch(Exception ex) {};
			}
			public void insertUpdate(DocumentEvent e) {
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
				reformatarMensagens();
			}
		});
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

			if (chkApenasDMs.isSelected())
				exibirDMs((String) cbxDestino.getSelectedItem());
			else
				exibirTodos();

			if (!isFocused())	
				requestFocus(); // Faz a janela piscar se o usuário recebeu uma mensagem, um aviso de entrada ou um aviso de saída
		}
	}

	protected void exibirTodos()
	{
		String texto = "<html><body bgcolor=\"#004b66\">";

		for (int i = 0; i < recebidos.size(); i++)
			texto += formatarRecebido(i);

		texto += "</body></html>";
		painelMensagens.setText(texto);

		painelMensagens.setCaretPosition(painelMensagens.getDocument().getLength());
	}
	protected void exibirDMs(String usuario)
	{
		String texto = "<html><body bgcolor=\"#004b66\">";

		for (int i = 0; i < recebidos.size(); i++)
		{
			if (recebidos.get(i) instanceof Mensagem)
			{
				Mensagem msg = (Mensagem) recebidos.get(i);
				if (msg.getDestinatarios().get(0).equals("dm") && (msg.getDestinatarios().get(1).equals(usuario) || msg.getUsuario().equals(usuario)))
					texto += formatarRecebido(i);
			}
		}

		texto += "</body></html>";
		painelMensagens.setText(texto);

		painelMensagens.setCaretPosition(painelMensagens.getDocument().getLength());
	}
	protected String formatarRecebido(int indiceRecebido)
	{
		Enviavel recebido = recebidos.get(indiceRecebido);
		String texto;
		texto = recebido.toString();
		if (recebido instanceof Mensagem)
		{
			Mensagem msg = (Mensagem)recebido;
			ArrayList<String> linhas = fazerWrap(texto);
			texto = "";
			for (int i = 0; i < linhas.size(); i++)
				texto += new String(formatarMensagem(msg, linhas.get(i), indiceRecebido, i));
		}
		return texto;
	}
	protected ArrayList<String> fazerWrap(String txt)
	{
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		Font font = new Font("Century Gothic", Font.PLAIN, 18);
		int widthAtual;
		int widthPalavraAtual;
		int widthPainel = painelMensagens.getWidth();
		String[] palavras = txt.split(" ");
		ArrayList<String> linhas = new ArrayList<String>();
		String textoAtual = "";
		String palavraAtual;
		for (int i = 0; i < palavras.length; i++)
		{
			palavraAtual = palavras[i];
			String aux = textoAtual + " " + palavraAtual;
			widthAtual = (int)(font.getStringBounds(aux, frc).getWidth());
			widthPalavraAtual = (int)(font.getStringBounds(palavraAtual, frc).getWidth());

			if (widthPalavraAtual < widthPainel)
			{
				if (linhas.size()==0 && widthAtual > widthPainel - 135)
				{
					adicionarLinha(linhas, textoAtual);
					textoAtual = palavraAtual;
				}
				else if (widthAtual > widthPainel)
				{
					adicionarLinha(linhas, textoAtual);
					textoAtual = palavraAtual;
				}
				else
					textoAtual += " " + palavraAtual;
				if (i == palavras.length - 1)
					adicionarLinha(linhas, textoAtual);
			}
			else
			{
				char[] caracteres = palavraAtual.toCharArray();
				for (int k = 0; k < caracteres.length; k++)
				{
					widthAtual = (int)(font.getStringBounds(textoAtual + caracteres[k], frc).getWidth());
					if (linhas.size()==0 && widthAtual > widthPainel-100)
					{
						adicionarLinha(linhas, textoAtual);
						textoAtual = "" + caracteres[k];
					}
					else if (widthAtual > widthPainel)
					{
						adicionarLinha(linhas, textoAtual);
						textoAtual = "" + caracteres[k];
					}
					else
						textoAtual += "" + caracteres[k];
					if (k == caracteres.length-1 && i == palavras.length - 1)
						adicionarLinha(linhas, textoAtual);
				}
			}
		}
		return consertarDecoracaoDeTexto(linhas);
	}
	protected void adicionarLinha(ArrayList<String> array, String linha)
	{
		if (linha.trim().length() > 0)
			array.add(linha.replace(" ", "&nbsp;"));
	}
	protected void reformatarMensagens()
	{
		for(int i = 0; i < recebidos.size(); i++)
			formatarRecebido(i);
		if (chkApenasDMs.isSelected())
			exibirDMs((String) cbxDestino.getSelectedItem());
		else
			exibirTodos();
	}
	protected String formatarMensagem(Mensagem msg, String txt, int indiceRecebido, int linhaAtual)
	{
		//*parte em negrito* _parte em italico_ ~parte riscada~ esse daqui vai ser um texto bem grande para fins de teste. eu realmente espero que isso funcione como deveria, separando as palavras e nao apenas as letras.
		//String texto = formatarComCaracteresEspeciais(txt);
		String texto = txt;
		if (msg.getDestinatarios().get(0).equals("dm"))
			texto = "<i><font size=\"4\" color=\"#B0B0B0\">" + msg.getHora() + "</font><b><x>" + msg.getUsuario().replace(" ", "&nbsp;") + "</x> --> <x>" + msg.getDestinatarios().get(1).replace(" ", "&nbsp;") + "</x>:</b></i>" + texto;
		else
			texto = "<i><font size=\"4\" color=\"#B0B0B0\">" + msg.getHora() + "</font></i>&nbsp;<b><x>" + msg.getUsuario().replace(" ", "&nbsp;") + "</x>:</b>&nbsp;" + texto;
		ArrayList<String> destinoAntigo = null;
		boolean recebidoEhUltimoUsuario = true;
		Mensagem ultimaMensagem = new Mensagem(msg);

		if (linhaAtual == 0 && indiceRecebido > 0 && recebidos.get(indiceRecebido - 1) instanceof Mensagem)
		{
			ultimaMensagem = (Mensagem)recebidos.get(indiceRecebido - 1);
			destinoAntigo = ultimaMensagem.getDestinatarios();
			recebidoEhUltimoUsuario = ultimaMensagem.getUsuario().equals(msg.getUsuario());
		}
		ArrayList<String> destinoAtual = msg.getDestinatarios();

		boolean destinoDiferente = false;
		if (linhaAtual == 0)
			destinoDiferente = destinoAntigo==null || !destinoAntigo.get(0).equals(destinoAtual.get(0)) || (destinoAntigo.get(0).equals("dm") && !destinoAntigo.get(1).equals(destinoAtual.get(1)));
		if (recebidoEhUltimoUsuario && !destinoDiferente && ultimaMensagem != null)
			texto = txt + "<br>";
		texto = "<p class=\"" + (msg.getDestinatarios().get(0).equals("dm")?"dm":"geral") + "\">" + 
				texto.replace("<x>" + this.nomeUsuario.replace(" ", "&nbsp;") + "</x>", "<font color=\"#00d3a5;\">Voc\u00EA</font>") + 
				"</p>";

		if ((!recebidoEhUltimoUsuario || destinoDiferente) && ultimaMensagem != null)
			texto = "<div class=\"espaco\"></div>" + texto;
		return texto;
	}
	protected ArrayList<String> consertarDecoracaoDeTexto(ArrayList<String> texto)
	{
		String linhaAtual;
		for (int i = 0; i < texto.size(); i++)
		{
			linhaAtual = texto.get(i);

			char[] caracteresEspeciais = {'*', '_', '~'};
			int[] quantidadeCaracteres = {0, 0, 0};

			for (int j = 0; j < linhaAtual.length(); j++)
			{
				for (int k = 0; k < caracteresEspeciais.length; k++)
					if (linhaAtual.charAt(j) == caracteresEspeciais[k])
						quantidadeCaracteres[k]++;
			}
			for (int j = 0; j < caracteresEspeciais.length; j++)
				if (quantidadeCaracteres[j] % 2 == 1 && i < texto.size() - 1 && texto.get(i + 1).indexOf(caracteresEspeciais[j]) > -1)
				{
					linhaAtual += caracteresEspeciais[j];
					texto.set(i + 1, caracteresEspeciais[j] + texto.get(i + 1));
				}

			texto.set(i, Mensagem.formatarMensagem(linhaAtual));
			linhaAtual = texto.get(i);

			String[] aberturas = {"<b>", "<i>", "<strike>"};
			String[] fechamentos = {"</b>", "</i>", "</strike>"};

			int ultimoIndiceAbreDecoracao;
			int ultimoIndiceFechaDecoracao;
			for (int j = 0; j < aberturas.length; j++)
			{
				ultimoIndiceAbreDecoracao = linhaAtual.lastIndexOf(aberturas[j]);
				ultimoIndiceFechaDecoracao = linhaAtual.lastIndexOf(fechamentos[j]);

				if (ultimoIndiceAbreDecoracao > ultimoIndiceFechaDecoracao || 
					(ultimoIndiceAbreDecoracao > -1 && ultimoIndiceFechaDecoracao == -1))
				{
					texto.set(i, linhaAtual + fechamentos[j]);
					texto.set(i + 1, aberturas[j] + texto.get(i + 1));
				}
			}
		}

		return texto;
	}
}