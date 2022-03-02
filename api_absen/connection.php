<?php

$_SERVER_DB = "localhost";
$_USERNAME = "root";
$_PASSWORD = "";
$_DATABASE = "db_absensi";

$_AUTH = mysqli_connect($_SERVER_DB, $_USERNAME, $_PASSWORD, $_DATABASE);

//     if ($_AUTH){
//         echo json_encode(array(
//             "message:" => "Success connect to " . $_DATABASE,
//             "code:" => 201,
//             "status:" => true,
//             )
//     );
// } else {
//     echo json_encode(array(
//         "message:" => "Unsuccessful connect to" . $_DATABASE,
//         "code:" => 401,
//         "status:" => false 
//         )
//     );
// }

//By Raka
?>