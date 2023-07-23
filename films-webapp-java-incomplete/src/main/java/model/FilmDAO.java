package model;

import java.util.ArrayList;
import java.sql.*;

public class FilmDAO {
	/*Film oneFilm = new Film();
	Connection conn = null;
	Statement stmt = null;
	String dbName = "films_schema";
	String ipAddr = "35.197.205.156";
	String port = "3306";
	String username = "root";
	String password = "cOk2ia8LJFfHMCx8";
	String url = String.format("jdbc:mysql://%1$s:%2$s/%3$s", ipAddr, port, dbName);*/
	Film oneFilm = new Film();
	Connection conn = null;
	Statement stmt = null;
	String dbName = "films-schema";
	String ipAddr = "localhost";
	String port = "3306";
	String username = "root";
	String password = "";
	String url = String.format("jdbc:mysql://%1$s:%2$s/%3$s", ipAddr, port, dbName);
	// Note none default port used, 6306 not 3306

	public FilmDAO() {
	}

	public void openConnection() {
		// loading jdbc driver for mysql
		try {
			System.out.println(url);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.out.println(e);
		}
		// connecting to database
		try {
			// connection string for demos database, username demos, password demos
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	private void closeConnection() {
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs) {
		Film thisFilm = null;
		try {
			thisFilm = new Film(rs.getString("title"), rs.getInt("year"), rs.getString("director"),
					rs.getString("stars"), rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thisFilm;
	}

	public ArrayList<Film> getAllFilms() {
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				allFilms.add(oneFilm);
			}
			System.out.println("All film(s) retrieved..");
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
		return allFilms;
	}

	public Film getFilmByID(int id) {
		openConnection();
		oneFilm = null;
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films where id=" + id;
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				System.out.println("Film(s) found by ID.");
			}
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
		return oneFilm;
	}

	public Film getLastFilm() {
		openConnection();
		oneFilm = null;
		// Create select statement and execute it
		try {
			String selectSQL = "select * from films order by id desc limit 1";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				System.out.println("Last film found.");
			}
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
		return oneFilm;
	}

	public ArrayList<Film> getFilmByTitle(String title) {
		ArrayList<Film> matchedFilmList = new ArrayList<Film>();
		openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from films where title like ?");
			pstmt.setString(1, "%" + title + "%");
			ResultSet rs1 = pstmt.executeQuery();
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				matchedFilmList.add(oneFilm);
			}
			pstmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
		return matchedFilmList;
	}

	public void insertFilm(String title, int year, String director, String stars, String review) {
		openConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("insert into films(title, year, director, stars, review) values (?, ?, ?, ?, ?)");
			pstmt.setString(1, title);
			pstmt.setInt(2, year);
			pstmt.setString(3, director);
			pstmt.setString(4, stars);
			pstmt.setString(5, review);
			int rs1 = pstmt.executeUpdate();
			if (rs1 == 1) {
				System.out.println("Insert successful.");
			} else {
				System.out.println("Insert unsuccessful.");
			}
			pstmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	public void updateFilm(Film film) {
		int id = film.getId();
		String title = film.getTitle();
		int year = film.getYear();
		String director = film.getDirector();
		String stars = film.getStars();
		String review = film.getReview();
		openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(
					"update films set title=?, year=?, director=?, stars=?, review=? where id=?");
			pstmt.setString(1, title);
			pstmt.setInt(2, year);
			pstmt.setString(3, director);
			pstmt.setString(4, stars);
			pstmt.setString(5, review);
			pstmt.setInt(6, id);
			int rs1 = pstmt.executeUpdate();
			if (rs1 == 1) {
				System.out.println("Update successful.");
			} else {
				System.out.println("Update unsuccessful.");
			}
			pstmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	public void deleteFilm(Film film) {
		int id = film.getId();
		openConnection();
		try {
			String deleteSQL = String.format("delete from films where id=" + id);
			int rs1 = stmt.executeUpdate(deleteSQL);
			if (rs1 == 1) {
				System.out.println("Delete successful.");
			} else {
				System.out.println("Delete unsuccessful.");
			}
			stmt.close();
			closeConnection();
		} catch (SQLException se) {
			System.out.println(se);
		}
	}
}
