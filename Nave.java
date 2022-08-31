
/**
 * Classe que representa uma nave do jogador no jogo extendida da classe principal Naves.java
 * 
 * @author Julio Cesar Alves
 * @version 2016-04-08
 */
public class Nave extends Naves {

    public Nave() {
        
        inicializar();

    }

    /**
     * Reinicia as característivas da nave (quando recomeca o jogo, por exemplo)
     */
    public void inicializar() {
        // posição inicial da nave na tela
         setPosX(10);
        setPosY(200);

        setEstaViva(true);
    }

    /**
     * Retorna o texto a ser exibido próximo à nave
     */
    public String getTextoExibicao() {
        // atualmente nao retorna nada
        return "";
    }

    /**
     * Move a nave pra direita, se ela estiver viva
     */
    public void moverDireita() {
        if (estaViva()) {
            setPosX(getPosX()+getVelocidade());
        }
    }

    /**
     * Move a nave pra esquerda, se ela estiver viva
     */
    public void moverEsquerda() {
        if (estaViva()) {
            setPosX(getPosX()-getVelocidade());
        }
    }
    @Override
    public Tiro atirar()
    {
        if (estaViva())
        {

            return new Tiro(getPosX()+getLargura(), getPosY() + (int)(getAltura()/2), false);
        }
        else
        {
            return null;
        }
    }

}