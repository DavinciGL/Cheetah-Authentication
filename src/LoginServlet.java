import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        auth Auth = new auth();

        if (Auth.validate(user, pass)) {
            out.println("<h2>Access Granted</h2>");
            out.println("<p>Welcome to Cheetah Network, " + user + "!</p>");
        } else {
            out.println("<h2>Access Denied</h2>");
            out.println("<p>Invalid credentials. Please try again.</p>");
        }
    }
}