package controller;

import dao.Dao;
import model.Carta;
import model.Color;
import model.Numero;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);
    Dao dao;
    int id_Jugador;
    int count =0;
    int opcionCarta = 0;
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
                addCartaMano();
                if(mano.size() == 0){
                    System.out.println("Necesitas cartas");
                    addCartaBase(7);
                    addCartaMano();
                    System.out.println("Has robado 7 cartas");
                }
                System.out.println(mano.size());
                while(opcionCarta == 0){
                    menuCartas();
                    opcionCarta = sc.nextInt();
                    switch (opcionCarta){
                        case (-1):
                            addCartaBase(1);
                            addCartaMano();
                            opcionCarta = 0;
                            break;
                        default:
                            addCartaMesa(mano.get(opcionCarta));
                    }


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



    private void addCartaBase(int numCartas){
        for(int x = 0 ; x<numCartas ; x++) {
            Carta carta = new Carta(id_Jugador);
            carta.setNumero(Numero.randomNumero().toString());
            if (carta.getNumero().equals(Numero.MASCUATRO.toString()) || carta.getNumero().equals(Numero.CAMBIOCOLOR.toString())) {
                carta.setColor(Color.NEGRO.toString());
            } else {
                carta.setColor(Color.randomColor().toString());
            }
            dao.addCarta(carta);
        }
    }
    private void addCartaMano(){
        mano = dao.comprovarCartas(id_Jugador);
    }
    private void menuCartas(){
        count =0;
        for (Carta carta : mano){
            System.out.println( count + "- " + carta.toString());
            count++;
        }
        System.out.println("Si quieres robar una carta escribe -1");
    }
    private Carta comprovarMesa(){
        Carta carta = new Carta(id_Jugador);
        return carta;
    }
    private void addCartaMesa(Carta carta){
        dao.addCartaTable(carta);
    }

}
