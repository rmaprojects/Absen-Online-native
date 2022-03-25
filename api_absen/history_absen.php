<?php

ini_set('display_errors', 1);
error_reporting(~0);

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $username = $_POST['username'];
        $password = $_POST['password'];
        $tahun = $_POST['tahun'];
        $bulan = $_POST['bulan'];

        $int_bulan = (int)$bulan + 1;
        $int_bulanMin1 = (int)$bulan - 1;

        $range_waktu = "{$tahun}-0{$int_bulan}-01";
        $range_tanggal = "{$tahun}-0{$int_bulanMin1}-01";

        $query_ambil_id = mysqli_query($_AUTH, "SELECT id_karyawan FROM tbl_karyawan WHERE username = '$username'");

        if ($tahun && $bulan == "") {

            $response['message'] = "Tahun dan bulan harus diisi";
            $response['code'] = 400;
            $response['status'] = false;

            echo json_encode($response);
        } else {

            $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
            $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

            if ($exec_queryCheckData == 0) {
                $response['message'] = "User tidak ditemukan, pastikan username atau password sudah benar";
                $response['code'] = 404;
                $response['status'] = false;

                echo json_encode($response);
            } else {
                $exec_ambil_id = mysqli_fetch_assoc($query_ambil_id);

                $id_karyawan = $exec_ambil_id['id_karyawan'];

                $waktu = $range_waktu;
                $waktu2 = $int_bulanMin1;

                $query_ambil_history = mysqli_query($_AUTH, "SELECT tbl_absensi.id_absensi, tbl_absensi.type_absen, TIME(tbl_absensi.waktu_absen) AS waktu_absen, DATE(tbl_absensi.waktu_absen) AS tanggal_absen, tbl_absensi.absen_awal, tbl_absensi.absen_akhir, tbl_absensi.absen_siang_diperlukan FROM tbl_absensi WHERE tbl_absensi.id_karyawan = '$id_karyawan' AND tbl_absensi.waktu_absen <= '$waktu' AND tbl_absensi.waktu_absen >= '$waktu2' ORDER BY tanggal_absen DESC");
                $query_ambil_nama_karyawan = mysqli_query($_AUTH,"SELECT nama_karyawan FROM tbl_karyawan WHERE id_karyawan = '$id_karyawan'");
                $exec_ambil_nama_karyawan = mysqli_fetch_assoc($query_ambil_nama_karyawan);

                $response['message'] = "Data history absen berhasil diambil";
                $response['code'] = 200;
                $response['status'] = true;
                $response['nama_karyawan'] = isset($exec_ambil_nama_karyawan['nama_karyawan']) ? $exec_ambil_nama_karyawan['nama_karyawan'] : "Data tidak ditemukan";
                $response['history'] = array();

                while ($row = mysqli_fetch_array($query_ambil_history)) {
                    $data = array();

                    $waktu_awal = new DateTime($row['absen_awal']);
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

                    $data['waktu_absen'] = isset($row['waktu_absen']) ? $row['waktu_absen'] : "Data tidak ditemukan";
                    $data['tanggal_absen'] = isset($row['tanggal_absen']) ? $row['tanggal_absen'] : "Data tidak ditemukan";
                    $data['tipe_absen'] = isset($row['type_absen']) ? $row['type_absen'] : "Data tidak ditemukan";
                    $data['status_absen'] = isset($status_absen) ? $status_absen : "Data tidak ditemukan";
                    $data['absen_siang_diperlukan'] = isset($row['absen_siang_diperlukan']) ? $row['absen_siang_diperlukan'] : "Data tidak ditemukan";

                    array_push($response['history'], $data);
                }

                echo json_encode($response);
            }
        }

    } else {
        $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
        $response['code'] = 400;
        $response['status'] = false ;

        echo json_encode($response);
    }




?>