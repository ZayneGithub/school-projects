$(function () {
	if (testDbConnection() == true) {
		getAllFilmTitles();
	}
	$(".small-button, #search-type").tooltip();
	$("#get-all-films-button").click(getAllFilmsClickHandler);
	$("#search-button").click(getFilmClickHandler);
	$("#insert-film-button").click(function () { $("#insert-popup-wrapper").show(); });
	$("#insert-form").on("submit", function (event) {
		event.preventDefault();
		insertFilmClickHandler();
	});
	$("#insert-cancel").click(function () {
		resetForm("#insert-form");
		$("#insert-popup-wrapper").hide();
	});
	$("#table").DataTable();
	$('#table').on("dblclick", "tbody tr", function () {
		if ($("#table").DataTable().data().any()) {
			var rowData = $("#table").DataTable().row(this).data();
			setupTableUpdateForm(rowData);
		}
	})
	$("#update-form").on("submit", function (event) {
		event.preventDefault();
		updateFilmClickHandler();
	});
	$("#update-delete").click(function () {
		deleteFilmClickHandler();
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
	})
	$("#update-cancel").click(function () {
		resetForm("#update-form");
		$("#update-popup-wrapper").hide();
	});
	$(".form-buttons button").button()
});

function getAllFilmsClickHandler() {
	getAllFilms();
}

function getFilmClickHandler() {
	resetTable();
	var searchVal = $("#search-tagged").val();
	var searchType = $("#search-type").val().toLowerCase();
	if (!searchVal == "") {
		if (searchType == "title") {
			var condition = $("#title-condition").val().toLowerCase();
			if (condition == "exact") {
				getFilmByTitle(searchVal);
			} else {
				getFilmsLikeTitle(searchVal);
			}
		}
		else if (searchType == "id") {
			if (isNumber(searchVal)) {
				getFilmById(searchVal);
			} else {
				console.log("search value is not a valid id")
				highlight("#search-type");
			}
		}
	} else {
		console.log("search value is empty");
		highlight("#search-tagged")
	}
}

function insertFilmClickHandler() {
	var data = $("#insert-form").serializeArray();
	insertFilm(data);
	resetForm("#insert-form");
	$("#insert-popup-wrapper").hide();
}

function updateFilmClickHandler() {
	var data = $("#update-form").serializeArray();
	var id = $('input[name="id"]').val();
	updateFilm(data, id);
	resetForm("#update-form");
	$("#update-popup-wrapper").hide();

}

function deleteFilmClickHandler() {
	var data = $("#update-form").serializeArray();
	deleteFilm(data);
	resetForm("#update-form");
	$("#update-popup-wrapper").hide();
}

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

function getFilmById(id) {
	xhr("./scripts/php/get-film-by-id.php", "GET", makeIdParamString(id)).done(function (result) {
		console.log("get-film-by-id successful");
		result = JSON.parse(result);
		if (result.length > 0) {
			splitFilmArray(result);
		} else {
			console.log(`no films matching the id ${id} were found`);
		}
	});
}

function getFilmByTitle(title) {
	xhr("./scripts/php/get-film-by-title.php", "GET", makeTitleParamString(title)).done(function (result) {
		console.log("get-film-by-title successful");
		result = JSON.parse(result);
		if (result.length > 0) {
			splitFilmArray(result);
		} else {
			console.log(`no films matching the title ${title} were found`);
		}
	});
}

function getFilmsLikeTitle(title) {
	xhr("./scripts/php/get-films-like-title.php", "GET", makeTitleParamString(title)).done(function (result) {
		console.log("get-films-like-title successful");
		result = JSON.parse(result);
		if (result.length > 0) {
			splitFilmArray(result);
		} else {
			console.log(`no films like the title ${title} were found`);
		}
	});
}

function getAllFilms() {
	xhr("./scripts/php/get-all-films.php", "GET").done(function (result) {
		console.log("get-all-films successful");
		result = JSON.parse(result);
		splitFilmArray(result);
	});
}

function getAllFilmTitles() {
	filmTitleList = [];
	$.ajax({
		url: "./scripts/php/get-all-film-titles.php",
		method: "GET",
	})
		.done(function (result) {
			console.log("get-all-film-titles successful");
			result = JSON.parse(result);
			for (var i = 0; i < result.length; i++) {
				filmTitleList.push(result[i].title);
			}
		})
		.fail(function (jqXHR, exception) {
			console.log("get-all-film-titles unsuccessful");
			console.log(determineError(jqXHR, exception));
		});
}

function getLastFilm() {
	xhr("./scripts/php/get-last-film.php", "GET").done(function (result) {
		console.log("get-last-film successful");
		result = JSON.parse(result);
		splitFilmArray(result);
	});
}

function insertFilm(data) {
	xhr("./scripts/php/insert-film.php", "POST", data).done(function (result) {
		console.log("insert-film successful");
		getLastFilm();
		getAllFilmTitles();
	});
}

function updateFilm(data, id) {
	xhr("./scripts/php/update-film.php", "POST", data).done(function (result) {
		console.log("update-film successful");
		getFilmById(id);
		getAllFilmTitles();
	});
}

function deleteFilm(id) {
	xhr("./scripts/php/delete-film.php", "POST", id).done(function (result) {
		console.log("delete-film successful");
		resetTable();
		getAllFilmTitles();
	});
}

function testDbConnection() {
	var connected = false;
	$.ajax({
		url: "./scripts/php/test-db-connection.php",
		method: "GET",
		async: false,
	})
		.done(function (result) {
			if (result == "true") {
				console.log("test-db-connection successful");
				connected = true;
			} else {
				console.log("test-db-connection unsuccessful, likely invalid database parameters");
				connected = false;
			}
		})
		.fail(function (jqXHR, exception) {
			console.log("test-db-connection unsuccessful");
			console.log(determineError(jqXHR, exception));
			connected = false;
		});
	return connected;
}