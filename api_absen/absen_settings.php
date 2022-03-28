<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'GET') {

        $query_get_settings_absen = "SELECT * FROM tbl_pengaturan_absen";
        $get_settings_absen = mysqli_query($_AUTH, $query_get_settings_absen);

        if (isset($get_settings_absen)) {
            $settingsAbsen = new SettingsAbsen();

            $data = array();

            while ($row = mysqli_fetch_array($get_settings_absen)) {
                $data[] = array(
                    'id' => $row['kunci'],
                    "nilai" => $row['nilai']
                );
            }

            $settingsAbsen->absen_siang_diperlukan = $data[0]['nilai'];
            $settingsAbsen->absen_pagi_awal = $data[1]['nilai'];
            $settingsAbsen->absen_pagi_akhir = $data[2]['nilai'];
            $settingsAbsen->absen_siang_awal = $data[3]['nilai'];
            $settingsAbsen->absen_siang_akhir = $data[4]['nilai'];
            $settingsAbsen->absen_pulang_awal = $data[5]['nilai'];
            $settingsAbsen->absen_pulang_akhir = $data[6]['nilai'];

            $response['message'] = "Pengaturan absen didapatkan!";
            $response['code'] = 200;
            $response['status'] = true;
            $response['setting'] = $settingsAbsen;

            echo json_encode($response);
        } else {
            $response['message'] = "Hmm... Mungkin pengaturan absen masih belum dibikin...";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);
        }

    } else {
        $response['message'] = "Method not allowed!";
        $response['code'] = 405;
        $response['status'] = false;

        echo json_encode($response);
    }

    class SettingsAbsen {
        public $absen_siang_diperlukan;
        public $absen_pagi_awal;
        public $absen_pagi_akhir;
        public $absen_siang_awal;
        public $absen_siang_akhir;
        public $absen_pulang_awal;
        public $absen_pulang_akhir;
    }

//Code by Raka
?>