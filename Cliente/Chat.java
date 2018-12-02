import java.awt.*;
import java.awt.Dimension; 
import javax.swing.*; 
import javax.swing.text.Element; 
import javax.swing.text.View; 
import javax.swing.text.ViewFactory; 
import javax.swing.text.html.HTMLEditorKit; 
import javax.swing.text.html.InlineView; 
import javax.swing.text.html.ParagraphView;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

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
	protected String ip;
	protected String nomeSala;

	protected ArrayList<Enviavel> recebidos;

	/**
	 * Create the frame.
	 */
	public Chat(JanelaDeEscolha escolha, String nomeSala, String nomeUsuario, ObjectOutputStream transmissor, String ip) {
		setTitle("Chat - Sala conectada: " + nomeSala);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 525);
		setMinimumSize(new Dimension(500, 300));
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
		this.ip = ip;

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
				if (e.getStateChange() == e.SELECTED)
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
		panel.add(btnEnviar, BorderLayout.EAST);

		txtMensagem.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				btnEnviar.setEnabled(txtMensagem.getText().length() > 0);
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
					btnEnviar.setEnabled(txtMensagem.getText().length() > 0);
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
				btnEnviar.setEnabled(txtMensagem.getText().length() > 0);
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
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() >= 2 && listUsuarios.getSelectedIndex() != 0) {
		
					// Double-click detected
					//int index = list.locationToIndex(evt.getPoint());
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
		this.editor = new HTMLEditorKit(){ 
			@Override 
			public ViewFactory getViewFactory(){ 
  
				return new HTMLFactory(){ 
					public View create(Element e){ 
					   	View v = super.create(e); 
					   	if(v instanceof InlineView){ 
						   	return new InlineView(e){ 
							   	public int getBreakWeight(int axis, float pos, float len) { 
									return GoodBreakWeight; 
							   	} 
							   	public View breakView(int axis, int p0, float pos, float len) { 
								   	if(axis == View.X_AXIS) { 
									   	checkPainter(); 
									   	int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len); 
									   	if(p0 == getStartOffset() && p1 == getEndOffset()) { 
											return this; 
									   	} 
									   	return createFragment(p0, p1); 
								   	} 
								   	return this; 
								} 
							}; 
					   	} 
					   	else if (v instanceof ParagraphView) { 
						   	return new ParagraphView(e) { 
							   	protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) { 
								   	if (r == null) { 
										r = new SizeRequirements(); 
								   	} 
								   	float pref = layoutPool.getPreferredSpan(axis); 
								   	float min = layoutPool.getMinimumSpan(axis); 
								   	// Don't include insets, Box.getXXXSpan will include them. 
									r.minimum = (int)min; 
									r.preferred = Math.max(r.minimum, (int) pref); 
									r.maximum = Integer.MAX_VALUE; 
									r.alignment = 0.5f; 
								   	return r; 
								} 
  
							}; 
						} 
					   	return v; 
					} 
				}; 
			} 
		};

		this.folhaDeEstilo.addRule("body {background-color: #004b66; font-size: 16pt; font-family: Century Gothic; color: white; max-width: 500px; overflow-x: hidden;}");
		this.folhaDeEstilo.addRule("p {padding: 2px;} ");
		this.folhaDeEstilo.addRule(".geral {background-color: #00384c; color: white;} ");
		this.folhaDeEstilo.addRule(".dm {background-color: #262626; color: #bcbcbc;} ");
		this.folhaDeEstilo.addRule(".espaco {font-size: 3pt;}");
		this.folhaDeEstilo.addRule("center {text-align: center; font-weight: bold; font-size: 22pt; margin-bottom: 5px; margin-top: 5px;}");
		this.folhaDeEstilo.addRule(".negrito {font-weight: bold}");
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
					novaJanela.setDados(ip, nomeSala, nomeUsuario);
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
		String texto = recebido.toString();

		if (recebido instanceof Mensagem)
		{
			Mensagem msg = (Mensagem) recebido;

			ArrayList<String> destinoAntigo = null;
			boolean recebidoEhUltimoUsuario = false;
			Mensagem ultimaMensagem = null;

			if (indiceRecebido > 0 && recebidos.get(indiceRecebido - 1) instanceof Mensagem)
			{
				ultimaMensagem = (Mensagem)recebidos.get(indiceRecebido - 1);
				destinoAntigo = ultimaMensagem.getDestinatarios();
				recebidoEhUltimoUsuario = ultimaMensagem.getUsuario().equals(recebido.getUsuario());
			}
			ArrayList<String> destinoAtual = msg.getDestinatarios();

			boolean destinoDiferente = destinoAntigo==null || !destinoAntigo.get(0).equals(destinoAtual.get(0)) || (destinoAntigo.get(0).equals("dm") && !destinoAntigo.get(1).equals(destinoAtual.get(1)));

			if (recebidoEhUltimoUsuario && !destinoDiferente && ultimaMensagem != null)
				texto = ((Mensagem)recebido).getMensagem() + "<br>";
			
			texto = "<p class=\"" + (((Mensagem)recebido).getDestinatarios().get(0).equals("dm")?"dm":"geral") + "\">" + 
					texto.replace("<x>" + this.nomeUsuario + "</x>", "<font color=\"#00d3a5;\">Voc\u00EA</font>") + 
					"</p>";

			if ((!recebidoEhUltimoUsuario || destinoDiferente) && ultimaMensagem != null)
				texto = "<div class=\"espaco\"></div>" + texto;	
				
		}
		
		return texto;
	}
}
//teste de wrap
/*
import java.awt.Dimension; 
import javax.swing.*; 
import javax.swing.text.Element; 
import javax.swing.text.View; 
import javax.swing.text.ViewFactory; 
import javax.swing.text.html.HTMLEditorKit; 
import javax.swing.text.html.InlineView; 
import javax.swing.text.html.ParagraphView; 
 
public class HtmlLetterWrap { 
 
    public HtmlLetterWrap(){ 
        final JFrame frame = new JFrame("Letter wrap test"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
 
        final JEditorPane htmlTextPane = newJEditorPane(); 
 
        htmlTextPane.setEditorKit(new HTMLEditorKit(){ 
           @Override 
           public ViewFactory getViewFactory(){ 
 
               return new HTMLFactory(){ 
                   public View create(Element e){ 
                      View v = super.create(e); 
                      if(v instanceof InlineView){ 
                          return new InlineView(e){ 
                              public int getBreakWeight(int axis, float pos, float len) { 
                                  return GoodBreakWeight; 
                              } 
                              public View breakView(int axis, int p0, float pos, float len) { 
                                  if(axis == View.X_AXIS) { 
                                      checkPainter(); 
                                      int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len); 
                                      if(p0 == getStartOffset() && p1 == getEndOffset()) { 
                                          return this; 
                                      } 
                                      return createFragment(p0, p1); 
                                  } 
                                  return this; 
                                } 
                            }; 
                      } 
                      else if (v instanceof ParagraphView) { 
                          return new ParagraphView(e) { 
                              protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) { 
                                  if (r == null) { 
                                        r = new SizeRequirements(); 
                                  } 
                                  float pref = layoutPool.getPreferredSpan(axis); 
                                  float min = layoutPool.getMinimumSpan(axis); 
                                  // Don't include insets, Box.getXXXSpan will include them. 
                                    r.minimum = (int)min; 
                                    r.preferred = Math.max(r.minimum, (int) pref); 
                                    r.maximum = Integer.MAX_VALUE; 
                                    r.alignment = 0.5f; 
                                  return r; 
                                } 
 
                            }; 
                        } 
                      return v; 
                    } 
                }; 
            } 
        }); 
 
        htmlTextPane.setContentType("text/html"); 
        htmlTextPane.setText("This text pane contains html. The custom HTMLEditorKit supports single letter wrapping."); 
 
        JEditorPane noHtmlTextPane = new JEditorPane(); 
        noHtmlTextPane.setText("This text pane contains no html. It supports single letter wrapping!"); 
 
        htmlTextPane.setMinimumSize(new Dimension(0, 0)); 
        noHtmlTextPane.setMinimumSize(new Dimension(0, 0)); 
 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, noHtmlTextPane, htmlTextPane); 
        splitPane.setContinuousLayout(true); 
 
        frame.add(splitPane); 
 
        frame.setSize(200, 200); 
        frame.setVisible(true); 
        splitPane.setDividerLocation(.5); 
    } 
 
  public static void main(String[] args) { 
      newHtmlLetterWrap(); 
  } 
}
	*/