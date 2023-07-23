<?php

require("film-controller.php");

$film_controller = new FilmController();
$film = $film_controller->get_last_film();

echo json_encode($film);

?>