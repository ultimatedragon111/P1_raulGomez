package dao;

import model.Carta;
import utils.Constants;

import java.sql.*;
import java.util.ArrayList;


public class Dao {
    private Connection conexion;

    public void connectar() throws SQLException {
        String url = Constants.CONNECTION;
        String user = Constants.USER_CONNECTION;
        String pass = Constants.PASS_CONNECTION;
        conexion = DriverManager.getConnection(url, user, pass);
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    //Consultas base de datos
    public int inicioSesion(String user, String password) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(Constants.GET_ID_PLAYER)) {
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

        try (PreparedStatement ps = conexion.prepareStatement(Constants.GET_COUNT_CARDS)) {
            ps.setInt(1, id_jugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String numero = rs.getString(3);
                    String color = rs.getString(4);
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
        try (PreparedStatement ps = conexion.prepareStatement(Constants.INSERT_CARD_CARDS)) {
            ps.setInt(1, carta.getId_jugador());
            ps.setString(2, carta.getNumero());
            ps.setString(3, carta.getColor());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addCartaTable(Carta carta) {
        try (PreparedStatement ps = conexion.prepareStatement(Constants.INSERT_CARD_TABLE)) {
            ps.setInt(1, carta.getId_carta());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Carta checkCardTable() {
        try (ResultSet rs = conexion.prepareStatement(Constants.LAST_CARD_TABLE).executeQuery()) {
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
        try (PreparedStatement ps = conexion.prepareStatement(Constants.UPDATE_ESTAT)) {
            ps.setInt(1, carta.getId_carta());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateGamePlus() {
        try (ResultSet rs = conexion.prepareStatement(Constants.PLAYER_GAME).executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement ps = conexion.prepareStatement(Constants.GAME_PLUS)) {
                    ps.setInt(1,rs.getInt(1));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void updateWinPlus(int id_Jugador){
        try (PreparedStatement ps = conexion.prepareStatement(Constants.WIN_PLUS)){
            ps.setInt(1,id_Jugador);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteGame(){
        try(PreparedStatement ps = conexion.prepareStatement(Constants.DELETE_PARTIDA)){
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteCards(){
        try(PreparedStatement ps = conexion.prepareStatement(Constants.DELETE_CARTA)){
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}







