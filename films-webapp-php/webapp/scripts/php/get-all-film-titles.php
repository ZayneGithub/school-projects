<?php

require("film-controller.php");

$film_controller = new FilmController();
$film_title_list = $film_controller->get_all_film_titles();
echo json_encode($film_title_list);

?>