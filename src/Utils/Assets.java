package Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Assets {

    public static BufferedImage waterNormal;
    public static BufferedImage waterDeep;

    public static BufferedImage[] dirtTiles;
    public static BufferedImage[] plainTiles;
    public static BufferedImage[] rockTiles;

    public static void init() {

        try {

            // =========================
            // WATER
            // =========================
            System.out.println(
                Assets.class.getResource("/resources/Gifs/WaterV1.png")
            );

            waterNormal = ImageIO.read(
                    Assets.class.getResourceAsStream("/resources/Gifs/WaterV1.png")
            );

            waterDeep = ImageIO.read(
                    Assets.class.getResourceAsStream("/resources/Gifs/WaterV1.png")
            );

            // =========================
            // DIRT
            // =========================

            dirtTiles = new BufferedImage[4];

            dirtTiles[0] = ImageIO.read(
                    Assets.class.getResourceAsStream("/sprites/Dirt1.png")
            );

            dirtTiles[1] = ImageIO.read(
                    Assets.class.getResourceAsStream("/sprites/Dirt2.png")
            );

            dirtTiles[2] = ImageIO.read(
                    Assets.class.getResourceAsStream("/sprites/Dirt3.png")
            );

            dirtTiles[3] = ImageIO.read(
                    Assets.class.getResourceAsStream("/sprites/Dirt4.png")
            );
            // =========================
// PLAIN
// =========================

plainTiles = new BufferedImage[4];

plainTiles[0] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Plain5.png")
);

plainTiles[1] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Plain6.png")
);

plainTiles[2] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Plain7.png")
);

plainTiles[3] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Plain8.png")
);

// =========================
// ROCK
// =========================

rockTiles = new BufferedImage[2];

rockTiles[0] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Rock8.png")
);

rockTiles[1] = ImageIO.read(
        Assets.class.getResourceAsStream("/sprites/Rock9.png")
);


            System.out.println("Tiles loaded.");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}