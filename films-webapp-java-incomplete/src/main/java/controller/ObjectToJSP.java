package controller;

import javax.servlet.http.HttpServletRequest;

/**
 * ObjectToJSP abstract class.
 * 
 * Contains commonly used functionality by it's extensions FilmToJSP.java and FilmListToJSP.java.
 *
 * @author ZAYNE
 *
 */

public abstract class ObjectToJSP {

	//Returns "json" by default, e.g. if format is missing or spelled incorrectly. Otherwise returns user defined format.
	public String validateFormat(HttpServletRequest request) {
		if (request.getParameter("format") == null || !request.getParameter("format").toLowerCase().equals("json")
				&& !request.getParameter("format").toLowerCase().equals("xml")
				&& !request.getParameter("format").toLowerCase().equals("text")) {
			return "json";
		} else {
			return request.getParameter("format").toLowerCase();
		}
	}
	
	//Returns error messages if user entered values are missing or contain illegal characters/values.
	public String validateParamValues(HttpServletRequest request, String servletName) {
		String errMsg = "";
		if (servletName.equals("get-film-by-id")) {
			if (request.getParameter("id") == null) {
				errMsg = "ID is undefined.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("id").contains("|") || request.getParameter("id").contains("@")) {
				errMsg = "ID contains illegal characters (| or @).\n";
				System.out.println(errMsg);
			} else {
				return null;
			}
		} else if (servletName.equals("get-films-by-title")) {
			if (request.getParameter("title") == null) {
				errMsg = "Title is undefined.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("title").length() > 100) {
				errMsg = "Title is greater than 100 characters.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("title").contains("|") || request.getParameter("title").contains("@")) {
				errMsg = "Title contains illegal characters (| or @).\n";
				System.out.println(errMsg);
			} else {
				return null;
			}
		} else if (servletName.equals("delete-film")) {
			if (request.getParameter("id") == null) {
				errMsg = "ID is undefined.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("id").contains("|") || request.getParameter("id").contains("@")) {
				errMsg = "ID contains illegal characters (| or @).\n";
				System.out.println(errMsg);
			} else {
				return null;
			}
		} else {
			if (request.getParameter("title") == null || request.getParameter("year") == null
					|| request.getParameter("director") == null || request.getParameter("stars") == null
					|| request.getParameter("review") == null) {
				errMsg = "Either title, year, director, stars, or review is undefined.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("title").length() > 100 || request.getParameter("director").length() > 100
					|| request.getParameter("stars").length() > 100) {
				errMsg = "Either title, director, or stars is greater than 100 characters.\n";
				System.out.println(errMsg);
			} else if (Integer.parseInt(request.getParameter("year")) < 1000
					|| Integer.parseInt(request.getParameter("year")) > 2022) {
				errMsg = "Year is either less than 1000 or greater than 2022.\n";
				System.out.println(errMsg);
			} else if (request.getParameter("title").contains("|")
					|| request.getParameter("year").contains("|") || request.getParameter("director").contains("|")
					|| request.getParameter("stars").contains("|") || request.getParameter("review").contains("|")) {
				errMsg = "This entry contains illegal characters (|).\n";
				System.out.println(errMsg);
			} else if (request.getParameter("title").contains("@")
					|| request.getParameter("year").contains("@") || request.getParameter("director").contains("@")
					|| request.getParameter("stars").contains("@") || request.getParameter("review").contains("@")) {
				errMsg = "This entry contains illegal characters (@).\n";
				System.out.println(errMsg);
			} else {
				return null;
			}
		}
		return errMsg;
	}
}
