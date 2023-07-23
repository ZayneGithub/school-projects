package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Film;
import model.HibernateUtil;

/**
 * GetAllFilms class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. The doGet service method is passed these object to extract parameter
 * values like the data format from it e.g. format=XML, and create an ArrayList of Film objects based on these values.
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to extract the appropriate data.
 * 
 * The also class creates an FilmListToJSP.java object to handle validation and convert the ArrayList of Film objects
 * into files containing the data in JSON, XML or TEXT format. 
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "get-all-films", urlPatterns = { "/get-all-films" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class GetAllFilms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetAllFilms() {
		super();
	}
	
	//Read part of CRUD.
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//No caching, i.e. every time the service is called, latest, non-cached version of get-all-films is provided.
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		FilmListToJSP fljsp = new FilmListToJSP();
		String format = fljsp.validateFormat(request); //Format parameter validation.

		HibernateUtil hu = new HibernateUtil();
		ArrayList<Film> filmList = new ArrayList<Film>();
		filmList.addAll(hu.getAllFilms()); //Puts ArrayList from Hibernate into separate ArrayList of Film objects.
		System.out.println("Get all films:");
		if (filmList.isEmpty()) {
			System.out.println("Films database empty.");
		} else {
			System.out.println("Films database entries found. Example film record:");
			for (int i = 0; i < 1; i++) {
				System.out.println(filmList.get(i));
			}
		}
		request.setAttribute("films", filmList);
		
		//Generates a JSP page from the ArrayList of Film objects based on the format param. value.
		String outputPage = fljsp.generateJSP(format, filmList, request, response);
		System.out.println(outputPage + "\n");
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response); //Dispatches to whichever resource called get-all-films, i.e. the xhr method in utils.js.
	}
}