package view;

import common.ValidationException;
import entity.Item;
import logic.ItemLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CategoryLogic;
import logic.ImageLogic;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "CreateItem", urlPatterns = {"/CreateItem"})
public class CreateItem extends HttpServlet {

    private String errorMessage = null;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Feed</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println(ItemLogic.ID+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.ID);
            out.println("<br>");
            out.println(ItemLogic.IMAGE_ID+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.IMAGE_ID);
            out.println("<br>");
            out.println(ItemLogic.CATEGORY_ID+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.CATEGORY_ID);
            out.println("<br>");
            out.println(ItemLogic.PRICE+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.PRICE);
            out.println("<br>");
            out.println(ItemLogic.TITLE+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.TITLE);
            out.println("<br>");
            out.println(ItemLogic.DATE+" (as DD/MM/YYYY):<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.DATE);
            out.println("<br>");
            out.println(ItemLogic.LOCATION+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.LOCATION);
            out.println("<br>");
            out.println(ItemLogic.DESCRIPTION+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.DESCRIPTION);
            out.println("<br>");
            out.println(ItemLogic.URL+":<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.URL);
            out.println("<br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if(errorMessage!=null&&!errorMessage.isEmpty()){
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * get method is called first when requesting a URL. since this servlet
     * will create a host this method simple delivers the html code. 
     * creation will be done in doPost method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * this method will handle the creation of entity. as it is called by user
     * submitting data through browser.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        
        ItemLogic aLogic = new ItemLogic();
        String url = request.getParameter( ItemLogic.URL);
        if(url.equals("")) errorMessage = "Item Url cannot be empty.";
        else{
            if(aLogic.getWithUrl(url)==null){
                try{
                    Item item = aLogic.createEntity( request.getParameterMap());

                    int catId = Integer.parseInt(request.getParameterMap().get(ItemLogic.CATEGORY_ID)[0]);
                    int imgId = Integer.parseInt(request.getParameterMap().get(ItemLogic.IMAGE_ID)[0]);
                    if(new CategoryLogic().getWithId(catId)!=null&&new ImageLogic().getWithId(imgId)!=null){
                        item.setCategory(new CategoryLogic().getWithId(catId));
                        item.setImage(new ImageLogic().getWithId(imgId));
                        aLogic.add(item);
                    }
                    else errorMessage = "Invalid Category and/or Image id.";
                }
                catch(NumberFormatException ex){
                    errorMessage = "Category or Image Id cannot be empty, and must exist in their tables.";
                }
                catch(ValidationException ex){
                    errorMessage = ex.getMessage();
                }


            }else{
                //if duplicate print the error message
                errorMessage = "Username: \"" + url + "\" already exists";
            }
        }
        if( request.getParameter("add")!=null){
            //if add button is pressed return the same page
            processRequest(request, response);
        }else if (request.getParameter("view")!=null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("ItemTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Account Entity";
    }

    private static final boolean DEBUG = true;

    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
}
