package JogoCampoMinado;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MainCampoMinado {
    
	public void executaJogo(){
		
            ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));


		//Declaração da variável para a escolha do nível do jogo.
		String[] niveis = { "Nível Iniciante: 9x9", "Nível Intermediário: 16x16"};

		//Escolha do nível do jogo.
		String nivel = (String) JOptionPane.showInputDialog(null, "Selecione a dificuldade do jogo: ", "Campo Minado", 3,
				iconJogo, niveis, null);

		//Instanciação da janela do jogo.
		Tabuleiro tab = new Tabuleiro();
		
		if (nivel.equals(niveis[0])) {
			//Se o nível escolhido for o "iniciante", o tabuleiro será setado com o tamanho de 9x9 
			//e com 10 minas espalhadas pelo tabuleiro (O método setSize define as dimensões do JFrame).
			tab.setTam(9);
			tab.setMinas(10);
			tab.setBounds(450,150,700, 486);
		} else {
			//Se o nível escolhido for o "intermediário", o tabuleiro será setado com o tamanho de 16x16 
			//e com 40 minas espalhadas pelo tabuleiro (O método setSize define as dimensões do JFrame).
			tab.setTam(16);
			tab.setMinas(40);
			tab.setBounds(350,100,900, 672);
		} 

		//Chamada do método que gera as células no tabuleiro.
		tab.geraCelulas();
                tab = null;

	}

	public static void main(String[] args){
		
		//Instanciação da classe executável.
		MainCampoMinado main = new MainCampoMinado();
		//Chamada do método que executa o jogo.
		main.executaJogo();
	}
}
