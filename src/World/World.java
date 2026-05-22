package World;

import Utils.Assets;
import java.awt.image.BufferedImage;
import java.util.Random;

public class World {

    int rows = 42;
    int colums = 40;

    Tile[][] map;

    Random rng = new Random();

    public World() {

        GenerateIsland();
    }

    public void GenerateIsland() {

        map = new Tile[rows][colums];

        int padding = 2;

        // =========================
        // GENERATE BIOME MAP
        // =========================

        int[][] biomeMap = new int[rows][colums];

        // centros de bioma
        for (int i = 0; i < 25; i++) {

            int centerRow = rng.nextInt(rows);
            int centerCol = rng.nextInt(colums);

            int biomeType = rng.nextInt(3);

            int radius = 3 + rng.nextInt(6);

            for (int r = centerRow - radius; r <= centerRow + radius; r++) {

                for (int c = centerCol - radius; c <= centerCol + radius; c++) {

                    if (r >= 0 && c >= 0 &&
                        r < rows && c < colums) {

                        double distance =
                                Math.sqrt(
                                        Math.pow(r - centerRow, 2) +
                                        Math.pow(c - centerCol, 2)
                                );

                        if (distance <= radius) {

                            biomeMap[r][c] = biomeType;
                        }
                    }
                }
            }
        }

        // =========================
        // CREATE TILES
        // =========================

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < colums; c++) {

                // =========================
                // WATER BORDER
                // =========================

                if (r < padding || c < padding
                        || r >= rows - padding
                        || c >= colums - padding) {

                    if (r < 2 || c < 2
                            || r >= rows - 2
                            || c >= colums - 2) {

                        map[r][c] = new Tile(
                                Tile.WATER,
                                0,
                                Assets.waterDeep
                        );

                    } else {

                        map[r][c] = new Tile(
                                Tile.WATER,
                                0,
                                Assets.waterNormal
                        );
                    }

                } else {

                    // =========================
                    // BIOME TYPE
                    // =========================

                    int biome = biomeMap[r][c];

                    int variant;

                    // =========================
                    // DIRT
                    // =========================

                    if (biome == 0) {

                        variant = rng.nextInt(4);

                        map[r][c] = new Tile(
                                Tile.GRASS,
                                variant,
                                Assets.dirtTiles[variant]
                        );
                    }

                    // =========================
                    // PLAIN
                    // =========================

                    else if (biome == 1) {

                        variant = rng.nextInt(4);

                        map[r][c] = new Tile(
                                Tile.GRASS,
                                variant,
                                Assets.plainTiles[variant]
                        );
                    }

                    // =========================
                    // ROCK
                    // =========================

                    else {

                        variant = rng.nextInt(2);

                        map[r][c] = new Tile(
                                Tile.GRASS,
                                variant,
                                Assets.rockTiles[variant]
                        );
                    }
                }
            }
        }
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
    public void setTile(int row, int col, int type) {

        BufferedImage sprite;

        switch (type) {

            // =========================
            // WATER
            // =========================

            case Tile.WATER:

                sprite = Assets.waterNormal;

                map[row][col] = new Tile(
                        Tile.WATER,
                        0,
                        sprite
                );

                break;

            // =========================
            // DIRT
            // =========================

            case Tile.DIRT_1:

                map[row][col] = new Tile(
                        Tile.DIRT_1,
                        0,
                        Assets.dirtTiles[0]
                );

                break;

            case Tile.DIRT_2:

                map[row][col] = new Tile(
                        Tile.DIRT_2,
                        1,
                        Assets.dirtTiles[1]
                );

                break;

            case Tile.DIRT_3:

                map[row][col] = new Tile(
                        Tile.DIRT_3,
                        2,
                        Assets.dirtTiles[2]
                );

                break;

            case Tile.DIRT_4:

                map[row][col] = new Tile(
                        Tile.DIRT_4,
                        3,
                        Assets.dirtTiles[3]
                );

                break;

            // =========================
            // PLAIN
            // =========================

            case Tile.PLAIN_5:

                map[row][col] = new Tile(
                        Tile.PLAIN_5,
                        0,
                        Assets.plainTiles[0]
                );

                break;

            case Tile.PLAIN_6:

                map[row][col] = new Tile(
                        Tile.PLAIN_6,
                        1,
                        Assets.plainTiles[1]
                );

                break;

            case Tile.PLAIN_7:

                map[row][col] = new Tile(
                        Tile.PLAIN_7,
                        2,
                        Assets.plainTiles[2]
                );

                break;

            case Tile.PLAIN_8:

                map[row][col] = new Tile(
                        Tile.PLAIN_8,
                        3,
                        Assets.plainTiles[3]
                );

                break;

            // =========================
            // ROCK
            // =========================

            case Tile.ROCK_8:

                map[row][col] = new Tile(
                        Tile.ROCK_8,
                        0,
                        Assets.rockTiles[0]
                );

                break;

            case Tile.ROCK_9:

                map[row][col] = new Tile(
                        Tile.ROCK_9,
                        1,
                        Assets.rockTiles[1]
                );

                break;
        }
    }
}