package JogoCampoMinado;

import java.util.ArrayList;

import javax.swing.JButton;

public class Celula extends JButton {

    int coordX;
    int coordY;
    int quantMinasVizinhas;
    boolean mina;
    boolean aberta;
    
    public Celula() {
    }

    public Celula(boolean aberta, boolean mina, int quantMinasVizinhas){
        this.aberta = aberta;
        this.mina = mina;
        this.quantMinasVizinhas = quantMinasVizinhas;
    }
    
    public int getQuantMinasVizinhas() {
        return quantMinasVizinhas;
    }

    public void setQuantMinasVizinhas(int quantMinasVizinhas) {
        this.quantMinasVizinhas = quantMinasVizinhas;
    }

    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public boolean isMina() {
        return mina;
    }

    public void setMina(boolean mina) {
        this.mina = mina;
    }

    public boolean isAberta() {
        return aberta;
    }

    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }
    
    

}
