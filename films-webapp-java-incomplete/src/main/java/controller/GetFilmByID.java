package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Film;
import model.HibernateUtil;

/**
 * GetFilmByID class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. The doGet service method is passed these object to extract parameter
 * values like the data format from it e.g. id=10852, and create a Film object based on the value.
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to extract the appropriate data.
 * 
 * The also class creates an FilmToJSP.java object to handle validation and convert the Film objects into files containing
 * the data in JSON, XML or TEXT format. 
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "get-film-by-id", urlPatterns = { "/get-film-by-id" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class GetFilmByID extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetFilmByID() {
		super();
	}
	
	//Read part of CRUD.
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//No caching, i.e. every time the service is called, latest, non-cached version of get-film-by-id is provided.
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		PrintWriter out = response.getWriter();
		FilmToJSP fjsp = new FilmToJSP();
		
		String format = fjsp.validateFormat(request); //Format parameter validation.
		String errMsg = fjsp.validateParamValues(request, "get-film-by-id"); //Other parameter validation.

		if (errMsg == null) {
			int id = Integer.parseInt(request.getParameter("id"));

			HibernateUtil hu = new HibernateUtil();
			Film film = new Film();
			film = hu.getFilmByID(id); //Finds a film record matching the provided ID and makes a Film object from it.
			System.out.println("Get film by ID:");
			if (film != null) {
				System.out.println("Films database record found:");
				System.out.println(film);
				
				request.setAttribute("film", film);

				//Generates a JSP page from the Film object based on the format param. value.
				String outputPage = fjsp.generateJSP(format, film, request, response);
				System.out.println(outputPage + "\n");
				RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
				dispatcher.include(request, response); //Dispatches to whichever resource called get-film-by-id, i.e. the xhr method in utils.js.
			} else {
				System.out.println("No matches found.\n");
				out.println("No matches found.");
			}
		} else {
			out.println("<h1>Get Film By ID</h1>");
			out.println("<h2>Error encountered:</h2>");
			out.println("<h2>" + errMsg + "</h2>");
			System.out.println(errMsg);
		}
	}
}