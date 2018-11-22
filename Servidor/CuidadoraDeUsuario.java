import java.net.Socket;

/**
 * Recebe as mensagens que o usuário deseja mandar e envia para os outros usuários na sala escolhida
 */
public class CuidadoraDeUsuario extends Thread
{
    private Usuario usuario;

    public CuidadoraDeUsuario(Socket conexao, Salas s) throws Exception 
    {
        // Declarar e instanciar OOS e OIS
        // Interagir com o usr via OOS e OIS ate obter o nome da sala que ele deseja entrar
        // Procurar em salas a sala com o nome desejado
        // Interagir com o usr via OOS e OIS ate obter o nome que o usuário deseja usar, e verificar se há um nome igual na sala ou inválido
        // Instanciar o Usuario, fornecendo a conexao, OOS, OIS, nome e sala
        // Fazer várias vezes this.usuario.envia(new AvisoDeEntradaNaSala(i)), onde i é o nome de algum usuário na sala
        // Fazer várias vezes i.envia(new AvisoDeEntradaNaSala(usuario.getNome())), onde i é o nome de algum usuário na sala
        // Incluir o usuario na sala
    }

    public void run()
    {
        Enviavel recebido = null;

        do
        {
            // Receber mensagens e avisos de entrada na e de saída da sala
        }
        while (!(recebido instanceof PedidoParaSairDaSala));

        // Remover this.usuario da sala
        // Mandar para todos da sala: new AvisoDeSaidaDaSala(this.usuario.getNome())
        this.usuario.fechaTudo();
    }
}