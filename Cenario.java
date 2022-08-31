import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.File;

/**
 * Trata o cenario visual do jogo da nave
 * 
 * @author Julio César Alves
 */
public class Cenario
{
    // janela da aplicacao
    private JFrame janela;
    // area de desenho do jogo (usa uma classe interna)
    private PainelDeDesenho areaDesenho;
    // trata a entrada de dados pelo teclado
    private InputHandler input;
    
    // numero de frames por segundo do jogo
    private int fps = 20;
    // contador de tempo do jogo (em quantidade de atualizacoes e não em minutos/segundos)
    private int tempo = 0;
    
    // nave do jogador
    private Nave nave;
    
    // inimigo no jogo
    private Inimigo inimigo;
    
    // conjunto de tiros existente no jogo (tanto do jogador quanto do inimigo)
    private ArrayList<Tiro> tiros;
    
    // placar do jogo
    private Placar placar;
    
    // guarda quando foi dado o ultimo tiro pelo jogador
    private int tempoUltimoTiro = 0;
    // guarda quando o ultimo inimigo foi morto
    private int tempoUltimoInimigoMorto = 0;
    
    // indica se o jogo esta pausado
    private boolean jogoPausado;
    // indica se o jogo terminou (vai sair do jogo, nao jogar de novo)
    private boolean jogoTerminou;
    // indica se o jogador perdeu, mas jogo pode continuar
    private boolean gameOver;
    
    // objetos para as imagens usadas no jogo
    private BufferedImage imgNave;
    private BufferedImage imgInimigo;  
    private BufferedImage imgMisselNave;
    private BufferedImage imgMisselInimigo;    
    private BufferedImage imgExplosao;    

    /**
     * Constroi o cenario com a nave e o inimigo passados (cria os objetos fixos, mas nao inicializa o loop de jogo)
     */
    public Cenario()
    {                      
        // cria e define as caracteristas da janela da aplicação
        janela = new JFrame();
        Dimension tamanho = new Dimension(800,600);
        janela.setPreferredSize(tamanho);
        janela.setTitle("Aula Pratica PPOO: Jogo da Nave 2.0");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false); 
        
        // cria e anexa a area de desenho do jogo a janela
        areaDesenho = new PainelDeDesenho();
        janela.setContentPane(areaDesenho);
        janela.pack(); 
        
        // cria e trata o objeto de entrada de dados pelo teclado
        input = new InputHandler();
        janela.addKeyListener(input);
        
        // cria a nave, o inimigo e o placar
        nave = new Nave();
        inimigo = new Inimigo();
        placar = new Placar();
        try
        {
            // carrega as imagens usadas no jogo
            imgNave = ImageIO.read(new File("imagens/nave.png"));
            imgInimigo = ImageIO.read(new File("imagens/inimigo.png"));
            imgMisselNave = ImageIO.read(new File("imagens/misselNave.png"));
            imgMisselInimigo = ImageIO.read(new File("imagens/misselInimigo.png"));
            imgExplosao = ImageIO.read(new File("imagens/explosao.png"));
            
            // altera as dimensoes da nave e do inimigo de acordo com as imagens carregadas
            nave.alterarTamanho(imgNave.getWidth(), imgNave.getHeight());
            inimigo.alterarTamanho(imgInimigo.getWidth(), imgInimigo.getHeight());
        } catch(Exception e) {	
			JOptionPane.showMessageDialog(null, "Houve algum erro ao carregar as imagens do jogo", "Ops...", JOptionPane.ERROR_MESSAGE);
		}        
    }
    
    /**
     * Executa o loop de jogo
	 *
     * Tradicionalmente um jogo desse tipo possui um loop da seguinte forma:
     *   <inicializar>
     *   while (<não termina>) {
     *      <atualiza as informações do jogo>
     *      <desenha informações atualizadas na tela>
     *   }
     * É justamente o que esse método faz, ele apenas acrescenta um tratamento para a taxa de atualização do jogo
     */
    public void executarLoopDeJogo()
    {
        inicializar();
        
        while (!jogoTerminou)
        {
            long tempo = System.currentTimeMillis();
            
            atualizar();
            desenhar();
            
            // trata a taxa de atualizacao do jogo
            tempo = (1000/fps) - (System.currentTimeMillis() - tempo);
            if (tempo > 0)
            {
                try
                {
                    Thread.sleep(tempo);
                }
                catch (Exception e) {}
            }
        }
    }
    
    /**
     * Inicializa os controles do jogo
     */
    private void inicializar()
    {        
        // reinicia a nave e o jogador
        nave.inicializar();
        reiniciarInimigo();
        placar.inicializar();
        
        // inicia a lista de tiros vazia
        tiros = new ArrayList<Tiro>();
        
        // indica que o jogo nao terminou e que esta pausado
        jogoTerminou = false;
        gameOver = false;
        jogoPausado = true;
                
        // mostra a janela
        janela.setVisible(true);
    }    
    
    /**
     * Reinicia a nave inimiga
     */
    private void reiniciarInimigo()
    {
        inimigo.inicializar(750, areaDesenho.getHeight());
    }
    
    /**
     * Atualiza o estado do jogo
     */
    private void atualizar()
    {        
        // trata os eventos de teclado
        tratarEventosTeclado();
        
        // se o jogo nao estiver pausado, trata as demais atualizacoes de estado
        if (!jogoPausado)
        {                          
            // realiza a movimentacao dos tiros existentes na tela
            for (int i = 0; i < tiros.size(); i++)
            {
                tiros.get(i).mover();
            }
            
            // exclui os tiros que sairam da tela
            excluirTirosForaDaTela();
            
            // verifica se houve alguma colisao dos tiros (com a nave do jogador ou com o inimigo)
            tratarColisaoTiros();
            
            // se o inimigo estiver vivo
            if (inimigo.estaVivo())
            {
                // executa a inteligência artificial do inimigo
                Tiro tiro = inimigo.executarIA();
                
                // trata o tiro do inimigo caso ele o tenha feito
                if (tiro != null)
                {
                    tiro.alterarTamanho(imgMisselInimigo.getWidth(), imgMisselInimigo.getHeight());
                    tiros.add(tiro);
                }
            }
            else // se o inimigo estiver morto
            {
                // se ja tiverem se passado 10 passos de tempo desde que ele foi morto, ressucita-o
                if (tempo - tempoUltimoInimigoMorto > 10)
                {
                    reiniciarInimigo();
                }
            }
            
            // atualiza o contador de tempo
            tempo++;
        }
    }
    
    /**
     * Trata os eventos de teclado (exceto teclas de controle geral para pausar, terminar e reiniciar jogo)
     */
    private void tratarEventosTeclado()
    {   
        // as demais teclas so sao tratadas se o jogo nao estiver pausado e a nave estiver viva
        if (!jogoPausado && nave.estaViva())
        {
            // se a tecla de SETA PRA CIMA for pressionada, faz a nave mover pra cima (se ela nao estiver no topo da tela)
            if (input.teclaEstaPressionada(KeyEvent.VK_UP))
            {
                if (nave.getPosY() > 0)
                {
                    nave.moverCima();
                }
            }
    
            // se a tecla de SETA PRA BAIXO for pressionada, faz a nave mover pra baixo (se ela nao estiver na base da tela)
            if (input.teclaEstaPressionada(KeyEvent.VK_DOWN))
            {
                if (nave.getPosY() < areaDesenho.getSize().getHeight() - nave.getAltura())
                {
                    nave.moverBaixo();
                }
            }
            
            // se a tecla de SETA PRA ESQUERDA for pressionada, faz a nave mover pra esquerda (se ela nao estiver no extremo esquerdo da tela)
            if (input.teclaEstaPressionada(KeyEvent.VK_LEFT))
            {
                if (nave.getPosX() > 0)
                {
                    nave.moverEsquerda();
                }
            }
            
            // se a tecla de SETA PRA DIREITA for pressionada, faz a nave mover pra direita (se ela nao estiver no extremo direito da tela)
            if (input.teclaEstaPressionada(KeyEvent.VK_RIGHT))
            {
                if (nave.getPosX() < areaDesenho.getSize().getWidth() - nave.getLargura())
                {
                    nave.moverDireita();
                }
            }  
            
            // se a tecla de CONTROL for pressionada, faz a nave atirar (mas ela so pode atirar uma vez a cada 8 atualizações)
            if (input.teclaEstaPressionada(KeyEvent.VK_CONTROL))
            {
                if (tempo - tempoUltimoTiro > 8)
                {
                    Tiro tiro = nave.atirar();                    
                    
                    // se a nave realmente atirou, trata o tiro
                    if (tiro != null)
                    {
                        tiro.alterarTamanho(imgMisselNave.getWidth(), imgMisselNave.getHeight());
                        tiros.add(tiro);
                        tempoUltimoTiro = tempo;                                                
                    }
                }
            }
        }   
    }
    
    /**
     * Exclui os tiros que sairam da tela
     */
    private void excluirTirosForaDaTela()
    {
        ArrayList<Tiro> foraDaTela = new ArrayList<Tiro>();
        
        for (Tiro tiro : tiros)
        {
            if ((tiro.getPosX() < 0) || (tiro.getPosX() > areaDesenho.getSize().getWidth()))
                foraDaTela.add(tiro);
        }
        
        excluirTiros(foraDaTela);
    }
    
    /**
     * Verifica e trata se algum tiro colidiu com a nave do jogador ou com o inimigo
     */
    private void tratarColisaoTiros()
    {
        // os tiros que colidem sao removidos
        ArrayList<Tiro> tirosARemover = new ArrayList<Tiro>();
        
        // pecorre a lista de tiros
        for (Tiro tiro : tiros)
        {
            // se o tiro e do inimigo e se colidiu com a nave do jogador (viva), trata o tiro tomado pela nave e indica que o tiro sera excluído
            if (nave.estaViva() && tiro.getEhDoInimigo() && 
                colisaoPontoRetangulo(tiro.getPosX(), tiro.getPosY(), tiro.getPosX()+tiro.getLargura(), tiro.getPosY()+tiro.getAltura(),
                                      nave.getPosX(), nave.getPosY(), nave.getPosX()+nave.getLargura(), nave.getPosY()+nave.getAltura()))
            {
                nave.tomarTiro();                
                tirosARemover.add(tiro);
                
                if (!nave.estaViva())
                {
                    gameOver = true;
                }
            }
            // se o tiro e da nave do jogador e se colidiu com o inimigo (vivo), trata o tiro tomado pelo inimigo e indica que o tiro sera excluído
            else if (inimigo.estaVivo() && !tiro.getEhDoInimigo() && 
                     colisaoPontoRetangulo(tiro.getPosX(), tiro.getPosY(), tiro.getPosX()+tiro.getLargura(), tiro.getPosY()+tiro.getAltura(),
                                           inimigo.getPosX(), inimigo.getPosY(), inimigo.getPosX()+inimigo.getLargura(), inimigo.getPosY()+inimigo.getAltura()))
            {
                inimigo.tomarTiro();                                                
                tirosARemover.add(tiro);
                                
                if (!inimigo.estaVivo())
                {
                    tempoUltimoInimigoMorto = tempo;
                    placar.contarMorteInimigo();
                }
            }
        }  
        
        // exclui os tiros que colidiram
        excluirTiros(tirosARemover);
    }
    
    /**
     * Exclui os tiros passados por parametro
     * 
     * @param tirosARemover tiros que serão excluidos do jogo
     */
    private void excluirTiros(ArrayList<Tiro> tirosARemover)
    {
        for (Tiro tiro : tirosARemover)
        {
            tiros.remove(tiro);
        }  
    }
    
    /**
     * Trata a colisao entre dois retangulos
     */
    private boolean colisaoPontoRetangulo(int xi1, int yi1, int xf1, int yf1, int xi2, int yi2, int xf2, int yf2)
    {
        return (
                (xi1 > xi2 && xi1 < xf2) && (yi1 > yi2 && yi1 < yf2) ||
                (xf1 > xi2 && xf1 < xf2) && (yf1 > yi2 && yf1 < yf2)
               );
    }
    
    /**
     * Trata o desenho do jogo
     */    
    private void desenhar()
    {
        // forca a area de desenho a se redesenhar
        areaDesenho.repaint();
    }    
    
    /**
     * Classe interna para a area de desenho na janela
     */
    private class PainelDeDesenho extends JPanel
    {                    
        private Font fontePadrao = new Font("Comic Sans MS", Font.PLAIN, 14);
        private Font fonteRodape = new Font("default", Font.PLAIN, 10);
        
        /**
         * Metodo chamado para desenhar a area de desenho
         */
        @Override
        public void paintComponent(Graphics g)
        {                        
            super.paintComponent(g);
            g.setFont(fontePadrao);
                        
            // desenha o fundo do jogo
            desenharFundo(g);
            
            // desenha a nave, inimigo e tiros
            desenharNave(g);
            desenharInimigo(g);
            desenharTiros(g);
            
            // desenha o placar do jogo
            desenharPlacar(g);
            
            // desenhar mensagens gerais
            desenharMensagensGerais(g);
        }
        
        /**
         * Desenha o fundo do jogo
         * 
         * @param g componente grafico de desenho
         */
        private void desenharFundo(Graphics g)
        {                        
            Dimension tamanho = getSize();
            
            // desenha o fundo preto
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, (int)tamanho.getWidth(), (int)tamanho.getHeight());
        }
        
        /**
         * Desenha as informacoes gerais do jogo
         * 
         * @param g componente grafico de desenho
         */
        private void desenharMensagensGerais(Graphics g)
        {
            // escreve informacoes basicas do jogo
            String mensagem1 = "";
            String mensagem2 = "";
            boolean temMensagem = true;
            
            if (gameOver)
            {
                mensagem1 = "GAME OVER!!!";
                mensagem2 = "F2 para reiniciar...";
                
            }
            else if (jogoPausado)
            {
                mensagem1 = "ENTER para pausar/continuar";
                mensagem2 = "Ctrl para atirar";
            }
            else
            {
                temMensagem = false;
            }
            
            if (temMensagem)
            {
                Dimension tamanho = getSize();
                int posX = (int)tamanho.getWidth()/3;
                int posY = (int)tamanho.getHeight()/2;
                
                g.setColor(Color.GRAY);
                g.fillRoundRect(posX - 30, posY - 30, 300, 50, 10, 10);
                
                g.setColor(Color.WHITE);
                g.drawString(mensagem1, posX, posY-10);
                g.drawString(mensagem2, posX, posY+10);
            }
            
            // desenhas informacoes de desenvolvimento
            g.setFont(fonteRodape);
            g.setColor(Color.GRAY);
            g.drawString("GAC106 (PPOO) - DAC/ICET/UFLA", (int)getSize().getWidth()-200, (int)getSize().getHeight()-15);
        }
        
        /**
         * Desenha a nave do jogador (ou uma explosao se ela estiver morta)
         * Desenha tambem o texto de exibição da nave
         * 
         * @param g componente grafico de desenho
         */
        private void desenharNave(Graphics g)
        {           
            if (nave.estaViva())
            {
                g.drawImage(imgNave, nave.getPosX(), nave.getPosY(),null);
            }
            else
            {
                g.drawImage(imgExplosao, nave.getPosX(), nave.getPosY(),null);
            }
            
            // desenha o texto de exibicao da nave            
            g.setColor(Color.WHITE);
            g.drawString(nave.getTextoExibicao(), nave.getPosX(), nave.getPosY()-10);
        }
        
        /**
         * Desenha o inimigo (ou uma explosao se ele estiver morto)
         * 
         * @param g componente grafico de desenho
         */
        private void desenharInimigo(Graphics g)
        {            
            if (inimigo.estaVivo())
            {
                g.drawImage(imgInimigo, inimigo.getPosX(), inimigo.getPosY(),null);
            }
            else
            {
                g.drawImage(imgExplosao, inimigo.getPosX(), inimigo.getPosY(),null);
            }
        }
        
        /**
         * Desenha os tiros do jogo
         * 
         * @param g componente grafico de desenho
         */
        private void desenharTiros(Graphics g)
        {
            // percorre a lista de tiros desenhando de acordo se sao da nave ou do inimigo            
            for (Tiro tiro : tiros)
            {
                if (tiro.getEhDoInimigo())
                {
                    g.drawImage(imgMisselInimigo, tiro.getPosX(), tiro.getPosY(),null);
                }
                else
                {
                    g.drawImage(imgMisselNave, tiro.getPosX(), tiro.getPosY(),null);                    
                }
            }                         
        }
        
        /**
         * Desenha o placar do jogo
         * 
         * @param g componente grafico de desenho
         */
        private void desenharPlacar(Graphics g)
        {
            Dimension tamanho = getSize();
            
            int posX = (int)tamanho.getWidth()/3;
            int posY = 20;
            int distanciaEntreInfos = 100;            
            
            // desenha os pontos do placar            
            g.setColor(Color.YELLOW);            
            g.drawString("Pontos: " + placar.getPontos(), posX, posY);
            
            // desenha as informacoes adicionais
            g.setColor(Color.GREEN);
            for (String texto : placar.getInfoAdicionais())
            {
                posX += distanciaEntreInfos;
                g.drawString(texto, posX, posY);
            }                        
        }
    }
    
    /**
     * Classe interna que trata os eventos de teclado
     */
    private class InputHandler implements KeyListener
    {
        // vetor que guarda para cada tecla se ela esta pressionada ou não
        private boolean[] keys = new boolean[256];
        
        /**
         * Retorna se uma tecla esta pressionada ou não
         */       
        private boolean teclaEstaPressionada(int keyCode)
        {
            if (keyCode > 0 && keyCode < 256)
            {
                return keys[keyCode];
            }
    
            return false;
        }
    
        /**
         * Trata quando uma tecla eh pressionada
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() > 0 && e.getKeyCode() < 256)
            {
                keys[e.getKeyCode()] = true;
                
                tratarTeclasControle(e.getKeyCode());
            }            
        }
    
        /**
         * Trata quando uma tecla eh liberada
         */
        @Override        
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() > 0 && e.getKeyCode() < 256)
            {
                keys[e.getKeyCode()] = false;
            }
        }
    
        /**
         * Trata quando uma tecla eh "digitada"
         */
        @Override
        public void keyTyped(KeyEvent e){}
        
        /**
         * Trata as teclas de controle gerais do jogo
         */
        private void tratarTeclasControle(int keyCode)
        {
            // se a tecla ENTER for pressionada (des)pausa o jogo
            if (input.teclaEstaPressionada(KeyEvent.VK_ENTER))
            {
                jogoPausado = !jogoPausado;
            }
            
            // se a tecla F2 for pressionada, reinicia o jogo
            if (input.teclaEstaPressionada(KeyEvent.VK_F2))
            {            
                inicializar();
            }
        }
    }
}
