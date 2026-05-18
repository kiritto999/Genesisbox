/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

public class TimeDay {
    private int year;
    private int day;
    private int totaldays;

    private int hour;
    private int minute;
    private int second;

    private boolean paused = false;
    private double speed = 1.0;
    private double accumulator = 0;

    public void updateTime(double realTime) {

        if (paused){
            return;
        }

        accumulator += realTime * speed;

        while (accumulator >= 1.0) {
            second += 1; 
            accumulator--;

            if (second >= 60) {
                minute += second / 60;
                second = second % 60;
            }

            if (minute >= 60) {
                hour += minute / 60;
                minute = minute % 60;
            }

            if (hour >= 5) {
                int daysPassed = hour / 5;
                day += daysPassed;
                totaldays += daysPassed;
                hour = hour % 5;
            }

            if (day >= 160) {
                year += day / 160;
                day = day % 160;
            }
        }
    }

    public String getTimeString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (!paused) {
            accumulator = 0; 
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public boolean isPaused() {
        return paused;
    }

    public int getHour() {return hour;}
    
}