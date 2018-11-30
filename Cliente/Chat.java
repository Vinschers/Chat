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
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.*;
import java.awt.*;
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
	protected JScrollPane scrollPane;

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

	/**
	 * Create the frame.
	 */
	public Chat(JanelaDeEscolha escolha, String nomeSala, String nomeUsuario, ObjectOutputStream transmissor, String ip) {
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
		this.nomeSala = nomeSala;
		this.transmissor = transmissor;
		this.escolha = escolha;
		this.ip = ip;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				try
				{
					fechar();
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
		
		this.folhaDeEstilo = new StyleSheet();
		this.editor = new HTMLEditorKit();

		this.folhaDeEstilo.addRule("body {background-color: \"#d7d0d6\"; font-size: 14pt;}");
		this.folhaDeEstilo.addRule("div {display: inline} ");
		this.folhaDeEstilo.addRule("center {text-align: center; font-weight: bold; font-size: 20pt; margin-bottom: 5px; margin-top: 5px;}");
		this.folhaDeEstilo.addRule(".negrito {font-weight: bold}");
		this.editor.setStyleSheet(this.folhaDeEstilo);
		this.documento = (HTMLDocument) this.editor.createDefaultDocument();
		this.elementoBody = documento.getRootElements()[0].getElement(0);

		painelMensagens = new JTextPane();
		painelMensagens.setEditable(false);
		painelMensagens.setEditorKit(this.editor);
		//painelMensagens.setDocument(this.documento);
		painelMensagens.setContentType("text/html");
		painelMensagens.setBackground(Color.getColor("#d7d0d6"));
		painelMensagens.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		painelMensagens.setText("<html><body bgcolor=\"#d7d0d6\"></body></html>");
		scrollPane = new JScrollPane(painelMensagens);
		panel_2.add(scrollPane, BorderLayout.CENTER);
			
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnSairDaSala = new JButton("Sair da sala");
		btnSairDaSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					fechar();
					dispose();
				}
				catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
			}
		});
		btnSairDaSala.setBackground(Color.LIGHT_GRAY);
		btnSairDaSala.setFont(new Font("Century Gothic", Font.PLAIN, 18));
		panel_3.add(btnSairDaSala, BorderLayout.EAST);
	}

	protected void fechar() throws Exception
	{
		transmissor.writeObject(new PedidoParaSairDaSala());
		transmissor.flush();

		transmissor.close();
		escolha.morra();

		JanelaDeEscolha novaJanela = new JanelaDeEscolha();
		novaJanela.setDados(ip, nomeSala, nomeUsuario);
		novaJanela.setVisible(true);
	}

	protected String ultimoUsuario = null;
	public void receber(Enviavel recebido)
	{
		String texto = recebido.toString();
		String backcolor = "#d7d0d6";
		boolean recebidoEhUltimoUsuario = recebido.equals(ultimoUsuario);

		if (recebido instanceof Mensagem)
		{
			if (ultimoUsuario != null && ultimoUsuario.equals(recebido.getUsuario()))
				texto = ((Mensagem)recebido).getMensagem() + "<br>";
			ultimoUsuario = recebido.getUsuario();

			if (ultimoUsuario.equals(this.nomeUsuario))
				backcolor = "#e1fec6";
			else
				backcolor = "white";
		}
		else
			ultimoUsuario = null;

		painelMensagens.setText("<html><body bgcolor=\"#d7d0d6\">" + painelMensagens.getText().substring(57, painelMensagens.getText().length() - (recebidoEhUltimoUsuario && painelMensagens.getText().length() > 57?23:17)) + (!recebidoEhUltimoUsuario && recebido instanceof Mensagem ?"<div bgcolor=\"" + backcolor + "\">":"") + "<font face=\"Century Gothic\">" + texto + "</font>" + (recebido instanceof Mensagem?"</div>":"") + "</body></html>");
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
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}
}