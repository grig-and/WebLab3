package com.example.wl3.beans;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/sse")
public class SseTime extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Connection", "keep-alive");

        while (true) {
            try {
                Thread.sleep(500);
                response.getWriter().write("data: " + System.currentTimeMillis() + "\n\n");
                response.flushBuffer();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
