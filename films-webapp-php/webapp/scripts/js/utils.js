function isNumber(n) {
	if (n > 99999) {
		return null;
	}
	return !isNaN(parseFloat(n)) && !isNaN(n - 0)
}

function makeTitleParamString(title) {
	var paramString = `title=${title}`;
	return (paramString);
}

function makeIdParamString(id) {
	var paramString = `id=${id}`;
	return (paramString);
}

function makeInsertParamString(title, year, director, stars, review) {
	var paramString = `title=${title}&year=${year}&director=${director}&stars=${stars}&review=${review}`;
	return (paramString);
}

function insertText(text, resultRegion) {
	$(resultRegion).html(`<p id="inserted-text">${text}</p>`);
}

function highlight(selector) {
	$(selector).hide().show("highlight");
}

function insertValuesIntoForm(id, title, year, director, stars, review) {
	$("#update-form").find('input[name="id"]').val(id);
	$("#update-form").find('input[name="title"]').val(title);
	$("#update-form").find('input[name="year"]').val(year);
	$("#update-form").find('input[name="director"]').val(director);
	$("#update-form").find('input[name="stars"]').val(stars);
	$("#update-form").find('input[name="review"]').val(review);
	$("#update-popup-wrapper").show();
}

function resetForm(selector) {
	$(`${selector}`).find('input[name="title"]').val("");
	$(`${selector}`).find('input[name="year"]').val("");
	$(`${selector}`).find('input[name="director"]').val("");
	$(`${selector}`).find('input[name="stars"]').val("");
	$(`${selector}`).find('input[name="review"]').val("");
	$(`${selector} select`).val("JSON");
}

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

function xhr(url, method, data, dataType) {
	wait("enable");
	return $.ajax({
		url: url,
		method: method,
		data: data,
		dataType: dataType,
	})
		.always(function () {
			wait("disable");
		})
		.fail(function (jqXHR, exception) {
			console.log(url + " via " + dataType + " unsuccessful.");
			var msg = determineError(jqXHR, exception);
			console.log(msg);
			insertText(msg, "#table");
		});
}

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
}

function getElementValues(element, subElementNames) {
	var values = new Array(subElementNames.length);
	for (var i = 0; i < subElementNames.length; i++) {
		var name = subElementNames[i];
		var subElement = element.getElementsByTagName(name)[0];
		values[i] = getBodyContent(subElement);
	}
	return (values);
}

function getBodyContent(element) {
	element.normalize();
	return (element.childNodes[0].nodeValue);
}

function splitFilmArray(result) {
	var dataSet = [];
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
	appendTable(dataSet);
}

function appendTable(dataSet) {
	$("#inserted-text").remove();
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

function resetTable() {
	$("#table-wrapper").html(`
		<table id="table" class="display" style="width: 100%;">
		<thead>
			<tr>
				<th>ID</th>
				<th>TITLE</th>
				<th>YEAR</th>
				<th>DIRECTOR</th>
				<th>STARS</th>
				<th>REVIEW</th>
			</tr>
		</thead>
		<tbody></tbody>
		<tfoot>
			<tr>
				<th>ID</th>
				<th>TITLE</th>
				<th>YEAR</th>
				<th>DIRECTOR</th>
				<th>STARS</th>
				<th>REVIEW</th>
			</tr>
		</tfoot>
	</table>
	`);
	$("#table").DataTable();
	$('#table').on("dblclick", "tbody tr", function () {
		if ($("#table").DataTable().data().any()) {
			var rowData = $("#table").DataTable().row(this).data();
			setupTableUpdateForm(rowData);
		}
	})
}

var filmTitleList = [];
var timer = window.setInterval(intervalChecks, 100);

function intervalChecks() {
	if ($("#search-type").val().toLowerCase() === "id") {
		$("#search-tagged").autocomplete({ source: [] });
		$("#search-tagged").prop("placeholder", "Search (e.g. 10852)");
		$("#title-condition").hide();
	} else if ($("#search-type").val().toLowerCase() == "title") {
		$("#search-tagged").autocomplete({ source: filmTitleList });
		$("#search-tagged").prop("placeholder", "Search (e.g. ROCKY III)");
		$("#title-condition").show();
	}
	$(".small-button").tooltip();
}
