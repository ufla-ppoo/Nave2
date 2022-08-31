import java.util.Random;

/**
 * Classe que representa uma nave inimiga no jogo
 *  
 * @author Julio Cesar Alves
 * @version 2016-08-04
 *
 */
public class Inimigo extends Naves
{
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
     */
    public Inimigo()
    {
        // cria o objeto gerador de números aleatórios
        random = new Random();
        posYMax = 100;
        
        // reinicia as característas da nave, utilizando o inicializar da classe pai, juntamente com as variavéis
        //únicas da classe "Inimigo"
        inicializar(getPosX(), posYMax);
        setPosYMax(posYMax);
        movendoPraCima=false;
        posYAlvo = 200;
    }
        
    /**
     * Reinicia as caracteristivas do inimigo (quando recomeça o jogo, por exemplo)
     * 
     * @param fixedPosX posicao fixa da nave na vertical
     * @param yMax valor maximo da posição X (para a nave nao sair da tela)
     */
    @Override
   public void inicializar(int fixedPosX, int yMax)
    {
        super.inicializar(fixedPosX,yMax);
        posYMax = yMax;
        posYAlvo = 200;
        movendoPraCima = false;                        
    }

    /**
     * Realiza um tiro do inimigo, se ele estiver vivo
     * @return Retorna o tiro criado, ou null se o inimigo nao atirou
     */
    @Override
    public Tiro atirar()
    {  
        if (estaViva())
        {
            return new Tiro(getPosX(), getPosY() + (int)(getAltura()/2), true);
        }
        else
        {
            return null;
        }
    }

    /**
     * Atribui a posição máxima que o inimigo pode alcançar
     * @param posYMax
     */
    public void setPosYMax(int posYMax) {
        this.posYMax = posYMax;
    }
    
    /**
     * Executa a Inteligência Artificial do inimigo, tratando seus movimentos e tiros
     * 
     * @return retorna o tiro dado pelo inimigo (se ele tiver dado um), caso contrario retorna null
     */
    public Tiro executarIA()
    {
        Tiro tiro = null;
        
        if (estaViva())
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
        if (movendoPraCima && (getPosY() > posYAlvo))
        {
            // continua se movendo pra cima
            moverCima();
        }
        // se o inimigo esta se movendo pra baixo e ainda nao alcancou a posicao alvo
        else if (!movendoPraCima && (getPosY() < posYAlvo))
        {
            // continua se movendo pra baixo
            moverBaixo();
        }
        // se a nave esta se movendo pra cima e passou da posicao alvo, ou movendo pra baixo e tambem passou da posição alvo
        else if ((movendoPraCima && (getPosY() <= posYAlvo)) || (!movendoPraCima && (getPosY() >= posYAlvo)))
        {
            // sorteia uma nova posicao alvo
            posYAlvo = random.nextInt(posYMax);
            
            // descobre se deve movimentar pra cima ou pra baixo para alcancar a nova posicao alvo
            movendoPraCima = (posYAlvo < getPosY());
        }
    }
}
