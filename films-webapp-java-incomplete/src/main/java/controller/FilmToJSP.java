package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Film;
import model.Films;

/**
 * FilmToJSP class.
 * 
 * Used by servlets to generate JSP pages containing structured data, e.g. JSON/XML/TEXT. 
 * Data is converted to JSON/XML format by GSON/JAXB. For text, data is separated by uncommon-
 * characters (| to separate Film properties e.g. ID and Title, @ to separate Film from Film).
 * 
 * From utils.js:
 * var filmInfo = films[i].split("|");
 * 
 * Utilises common functionality (validation) from ObjectToJSP by extending it.
 *
 * @author ZAYNE
 *
 */

public class FilmToJSP extends ObjectToJSP {
	
	//Specifies file name based on format param. value. Calls other methods to write data to the file.
	public String generateJSP(String format, Film film, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String outputPage = null;
		if (format.equals("json")) {
			response.setContentType("application/json");
			outputPage = "films-json.jsp";
			jsonToFile(film, outputPage);
		} else if (format.equals("xml")) {
			try {
				response.setContentType("text/xml");
				outputPage = "films-xml.jstl";
				xmlToFile(film, outputPage);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else if (format.equals("text")) {
			response.setContentType("text/plain");
			outputPage = "films-text.jsp";
			textToFile(film, outputPage);
		} else {
			System.out.println("Type JSON, XML or TEXT not specified.");
		}
		return outputPage;
	}

	//Uses FileWriter with GSON to write Film object to the file.
	public void jsonToFile(Film film, String outputPage) throws IOException {
		try (FileWriter fw = new FileWriter(outputPage)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(film, fw);
		}
	}
	
	//Uses FileWriter with JAXB to write Film object to the file.
	//Have to insert the Film object into the Films object ArrayList for JAXB to handle.
	public void xmlToFile(Film film, String outputPage) throws IOException, JAXBException {
		try (FileWriter fw = new FileWriter(outputPage)) {
			ArrayList<Film> filmList = new ArrayList<Film>();
			filmList.add(film);
			Films films = new Films();
			films.setFilmList(filmList);
			JAXBContext context = JAXBContext.newInstance(Films.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(films, fw);
		}
	}
	
	//Uses FileWriter with BufferedWriter to write Film object to the file.
	//Writes to the file using uncommon characters to identify Film properties.
	public void textToFile(Film film, String outputPage) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPage))) {
			String filmStr = String.format("%d|%s|%d|%s|%s|%s@", film.getId(), film.getTitle(), film.getYear(),
					film.getDirector(), film.getStars(), film.getReview());
			bw.write(filmStr);
			bw.flush();
		}
	}
}
