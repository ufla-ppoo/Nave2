
/**
 * Classe que representa uma nave do player ou do inimigo
 *
 * @author Walisson Mendes Ferreira
 * @version 2022-08-30
 */
public class Naves {

    // posição X da nave
    private int posX;

    // posição Y da nave
    private int posY;

    // velocidade da nave ao mover (em pixels)
    private int velocidade;

    // dimensões da nave
    private int largura;

    private int altura;

    // indica se a nave do jogador esta viva
    private boolean estaViva;

    /**
     * Contrói um nave padrão que possui as caracteristicas semelhantes da nave do player e do inimigo
     *
     */
    public Naves(){

        largura = 50;
        altura = 50;
        velocidade = 15;
    }

    /**
     * Inicializar do Player, que possui atributos únicos.
     * Reinicia o Player com as caracteristicas originais, toda vez que atingido.
     *
     */
    public void inicializar()
    {
        // posição inicial da nave na tela
        posX = 10; //parametroInimigo
        posY = 200; //parametroInimigo
        estaViva = true;
    }

    /**
     * Inicializar do Inimigo, que possui atributos únicos.
     * Reinicia o inimigo com as caracteristicas originais.
     */
    public void inicializar(int fixedPosX, int yMax){
        posX = fixedPosX;
        velocidade = 15;
        posY = 200;
        estaViva = true;

    }

    /**
     * Retorna a posição X da nave na tela
     */
    public int getPosX()
    {
        return posX;
    }

    /**
     * Retorna a posição Y da nave na tela
     */
    public int getPosY()
    {
        return posY;
    }

    /**
     * Retorna a largura da nave
     */
    public int getLargura()
    {
        return largura;
    }

    /**
     * Retorna a altura da nave
     */
    public int getAltura()
    {
        return altura;
    }

    /**
     * Retorna se a nave esta viva
     */
    public boolean estaViva()
    {
        return estaViva;
    }

    /**
     * Altera as dimensoes da nave (para que ela fique do tamanho da figura que a representa)
     *
     * @param largura nova largura da nave
     * @param altura nova altura da nave
     */
    public void alterarTamanho(int largura, int altura)
    {
        this.largura = largura;
        this.altura = altura;
    }

    /**
     * Move a nave pra cima, se ela estiver viva
     */
    public void moverCima()
    {
        if (estaViva)
        {
            posY -= velocidade;
        }
    }

    /**
     * Move a nave pra baixo, se ela estiver viva
     */
    public void moverBaixo()
    {
        if (estaViva)
        {
            posY += velocidade;
        }
    }

    /**
     * Método vazio que é sobrecarregado nos filhos
     */
    public Tiro atirar()
    {
        return null;
    }

    /**
     * Trata quando a nave do jogador toma um tiro, se ela estiver viva
     */
    public void tomarTiro()
    {
        if (estaViva)
        {
            // morre
            estaViva = false;
        }
    }

    /**
     * Atribui o valor X da posição da nave.
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Atribui o valor Y da posição da nave
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Altera o valor do "estaViva" da nave
     */
    public void setEstaViva(boolean estaViva) {
        this.estaViva = estaViva;
    }

    /**
     * Retorna o valor da velocidade da nave.
     */
    public int getVelocidade() {
        return velocidade;
    }
}
