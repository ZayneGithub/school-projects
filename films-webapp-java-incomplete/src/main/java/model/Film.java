package model;

import java.util.Observable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Standard Film Java Bean.
 * 
 * Provides Getter/Setter and toString() methods, a public no-argument constructor.
 * Includes XML annotations for JAXB compatibility, and java.x.persistence annotations-
 * for Hibernate compatibility.
 * 
 * Class used by SessionFactory in HibernateUtils.java.
 * From HibernateUtils.java:
 * sessionFactory = new AnnotationConfiguration()
 * 	.configure()
 * 	.addAnnotatedClass(Film.class)
 * 	.buildSessionFactory();
 * 
 * Extends Observable class, informing TitleObserver.java and YearObserver.java upon changes.
 * 
 * @author ZAYNE
 */

@XmlRootElement(name = "film") //XML annotations.
@XmlType(propOrder = { "id", "title", "year", "director", "stars", "review" })

@Entity //Persistence/Hibernate annotations.
@Table(name = "films")
public class Film extends Observable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Lets database handle primary key generation.
	@Column(name = "id") //Links Film variables to Film columns.
	int id;
	@Column(name = "title")
	String title;
	@Column(name = "year")
	int year;
	@Column(name = "director")
	String director;
	@Column(name = "stars")
	String stars;
	@Column(name = "review")
	String review;

	public Film(String title, int year, String director, String stars, String review) {
		super();
		this.title = title;
		this.year = year;
		this.director = director;
		this.stars = stars;
		this.review = review;
		System.out.println("Film object created: " + title + " released in " + year + " directed by " + director
				+ " staring " + stars + ". Sample review: " + review);
	}

	public Film() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setChanged();
		notifyObservers(title); //Notifies Title & Year Observers.
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		setChanged();
		notifyObservers(year);
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	@Override
	public String toString() {
		return "Film [id=" + id + ", title=" + title + ", year=" + year + ", director=" + director + ", stars=" + stars
				+ ", review=" + review + "]";
	}
}
