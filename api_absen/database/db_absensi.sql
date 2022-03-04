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

ALTER TABLE tbl_karyawan ADD COLUMN username VARCHAR (60);
ALTER TABLE tbl_karyawan ADD COLUMN password VARCHAR (60);

INSERT INTO tbl_karyawan(nama_karyawan ,jabatan ,departement ,business_unit ,status_admin)
VALUES ("Mamang Sumamang", "Manager", "IT", "OMG Indonesia", true);

INSERT INTO tbl_karyawan(nama_karyawan ,jabatan ,departement ,business_unit ,status_admin)
VALUES ("Sumamang Sumang", "Manager", "IT", "OMG Indonesia", false);


INSERT INTO tbl_absensi(id_karyawan, nama_karyawan,absen_pagi ,absen_siang ,absen_pulang ,absen_pagi_awal ,absen_pagi_akhir ,absen_siang_awal ,absen_siang_akhir ,absen_pulang_awal ,absen_pulang_akhir)
VALUES (1, "Mamang Sumamang", "09:14:00", "15:12:00", "17:00:00", "09:00:00", "15:00:00", "15:00:00", "17:00:00", "17:00:00","22:00:00");

INSERT INTO tbl_pengaturan_absen (absen_siang_diperlukan,absen_pagi_awal,absen_pagi_akhir,absen_siang_awal,absen_siang_akhir,absen_pulang_awal,absen_pulang_akhir)
VALUES (true, "09:00:00", "15:00:00", "15:00:00", "17:00:00", "17:00:00","22:00:00");
