package controller;

import dao.Dao;
import model.Carta;
import utils.Color;
import utils.Numero;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Scanner sc = new Scanner(System.in);
    Dao dao;
    int id_Jugador;
    int opcionCarta = -2;
    Carta cartaMesa;
    ArrayList<Carta> mano = new ArrayList<Carta>();
    public Controller(){
        dao = new Dao();
    }
    public void init() {
        try {
            dao.connectar();
            if (menuPrincipal() == 1){
                crearUsuario();
            }
            else {
                id_Jugador = inicioSesion();
                if (id_Jugador != 0) {
                    System.out.println("Inicio de sesion correcto");
                    addCartaMano();
                    if (mano.size() == 0) {
                        robarManoNueva();
                    }
                    cartaMesa = checkCartaMesa();
                    System.out.println("Carta de la mesa");
                    if (cartaMesa != null) {
                        System.out.println(cartaMesa.toString());
                        switch (cartaMesa.getNumero()) {
                            case "MASDOS":
                            case "MASCUATRO":
                                opcionCarta = masCuatroComprovacion(cartaMesa);
                                break;
                            case "SALTO":
                            case "CAMBIO":
                                opcionCarta = saltoTurno(cartaMesa);
                                break;
                        }
                    } else {
                        System.out.println("No hay carta de en la mesa");
                    }
                    jugarCartaComprovacion(opcionCarta, cartaMesa);
                    addCartaMano();
                    comprovarVictoria();
                } else {
                    System.out.println("Inicio de sesion incorrecto");
                }
            }
            dao.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int inicioSesion() throws SQLException {
        System.out.println("Nombre de usuario : ");
        sc.nextLine();
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
    private int menuCartas(String text){
        int opcionCarta = -2;
        int count = 0;
        for (Carta carta : mano){
            System.out.println( count + "- " + carta.toString());
            count++;
        }
        return seleccionCarta(opcionCarta,text);

    }
    private int seleccionCarta(int opcionCarta , String text){
        while(opcionCarta == -2) {
            System.out.println(text);
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
    private void robarManoNueva(){
        System.out.println("Necesitas cartas");
        addCartaBase(7);
        addCartaMano();
        System.out.println("Has robado 7 cartas");
    }
    private void addCartaMesa(Carta carta){
        dao.addCartaTable(carta);
    }
    private Carta checkCartaMesa(){
        return dao.checkCardTable();
    }
    private void updateEstat(){
        dao.updateEstat();
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
    private boolean cartaJugarMesa(Carta carta,Carta cartaMesa){
        if(cartaMesa == null){
            addCartaMesa(carta);
            return true;
        }
        if(cartaMesa.getNumero().equals(Numero.MASDOS.toString()) && cartaMesa.getEstat() == 0){
            if(carta.getNumero().equals(Numero.MASDOS.toString()) || carta.getNumero().equals(Numero.MASCUATRO.toString())){
                addCartaMesa(carta);
                return true;
            }
            else{
                return false;
            }
        }
        else if(cartaMesa.getNumero().equals(Numero.MASCUATRO.toString()) && cartaMesa.getEstat() == 0){
            if(carta.getNumero().equals(Numero.MASCUATRO.toString())){
                addCartaMesa(carta);
                return true;
            }
            else{
                return false;
            }
        }
        else if(cartaMesa.getColor().equals(Color.NEGRO.toString()) || cartaMesa.getColor().equals(carta.getColor()) || carta.getColor().equals(Color.NEGRO.toString())){
            addCartaMesa(carta);
            return true;
        }
        else{
            if(cartaMesa.getNumero().equals(carta.getNumero())){
                addCartaMesa(carta);
                return true;
            }
            else{
                System.out.println("No se puede jugar la carta");
            }
        }
        return false;
    }
    private void totalDrawCards(){
        int numCards = dao.drawCards();
        System.out.println("Vas a robar " + numCards + " cartas");
        addCartaBase(numCards);
        updateEstat();
    }
    private void comprovarVictoria(){
        if (mano.size() == 0){
            updateNumPartida();
            updatePartidasGanadas(id_Jugador);
            deleteGame();
        }
    }
    private int menuPrincipal(){
        int a = 0;
        while(a<1 || a>2 ){
            System.out.println("1- Crea Usuario");
            System.out.println("2- Iniciar Sesion");
            a = sc.nextInt();

            if (a<1 || a>2 ){
                System.out.println("Opcion no valida");
            }
        }
        return a;
    }
    private void crearUsuario(){
        String name,username,password;
        System.out.println("Escribe el usuario que quieres: ");
        sc.nextLine();
        username = sc.nextLine();
        System.out.println("Escribe la contraseña que quieres: ");
        password = sc.nextLine();
        System.out.println("Escribe el nombre que quieres: ");
        name = sc.nextLine();
        dao.createUser(username,password,name);
        System.out.println("Usuario creado");
    }
    private int masCuatroComprovacion(Carta cartaMesa){
        if (cartaMesa.getEstat() == 0) {
            int opcionCarta = menuCartas("Seleciona una carta jugable o -1 para robar la pila de MASDOS Y MASCUATRO acumuladas (Si pones una carta no valida robaras todas las cartas)");
            if (opcionCarta == (-1)) {
                totalDrawCards();
            } else if (cartaJugarMesa(mano.get(opcionCarta), cartaMesa)) {
                System.out.println("Carta jugada " + mano.get(opcionCarta).toString());
            } else {
                totalDrawCards();
            }
            return 0;
        }
        return -2;
    }
    private void jugarCartaComprovacion(int opcionCarta ,Carta cartaMesa) {
        while (opcionCarta == -2) {
            opcionCarta = menuCartas("Seleciona una carta o escribe -1 para robar");
            if (opcionCarta == (-1)) {
                addCartaBase(1);
                addCartaMano();
                opcionCarta = -2;
            } else {
                if (cartaJugarMesa(mano.get(opcionCarta), cartaMesa)) {
                    System.out.println("Carta jugada " + mano.get(opcionCarta).toString());
                } else {
                    System.out.println("Carta no valida");
                    opcionCarta = -2;
                }
            }
        }
    }
    private int saltoTurno(Carta cartaMesa){
        if (cartaMesa.getEstat() == 0){
            System.out.println("Salto de turno");
            updateEstat();
            return 0;
        }
        return -2;

    }


}
