package com.chen.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");  
		resp.setCharacterEncoding("utf-8");  
        PrintWriter out = resp.getWriter();  
        String title="Webapp Demo";  
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");  
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");  
        out.println("<head>");  
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");  
        out.println("<title>" + title + "</title>");  
        out.println("<body>");  
        out.println("<h1>Hello World!</h1>");  
        out.println("</body>");  
        out.println("</html>");  
	}
}
