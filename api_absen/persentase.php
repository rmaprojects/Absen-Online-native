<?php

ini_set('display_errors', 1);
error_reporting(~0);

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $username = $_POST['username'];
    $password = $_POST['password'];
    $tanggal_awal = $_POST['tanggal_awal'];
    $tanggal_akhir = $_POST['tanggal_akhir'];


    $queryCheckData = "SELECT COUNT(*) 'total' FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
    $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

    if ($exec_queryCheckData['total'] == 0) {
        $response['message'] = "User tidak ditemukan, pastikan username atau password sudah benar";
        $response['code'] = 404;
        $response['status'] = false;

        echo json_encode($response);
    } else {

        $queryGetData = "SELECT id_karyawan FROM tbl_karyawan WHERE username = '$username' AND password = '$password'";
        $exec_queryGetData = mysqli_fetch_array(mysqli_query($_AUTH, $queryGetData));

        $id_karyawan = $exec_queryGetData['id_karyawan'];

        $range_tanggal_akhir = $tanggal_akhir;
        $range_tanggal_awal = $tanggal_awal;

        $queryGetDataPersentase = mysqli_query($_AUTH, "SELECT dates.fulldate, id_karyawan, tbl_absensi.izin, tbl_absensi.cuti, tbl_absensi.hadir, tbl_absensi.telat, tbl_absensi.full_absen FROM `dates` LEFT OUTER JOIN  `tbl_absensi` ON dates.fulldate = DATE(tbl_absensi.tanggal) WHERE id_karyawan = '$id_karyawan' AND tanggal <= '$range_tanggal_akhir' AND tanggal >= '$range_tanggal_awal' UNION ALL SELECT dates.fulldate, id_karyawan, tbl_absensi.izin, tbl_absensi.cuti, tbl_absensi.hadir, tbl_absensi.telat, tbl_absensi.full_absen  FROM `tbl_absensi` RIGHT OUTER JOIN `dates` ON dates.fulldate = DATE(tbl_absensi.tanggal) WHERE tbl_absensi.tanggal IS NULL AND weekend = 0 AND fulldate <= '$range_tanggal_akhir' AND fulldate >= '$range_tanggal_awal' ORDER BY fulldate ASC");

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
        $totalTidakFullAbsen = 0;
        $totalTelat = 0;

        $jumlah_hari_keseluruhan = count($list_absensi);
        $list_tanggal = array();

        foreach ($list_absensi as $key => $value) {

            //Hadir atau tidak hadir
            if ($value['hadir'] == "1" && $value['telat'] == "0" || $value['telat'] == null && $value['full_absen'] == "1") {
                $totalHadir++;
            }

            //Cuti atau izin
            if ($value['cuti'] == "1" || $value['izin'] == "1") {
                $cutiAtauIzin++;
            }

            //Tidak full Absen

            if ($value['full_absen'] == "0" || $value['full_absen'] == null && $value['hadir'] == "1") {
                $totalTidakFullAbsen++;
            }

            //Telat
            if ($value['telat'] == "1" && $value['hadir'] == "1") {
                $totalTelat++;
            }

            array_push($list_tanggal, $value['tanggal']);
        }

        $persentaseKehadiran = (int)$totalHadir / (int)$jumlah_hari_keseluruhan * 100;
        $persentaseIzinAtauCuti = (int)$cutiAtauIzin / (int)$jumlah_hari_keseluruhan * 100;
        $persentaseTidakFullAbsen = (int)$totalTidakFullAbsen / (int)$jumlah_hari_keseluruhan * 100;
        $persentaseTelat = (int)$totalTelat / (int)$jumlah_hari_keseluruhan * 100;

        $result = new PersentaseAbsensi();

        $result->persentase_hadir = (int)$persentaseKehadiran;
        $result->persentase_tidak_full_absen = (int)$persentaseTidakFullAbsen;
        $result->persentase_izin_atau_cuti = (int)$persentaseIzinAtauCuti;
        $result->persentase_telat = (int)$persentaseTelat;
        $result->persentase_tidak_hadir = 100 - ($persentaseKehadiran + $persentaseTidakFullAbsen + $persentaseIzinAtauCuti + $persentaseTelat);

        $persentase = new Persentase();

        $persentase->persentase_kehadiran = (int)$persentaseKehadiran + $persentaseTidakFullAbsen;
        $persentase->persentase_ketidakhadiran = 100 - (int)$persentaseKehadiran - $persentaseTidakFullAbsen;

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
    public $persentase_hadir;
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