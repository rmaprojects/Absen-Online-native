<?php

ini_set('display_errors', 1);
error_reporting(~0);

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $username = $_POST['username'];
    $password = $_POST['password'];
    $hari_ini = $_POST['tanggal_sekarang'];

    $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
    $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

    if ($exec_queryCheckData['total'] == 0) {

        $response['message'] = "User tidak ditemukan, pastikan password dan penamaan username sudah benar, lalu coba lagi!";
        $response['code'] = 404;
        $response['status'] = false;

        echo json_encode($response);
    } else {

        $queryGetId = "SELECT id_karyawan FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
        $exec_getId = mysqli_query($_AUTH, $queryGetId);

        $id_karyawan = mysqli_fetch_assoc($exec_getId)['id_karyawan'];

        $query_cek_absen = "SELECT tanggal, jam_masuk_pagi, jam_masuk_siang, jam_masuk_pulang, izin, cuti FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$hari_ini' ORDER BY tanggal DESC";
        $exec_cek_absen = mysqli_query($_AUTH, $query_cek_absen);

        $query_cek_jumlah_absen = "SELECT COUNT(*) 'jumlah_absen' FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$hari_ini'";
        $exec_cek_jumlah_absen = mysqli_fetch_assoc(mysqli_query($_AUTH, $query_cek_jumlah_absen));

        if ($exec_cek_jumlah_absen['jumlah_absen'] == 0) {
            $response['message'] = "History login hari ini ditampilan";
            $response['code'] = 202;
            $response['status'] = true;
            $response['absen_hari_ini'] = array();

            $data = array();

            $data['tanggal'] = null;
            $data['jam_masuk_pagi'] = null;
            $data['jam_masuk_siang'] = null;
            $data['jam_masuk_pulang'] = null;
            $data['izin'] = null;
            $data['cuti'] = null;

            array_push($response['absen_hari_ini'], $data);

            echo json_encode($response);
        } else {
            $response['message'] = "History login hari ini ditampilan";
            $response['code'] = 200;
            $response['status'] = true;
            $response['absen_hari_ini'] = array();

            while ($row = mysqli_fetch_array($exec_cek_absen)) {

                $data = array();

                $absen_yang_dibutuhkan = "pagi";

                if (isset($row['jam_masuk_pagi'])) {
                    $absen_yang_dibutuhkan = "siang";
                    if (isset($row['jam_masuk_siang'])) {
                        $absen_yang_dibutuhkan = "pulang";
                        if (isset($row['jam_masuk_pulang'])) {
                            $absen_yang_dibutuhkan = "selesai";
                        }
                    }
                }

                if ($row['izin'] == '1' || $row['cuti'] == '1') {
                    $absen_yang_dibutuhkan = "selesai";
                }

                $data['tanggal'] = $row['tanggal'];
                $data['jam_masuk_pagi'] = $row['jam_masuk_pagi'];
                $data['jam_masuk_siang'] = $row['jam_masuk_siang'];
                $data['jam_masuk_pulang'] = $row['jam_masuk_pulang'];
                $data['izin'] = $row['izin'];
                $data['cuti'] = $row['cuti'];
                $data['absen_yang_dibutuhkan'] = $absen_yang_dibutuhkan;

                array_push($response['absen_hari_ini'], $data);
            }
            echo json_encode($response);
        }
    }
} else {
    $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
    $response['code'] = 400;
    $response['status'] = false;

    echo json_encode($response);
}
