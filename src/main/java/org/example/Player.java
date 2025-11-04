package org.example;

public class Player extends Entity{
    public int candyHeld;
    public Player(String name, int x, int y, int velocity_x, int velocity_y, int hitbox_length, int hitbox_height, String type, int candyHeld) {
        super(name, x, y, velocity_x, velocity_y, hitbox_length, hitbox_height, type);
        this.candyHeld = candyHeld;

    }
}
