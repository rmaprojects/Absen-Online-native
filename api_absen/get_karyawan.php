<?php

    include 'connection.php';

    if ($_SERVER == $_SERVER['POST']) {

        

    } else {
        $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
        $response['code'] = 400;
        $response['status'] = false ;

        echo json_encode($response);
    }

?>