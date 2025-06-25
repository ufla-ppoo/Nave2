/**
 * Classe que representa uma nave do jogador no jogo
 *
 * @author Julio Cesar Alves
 * @version 2016-04-08
 */
public class NaveJogador extends Nave {

    /**
     * Constrói uma nave com as características iniciais padrão
     */
    public NaveJogador() {
        // tamanho padrão da nave
        largura = 50;
        altura = 50;
        // velocidade da nave
        velocidade = 15;
        inicializar();
    }

    /**
     * Reinicia as características da nave (quando recomeça o jogo, por exemplo)
     */
    @Override
    public void inicializar() {
        // posição inicial da nave na tela
        posX = 10;
        posY = 200;
        estaViva = true;
    }

    /**
     * Move a nave para a direita, se ela estiver viva
     */
    @Override
    public void moverDireita() {
        if (estaViva) {
            posX += velocidade;
        }
    }

    /**
     * Move a nave para a esquerda, se ela estiver viva
     */
    @Override
    public void moverEsquerda() {
        if (estaViva) {
            posX -= velocidade;
        }
    }

    /**
     * Realiza um tiro se a nave estiver viva
     *
     * @return Retorna o tiro criado, ou null se a nave não atirou
     */
    @Override
    public Tiro atirar() {
        if (estaViva) {
            return new Tiro(posX + largura, posY + (int) (altura / 2), false);
        } else {
            return null;
        }
    }

    /**
     * Trata quando a nave do jogador toma um tiro, se ela estiver viva
     */
    @Override
    public void tomarTiro() {
        if (estaViva) {
            // morre
            estaViva = false;
        }
    }
}
