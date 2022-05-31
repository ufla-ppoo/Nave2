
/**
 * Classe que representa um tiro dado pela nave do jogador ou pelo inimigo
 * 
 * @author Julio César Alves
 * @version 2022-05-31
 */
public class Tiro
{
    // posicao atual do tiro na tela
    private int posX;
    private int posY;
    
    // velocidade do tiro (pixels por atualizacao)
    private int velocidade;
    
    // dimensoes do tiro
    private int largura;
    private int altura;
    
    // indica se o tiro foi dado pelo inimigo ou, caso contrario, pela nave do jogador
    private boolean ehDoInimigo;

    /**
     * Constroi um tiro na posicao passada e com velocidade padrao
     * 
     * @param posX posicao X inicial do tiro
     * @param posY posicao Y inicial do tiro
     * @param ehDoInimigo indica se eh um tiro dado pelo inimigo, caso contrario sera do jogador
     */
    public Tiro(int x, int y, boolean tiroDoInimigo)
    {
        // inicializa os atributos referentes aos parametros passados
        posX = x;
        posY = y;
        ehDoInimigo = tiroDoInimigo;
        
        // inicializa velocidade padrao
        velocidade = 20;        
    }
    
    /**
     * Retorna a posicao X atual do tiro
     */
    public int getPosX()
    {
        return posX;
    }
    
    /**
     * Retorna a posicao Y atual do tiro
     */
    public int getPosY()
    {
        return posY;
    }
    
    /**
     * Retorna a largura do tiro
     */
    public int getLargura()
    {
        return largura;
    }
    
    /**
     * Retorna a altura do tiro
     */
    public int getAltura()
    {
        return altura;
    } 
    
    /**
     * Altera as dimensoes do tiro (para que ele fique do tamanho da figura que o representa)
     * 
     * @param largura nova largura do tiro
     * @param altura nova altura do tiro
     */
    public void alterarTamanho(int largura, int altura)
    {
        this.largura = largura;
        this.altura = altura;
    }       
    
    /**
     * Retorna se o tiro eh do inimigo ou, caso contrário, da nave do jogador
     */
    public boolean getEhDoInimigo()
    {
       return ehDoInimigo;
    }
    
    /**
     * Realiza a movimentacao do tiro (ele se movimenta apenas na horizontal)
     * Se for tiro do inimigo se move pra esquerda, se for da nave do jogador pra direita
     */
    public void mover()
    {
        if (ehDoInimigo)
        {
            posX -= velocidade;
        }
        else
        {
            posX += velocidade;
        }
    }
}
