<?php

require("film-controller.php");

$title = $_POST['title'];
$year = $_POST['year'];
$director = $_POST['director'];
$stars = $_POST['stars'];
$review = $_POST['review'];
$film_controller = new FilmController();
$film = $film_controller->insert_film($title, $year, $director, $stars, $review);

echo json_encode($film);
