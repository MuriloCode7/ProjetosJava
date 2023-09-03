package JogoCampoMinado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    int jogarNovamente;
    ConteudoPainel painel;

    public Tabuleiro() {

        // Declaração das propriedades do tabuleiro/JFrame
        setLayout(new BorderLayout());
        setTitle("Campo Minado");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        getContentPane().setBackground(Color.WHITE);
    }

    /**
     * Método que gera as células no tabuleiro respeitando as características do
     * nível escolhido
     *
     * @throws InterruptedException
     */
    public void geraCelulas(ConteudoPainel painel) {

        this.painel = painel;

        for (int x = 0; x < painel.getNumColunas(); x++) {
            for (int y = 0; y < painel.getNumLinhas(); y++) {

                Celula celula = new Celula();
                celula.setBounds((x * 40) + 210, y * 40, 40, 40);
                celula.setFocusable(false);
                celula.setHorizontalTextPosition(JButton.CENTER);
                celula.setVerticalTextPosition(JButton.CENTER);
                celula.setRolloverEnabled(false);
                celula.addActionListener(this::click);
                if (x < 10 && y < 10) {
                    celula.setText("0" + x + ".0" + y + " m:X");
                } else if (x < 10 && y >= 10) {
                    celula.setText("0" + x + "." + y + " m:X");
                } else if (x >= 10 && y < 10) {
                    celula.setText(+x + ".0" + y + " m:X");
                } else if (x >= 10 && y >= 10) {
                    celula.setText(+x + "." + y + " m:X");
                }
                celula.setForeground(new Color(1.0f, 1.0f, 1.0f, 0f));
                celula.setVisible(true);

                // Colocar e tirar bandeiras com o botao direito do mouse.
                celula.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            ImageIcon bandeira;
                            if (celula.getBackground() == painel.getVermelho()) {
                                bandeira = painel.getIconBandeiraAzul();
                            } else {
                                bandeira = painel.getIconBandeiraVermelha();
                            }
                            if (!celula.getText().contains("aberta")) {
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
                });
                adicionaCelula(celula);
                painel.add(celula);
            }
        }

        carregaCelulas();
        criaMenu(painel);

        revalidate();
        repaint();
    }

    public void carregaCelulas() {
        for (JButton celula : painel.getCelulas()) {
            celula.setBackground(painel.getAzulClaro());
        }
    }

    public void updateBandeiras() {

        if (painel.getNumBandeiras() > painel.getQuantMinas()) {
            painel.getLabelQuantBandeiras().setForeground(painel.getVermelho());
        } else {
            painel.getLabelQuantBandeiras().setForeground(new Color(0, 0, 0));
        }
        painel.getLabelQuantBandeiras().setText("" + painel.getNumBandeiras());
    }

    public void iniciarCronometro() {
        final long segundo = (1000);

        TimerTask tarefa = new TimerTask() {

            @Override
            public void run() {
                int tempo = painel.getTempo();
                painel.setTempo(tempo + 1);
                updateTempo();
            }

        };

        painel.getTm().scheduleAtFixedRate(tarefa, 0, segundo);
    }

    public void click(ActionEvent e) {
        
        System.out.println("Jogada: " + painel.getNumJogadas());
        
        painel.setNumJogadas(painel.getNumJogadas() + 1);

        if (painel.getTempo() == 0) {
            iniciarCronometro();
        }
        
        for (JButton celula : painel.getCelulas()) {
            if (e.getSource() == celula) {
                if (celula.getIcon() == null) {
                    if (painel.getNumJogadas() == 1) {
                        geraMinas(celula);
                        abreCelula(celula);
                    }

                    if (!celula.getText().contains("aberta")) {
                        if (celula.getText().contains("mina")) {
                            painel.getTm().cancel();
                            emiteSom("explosao");
                            //abreMinas(true);
                            celula.setIcon(painel.getIconMina());
                            celula.setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));
                        } else {
                            //emiteSom("abertura");
                            abreCelula(celula);
                        }
                    }
                }
            }
        }
    }

    public void geraMinas(JButton celulaAberta) {

        Random aleatorio = new Random();

        // O jogo pega a primeira celula clicada para não gerar uma mina 
        int xCelula = Integer.parseInt(celulaAberta.getText().substring(0, 2));
        int yCelula = Integer.parseInt(celulaAberta.getText().substring(3, 5));

        int i = 0;

        while (i < painel.getQuantMinas()) {
            int limiteHorizontal = painel.getNumColunas() - 2;
            int limiteVertical = painel.getNumLinhas() - 2;
            int x = aleatorio.nextInt(limiteHorizontal);
            int y = aleatorio.nextInt(limiteVertical);
            for (JButton celula : painel.getCelulas()) {
                if (x > 0 || y > 0) {
                    if (celula.getText().equals(x + "." + y + " m:X") || celula.getText().equals(x + ".0" + y + " m:X")
                            || celula.getText().equals("0" + x + "." + y + " m:X")
                            || celula.getText().equals("0" + x + ".0" + y + " m:X")) {
                        if (x > xCelula + 1 && y > yCelula + 1 || x > xCelula + 1 && y < yCelula - 1
                                || x < xCelula - 1 && y < yCelula - 1 || x < xCelula - 1 && y > yCelula + 1) {
                            if (!celula.getText().contains("mina")) {
                                celula.setText(celula.getText() + " /mina:");
                                i++;
                            }
                        }
                    }
                }
            }

        }

        geraVizinhas();
    }

    public void geraVizinhas() {

        for (JButton celula : painel.getCelulas()) {
            int minas = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        int x = Integer.parseInt(celula.getText().substring(0, 2)) + dx;
                        int y = Integer.parseInt(celula.getText().substring(3, 5)) + dy;
                        if (x >= 0 && x < painel.getNumColunas() && y >= 0 && y < painel.getNumLinhas()) {
                            for (JButton celulaCheca : painel.getCelulas()) {
                                int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
                                int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
                                if (x == xCheca && y == yCheca) {
                                    if (celulaCheca.getText().contains("mina")) {
                                        minas++;

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!celula.getText().contains("mina")) {
                celula.setText(celula.getText().replace("X", String.valueOf(minas)));
            }
        }
    }

    public void abreCelula(JButton celulaAberta) {

        celulaAberta.setText(celulaAberta.getText() + " /aberta");

        if (celulaAberta.getText().substring(8, 9).equals("0")) {

            for (JButton c : painel.getCelulas()) {
                if (c == celulaAberta) {

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
                            int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
                            if (x >= 0 && x < painel.getNumColunas() && y >= 0 && y < painel.getNumLinhas()) {
                                for (JButton celulaCheca : painel.getCelulas()) {
                                    int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
                                    int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
                                    if (x == xCheca && y == yCheca) {
                                        if (!celulaCheca.getText().contains("aberta")
                                                && !celulaCheca.getText().contains("mina")) {
                                            celulaCheca.setText(celulaCheca.getText() + " /aberta");
                                        }
                                    }
                                }
                            }
                        }
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

            for (JButton c : painel.getCelulas()) {

                if (!c.getText().contains("aberta")) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
                            int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
                            if (x >= 0 && x < painel.getNumColunas() && y >= 0 && y < painel.getNumLinhas()) {
                                for (JButton celulaCheca : painel.getCelulas()) {
                                    int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
                                    int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
                                    if (x == xCheca && y == yCheca) {
                                        if (celulaCheca.getText().substring(8, 9).equals("0")
                                                && celulaCheca.getText().contains("aberta")) {
                                            c.setText(c.getText() + " /aberta");
                                            confere++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } while (confere != 0);
    }

    public void executaAbertura() {
        for (JButton c : painel.getCelulas()) {
            if (c.getText().contains("aberta")) {
                c.setBackground(painel.getCinza());
                defineNumero(c);
            }
        }
        checaCelulas();
    }

    public void defineNumero(JButton celula) {

        for (JButton c : painel.getCelulas()) {
            if (c == celula) {

                if (!celula.getText().substring(8, 9).equals("X")) {
                    int quantMinas = Integer.parseInt(celula.getText().substring(8, 9));

                    switch (quantMinas) {
                        case 0:
                            c.setIcon(null);
                            break;
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
                }
            }
        }
    }

    public void checaCelulas() {

        int celulasAbertas = 0;

        for (JButton celula : painel.getCelulas()) {
            if (celula.getText().contains("aberta")) {
                celulasAbertas++;
            }
        }

        if (celulasAbertas == painel.getNumColunas() * painel.getNumLinhas() - painel.getQuantMinas()) {
            painel.getTm().cancel();
            emiteSom("vitoria");
            abreMinas(false);
        }

    }

    public void abreMinas(boolean derrota) {

        /*
        for (JButton celula : celulas) {
            if (celula.getText().contains("mina")) {
                celula.setIcon(iconMina);
                celula.setBackground(cinza);
            }
        }
         */
        if (derrota) {
            jogarNovamente = JOptionPane.showConfirmDialog(null,
                    "Você perdeu o jogo na " + painel.getNumJogadas() + "ª jogada. " + "\nDeseja jogar novamente?", "Confirmação",
                    JOptionPane.YES_NO_OPTION, 0, painel.getIconExplosao());
        } else {
            for (JButton celula : painel.getCelulas()) {
                if (celula.getText().contains("mina")) {
                    celula.setIcon(painel.getIconMina());
                    celula.setBackground(painel.getCinza());
                }
            }

            int minutos = painel.getTempo() / 60;
            int segundos = painel.getTempo() % 60;
            this.jogarNovamente = JOptionPane.showConfirmDialog(null,
                    "Parabéns, você ganhou o jogo!!!" + "\nCom " + painel.getNumJogadas() + " jogadas! " + "\nTempo: " + minutos
                    + "m" + segundos + "s." + "\n\n Deseja jogar novamente?",
                    "Confirmação", JOptionPane.YES_NO_OPTION, 0, painel.getIconTrofeu());

        }

        this.dispose();
        if (jogarNovamente == 0) {
            MainCampoMinado m = new MainCampoMinado();
            m.executaJogo();
        }
    }

    public void emiteSom(String nome) { // Método AudioAcerto para chamar na classe executavel.

        try {
            // URL do som que no caso esta no pendrive, mais ainda é uma fase de teste.
            AudioInputStream audioInputStream;
            audioInputStream = AudioSystem
                    .getAudioInputStream(new File(getClass().getResource("/midia/" + nome + ".wav").toURI()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }

    public void updateTempo() {
        if (painel.getTempo() < 60) {
            painel.getLabelCronometro().setText(painel.getTempo() + "s");
        } else {
            painel.getLabelCronometro().setText("" + painel.getTempo() / 60 + "m" + painel.getTempo() % 60 + "s");
        }
    }

    public void criaMenu(ConteudoPainel painel) {
        JButton recomecar = new JButton();
        painel.setLabelQuantMinas(new JLabel("Quant. de minas: " + painel.getQuantMinas()));
        painel.setLabelAparencia(new JLabel("Mudar aparência:"));

        painel.add(recomecar);
        painel.add(painel.getLabelQuantMinas());
        painel.add(painel.getLabelAparencia());
        painel.add(painel.getBtnAparenciaVermelho());
        painel.add(painel.getBtnAparenciaVerde());
        painel.add(painel.getBtnAparenciaAzul());
        painel.add(painel.getLabelQuantBandeiras());
        painel.add(painel.getLabelCronometro());
        painel.add(painel.getBtnAjuda());

        add(painel);
        configuraBtnsMenu(recomecar, painel.getLabelQuantMinas(), painel.getLabelAparencia(),
                painel.getBtnAparenciaVermelho(), painel.getBtnAparenciaVerde(),
                painel.getBtnAparenciaAzul(), painel.getLabelQuantBandeiras(),
                painel.getLabelCronometro(), painel.getBtnAjuda());
    }

    public void configuraBtnsMenu(JButton recomecar, JLabel quantMinas, JLabel aparencia, JButton apVermelho,
            JButton apVerde, JButton apAzul, JLabel quantBandeiras, JLabel cronometro, JButton ajuda) {

        // Configuração das dimensões dos componentes do menu
        recomecar.setBounds(30, 115, 150, 50);

        aparencia.setBounds(40, 0, 150, 50);
        apVermelho.setBounds(15, 40, 50, 50);
        apVerde.setBounds(80, 40, 50, 50);
        apAzul.setBounds(145, 40, 50, 50);

        quantMinas.setBounds(32, 190, 170, 50);
        quantBandeiras.setBounds(140, this.getHeight() - 150, 60, 50);
        quantBandeiras.setText("" + painel.getNumBandeiras());
        cronometro.setBounds(20, this.getHeight() - 150, 180, 50);
        cronometro.setText(painel.getTempo() + "s");

        ajuda.setBounds(0, this.getHeight() - 90, 180, 50);

        // Demais configurações dos botões do menu
        ajuda.setIcon(painel.getIconAjuda());
        ajuda.setBackground(Color.WHITE);
        ajuda.setBorder(null);
        ajuda.setFocusable(false);
        ajuda.setText("Como jogar?");
        ajuda.addActionListener(this::exibirAjuda);

        apVermelho.setBackground(painel.getVermelho());
        apVermelho.addActionListener(this::mudarAparencia);
        apVerde.setBackground(painel.getVerde());
        apVerde.addActionListener(this::mudarAparencia);
        apAzul.setBackground(painel.getAzulClaro());
        apAzul.addActionListener(this::mudarAparencia);

        quantBandeiras.setIcon(painel.getIconBandeiraVermelha());
        cronometro.setIcon(painel.getIconRelogio());

        recomecar.setText("Recomeçar");
        recomecar.setBackground(new Color(255, 255, 255));
        recomecar.setFocusable(false);
        recomecar.addActionListener(this::recomecar);
        recomecar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cria um novo painel para substituir o conteúdo atual
                ConteudoPainel novoPainel = new ConteudoPainel();
                novoPainel.setLayout(null);
                geraCelulas(novoPainel);

                // Remove o conteúdo atual e adiciona o novo painel
                getContentPane().removeAll();
                getContentPane().add(novoPainel);
                revalidate();
                repaint();
            }
        });

        // Definição das fontes
        recomecar.setFont(new Font("Calibri", Font.BOLD, 18));
        ajuda.setFont(new Font("Calibri", Font.BOLD, 18));
        aparencia.setFont(new Font("Calibri", Font.BOLD, 18));
        quantMinas.setFont(new Font("Calibri", Font.BOLD, 18));
        quantBandeiras.setFont(new Font("Calibri", Font.BOLD, 30));
        cronometro.setFont(new Font("Calibri", Font.BOLD, 22));

    }

    public void exibirAjuda(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Restrições:\r\n" + "→ Qualquer duplo clique não terá função;\r\n"
                + "→ Não é possível escolher duas casas simultaneamente.\r\n\n" + "Condições:\r\n"
                + "→Se uma mina for revelada, o jogo acaba;\r\n"
                + "→Se um quadrado vazio for revelado, o jogo continua;\r\n"
                + "→Se um número for revelado, ele informará quantas minas estão escondidas nos 8 "
                + "quadrados que o cercam;\r\n"
                + "→Se o botão direito for clicado sobre um quadrado, não marcado anteriormente, será marcado com uma "
                + "bandeira;\r\n"
                + "→Se o botão direito for clicado novamente sobre uma bandeira, será marcado um ponto de interrogação;\r\n"
                + "→Se o botão esquerdo for clicado sobre um quadrado já marcado, não acontecerá nada.");
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

        for (JButton celula : painel.getCelulas()) {
            if (!celula.getText().contains("aberta")) {
                celula.setBackground(corEscolhida);
                if (corEscolhida == painel.getVermelho()) {
                    if (celula.getIcon() != null) {
                        celula.setIcon(painel.getIconBandeiraAzul());

                    }
                }
                if (corEscolhida == painel.getAzulClaro()) {
                    if (celula.getIcon() != null) {
                        celula.setIcon(painel.getIconBandeiraVermelha());
                    }
                }
            }
        }
    }

    public void recomecar(ActionEvent e) {
        this.dispose();
        MainCampoMinado main = new MainCampoMinado();
        main.executaJogo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void adicionaCelula(JButton celula) {
        painel.getCelulas().add(celula);
    }

}
