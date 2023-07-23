//Used by the getFilmClickHandler() films.js function to confirm whether
//the ID value is or is not a number, preventing non-numeric ID queries.
function isNumber(n) {
	if (n > 99999) {
		return null;
	}
	return !isNaN(parseFloat(n)) && !isNaN(n - 0)
}

//Used by most click handler functions in films.js to validate the supplied format.
function confirmFormat(format) {
	if (format == "text" || format == "xml" || format == "json") {
		return true;
	} else {
		return false;
	}
}

//Used to encode the format entry.
function makeFormatParamString(format) {
	var paramString =
		"format=" + format;
	return (paramString);
}

//Used to encode the title and format entry.
function makeTitleParamString(title, format) {
	var paramString = `title=${title}&format=${format}`;
	return (paramString);
}

//Used to encode the ID and format entry.
function makeIdParamString(id, format) {
	var paramString = `id=${id}&format=${format}`;
	return (paramString);
}

//Used to encode the film property and format entry.
function makeInsertParamString(title, year, director, stars, review, format) {
	var paramString = `title=${title}&year=${year}&director=${director}&stars=${stars}&review=${review}&format=${format}`;
	return (paramString);
}

//Used to REPLACE html within the resultRegion with the text supplied.
//html() function not append() function. Used to insert error messages.
function insertText(text, resultRegion) {
	$(resultRegion).html(`<p id="inserted-text">${text}</p>`);
}

//Used e.g. by the getFilmClickHandler() films.js function if the search value
//is empty or if the ID is not a number.
function highlight(selector) {
	$(selector).hide().show("highlight");
}

//Used by both update form setup functions to insert the values into the form, ready for updating.
//Also shows the update form.
function insertValuesIntoForm(id, title, year, director, stars, review) {
	$("#update-form").find('input[name="id"]').val(id);
	$("#update-form").find('input[name="title"]').val(title);
	$("#update-form").find('input[name="year"]').val(year);
	$("#update-form").find('input[name="director"]').val(director);
	$("#update-form").find('input[name="stars"]').val(stars);
	$("#update-form").find('input[name="review"]').val(review);
	$("#update-popup-wrapper").show();
}

//Used to reset the insert form whenever it is cancelled/closed,
//e.g. when the deleteFilmClickHandler() is called.
function resetForm(selector) {
	$(`${selector}`).find('input[name="title"]').val("");
	$(`${selector}`).find('input[name="year"]').val("");
	$(`${selector}`).find('input[name="director"]').val("");
	$(`${selector}`).find('input[name="stars"]').val("");
	$(`${selector}`).find('input[name="review"]').val("");
	$(`${selector} select`).val("JSON");
}

//Disables pointer events and buttons whenever a xhr or ajax function is in process.
//Also shows/hides the waiting <p> when the process starts/ends.
function wait(handler) {
	if (handler === "enable") {
		$("body").css("pointer-events", "none");
		$(".small-button").prop("disabled", true);
		$("#waiting").show();
		$(".subtitle").show();
	} else {
		$("body").css("pointer-events", "auto");
		$(".small-button").prop("disabled", false);
		$("#waiting").hide();
	}
}

//Used by most of the get/post ajax calls to most of the servlets.
//Upon call, the wait function is called to disable buttons, etc.
//Upon failing, the determineError function is called and the error
//message is inserted into the display region (accordion/table).
//The wait function is then passed disable to reenable the buttons.
function xhr(url, method, data, dataType) {
	wait("enable");
	return $.ajax({
		url: url,
		method: method,
		data: data,
		dataType: dataType,
	})
		.always(function() {
			wait("disable");
		})
		.fail(function(jqXHR, exception) {
			console.log(url + " via " + dataType + " unsuccessful.")
			var msg = determineError(jqXHR, exception);
			insertText(msg, "#accordion");
			insertText(msg, "#table");
		});
}

//Returns an error message depending on the type of error returned by the
//xhr.fail function.
function determineError(jqXHR, exception) {
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
	//Internet resource.
}

// Given an element object and an array of sub-element names,
// returns an array of the values of the sub-elements.
// E.g., for <foo><a>one</a><c>two</c><b>three</b></foo>,
// if the element points at foo,
// getElementValues(element, ["a", "b", "c"]) would return
// ["one", "three", "two"]

function getElementValues(element, subElementNames) {
	var values = new Array(subElementNames.length);
	for (var i = 0; i < subElementNames.length; i++) {
		var name = subElementNames[i];
		var subElement = element.getElementsByTagName(name)[0];
		values[i] = getBodyContent(subElement);
	}
	return (values);
}

// Given an element, returns the body content.
function getBodyContent(element) {
	element.normalize();
	return (element.childNodes[0].nodeValue);
}

//Used by the xhr function calls e.g. getAllFilms() in films.js. Depending on the
//format passed, the function will split the result in different ways, i.e.
//if format = text, the result is split using the characters used to encode the
//string in the FilmToJSP.java and FilmListToJSP.java classes: "%d|%s|%d|%s|%s|%s@".
//This split data is pushed into the dataSet array, used by the appendAccordion and
//appendDataTable functions to insert the data film by film.
function splitFilmArray(format, result) {
	var dataSet = [];
	if (format == "text") {
		var films = result.split("@");
		for (var i = 0; i < films.length - 1; i++) { //-1 because of the way the string is encoded.
			var filmInfo = films[i].split("|");
			var id = filmInfo[0];
			var title = filmInfo[1];
			var year = filmInfo[2];
			var director = filmInfo[3];
			var stars = filmInfo[4];
			var review = filmInfo[5];
			var data = [id, title, year, director, stars, review];
			dataSet.push(data);
		}
	} else if (format == "xml") {
		var films = result.getElementsByTagName("film");
		var filmInfo = new Array();
		for (var i = 0; i < films.length; i++) {
			var film = films[i];
			var subElements =
				["id", "title", "year", "director", "stars", "review"];
			filmInfo[i] = getElementValues(film, subElements);
			var id = filmInfo[i][0];
			var title = filmInfo[i][1];
			var year = filmInfo[i][2];
			var director = filmInfo[i][3];
			var stars = filmInfo[i][4];
			var review = filmInfo[i][5];
			var data = [id, title, year, director, stars, review];
			dataSet.push(data);
		}
	} else { //default
		if (result.length == undefined) { //for if there's only one film, only for json.
			film = result;
			var id = film.id;
			var title = film.title;
			var year = film.year;
			var director = film.director;
			var stars = film.stars;
			var review = film.review;
			var data = [id, title, year, director, stars, review];
			dataSet.push(data);
		} else {
			for (var i = 0; i < result.length; i++) {
				film = result[i];
				var id = film.id;
				var title = film.title;
				var year = film.year;
				var director = film.director;
				var stars = film.stars;
				var review = film.review;
				var data = [id, title, year, director, stars, review];
				dataSet.push(data);
			}
		}
	}
	appendAccordion(dataSet);
	appendTable(dataSet);
}

//For each set of data within dataSet i.e. for each Film with dataSet, this function removes
//the previous accordion data, and appends i.e. adds on to the accordion with that Film's unique data.
function appendAccordion(dataSet) {
	$("#accordion").html(""); //Clear the previous accordion entries.
	for (var i = 0; i < dataSet.length; i++) {
		$("#accordion").append(`<h3 id="accordion${i}-title">${dataSet[i][1]}
									<button id="update-film-button" class="small-button accordion${i}"
											title="Update film">
											<i class="fa fa-wrench"></i>
									</button>
								</h3>
								<div>
									<p>ID: <span id="accordion${i}-id">${dataSet[i][0]}</span></p>
									<p>YEAR: <span id="accordion${i}-year">${dataSet[i][2]}</span></p>
									<p>DIRECTOR: <span id="accordion${i}-director">${dataSet[i][3]}</span></p>
									<p>STARS:<br/><span id="accordion${i}-stars">${dataSet[i][4]}</span></p>
									<p>REVIEW:<br/><span id="accordion${i}-review">${dataSet[i][5]}</span></p>
								</div>`);
	}
	//Process any headers and panels that were added or removed directly in the DOM and recompute the height of the accordion panels.
	$("#accordion").accordion("refresh");
	$("#accordion").accordion({ active: 0 }); //Opens the first accordion panel, collapses the rest.
}

//For each set of data within dataSet i.e. for each Film with dataSet, this function clears, destroys
//and then recreates the DataTable with the new data set.
function appendTable(dataSet) {
	$("#inserted-text").remove(); //Completely reset the previous datatable.
	$("#table").DataTable().clear().destroy();
	$("#table").DataTable({
		data: dataSet,
		columns: [
			{ title: "ID" },
			{ title: "TITLE" },
			{ title: "YEAR" },
			{ title: "DIRECTOR" },
			{ title: "STARS" },
			{ title: "REVIEW" }
		]
	})
}

var filmTitleList = []; //Used by the tagged jQuery search bar.
var timer = window.setInterval(intervalChecks, 200); //Every 200ms, repeat the interval checks.

//This function repeatedly checks for changes to the search type. When the search type is changed
//from TITLE to ID, the autocompleted search bar gets reset. This is so that a film title is not
//mistakenly inputted when an ID is requested. It also changes the placeholder to aid user entry.
function intervalChecks() {
	if ($("#search-type").val().toLowerCase() === "id") {
		$("#search-tagged").autocomplete({ source: [] });
		$("#search-tagged").prop("placeholder", "Search (e.g. 10852)");
	} else if ($("#search-type").val().toLowerCase() == "title") {
		$("#search-tagged").autocomplete({ source: filmTitleList });
		$("#search-tagged").prop("placeholder", "Search (e.g. ROCKY III)");
	}
	$(".small-button").tooltip();
}
