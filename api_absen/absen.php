<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $username = $_POST['username'];
        $password = $_POST['password'];
        $absen_type = $_POST['tipe_absen'];
        

        $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
        $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

        if ($exec_queryCheckData['total'] == 0) {

            $response['message'] = "User tidak ditemukan, pastikan password dan username sudah benar lalu coba lagi";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);

        } else {

            $queryGetData = "SELECT * FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
            $exec_queryGetData = mysqli_fetch_array(mysqli_query($_AUTH, $queryGetData));

            $id_karyawan = $exec_queryGetData['id_karyawan'];
            $nama_karyawan = $exec_queryGetData['nama_karyawan'];
            $type = $absen_type;

            $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, nama_karyawan, absen_type) VALUES ($id_karyawan, '$nama_karyawan', '$type')");

            if ($inputAbsen) {

                $response['message'] = "Sukses menginput absensi!";
                $response['code'] = 200;
                $response['status'] = true;

                echo json_encode($response);
            } else {

                $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                $response['code'] = 400;
                $response['status'] = false;

                echo json_encode($response);
            }
        }

    } else {

        $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
        $response['code'] = 400;
        $response['status'] = false;

        echo json_encode($response);
    }
//Code by Raka

?>