package World;

import java.awt.image.BufferedImage;

public class Tile {

    // =========================
    // TYPES
    // =========================

    public static final int WATER = 0;
    public static final int GRASS = 1;
    public static final int DIRT = 2;
    public static final int PLAIN = 3;
    public static final int ROCK = 4;

    // =========================
    // DIRT VARIANTS
    // =========================

    public static final int DIRT_1 = 0;
    public static final int DIRT_2 = 1;
    public static final int DIRT_3 = 2;
    public static final int DIRT_4 = 3;

    // =========================
    // PLAIN VARIANTS
    // =========================

    public static final int PLAIN_5 = 4;
    public static final int PLAIN_6 = 5;
    public static final int PLAIN_7 = 6;
    public static final int PLAIN_8 = 7;

    // =========================
    // ROCK VARIANTS
    // =========================

    public static final int ROCK_8 = 8;
    public static final int ROCK_9 = 9;

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
}