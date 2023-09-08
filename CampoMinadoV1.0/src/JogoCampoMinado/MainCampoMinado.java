package JogoCampoMinado;

//import javax.swing.ImageIcon;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainCampoMinado {

    public void executaJogo() {

        final int numLinhas = 16;
        final int numColunas = 16;

        ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));

        int numJogo = 0;

        boolean entradaCorreta;
        do {
            entradaCorreta = true;
            try {
                String input = (String) JOptionPane.showInputDialog(null, "Bem-vindo(a) ao campo minado! "
                        + "\n Informe o número do jogo (1 - 100)", "Campo Minado",
                        JOptionPane.QUESTION_MESSAGE, iconJogo, null, null);

                // Verifique se o usuário clicou em "Cancelar" ou fechou a caixa de diálogo
                if (input == null) {
                    entradaCorreta = false;
                    break; // Saia do loop
                }

                numJogo = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                entradaCorreta = false;
            }
        } while (numJogo < 0 || numJogo > 99 || !entradaCorreta);

        //Instanciação da janela do jogo.
        Tabuleiro tab = new Tabuleiro();
        ConteudoPainel painel = new ConteudoPainel(numColunas, numLinhas);

        //Chamada do método que executa o jogo.
        tab.setNumJogo(numJogo);

        int quantTotalCelulas = numLinhas * numColunas;
        Double quantMinas = quantTotalCelulas * 0.15625;

        painel.setQuantMinas(quantMinas.intValue());
        int alturaTela = (50 * numLinhas) + 40;
        int larguraTela = (50 * numColunas) + 12 + 210;
        tab.setSize(larguraTela, alturaTela);

        //Chamada do método que gera as células no tabuleiro.
        tab.geraCelulas(painel);

    }

    public static void main(String[] args) {

        // Verificar se o L&F Nimbus está disponível
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Nimbus não está disponível, você pode lidar com isso de acordo com suas necessidades
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int numJogo = 0;
                boolean entradaCorreta = true;

                //Instanciação da classe executável.
                MainCampoMinado main = new MainCampoMinado();

                main.executaJogo();
            }
        });
    }

}
