<?php

require("film-controller.php");

$film_controller = new FilmController();

if ($film_controller->test_db_connection() == true) {
    echo "true";
} else {
    echo "false";
}
