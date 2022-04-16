<?php
include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $tipe_absen = $_POST['tipe_absen'];
    $absen_awal = $_POST['absen_awal'];
    $absen_akhir = $_POST['absen_akhir'];

    if ($tipe_absen == '1') {

        $query_updateWaktuAwalAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_awal' WHERE id = '2'";
        $query_updateWaktuAkhirAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_akhir' WHERE id = '3'";

        $exec_updateWaktuAwalAbsen = mysqli_query($_AUTH, $query_updateWaktuAwalAbsen);
        $exec_updateWaktuAkhirAbsen = mysqli_query($_AUTH, $query_updateWaktuAkhirAbsen);

        if ($exec_updateWaktuAwalAbsen && $exec_updateWaktuAkhirAbsen) {
            $response['message'] = "Waktu absen berhasil diubah";
            $response['code'] = 200;
            $response['status'] = true;

            echo json_encode($response);
        } else {
            $response['message'] = "Waktu absen gagal diubah";
            $response['code'] = 400;
            $response['status'] = false;

            echo json_encode($response);
        }

    } else if ($tipe_absen == '2') {

        $query_updateWaktuAwalAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_awal' WHERE id = '4'";
        $query_updateWaktuAkhirAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_akhir' WHERE id = '5'";

        $exec_updateWaktuAwalAbsen = mysqli_query($_AUTH, $query_updateWaktuAwalAbsen);
        $exec_updateWaktuAkhirAbsen = mysqli_query($_AUTH, $query_updateWaktuAkhirAbsen);

        if ($exec_updateWaktuAwalAbsen && $exec_updateWaktuAkhirAbsen) {
            $response['message'] = "Waktu absen berhasil diubah";
            $response['code'] = 200;
            $response['status'] = true;

            echo json_encode($response);
        } else {
            $response['message'] = "Waktu absen gagal diubah";
            $response['code'] = 400;
            $response['status'] = false;

            echo json_encode($response);
        }

    } else if ($tipe_absen == '3') {

        $query_updateWaktuAwalAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_awal' WHERE id = '6'";
        $query_updateWaktuAkhirAbsen = "UPDATE tbl_pengaturan_absen SET nilai = '$absen_akhir' WHERE id = '7'";

        $exec_updateWaktuAwalAbsen = mysqli_query($_AUTH, $query_updateWaktuAwalAbsen);
        $exec_updateWaktuAkhirAbsen = mysqli_query($_AUTH, $query_updateWaktuAkhirAbsen);

        if ($exec_updateWaktuAwalAbsen && $exec_updateWaktuAkhirAbsen) {
            $response['message'] = "Waktu absen berhasil diubah";
            $response['code'] = 200;
            $response['status'] = true;

            echo json_encode($response);
        } else {
            $response['message'] = "Waktu absen gagal diubah";
            $response['code'] = 400;
            $response['status'] = false;

            echo json_encode($response);
        }

    } else if ($tipe_absen == '4') {
        // $query_updateAbsenSiangDiperlukan = "UPDATE tbl_pengaturan_absen SET nilai = '0' WHERE id = '1'";
        // $query_updateAbsenSiangTidakDiperlukan = "UPDATE tbl_pengaturan_absen SET nilai = '1' WHERE id = '1'";
        $query_cekNilaiSekarang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";

        $execute_query_cekNilaiSekarang = mysqli_query($_AUTH, $query_cekNilaiSekarang);
        $cek_execute_query_cekNilaiSekarang = mysqli_fetch_assoc($execute_query_cekNilaiSekarang);

        if ($cek_execute_query_cekNilaiSekarang['nilai'] == '1') {
            // echo "true";
            $execute_query_updateAbsenSiangTidakDiperlukan = mysqli_query($_AUTH, "UPDATE tbl_pengaturan_absen SET nilai = '0' WHERE id = '1'");
            if ($execute_query_updateAbsenSiangTidakDiperlukan) {
                $response['message'] = "Berhasil mengubah pengaturan absen siang";
                $response['code'] = 200;
                $response['status'] = true;

                echo json_encode($response);
            } else {
                $response['message'] = "Gagal mengubah pengaturan absen siang";
                $response['code'] = 404;
                $response['status'] = false;

                echo json_encode($response);
            }
        } else {
            // echo "false";
            $execute_query_updateAbsenSiangDiperlukan = mysqli_query($_AUTH, "UPDATE tbl_pengaturan_absen SET nilai = '1' WHERE id = '1'");
            if ($execute_query_updateAbsenSiangDiperlukan) {
                $response['message'] = "Berhasil mengubah pengaturan absen siang";
                $response['code'] = 200;
                $response['status'] = true;

                echo json_encode($response);
            } else {
                $response['message'] = "Gagal mengubah pengaturan absen siang";
                $response['code'] = 404;
                $response['status'] = false;

                echo json_encode($response);
            }
        }
    }
} else {

    $response['message'] = "Method not allowed";
    $response['code'] = 405;
    $response['status'] = false;

    echo json_encode($response);
}
