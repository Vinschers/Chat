import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

public class Wrapper
{
	private Wrapper() {} // Impede que seja instanciada
    public static ArrayList<String> fazerWrap(String txt, int widthPainel)
    {
        AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		Font font = new Font("Century Gothic", Font.PLAIN, 18);
		int widthAtual;
		int widthPalavraAtual;

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
		return Formatador.consertarDecoracaoDeTexto(linhas);
	}
	protected static void adicionarLinha(ArrayList<String> array, String linha)
	{
		if (linha.trim().length() > 0)
			array.add(linha.replace(" ", "&nbsp;"));
	}
}