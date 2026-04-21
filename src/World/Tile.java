/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

/**
 *
 * @author blope
 */
public class Tile {
    public static final int WATER = 0;
    public static final int GRASS = 1;

    private int type;

    public Tile(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
