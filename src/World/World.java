/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

/**
 *
 * @author blope
 */
public class World {
    int rows = 60;
    int colums = 60;
    Tile[][] map;
    
    public World(){
        GenerateIsland();
    }
    
    public void GenerateIsland(){
        map= new Tile[rows][colums];
        for (int r=0 ; r < rows ; r++){
            for (int c=0;c<colums;c++){
                if (r == 0 || c == 0 || r == rows-1 || c == colums-1){
                    map [r][c]= new Tile(Tile.WATER);
                }else{
                    map [r][c]=new Tile(Tile.GRASS);
                }
            }
        }
    }
    
    public Tile[][] getMap() {
        return map;
    }
    
    public int getRows() {
        return rows;
    }

    public int getColums() {
        return colums;
    }
    
    
    
    
    
    
}
