import java.net.Socket;

/**
 * Recebe as mensagens que o usuário deseja mandar e envia para os outros usuários na sala escolhida
 */
public class CuidadoraDeUsuario extends Thread
{
    private Usuario usuario;

    public CuidadoraDeUsuario(Socket conexao, Salas s) throws Exception 
    {
        // Declarar e instanciar PW e BR (Pode ser útil usar o ObjectInputStream e o ObjectOutputStream)
        // Interagir com o usr via PW e BR para obter o nome da sala que ele deseja entrar
        // Procurar em salas a sala com o nome desejado
        // Interagir com o usr via PW e BR para obter o nome que o usuário deseja usar, e verificar se há um nome igual na sala ou inválido
        // Instanciar o Usuario, fornecendo a conexao e as salas
        // Incluir o usuario na sala
    }
}