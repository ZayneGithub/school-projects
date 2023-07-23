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
 * UpdateFilm class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. The doPost service method is passed these object to extract parameter
 * values like the data format from it e.g. id=10852&title=rocky. 
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to update using the 
 * appropriate data.
 * 
 * The also class creates an FilmToJSP.java object to handle validation and convert the Film objects into files containing
 * the data in JSON, XML or TEXT format.
 * 
 * It also adds observers to the Film object to inform the observers that changes have been made to a Film object.
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "update-film", urlPatterns = { "/update-film" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class UpdateFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateFilm() {
		super();
	}

	//Update part of CRUD.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter(); //To print validation error messages to the screen.
		FilmToJSP fjsp = new FilmToJSP();

		String format = fjsp.validateFormat(request); //Format parameter validation.
		String errMsg = fjsp.validateParamValues(request, "update-film"); //Other parameter validation.

		if (errMsg == null && !(request.getParameter("id") == null)) {
			int id = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title").toUpperCase();
			int year = Integer.parseInt(request.getParameter("year"));
			String director = request.getParameter("director").toUpperCase();
			String stars = request.getParameter("stars").toUpperCase();
			String review = request.getParameter("review");

			HibernateUtil hu = new HibernateUtil();
			hu.updateFilm(id, title, year, director, stars, review); //Updates the film record based on the provided param. values.

			Film film = new Film();
			System.out.println("Update film:");
			film.setId(id);
			film.setTitle(title);
			film.setYear(year);
			film.setDirector(director);
			film.setStars(stars);
			film.setReview(review);
			System.out.println(film);

			request.setAttribute("film", film);

			//Generates a JSP page from the Film object based on the format param. value.
			String outputPage = fjsp.generateJSP(format, film, request, response);
			System.out.println(outputPage + "\n");
			RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
			dispatcher.include(request, response); //Dispatches to whichever resource called update-film, i.e. the xhr method in utils.js.
		} else if (request.getParameter("id") == null) { //Extra parameter validation.
			errMsg = "ID is undefined.\n";
			out.println("<h1>Error encountered:</h1>");
			out.println("<h2>" + errMsg + "</h2>");
			System.out.println(errMsg);
		} else if (request.getParameter("id").contains("|") 
				|| request.getParameter("id").contains("@")) {
			errMsg = "ID contains illegal characters (| or @).\n";
			out.println("<h1>Error encountered:</h1>");
			out.println("<h2>" + errMsg + "</h2>");
			System.out.println(errMsg);
		} else {
			out.println("<h1>Update Film</h1>");
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