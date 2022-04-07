CREATE TABLE `tbl_karyawan` (
 `id_karyawan` int(11) NOT NULL AUTO_INCREMENT,
 `nama_karyawan` varchar(60) DEFAULT NULL,
 `jabatan` varchar(24) DEFAULT NULL,
 `departement` varchar(24) DEFAULT NULL,
 `business_unit` varchar(24) DEFAULT NULL,
 `status_admin` tinyint(1) DEFAULT NULL,
 `status_karyawan` tinyint(1) DEFAULT NULL,
 `username` varchar(60) DEFAULT NULL,
 `password` varchar(60) DEFAULT NULL,
 PRIMARY KEY (`id_karyawan`),
 UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4

CREATE TABLE `tbl_pengaturan_absen` (
 `id` int(2) NOT NULL,
 `kunci` varchar(60) DEFAULT NULL,
 `nilai` varchar(60) DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

CREATE TABLE `tbl_absensi` (
 `id_absensi` int(11) NOT NULL AUTO_INCREMENT,
 `id_karyawan` int(11) DEFAULT NULL,
 `tanggal` date DEFAULT current_timestamp(),
 `jam_masuk_pagi` time DEFAULT NULL,
 `jam_awal_pagi` time DEFAULT NULL,
 `jam_akhir_pagi` time DEFAULT NULL,
 `longitude_pagi` varchar(255) DEFAULT NULL,
 `latitude_pagi` varchar(255) DEFAULT NULL,
 `photo_pagi` varchar(64) DEFAULT NULL,
 `jam_masuk_siang` time DEFAULT NULL,
 `jam_awal_siang` time DEFAULT NULL,
 `jam_akhir_siang` time DEFAULT NULL,
 `longitude_siang` varchar(255) DEFAULT NULL,
 `latitude_siang` varchar(255) DEFAULT NULL,
 `photo_siang` varchar(64) DEFAULT NULL,
 `jam_masuk_pulang` time DEFAULT NULL,
 `jam_awal_pulang` time DEFAULT NULL,
 `jam_akhir_pulang` time DEFAULT NULL,
 `longitude_pulang` varchar(255) DEFAULT NULL,
 `latitude_pulang` varchar(255) DEFAULT NULL,
 `photo_pulang` varchar(64) DEFAULT NULL,
 `izin` tinyint(1) DEFAULT NULL,
 `cuti` tinyint(1) DEFAULT NULL,
 `keterangan` text DEFAULT NULL,
 `persentase` float DEFAULT 0,
 `absen_siang_diperlukan` tinyint(1) DEFAULT NULL,
 `longitude_izin_cuti` varchar(255) DEFAULT NULL,
 `latitude_izin_cuti` varchar(255) DEFAULT NULL,
 PRIMARY KEY (`id_absensi`),
 KEY `id_karyawan` (`id_karyawan`),
 CONSTRAINT `tbl_absensi_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `tbl_karyawan` (`id_karyawan`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;
