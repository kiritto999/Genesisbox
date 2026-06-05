package World;

import java.awt.image.BufferedImage;

public class Tile {

    // =========================
    // TYPES
    // =========================

    public static final int WATER = 0;
    public static final int GRASS = 1;

    // =========================
    // DIRT VARIANTS
    // =========================

    public static final int DIRT_1 = 1;
    public static final int DIRT_2 = 2;
    public static final int DIRT_3 = 3;
    public static final int DIRT_4 = 4;

    // =========================
    // PLAIN VARIANTS
    // =========================

    public static final int PLAIN_5 = 5;
    public static final int PLAIN_6 = 6;
    public static final int PLAIN_7 = 7;
    public static final int PLAIN_8 = 8;

    // =========================
    // ROCK VARIANTS
    // =========================

    public static final int ROCK_8 = 9;
    public static final int ROCK_9 = 10;

    // =========================

    private int type;
    private int variant;

    private BufferedImage sprite;

    // =========================

    public Tile(int type, int variant, BufferedImage sprite) {

        this.type = type;
        this.variant = variant;
        this.sprite = sprite;
    }

    // =========================

    public int getType() {
        return type;
    }

    public int getVariant() {
        return variant;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
    public void setType(int type) {
        this.type = type;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }
}