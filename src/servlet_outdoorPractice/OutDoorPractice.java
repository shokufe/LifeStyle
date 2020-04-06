package servlet_outdoorPractice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OutDoorPractice extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PrintWriter out= resp.getWriter();
		resp.setContentType("text/html");
		out.println("<html><body>"+"<h1>Bonjour Monde</h1>"+"</html></body>");
	}

}
