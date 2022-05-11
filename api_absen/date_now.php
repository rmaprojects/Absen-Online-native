<?php

include 'connection.php';

$getDateQuery = mysqli_query($_AUTH, "SELECT now() AS date");
$exec_DateQuery = mysqli_fetch_assoc($getDateQuery);

$splitExecDateQuery = explode(" ", $exec_DateQuery['date']);

$clockNow = $splitExecDateQuery[1];
$dateNow = $splitExecDateQuery[0];

$response['date_now'] = $exec_DateQuery['date'];
$response['date'] = $dateNow;
$response['clock'] = $clockNow;

echo json_encode($response);


?>