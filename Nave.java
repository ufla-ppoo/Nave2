/**
 * Classe que representa uma nave do jogo (superclasse)
 * 
 * @author Julio Cesar Alves
 * @version 2016-04-08
 */
public abstract class Nave {
    protected int posX, posY, largura, altura;
    protected boolean estaViva;
    protected double velocidade;

    // Métodos comuns a todas as naves
    public Nave() {}

    public void alterarTamanho(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }

    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public boolean estaViva() { return estaViva; }
    public void inicializar() { estaViva = true; }

    // Métodos de movimentação horizontal (interface pública para todas as naves)
    public void moverDireita() { }
    public void moverEsquerda() { }

    // Métodos de movimentação vertical (padrão)
    public void moverCima() {
        if (estaViva) posY -= velocidade;
    }
    public void moverBaixo() {
        if (estaViva) posY += velocidade;
    }

    // Texto padrão (pode ser sobrescrito)
    public String getTextoExibicao() {
        return "";
    }

    // Métodos que variam em cada tipo de nave
    public abstract Tiro atirar();
    public abstract void tomarTiro();
}
