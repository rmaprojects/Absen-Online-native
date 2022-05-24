<?php

ini_set('display_errors', 1);
error_reporting(~0);

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $username = $_POST['username'];
    $tanggal_awal = $_POST['tanggal_awal'];
    $tanggal_akhir = $_POST['tanggal_akhir'];

    $queryCheckData = "SELECT COUNT(*) 'total' FROM v_tbl_karyawan WHERE username = '$username'";
    $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

    if ($exec_queryCheckData['total'] == 0) {

        $response['message'] = "User tidak ditemukan, pastikan password dan penamaan username sudah benar, lalu coba lagi!";
        $response['code'] = 404;
        $response['status'] = false;

        echo json_encode($response);
    } else {

        $queryGetData = "SELECT id_karyawan FROM v_tbl_karyawan WHERE username = '$username'";
        $exec_queryGetData = mysqli_fetch_array(mysqli_query($_AUTH, $queryGetData));

        $id_karyawan = $exec_queryGetData['id_karyawan'];

        $range_tanggal_akhir = $tanggal_akhir;
        $range_tanggal_awal = $tanggal_awal;

        $queryGetDataPersentase = mysqli_query($_AUTH, "SELECT dates.fulldate, id_karyawan, tbl_absensi.izin, tbl_absensi.cuti, tbl_absensi.hadir, tbl_absensi.telat, tbl_absensi.full_absen FROM `dates` LEFT OUTER JOIN `tbl_absensi` ON dates.fulldate = Date(tbl_absensi.tanggal) WHERE  id_karyawan = '$id_karyawan' AND date(tanggal) >= '$range_tanggal_awal' AND date(tanggal) <= '$range_tanggal_akhir' UNION ALL SELECT dates.fulldate, id_karyawan, tbl_absensi.izin, tbl_absensi.cuti, tbl_absensi.hadir, tbl_absensi.telat, tbl_absensi.full_absen FROM `tbl_absensi` RIGHT OUTER JOIN `dates` ON dates.fulldate = Date(tbl_absensi.tanggal) WHERE  tbl_absensi.tanggal IS NULL AND weekend = 0 AND fulldate >= '$range_tanggal_awal' AND fulldate <= '$range_tanggal_akhir' ORDER BY fulldate ASC");

        $list_absensi = array();

        while ($exec_queryGetDataPersentase = mysqli_fetch_array($queryGetDataPersentase)) {
            $data = array(
                'tanggal' => $exec_queryGetDataPersentase['fulldate'],
                'izin' => $exec_queryGetDataPersentase['izin'],
                'cuti' => $exec_queryGetDataPersentase['cuti'],
                'hadir' => $exec_queryGetDataPersentase['hadir'],
                'telat' => $exec_queryGetDataPersentase['telat'],
                'full_absen' => $exec_queryGetDataPersentase['full_absen']
            );

            array_push($list_absensi, $data);
        }

        $totalHadir = 0;
        $cutiAtauIzin = 0;
        $totalTelat = 0;
        $onTime = 0;
        $totalTidakHadir = 0;
        $totalTidakHadirKecil = 0;
        $tidakFullAbsen = 0;

        $jumlah_hari_keseluruhan = count($list_absensi);
        $list_tanggal = array();

        foreach ($list_absensi as $key => $value) {

            //Hadir
            if ($value['hadir'] == "1") {
                $totalHadir++;
            }

            //Cuti dan tidak masuk
            if ($value['izin'] == "1" || $value['cuti'] == "1") {
                $cutiAtauIzin++;
            }

            if ($value['hadir'] == 0 || $value['hadir'] == null) {
                $totalTidakHadir++;
            }

            //On Time
            if ($value['hadir'] == "1" && $value['full_absen'] == "1" && $value['telat'] == "0" ) {
                $onTime++;
            }

            //Telat
            if ($value['hadir'] == "1" && $value['telat'] == "1") {
                $totalTelat++;
            }

            if ($value['hadir'] == "1" && $value['full_absen'] == "0") {
                $tidakFullAbsen++;
            }

            //Tidak Hadir
            if ($value['hadir'] == "0" || $value['hadir'] == null) {
                $totalTidakHadirKecil++;
            }


            array_push($list_tanggal, $value['tanggal']);
        }

        $persentaseKehadiran = 0;
        $persentaseIzinAtauCuti = 0;
        $persentaseTelat = 0;
        $persentaseOnTime = 0;
        $persentaseTidakHadir = 0;
        $persentaseTidakHadirKecil = 0;
        $persentaseTidakFullAbsen = 0;

        if ($totalHadir == 0) {
            $persentaseKehadiran = 0;
        } else {
            $persentaseKehadiran = (int)$totalHadir / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($cutiAtauIzin == 0) {
            $persentaseIzinAtauCuti = 0;
        } else {
            $persentaseIzinAtauCuti = (int)$cutiAtauIzin / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($totalTelat == 0) {
            $persentaseTelat = 0;
        } else {
            $persentaseTelat = (int)$totalTelat / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($onTime == 0) {
            $persentaseOnTime = 0;
        } else {
            $persentaseOnTime = (int)$onTime / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($totalTidakHadir == 0) {
            $persentaseTidakHadir = 0;
        } else {
            $persentaseTidakHadir = (int)$totalTidakHadir / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($totalTidakHadirKecil == 0) {
            $persentaseTidakHadirKecil = 0;
        } else {
            $persentaseTidakHadirKecil = (int)$totalTidakHadirKecil / (int)$jumlah_hari_keseluruhan * 100;
        }

        if ($tidakFullAbsen == 0) {
            $persentaseTidakFullAbsen = 0;
        } else {
            $persentaseTidakFullAbsen = (int)$tidakFullAbsen / (int)$jumlah_hari_keseluruhan * 100;
        }

        $result = new PersentaseAbsensi();

        $result->persentase_on_time = round((int)$persentaseOnTime);
        $result->persentase_tidak_full_absen = round((int)$persentaseTidakFullAbsen);
        $result->persentase_telat = round((int)$persentaseTelat);
        $result->persentase_izin_atau_cuti = round((int)$persentaseIzinAtauCuti);
        $result->persentase_tidak_hadir = round((int)$persentaseTidakHadirKecil);

        $persentase = new Persentase();

        $persentase->persentase_kehadiran = round((int)$persentaseKehadiran);
        $persentase->persentase_ketidakhadiran = round((int)$persentaseTidakHadir);

        $response['message'] = "Data persentase absensi berhasil diambil";
        $response['code'] = 200;
        $response['status'] = true;
        $response['username'] = $username;
        $response['range_tanggal'] = $range_tanggal_awal . " - " . end($list_tanggal);
        $response['total_hari'] = $jumlah_hari_keseluruhan;
        $response['persentase'] = $persentase;
        $response['hasil'] = $result;

        echo json_encode($response);
    }
} else {
    $response['message'] = "Akses ditolak, API ini menggunakan method POST";
    $response['code'] = 403;
    $response['status'] = false;

    echo json_encode($response);
}

class PersentaseAbsensi
{
    public $persentase_on_time;
    public $persentase_tidak_full_absen;
    public $persentase_telat;
    public $persentase_tidak_hadir;
    public $persentase_izin_atau_cuti;
}
class Persentase
{
    public $persentase_kehadiran;
    public $persentase_ketidakhadiran;
}