package JogoCampoMinado;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MainCampoMinado {
    
	public void executaJogo(){
		
            ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));


		//Escolha do nível do jogo.
		int comecar = JOptionPane.showConfirmDialog(null, "Deseja iniciar o jogo?", "Campo Minado",  
                        JOptionPane.YES_NO_OPTION, 0, iconJogo);
                
                if (comecar == 0) {
                    //Instanciação da janela do jogo.
                    Tabuleiro tab = new Tabuleiro();

                    tab.setTam(16);
                    tab.setMinas(40);
                    tab.setBounds(350,100,900, 672);


                    //Chamada do método que gera as células no tabuleiro.
                    tab.geraCelulas();
                }
		

	}

	public static void main(String[] args){
		
		//Instanciação da classe executável.
		MainCampoMinado main = new MainCampoMinado();
		//Chamada do método que executa o jogo.
		main.executaJogo();
	}
}
