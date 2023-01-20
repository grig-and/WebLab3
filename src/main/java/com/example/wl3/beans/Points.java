package com.example.wl3.beans;

import com.example.wl3.util.PG;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class Points implements Serializable {
    //    List of Point
    private List<Point> list = new ArrayList<>();


    public Points() {
        list = PG.getPoints();
    }

    public List<Point> getList() {
        return list;
    }

    public void addPoint(Point p) {
        System.out.println("addPoint" + p);
        list.add(p);
        PG.addPoint(p);
    }

    //    add point remote command
    public void addPointRC() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        System.out.println("params:" + params);
        double x = Double.parseDouble(params.get("x"));
        double y = Double.parseDouble(params.get("y"));
        double r = Double.parseDouble(params.get("r"));
        Point point = new Point(x, y, r);
        addPoint(point);
//        list.get(list.size() - 1).setExecutionTime(System.nanoTime() - point.getStartTime());
        System.out.println("addPointRC" + point);
    }

    public String getRowClass() {
        StringBuilder rc = new StringBuilder();

        for (Point p : list) {
            if (rc.length() > 0) rc.append(",");
            if (p.isHit() == true) {
                rc.append("row_true");
            } else {
                rc.append("row_false");
            }

        }
        return rc.toString();
    }

    public void submit(Point point) {
        Point p = new Point(point.getX(), point.getY(), point.getR());
        p.setCurrentTime(new Date());
        p.getStartTime();
        addPoint(p);
        list.get(list.size() - 1).setExecutionTime(System.nanoTime() - p.getStartTime());
    }

    //    init from postgress PG
    public void init() {
        list.clear();
        List<Point> listPG = PG.getPoints();
        list.addAll(listPG);
        System.out.println("loaded from PG:" + list);
    }

    public void delete() {
        list.clear();
        PG.deletePoints();
    }
}
