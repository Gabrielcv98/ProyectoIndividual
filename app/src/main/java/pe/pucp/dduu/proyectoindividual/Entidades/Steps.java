package pe.pucp.dduu.proyectoindividual.Entidades;

public class Steps {
    private String id;
    private String nombre;
    private String creador;
    private String url;
    private String pais;

    public Steps(String nombre, String creador, String url, String pais) {
        this.nombre = nombre;
        this.creador = creador;
        this.url = url;
        this.pais = pais;
    }

    public Steps() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }


    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
