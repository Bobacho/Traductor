package org.example.models;

public class Tokens {
    private int Id;

    public int getId() {
        return Id;
    }


    public Tokens() {
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public org.example.models.Tipo getTipo() {
        return Tipo;
    }

    public void setTipo(org.example.models.Tipo tipo) {
        Tipo = tipo;
    }

    private String Nombre;
    private String Descripcion;
    private Tipo Tipo;

    @Override
    public String toString() {
        return "\nTokens{" +
                "Id=" + Id +
                ", Nombre='" + Nombre + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Tipo=" + Tipo +
                '}';
    }
}
