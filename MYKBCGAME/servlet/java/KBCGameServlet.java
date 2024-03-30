import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/KBCGameServlet"})
public class KBCGameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String[] QUESTIONS = {
             "What is the main advantage of using a three-tier architecture for web applications?" ,
             "What is the purpose of the doGet() method in a servlet?",            
             "Which method of the Servlet interface is invoked when a servlet is being unloaded?",
             "HTTP is a ___________ protocol?"
    };

    private static final String[][] OPTIONS = {
            {"Better scalability ","Enhanced security ","Faster load times"," Simpler development process"},
            {" To handle POST requests from the client","To initialize the servlet","To handle GET requests from the client"," To destroy the servlet instance"},
            {" init() ","service()", "doGet()"," destroy()"},
            {"Dynamic", "static", "stateless","state"}
    };

    private static final int[] ANSWERS = {0,2,3,2}; // Index of correct option for each question
    private static int questionIndex = 0;
    private static int totalPrize = 0;

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String selectedOption = request.getParameter("option");

    if (selectedOption != null && !selectedOption.isEmpty()) {
        int selectedOptionIndex = Integer.parseInt(selectedOption);
        if (selectedOptionIndex == ANSWERS[questionIndex]) {
            totalPrize += 10000;
            displayCorrectAnswerPopup(response.getWriter());

        } else {
            if (questionIndex != 0) {
                // If any answer is wrong except for the first question, display the total prize page
                displaywrongAnswerPopup(response.getWriter());
                displayResult(request, response);
                // Reset the game after displaying the result
                questionIndex = 0;
                totalPrize = 0;
                return; // End the method here
                
            } else {
                totalPrize = 0; // Reset points if the first answer is incorrect
                displayZeroPointsResult(request, response); // Display result with zero points for the first question
                return; // End the method here
            }
        }

        questionIndex++;

        if (questionIndex < QUESTIONS.length) {
            displayQuestion(request, response);
        } else {
            displayResult(request, response);
            // Reset the game after displaying the result
            questionIndex = 0;
            totalPrize = 0;
        }
    } else {
        displayQuestion(request, response);
    }
}
private void displayCorrectAnswerPopup(PrintWriter out) {
    out.println("<link rel=\"stylesheet\" href=\"KBCstyle.css\">");
    out.println("<div id=\"correctPopup\" class=\"popup\"><img src='image/correct.png' /></div>");
    out.println("<script>");
    out.println("var correctPopup = document.getElementById('correctPopup');");
    out.println("correctPopup.style.display = 'block';"); // Show the popup
    out.println("setTimeout(function() {");
    out.println("correctPopup.style.display = 'none';"); // Hide the popup after 2 seconds
    out.println("}, 5000);");
    out.println("</script>");
}
private void displaywrongAnswerPopup(PrintWriter out) {
    out.println("<link rel=\"stylesheet\" href=\"KBCstyle.css\">");
    out.println("<div id=\"correctPopup\" class=\"popup\"><img src='image/wrong.png'/></div>");
    out.println("<script>");
    out.println("var correctPopup = document.getElementById('correctPopup');");
    out.println("correctPopup.style.display = 'block';"); // Show the popup
    out.println("setTimeout(function() {");
    out.println("correctPopup.style.display = 'none';"); // Hide the popup after 2 seconds
    out.println("}, 5000);");
    out.println("</script>");
}

    private void displayZeroPointsResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>KBC Game Result</title><link rel=\"stylesheet\" href=\"KBCstyle.css\"></head>");
        out.println("<body>");
        out.println("<style>body{ background: url('image/oops.png') fixed;\n" +
                "background-repeat: no-repeat;\n" +
                "background-size: fixed;}</style>");
        out.println("<div class=\"question\">");
        out.println("<h2>You didn't earn any points. Better luck next time!</h2>"); // Display zero points
        out.println("<h2></h2>");
        out.println("</div>");
        out.println("<div id=\"correctPopup\" class=\"popup\"><img src='image/wrong.png' /></div>");
        out.println("<script>");
        out.println("var correctPopup = document.getElementById('correctPopup');");
        out.println("correctPopup.style.display = 'block';"); // Show the popup
        out.println("setTimeout(function() {");
        out.println("correctPopup.style.display = 'none';"); // Hide the popup after 2 seconds
        out.println("}, 1000);");
        out.println("</script>");
        out.println("</body></html>");
        out.close();
    }
private void displayQuestion(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta "
            + "name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>KBC Game</title><link rel=\"stylesheet\" href=\"KBCstyle.css\"></head>");
    out.println("<body>");
    out.println("<div class=\"container\">");
    out.println("<h1>KBC GAME!</h1>");
    out.println("<img src=\"image/kbc.jpg\" class=\"img\" alt=\"alt\"/>\n");
    out.println("<div class=\"points\">"); 
    out.println("<img src=\"image/coinbg.png\" alt=\"Coin\">"); // Assuming the coin image file is named "coin_image.jpg"
    out.println("</h3><span>Rs: " + totalPrize + "</span>"); // Display current points
    out.println("</div>"); // End of points
    out.println("</div>"); // End of content
    out.println("</div> <br>"); // End of container
    out.println("<form id=\"questionForm\" action=\"KBCGameServlet\" method=\"post\">");
    out.println("<div class=\"question\">");
    out.println("<h2 id=\"question\"><u>Question " + (questionIndex + 1) + ":</u>" + QUESTIONS[questionIndex] + "</h2>");
    out.println("</div>");
    out.println("<div class=\"options\">");
    // Determine the number of options per row
    int optionsPerRow = OPTIONS[questionIndex].length / 2;
    // Display left side buttons
    out.println("<div class=\"option-row\">");
    for (int i = 0; i < optionsPerRow; i++) {
        out.println("<button type=\"submit\" name=\"option\" value=\"" + i + "\" class=\"option-btn\">" + OPTIONS[questionIndex][i] + "</button>");
    }
    out.println("</div>");
    // Display right side buttons
    out.println("<div class=\"option-row\">");
    for (int i = optionsPerRow; i < OPTIONS[questionIndex].length; i++) {
        out.println("<button type=\"submit\" name=\"option\" value=\"" + i + "\" class=\"option-btn\">" + OPTIONS[questionIndex][i] + "</button>");
    }
    out.println("</div>");
    out.println("</div>");
    out.println("</form>");
    out.println("</div>");
    out.println("<script>");
    out.println("window.onload = function() {");
    out.println(" if (window.history && window.history.pushState) {");
    out.println(" window.history.pushState('forward', null, './#forward');");
    out.println(" window.onpopstate = function() {");
    out.println(" window.history.pushState('forward', null, './#forward');");
    out.println(" alert(\'Careful! Backtracking is not allowed in this quiz. Keep moving forward!\');"); // Alert message for back button
    out.println(" };");
    out.println(" }");
    out.println(" document.getElementById('questionForm').addEventListener('submit', function() {");
    out.println(" window.history.pushState('forward', null, './#forward');");
    out.println(" });");
    out.println("};");
    out.println("</script>");
    out.println("</body></html>");
    out.close();
}

private void displayResult(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>KBC Game Result</title><link rel=\"stylesheet\" href=\"KBCstyle.css\"></head><body>");
    out.println("<style>body{ background: url('image/bg.gif') fixed;\n" +
            "background-repeat: no-repeat;\n" +
            "margin-top: 20%;"+
            "background-size: cover;}</style>");
    out.println("<div class=\"gameover\">");
        out.println("<div class=\"points\">"); 
        out.println("<img src=\"image/coinbg.png\" alt=\"Coin\">"); // Assuming the coin image file is named "coin_image.jpg"
    out.println("<h2>Congratulations! You have won Rs. " + totalPrize + "</h2>");
       out.println("</div>"); 
    out.println("<h2></h2>");
    out.println("</div>");
    out.println("</body></html>");
    out.close();
}
}
        
                
