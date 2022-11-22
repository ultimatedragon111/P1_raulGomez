package controller;

import java.util.Random;

public enum Color {
    ROJO,AMARILLO,VERDE,AZUL,NEGRO;
    public static Color randomColor(){
        return Color.values()[new Random().nextInt(Color.values().length - 1)];
    }
}
