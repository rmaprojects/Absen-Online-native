<?php

ini_set('display_errors', 1);
error_reporting(~0);

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $username = $_POST['username'];
        $password = $_POST['password'];
        $absen_type = $_POST['tipe_absen'];
        $longitude = $_POST['longitude'];
        $latitude = $_POST['latitude'];
        $photo_name = $_POST['photo_name'];
        $keterangan = $_POST['keterangan'];

        $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
        $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

        if ($exec_queryCheckData['total'] == 0) {

            $response['message'] = "User tidak ditemukan, pastikan password dan username sudah benar lalu coba lagi";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);

        } else {

            $queryGetData = "SELECT id_karyawan FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
            $exec_queryGetData = mysqli_fetch_array(mysqli_query($_AUTH, $queryGetData));

            $id_karyawan = $exec_queryGetData['id_karyawan'];
            $type = $absen_type;
            $long = $longitude;
            $lat = $latitude;
            $ket = $keterangan;
            $photo = $photo_name;

            if ($type == '1') {
                $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '2'";
                $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

                $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '3'";
                $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

                $queryGetBoolSiang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";
                $exec_queryGetSettingsBoolSiang = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetBoolSiang));

                $nilaiAwal = array();
                $nilaiAkhir = array();
                $absen_siang_diperlukan = array();

                for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                    array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
                }

                for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                    array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
                }

                for ($i = 0; $i < count($exec_queryGetSettingsBoolSiang); $i++) {
                    array_push($absen_siang_diperlukan, $exec_queryGetSettingsBoolSiang['nilai']);
                }

                $waktu_absen_awal = $nilaiAwal[0];
                $waktu_absen_akhir = $nilaiAkhir[0];
                $perlu_absen_siang = $absen_siang_diperlukan[0];

                $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, type_absen, longitude, latitude, photo_directory, keterangan, absen_awal, absen_akhir, absen_siang_diperlukan) VALUES ('$id_karyawan', '$type', '$long', '$lat', '$photo', '$ket', '$waktu_absen_awal', '$waktu_absen_akhir', '$perlu_absen_siang')");

                if ($inputAbsen) {

                    $response['message'] = "Sukses menginput absensi!";
                    $response['code'] = 200;
                    $response['status'] = true;
                    $response['tipe_absen'] = "Absen Pagi";

                    echo json_encode($response);
                } else {

                    $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                    $response['code'] = 400;
                    $response['status'] = false;

                    echo json_encode($response);
                }
            } else if ($absen_type == '2') {

                $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '4'";
                $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

                $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '5'";
                $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

                $nilaiAwal = array();
                $nilaiAkhir = array();

                for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                    array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
                }

                for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                    array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
                }

                $waktu_absen_awal = $nilaiAwal[0];
                $waktu_absen_akhir = $nilaiAkhir[0];

                $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, type_absen, longitude, latitude, photo_directory, keterangan, absen_awal, absen_akhir) VALUES ('$id_karyawan', '$type', '$long', '$lat', '$photo', '$ket', '$waktu_absen_awal', '$waktu_absen_akhir')");

                if ($inputAbsen) {

                    $response['message'] = "Sukses menginput absensi!";
                    $response['code'] = 200;
                    $response['status'] = true;
                    $response['tipe_absen'] = "Absen Siang";

                    echo json_encode($response);
                } else {

                    $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                    $response['code'] = 400;
                    $response['status'] = false;

                    echo json_encode($response);
                }

            } else if ($absen_type ==  '3') {
                $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '6'";
                $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

                $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '7'";
                $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

                $nilaiAwal = array();
                $nilaiAkhir = array();

                for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                    array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
                }

                for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                    array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
                }

                $waktu_absen_awal = $nilaiAwal[0];
                $waktu_absen_akhir = $nilaiAkhir[0];

                $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, type_absen, longitude, latitude, photo_directory, keterangan, absen_awal, absen_akhir) VALUES ('$id_karyawan', '$type', '$long', '$lat', '$photo', '$ket', '$waktu_absen_awal', '$waktu_absen_akhir')");

                if ($inputAbsen) {

                    $response['message'] = "Sukses menginput absensi!";
                    $response['code'] = 200;
                    $response['status'] = true;
                    $response['tipe_absen'] = "Absen Pulang";

                    echo json_encode($response);
                } else {

                    $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                    $response['code'] = 400;
                    $response['status'] = false;

                    echo json_encode($response);
                }
            } else if ($absen_type == '4' || '5') {
                $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, type_absen, longitude, latitude, photo_directory, keterangan, absen_awal, absen_akhir) VALUES ('$id_karyawan', '$type', '$long', '$lat', '$photo', '$ket', null, null)");

                $tipe_absen = "";

                if ($absen_type == '4') {
                    $tipe_absen = "Cuti";
                } else if ($absen_type == '5') {
                    $tipe_absen = "Izin";
                }

                if ($inputAbsen) {

                    $response['message'] = "Sukses menginput absensi!";
                    $response['code'] = 200;
                    $response['status'] = true;
                    $response['tipe_absen'] = $tipe_absen;

                    echo json_encode($response);
                } else {

                    $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                    $response['code'] = 400;
                    $response['status'] = false;

                    echo json_encode($response);
                }
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