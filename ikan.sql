-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2019 at 06:14 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ikan`
--

-- --------------------------------------------------------

--
-- Table structure for table `laporan`
--

CREATE TABLE `laporan` (
  `id_laporan` int(11) NOT NULL,
  `id_transaksi` varchar(7) NOT NULL,
  `tgl_transaksi` date NOT NULL,
  `total_harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `laporan`
--

INSERT INTO `laporan` (`id_laporan`, `id_transaksi`, `tgl_transaksi`, `total_harga`) VALUES
(1, 'K0004', '2019-05-21', 5500),
(2, 'K0005', '2019-05-22', 10500),
(3, 'K0006', '2019-05-22', 5500),
(4, 'K0007', '2019-05-22', 5500),
(5, 'K0008', '2019-05-22', 5500),
(6, 'K0009', '2019-05-22', 6000),
(7, 'K0010', '2019-05-22', 5500),
(8, 'K0011', '2019-05-22', 6000),
(9, 'K0012', '2019-05-22', 11500),
(10, 'K0013', '2019-05-22', 16500),
(11, 'K0014', '2019-05-22', 6000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_barang`
--

CREATE TABLE `tb_barang` (
  `id_barang` int(6) NOT NULL,
  `nama_barang` varchar(10) NOT NULL,
  `stok` int(4) NOT NULL,
  `harga` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_barang`
--

INSERT INTO `tb_barang` (`id_barang`, `nama_barang`, `stok`, `harga`) VALUES
(3, 'Gurami', 9, 5500),
(4, 'Ikan Lele', 13, 6000),
(5, 'Mujair', 7, 5000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_dtltransaksi`
--

CREATE TABLE `tb_dtltransaksi` (
  `id_dtltransaksi` int(5) NOT NULL,
  `id_transaksi` varchar(7) NOT NULL,
  `id_barang` int(6) NOT NULL,
  `nama_barang` varchar(10) NOT NULL,
  `harga` int(5) NOT NULL,
  `jumlah_beli` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_dtltransaksi`
--

INSERT INTO `tb_dtltransaksi` (`id_dtltransaksi`, `id_transaksi`, `id_barang`, `nama_barang`, `harga`, `jumlah_beli`) VALUES
(78, 'K0015', 4, 'Ikan Lele', 6000, 10),
(79, 'K0015', 3, 'Gurami', 5500, 9),
(80, 'K0015', 5, 'Mujair', 5000, 7);

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `id_transaksi` varchar(7) NOT NULL,
  `tgl_transaksi` date NOT NULL,
  `total_harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`id_transaksi`, `tgl_transaksi`, `total_harga`) VALUES
('K0001', '2019-05-21', 11500),
('K0002', '2019-05-21', 5500),
('K0003', '2019-05-21', 5000),
('K0004', '2019-05-21', 5500),
('K0005', '2019-05-22', 10500),
('K0006', '2019-05-22', 5500),
('K0007', '2019-05-22', 5500),
('K0008', '2019-05-22', 5500),
('K0009', '2019-05-22', 6000),
('K0010', '2019-05-22', 5500),
('K0011', '2019-05-22', 6000),
('K0012', '2019-05-22', 11500),
('K0013', '2019-05-22', 16500),
('K0014', '2019-05-22', 6000);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `laporan`
--
ALTER TABLE `laporan`
  ADD PRIMARY KEY (`id_laporan`);

--
-- Indexes for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `tb_dtltransaksi`
--
ALTER TABLE `tb_dtltransaksi`
  ADD PRIMARY KEY (`id_dtltransaksi`);

--
-- Indexes for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`id_transaksi`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `laporan`
--
ALTER TABLE `laporan`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tb_barang`
--
ALTER TABLE `tb_barang`
  MODIFY `id_barang` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tb_dtltransaksi`
--
ALTER TABLE `tb_dtltransaksi`
  MODIFY `id_dtltransaksi` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
