<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $username = $_POST['username'];
        $password = $_POST['password'];

        $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
        $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

        if ($exec_queryCheckData['total'] == 0) {

            $response['message'] = "User tidak ditemukan, pastikan password dan penamaan username sudah benar, lalu coba lagi!";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);
        } else {

            $queryGetData = "SELECT * FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
            $exec_queryGetData = mysqli_query($_AUTH, $queryGetData);

            $row = mysqli_fetch_array($exec_queryGetData);

            $response['message'] = "Login karyawan sukses!";
            $response['code'] = 200;
            $response['status'] = true;

            $response['id_karyawan'] = $row['id_karyawan'];
            $response['nama_karyawan'] = $row['nama_karyawan'];
            $response['jabatan'] = $row['jabatan'];
            $response['departement'] = $row['departement'];
            $response['business_unit'] = $row['business_unit'];
            $response['status_admin'] = $row['status_admin'];
            $response['status_karyawan'] = $row['status_karyawan'];
            $response['username'] = $row['username'];
            $response['password'] = $row['password'];

            echo json_encode($response);
        }

    } else {
        $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
        $response['code'] = 400;
        $response['status'] = false ;

        echo json_encode($response);
    }
//Code by Raka
?>