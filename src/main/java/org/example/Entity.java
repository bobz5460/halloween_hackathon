package org.example;

public class Entity {
    String name, type;
    int x, y, velocity_x, velocity_y, hitbox_length, hitbox_height;
    public Entity(String name, int x, int y, int velocity_x, int velocity_y, int hitbox_length, int hitbox_height, String type){
        this.name = name;
        this.x = x;
        this.y = y;
        this.velocity_x = velocity_x;
        this.velocity_y = velocity_y;
        this.hitbox_length = hitbox_length;
        this.hitbox_height = hitbox_height;
        this.type = type;

    }
    public Integer[] getPos(){
        return new Integer[]{x, y};
    }
    public Integer[] getVel(){
        return new Integer[]{velocity_x, velocity_y};
    }
    public void setPos(Integer[] pos){
        x = pos[0];
        y = pos[1];
    }
    public void setVel(Integer[] vel){
        this.velocity_x = vel[0];
        this.velocity_y = vel[1];
        System.out.println("set vel");
        System.out.println(vel[0] + " " + vel[1]);
    }
    public void applyVel(){
        x += velocity_x;
        y += velocity_y;
        System.out.println("apply vel");
        System.out.println(x + " " + y);
    }

}
