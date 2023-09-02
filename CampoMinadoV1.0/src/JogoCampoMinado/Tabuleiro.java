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

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // Declaração dos atributos do tabuleiro
    private int numLinhas;
    private int numColunas;
    private int minas;
    private int jogadas;
    private int bandeiras;
    private int numBandeiras;
    private int tempo;
    private ArrayList<JButton> celulas;

    // Declaração das cores que são usadas no jogo.
    Color azul = new Color(0, 91, 255);
    Color verde = new Color(0, 220, 0);
    Color azulClaro = new Color(30, 144, 255);
    Color azulClaro2 = new Color(30, 144, 254);
    Color vermelho = new Color(255, 40, 50);
    Color cinza = new Color(211, 211, 211);

    // Declaração dos ícones que são usadas no jogo.
    ImageIcon iconMina = new ImageIcon(getClass().getResource("/midia/mina.png"));
    ImageIcon iconBandeira = new ImageIcon(getClass().getResource("/midia/bandeira2.png"));
    ImageIcon iconBandeiraAzul = new ImageIcon(getClass().getResource("/midia/bandeiraAzul2.png"));
    ImageIcon iconInterrogacao = new ImageIcon(getClass().getResource("/midia/interrogacao.png"));
    ImageIcon iconRelogio = new ImageIcon(getClass().getResource("/midia/relogio.png"));
    ImageIcon iconUm = new ImageIcon(getClass().getResource("/midia/1.png"));
    ImageIcon iconDois = new ImageIcon(getClass().getResource("/midia/2.png"));
    ImageIcon iconTres = new ImageIcon(getClass().getResource("/midia/3.png"));
    ImageIcon iconQuatro = new ImageIcon(getClass().getResource("/midia/4.png"));
    ImageIcon iconCinco = new ImageIcon(getClass().getResource("/midia/5.png"));
    ImageIcon iconSeis = new ImageIcon(getClass().getResource("/midia/6.png"));
    ImageIcon iconSete = new ImageIcon(getClass().getResource("/midia/7.png"));
    ImageIcon iconOito = new ImageIcon(getClass().getResource("/midia/8.png"));
    ImageIcon iconTrofeu = new ImageIcon(getClass().getResource("/midia/trofeu.png"));
    ImageIcon iconExplosao = new ImageIcon(getClass().getResource("/midia/explosao.png"));
    ImageIcon iconAjuda = new ImageIcon(getClass().getResource("/midia/iconAjuda.png"));

    // Declaração do campo que informa a quantidade de bandeiras colocadas pelo(a)
    // jogador(a) no tabuleiro.
    JLabel quantBandeiras = new JLabel();
    JLabel cronometro = new JLabel();
    JButton ajuda = new JButton();
    JButton apVermelho = new JButton();
    JButton apVerde = new JButton();
    JButton apAzul = new JButton();

    Timer tm = new Timer();
    // Timer tm2 = new Timer();

    // Declaração da variável que permite o jogador jogar novamente.
    int jogarNovamente;

    public Tabuleiro() {

        // Declaração das propriedades do tabuleiro/JFrame
        setLayout(new BorderLayout());
        setTitle("Campo Minado");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        getContentPane().setBackground(Color.white);

        celulas = new ArrayList<JButton>();
        tempo = 0;
        numBandeiras = 0;
    }

    /**
     * Método que gera as células no tabuleiro respeitando as características do
     * nível escolhido
     *
     * @throws InterruptedException
     */
    public void geraCelulas(JPanel painel) {

        for (int x = 0; x < this.numColunas; x++) {

            for (int y = 0; y < this.numLinhas; y++) {

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
                            if (celula.getBackground() == vermelho) {
                                bandeira = iconBandeiraAzul;
                            } else {
                                bandeira = iconBandeira;
                            }
                            if (!celula.getText().contains("aberta")) {
                                if (celula.getIcon() == null) {
                                    // Tira a bandeira da célula.
                                    celula.setIcon(bandeira);
                                    numBandeiras += 1;
                                } else if (celula.getIcon() == bandeira) {
                                    // Coloca a bandeira na célula.
                                    celula.setIcon(iconInterrogacao);
                                    numBandeiras -= 1;
                                } else if (celula.getIcon() == iconInterrogacao) {
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
        for (JButton celula : celulas) {
            celula.setBackground(this.azulClaro);
        }
        for (JButton celula : celulas) {
            celula.setBackground(this.azulClaro2);
        }
    }

    public void updateBandeiras() {

        if (numBandeiras > minas) {
            quantBandeiras.setForeground(vermelho);
        } else {
            quantBandeiras.setForeground(new Color(0, 0, 0));
        }
        quantBandeiras.setText("" + numBandeiras);
    }

    public void click(ActionEvent e) {

        if (tempo == 0) {
            final long segundo = (1000);

            TimerTask tarefa = new TimerTask() {

                @Override
                public void run() {
                    updateTempo();
                    tempo++;
                }

            };

            tm.scheduleAtFixedRate(tarefa, 0, segundo);
        }

        for (JButton celula : celulas) {
            if (e.getSource() == celula) {
                if (celula.getIcon() == null) {
                    this.jogadas++;
                    if (this.jogadas == 1) {
                        geraMinas(celula);
                        abreCelula(celula);
                    }

                    if (!celula.getText().contains("aberta")) {
                        if (celula.getText().contains("mina")) {
                            tm.cancel();
                            emiteSom("explosao");
                            //abreMinas(true);
                            celula.setIcon(iconMina);
                            celula.setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));
                        } else {
                            emiteSom("abertura");
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

        while (i < this.minas) {
            int limiteHorizontal = this.numColunas - 2;
            int limiteVertical = this.numLinhas - 2;
            int x = aleatorio.nextInt(limiteHorizontal);
            int y = aleatorio.nextInt(limiteVertical);
            for (JButton celula : celulas) {
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

        for (JButton celula : celulas) {
            int minas = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        int x = Integer.parseInt(celula.getText().substring(0, 2)) + dx;
                        int y = Integer.parseInt(celula.getText().substring(3, 5)) + dy;
                        if (x >= 0 && x < this.numColunas && y >= 0 && y < this.numLinhas) {
                            for (JButton celulaCheca : celulas) {
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

            for (JButton c : celulas) {
                if (c == celulaAberta) {

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
                            int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
                            if (x >= 0 && x < this.numColunas && y >= 0 && y < this.numLinhas) {
                                for (JButton celulaCheca : celulas) {
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

            for (JButton c : celulas) {

                if (!c.getText().contains("aberta")) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
                            int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
                            if (x >= 0 && x < this.numColunas && y >= 0 && y < this.numLinhas) {
                                for (JButton celulaCheca : celulas) {
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
        for (JButton c : celulas) {
            if (c.getText().contains("aberta")) {
                c.setBackground(cinza);
                defineNumero(c);
            }
        }
        checaCelulas();
    }

    public void defineNumero(JButton celula) {

        for (JButton c : celulas) {
            if (c == celula) {

                if (!celula.getText().substring(8, 9).equals("X")) {
                    int quantMinas = Integer.parseInt(celula.getText().substring(8, 9));

                    switch (quantMinas) {

                        case 0:
                            c.setIcon(null);
                            break;
                        case 1:
                            c.setBackground(this.cinza);
                            c.setIcon(iconUm);
                            break;
                        case 2:
                            c.setBackground(this.cinza);
                            c.setIcon(iconDois);
                            break;
                        case 3:
                            c.setBackground(this.cinza);
                            c.setIcon(iconTres);
                            break;
                        case 4:
                            c.setBackground(this.cinza);
                            c.setIcon(iconQuatro);
                            break;
                        case 5:
                            c.setBackground(this.cinza);
                            c.setIcon(iconCinco);
                            break;
                        case 6:
                            c.setBackground(this.cinza);
                            c.setIcon(iconSeis);
                            break;
                        case 7:
                            c.setBackground(this.cinza);
                            c.setIcon(iconSete);
                            break;
                        case 8:
                            c.setBackground(this.cinza);
                            c.setIcon(iconOito);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void checaCelulas() {

        int celulasAbertas = 0;

        for (JButton celula : celulas) {
            if (celula.getText().contains("aberta")) {
                celulasAbertas++;
            }
        }

        if (celulasAbertas == this.numColunas * this.numLinhas - this.minas) {
            tm.cancel();
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
                    "Você perdeu o jogo na " + this.jogadas + "ª jogada. " + "\nDeseja jogar novamente?", "Confirmação",
                    JOptionPane.YES_NO_OPTION, 0, iconExplosao);
        } else {
            for (JButton celula : celulas) {
                if (celula.getText().contains("mina")) {
                    celula.setIcon(iconMina);
                    celula.setBackground(cinza);
                }
            }

            int minutos = tempo / 60;
            int segundos = tempo % 60;
            this.jogarNovamente = JOptionPane.showConfirmDialog(null,
                    "Parabéns, você ganhou o jogo!!!" + "\nCom " + this.jogadas + " jogadas! " + "\nTempo: " + minutos
                    + "m" + segundos + "s." + "\n\n Deseja jogar novamente?",
                    "Confirmação", JOptionPane.YES_NO_OPTION, 0, iconTrofeu);

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
        if (tempo < 60) {
            cronometro.setText(tempo + "s");
        } else {
            cronometro.setText("" + tempo / 60 + "m" + tempo % 60 + "s");
        }
    }

    public void criaMenu(JPanel painel) {
        JButton recomecar = new JButton();
        JLabel quantMinas = new JLabel("Quant. de minas: " + this.minas);
        JLabel aparencia = new JLabel("Mudar aparência:");

        painel.add(recomecar);
        painel.add(quantMinas);
        painel.add(aparencia);
        painel.add(apVermelho);
        painel.add(apVerde);
        painel.add(apAzul);
        painel.add(quantBandeiras);
        painel.add(cronometro);
        painel.add(ajuda);
        
        this.add(painel);
        configuraBtnsMenu(recomecar, quantMinas, aparencia, apVermelho, apVerde, apAzul, quantBandeiras, cronometro,
                ajuda);
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
        quantBandeiras.setText("" + numBandeiras);
        cronometro.setBounds(20, this.getHeight() - 150, 180, 50);
        cronometro.setText(tempo + "s");

        ajuda.setBounds(0, this.getHeight() - 90, 180, 50);

        // Demais configurações dos botões do menu
        ajuda.setIcon(iconAjuda);
        ajuda.setBackground(new Color(1.0f, 1.0f, 1.0f, 0f));
        ajuda.setBorder(null);
        ajuda.setFocusable(false);
        ajuda.setText("Como jogar?");
        ajuda.addActionListener(this::exibirAjuda);

        apVermelho.setBackground(vermelho);
        apVermelho.addActionListener(this::mudarAparencia);
        apVerde.setBackground(verde);
        apVerde.addActionListener(this::mudarAparencia);
        apAzul.setBackground(azulClaro);
        apAzul.addActionListener(this::mudarAparencia);

        quantBandeiras.setIcon(iconBandeira);
        cronometro.setIcon(iconRelogio);

        recomecar.setText("Recomeçar");
        recomecar.setBackground(new Color(255, 255, 255));
        recomecar.setFocusable(false);
        recomecar.addActionListener(this::recomecar);
        recomecar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cria um novo painel para substituir o conteúdo atual
                JPanel novoPainel = new JPanel();
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

        if (e.getSource() == apVermelho) {
            corEscolhida = vermelho;
            quantBandeiras.setIcon(iconBandeiraAzul);
        } else if (e.getSource() == apVerde) {
            corEscolhida = verde;
        } else {
            corEscolhida = azulClaro;
            quantBandeiras.setIcon(iconBandeira);
        }

        for (JButton celula : celulas) {
            if (!celula.getText().contains("aberta")) {
                celula.setBackground(corEscolhida);
                if (corEscolhida == vermelho) {
                    if (celula.getIcon() != null) {
                        celula.setIcon(iconBandeiraAzul);

                    }
                }
                if (corEscolhida == azulClaro) {
                    if (celula.getIcon() != null) {
                        celula.setIcon(iconBandeira);
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

    public int getNumLinhas() {
        return numLinhas;
    }

    public void setNumLinhas(int numLinhas) {
        this.numLinhas = numLinhas;
    }

    public int getNumColunas() {
        return numColunas;
    }

    public void setNumColunas(int numColunas) {
        this.numColunas = numColunas;
    }

    public void setMinas(int minas) {
        this.minas = minas;
    }

    public int getMinas() {
        return minas;
    }

    public ArrayList<JButton> getCelulas() {
        return celulas;
    }

    public void setCelulas(ArrayList<JButton> celulas) {
        this.celulas = celulas;
    }

    public void adicionaCelula(JButton celula) {
        this.celulas.add(celula);
    }

    public int getNumBandeiras() {
        return numBandeiras;
    }

    public void setNumBandeiras(int numBandeiras) {
        this.numBandeiras = numBandeiras;
    }

}
