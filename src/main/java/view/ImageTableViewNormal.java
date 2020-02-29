 package view;

import logic.ImageLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Image;
import entity.Item;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import logic.ItemLogic;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "ImageTable", urlPatterns = {"/ImageTable"})
public class ImageTableViewNormal extends HttpServlet {
    
    private String returnData="0";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>ImageViewNormal</title>");
            out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>");
            out.println("<script type=\"text/javascript\">");
            out.println("function deleteImage(id){");
            out.println("if(confirm(\"Do you really want to delete the image with Id \"+id+\"?\")){");
            out.println("$.get(\"/WebScraper/ImageTable\", {del: id },function(data, status){"
                    + "var data = \""+returnData+"\";");
            out.println("if(data=='0') {alert(\"Image deleted from database!\");}");
            out.println("if(data!='0') {alert(\"Cannot delete Image. Item with Id \"+data+\" must be deleted first.\");}");
            out.println("});");
            out.println("}");
            out.println("}");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption><b>Image<b></caption>");
            //this is an example, for your other tables use getColumnNames from
            //logic to create headers in a loop.
            ImageLogic logic = new ImageLogic();
            List<String> columnNames = logic.getColumnNames();
            out.println("<tr>");
            for(String name:columnNames){
            
                out.println("<th>"+name+"</th>");
            }
            out.println("</tr>");
            
            List<Image> entities = logic.getAll();
            for (Image e : entities) {
                //for other tables replace the code bellow with
                //extractDataAsList in a loop to fill the data.
                List<?> data = logic.extractDataAsList(e);
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td><a href=\"javascript: deleteImage(%d)\">Delete</a></td></tr>",
                        data.get(0),data.get(1),data.get(2),data.get(3),e.getId());
                        
                //out.printf("<tr><a href=\\\"delete(%d)\\\">Delete</a></tr>", e.getId());
            }
           //<a href=\"delete(%d)\">Delete</a></tr>
            
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *If there is a value in the request parameter 'del', the method processes the request to delete the 
     * image with id equal to the value in 'del'. It only deletes if the image is not used by an Item entity in a parent-child
     * relationship.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        if(request.getParameter("del")!=null){
            try{
                Integer id = Integer.parseInt(request.getParameter("del"));
                List<Item> itemList = new ItemLogic().getAll();
                Item boundItem = null;
                for(Item item : itemList){
                    if(item.getImage().getId().equals(id)){
                        boundItem = item;
                        returnData = String.valueOf(item.getId());
                        break;
                    }
                }
                if(boundItem==null){
                    returnData="0";
                    ImageLogic logic = new ImageLogic();
                    Image imageToDelete = logic.getWithId(id);
                    logic.delete(imageToDelete);
                    if(logic.getWithId(id)==null) response.setStatus(200); //shows that the entity was successfully deleted
                }
                //else processRequest(request, response);
            }
            catch(Exception ex){
                log("ImageTable GET: "+ex.getMessage());
                response.setStatus(500); //Conlict, will show on the browser that the image could not be deleted.
                
            }
        }
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
        
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Account View Normal";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
