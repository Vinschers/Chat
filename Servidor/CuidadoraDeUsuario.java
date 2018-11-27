import java.net.Socket;

/**
 * Recebe as mensagens que o usuário deseja mandar e envia para os outros usuários na sala escolhida
 */
public class CuidadoraDeUsuario extends Thread
{
    private Socket  conexao;
    private Salas   salas;
    private Usuario usuario;

    public CuidadoraDeUsuario(Socket conexao, Salas s) throws Exception 
    {
        if (conexao == null)
            throw new Exception("Conexão inexistente");
        if (s == null)
            throw new Exception("Salas vazias");
        this.conexao = conexao;
        this.salas = s;
    }

    public void run()
    {
        this.salas = new Salas();
        this.salas.adicionarSala(new Sala("teste"), 2);
        boolean houveErro = true;
        ObjectOutputStream oos = new ObjectOutputStream(conexao.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(new InputStreamReader(conexao.getInputStream()));
        oos.writeObject(new SalasDisponiveis(this.salas));
        ArrayList<Usuario> us;
        String nomeEscolhido;
        String nomeSala;
        ArrayList<Sala> vetSalas;
        Sala salaEscolhida;
        boolean jaExiste;
        while(houveErro)
        {
            houveErro = false;
            nomeEscolhido = (String)ois.readObject();
            nomeSala = (String)ois.readObject();
            vetSalas = this.salas.getSalas();
            salaEscolhida = null;
            for (int i = 0; i < vetSalas.length(); i++)
            {
                if(vetSalas.get(i).getNome() == nomeSala)
                    salaEscolhida = vetSalas.get(i);
            }
            if (salaEscolhida.isCheia())
            {
                oos.writeObject(new AvisoErro("Sala já está cheia!"));
                houveErro = true;
            }
            else if (nomeEscolhido == null || nomeEscolhido.equals(""))
            {
                oos.writeObject(new AvisoErro("Nome inválido!"));
                houveErro = true;
            }
            else
            {
                us = salaEscolhida.getUsuarios();
                jaExiste = false;
                for (int i = 0; i < us.length(); i++)
                {
                    if (us.get(i).getNickname() == nomeEscolhido)
                        jaExiste = true;
                }
                if (jaExiste)
                {
                    oos.writeObject(new AvisoErro("Nome de usuário já existe na sala!"));
                    houveErro = true;
                    nomeEscolhido = (String)ois.readObject();
                }
            }
        }
        this.usuario = new Usuario(this.conexao, oos, ois, nomeEscolhido, salaEscolhida);
        for (int i = 0; i < us.size(); i++)
        {
            this.usuario.envia(new AvisoDeEntradaNaSala(this.usuario), us.get(i));
            us.get(i).envia(new AvisoDeEntradaNaSala(us.get(i)), this.usuario);
        }
        salaEscolhida.adicionarUsuario(this.usuario);
        // Fazer várias vezes this.usuario.envia(new AvisoDeEntradaNaSala(i)), onde i é o nome de algum usuário na sala
        // Fazer várias vezes i.envia(new AvisoDeEntradaNaSala(usuario.getNome())), onde i é o nome de algum usuário na sala
        // Incluir o usuario na sala

        Enviavel recebido = null;
        Object aux;
        ArrayList<Usuario> dest;

        do
        {
            aux = ois.readObject();
            if (aux instanceof Mensagem)
            {
                recebido = (Mensagem)aux;
                dest = recebido.getDestinatarios();
                for (int i = 0; i < dest.size(); i++)
                    this.usuario.envia(recebido, dest.get(i));
            }
            // receber mensagens, avisos de entrada na e de saida da sala
            // se for mensagem, pega nela o destinatario, acha o destinatario na sala e manda para ele a mensagem
        }
        while (!(recebido instanceof PedidoParaSairDaSala));

        // Remover this.usuario da sala
        // Mandar para todos da sala: new AvisoDeSaidaDaSala(this.usuario.getNome())
        salaEscolhida.removerUsuario(this.usuario);
        us = salaEscolhida.getUsuarios();
        for (int i = 0; i < us.size(); i++)
        {
            this.usuario.envia(new AvisoDeEntradaNaSala(this.usuario), us.get(i));
            us.get(i).envia(new AvisoDeEntradaNaSala(us.get(i)), this.usuario);
        }
        this.usuario.fechaTudo();
    }
}