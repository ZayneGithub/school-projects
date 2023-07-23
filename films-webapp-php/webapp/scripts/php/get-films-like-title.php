<?php

require("film-controller.php");

$title = $_GET['title'];
$film_controller = new FilmController();
$film_list = $film_controller->get_films_like_title($title);

echo json_encode($film_list);

?>