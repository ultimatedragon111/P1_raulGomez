package dao;

import model.Carta;

import java.sql.*;
import java.util.ArrayList;

public class Dao {
    private Connection conexion;

    //Constantes. Deberan estar en una clase de constanes en el pck utils
    public static final String SCHEMA_NAME = "dam2tm06uf2p1";
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/" +
            SCHEMA_NAME +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    public static final String USER_CONNECTION = "root";
    public static final String PASS_CONNECTION = "root";

    public static final String GET_ID_PLAYER = "select * from jugador where usuario=? AND password=?  ";
    public static final String GET_COUNT_CARDS = "select id,numero,color from carta where id_jugador = ? AND id NOT IN (SELECT id_carta FROM partida)";
    public static final String INSERT_CARD_CARDS = "insert into carta (id_jugador,numero,color) VALUES (?,?,?);";
    public static final String INSERT_CARD_TABLE = "insert into partida (id_carta) VALUES (?);";
    public static final String LAST_CARD_TABLE = "SELECT P.id,id_carta,C.numero,C.color,estat  FROM partida P INNER JOIN carta C ON P.id_carta = C.id order by P.id  DESC LIMIT 1;";
    public static final String UPDATE_ESTAT = "update partida SET estat = 1 WHERE id_carta = ?;";
    public static final String PLAYER_GAME = "SELECT distinct id_jugador from carta;";
    public static final String GAME_PLUS = "UPDATE jugador SET partidas = partidas + 1  WHERE id = ?;";
    public static final String WIN_PLUS = "UPDATE jugador SET ganadas = ganadas + 1  WHERE id = ?;";
    public static final String DELETE_PARTIDA = "DELETE FROM partida;";
    public static final String DELETE_CARTA = "DELETE FROM carta;";


    public void connectar() throws SQLException {
        String url = CONNECTION;
        String user = USER_CONNECTION;
        String pass = PASS_CONNECTION;
        conexion = DriverManager.getConnection(url, user, pass);
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    //Consultas base de datos
    public int inicioSesion(String user, String password) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(GET_ID_PLAYER)) {
            ps.setString(1, user);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return 0;
    }

    public ArrayList<Carta> comprovarCartas(int id_jugador) {
        ArrayList<Carta> mano = new ArrayList<Carta>();

        try (PreparedStatement ps = conexion.prepareStatement(GET_COUNT_CARDS)) {
            ps.setInt(1, id_jugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String numero = rs.getString(2);
                    String color = rs.getString(3);
                    Carta carta = new Carta(id, id_jugador, numero, color, 0);
                    mano.add(carta);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return mano;
    }

    public void addCarta(Carta carta) {
        try (PreparedStatement ps = conexion.prepareStatement(INSERT_CARD_CARDS)) {
            ps.setInt(1, carta.getId_jugador());
            ps.setString(2, carta.getNumero());
            ps.setString(3, carta.getColor());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addCartaTable(Carta carta) {
        try (PreparedStatement ps = conexion.prepareStatement(INSERT_CARD_TABLE)) {
            ps.setInt(1, carta.getId_carta());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Carta checkCardTable() {
        try (ResultSet rs = conexion.prepareStatement(LAST_CARD_TABLE).executeQuery()) {
            while (rs.next()) {
                Carta carta = new Carta(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                return carta;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void updateEstat(Carta carta) {
        try (PreparedStatement ps = conexion.prepareStatement(UPDATE_ESTAT)) {
            ps.setInt(1, carta.getId_carta());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateGamePlus() {
        try (ResultSet rs = conexion.prepareStatement(PLAYER_GAME).executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement ps = conexion.prepareStatement(GAME_PLUS)) {
                    ps.setInt(1,rs.getInt(1));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void updateWinPlus(int id_Jugador){
        try (PreparedStatement ps = conexion.prepareStatement(WIN_PLUS)){
            ps.setInt(1,id_Jugador);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteGame(){
        try(PreparedStatement ps = conexion.prepareStatement(DELETE_PARTIDA)){
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteCards(){
        try(PreparedStatement ps = conexion.prepareStatement(DELETE_CARTA)){
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}







