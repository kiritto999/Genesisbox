package World;

public class World {
    
    int rows = 60;
    int colums = 60;
    Tile[][] map;
    
    public World(){
        GenerateIsland();
    }
    
    public void GenerateIsland(){
        map = new Tile[rows][colums];
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < colums; c++){
                
                if (r == 0 || c == 0 || r == rows-1 || c == colums-1){
                    map[r][c] = new Tile(Tile.WATER);
                } else {
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