<?php

require("dbh-class.php");

class Film extends Dbh
{
    protected int $id;
    protected string $title;
    protected int $year;
    protected string $director;
    protected string $stars;
    protected string $review;

    function get_id()
    {
        return $this->id;
    }

    function set_id($id)
    {
        $this->id = $id;
    }

    function get_title()
    {
        return $this->title;
    }

    function set_title($title)
    {
        $this->title = $title;
    }

    function get_year()
    {
        return $this->year;
    }

    function set_year($year)
    {
        $this->year = $year;
    }

    function get_director()
    {
        return $this->director;
    }

    function set_director($director)
    {
        $this->director = $director;
    }

    function get_stars()
    {
        return $this->stars;
    }

    function set_stars($stars)
    {
        $this->stars = $stars;
    }

    function get_review()
    {
        return $this->review;
    }

    function set_review($review)
    {
        $this->review = $review;
    }

    function get_film_by_id($id)
    {
        $sql = "SELECT * FROM films WHERE id = ?";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute([$id]);
        $results = $stmt->fetchAll();
        return $results;
    }

    function get_film_by_title($title)
    {
        $sql = "SELECT * FROM films WHERE title = ?";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute([$title]);
        $results = $stmt->fetchAll();
        return $results;
    }

    function get_films_like_title($title)
    {
        $sql = "SELECT * FROM films WHERE (title LIKE '%$title%')";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute();
        $results = $stmt->fetchAll();
        return $results;
    }

    function get_all_films()
    {
        $sql = "SELECT * FROM films";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute();
        $results = $stmt->fetchAll();
        return $results;
    }

    function get_all_film_titles()
    {
        $sql = "SELECT title FROM films";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute();
        $results = $stmt->fetchAll();
        return $results;
    }

    function get_last_film()
    {
        $sql = "SELECT * FROM films ORDER BY id DESC LIMIT 1";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute();
        $results = $stmt->fetchAll();
        return $results;
    }

    function insert_film($title, $year, $director, $stars, $review)
    {
        $sql = "INSERT INTO films (title, year, director, stars, review) VALUES (?, ?, ?, ?, ?)";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute([$title, $year, $director, $stars, $review]);
        $results = $stmt->fetchAll();
        return $results;
    }

    function update_film($id, $title, $year, $director, $stars, $review)
    {
        $sql = "UPDATE films SET title=?, year=?, director=?, stars=?, review=? WHERE id=?";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute([$title, $year, $director, $stars, $review, $id]);
        $results = $stmt->fetchAll();
        return $results;
    }

    function delete_film($id)
    {
        $sql = "DELETE FROM films WHERE id = ?";
        $stmt = $this->connect()->prepare($sql);
        $stmt->execute([$id]);
        $results = $stmt->fetchAll();
        return $results;
    }

    function test_db_connection()
    {
        if ($this->test_connect() == true) {
            return true;
        } else {
            return false;
        }
    }
}
