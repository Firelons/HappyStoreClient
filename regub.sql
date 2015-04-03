-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Ven 03 Avril 2015 à 14:02
-- Version du serveur: 5.5.41-MariaDB-1ubuntu0.14.04.1
-- Version de PHP: 5.5.9-1ubuntu4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `regub`
--

DELIMITER $$
--
-- Procédures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `getLogin`(IN `log` VARCHAR(64) CHARSET utf8, IN `pass` VARCHAR(64) CHARSET utf8)
    NO SQL
    DETERMINISTIC
BEGIN
	SELECT Compte.nom,Compte.prenom,Compte.login,Compte.creation,TypeCompte.libelle AS type,TypeCompte.dblogin,TypeCompte.dbpassword
	FROM Compte INNER JOIN TypeCompte ON Compte.idTypeCompte = TypeCompte.idTypeCompte
	WHERE (log = Compte.login AND Compte.password = SHA2( CONCAT( pass , Compte.salt),256));
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `updPass`(IN `login` VARCHAR(64) CHARSET utf8, IN `oldpass` VARCHAR(64) CHARSET utf8, IN `newpass` VARCHAR(64) CHARSET utf8)
    NO SQL
UPDATE `Compte`
SET `password` = newpass
WHERE(`Compte`.`login` = login AND
      `Compte`.`password` = SHA2(CONCAT(oldpass,Compte.salt),256))$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `Client`
--

CREATE TABLE IF NOT EXISTS `Client` (
  `idClient` int(8) NOT NULL AUTO_INCREMENT,
  `societe` varchar(64) NOT NULL,
  `telephone` varchar(10) NOT NULL,
  `email` varchar(32) NOT NULL,
  `addr_ligne1` varchar(128) DEFAULT NULL,
  `addr_ligne2` varchar(128) DEFAULT NULL,
  `ville` varchar(64) DEFAULT NULL,
  `code_postal` char(5) NOT NULL,
  PRIMARY KEY (`idClient`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `Client`
--

INSERT INTO `Client` (`idClient`, `societe`, `telephone`, `email`, `addr_ligne1`, `addr_ligne2`, `ville`, `code_postal`) VALUES
(1, 'TF1', '0606060606', 'tf1@gmail.com', 'rue de truc', '', 'Paris', '75000');

-- --------------------------------------------------------

--
-- Structure de la table `Compte`
--

CREATE TABLE IF NOT EXISTS `Compte` (
  `idCompte` int(8) NOT NULL AUTO_INCREMENT,
  `nom` varchar(32) NOT NULL,
  `prenom` varchar(32) NOT NULL,
  `login` varchar(32) NOT NULL,
  `password` char(64) NOT NULL,
  `salt` char(32) NOT NULL,
  `creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idTypeCompte` int(8) NOT NULL,
  PRIMARY KEY (`idCompte`),
  UNIQUE KEY `login` (`login`),
  KEY `idTypeCompte` (`idTypeCompte`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `Compte`
--

INSERT INTO `Compte` (`idCompte`, `nom`, `prenom`, `login`, `password`, `salt`, `creation`, `idTypeCompte`) VALUES
(1, 'toto', 'toto', 'toto', '886bbdd455979d3ec863ef9d6f471cd056bbb0dd0215a04aaf73e45069341a7c', '8ece59717aa770a50b6a43decd067257', '2015-03-17 23:00:00', 2),
(2, 'titi', 'titi', 'titi', 'a90dacbe58f3aa6825c04ff467030b36472ef80fbca958934871d1a73a8c4dbd', '8493cf52b4a630b881bb235ee121d17c', '2015-03-24 23:00:00', 1),
(3, 'tutu', 'tutu', 'tutu', 'c38e1402d2ab0207a8852a8621db41a1f623cdd9d23e4dadc3d366fa8eda7b6b', '8cbfb49f6420a4dde052c55ab57a7aa0', '2015-03-25 22:08:28', 3);

--
-- Déclencheurs `Compte`
--
DROP TRIGGER IF EXISTS `insCmpt`;
DELIMITER //
CREATE TRIGGER `insCmpt` BEFORE INSERT ON `Compte`
 FOR EACH ROW BEGIN
	SET NEW.salt = md5(uuid());            
	SET NEW.password =SHA2(CONCAT(NEW.password,NEW.salt),256);            
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `updCmpt`;
DELIMITER //
CREATE TRIGGER `updCmpt` BEFORE UPDATE ON `Compte`
 FOR EACH ROW BEGIN
	IF ( NEW.password <> OLD.password ) THEN
		SET NEW.salt = md5(uuid());            
		SET NEW.password =SHA2(CONCAT(NEW.password,NEW.salt),256); 
	ELSE
		SET NEW.salt = OLD.salt;
	END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `DiffusionRegions`
--

CREATE TABLE IF NOT EXISTS `DiffusionRegions` (
  `idVideo` int(8) NOT NULL,
  `idRegion` int(8) NOT NULL,
  PRIMARY KEY (`idVideo`,`idRegion`),
  KEY `fk_idRegion` (`idRegion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `Diffusions`
--

CREATE TABLE IF NOT EXISTS `Diffusions` (
  `idDiffusion` int(8) NOT NULL AUTO_INCREMENT,
  `idVideo` int(8) NOT NULL,
  `idMagasin` int(8) NOT NULL,
  `idTypeRayon` int(8) NOT NULL,
  `dateDiffusion` datetime NOT NULL,
  PRIMARY KEY (`idDiffusion`),
  KEY `idVideo` (`idVideo`),
  KEY `idMagasin` (`idMagasin`),
  KEY `idTypeRayon` (`idTypeRayon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `DiffusionsTypesRayons`
--

CREATE TABLE IF NOT EXISTS `DiffusionsTypesRayons` (
  `idVideo` int(8) NOT NULL,
  `idTypeRayon` int(8) NOT NULL,
  PRIMARY KEY (`idVideo`,`idTypeRayon`),
  KEY `fk_typerayon` (`idTypeRayon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `DiffusionsTypesRayons`
--

INSERT INTO `DiffusionsTypesRayons` (`idVideo`, `idTypeRayon`) VALUES
(2, 1),
(3, 1),
(4, 1),
(4, 2),
(5, 1);

-- --------------------------------------------------------

--
-- Structure de la table `Magasin`
--

CREATE TABLE IF NOT EXISTS `Magasin` (
  `idMagasin` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(32) NOT NULL,
  `addr_ligne1` varchar(32) DEFAULT NULL,
  `addr_ligne2` varchar(32) DEFAULT NULL,
  `code_postal` char(5) NOT NULL,
  `idRegion` int(8) NOT NULL,
  `ville` varchar(64) NOT NULL,
  PRIMARY KEY (`idMagasin`),
  KEY `idRegion` (`idRegion`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `Magasin`
--

INSERT INTO `Magasin` (`idMagasin`, `nom`, `addr_ligne1`, `addr_ligne2`, `code_postal`, `idRegion`, `ville`) VALUES
(1, 'Carouf', '3 rue de limoges', 'les 7 chemins', '87100', 17, '');

-- --------------------------------------------------------

--
-- Structure de la table `Rayons`
--

CREATE TABLE IF NOT EXISTS `Rayons` (
  `idMagasin` int(8) NOT NULL,
  `idTypeRayon` int(8) NOT NULL,
  PRIMARY KEY (`idMagasin`,`idTypeRayon`),
  KEY `fk_typRay` (`idTypeRayon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `Rayons`
--

INSERT INTO `Rayons` (`idMagasin`, `idTypeRayon`) VALUES
(1, 1),
(1, 2);

-- --------------------------------------------------------

--
-- Structure de la table `Region`
--

CREATE TABLE IF NOT EXISTS `Region` (
  `idRegion` int(8) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(32) NOT NULL,
  PRIMARY KEY (`idRegion`),
  UNIQUE KEY `libelle` (`libelle`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;

--
-- Contenu de la table `Region`
--

INSERT INTO `Region` (`idRegion`, `libelle`) VALUES
(1, 'Alsace'),
(2, 'Aquitaine'),
(3, 'Auvergne'),
(4, 'Basse-Normandie'),
(5, 'Bourgogne'),
(6, 'Bretagne'),
(7, 'Centre'),
(8, 'Champagne-Ardenne'),
(9, 'Corse'),
(10, 'Franche-Comté'),
(11, 'Guadeloupe'),
(12, 'Guyane'),
(13, 'Haute-Normandie'),
(14, 'Île-de-France'),
(15, 'La Réunion'),
(16, 'Languedoc-Roussillon'),
(17, 'Limousin'),
(18, 'Lorraine'),
(19, 'Martinique'),
(20, 'Mayotte'),
(21, 'Midi-Pyrénées'),
(22, 'Nord-Pas-de-Calais'),
(23, 'Pays de la Loire'),
(24, 'Picardie'),
(25, 'Poitou-Charentes'),
(26, 'Provence-Alpes-Côte d''Azur'),
(27, 'Rhône-Alpes');

-- --------------------------------------------------------

--
-- Structure de la table `TypeCompte`
--

CREATE TABLE IF NOT EXISTS `TypeCompte` (
  `idTypeCompte` int(8) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(32) NOT NULL,
  `dblogin` varchar(64) NOT NULL,
  `dbpassword` varchar(64) NOT NULL,
  PRIMARY KEY (`idTypeCompte`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `TypeCompte`
--

INSERT INTO `TypeCompte` (`idTypeCompte`, `libelle`, `dblogin`, `dbpassword`) VALUES
(1, 'administrateur', 'adm', 'adm'),
(2, 'commercial', 'com', 'com'),
(3, 'gestionnaire', 'ges', 'ges');

-- --------------------------------------------------------

--
-- Structure de la table `TypeRayon`
--

CREATE TABLE IF NOT EXISTS `TypeRayon` (
  `idTypeRayon` int(8) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(32) NOT NULL,
  PRIMARY KEY (`idTypeRayon`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `TypeRayon`
--

INSERT INTO `TypeRayon` (`idTypeRayon`, `libelle`) VALUES
(1, 'Poissonerie'),
(2, 'Charcuterie'),
(3, 'Soins femmes');

-- --------------------------------------------------------

--
-- Structure de la table `Video`
--

CREATE TABLE IF NOT EXISTS `Video` (
  `idVideo` int(8) NOT NULL AUTO_INCREMENT,
  `titre` varchar(32) NOT NULL,
  `frequence` int(11) NOT NULL,
  `duree` int(11) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL,
  `dateReception` date NOT NULL,
  `dateValidation` date NOT NULL,
  `tarif` double NOT NULL,
  `statut` int(1) NOT NULL,
  `idCommercial` int(8) NOT NULL,
  `idClient` int(8) NOT NULL,
  PRIMARY KEY (`idVideo`),
  KEY `idCommercial` (`idCommercial`),
  KEY `idClient` (`idClient`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `Video`
--

INSERT INTO `Video` (`idVideo`, `titre`, `frequence`, `duree`, `dateDebut`, `dateFin`, `dateReception`, `dateValidation`, `tarif`, `statut`, `idCommercial`, `idClient`) VALUES
(2, 'Test', 3, 26, '2015-03-19', '2015-03-31', '2015-03-11', '2015-03-13', 25, 2, 1, 1),
(3, 'Video de merde', 4, 30, '2015-03-11', '2015-03-28', '2015-03-31', '2015-03-27', 111, 1, 1, 1),
(4, 'Video courte', 7, 100, '2015-03-26', '2015-06-19', '2015-03-23', '2015-03-27', 155, 1, 1, 1),
(5, 'Video stupide', 2, 85, '2015-03-18', '2015-07-16', '2015-03-01', '2015-03-02', 1222, 1, 1, 1);

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `Compte`
--
ALTER TABLE `Compte`
  ADD CONSTRAINT `foreikeyTypeCompte` FOREIGN KEY (`idTypeCompte`) REFERENCES `TypeCompte` (`idTypeCompte`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Contraintes pour la table `DiffusionRegions`
--
ALTER TABLE `DiffusionRegions`
  ADD CONSTRAINT `DiffusionRegions_ibfk_1` FOREIGN KEY (`idVideo`) REFERENCES `Video` (`idVideo`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_idRegion` FOREIGN KEY (`idRegion`) REFERENCES `Region` (`idRegion`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Contraintes pour la table `Diffusions`
--
ALTER TABLE `Diffusions`
  ADD CONSTRAINT `fk_magasin` FOREIGN KEY (`idMagasin`) REFERENCES `Magasin` (`idMagasin`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `foreikeyTypeRayon` FOREIGN KEY (`idTypeRayon`) REFERENCES `TypeRayon` (`idTypeRayon`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `foreikeyVideo` FOREIGN KEY (`idVideo`) REFERENCES `Video` (`idVideo`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Contraintes pour la table `DiffusionsTypesRayons`
--
ALTER TABLE `DiffusionsTypesRayons`
  ADD CONSTRAINT `fk_typerayon` FOREIGN KEY (`idTypeRayon`) REFERENCES `TypeRayon` (`idTypeRayon`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `foreikeyidVideo` FOREIGN KEY (`idVideo`) REFERENCES `Video` (`idVideo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `Magasin`
--
ALTER TABLE `Magasin`
  ADD CONSTRAINT `fk_region` FOREIGN KEY (`idRegion`) REFERENCES `Region` (`idRegion`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Contraintes pour la table `Rayons`
--
ALTER TABLE `Rayons`
  ADD CONSTRAINT `fk_typRay` FOREIGN KEY (`idTypeRayon`) REFERENCES `TypeRayon` (`idTypeRayon`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `foreikeyMagasin` FOREIGN KEY (`idMagasin`) REFERENCES `Magasin` (`idMagasin`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Contraintes pour la table `Video`
--
ALTER TABLE `Video`
  ADD CONSTRAINT `fk_client` FOREIGN KEY (`idClient`) REFERENCES `Client` (`idClient`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `foreikeyCom` FOREIGN KEY (`idCommercial`) REFERENCES `Compte` (`idCompte`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
