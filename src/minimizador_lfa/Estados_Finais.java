package minimizador_lfa;
public class Estados_Finais {
    private String el_final;

    public Estados_Finais(String el_final) {
        this.el_final = el_final;
    }

    public String getEl_final() {
        return el_final;
    }

    public void setEl_final(String el_final) {
        this.el_final = el_final;
    }

    @Override
    public String toString() {
        return "Estados_Finais{" + "el_final=" + el_final + '}';
    }
    
}
