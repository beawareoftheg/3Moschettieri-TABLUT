package it.unibo.ai.didattica.competition.tablut.util;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    private int oldX;
    private int oldY;

    private boolean hasLeft;

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    public boolean isHasLeft() {
        return hasLeft;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                ", oldX=" + oldX +
                ", oldY=" + oldY +
                ", hasLeft=" + hasLeft +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && oldX == that.oldX && oldY == that.oldY && hasLeft == that.hasLeft;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, oldX, oldY, hasLeft);
    }

    public Coordinate(int x, int y, int oldX, int oldY, boolean hasLeft) {
        this.x = x;
        this.y = y;
        this.oldX = oldX;
        this.oldY = oldY;
        this.hasLeft = hasLeft;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
