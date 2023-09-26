package minimizador_lfa;

public class Transicoes {
    private String estadoA;
    private String simbolo;
    private String estadoB;

    public String getEstadoA() {
        return estadoA;
    }

    public Transicoes(String estadoA, String simbolo, String estadoB) {
        this.estadoA = estadoA;
        this.simbolo = simbolo;
        this.estadoB = estadoB;
    }

    public void setEstadoA(String estadoA) {
        this.estadoA = estadoA;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getEstadoB() {
        return estadoB;
    }

    public void setEstadoB(String estadoB) {
        this.estadoB = estadoB;
    }
    
    
}
