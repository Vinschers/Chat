import java.util.ArrayList;

public class Formatador
{
    private Formatador() {} // Impede que seja instanciada

    public static String formatar(ArrayList<Enviavel> recebidos, String nomeUsuario, int widthPainel)
    {
        String texto = "";

        for (int i = 0; i < recebidos.size(); i++)
            texto += formatarRecebido(recebidos, i, nomeUsuario, widthPainel);

        return texto;
    } 
    protected static String formatarRecebido(ArrayList<Enviavel> recebidos, int indiceRecebido, String nomeUsuario, int widthPainel)
    {
        Enviavel recebido = recebidos.get(indiceRecebido);
        String texto = recebido.toString();

		if (recebido instanceof Mensagem)
		{
			Mensagem msg = (Mensagem)recebido;
			ArrayList<String> linhas = Wrapper.fazerWrap(texto, widthPainel);
			texto = "";
			for (int i = 0; i < linhas.size(); i++)
				texto += new String(formatarMensagem(msg, linhas.get(i), recebidos, indiceRecebido, i, nomeUsuario));
		}
		return texto;
    }
    protected static String formatarMensagem(Mensagem msg, String txt, ArrayList<Enviavel> recebidos, int indiceRecebido, int linhaAtual, String nomeUsuario)
    {
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
				texto.replace("<x>" + nomeUsuario.replace(" ", "&nbsp;") + "</x>", "<font color=\"#00d3a5;\">Voc\u00EA</font>") + 
				"</p>";

		if ((!recebidoEhUltimoUsuario || destinoDiferente) && ultimaMensagem != null)
			texto = "<div class=\"espaco\"></div>" + texto;
		return texto;
    }
    protected static ArrayList<String> consertarDecoracaoDeTexto(ArrayList<String> texto)
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