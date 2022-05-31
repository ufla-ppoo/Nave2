
/**
 * Classe criada para facilitar a execucao do Jogo
 * 
 * @author Julio César Alves
 * @version 2022-05-31
 */
public class JogoNave
{
    public static void main(String[] args)
    {
		Nave nave;
		Inimigo inimigo;
		Placar placar;
		Cenario cenario;
		
        nave = new Nave();
        inimigo = new Inimigo();
        placar = new Placar();                
        cenario = new Cenario(nave, inimigo, placar);
        
        cenario.executarLoopDeJogo();
    }
}
