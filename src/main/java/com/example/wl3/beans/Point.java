package com.example.wl3.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Date;

@Named
@SessionScoped
public class Point implements Serializable {
    private double x = 0;
    private double y = 0;
    private double r = 2;

    private Date currentTime;
    private long startTime;

    private long executionTime;
    private boolean hit;


    public Point() {
        this.startTime = System.nanoTime();
        this.currentTime = new Date();
    }

    public Point(double x, double y, double r) {
        this.startTime = System.nanoTime();
        this.currentTime = new Date();
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = checkHit(x, y, r);
    }

    public double getX() {
        return x;
    }


    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean checkHit(double x, double y, double r) {
        if (x >= 0 && y >= 0 && x <= r && y <= r / 2) {
            return true;
        }
        if (x <= 0 && y <= 0 && y >= -r / 2 - x) {
            return true;
        }
        if (x <= 0 && y >= 0 && x * x + y * y <= r * r) {
            return true;
        }
        return false;
    }

    public void reset() {
        x = 0;
        y = 0;
        r = 1;
        hit = false;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", hit=" + hit +
                '}';
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @FacesValidator("com.example.wl3.PointValidator")
    public static class PointValidator implements Validator {
        @Override
        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            if (value == null) {
                return;
            }
            switch (component.getId()) {
                case "input_y":
                    if (value instanceof Double) {
                        double y = (double) value;
                        if (y < -5 || y > 5) {
                            throw new ValidatorException(new FacesMessage("Y must be in range [-5;5]"));
                        }
                    }
                    break;
                case "input_r":
                    if (value instanceof Double) {
                        double r = (double) value;
                        if (r < 2 || r > 5) {
                            throw new ValidatorException(new FacesMessage("R must be in range [2;5]"));
                        }
                    }
                    break;
            }
        }
    }
}