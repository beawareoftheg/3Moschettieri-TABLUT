package it.unibo.ai.didattica.competition.tablut.util;

public class Timer {
    private long star;
    private long passed;
    public Timer(int time) {

        this.passed = 1000 * time;
    }
    public void startTime() {

        star = System.currentTimeMillis();
    }
    public boolean isExpired() {
        if(System.currentTimeMillis() > star + passed - 5) return true; else return false;
    }
}