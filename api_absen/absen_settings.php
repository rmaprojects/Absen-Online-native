<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'GET') {

        $query_get_settings_absen = "SELECT * FROM tbl_pengaturan_absen";
        $get_settings_absen = mysqli_query($_AUTH, $query_get_settings_absen);

        if (isset($get_settings_absen)) {
            $response['message'] = "Pengaturan absen diterima!";
            $response['code'] = 200;
            $response['status'] = true;
            $response['setting'] = array();
            while ($row = mysqli_fetch_array($get_settings_absen)) {
                $data = array();
                $condition;

                if ($row['absen_siang_diperlukan'] == 1) {
                    $condition = true;
                } else {
                    $condition = false;
                }

                $data['absen_siang_diperlukan'] = $condition;
                $data['absen_pagi_awal'] = $row['absen_pagi_awal'];
                $data['absen_pagi_akhir'] = $row['absen_pagi_akhir'];
                $data['absen_siang_awal'] = $row['absen_siang_awal'];
                $data['absen_siang_akhir'] = $row['absen_siang_akhir'];
                $data['absen_pulang_awal'] = $row['absen_pulang_awal'];
                $data['absen_pulang_akhir'] = $row['absen_pulang_akhir'];

                array_push($response['setting'], $data);
            }

            echo json_encode($response);
        } else {
            $response['message'] = "Hmm... Mungkin pengaturan absen masih belum dibikin...";
            $response['code'] = 404;
            $response['status'] = false;

            echo json_encode($response);
        }

    }

//Code by Raka
?>