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
 * InsertFilm class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. The doPost service method is passed these object to extract parameter
 * values like the data format from it e.g. title=rocky&year=1976. 
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to insert using the appropriate 
 * data. It also calls the HibernateUtil.java object to getLastFilm() which is then dispatched so that the user can see the
 * latest inserted film.
 * 
 * The also class creates an FilmToJSP.java object to handle validation and convert the Film objects into files containing
 * the data in JSON, XML or TEXT format.
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "insert-film", urlPatterns = { "/insert-film" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class InsertFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InsertFilm() {
		super();
	}
	
	//Create part of CRUD.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter(); //To print validation error messages to the screen.
		FilmToJSP fjsp = new FilmToJSP();

		String format = fjsp.validateFormat(request); //Format parameter validation.
		String errMsg = fjsp.validateParamValues(request, "insert-film"); //Other parameter validation.

		if (errMsg == null) {
			String title = request.getParameter("title").toUpperCase();
			int year = Integer.parseInt(request.getParameter("year"));
			String director = request.getParameter("director").toUpperCase();
			String stars = request.getParameter("stars").toUpperCase();
			String review = request.getParameter("review");

			HibernateUtil hu = new HibernateUtil();
			hu.insertFilm(title, year, director, stars, review); //Inserts a film record based on the provided param. values.
			Film film = hu.getLastFilm(); //Gets the last film record, the latest inserted film.
			System.out.println("Insert film:");
			System.out.println(film);

			request.setAttribute("film", film);
			
			//Generates a JSP page from the Film object based on the format param. value.
			String outputPage = fjsp.generateJSP(format, film, request, response);
			System.out.println(outputPage + "\n");
			RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
			dispatcher.include(request, response); //Dispatches to whichever resource called insert-film, i.e. the xhr method in utils.js.
		} else {
			out.println("<h1>Insert Film</h1>");
			out.println("<h2>Error encountered:</h2>");
			out.println("<h2>" + errMsg + "</h2>");
			System.out.println(errMsg);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		doPost(request, response);
	}
}