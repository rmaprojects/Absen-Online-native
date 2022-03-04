<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $username = $_POST['username'];
        $password = $_POST['password'];

        

    } else {

        $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
        $response['code'] = 400;
        $response['status'] = false;

        echo json_encode($response);
    }

//Code by Raka
?>