<?php

require("film-controller.php");

$title = $_GET['title'];
$film_controller = new FilmController();
$film = $film_controller->get_film_by_title($title);

echo json_encode($film);

?>