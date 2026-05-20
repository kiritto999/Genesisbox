/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Color;
import java.util.Random;

public class Tile {

    public static final int WATER = 0;
    public static final int GRASS = 1;

    private static final Random rng = new Random();

    private int type;
    private int variant;
    private Color color;

    public Tile(int type){
        this(type, 0);
    }

    public Tile(int type, int variant){

        this.type = type;
        this.variant = variant;

        generateColor();
    }

    private void generateColor(){

        switch(type){
            case WATER:
                switch(variant){
                    // agua normal
                    case 0:
                        color = new Color(73, 201, 252);
                        break;

                    // agua profunda
                    case 1:
                        color = new Color(81, 166, 214);
                        break;
                }
                break;

            case GRASS:
                int variant = rng.nextInt(4);
                switch(variant){
                    case 0:
                        color = new Color(245, 125, 39);
                        break;
                    case 1:
                        color = new Color(241, 123, 40);
                        break;
                    case 2:
                        color = new Color(247, 128, 41);
                        break;
                    case 3:
                        color = new Color(238, 121, 42);
                        break;
                }
                break;
        }
    }

    public int getType() {
        return type;
    }

    public Color getColor(){
        return color;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }
    
}