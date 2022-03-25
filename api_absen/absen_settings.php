<?php

    include 'connection.php';

    if ($_SERVER['REQUEST_METHOD'] == 'GET') {

        $query_get_settings_absen = "SELECT kunci, nilai FROM tbl_pengaturan_absen";
        $get_settings_absen = mysqli_query($_AUTH, $query_get_settings_absen);

        if (isset($get_settings_absen)) {
            $response['message'] = "Pengaturan absen didapatkan!";
            $response['code'] = 200;
            $response['status'] = true;
            $response['setting'] = array();

            while ($row = mysqli_fetch_array($get_settings_absen)) {
                $data = array();

                $list_nilai = array();
                array_push($list_nilai, $row['nilai']);

                $data[$row['kunci']] = $list_nilai[0];

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