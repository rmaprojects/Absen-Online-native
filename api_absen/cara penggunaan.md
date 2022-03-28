
# Cara Penggunaan:

    absen.php:
                Method: POST
                    Parameter:
                                - username
                                - password
                                - tipe_absen
                                - longitude
                                - latitude
                                - photo_name
                                - keterangan
                    Code:
                        - 200 = Success
                        - 404 = User not found
                        - 400 = Failed

    absen_settings.php:
                Method: GET
                    Parameter: NONE

    login.php:
                Method: POST
                    Parameter:
                                - username
                                - password
                    Code:
                        - 200 = Success
                        - 404 = User not found
                        - 400 = Failed

    history.php:
                Method: POST
                    Parameter:
                                - username
                                - password
                    Code:
                        - 200 = Success
                        - 404 = User Not Found
                        - 400 = Failed

    cek_absen_hari_ini.php:
                Method: POST
                    Parameter:
                                - username
                                - password
                                - tanggal_sekarang (format: yyyy-mm-dd)
                    Code:
                        - 200 = Success
                        - 202 = Success, but no attendance today
                        - 404 = User Not Found
                        - 400 = Failed