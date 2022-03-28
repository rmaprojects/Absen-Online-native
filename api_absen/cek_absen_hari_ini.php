<?php
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

        $query_cek_absen = "SELECT DATE(waktu_absen) 'tanggal_absen', TIME(waktu_absen) 'waktu_absen', type_absen, absen_awal, absen_akhir, absen_siang_diperlukan FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND waktu_absen >= '$hari_ini' ORDER BY waktu_absen DESC";
        $exec_cek_absen = mysqli_query($_AUTH, $query_cek_absen);

        $query_cek_jumlah_absen = "SELECT COUNT(*) 'jumlah_absen' FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND waktu_absen >= '$hari_ini'";
        $exec_cek_jumlah_absen = mysqli_fetch_assoc(mysqli_query($_AUTH, $query_cek_jumlah_absen));

        if ($exec_cek_jumlah_absen['jumlah_absen'] == 0) {
            $response['message'] = "History login hari ini ditampilan";
            $response['code'] = 202;
            $response['status'] = true;
            $response['absen_hari_ini'] = array();

            $data = array();

            $data['tanggal_absen'] = "Data Kosong";
            $data['waktu_absen'] = "Data Kosong";
            $data['tipe_absen'] = "Data Kosong";
            $data['status_absen'] = "Data Kosong";
            array_push($response['absen_hari_ini'], $data);

            echo json_encode($response);
        } else {
            $response['message'] = "History login hari ini ditampilan";
            $response['code'] = 200;
            $response['status'] = true;
            $response['absen_hari_ini'] = array();

            while ($row = mysqli_fetch_array($exec_cek_absen)) {

                $data = array();

                $waktu_akhir = new DateTime($row['absen_akhir']);
                $waktu_absen = new DateTime($row['waktu_absen']);

                $status_absen = "";

                if ($waktu_absen <= $waktu_akhir) {
                    $status_absen = "Hadir";
                } else if ($waktu_absen >= $waktu_akhir) {
                    $status_absen = "Terlambat";
                } else {
                    $status_absen = "Tidak Absen";
                }

                $data['tanggal_absen'] = isset($row['tanggal_absen']) ? $row['tanggal_absen'] : "Belum Absen";
                $data['waktu_absen'] = isset($row['waktu_absen']) ? $row['waktu_absen'] : "Belum Absen";
                $data['tipe_absen'] = isset($row['type_absen']) ? $row['type_absen'] : "Belum Absen";
                $data['status_absen'] = isset($status_absen) ? $status_absen : "Belum Absen";

                array_push($response['absen_hari_ini'], $data);
            }

            echo json_encode($response);
        }
    }
} else {
    $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
    $response['code'] = 400;
    $response['status'] = false;
}
