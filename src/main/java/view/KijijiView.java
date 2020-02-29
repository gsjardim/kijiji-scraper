/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import common.ValidationException;
import entity.Category;
import entity.Image;
import entity.Item;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import scraper.kijiji.Kijiji;
import scraper.kijiji.KijijiItem;

/**
 *
 * @author gsoar
 */
@WebServlet(name = "Kijiji", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {

    
    private String errorMessage = null;

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
            /* TODO output your page here. You may use following sample code. */
            ItemLogic logic = new ItemLogic();
            List<Item> entities = logic.getAll();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet KijijiView</title>");
            out.println("<style>");
            out.println(".item{border-style: outset; border-width: 2px; border-color: #7582FB;");
            out.println("margin-top: 3px; margin-bottom: 3px; paddin;");
            out.println("background-color: #E5E6E9;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet KijijiView at " + request.getContextPath() + "</h1>");
            out.println("<div class=\"center-column\">");
            //<!-- you can add many <div class="item"> with in this div -->
            for (Item item : entities) {
                out.println("<div class=\"item\">");
                out.println("<div class=\"image\" style= \"margin: auto;\">");
                out.println("<img src=\"image/" + item.getId() +".jpg\" style=\"max-width: 250px; max-height: 200px;\" />");
                out.println("</div>");
                out.println("<div class=\"details\" style= \"margin-left:6px; margin-top: 5px; margin-bottom: 5px;\">");
                out.println("<div class=\"title\">");
                out.println("<a href=\"" + item.getUrl() + "\" target=\"_blank\">" + item.getTitle() + "</a>");
                out.println("</div>");
                out.println("<div class=\"price\">");
                out.println("<b>Price</b>: $"+item.getPrice());
                out.println("</div>");
                out.println("<div class=\"date\">");
                out.println("<b>Date</b>: "+item.getDate());
                out.println("</div>");
                out.println("<div class=\"location\">");
                out.println("<b>Location</b>: "+item.getLocation());
                out.println("</div>");
                out.println("<div class=\"description\">");
                out.println("<b>Description</b>: "+item.getDescription());
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imagesDir = System.getProperty("user.home") + "/KijijiImages/";
//        File file = new File(imagesDir);
//        //source: https://www.tutorialspoint.com/how-to-create-a-new-directory-by-using-file-object-in-java
//        if (!file.exists()) {
//            file.mkdir();
//        }
        FileUtility.createDirectory(imagesDir);
        Category cat = new CategoryLogic().getWithId(1);

        Kijiji kijiji = new Kijiji();
        kijiji.downloadPage(cat.getUrl());
        kijiji.findAllItems();
        kijiji.proccessItems(e -> {
            //first create the image using data from KijijiItem, which is passed through the lambda "e"
            Image image = new ImageLogic().getWithPath(imagesDir + e.getId() + ".jpg");
            Map<String, String[]> imageMap = new HashMap<>();
            if (image == null) {
                FileUtility.downloadAndSaveFile(e.getImageUrl(), imagesDir, e.getId() + ".jpg");
                String imagePath = imagesDir + e.getId() + ".jpg";
                String imageUrl = e.getImageUrl();
                String imageName = e.getImageName();

                imageMap.put(ImageLogic.PATH, new String[]{imagePath});
                imageMap.put(ImageLogic.NAME, new String[]{imageName});
                imageMap.put(ImageLogic.URL, new String[]{imageUrl});
                try{
                    image = new ImageLogic().createEntity(imageMap);
                    new ImageLogic().add(image);
                    
                        
                }
                catch(ValidationException ex){
                    log(ex.getMessage());
                    
                }
                
            }
            ItemLogic itemLogic = new ItemLogic();
            //Then, create the item using data from KijijiItem
            Map<String, String[]> kijijiItemMap = new HashMap<>();
            kijijiItemMap.put(ItemLogic.DESCRIPTION, new String[]{e.getDescription()});
            kijijiItemMap.put(ItemLogic.LOCATION, new String[]{e.getLocation()});
            kijijiItemMap.put(ItemLogic.PRICE, new String[]{e.getPrice()});
            kijijiItemMap.put(ItemLogic.TITLE, new String[]{e.getTitle()});
            kijijiItemMap.put(ItemLogic.DATE, new String[]{e.getDate()});
            kijijiItemMap.put(ItemLogic.URL, new String[]{e.getUrl()});
            kijijiItemMap.put(ItemLogic.ID, new String[]{e.getId()});
            try{
                Item item = new ItemLogic().createEntity(kijijiItemMap);
                item.setCategory(cat);
                item.setImage(image);
                if(itemLogic.getWithId(item.getId())==null)
                    itemLogic.add(item); //here we finally add the item to the database. 
            }
            catch(ValidationException ex){
                log("KijijiView GET: "+ex.getMessage());
            }
            

        });

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
        Consumer<KijijiItem> saveItems = (KijijiItem item) -> {
            System.out.println(item);
        };

        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
}
