
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/ScoreServlet")

public class ScoreServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // RESET LOGIC (fixed)
        if ("true".equals(request.getParameter("reset"))) {
            session.invalidate();
            response.sendRedirect("index.html");
            return;
        }

        List<String[]> scores = (List<String[]>) session.getAttribute("scores");
        if (scores == null) {
            scores = new ArrayList<>();
        }

        String subject = request.getParameter("subject");
        String scoreStr = request.getParameter("score");

        if (subject != null && scoreStr != null && !scoreStr.isEmpty()) {
            scores.add(new String[]{subject, scoreStr});
        }

        session.setAttribute("scores", scores);

        // Calculate average
        double total = 0;
        for (String[] s : scores) {
            total += Double.parseDouble(s[1]);
        }

        double avg = scores.size() > 0 ? total / scores.size() : 0;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>Score List</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>Subject</th><th>Score</th></tr>");

        for (String[] s : scores) {
            out.println("<tr><td>" + s[0] + "</td><td>" + s[1] + "</td></tr>");
        }

        out.println("</table>");
        out.println("<h3>Average: " + avg + "</h3>");
        out.println("<br><a href='index.html'>Go Back</a>");
    }
}
