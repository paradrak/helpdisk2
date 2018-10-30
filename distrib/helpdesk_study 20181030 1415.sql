-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.18-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema helpdesk_study
--

CREATE DATABASE IF NOT EXISTS helpdesk_study;
USE helpdesk_study;

--
-- Definition of table `task_status`
--

DROP TABLE IF EXISTS `task_status`;
CREATE TABLE `task_status` (
  `id` int(11) NOT NULL,
  `t_status` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `task_status`
--

/*!40000 ALTER TABLE `task_status` DISABLE KEYS */;
INSERT INTO `task_status` (`id`,`t_status`) VALUES 
 (1,'New'),
 (2,'Viewed'),
 (3,'In work'),
 (4,'Success'),
 (5,'Reject'),
 (6,'Deleted');
/*!40000 ALTER TABLE `task_status` ENABLE KEYS */;


--
-- Definition of table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(45) NOT NULL,
  `datetime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `author` int(11) NOT NULL,
  `contractor` int(11) default NULL,
  `t_status` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_autor` (`author`),
  KEY `FK_contractor` (`contractor`),
  KEY `FK_status` (`t_status`),
  CONSTRAINT `FK_autor` FOREIGN KEY (`author`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_contractor` FOREIGN KEY (`contractor`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_status` FOREIGN KEY (`t_status`) REFERENCES `task_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tasks`
--

/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` (`id`,`title`,`datetime`,`author`,`contractor`,`t_status`) VALUES 
 (1,'Test1','2018-10-22 17:28:55',3,4,1),
 (2,'Test2','2018-10-23 17:28:55',3,5,1),
 (3,'Test3','2018-10-24 17:28:55',4,3,2),
 (6,'Test6','2018-10-22 19:38:35',3,3,3);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;


--
-- Definition of table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `status` int(11) NOT NULL COMMENT '0 - скрыт, 1 - активен, 2 - заблокирован',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`,`login`,`password`,`name`,`status`) VALUES 
 (1,'guest','guest','guest',0),
 (2,'admin','1234','admin',0),
 (3,'Shiro','000','Shiro-san',1),
 (4,'Kuro','000','Kuro-san',1),
 (5,'Sora','000','Sora-san',1),
 (6,'defender','123','Aleksey',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
