import java.util.Random;

/**
 * Classe que representa uma nave inimiga no jogo
 *  
 * @author Julio Cesar Alves
 * @version 2016-08-04
 */
public class Inimigo
{
    // posicao do inimigo na tela
    private int posX;
    private int posY;
    
    // velocidade do inimigo ao mover (em pixels)
    private int velocidade;
    
    // dimensoes do inimigo
    private int largura;
    private int altura;
    
    // indica se o inimigo esta vivo
    private boolean estaVivo;
    
    // indica se o inimigo esta se movendo para cima ou para baixo
    private boolean movendoPraCima;
    
    // posicao Y máxima que o inimigo pode ir, assume zero como minima (para que ele nao saia da tela)
    private int posYMax;
    
    // posicao Y para onde o inimigo está indo em um determinado momento
    private int posYAlvo;
    
    // Objeto para geracao de números aleatorios
    // Usado para sortear para onde o inimigo vai se deslocar e também para atirar
    private Random random;

    /**
     * Contrói um inimigo e atribui suas características padrões
     *      
     */
    public Inimigo()
    {
        // cria o objeto gerador de números aleatórios
        random = new Random();
        
        // tamanho padrão do inimigo
        largura = 50;
        altura = 50;
        
        posYMax = 100;
        
        // reinicia as característas da nave
        inicializar(posX, posYMax);                
    }
        
    /**
     * Reinicia as caracteristivas do inimigo (quando recomeça o jogo, por exemplo)
     * 
     * @param fixedPosX posicao fixa da nave na vertical
     * @param posYMax valor maximo da posição X (para a nave nao sair da tela)
     */
    public void inicializar(int fixedPosX, int yMax)
    {
        // define a posicao X fixa e o valor maximo de posicao Y.
        posX = fixedPosX;
        posYMax = yMax;

        velocidade = 15;        
        posY = 200;
        posYAlvo = posY;
        
        estaVivo = true;
        movendoPraCima = false;                        
    }
    
    /**
     * Retorna a posicao X do inimigo na tela
     */
    public int getPosX()
    {
        return posX;
    }
    
    /**
     * Retorna a posicao Y do inimigo na tela
     */
    public int getPosY()
    {
        return posY;
    }
    
    /**
     * Retorna se o inimigo esta vivo
     */
    public boolean estaVivo()
    {
        return estaVivo;
    }
    
    /**
     * Retorna a largura do inimigo
     */
    public int getLargura()
    {
        return largura;
    }
    
    /**
     * Retorna a altura do inimigo
     */
    public int getAltura()
    {
        return altura;
    }  
    
    /**
     * Altera as dimensoes do inimigo (para que ele fique do tamanho da figura que o representa)
     * 
     * @param largura nova largura do inimigo
     * @param altura nova altura do inimigo
     */
    public void alterarTamanho(int largura, int altura)
    {
        this.largura = largura;
        this.altura = altura;
    }   

    /**
     * Move o inimigo pra cima, se ele estiver vivo
     */
    public void moverCima()
    {
        if (estaVivo)
        {
            posY -= velocidade;
        }
    }

    /**
     * Move o inimigo pra baixo, se ele estiver vivo
     */
    public void moverBaixo()
    {
        if (estaVivo)
        {
            posY += velocidade;
        }
    }
    
    
    /**
     * Realiza um tiro do inimigo, se ele estiver vivo
     * 
     * @return Retorna o tiro criado, ou null se o inimigo nao atirou
     */
    public Tiro atirar()
    {  
        if (estaVivo)
        {
            return new Tiro(posX, posY + (int)(altura/2), true);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Trata quando o inimigo toma um tiro, se ele estiver vivo
     */
    public void tomarTiro()
    {
        if (estaVivo)
        {
            estaVivo = false; // morre
        }
    }
    
    /**
     * Executa a Inteligência Artificial do inimigo, tratando seus movimentos e tiros
     * 
     * @return retorna o tiro dado pelo inimigo (se ele tiver dado um), caso contrario retorna null
     */
    public Tiro executarIA()
    {
        Tiro tiro = null;
        
        if (estaVivo)
        {
            // realiza a movimentação do inimigo
            movimentar();
            
            // define aleatoriamente se o inimigo irá atirar
            // atirar com a probabilidade de 1 em 15
            if (random.nextInt(15) < 1)
            {
                tiro = atirar();
            }
        }
        
        return tiro;
    }
    
    /**
     * Trata a movimentação do inimigo
     */
    private void movimentar()
    {
        // se o inimigo esta se movendo pra cima e ainda nao alcancou a posicao alvo
        if (movendoPraCima && (posY > posYAlvo))
        {
            // continua se movendo pra cima
            moverCima();
        }
        // se o inimigo esta se movendo pra baixo e ainda nao alcancou a posicao alvo
        else if (!movendoPraCima && (posY < posYAlvo))        
        {
            // continua se movendo pra baixo
            moverBaixo();
        }
        // se a nave esta se movendo pra cima e passou da posicao alvo, ou movendo pra baixo e tambem passou da posição alvo
        else if ((movendoPraCima && (posY <= posYAlvo)) || (!movendoPraCima && (posY >= posYAlvo)))
        {
            // sorteia uma nova posicao alvo
            posYAlvo = random.nextInt(posYMax);
            
            // descobre se deve movimentar pra cima ou pra baixo para alcancar a nova posicao alvo
            movendoPraCima = (posYAlvo < posY);
        }
    }
}
