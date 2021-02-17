-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 11, 2019 at 07:58 PM
-- Server version: 10.1.40-MariaDB
-- PHP Version: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rentacarpz`
--

-- --------------------------------------------------------

--
-- Table structure for table `firmaosiguranja`
--

CREATE TABLE `firmaosiguranja` (
  `ID_FIRME` int(11) NOT NULL,
  `NAZIV` text NOT NULL,
  `ADRESA_FIRME` text NOT NULL,
  `TELEFON_FIRME` text NOT NULL,
  `FAKS_FIRME` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `firmaosiguranja`
--

INSERT INTO `firmaosiguranja` (`ID_FIRME`, `NAZIV`, `ADRESA_FIRME`, `TELEFON_FIRME`, `FAKS_FIRME`) VALUES
(1, 'Dunav Osiguranje', 'Maršala Tita  1', '+3815435612', '+38111897230'),
(2, 'Top Osiguranje', 'Jurija Gagarina 14D, Belvil, Beograd', '+381 60 5813 979', '+381 60 5813 979'),
(3, 'Wiener Städtische osiguranje', 'Trešnjinog cveta br. 1, 11070 Novi Beograd', '011 2209 800', '0800 200 800'),
(4, 'DDOR Osiguranje', 'Bulevar Mihajla Pupina 8\r\n21101 Novi Sad', '+381214802222', '0800 303 301'),
(5, 'SOCIETE GENERALE OSIGURANJE', 'Bulevar Zorana ?in?i?a 50a/b, \r\n11070 Novi Beograd, Srbija', '+381 (0) 11 260 8734', '+381 011 260 7330');

-- --------------------------------------------------------

--
-- Table structure for table `iznajmljivanje`
--

CREATE TABLE `iznajmljivanje` (
  `ID_IZNAJMLJIVANJA` int(11) NOT NULL,
  `ID_OSIGURANJA` int(11) NOT NULL,
  `ID_TIPAIZN` int(11) NOT NULL,
  `UKUPNA_CENA` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `iznajmljivanje`
--

INSERT INTO `iznajmljivanje` (`ID_IZNAJMLJIVANJA`, `ID_OSIGURANJA`, `ID_TIPAIZN`, `UKUPNA_CENA`) VALUES
(2, 1, 1, '8000'),
(3, 4, 2, '10000'),
(4, 5, 3, '20000');

-- --------------------------------------------------------

--
-- Table structure for table `klijent`
--

CREATE TABLE `klijent` (
  `ID_KLIJENTA` int(11) NOT NULL,
  `IME_KLIJENTA` text NOT NULL,
  `ADRESA_KLIJENTA` text NOT NULL,
  `TELEFON_KLIJENTA` text NOT NULL,
  `STAROST` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `klijent`
--

INSERT INTO `klijent` (`ID_KLIJENTA`, `IME_KLIJENTA`, `ADRESA_KLIJENTA`, `TELEFON_KLIJENTA`, `STAROST`) VALUES
(1, 'Luka', 'Sekulica Brdo', '+381-675-558-836', '20'),
(3, 'Damjan', 'Niksicka 16 a', '+381-635-555-491', '20'),
(4, 'Marko', 'Pozeska 18', '+381-655-554-308', '20'),
(5, 'Delic', 'Bulevar Kralja Aleksandra 2', '+381-665-556-480', '22');

-- --------------------------------------------------------

--
-- Table structure for table `markaautomobila`
--

CREATE TABLE `markaautomobila` (
  `ID_MARKE` int(11) NOT NULL,
  `MARKA` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `markaautomobila`
--

INSERT INTO `markaautomobila` (`ID_MARKE`, `MARKA`) VALUES
(1, 'Audi'),
(2, 'BMW'),
(3, 'Lamborghini'),
(4, 'Masserati'),
(5, 'Mercedes'),
(6, 'Toyota'),
(7, 'Fiat'),
(8, 'Nissan'),
(9, 'Ford'),
(10, 'Honda'),
(11, 'Suzuki');

-- --------------------------------------------------------

--
-- Table structure for table `modelautomobila`
--

CREATE TABLE `modelautomobila` (
  `ID_MODELA` int(11) NOT NULL,
  `ID_MARKE` int(11) NOT NULL,
  `TIP_AUTA` text NOT NULL,
  `SNAGA` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `modelautomobila`
--

INSERT INTO `modelautomobila` (`ID_MODELA`, `ID_MARKE`, `TIP_AUTA`, `SNAGA`) VALUES
(1, 1, 'A4', 148),
(2, 2, 'X6', 186),
(3, 3, 'Aventador', 700),
(4, 4, 'Levante', 550),
(5, 5, 'GLK', 350),
(6, 6, 'Rav 4', 160),
(7, 7, 'Punto', 90),
(8, 8, 'Qashqai', 140),
(9, 9, 'Focus', 80),
(10, 10, 'Civic', 128),
(11, 11, 'Swift', 75);

-- --------------------------------------------------------

--
-- Table structure for table `obelezje`
--

CREATE TABLE `obelezje` (
  `ID_OBELEZJA` int(11) NOT NULL,
  `ID_MODELA` int(11) NOT NULL,
  `ID_VOZILA` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `obelezje`
--

INSERT INTO `obelezje` (`ID_OBELEZJA`, `ID_MODELA`, `ID_VOZILA`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `osiguranje`
--

CREATE TABLE `osiguranje` (
  `ID_OSIGURANJA` int(11) NOT NULL,
  `ID_FIRME` int(11) NOT NULL,
  `TIP_OSIGURANJA` text NOT NULL,
  `CENA_OSIGURANJA` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `osiguranje`
--

INSERT INTO `osiguranje` (`ID_OSIGURANJA`, `ID_FIRME`, `TIP_OSIGURANJA`, `CENA_OSIGURANJA`) VALUES
(1, 1, 'FULL', 5000),
(2, 1, 'HALF', 2500),
(3, 2, 'FULL', 10000),
(4, 2, 'HALF', 5000),
(5, 3, 'FULL', 8000),
(6, 3, 'HALF', 4000),
(7, 4, 'FULL', 2000),
(8, 4, 'HALF', 1000),
(9, 5, 'FULL', 6000),
(10, 5, 'HALF', 3000);

-- --------------------------------------------------------

--
-- Table structure for table `rezervacija`
--

CREATE TABLE `rezervacija` (
  `ID_REZERVACIJE` int(11) NOT NULL,
  `ID_KLIJENTA` int(11) NOT NULL,
  `ID_VOZILA` int(11) NOT NULL,
  `ID_IZNAJMLJIVANJA` int(11) NOT NULL,
  `DATUM_POCETKA` date NOT NULL,
  `DATUN_KRAJA` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rezervacija`
--

INSERT INTO `rezervacija` (`ID_REZERVACIJE`, `ID_KLIJENTA`, `ID_VOZILA`, `ID_IZNAJMLJIVANJA`, `DATUM_POCETKA`, `DATUN_KRAJA`) VALUES
(2, 1, 1, 3, '2019-05-20', '2019-06-20');

-- --------------------------------------------------------

--
-- Table structure for table `tipiznajmljivanja`
--

CREATE TABLE `tipiznajmljivanja` (
  `ID_TIPAIZN` int(11) NOT NULL,
  `TIP_IZNAJMLJIVANJA` text NOT NULL,
  `CENA_IZNAJMLJIVANJA` decimal(10,0) NOT NULL,
  `MAX_KILOMETRAZA` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tipiznajmljivanja`
--

INSERT INTO `tipiznajmljivanja` (`ID_TIPAIZN`, `TIP_IZNAJMLJIVANJA`, `CENA_IZNAJMLJIVANJA`, `MAX_KILOMETRAZA`) VALUES
(1, 'Dnevno', '5000', 1500),
(2, 'Nedeljno', '34000', 3000),
(3, 'Vikend', '12000', 1299);

-- --------------------------------------------------------

--
-- Table structure for table `vozilo`
--

CREATE TABLE `vozilo` (
  `ID_VOZILA` int(11) NOT NULL,
  `DATUM_VRACANJA` date NOT NULL,
  `KUPOVNA_CENA` float NOT NULL,
  `KILOMETRAZA` float NOT NULL,
  `POPRAVKA` tinyint(1) NOT NULL,
  `DATUM_POPRAVKE` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vozilo`
--

INSERT INTO `vozilo` (`ID_VOZILA`, `DATUM_VRACANJA`, `KUPOVNA_CENA`, `KILOMETRAZA`, `POPRAVKA`, `DATUM_POPRAVKE`) VALUES
(1, '2019-01-01', 33650, 15000, 1, '2019-01-01'),
(2, '2019-06-06', 16000, 99000, 1, '2019-07-06');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `firmaosiguranja`
--
ALTER TABLE `firmaosiguranja`
  ADD PRIMARY KEY (`ID_FIRME`);

--
-- Indexes for table `iznajmljivanje`
--
ALTER TABLE `iznajmljivanje`
  ADD PRIMARY KEY (`ID_IZNAJMLJIVANJA`),
  ADD KEY `FK_RELATIONSHIP_12` (`ID_OSIGURANJA`),
  ADD KEY `FK_RELATIONSHIP_13` (`ID_TIPAIZN`);

--
-- Indexes for table `klijent`
--
ALTER TABLE `klijent`
  ADD PRIMARY KEY (`ID_KLIJENTA`);

--
-- Indexes for table `markaautomobila`
--
ALTER TABLE `markaautomobila`
  ADD PRIMARY KEY (`ID_MARKE`);

--
-- Indexes for table `modelautomobila`
--
ALTER TABLE `modelautomobila`
  ADD PRIMARY KEY (`ID_MODELA`),
  ADD KEY `FK_RELATIONSHIP_1` (`ID_MARKE`);

--
-- Indexes for table `obelezje`
--
ALTER TABLE `obelezje`
  ADD PRIMARY KEY (`ID_OBELEZJA`),
  ADD KEY `FK_RELATIONSHIP_2` (`ID_VOZILA`),
  ADD KEY `FK_RELATIONSHIP_3` (`ID_MODELA`);

--
-- Indexes for table `osiguranje`
--
ALTER TABLE `osiguranje`
  ADD PRIMARY KEY (`ID_OSIGURANJA`),
  ADD KEY `FK_RELATIONSHIP_4` (`ID_FIRME`);

--
-- Indexes for table `rezervacija`
--
ALTER TABLE `rezervacija`
  ADD PRIMARY KEY (`ID_REZERVACIJE`),
  ADD KEY `FK_RELATIONSHIP_10` (`ID_KLIJENTA`),
  ADD KEY `FK_RELATIONSHIP_11` (`ID_VOZILA`),
  ADD KEY `FK_RELATIONSHIP_9` (`ID_IZNAJMLJIVANJA`);

--
-- Indexes for table `tipiznajmljivanja`
--
ALTER TABLE `tipiznajmljivanja`
  ADD PRIMARY KEY (`ID_TIPAIZN`);

--
-- Indexes for table `vozilo`
--
ALTER TABLE `vozilo`
  ADD PRIMARY KEY (`ID_VOZILA`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `firmaosiguranja`
--
ALTER TABLE `firmaosiguranja`
  MODIFY `ID_FIRME` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `iznajmljivanje`
--
ALTER TABLE `iznajmljivanje`
  MODIFY `ID_IZNAJMLJIVANJA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `klijent`
--
ALTER TABLE `klijent`
  MODIFY `ID_KLIJENTA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `markaautomobila`
--
ALTER TABLE `markaautomobila`
  MODIFY `ID_MARKE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `modelautomobila`
--
ALTER TABLE `modelautomobila`
  MODIFY `ID_MODELA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `obelezje`
--
ALTER TABLE `obelezje`
  MODIFY `ID_OBELEZJA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `osiguranje`
--
ALTER TABLE `osiguranje`
  MODIFY `ID_OSIGURANJA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `rezervacija`
--
ALTER TABLE `rezervacija`
  MODIFY `ID_REZERVACIJE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tipiznajmljivanja`
--
ALTER TABLE `tipiznajmljivanja`
  MODIFY `ID_TIPAIZN` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `vozilo`
--
ALTER TABLE `vozilo`
  MODIFY `ID_VOZILA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `iznajmljivanje`
--
ALTER TABLE `iznajmljivanje`
  ADD CONSTRAINT `FK_RELATIONSHIP_12` FOREIGN KEY (`ID_OSIGURANJA`) REFERENCES `osiguranje` (`ID_OSIGURANJA`),
  ADD CONSTRAINT `FK_RELATIONSHIP_13` FOREIGN KEY (`ID_TIPAIZN`) REFERENCES `tipiznajmljivanja` (`ID_TIPAIZN`);

--
-- Constraints for table `modelautomobila`
--
ALTER TABLE `modelautomobila`
  ADD CONSTRAINT `FK_RELATIONSHIP_1` FOREIGN KEY (`ID_MARKE`) REFERENCES `markaautomobila` (`ID_MARKE`);

--
-- Constraints for table `obelezje`
--
ALTER TABLE `obelezje`
  ADD CONSTRAINT `FK_RELATIONSHIP_2` FOREIGN KEY (`ID_VOZILA`) REFERENCES `vozilo` (`ID_VOZILA`),
  ADD CONSTRAINT `FK_RELATIONSHIP_3` FOREIGN KEY (`ID_MODELA`) REFERENCES `modelautomobila` (`ID_MODELA`);

--
-- Constraints for table `osiguranje`
--
ALTER TABLE `osiguranje`
  ADD CONSTRAINT `FK_RELATIONSHIP_4` FOREIGN KEY (`ID_FIRME`) REFERENCES `firmaosiguranja` (`ID_FIRME`);

--
-- Constraints for table `rezervacija`
--
ALTER TABLE `rezervacija`
  ADD CONSTRAINT `FK_RELATIONSHIP_10` FOREIGN KEY (`ID_KLIJENTA`) REFERENCES `klijent` (`ID_KLIJENTA`),
  ADD CONSTRAINT `FK_RELATIONSHIP_11` FOREIGN KEY (`ID_VOZILA`) REFERENCES `vozilo` (`ID_VOZILA`),
  ADD CONSTRAINT `FK_RELATIONSHIP_9` FOREIGN KEY (`ID_IZNAJMLJIVANJA`) REFERENCES `iznajmljivanje` (`ID_IZNAJMLJIVANJA`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
