package World;

public class World {
    
    int rows = 40;
    int colums = 40;
    Tile[][] map;
    
    public World(){
        GenerateIsland();
    }
    
    public void GenerateIsland(){
        map = new Tile[rows][colums];
        int padding = 4;
        for (int r = 0; r < rows; r++){

            for (int c = 0; c < colums; c++){

                if( r < padding || c < padding || r >= rows - padding || c >= colums - padding){
                    // borde profundo
                    if(r < 2 || c < 2 || r >= rows - 2 || c >= colums - 2 ){
                        map[r][c] = new Tile(Tile.WATER, 1);
                    }else{
                        map[r][c] = new Tile(Tile.WATER, 0);
                    }
                }else{
                    map[r][c] = new Tile(Tile.GRASS);
                }
            }
        }
    }
    
    public void setTile(int row, int col, int type) {

        if (row < 0 || col < 0 || row >= rows || col >= colums) {
            return;
        }

        map[row][col] = new Tile(type);
    }

    public Tile getTile(int row, int col) {
        return map[row][col];
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