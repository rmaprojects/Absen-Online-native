<?php

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $username = $_POST['username'];
    $tahun = $_POST['tahun'];
    $bulan = $_POST['bulan'];

    $int_bulanP1 = (int)$bulan + 1;

    $bulan_ini = "{$tahun}-0{$bulan}-01"; // 2022-04-01
    $bulan_setelah_ini = "{$tahun}-0{$int_bulanP1}-01"; // 2022-05-01

    $query_ambil_id = mysqli_query($_AUTH, "SELECT id_karyawan FROM tbl_karyawan WHERE username = '$username'");

    if ($tahun == "" && $bulan == "") {

        $response['message'] = "Tahun dan bulan harus diisi";
        $response['code'] = 400;
        $response['status'] = false;

        echo json_encode($response);
    } else {

        $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username'";
        $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

        if ($exec_queryCheckData == 0) {
            $response['message'] = "User tidak ditemukan, pastikan username atau password sudah benar";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);
        } else {
            $exec_ambil_id = mysqli_fetch_assoc($query_ambil_id);

            $id_karyawan = $exec_ambil_id['id_karyawan'];

            $this_month = $bulan_ini; // 2022-04-01
            $next_month = $bulan_setelah_ini; // 2022-03-01

            $query_ambil_history = mysqli_query($_AUTH, "SELECT tanggal, jam_masuk_pagi, jam_akhir_pagi, jam_masuk_siang, jam_akhir_siang, jam_masuk_pulang, jam_akhir_pulang, cuti, izin, absen_siang_diperlukan FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$this_month' AND tanggal < '$next_month' ORDER BY tanggal DESC");
            $query_ambil_nama_karyawan = mysqli_query($_AUTH, "SELECT nama_karyawan FROM tbl_karyawan WHERE id_karyawan = '$id_karyawan'");
            $exec_ambil_nama_karyawan = mysqli_fetch_assoc($query_ambil_nama_karyawan);

            $checkIfTableIsExist = mysqli_query($_AUTH, "SELECT COUNT(*) 'total' FROM tbl_absensi WHERE id_karyawan = '$id_karyawan' AND tanggal >= '$this_month' AND tanggal < '$next_month'");
            $exec_checkIfTableIsExist = mysqli_fetch_assoc($checkIfTableIsExist);

            if ($exec_checkIfTableIsExist['total'] == 0) {
                $response['message'] = "Data history absen berhasil diambil";
                $response['code'] = 201;
                $response['status'] = true;
                $response['nama_karyawan'] = isset($exec_ambil_nama_karyawan['nama_karyawan']) ? $exec_ambil_nama_karyawan['nama_karyawan'] : "Data tidak ditemukan";
                $response['history'] = array();

                $data = array();

                $data['tanggal'] = "Data Belum Ada";
                $data['jam_masuk_pagi'] = "Data Belum Ada";
                $data['status_keterlambatan_pagi'] = "Data Belum Ada";
                $data['jam_masuk_siang'] = "Data Belum Ada";
                $data['status_keterlambatan_siang'] = "Data Belum Ada";
                $data['jam_masuk_pulang'] = "Data Belum Ada";
                $data['status_keterlambatan_siang'] = "Data Belum Ada";
                $data['cuti'] = "Data Belum Ada";
                $data['izin'] = "Data Belum Ada";
                $data['absen_siang_diperlukan'] = "Data Belum Ada";

                array_push($response['history'], $data);

                echo json_encode($response);
            } else {
                $response['message'] = "Data history absen berhasil diambil";
                $response['code'] = 200;
                $response['status'] = true;
                $response['nama_karyawan'] = isset($exec_ambil_nama_karyawan['nama_karyawan']) ? $exec_ambil_nama_karyawan['nama_karyawan'] : "Data tidak ditemukan";
                $response['history'] = array();

                while ($row = mysqli_fetch_array($query_ambil_history)) {

                    $data = array();

                    $status_terlambat_pagi = "Unknown";
                    $status_terlambat_siang = "Unknown";
                    $status_terlambat_pulang = "Unknown";

                    $jamMasukPagi = $row['jam_masuk_pagi'];
                    $jamMasukSiang = $row['jam_masuk_siang'];
                    $jamMasukPulang = $row['jam_masuk_pulang'];
                    $jamAkhirPagi = $row['jam_akhir_pagi'];
                    $jamAkhirSiang = $row['jam_akhir_siang'];
                    $jamAkhirPulang = $row['jam_akhir_pulang'];

                    if ($jamMasukPagi != null && $jamAkhirPagi != null) {
                        $jamAkhirPagi = new DateTime($row['jam_akhir_pagi']);
                        $jamMasukPagi = new DateTime($row['jam_masuk_pagi']);
                        if ($jamMasukPagi > $jamAkhirPagi) {
                            $status_terlambat_pagi = "Terlambat";
                        } else {
                            $status_terlambat_pagi = "Hadir";
                        }
                    }
                    if ($jamMasukSiang != null && $jamAkhirSiang != null) {
                        $jamAkhirSiang = new DateTime($row['jam_akhir_siang']);
                        $jamMasukSiang = new DateTime($row['jam_masuk_siang']);
                        if ($jamMasukSiang > $jamAkhirSiang) {
                            $status_terlambat_siang = "Terlambat";
                        } else {
                            $status_terlambat_siang = "Hadir";
                        }
                    }
                    if ($jamMasukPulang != null && $jamAkhirPulang != null) {
                        $jamAkhirPulang = new DateTime($row['jam_akhir_pulang']);
                        $jamMasukPulang = new DateTime($row['jam_masuk_pulang']);
                        if ($jamMasukPulang > $jamAkhirPulang) {
                            $status_terlambat_pulang = "Terlambat";
                        } else {
                            $status_terlambat_pulang = "Hadir";
                        }
                    }

                    $data['tanggal'] = $row['tanggal'];
                    $data['jam_masuk_pagi'] = $row['jam_masuk_pagi'];
                    $data['status_keterlambatan_pagi'] = $status_terlambat_pagi;
                    $data['jam_masuk_siang'] = $row['jam_masuk_siang'];
                    $data['status_keterlambatan_siang'] = $status_terlambat_siang;
                    $data['jam_masuk_pulang'] = $row['jam_masuk_pulang'];
                    $data['status_keterlambatan_pulang'] = $status_terlambat_pulang;
                    $data['cuti'] = $row['cuti'];
                    $data['izin'] = $row['izin'];
                    $data['absen_siang_diperlukan'] = $row['absen_siang_diperlukan'];

                    array_push($response['history'], $data);
                }

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
