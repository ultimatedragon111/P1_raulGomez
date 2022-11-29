package utils;

public class Constants {
    public static final String SCHEMA_NAME = "dam2tm06uf2p1";
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/" +
            SCHEMA_NAME +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    public static final String USER_CONNECTION = "root";
    public static final String PASS_CONNECTION = "root";

    public static final String GET_ID_PLAYER = "select * from jugador where usuario=? AND password=?  ";
    public static final String GET_COUNT_CARDS = "SELECT c.id ,c.id_jugador,numero,color  FROM carta c LEFT JOIN partida p  ON c.id = p.id_carta  JOIN jugador j ON j.id = c.id_jugador WHERE id_jugador = ? AND p.id_carta IS NULL ;";
    public static final String INSERT_CARD_CARDS = "insert into carta (id_jugador,numero,color) VALUES (?,?,?);";
    public static final String INSERT_CARD_TABLE = "insert into partida (id_carta) VALUES (?);";
    public static final String LAST_CARD_TABLE = "SELECT P.id,id_carta,C.numero,C.color,estat  FROM partida P INNER JOIN carta C ON P.id_carta = C.id order by P.id  DESC LIMIT 1;";
    public static final String UPDATE_ESTAT = "update partida SET estat = 1 WHERE id_carta = ?;";
    public static final String PLAYER_GAME = "SELECT distinct id_jugador from carta;";
    public static final String GAME_PLUS = "UPDATE jugador SET partidas = partidas + 1  WHERE id = ?;";
    public static final String WIN_PLUS = "UPDATE jugador SET ganadas = ganadas + 1  WHERE id = ?;";
    public static final String DELETE_PARTIDA = "DELETE FROM partida;";
    public static final String DELETE_CARTA = "DELETE FROM carta;";
    public static final String CREATE_USER = "insert into jugador (usuario,password,nombre,partidas,ganadas) VALUES (?,?,?,0,0);";
    public static final String query = "SELECT count(*) FROm partida p INNER JOIN carta c ON p.id_carta = c.id WHERE estat = 0 ;";
}
