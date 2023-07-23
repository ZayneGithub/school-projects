package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * HibernateUtil standard class.
 * 
 * Obtains the Hibernate SessionFactory instance, which is then used for
 * building hibernate sessions. Sessions are NOT threat safe, always close
 * session in each method.
 * 
 * Class also contains CRUD operations, returning either a Film or ArrayList of
 * film objects, depending on use case.
 * 
 * Configuration at hibernate.cfg.xml.
 * 
 * @author ZAYNE
 *
 */

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	private static SessionFactory buildSessionFactory() { // Includes annotated Film.java class.
		sessionFactory = new AnnotationConfiguration().configure("hibernate.cfg.xml").addAnnotatedClass(Film.class).buildSessionFactory();
		return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory;
	}

	// Read part of CRUD.
	// Iterates through List of casted film objects and adds them to an ArrayList.
	public ArrayList<Film> getAllFilms() {
		ArrayList<Film> filmList = new ArrayList<Film>();
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<?> films = session.createQuery("from Film").list();
			for (Iterator<?> iterator = films.iterator(); iterator.hasNext();) {
				Film film = (Film) iterator.next();
				filmList.add(film);
			}
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
		return filmList;
	}

	// Read part of CRUD.
	// Casts the first match found within the list as a Film object and returns it.
	public Film getFilmByID(int id) {
		Film film = new Film();
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			//List<?> films = session.createQuery("select f from Film f where f.id = " + id).list();
			Query query = session.createQuery("select f from Film f where f.id = " + id);
			query.setMaxResults(1); //Limited to 1 result, the record with the highest ID #.
			film = (Film) query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
		return film;
	}

	// Read part of CRUD.
	// Iterates through List of matched (casted) film objects and adds them to an
	// ArrayList.
	public ArrayList<Film> getFilmsByTitle(String title) {
		ArrayList<Film> filmList = new ArrayList<Film>();
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("select f from Film f where f.title like CONCAT('%', ?, '%')");
			query.setString(0, title); // Sets ? in query to title parameter value.
			List<?> films = query.list();
			if (!films.isEmpty()) {
				for (Iterator<?> iterator = films.iterator(); iterator.hasNext();) {
					Film film = (Film) iterator.next();
					filmList.add(film);
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
		return filmList;
	} // SQL Injection...

	// Read part of CRUD.
	// Used to return the film object most recently inserted into the database.
	public Film getLastFilm() {
		Film film = new Film();
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from Film order by id DESC"); // Descending order.
			query.setMaxResults(1); // Limited to 1 result, the record with the highest ID #.
			film = (Film) query.uniqueResult();// Convenience method to return a single instance that matches the query.
			System.out.println(film);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
		return film;
	}

	// Create part of CRUD.
	// Creates a new Film object based on param. values, inserted using
	// saveOrUpdate.
	public void insertFilm(String title, int year, String director, String stars, String review) {
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Film film = new Film(title, year, director, stars, review);
			session.saveOrUpdate(film);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
	}

	// Update part of CRUD.
	// Gets the record matching the ID param. value. Uses setters to update the Film
	// object then update.
	public void updateFilm(int id, String title, int year, String director, String stars, String review) {
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Film film = (Film) session.get(Film.class, id);
			film.setTitle(title);
			film.setYear(year);
			film.setDirector(director);
			film.setStars(stars);
			film.setReview(review);
			session.update(film);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
	}

	// Delete part of CRUD.
	// Gets the record matching the ID param. value then delete.
	public void deleteFilm(int id) {
		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Film film = (Film) session.get(Film.class, id);
			session.delete(film);
			tx.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally {
			session.close();
		}
	}
}
