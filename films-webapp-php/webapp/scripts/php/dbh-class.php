<?php

class Dbh
{
    private $host = "localhost";
    private $dbname = "films";
    private $user = "root";
    private $pass = "";

    protected function test_connect()
    {
        $conn = new PDO("mysql:host=$this->host;dbname=$this->dbname", $this->user, $this->pass);
        if ($conn->query('SELECT NOW()')->fetchColumn()) {
            return true;
        } else {
            return false;
        }
    }

    protected function connect()
    {
        $conn = new PDO("mysql:host=$this->host;dbname=$this->dbname", $this->user, $this->pass);
        $conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        return $conn;
    }
}
