package minimizador_lfa;

class Estados {
    private String estado;
    private String estado_fim;

    public Estados(String estado, String estado_fim) {
        this.estado = estado;
        this.estado_fim = estado_fim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado_fim() {
        return estado_fim;
    }

    public void setEstado_fim(String estado_fim) {
        this.estado_fim = estado_fim;
    }

    
}
