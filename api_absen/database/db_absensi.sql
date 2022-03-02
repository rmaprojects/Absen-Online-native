CREATE TABLE tbl_karyawan(
    id_karyawan INT(11) AUTO_INCREMENT ,
    nama_karyawan VARCHAR (60),
    jabatan VARCHAR (24),
    departement VARCHAR (24),
    business_unit VARCHAR(24),
    status_admin boolean,
    PRIMARY KEY (id_karyawan)
);

CREATE TABLE tbl_pengaturan_absen (
    absen_siang_diperlukan boolean,
    absen_pagi_awal TIME,
    absen_pagi_akhir TIME,
    absen_siang_awal TIME,
    absen_siang_akhir TIME,
    absen_pulang_awal TIME,
    absen_pulang_akhir TIME
);

CREATE TABLE tbl_absensi (
    id_karyawan INT(11),
    nama_karyawan VARCHAR (60),
    absen_pagi TIME,
    absen_siang TIME,
    absen_pulang TIME,
    absen_pagi_awal TIME,
    absen_pagi_akhir TIME,
    absen_siang_awal TIME,
    absen_siang_akhir TIME,
    absen_pulang_awal TIME,
    absen_pulang_akhir TIME,
    FOREIGN KEY (id_karyawan) REFERENCES tbl_karyawan(id_karyawan)
);

INSERT INTO tbl_karyawan()