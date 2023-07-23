package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Film;
import model.HibernateUtil;

/**
 * GetAllFilmTitles class.
 * 
 * A Servlet class by extending HttpServlet interface and using javax.servlet annotations and features including
 * HttpServlet request and response objects. Unlike GetAllFilms.java, the doGet method here does not extract any
 * parameter values as this servlet is used to display the film titles in the search bar for easier user navigation.
 * Therefore, there is no validation, nor the need to create a FilmListToJSP.java object.
 * 
 * The class creates a HibernateUtil.java object to handle database connections and queries to extract the appropriate data.
 * 
 * ArrayList of Film objects from calling get-all-films is iterated through, the title is inserted into a separate
 * ArrayList of Strings. The JSP is generated based on this list but using JSON only.
 * 
 * @author ZAYNE
 *
 */

@WebServlet(name = "get-all-film-titles", urlPatterns = { "/get-all-film-titles" }) //Servlet annotations, rather than using servlet mappings in web.xml.
public class getAllFilmTitles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public getAllFilmTitles() {
		super();
	}
	
	//Read part of CRUD.
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//No caching, i.e. every time the service is called, latest, non-cached version of get-all-film-titles is provided.
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		HibernateUtil hu = new HibernateUtil();
		ArrayList<Film> filmList = new ArrayList<Film>();
		ArrayList<String> filmTitleList = new ArrayList<String>();
		filmList.addAll(hu.getAllFilms()); //Puts ArrayList from Hibernate into separate ArrayList of Film objects.
		System.out.println("Get all film titles:");
		if (filmList.isEmpty()) {
			System.out.println("Films database empty.");
		} else {
			System.out.println("Films database entries found. Example film record title:");
			for (int i = 0; i < filmList.size(); i++) {
				filmTitleList.add(filmList.get(i).getTitle()); //For each Film object, add the title to a separate ArrayList.
			}
			for (int i = 0; i < 1; i++) {
				System.out.println(filmTitleList.get(i));
			}
		}
		request.setAttribute("filmTitleList", filmTitleList);
		response.setContentType("application/json"); //Using JSON only in this class.
		
		//Generates a JSP page from the ArrayList of Strings.
		//String googleBucket = "gs://films-web-service.appspot.com/";
		//String outputPage = googleBucket + "film-titles-json.jsp";
		String outputPage = "film-titles-json.jsp";
		jsonToFile(filmTitleList, outputPage);
		System.out.println(outputPage + "\n");
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response); //Dispatches to whichever resource called get-all-film-titles, i.e. the getAllFilmTitles() method in films.js.
	}
	
	//Separate jsonToFile method from the FileListToJSP.java class, as there are no parameters to pass.S 
	private static void jsonToFile(ArrayList<String> filmTitleList, String outputPage) throws IOException {
		try (FileWriter fw = new FileWriter(outputPage)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(filmTitleList, fw);
		}
	}
}