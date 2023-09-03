/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JogoCampoMinado;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author NumbERS
 */
public class ConteudoPainel extends JPanel {
    
    // Declaração dos atributos do tabuleiro
    private int quantMinas;
    private int numJogadas;
    private int numLinhas;
    private int numColunas;
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
    ImageIcon iconBandeiraVermelha = new ImageIcon(getClass().getResource("/midia/bandeira2.png"));
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
    JLabel labelQuantBandeiras;
    JLabel labelCronometro;
    JLabel labelQuantMinas;
    JLabel labelAparencia;
    JButton btnAjuda;
    JButton btnAparenciaVermelho = new JButton();
    JButton btnAparenciaVerde = new JButton();
    JButton btnAparenciaAzul = new JButton();
    
    Timer tm = new Timer();
    
    public ConteudoPainel(){
        setLayout(null);
        celulas = new ArrayList<>();
        labelQuantBandeiras = new JLabel();
        labelCronometro = new JLabel();
        btnAjuda = new JButton();
        tempo = 0;
        numBandeiras = 0;
        numJogadas = 0;
    }

    public int getQuantMinas() {
        return quantMinas;
    }

    public void setQuantMinas(int quantMinas) {
        this.quantMinas = quantMinas;
    }

    public int getNumJogadas() {
        return numJogadas;
    }

    public void setNumJogadas(int numJogadas) {
        this.numJogadas = numJogadas;
    }

    public int getBandeiras() {
        return bandeiras;
    }

    public void setBandeiras(int bandeiras) {
        this.bandeiras = bandeiras;
    }

    public int getNumBandeiras() {
        return numBandeiras;
    }

    public void setNumBandeiras(int numBandeiras) {
        this.numBandeiras = numBandeiras;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public ArrayList<JButton> getCelulas() {
        return celulas;
    }

    public void setCelulas(ArrayList<JButton> celulas) {
        this.celulas = celulas;
    }

    public Color getAzul() {
        return azul;
    }

    public void setAzul(Color azul) {
        this.azul = azul;
    }

    public Color getVerde() {
        return verde;
    }

    public void setVerde(Color verde) {
        this.verde = verde;
    }

    public Color getAzulClaro() {
        return azulClaro;
    }

    public void setAzulClaro(Color azulClaro) {
        this.azulClaro = azulClaro;
    }

    public Color getAzulClaro2() {
        return azulClaro2;
    }

    public void setAzulClaro2(Color azulClaro2) {
        this.azulClaro2 = azulClaro2;
    }

    public Color getVermelho() {
        return vermelho;
    }

    public void setVermelho(Color vermelho) {
        this.vermelho = vermelho;
    }

    public Color getCinza() {
        return cinza;
    }

    public void setCinza(Color cinza) {
        this.cinza = cinza;
    }

    public ImageIcon getIconMina() {
        return iconMina;
    }

    public void setIconMina(ImageIcon iconMina) {
        this.iconMina = iconMina;
    }

    public ImageIcon getIconBandeiraVermelha() {
        return iconBandeiraVermelha;
    }

    public void setIconBandeiraVermelha(ImageIcon iconBandeiraVermelha) {
        this.iconBandeiraVermelha = iconBandeiraVermelha;
    }

    public ImageIcon getIconBandeiraAzul() {
        return iconBandeiraAzul;
    }

    public void setIconBandeiraAzul(ImageIcon iconBandeiraAzul) {
        this.iconBandeiraAzul = iconBandeiraAzul;
    }

    public ImageIcon getIconInterrogacao() {
        return iconInterrogacao;
    }

    public void setIconInterrogacao(ImageIcon iconInterrogacao) {
        this.iconInterrogacao = iconInterrogacao;
    }

    public ImageIcon getIconRelogio() {
        return iconRelogio;
    }

    public void setIconRelogio(ImageIcon iconRelogio) {
        this.iconRelogio = iconRelogio;
    }

    public ImageIcon getIconUm() {
        return iconUm;
    }

    public void setIconUm(ImageIcon iconUm) {
        this.iconUm = iconUm;
    }

    public ImageIcon getIconDois() {
        return iconDois;
    }

    public void setIconDois(ImageIcon iconDois) {
        this.iconDois = iconDois;
    }

    public ImageIcon getIconTres() {
        return iconTres;
    }

    public void setIconTres(ImageIcon iconTres) {
        this.iconTres = iconTres;
    }

    public ImageIcon getIconQuatro() {
        return iconQuatro;
    }

    public void setIconQuatro(ImageIcon iconQuatro) {
        this.iconQuatro = iconQuatro;
    }

    public ImageIcon getIconCinco() {
        return iconCinco;
    }

    public void setIconCinco(ImageIcon iconCinco) {
        this.iconCinco = iconCinco;
    }

    public ImageIcon getIconSeis() {
        return iconSeis;
    }

    public void setIconSeis(ImageIcon iconSeis) {
        this.iconSeis = iconSeis;
    }

    public ImageIcon getIconSete() {
        return iconSete;
    }

    public void setIconSete(ImageIcon iconSete) {
        this.iconSete = iconSete;
    }

    public ImageIcon getIconOito() {
        return iconOito;
    }

    public void setIconOito(ImageIcon iconOito) {
        this.iconOito = iconOito;
    }

    public ImageIcon getIconTrofeu() {
        return iconTrofeu;
    }

    public void setIconTrofeu(ImageIcon iconTrofeu) {
        this.iconTrofeu = iconTrofeu;
    }

    public ImageIcon getIconExplosao() {
        return iconExplosao;
    }

    public void setIconExplosao(ImageIcon iconExplosao) {
        this.iconExplosao = iconExplosao;
    }

    public ImageIcon getIconAjuda() {
        return iconAjuda;
    }

    public void setIconAjuda(ImageIcon iconAjuda) {
        this.iconAjuda = iconAjuda;
    }

    public JLabel getLabelQuantBandeiras() {
        return labelQuantBandeiras;
    }

    public void setLabelQuantBandeiras(JLabel labelQuantBandeiras) {
        this.labelQuantBandeiras = labelQuantBandeiras;
    }

    public JLabel getLabelCronometro() {
        return labelCronometro;
    }

    public void setLabelCronometro(JLabel labelCronometro) {
        this.labelCronometro = labelCronometro;
    }

    public JButton getBtnAjuda() {
        return btnAjuda;
    }

    public void setBtnAjuda(JButton btnAjuda) {
        this.btnAjuda = btnAjuda;
    }

    public JButton getBtnAparenciaVermelho() {
        return btnAparenciaVermelho;
    }

    public void setBtnAparenciaVermelho(JButton btnAparenciaVermelho) {
        this.btnAparenciaVermelho = btnAparenciaVermelho;
    }

    public JButton getBtnAparenciaVerde() {
        return btnAparenciaVerde;
    }

    public void setBtnAparenciaVerde(JButton btnAparenciaVerde) {
        this.btnAparenciaVerde = btnAparenciaVerde;
    }

    public JButton getBtnAparenciaAzul() {
        return btnAparenciaAzul;
    }

    public void setBtnAparenciaAzul(JButton btnAparenciaAzul) {
        this.btnAparenciaAzul = btnAparenciaAzul;
    }

    public Timer getTm() {
        return tm;
    }

    public void setTm(Timer tm) {
        this.tm = tm;
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

    public JLabel getLabelQuantMinas() {
        return labelQuantMinas;
    }

    public void setLabelQuantMinas(JLabel labelQuantMinas) {
        this.labelQuantMinas = labelQuantMinas;
    }

    public JLabel getLabelAparencia() {
        return labelAparencia;
    }

    public void setLabelAparencia(JLabel labelAparencia) {
        this.labelAparencia = labelAparencia;
    }   
}
