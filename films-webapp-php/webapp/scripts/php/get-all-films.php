<?php

require("film-controller.php");

$film_controller = new FilmController();
$film_list = $film_controller->get_all_films();

echo json_encode($film_list);

?>