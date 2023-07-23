<?php

require("film-controller.php");

$id = $_POST['id'];
$film_controller = new FilmController();
$film = $film_controller->delete_film($id);

echo json_encode($film);