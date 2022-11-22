package controller;

import dao.Dao;
import model.Carta;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);
    Dao dao;
    int id_Jugador;
    ArrayList<Carta> mano = new ArrayList<Carta>();
    public Controller(){
        dao = new Dao();
    }
    public void init() {
        try {
            dao.connectar();
            id_Jugador = inicioSesion();
            if(id_Jugador != 0){
                System.out.println("Inicio de sesion correcto");
                if(comprovarCartas().size() == 0){
                    System.out.println("Necesitas cartas");
                    while(mano.size() < 7){
                        addCarta();
                    }
                }
                else{
                    System.out.println("Ya tienes cartas");
                }
            }
            else{
                System.out.println("Inicio de sesion incorrecto");
            }
            dao.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int inicioSesion() throws SQLException {
        System.out.println("Nombre de usuario : ");
        String nombre = sc.nextLine();
        System.out.println("Contraseña : ");
        String con = sc.nextLine();
        return dao.inicioSesion(nombre,con);

    }
    private ArrayList<Carta> comprovarCartas(){
        return dao.comprovarCartas(id_Jugador);
    }

    private void addCarta(){
        Carta carta = new Carta(id_Jugador);
        Color color;
        carta.setNumero(Numero.randomNumero().toString());
        if (carta.getNumero().equals(Numero.MASCUATRO.toString()) || carta.getNumero().equals(Numero.CAMBIOCOLOR.toString())){
            carta.setColor(Color.NEGRO.toString());
        }
        else {
            carta.setColor(Color.randomColor().toString());
        }
        dao.addCarta(carta);
        mano.add(carta);



    }
}
