<?php

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    $username = $_POST['username'];

    $query_getData = mysqli_query($_AUTH, "SELECT nama_karyawan FROM tbl_karyawan WHERE nama_karyawan = '$username'");

    $exec_queryGetData = mysqli_fetch_assoc($query_getData);

    $response['message'] = "Data karyawan berhasil diambil!";
    $response['code'] = 200;
    $response['status'] = true;
    $response['data'] = $exec_queryGetData['nama_karyawan'];

    echo json_encode($response);
} else {
    $response['message'] = "Method not allowed!";
    $response['code'] = 405;
    $response['status'] = false;

    echo json_encode($response);
}

?>