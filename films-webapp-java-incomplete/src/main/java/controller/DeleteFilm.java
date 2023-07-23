package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Film;
import model.HibernateUtil;

/**
 * DeleteFilm class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. The doPost service method is passed these object to extract parameter
 * values like the data format from it e.g. id=10852. 
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to delete using the 
 * appropriate data. It also calls the HibernateUtil.java object to getAllFilms() which is then dispatched so that the user can see the
 * film is no longer recorded.
 * 
 * The also class creates an FilmListToJSP.java object to handle validation and convert the Film objects into files containing
 * the data in JSON, XML or TEXT format.
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "delete-film", urlPatterns = { "/delete-film" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class DeleteFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteFilm() {
		super();
	}
	
	//Delete part of CRUD.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter(); //To print validation error messages to the screen.
		FilmListToJSP fljsp = new FilmListToJSP();
		
		String errMsg = fljsp.validateParamValues(request, "delete-film"); //Parameter validation.
		
		if (errMsg == null) {
			int id = Integer.parseInt(request.getParameter("id"));

			HibernateUtil hu = new HibernateUtil();
			Film film = new Film();
			film = hu.getFilmByID(id);
			if (film != null) {
				System.out.println(film);
			} else {
				System.out.println("No matches found.");
			}
			hu.deleteFilm(id); //Deletes the film record based on the provided ID param. value.
			System.out.println("Delete film: " + id);
		} else {
			out.println("<h1>Delete Film</h1>");
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
	
	public void textToFile(String responseStr, String outputPage) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPage))) {
			bw.write(responseStr);
			bw.flush();
		}
	}
}