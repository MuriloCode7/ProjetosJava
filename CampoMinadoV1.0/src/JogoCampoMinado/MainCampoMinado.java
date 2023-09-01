package JogoCampoMinado;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MainCampoMinado {

    public void executaJogo() {

        ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));

        int numLinhas = 0;
        int numColunas = 0;
        
        do {
        
            boolean entradaCorreta = false;
            while (!entradaCorreta){
                try {
                    numLinhas = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Bem-vindo(a) ao campo minado! "
                        + "\n Quantas linhas você deseja no seu jogo? (Mínimo 9, máximo 24)", "Campo Minado", JOptionPane.QUESTION_MESSAGE, iconJogo, null, null));
                    entradaCorreta = true;   
                } catch (NumberFormatException e){
                    entradaCorreta = false;
                }
                try {
                    numColunas = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Bem-vindo(a) ao campo minado! "
                        + "\n Quantas colunas você deseja no seu jogo? (Mínimo 9, máximo 42)", "Campo Minado", JOptionPane.QUESTION_MESSAGE, iconJogo, null, null));
                    entradaCorreta = true;
                } catch (NumberFormatException e) {
                    entradaCorreta = false;
                }
            }

        } while (numLinhas < 9 || numColunas < 9 || numLinhas > 24 || numColunas > 42);

        //Instanciação da janela do jogo.
        Tabuleiro tab = new Tabuleiro();

        tab.setNumLinhas(numLinhas);
        tab.setNumColunas(numColunas);

        int quantTotalCelulas = numLinhas * numColunas;
        Double quantMinas = quantTotalCelulas * 0.15625;

        System.out.println(quantMinas.intValue());
        tab.setMinas(quantMinas.intValue());
        int alturaTela = (40 * numLinhas) + 35;
        int larguraTela = (40 * numColunas) + 4 + 210;
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
