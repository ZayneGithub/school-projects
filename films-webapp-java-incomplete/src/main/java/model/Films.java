package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * Films class.
 * 
 * Essentially an ArrayList with getter/setter methods.
 * Mainly used for XML annotations so that JAXB can access list of films.
 * 
 * From FilmListToJSP.java:
 * JAXBContext context = JAXBContext.newInstance(Films.class);
 * 
 * @author ZAYNE
 *
 */

@XmlRootElement(name = "films") //Required root element.
@XmlAccessorType(XmlAccessType.FIELD)
public class Films {
	
	@XmlElement(name = "film") //Sets name of elements.
	private ArrayList<Film> filmList;

	public void setFilmList(ArrayList<Film> filmList) {
		this.filmList = filmList;
	}

	public ArrayList<Film> getFilmList() {
		return filmList;
	}
}