package JogoCampoMinado;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MainCampoMinado {

    public void executaJogo() {

        ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));

        int numLinhas = 0;
        int numColunas = 0;
        do {
            numLinhas = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Bem-vindo(a) ao campo minado! "
                    + "\n Quantas linhas você deseja no seu jogo? (Mínimo 9, máximo 22)", "Campo Minado", JOptionPane.QUESTION_MESSAGE, iconJogo, null, null));
            numColunas = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Bem-vindo(a) ao campo minado! "
                    + "\n Quantas colunas você deseja no seu jogo? (Mínimo 9, máximo 48)", "Campo Minado", JOptionPane.QUESTION_MESSAGE, iconJogo, null, null));

            
        } while (numLinhas < 9 || numColunas < 9 || numLinhas > 22 || numColunas > 48);

        //Instanciação da janela do jogo.
        Tabuleiro tab = new Tabuleiro();

        tab.setNumLinhas(numLinhas);
        tab.setNumColunas(numColunas);

        int quantTotalCelulas = numLinhas * numColunas;
        Double quantMinas = quantTotalCelulas * 0.15625;

        System.out.println(quantMinas.intValue());
        tab.setMinas(quantMinas.intValue());
        int alturaTela = (40 * numLinhas) + 35;
        int larguraTela = (40 * numColunas) + 2 + 210;
        tab.setBounds(0, 0, larguraTela, alturaTela);

        //Chamada do método que gera as células no tabuleiro.
        tab.geraCelulas();
        tab = null;

    }

    public static void main(String[] args) {
        //Instanciação da classe executável.
        MainCampoMinado main = new MainCampoMinado();
        //Chamada do método que executa o jogo.
        main.executaJogo();
    }
}
