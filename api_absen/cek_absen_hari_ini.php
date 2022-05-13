<?php

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $username = $_POST['username'];
    $hari_ini = $_POST['tanggal_sekarang'];

    $queryCheckData = "SELECT COUNT(*) 'total' FROM v_tbl_karyawan WHERE username = '$username'";
    $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

    if ($exec_queryCheckData['total'] == 0) {

        $response['message'] = "User tidak ditemukan, pastikan password dan penamaan username sudah benar, lalu coba lagi!";
        $response['code'] = 404;
        $response['status'] = false;

        echo json_encode($response);
    } else {

        $queryGetId = "SELECT id_karyawan FROM v_tbl_karyawan WHERE username = '$username'";
        $exec_getId = mysqli_query($_AUTH, $queryGetId);

        $id_karyawan = mysqli_fetch_assoc($exec_getId)['id_karyawan'];

        $query_cek_absen = "SELECT tanggal, jam_masuk_pagi, jam_masuk_siang, jam_masuk_pulang, izin, cuti, absen_siang_diperlukan FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$hari_ini' ORDER BY tanggal DESC";
        $exec_cek_absen = mysqli_query($_AUTH, $query_cek_absen);

        $getPengaturanCekAbsenSiang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";
        $exec_getPengaturanCekAbsenSiang = mysqli_query($_AUTH, $getPengaturanCekAbsenSiang);

        $query_cek_jumlah_absen = "SELECT COUNT(*) 'jumlah_absen' FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$hari_ini'";
        $exec_cek_jumlah_absen = mysqli_fetch_assoc(mysqli_query($_AUTH, $query_cek_jumlah_absen));

        $isAbsenSiangDiperlukan = mysqli_fetch_assoc($exec_getPengaturanCekAbsenSiang)['nilai'];

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
            $data['absen_yang_dibutuhkan'] = "pagi";
            $data['absen_siang_diperlukan'] = $isAbsenSiangDiperlukan;

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
                    if ($isAbsenSiangDiperlukan == "0") {
                        $absen_yang_dibutuhkan = "pulang-siang-tidak-perlu";
                        if (isset($row['jam_masuk_pulang'])) {
                            $absen_yang_dibutuhkan = "selesai";
                        }
                    } else if ($isAbsenSiangDiperlukan == "1") {
                        $absen_yang_dibutuhkan = "siang";
                        if (isset($row['jam_masuk_siang'])) {
                            $absen_yang_dibutuhkan = "pulang";
                            if (isset($row['jam_masuk_pulang'])) {
                                $absen_yang_dibutuhkan = "selesai";
                            }
                        }
                    }
                }

                if ($row['izin'] == '1' || $row['cuti'] == '1') {
                    $absen_yang_dibutuhkan = "selesai-cuti-atau-izin";
                }

                $data['tanggal'] = $row['tanggal'];
                $data['jam_masuk_pagi'] = $row['jam_masuk_pagi'];
                $data['jam_masuk_siang'] = $row['jam_masuk_siang'];
                $data['jam_masuk_pulang'] = $row['jam_masuk_pulang'];
                $data['izin'] = $row['izin'];
                $data['cuti'] = $row['cuti'];
                $data['absen_siang_diperlukan'] = $row['absen_siang_diperlukan'];
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
