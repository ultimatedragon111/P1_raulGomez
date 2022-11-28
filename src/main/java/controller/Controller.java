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
    int opcionCarta = -2;
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
                System.out.println("Carta de la mesa");
                if(checkCartaMesa() != null){
                    System.out.println(checkCartaMesa().toString());
                    switch(checkCartaMesa().getNumero()){
                        case "MASDOS":
                            if(checkCartaMesa().getEstat() == 0){
                                addCartaBase(2);
                                updateEstat();
                                System.out.println("Robas dos cartas");
                                opcionCarta = 0;
                            }
                            break;
                        case "MASCUATRO":
                            if(checkCartaMesa().getEstat() == 0){
                                addCartaBase(4);
                                updateEstat();
                                System.out.println("Robas cuatro cartas");
                                opcionCarta = 0;
                            }
                            break;
                        case "SALTO":
                        case "CAMBIO":
                            if(checkCartaMesa().getEstat() == 0){
                                System.out.println("Salto de turno");
                                updateEstat();
                                opcionCarta = 0;
                            }
                            break;
                    }
                }
                else{
                    System.out.println("No hay carta de en la mesa");
                }
                addCartaMano();
                if(mano.size() == 0){
                    System.out.println("Necesitas cartas");
                    addCartaBase(7);
                    addCartaMano();
                    System.out.println("Has robado 7 cartas");
                }
                while(opcionCarta == -2){
                    menuCartas();
                    opcionCarta = seleccionCarta(opcionCarta);
                    if(opcionCarta == (-1)) {
                        addCartaBase(1);
                        addCartaMano();
                        opcionCarta = -2;
                    }
                    else{
                        if(checkCartaMesa() == null){
                            addCartaMesa(mano.get(opcionCarta));
                        }
                        else if(checkCartaMesa().getColor().equals("NEGRO") || checkCartaMesa().getColor().equals(mano.get(opcionCarta).getColor()) || mano.get(opcionCarta).getColor().equals("NEGRO")){
                            addCartaMesa(mano.get(opcionCarta));
                        }
                        else{
                          /*  if(checkCartaMesa().getNumero().equals(mano.get(opcionCarta).getNumero())){
                                addCartaMesa(mano.get(opcionCarta));
                            }
                            else{

                            }*/
                            System.out.println("No se puede jugar la carta");
                            opcionCarta = -2;
                        }

                    }
                }
                addCartaMano();
                if (mano.size() == 0){
                    updateNumPartida();
                    updatePartidasGanadas(id_Jugador);
                    deleteGame();
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

    }
    private int seleccionCarta(int opcionCarta){
        while(opcionCarta == -2) {
            System.out.println("Selecciona una carta , si quieres robar una carta escribe -1");
            opcionCarta = sc.nextInt();
            if(opcionCarta < -1 || opcionCarta > mano.size()-1){
                opcionCarta = -2;
                System.out.println("Opcion no valida");
            }
        }
        return opcionCarta;
    }
    private Carta comprovarMesa(){
        Carta carta = new Carta(id_Jugador);
        return carta;
    }
    private void addCartaMesa(Carta carta){
        dao.addCartaTable(carta);
    }
    private Carta checkCartaMesa(){
        return dao.checkCardTable();
    }
    private void updateEstat(){
        dao.updateEstat(checkCartaMesa());
    }
    private void updateNumPartida(){
        dao.updateGamePlus();
    }
    private void updatePartidasGanadas(int id_Jugador){
        dao.updateWinPlus(id_Jugador);
    }
    private void deleteGame(){
        dao.deleteGame();
        dao.deleteCards();
    }

}
