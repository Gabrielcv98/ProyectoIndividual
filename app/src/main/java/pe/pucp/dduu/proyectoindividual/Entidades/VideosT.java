package pe.pucp.dduu.proyectoindividual.Entidades;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class VideosT {
    private String nombre;
    private String fecha;
    private Task<Uri> descarga;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Task<Uri> getDescarga() {
        return descarga;
    }

    public void setDescarga(Task<Uri> descarga) {
        this.descarga = descarga;
    }
}
