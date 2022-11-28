package model;

public class Carta {
    public int id_carta;
    public int id_jugador;
    public String numero;
    public String color;
    public int estat;
    public Carta(int id_jugador){
        this.id_jugador = id_jugador;
    }
    public Carta(int id_carta,int id_jugador, String numero , String color, int estat){
        this.id_carta = id_carta;
        this.id_jugador = id_jugador;
        this.numero = numero;
        this.color = color;
        this.estat = estat;
    }
    public Carta(int id_carta, String numero , String color, int estat){
        this.id_carta = id_carta;
        this.numero = numero;
        this.color = color;
        this.estat = estat;
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

    public int getId_carta() {
        return id_carta;
    }

    public void setId_carta(int id_carta) {
        this.id_carta = id_carta;
    }

    public int getEstat() {
        return estat;
    }

    public void setEstat(int estat) {
        this.estat = estat;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "id_carta=" + id_carta +
                ", id_jugador=" + id_jugador +
                ", numero='" + numero + '\'' +
                ", color='" + color + '\'' +
                ", estat=" + estat +
                '}';
    }
}
