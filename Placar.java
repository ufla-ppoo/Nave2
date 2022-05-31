import java.util.Map;
import java.util.HashMap;

/**
 * Representa o placar do jogo
 * 
 * @author Julio César Alves
 * @version 2022-05-31
 */
public class Placar
{
    // pontos atuais do placar
    private int pontos;
    
    // pontos ganhos a cada inimigo morto
    private final int pontoPorInimigo = 50;
    
    // informacoes adicionais exibidas no placar
    // cada elemento do conjunto contem um identificador da informacao e o valor da informacao
    private Map<String, String> infoAdicionais;

    /**
     * Construtor do Placar
     */
    public Placar()
    {
        infoAdicionais = new HashMap<String, String>();
        
        inicializar();
    }
    
    /**
     * Inicializa o placar. Limpa os valores das informacoes adicionais, mantendo as chaves
     */
    public void inicializar()
    {        
        pontos = 0;
        
        infoAdicionais.clear();
    }
    
    /**
     * Retorna o numero de pontos do placar
     */
    public int getPontos()
    {
        return pontos;
    }
    
    /**
     * Contabiliza a morte de um inimigo no placar
     */
    public void contarMorteInimigo()
    {
        pontos += pontoPorInimigo;
    }
    
    /**
     * Retorna um vetor com as informacoes adicionais do placar
     */
    public String[] getInfoAdicionais()
    {
        String[] info = new String[infoAdicionais.size()];
        
        int i = 0;
        for (String texto : infoAdicionais.values())
        {
            info[i] = texto;
            i++;
        }
        
        return info;
    }
    
    /**
     * Adiciona uma nova informacao ao placar. Se ja existe informação com a mesma chave, apenas atualiza o valor
     * 
     * @param chave identificador da informacao a ser adicionada/atualizada
     * @param valor valor a ser exibido no placar
     */
    public void adicionarInformacao(String chave, String valor)
    {
        infoAdicionais.put(chave, valor);
    }
}















