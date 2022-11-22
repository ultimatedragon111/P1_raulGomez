package model;

public class Carta {
    public int id_jugador;
    public String numero;
    public String color;
    public Carta(int id_jugador){
        this.id_jugador = id_jugador;
    }
    public Carta(int id_jugador, String numero , String color){
        this.id_jugador = id_jugador;
        this.numero = numero;
        this.color = color;
    }

    public int getId_jugador() {
        return id_jugador;
    }

    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
