/*
 * Code Snippet, $function() --> $("#search-tagged").autocomplete({source: filmTitleList});
 * $("#accordion").empty();
 * $("#table").show();
 * $('#table').DataTable();
 * $(document).ready(function() {
 * 		$('#table').DataTable();
 * });
 * $('#table').DataTable().destroy();
 */


/*
 * Code Snippet, getAllFilmsClickHandler() --> if (document.getElementById("data-type").value == "JSON") {}
 * getAllFilmsJson("#get-all-films-table");
 */

// <textarea placeholder="e.g. A dark, gritty, and realistic look..." name="review" rows="4" cols="50"></textarea>

/*function getFilmTable(rows) {
	var headings =
		["id", "title", "year", "director", "stars", "review"];
	return (getTable(headings, rows));
}

function jsonFilmTable(resultRegion) {
	var address = "Control";
	var data = makeParamString("json");
	ajaxPost(address, data,
		function(request) {
			showJsonFilmInfo(request, resultRegion);
		});
}

function showJsonFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var rawData = request.responseText;
		var films = JSON.parse(rawData);
		console.log(films);
		var rows = new Array();
		for (var i = 0; i < films.length; i++) {
			var film = films[i];
			rows[i] = [film.id, film.title, film.year,
			film.director, film.stars, film.review];
		}
		console.log(rows);
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
	}
}*/

/*
 *Code Snippet, showJsonFilmInfo() --> var films = eval("(" + rawData + ")");
 */

/*function xmlFilmTable(resultRegion) {
	var address = "Control";
	var data = makeParamString("xml");
	ajaxPost(address, data,
		function(request) {
			showXmlFilmInfo(request, resultRegion);
		});
}

function showXmlFilmInfo(request, resultRegion) {
	if ((request.readyState == 4) &&
		(request.status == 200)) {
		var xmlDocument = request.responseXML;
		var films =
			xmlDocument.getElementsByTagName("film");
		var rows = new Array();
		for (var i = 0; i < films.length; i++) {
			var film = films[i];
			var subElements =
				["id", "title", "year", "director", "stars", "review"];
			rows[i] = getElementValues(film, subElements);
		}
		console.log(rows);
		var table = getFilmTable(rows);
		htmlInsert(resultRegion, table);
	}
}*/

/*function makeParamString(format) {
  var paramString =
	"format=" + format;
  return(paramString);
}*/

// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry). 

/*function getRequestObject() {
  if (window.XMLHttpRequest) {
	return(new XMLHttpRequest());
  } else if (window.ActiveXObject) {
	return(new ActiveXObject("Microsoft.XMLHTTP"));
  } else {
	return(null);
  }
}

// Insert the html data into the element
// that has the specified id.

function htmlInsert(id, htmlData) {
  document.getElementById(id).innerHTML = htmlData;
}

// Return escaped value of textfield that has given id.
// The builtin "escape" function url-encodes characters.

function getValue(id) {
  return(escape(document.getElementById(id).value));
}

// Generalized version of ajaxResultPost. In this
// version, you pass in a response handler function
// instead of just a result region.

function ajaxPost(address, data, responseHandler) {
  var request = getRequestObject();
  request.onreadystatechange =
	function() { responseHandler(request); };
  request.open("POST", address, true);
  request.setRequestHeader("Content-Type",
						   "application/x-www-form-urlencoded");
  request.send(data);
}

// Given an element, returns the body content.

function getBodyContent(element) {
  element.normalize();
  return(element.childNodes[0].nodeValue);
}

// Given a doc and the name of an XML element, returns an
// array of the values of all elements with that name.
// E.g., for
//   <foo><a>one</a><q>two</q><a>three</a></foo>
// getXmlValues(doc, "a") would return
//   ["one", "three"].

function getXmlValues(xmlDocument, xmlElementName) {
  var elementArray =
	 xmlDocument.getElementsByTagName(xmlElementName);
  var valueArray = new Array();
  for(var i=0; i<elementArray.length; i++) {
	 valueArray[i] = getBodyContent(elementArray[i]);
  }
  return(valueArray);
}

// Given an element object and an array of sub-element names,
// returns an array of the values of the sub-elements.
// E.g., for <foo><a>one</a><c>two</c><b>three</b></foo>,
// if the element points at foo,
// getElementValues(element, ["a", "b", "c"]) would return
// ["one", "three", "two"]

function getElementValues(element, subElementNames) {
  var values = new Array(subElementNames.length);
  for(var i=0; i<subElementNames.length; i++) {
	var name = subElementNames[i];
	var subElement = element.getElementsByTagName(name)[0];
	values[i] = getBodyContent(subElement);
  }
  return(values);
}

// Takes as input an array of headings (to go into th elements)
// and an array-of-arrays of rows (to go into td
// elements). Builds an xhtml table from the data.

function getTable(headings, rows) {
  var table = "<table border='1' class='ajaxTable'>\n" +
			  getTableHeadings(headings) +
			  getTableBody(rows) +
			  "</table>";
  return(table);
}

function getTableHeadings(headings) {
  var firstRow = "  <tr>";
  for(var i=0; i<headings.length; i++) {
	firstRow += "<th>" + headings[i] + "</th>";
  }
  firstRow += "</tr>\n";
  return(firstRow);
}

function getTableBody(rows) {
  var body = "";
  for(var i=0; i<rows.length; i++) {
	body += "  <tr>";
	var row = rows[i];
	for(var j=0; j<row.length; j++) {
	  body += "<td>" + row[j] + "</td>";
	}
	body += "</tr>\n";
  }
  return(body);
}*/

/*$(function() {
	$("#jquery-json-film-button").click(jqueryJsonClickHandler);
	$("#jquery-deferred-json-film-button").click(jqueryDeferredJsonClickHandler);
	$("#jquery-deferred-final-json-film-button").click(jqueryDeferredFinalJsonClickHandler);
	$("#jquery-xml-film-button").click(jqueryXmlClickHandler);
	$("#jquery-deferred-xml-film-button").click(jqueryDeferredXmlClickHandler);
	$("#jquery-deferred-final-xml-film-button").click(jqueryDeferredFinalXmlClickHandler);
});

function jqueryJsonClickHandler() {
	jqueryJsonFilmTable("#jquery-json-film-table");
}

function jqueryDeferredJsonClickHandler() {
	jqueryDeferredJsonFilmTable("#jquery-deferred-json-film-table");
}

function jqueryDeferredFinalJsonClickHandler() {
	jqueryDeferredFinalJsonFilmTable("#jquery-deferred-final-json-film-table");
}

function jqueryXmlClickHandler() {
	jqueryXmlFilmTable("#jquery-xml-film-table");
}

function jqueryDeferredXmlClickHandler() {
	jqueryDeferredXmlFilmTable("#jquery-deferred-xml-film-table");
}

function jqueryDeferredFinalXmlClickHandler() {
	jqueryDeferredFinalXmlFilmTable("#jquery-deferred-final-xml-film-table");
}

function getFilmTable(rows) {
	var headings =
		["id", "title", "year", "director", "stars", "review"];
	return (getTable(headings, rows));
}

function insertText(text, resultRegion) {
	$(resultRegion).html(text);
}

function highlight(selector) {
	$(selector).hide().show("highlight");
}

function determineError(jqXHR, exception) { //copied from internet, redesign
	var msg = '';
	if (jqXHR.status === 0) {
		msg = 'Not connect.\n Verify Network.';
	} else if (jqXHR.status == 404) {
		msg = 'Requested page not found. [404]';
	} else if (jqXHR.status == 500) {
		msg = 'Internal Server Error [500].';
	} else if (exception === 'parsererror') {
		msg = 'Requested parse failed.';
	} else if (exception === 'timeout') {
		msg = 'Time out error.';
	} else if (exception === 'abort') {
		msg = 'Ajax request aborted.';
	} else {
		msg = 'Uncaught Error.\n' + jqXHR.responseText;
	}
	return msg;
}

function xhrGet(url, dataType, data, resultRegion) {
	return $.ajax({
		url: url,
		method: "GET",
		data: data,
		dataType: dataType,
	})
	.fail(function(jqXHR, exception) {
		var msg = determineError(jqXHR, exception);
		insertText(msg, resultRegion);
	})
	.always(function() {
		highlight(resultRegion);
	});
}

function jqueryJsonFilmTable(resultRegion) {
	var data = makeParamString("json");
	$.ajax({
		url: "Control",
		method: "GET", //default
		data: data,
		dataType: "json",
		success:
			function(result) {
				console.log("jquery JSON success");
				highlight(resultRegion);
				jqueryShowJsonFilmInfo(result, resultRegion);
			},
		error: function(jqXHR, exception) {
			var msg = determineError(jqXHR, exception);
			insertText(msg, resultRegion);
		},
	});
}*/

/*
 *Code Snippet, jqueryJsonFilmTable() --> result = JSON.stringify(result);;
 */

/*function jqueryDeferredJsonFilmTable(resultRegion) {
	var data = makeParamString("json");
	$.ajax({
		url: "Control",
		method: "GET", //default
		data: data,
		dataType: "json",
	})
	.done(function(result) {
		console.log("jquery deferred JSON success");
		jqueryShowJsonFilmInfo(result, resultRegion);
	})
	.fail(function(jqXHR, exception) {
		var msg = determineError(jqXHR, exception);
		insertText(msg, resultRegion);
	})
	.always(function() {
		highlight(resultRegion);
	});
}

function jqueryDeferredFinalJsonFilmTable(resultRegion) {
	xhrGet("Control", "json", makeParamString("json"), resultRegion).done(function(result) {
		console.log("jquery deferred final JSON success");
		jqueryShowJsonFilmInfo(result, resultRegion);
	});
}

function jqueryShowJsonFilmInfo(result, resultRegion) {
	var rows = new Array();
	for (var i = 0; i < result.length; i++) {
		var film = result[i];
		rows[i] = [film.id, film.title, film.year,
		film.director, film.stars, film.review];
	}
	console.log(rows);
	var table = getFilmTable(rows);
	insertText(table, resultRegion);
}

function jqueryXmlFilmTable(resultRegion) {
	var data = makeParamString("xml");
	$.ajax({
		url: "Control",
		method: "GET", //default
		data: data,
		dataType: "xml",
		success:
			function(result) {
				console.log("jquery XML success");
				highlight(resultRegion);
				jqueryShowXmlFilmInfo(result, resultRegion);
			},
		error: function(jqXHR, exception) {
			var msg = determineError(jqXHR, exception);
			insertText(msg, resultRegion);
		},
	});
}*/

/*
 *Code Snippet, jqueryXmlFilmTable() --> result = new XMLSerializer().serializeToString(result.documentElement);
 */

/*function jqueryDeferredXmlFilmTable(resultRegion) {
	var data = makeParamString("xml");
	$.ajax({
		url: "Control",
		method: "GET", //default
		data: data,
		dataType: "xml",
	})
	.done(function(result) {
		console.log("jquery deferred XML success");
		jqueryShowXmlFilmInfo(result, resultRegion);
	})
	.fail(function(jqXHR, exception) {
		var msg = determineError(jqXHR, exception);
		insertText(msg, resultRegion);
	})
	.always(function() {
		highlight(resultRegion);
	});
}

function jqueryDeferredFinalXmlFilmTable(resultRegion) {
	xhrGet("Control", "xml", makeParamString("xml"), resultRegion).done(function(result) {
		console.log("jquery deferred final XML success");
		jqueryShowXmlFilmInfo(result, resultRegion);
	});
}

function jqueryShowXmlFilmInfo(result, resultRegion) {
	var films = result.getElementsByTagName("film");
	var rows = new Array();
	for (var i = 0; i < films.length; i++) {
		var film = films[i];
		var subElements =
			["id", "title", "year", "director", "stars", "review"];
		rows[i] = getElementValues(film, subElements);
	}
	console.log(rows);
	var table = getFilmTable(rows);
	insertText(table, resultRegion);
}*/

/*$(function() {
	$("#standard-orig-button").click(standardOrigHandler);
	$("#standard-highlight-1-button").click(standardHighlight1Handler);
	$("#standard-highlight-2-button").click(standardHighlight2Handler);
	$("#standard-temp-message-button").click(standardTempMessageHandler);
	$("#deferred-orig-button").click(deferredOrigHandler);
	$("#deferred-highlight-button").click(deferredHighlightHandler);
	$("#deferred-temp-message-button").click(deferredTempMessageHandler);
	$("#deferred-when-button").click(showTimeAndNumber);
	$("#deferred-custom-button").click(showNumbers);
});

function standardOrigHandler() {
  insertAjaxResult("show-time.jsp",
				   "#standard-orig-result");
}

function insertAjaxResult(address, resultRegion) {
  $.ajax({
	  url: address,
	  success:
		function(text) {
		  insertText(text, resultRegion);
		}
  });
}

function insertText(text, resultRegion) {
  $(resultRegion).html(text);
}

function standardHighlight1Handler() {
  insertAjaxResult2("show-time.jsp",
					"#standard-highlight-1-result");
}

function insertAjaxResult2(address, resultRegion) {
  $.ajax({
	  url: address,
	  success:
		function(text) {
		  insertText2(text, resultRegion);
		}
  });
}

function insertText2(text, resultRegion) {
  $(resultRegion).html(text);
  highlight(resultRegion);
}

function standardHighlight2Handler() {
  insertAjaxResult3("show-time.jsp",
					"#standard-highlight-2-result");
}

function highlight(selector) {
  $(selector).hide().show("highlight");
}

function insertAjaxResult3(address, resultRegion) {
  $.ajax({
	  url: address,
	  success:
		function(text) {
		  insertText(text, resultRegion);
		  highlight(resultRegion);
		}
  });
}

function standardTempMessageHandler() {
  var address = "show-time-slow.jsp";
  var resultRegion = "#standard-temp-message-result";
  var workingRegion = "#standard-temp-message-working";
  $(resultRegion).html(""); // Erase any previous results
  $(workingRegion).show();
  $.ajax({
	  url: address,
	  success: function(text) {
		insertText(text, resultRegion);
		highlight(resultRegion);
	  },
	  complete: function(text) { $(workingRegion).hide(); }
  });
}

function deferredOrigHandler() {
  var address = "show-time.jsp";
  var resultRegion = "#deferred-orig-result";
  var req = $.ajax({ url: address });
  req.done(function(text) { insertText(text, resultRegion); });
}

function deferredHighlightHandler() {
  var address = "show-time.jsp";
  var resultRegion = "#deferred-highlight-result";
  var req = $.ajax({ url: address });
  req.done(function(text) { insertText(text, resultRegion); });
  req.done(function(text) { highlight(resultRegion); });
}

function deferredTempMessageHandler() {
  var address = "show-time-slow.jsp";
  var resultRegion = "#deferred-temp-message-result";
  var workingRegion = "#deferred-temp-message-working";
  $(resultRegion).html(""); // Erase any previous results
  $(workingRegion).show();
  var req = $.ajax({ url: address });
  req.done(function(text) { insertText(text, resultRegion); });
  req.done(function(text) { highlight(resultRegion); });
  req.complete(function(text) { $(workingRegion).hide(); });
}

function showTimeAndNumber() {
  var address1 = "show-time-slow.jsp";
  var resultRegion1 = "#deferred-when-time-result";
  var address2 = "show-number-slow.jsp";
  var resultRegion2 = "#deferred-when-number-result";
  var workingRegion = "#deferred-when-working";
  $(resultRegion1).html("");
  $(resultRegion2).html("");
  $(workingRegion).show();
  var req1 = $.ajax({ url: address1 });
  req1.done(function(text) { insertText(text, resultRegion1); });
  req1.done(function(text) { highlight(resultRegion1); });
  var req2 = $.ajax({ url: address2 });
  req2.done(function(text) { insertText(text, resultRegion2); });
  req2.done(function(text) { highlight(resultRegion2); });
  $.when(req1, req2).always(function(text) { $(workingRegion).hide(); });
}

function showNum(resultRegion, millis) {
  var dfd = $.Deferred();
  $(resultRegion).html("Number: " + Math.random());
  $(resultRegion).fadeIn(millis, dfd.resolve);
  return(dfd.promise());
}

function showNumbers() {
  var workingRegion = "#deferred-custom-working";
  var resultRegion1 = "#deferred-custom-number-result-1";
  var resultRegion2 = "#deferred-custom-number-result-2";
  $(resultRegion1).hide();
  $(resultRegion2).hide();
  $(workingRegion).show();
  $.when(showNum(resultRegion1, 6000), showNum(resultRegion2, 3000))
	  .done(function(text) { $(workingRegion).hide(); });
}*/

/*<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="content-type"
	content="application/xhtml+xml; charset=UTF-8" />
<script src="./scripts/ajax-utils.js" type="text/javascript"></script>
<script src="./scripts/ajax-data-tables.js" type="text/javascript"></script>
<script src="./scripts/ajax-param-string.js" type="text/javascript"></script>
<script src="./scripts/jquery.js" type="text/javascript"></script>
<script src="./scripts/jquery-data-tables.js" type="text/javascript"></script>
<title>Hello App Engine</title>
</head>

<body>
	<h1>Hello App Engine!</h1>
	<table>
		<tr>
			<td colspan="2" style="font-weight: bold;">Available Servlets:</td>
		</tr>
		<tr>
			<td><a href='/hello'>A Servlet</a></td>
		</tr>
		<tr>
			<td><a href='/Control'>The Controller Servlet</a></td>
		</tr>
	</table>
	<br/>
	<br/>
	<div align="center">
		<fieldset>
			<legend>Display Film Data: JSON</legend>
			<form action="#">
				<input type="button" value="Show Film Table"
					onclick='jsonFilmTable("json-film-table")' />
			</form>
			<div id="json-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery) Display Film Data: JSON</legend>
			<input type="button" value="Show Film Table"
				id="jquery-json-film-button" />
			<div id="jquery-json-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery - Deferred) Display Film Data: JSON</legend>
			<input type="button" value="Show Film Table"
				id="jquery-deferred-json-film-button" />
			<div id="jquery-deferred-json-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery - Deferred - Final) Display Film Data: JSON</legend>
			<input type="button" value="Show Film Table"
				id="jquery-deferred-final-json-film-button" />
			<div id="jquery-deferred-final-json-film-table"></div>
		</fieldset>
		<br/>
		<br/>
		<fieldset>
			<legend>Display Film Data: XML</legend>
			<form action="#">
				<input type="button" value="Show Film Table"
					onclick='xmlFilmTable("xml-film-table")' />
			</form>
			<div id="xml-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery) Display Film Data: XML</legend>
			<input type="button" value="Show Film Table"
				id="jquery-xml-film-button" />
			<div id="jquery-xml-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery - Deferred) Display Film Data: XML</legend>
			<input type="button" value="Show Film Table"
				id="jquery-deferred-xml-film-button" />
			<div id="jquery-deferred-xml-film-table"></div>
		</fieldset>
		<fieldset>
			<legend>(jQuery - Deferred - Final) Display Film Data: XML</legend>
			<input type="button" value="Show Film Table"
				id="jquery-deferred-final-xml-film-button" />
			<div id="jquery-deferred-final-xml-film-table"></div>
		</fieldset>
	</div>
</body>
</html>*/

/*package com.epmmu; HelloAppEngine.java

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
	name = "HelloAppEngine",
	urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws IOException {

	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");

	response.getWriter().print("Hello App Engine!\r\n");

  }
}*/

/*package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Film;
import model.FilmDAO;
import model.Films;

@WebServlet(name = "control", urlPatterns = { "/control" })
public class control extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public control() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		FilmDAO filmDAO = new FilmDAO();
		ArrayList<Film> filmList = new ArrayList<Film>();
		filmList.addAll(filmDAO.getAllFilms());

		String format = request.getParameter("format");
		String outputPage = null;
		if (format.equals("json")) {
			response.setContentType("application/json");
			outputPage = "films-json.jsp";
			gsonListToJSON(filmList, outputPage);
		} else if (format.equals("xml")) {
			try {
				response.setContentType("text/xml");
				outputPage = "films-xml.jsp";
				jaxbListToXML(filmList, outputPage);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Type JSON or XML not specified.");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(outputPage);
		dispatcher.include(request, response);
	}*/

/*
 * Code Snippet, doGet() --> request.setAttribute("films", filmList);
 * response.setContentType("text/plain"); outputPage = "films-string.jsp";
 * listToString(filmList, outputPage);
 */

/*protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	doGet(request, response);
}

private static void gsonListToJSON(ArrayList<Film> filmList, String outputPage) throws IOException {
	try (FileWriter fw = new FileWriter(outputPage)) {
		Gson gson = new GsonBuilder().create();
		gson.toJson(filmList, fw);
		fw.close();
	}
}*/

/*
 * Code Snippet, gsonListToJSON() --> Gson gson = new Gson();
 * gson.toJson(filmList, new FileWriter(outputPage));
 */

/*private static void jaxbListToXML(ArrayList<Film> filmList, String outputPage) throws IOException, JAXBException {
	try (FileWriter fw = new FileWriter(outputPage)) {
		Films films = new Films();
		films.setFilmList(filmList);
		JAXBContext context = JAXBContext.newInstance(Films.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(films, fw);
		fw.close();
	}
}*/

/*
 * Code Snippet, jaxbListToXML() --> StringWriter sw = new StringWriter();
 * m.marshal(filmList, sw); sw.close();
 */

/*import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.epmmu.HelloAppEngine;

public class HelloAppEngineTest {

  @Test
  public void test() throws IOException {
	MockHttpServletResponse response = new MockHttpServletResponse();
	new HelloAppEngine().doGet(null, response);
	Assert.assertEquals("text/plain", response.getContentType());
	Assert.assertEquals("UTF-8", response.getCharacterEncoding());
	Assert.assertEquals("Hello App Engine!\r\n", response.getWriterContent().toString());
  }
}*/

/*import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * This mock class is created to enable basic unit testing of the
 * {@link HelloAppEngine} class. Only methods used in the unit test
 * have a non-trivial implementation.
 *
 * Feel free to change this class or replace it using other ways for testing
 * {@link HttpServlet}s, e.g. Spring MVC Test or Mockito to suit your needs.
 */

/*class MockHttpServletResponse implements HttpServletResponse {

  private String contentType;
  private String encoding;
  private StringWriter writerContent = new StringWriter();
  private PrintWriter writer = new PrintWriter(writerContent);

  @Override
  public void setContentType(String contentType) {
	this.contentType = contentType;
  }

  @Override
  public String getContentType() {
	return contentType;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
	return writer;
  }

  public StringWriter getWriterContent() {
	return writerContent;
  }

  // anything below is the default generated implementation

  @Override
  public void flushBuffer() throws IOException {
  }

  @Override
  public int getBufferSize() {
	return 0;
  }

  @Override
  public String getCharacterEncoding() {
	return encoding;
  }

  @Override
  public Locale getLocale() {
	return null;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
	return null;
  }

  @Override
  public boolean isCommitted() {
	return false;
  }

  @Override
  public void reset() {
  }

  @Override
  public void resetBuffer() {
  }

  @Override
  public void setBufferSize(int size) {
  }

  @Override
  public void setCharacterEncoding(String encoding) {
	this.encoding = encoding;
  }

  @Override
  public void setContentLength(int length) {
  }

  @Override
  public void setLocale(Locale locale) {
  }

  @Override
  public void addCookie(Cookie cookie) {
  }

  @Override
  public void addDateHeader(String name, long date) {
  }

  @Override
  public void addHeader(String name, String value) {
  }

  @Override
  public void addIntHeader(String name, int value) {
  }

  @Override
  public boolean containsHeader(String name) {
	return false;
  }

  @Override
  public String encodeRedirectURL(String url) {
	return null;
  }

  @Deprecated
  @Override
  public String encodeRedirectUrl(String url) {
	return null;
  }

  @Override
  public String encodeURL(String url) {
	return null;
  }

  @Deprecated
  @Override
  public String encodeUrl(String url) {
	return null;
  }

  @Override
  public void sendError(int statusCode) throws IOExsception {
  }

  @Override
  public void sendError(int statusCode, String message) throws IOException {
  }

  @Override
  public void sendRedirect(String url) throws IOException {
  }

  @Override
  public void setDateHeader(String name, long date) {
  }

  @Override
  public void setHeader(String name, String value) {
  }

  @Override
  public void setIntHeader(String name, int value) {
  }

  @Override
  public void setStatus(int statusCode) {
  }

  @Deprecated
  @Override
  public void setStatus(int statusCode, String message) {
  }

  // Servlet API 3.0 and 3.1 methods
  @Override
  public void setContentLengthLong(long length) {
  }

  @Override
  public int getStatus() {
	return 0;
  }

  @Override
  public String getHeader(String name) {
	return null;
  }

  @Override
  public Collection<String> getHeaders(String name) {
	return null;
  }

  @Override
  public Collection<String> getHeaderNames() {
	return null;
  }
}*/

//Set<String> paramNames = request.getParameterMap().keySet();

//		for (String name : paramNames) {
//			String value = request.getParameter(name);
//			if (value == null) {
//				String errMsg = "Update film: " + name + " is undefined.\n";
//				out.println("<h1>Error encountered:</h1>");
//				out.println("<h2>" + errMsg + "</2>");
//				System.out.println(errMsg);
//			}
//		}

/*
 * Code Snippet, filmListHandler() --> request.setAttribute("films", filmList);
 * response.setContentType("text/plain"); outputPage = "films-string.jsp";
 * listToString(filmList, outputPage);
 */

/*
* Code Snippet, gsonObjectToJSON() --> Gson gson = new Gson();
* gson.toJson(filmList, new FileWriter(outputPage));
*/

/*
 * Code Snippet, jaxbObjectToXML() --> StringWriter sw = new StringWriter();
 * m.marshal(filmList, sw); sw.close();
 */

/*
* Code Snippet, filmListHandler() --> request.setAttribute("films", filmList);
* response.setContentType("text/plain"); outputPage = "films-string.jsp";
* listToString(filmList, outputPage);
*/

/*
* Code Snippet, gsonListToJSON() --> Gson gson = new Gson();
* gson.toJson(filmList, new FileWriter(outputPage));
*/

/*
* Code Snippet, jaxbListToXML() --> StringWriter sw = new StringWriter();
* m.marshal(filmList, sw); sw.close();
*/

/*
 * Code Snippet, gsonListToJSON() --> Gson gson = new Gson();
 * gson.toJson(filmList, new FileWriter(outputPage));
 */

 //	public boolean checkForDuplicateFilm(Film film) {
//		ArrayList<Film> allFilms = getAllFilms();
//		if (allFilms.contains(film)) {
//			return true;
//		} else {
//			return false;
//		}
//	}

// XmLElementWrapper generates a wrapper element around XML representation
//	@XmlElementWrapper(name = "filmList")