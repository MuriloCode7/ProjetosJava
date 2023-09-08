package JogoCampoMinado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tabuleiro extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Declaração da variável que permite o jogador jogar novamente.
    int jogarNovamente = 1;
    boolean perdeu = false;
    int numJogo = 0;
    int numJogadas = 0;

    JButton btnRecomecar;
    ConteudoPainel painel;

    public Tabuleiro() {

        // Declaração das propriedades do tabuleiro/JFrame
        setLayout(new BorderLayout());
        setTitle("Campo Minado do Magal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/midia/iconJogo.png")));
        setResizable(false);
        setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 4);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 8);
        setLocation(centerX, centerY);
    }

    /**
     * Método que gera as células no tabuleiro respeitando as características do
     * nível escolhido
     *
     * @throws InterruptedException
     */
    public void geraCelulas(ConteudoPainel painel) {

        this.painel = painel;
        painel.setNumBandeiras(0);

        for (int x = 0; x < painel.getNumColunas(); x++) {
            for (int y = 0; y < painel.getNumLinhas(); y++) {

                Celula celula = new Celula(false, false, 0);
                celula.setBounds((x * 50) + 210, y * 50, 50, 50);
                celula.setFocusable(false);
                celula.setHorizontalTextPosition(JButton.CENTER);
                celula.setVerticalTextPosition(JButton.CENTER);
                celula.setRolloverEnabled(true);
                celula.addActionListener(this::click);
                celula.setCoordX(x);
                celula.setCoordY(y);
                celula.setBackground(painel.getCorCelulas());
                celula.setVisible(true);

                // Colocar e tirar bandeiras com o botao direito do mouse.
                celula.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {

                            Icon bandeira = painel.getLabelQuantBandeiras().getIcon();

                            if (!perdeu) {

                                // Atualizacao da cor das bandeiras no campo
                                if (!celula.isAberta()) {
                                    int numBandeiras = painel.getNumBandeiras();
                                    if (celula.getIcon() == null) {
                                        // Tira a bandeira da célula.
                                        celula.setIcon(bandeira);
                                        painel.setNumBandeiras(numBandeiras + 1);
                                    } else if (celula.getIcon() == bandeira) {
                                        // Coloca a bandeira na célula.
                                        celula.setIcon(painel.getIconInterrogacao());
                                        painel.setNumBandeiras(numBandeiras - 1);
                                    } else if (celula.getIcon() == painel.getIconInterrogacao()) {
                                        celula.setIcon(null);
                                    }
                                    updateBandeiras();
                                }
                            }
                        }
                    }
                });
                adicionaCelula(celula);
                painel.add(celula);
            }
        }
        criaMenu(painel);

        geraMinasComJogoPronto(numJogo);

        revalidate();
        repaint();
    }

    public void updateBandeiras() {

        if (painel.getNumBandeiras() > painel.getQuantMinas()) {
            painel.getLabelQuantBandeiras().setForeground(painel.getVermelho());
        } else {
            painel.getLabelQuantBandeiras().setForeground(new Color(0, 0, 0));
        }
        painel.getLabelQuantBandeiras().setText("  " + painel.getNumBandeiras());
    }

    public void click(ActionEvent e) {

        if (!perdeu) {

            for (Celula celula : painel.getCelulas()) {
                if (e.getSource() == celula) {
                    if (celula.getIcon() == null) {

                        if (!celula.isAberta()) {
                            if (celula.isMina()) {
                                perdeu = true;
                                emiteSom("explosao");
                                //abreMinas();
                                celula.setIcon(painel.getIconMina());
                                celula.setBackground(painel.getVermelho());
                                btnRecomecar.setBackground(painel.getAzulClaro());
                            } else {
                                emiteSom("abertura");
                                abreCelula(celula);
                                numJogadas++;
                            }
                        }
                    }
                }
            }

            if (numJogadas == 1) {
                painel.iniciarCronometro(0);
            }

        }
    }

    public void geraMinasComJogoPronto(int numJogo) {

        JogosProntos jogosProntos = new JogosProntos();
        int[][][] jogos = jogosProntos.getJogosProntos();

        for (int coord[] : jogos[numJogo]) {
            int x = coord[0];
            int y = coord[1];

            Celula celula = encontraCelulaPorCoordenada(x, y);
            celula.setMina(true);
            painel.adicionaCelulaAlterada(celula);

        }

        geraVizinhas();
    }

    public void geraVizinhas() {

        for (Celula celula : painel.getCelulas()) {
            if (!celula.isMina()) {

                int minasVizinhas = 0;

                // Esse laco duplo serve para verificar as 8 celulas que cercam a 
                // célula que está passando
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        // Condicional para não olhar a mesma célula que está passando
                        if (dx != 0 || dy != 0) {

                            int x = celula.getCoordX() + dx;
                            int y = celula.getCoordY() + dy;

                            Celula celulaVizinhaOlhando = encontraCelulaPorCoordenada(x, y);
                            if (celulaVizinhaOlhando != null && celulaVizinhaOlhando.isMina()) {
                                minasVizinhas++;
                            }
                        }
                    }
                }
                celula.setQuantMinasVizinhas(minasVizinhas);
//                System.out.println("A célula (" + celula.getCoordX() + ", " + celula.getCoordY() 
//                        + ") tem " + celula.getQuantMinasVizinhas() + " minas vizinhas.");
            }
        }
    }

    public void abreCelula(Celula celulaAberta) {

        celulaAberta.setAberta(true);

        if (celulaAberta.getQuantMinasVizinhas() == 0) {

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {

                    int x = celulaAberta.getCoordX() + dx;
                    int y = celulaAberta.getCoordY() + dy;

                    Celula celulaVizinhaOlhando = encontraCelulaPorCoordenada(x, y);
                    if (celulaVizinhaOlhando != null && !celulaVizinhaOlhando.isAberta()) {
                        celulaVizinhaOlhando.setAberta(true);
                        painel.adicionaCelulaAlterada(celulaVizinhaOlhando);
                    }

                }
            }
            abreVaziasProximas();
        }
        executaAbertura();
    }

    public void abreVaziasProximas() {

        int confere = 0;

        do {
            // Esse laço serve para repetir o processo de abertura das células toda vez que
            // uma célula
            // vazia for aberta, porque caso contrário, algumas células que deveriam abrir
            // não abririam
            // por já terem sido percorridas no processo.

            confere = 0;

            for (Celula celulaAtual : painel.getCelulas()) {

                if (!celulaAtual.isAberta() && !celulaAtual.isMina()) {

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {

                            int x = celulaAtual.getCoordX() + dx;
                            int y = celulaAtual.getCoordY() + dy;

                            Celula celulaVizinha = encontraCelulaPorCoordenada(x, y);
                            if (celulaVizinha != null && celulaVizinha.getQuantMinasVizinhas() == 0 && celulaVizinha.isAberta()) {
                                celulaAtual.setAberta(true);
                                confere++;
                            }
                        }
                    }
                }
            }

        } while (confere != 0);
    }

    public void executaAbertura() {
        for (Celula c : painel.getCelulas()) {
            if (c.isAberta()) {
                c.setBackground(painel.getCinza());
                defineNumero(c);
            }
        }
        checaCelulas();
    }

    public void defineNumero(Celula celula) {

        Celula c = encontraCelulaPorCoordenada(celula.getCoordX(), celula.getCoordY());

        if (celula.getQuantMinasVizinhas() > 0) {

            switch (celula.getQuantMinasVizinhas()) {
                case 1:
                    c.setIcon(painel.getIconUm());
                    break;
                case 2:
                    c.setIcon(painel.getIconDois());
                    break;
                case 3:
                    c.setIcon(painel.getIconTres());
                    break;
                case 4:
                    c.setIcon(painel.getIconQuatro());
                    break;
                case 5:
                    c.setIcon(painel.getIconCinco());
                    break;
                case 6:
                    c.setIcon(painel.getIconSeis());
                    break;
                case 7:
                    c.setIcon(painel.getIconSete());
                    break;
                case 8:
                    c.setIcon(painel.getIconOito());
                    break;
                default:
                    break;
            }
            c.setBackground(painel.getCinza());
            painel.adicionaCelulaAlterada(c);
        }
    }

    public void checaCelulas() {

        int celulasAbertas = 0;

        for (Celula celula : painel.getCelulas()) {
            if (celula.isAberta()) {
                celulasAbertas++;
            }
        }

        if (celulasAbertas == painel.getNumColunas() * painel.getNumLinhas() - painel.getQuantMinas()) {

            ImageIcon iconJogo = new ImageIcon(getClass().getResource("/midia/iconJogo.png"));

            painel.getTm().cancel();
            emiteSom("vitoria");
            jogarNovamente = JOptionPane.showConfirmDialog(null, "Parabéns você o jogo na " + numJogadas + "ª jogada! "
                    + "\nClique sim para voltar a tela de seleção de jogo", "Campo Minado do Magal", JOptionPane.YES_NO_OPTION, 0, iconJogo);
            if (jogarNovamente == 0) {
                this.dispose();
                MainCampoMinado main = new MainCampoMinado();
                main.executaJogo();
            }

        }

    }

    public void abreMinas() {

        for (Celula celula : painel.getCelulas()) {
            if (celula.isMina()) {
                celula.setIcon(painel.getIconMina());
                celula.setBackground(painel.getCinza());
            }
        }

    }
        
    public void emiteSom(String nome) { // Método AudioAcerto para chamar na classe executavel.

        try {
            // URL do som que no caso esta no pendrive, mais ainda é uma fase de teste.
            AudioInputStream audioInputStream;
            audioInputStream = AudioSystem
                    .getAudioInputStream(getClass().getResource("/midia/" + nome + ".wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }

    }

    public void criaMenu(ConteudoPainel painel) {
        btnRecomecar = new JButton();
        painel.setLabelQuantMinas(new JLabel(" " + painel.getQuantMinas()));
        painel.setLabelAparencia(new JLabel("Aparência"));
        painel.setLabelNumJogo(new JLabel("Jogo nº " + (numJogo + 1)));

        painel.add(btnRecomecar);
        painel.add(painel.getLabelQuantMinas());
        painel.add(painel.getLabelAparencia());
        painel.add(painel.getBtnAparenciaVermelho());
        painel.add(painel.getBtnAparenciaVerde());
        painel.add(painel.getBtnAparenciaAzul());
        painel.add(painel.getLabelQuantBandeiras());
        painel.add(painel.getLabelCronometro());
        painel.add(painel.getBtnAjuda());
        painel.add(painel.getLabelNumJogo());

        add(painel);
        configuraBtnsMenu(painel.getLabelQuantMinas(), painel.getLabelAparencia(),
                painel.getBtnAparenciaVermelho(), painel.getBtnAparenciaVerde(),
                painel.getBtnAparenciaAzul(), painel.getLabelQuantBandeiras(),
                painel.getLabelCronometro(), painel.getBtnAjuda(), painel.getLabelNumJogo());
    }

    public void configuraBtnsMenu(JLabel quantMinas, JLabel aparencia, JButton apVermelho,
            JButton apVerde, JButton apAzul, JLabel quantBandeiras, JLabel cronometro, JButton ajuda, JLabel labelNumJogo) {

        Font fontePadrao = new Font("Calibri", Font.PLAIN, 24);
        Font fonteMaior = new Font("Calibri", Font.PLAIN, 34);

        aparencia.setBounds(50, 0, 150, 50);
        aparencia.setFont(fontePadrao);

        apVermelho.setBounds(15, 40, 50, 50);
        apVermelho.setBackground(painel.getVermelho());
        apVermelho.addActionListener(this::mudarAparencia);

        apVerde.setBounds(80, 40, 50, 50);
        apVerde.setBackground(painel.getVerde());
        apVerde.addActionListener(this::mudarAparencia);

        apAzul.setBounds(145, 40, 50, 50);
        apAzul.setBackground(painel.getAzulClaro());
        apAzul.addActionListener(this::mudarAparencia);

        btnRecomecar.setBounds(30, 115, 150, 50);
        btnRecomecar.setText("Recomeçar");
        btnRecomecar.setBackground(new Color(255, 255, 255));
        btnRecomecar.setFocusable(false);
        //btnRecomecar.addActionListener(this::recomecar);
        btnRecomecar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Cria um novo painel para substituir o conteúdo atual
                ConteudoPainel novoPainel = new ConteudoPainel(painel.getNumColunas(), painel.getNumLinhas());

                novoPainel.setLayout(null);
                novoPainel.setLabelPenalidadeTempo(new JLabel("+ 30seg"));
                novoPainel.getLabelPenalidadeTempo().setForeground(painel.getVermelho());
                novoPainel.getLabelPenalidadeTempo().setBounds(50, 230, 170, 50);
                novoPainel.getLabelPenalidadeTempo().setFont(fonteMaior);
                novoPainel.getLabelPenalidadeTempo().setVisible(true);
                novoPainel.add(novoPainel.getLabelPenalidadeTempo());
                novoPainel.setCorCelulas(painel.getCorCelulas());

                novoPainel.setTempo(painel.getTempo() + 29);
                novoPainel.iniciarCronometro(novoPainel.getTempo());

                geraCelulas(novoPainel);

                perdeu = false;

                // Remove o conteúdo atual e adiciona o novo painel
                getContentPane().removeAll();
                getContentPane().add(novoPainel);
                revalidate();
                repaint();

            }
        });
        btnRecomecar.setFont(fontePadrao);

        labelNumJogo.setBounds(60, 170, 170, 50);
        labelNumJogo.setFont(fontePadrao);

        quantBandeiras.setBounds(40, this.getHeight() - 310, 170, 50);
        quantBandeiras.setText("" + painel.getNumBandeiras());
        quantBandeiras.setIcon(painel.getIconBandeiraVermelha());
        quantBandeiras.setFont(fontePadrao);

        quantMinas.setBounds(21, this.getHeight() - 250, 170, 50);
        quantMinas.setIcon(painel.getIconMinaMenu());
        quantMinas.setFont(fontePadrao);

        cronometro.setBounds(21, this.getHeight() - 185, 180, 50);
        cronometro.setText(painel.getTempo() + "s");
        cronometro.setIcon(painel.getIconRelogio());
        cronometro.setFont(fontePadrao);

        ajuda.setBounds(24, this.getHeight() - 100, 160, 50);
        ajuda.setIcon(painel.getIconAjuda());
        ajuda.setBackground(new Color(238, 238, 238));
        ajuda.setBorder(null);
        ajuda.setFocusable(false);
        ajuda.setText("Ajuda");
        ajuda.addActionListener(this::exibirAjuda);
        ajuda.setFont(fontePadrao);
    }

    public void exibirAjuda(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Restrições:\r\n" + "→ Qualquer duplo clique não terá função;\r\n"
                + "→ Não é possível escolher duas células simultaneamente.\r\n\n" + "Condições:\r\n"
                + "→Se uma mina for revelada, o jogo acaba;\r\n"
                + "→Se um quadrado vazio for revelado, o jogo continua;\r\n"
                + "→Se um número for revelado, ele informará quantas minas estão escondidas nos 8 "
                + "quadrados que o cercam;\r\n"
                + "→Se o botão direito for clicado sobre um quadrado, não marcado anteriormente, será marcado com uma "
                + "bandeira;\r\n"
                + "→Se o botão direito for clicado novamente sobre uma bandeira, será marcado um ponto de interrogação;\r\n"
                + "→Se o botão esquerdo for clicado sobre um quadrado já marcado, não acontecerá nada.\n\n"
                + "-------------------------------------------------------------------------------------------------"
                + "----------------------------------------------------------------\n"
                + "Software Campo Minado Versão 1.0\n"
                + "Desenvolvido por Murilo Magalhães (Magal)\n"
                + "Aluno do 2° Período Curso Bach. em Engenharia de Software\n"
                + "IFG - Câmpus Inhumas - Ano (2021 - 2023)", "Ajuda", 3);
    }

    public void mudarAparencia(ActionEvent e) {

        Color corEscolhida;

        if (e.getSource() == painel.getBtnAparenciaVermelho()) {
            corEscolhida = painel.getVermelho();
            painel.getLabelQuantBandeiras().setIcon(painel.getIconBandeiraAzul());
        } else if (e.getSource() == painel.getBtnAparenciaVerde()) {
            corEscolhida = painel.getVerde();
        } else {
            corEscolhida = painel.getAzulClaro();
            painel.getLabelQuantBandeiras().setIcon(painel.getIconBandeiraVermelha());
        }

        for (Celula celula : painel.getCelulas()) {
            if (!celula.isAberta()) {
                celula.setBackground(corEscolhida);
                if (celula.getIcon() == painel.getIconBandeiraAzul() || celula.getIcon() == painel.getIconBandeiraVermelha()) {
                    celula.setIcon(painel.getLabelQuantBandeiras().getIcon());
                }
            }
        }
        painel.setCorCelulas(corEscolhida);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void adicionaCelula(Celula celula) {
        painel.getCelulas().add(celula);
    }

    public int getNumJogo() {
        return numJogo;
    }

    public void setNumJogo(int numJogo) {
        this.numJogo = numJogo;
    }

    public Celula encontraCelulaPorCoordenada(int x, int y) {

        for (Celula celula : painel.getCelulas()) {
            if (celula.getCoordX() == x && celula.getCoordY() == y) {
                return celula;
            }

        }

        return null;
    }

}
