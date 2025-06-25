import java.util.Random;

/**
 * Classe que representa uma nave inimiga no jogo
 *
 * @author Julio Cesar Alves
 * @version 2016-08-04
 */
public class NaveInimiga extends Nave {
    // indica se o inimigo está se movendo para cima ou para baixo
    private boolean movendoPraCima;
    // posição Y máxima que o inimigo pode ir, assume zero como mínima (para que ele não saia da tela)
    private int posYMax;
    // posição Y para onde o inimigo está indo em um determinado momento
    private int posYAlvo;
    // Objeto para geração de números aleatórios (Usado para sortear movimento e tiro)
    private Random random;

    /**
     * Constrói um inimigo e atribui suas características padrões
     */
    public NaveInimiga() {
        random = new Random();
        largura = 50;
        altura = 50;
        posYMax = 100;
        inicializar(posX, posYMax);
    }

    /**
     * Reinicia as características do inimigo (quando recomeça o jogo, por exemplo)
     *
     * @param fixedPosX posição fixa da nave na vertical
     * @param yMax valor máximo da posição Y (para a nave não sair da tela)
     */
    public void inicializar(int fixedPosX, int yMax) {
        posX = fixedPosX;
        posYMax = yMax;
        velocidade = 15;
        posY = 200;
        posYAlvo = posY;
        estaViva = true;
        movendoPraCima = false;
    }

    /**
     * Realiza um tiro do inimigo, se ele estiver vivo
     *
     * @return Retorna o tiro criado, ou null se o inimigo não atirou
     */
    @Override
    public Tiro atirar() {
        if (estaViva) {
            return new Tiro(posX, posY + (int) (altura / 2), true);
        } else {
            return null;
        }
    }

    /**
     * Trata quando o inimigo toma um tiro, se ele estiver vivo
     */
    @Override
    public void tomarTiro() {
        if (estaViva) {
            estaViva = false; // morre
        }
    }

    /**
     * Executa a Inteligência Artificial do inimigo, tratando seus movimentos e tiros
     *
     * @return retorna o tiro dado pelo inimigo (se ele tiver dado um), caso contrário retorna null
     */
    public Tiro executarIA() {
        Tiro tiro = null;
        if (estaViva) {
            // realiza a movimentação do inimigo
            movimentar();
            // define aleatoriamente se o inimigo irá atirar
            // atirar com a probabilidade de 1 em 15
            if (random.nextInt(15) < 1) {
                tiro = atirar();
            }
        }
        return tiro;
    }

    /**
     * Trata a movimentação do inimigo
     */
    private void movimentar() {
        // se o inimigo está se movendo para cima e ainda não alcançou a posição alvo
        if (movendoPraCima && (posY > posYAlvo)) {
            moverCima();
        }
        // se o inimigo está se movendo para baixo e ainda não alcançou a posição alvo
        else if (!movendoPraCima && (posY < posYAlvo)) {
            moverBaixo();
        }
        // se a nave está se movendo para cima e passou da posição alvo,
        // ou movendo para baixo e também passou da posição alvo
        else if ((movendoPraCima && (posY <= posYAlvo)) || (!movendoPraCima && (posY >= posYAlvo))) {
            posYAlvo = random.nextInt(posYMax);
            movendoPraCima = (posYAlvo < posY);
        }
    }
}
