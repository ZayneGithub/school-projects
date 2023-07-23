//jQuery version of document.ready function. 
//Executed when the DOM elements are ready to be used.
//Mostly button click functions e.g. ("#get-all-films-button").click
//or creating JQuery UI widgets e.g. Accordion/Tooltip. 
//Mostly self explanatory, messy code.
$(function() {
	getAllFilmTitles(); //Used in the search bar for easier film selection.
	$(".small-button, #search-type, #get-format").tooltip();
	$("#get-all-films-button").click(getAllFilmsClickHandler);
	$("#search-button").click(getFilmClickHandler);
	$("#insert-film-button").click(function() { $("#insert-popup-wrapper").show(); });
	$("#insert-form").on("submit", function(event) {
		event.preventDefault();
		insertFilmClickHandler();
	});
	$("#insert-cancel").click(function() {
		resetForm("#insert-form"); //A utils.js function for resetting.
		$("#insert-popup-wrapper").hide();
	});
	$("#accordion").on("click", "#update-film-button", function() {
		var accordionClass = $(this).attr("class").split(" ")[1];
		setupAccordionUpdateForm(accordionClass);
	})
	$('#table').on("dblclick", "tbody tr", function() {
		var rowData = $("#table").DataTable().row(this).data();
		setupTableUpdateForm(rowData);
	})
	$("#update-form").on("submit", function(event) {
		event.preventDefault();
		updateFilmClickHandler();
	});
	$("#update-delete").click(function() {
		deleteFilmClickHandler();
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
	})
	$("#update-cancel").click(function() {
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
	});
	$(".form-buttons button").button()
	$("#table-wrapper").hide();
	$("#toggler").click(function() { //The Accordion/Table toggle button. Switches display modes.
		if (!$("#toggler").is(".table")) {
			$("#toggler").addClass("table");
			$("#table-wrapper").show();
			$("#accordion").hide();
			console.log("displaying by table");
		} else if ($("#toggler").is(".table")) {
			$("#toggler").removeClass("table");
			$("#table-wrapper").hide();
			//$("#accordion").show();
			console.log("displaying by accordion");
		}
	});
});

//Executed upon clicking the get all films button.
function getAllFilmsClickHandler() {
	var format = $("#get-format").val().toLowerCase();
	if (confirmFormat(format)) { //utils.js, ensures format is xml, json or text.
		getAllFilms(format);
	} else {
		highlight("#get-format");
	}
}

//Executed upon clicking the search button.
//Depending on the search type, different functions are called, i.e. if TITLE
//is selected, getFilmsByTitle(), if ID is selected, getFilmById().
function getFilmClickHandler() {
	var searchVal = $("#search-tagged").val();
	var searchType = $("#search-type").val().toLowerCase();
	var format = $("#get-format").val().toLowerCase();
	if (confirmFormat(format)) {
		if (!searchVal == "") {
			if (searchType == "title") {
				getFilmsByTitle(searchVal, format);
			}
			else if (searchType == "id") {
				//A utils.js function that confirms the value is a number. Prevents errors.
				if (isNumber(searchVal)) {
					getFilmById(searchVal, format);
				} else {
					console.log("search value is not a valid id")
					highlight("#search-type");
				}
			}
		} else {
			console.log("search value is empty");
			highlight("#search-tagged")
		}
	} else {
		highlight("#get-format");
	}
}

//Executed upon clicking the insert film button.
//Resets and hides the insert form upon completion.
function insertFilmClickHandler() {
	var data = $("#insert-form").serialize();
	//alert(data); 
	//return false;
	var format = $('#insert-format').val().toLowerCase();
	if (confirmFormat(format)) {
		insertFilm(data, format);
		alert("Film successfully inserted.\nValues used:\n" + data);
		resetForm("#insert-form");
		$("#insert-popup-wrapper").hide();
	} else {
		highlight("#insert-format"); s
	}
}

//Executed upon clicking the update (submit) button within the update form.
//Resets and hides the update form upon completion.
function updateFilmClickHandler() {
	var data = $("#update-form").serialize();
	//alert(data); 
	//return false;
	var format = $('#update-format').val().toLowerCase();
	if (confirmFormat(format)) {
		updateFilm(data, format);
		alert("Film successfully updated.\nValues used:\n" + data);
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
	} else {
		highlight("#update-format"); s
	}
}

//Executed upon clicking the delete button within the update form.
//Clears the accordion and datatable upon deletion, ready for next operation.
//Resets and hides the update form upon completion.
function deleteFilmClickHandler() {
	var id = $("#update-form").find('input[name="id"]').val();
	//alert(data); 
	//return false;
	var format = $('#update-format').val().toLowerCase();
	if (confirmFormat(format)) {
		deleteFilm(id, format);
		alert("Film successfully deleted.\nValues used:\n" + id + ", " + format);
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
		$("#accordion").html("");
		$("#accordion").hide();
		$("#table").DataTable().clear();
		$("#table").hide();
		$(".subtitle").hide();
	} else {
		highlight("#update-format"); s
	}
}

//Executed upon clicking the dynamically generated update button (see appendAccordion in utils.js).
function setupAccordionUpdateForm(accordionClass) {
	//console.log(accordionClass);
	var id = $(`#${accordionClass}-id`).text().trim();
	id = parseInt(id);
	var title = $(`#${accordionClass}-title`).text().trim();
	var year = $(`#${accordionClass}-year`).text().trim();
	var director = $(`#${accordionClass}-director`).text().trim();
	var stars = $(`#${accordionClass}-stars`).text().trim();
	var review = $(`#${accordionClass}-review`).text().trim();
	insertValuesIntoForm(id, title, year, director, stars, review);
}

//Executed upon double clicking a datatable row (see first ready function).
//Inserts the values using the insertValuesIntoForm utils.js function.
function setupTableUpdateForm(rowData) {
	var id = rowData[0];
	id = parseInt(id);
	var title = rowData[1];
	var year = rowData[2];
	var director = rowData[3];
	var stars = rowData[4];
	var review = rowData[5];
	insertValuesIntoForm(id, title, year, director, stars, review);
}

//Ajax function, separated from the xhr() function due to uncommon functionality.
//Called at the start of the program under the first ready function.
//Inserts all of the film titles into an array in utils.js. This list is used by
//the jQuert UI search bar (called search-tagged) to help users search for films.
function getAllFilmTitles() {
	$.ajax({
		url: "get-all-film-titles",
		method: "GET",
		data: makeFormatParamString("json"),
		dataType: "json",
	})
		.fail(function(jqXHR, exception) {
			//Determines which error is returned when the Ajax function fails.
			//The error message is inserted into the accordion/table. Check in utils.js.
			console.log(determineError(jqXHR, exception));
			filmTitleList.push("get-all-film titles via json unsuccessful ");
		})
		.done(function(result) {
			console.log("get-all-film-titles via json successful");
			for (var i = 0; i < result.length; i++) {
				filmTitleList.push(result[i]);
			}
		});
}

//Core CRUD functionality functions. All utilize the xhr function which is a utils.js function
//that makes calls to the servlet provided in the first parameter and performs both get and post 
//Ajax calls depending on the second parameter. When done, the function logs the outcome and uses
//the splitFilmArray utils.js function to identify Film objects to then insert into the accordion/table.
function getAllFilms(format) {
	xhr("get-all-films", "GET", makeFormatParamString(format), format).done(function(result) {
		console.log(`get-all-films via ${format} successful`);
		splitFilmArray(format, result);
	});
}

function getFilmsByTitle(title, format) {
	xhr("get-films-by-title", "GET", makeTitleParamString(title, format), format).done(function(result) {
		//When GetFilmByTitle.java receives a null Film object from HibernateUtils.java, PrintWriter writes plain
		//text stating that "No matches found." As getting films by text expects a text response, it does not
		//trigger ajax.fail, therefore it must be handled here.
		if (result.toString().includes("No matches found.") == true) {
			console.log(`get-films-by-title via ${format} unsuccessful.`);
			var msg = "Requested parse failed.";
			insertText(msg, "#accordion");
			insertText(msg, "#table");
		} else {
			console.log(`get-films-by-title via ${format} successful`);
			splitFilmArray(format, result);
		}
	});
}

function getFilmById(id, format) {
	xhr("get-film-by-id", "GET", makeIdParamString(id, format), format).done(function(result) {
		//When GetFilmByID.java receives a null Film object from HibernateUtils.java, PrintWriter writes plain
		//text stating that "No matches found." As getting films by text expects a text response, it does not
		//trigger ajax.fail, therefore it must be handled here.
		if (result.toString().includes("No matches found.") == true) {
			console.log(`get-film-by-id via ${format} unsuccessful.`);
			var msg = "Requested parse failed.";
			insertText(msg, "#accordion");
			insertText(msg, "#table");
		} else {
			console.log(`get-film-by-id via ${format} successful`);
			splitFilmArray(format, result);
		}
	});
}

function insertFilm(data, format) {
	xhr("insert-film", "POST", data, format).done(function(result) {
		console.log(`insert-film via ${format} successful`);
		splitFilmArray(format, result);
	});
}

function updateFilm(data, format) {
	xhr("update-film", "POST", data, format).done(function(result) {
		console.log(`update-film via ${format} successful`);
		splitFilmArray(format, result);
	});
}

function deleteFilm(id, format) {
	xhr("delete-film", "POST", makeIdParamString(id, format), format).done(function() {
		console.log(`delete-film via ${format} successful`);
		//Does not splitFilmArray as there is no Film result returned.
	});
}