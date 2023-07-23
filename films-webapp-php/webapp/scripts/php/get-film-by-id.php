<?php

require("film-controller.php");

$id = $_GET['id'];
$film_controller = new FilmController();
$film = $film_controller->get_film_by_id($id);

echo json_encode($film);

?>