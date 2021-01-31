/*
MySQL Data Transfer
Source Host: localhost
Source Database: odinms
Target Host: localhost
Target Database: odinms
Date: 2009-10-15 9:00:19
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(13) NOT NULL default '',
  `password` varchar(128) NOT NULL default '',
  `salt` varchar(32) default NULL,
  `loggedin` tinyint(4) NOT NULL default '0',
  `lastlogin` timestamp NULL default NULL,
  `createdat` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `birthday` date NOT NULL default '0000-00-00',
  `banned` tinyint(1) NOT NULL default '0',
  `banreason` text,
  `gm` tinyint(1) NOT NULL default '0',
  `email` tinytext,
  `emailcode` varchar(40) default NULL,
  `md5pass` varchar(128) default NULL,
  `QQ` varchar(255) default NULL,
  `forumaccid` int(11) NOT NULL default '0',
  `macs` tinytext,
  `lastknownip` varchar(30) NOT NULL default '',
  `lastpwemail` timestamp NOT NULL default '2002-12-31 17:00:00',
  `tempban` timestamp NOT NULL default '0000-00-00 00:00:00',
  `greason` tinyint(4) default NULL,
  `paypalNX` int(11) default NULL,
  `mPoints` int(11) default NULL,
  `cardNX` int(11) default NULL,
  `donatorpoints` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `forumaccid` (`forumaccid`),
  KEY `ranking1` (`id`,`banned`,`gm`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bank
-- ----------------------------
DROP TABLE IF EXISTS `bank`;
CREATE TABLE `bank` (
  `id` int(11) NOT NULL auto_increment,
  `charid` int(11) NOT NULL,
  `money` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `charid` (`charid`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bbs_replies
-- ----------------------------
DROP TABLE IF EXISTS `bbs_replies`;
CREATE TABLE `bbs_replies` (
  `replyid` int(10) unsigned NOT NULL auto_increment,
  `threadid` int(10) unsigned NOT NULL,
  `postercid` int(10) unsigned NOT NULL,
  `timestamp` bigint(20) unsigned NOT NULL,
  `content` varchar(26) NOT NULL default '',
  PRIMARY KEY  (`replyid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bbs_threads
-- ----------------------------
DROP TABLE IF EXISTS `bbs_threads`;
CREATE TABLE `bbs_threads` (
  `threadid` int(10) unsigned NOT NULL auto_increment,
  `postercid` int(10) unsigned NOT NULL,
  `name` varchar(26) NOT NULL default '',
  `timestamp` bigint(20) unsigned NOT NULL,
  `icon` smallint(5) unsigned NOT NULL,
  `replycount` smallint(5) unsigned NOT NULL default '0',
  `startpost` text NOT NULL,
  `guildid` int(10) unsigned NOT NULL,
  `localthreadid` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`threadid`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for buddies
-- ----------------------------
DROP TABLE IF EXISTS `buddies`;
CREATE TABLE `buddies` (
  `id` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL,
  `buddyid` int(11) NOT NULL,
  `pending` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `buddies_ibfk_1` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=32228 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for cashshop
-- ----------------------------
DROP TABLE IF EXISTS `cashshop`;
CREATE TABLE `cashshop` (
  `sn` int(11) NOT NULL,
  `arg1` int(11) NOT NULL default '0',
  `arg2` int(11) NOT NULL default '0',
  `arg3` int(11) NOT NULL default '0',
  PRIMARY KEY  (`sn`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for channelconfig
-- ----------------------------
DROP TABLE IF EXISTS `channelconfig`;
CREATE TABLE `channelconfig` (
  `channelconfigid` int(10) unsigned NOT NULL auto_increment,
  `channelid` int(10) unsigned NOT NULL default '0',
  `name` tinytext NOT NULL,
  `value` tinytext NOT NULL,
  PRIMARY KEY  (`channelconfigid`),
  KEY `channelid` (`channelid`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for channels
-- ----------------------------
DROP TABLE IF EXISTS `channels`;
CREATE TABLE `channels` (
  `channelid` int(10) unsigned NOT NULL auto_increment,
  `world` int(11) NOT NULL default '0',
  `number` int(11) default NULL,
  `key` varchar(40) NOT NULL default '',
  PRIMARY KEY  (`channelid`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for characters
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `id` int(11) NOT NULL auto_increment,
  `accountid` int(11) NOT NULL default '0',
  `gm` tinyint(1) NOT NULL default '0',
  `vip` int(11) NOT NULL default '0',
  `name` varchar(13) character set gbk NOT NULL default '',
  `world` int(11) NOT NULL default '0',
  `level` int(11) NOT NULL default '0',
  `exp` int(11) NOT NULL default '0',
  `str` int(11) NOT NULL default '0',
  `dex` int(11) NOT NULL default '0',
  `luk` int(11) NOT NULL default '0',
  `int` int(11) NOT NULL default '0',
  `hp` int(11) NOT NULL default '0',
  `mp` int(11) NOT NULL default '0',
  `maxhp` int(11) NOT NULL default '0',
  `maxmp` int(11) NOT NULL default '0',
  `meso` int(11) NOT NULL default '0',
  `hpApUsed` int(11) NOT NULL default '0',
  `mpApUsed` int(11) NOT NULL default '0',
  `job` int(11) NOT NULL default '0',
  `skincolor` int(11) NOT NULL default '0',
  `gender` int(11) NOT NULL default '0',
  `fame` int(11) NOT NULL default '0',
  `hair` int(11) NOT NULL default '0',
  `face` int(11) NOT NULL default '0',
  `ap` int(11) NOT NULL default '0',
  `sp` int(11) NOT NULL default '0',
  `map` int(11) NOT NULL default '0',
  `spawnpoint` int(11) NOT NULL default '0',
  `party` int(11) NOT NULL default '0',
  `buddyCapacity` int(11) NOT NULL default '25',
  `createdate` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `rank` int(10) unsigned NOT NULL default '1',
  `rankMove` int(11) NOT NULL default '0',
  `jobRank` int(10) unsigned NOT NULL default '1',
  `jobRankMove` int(11) NOT NULL default '0',
  `guildid` int(10) unsigned NOT NULL default '0',
  `guildrank` int(10) unsigned NOT NULL default '5',
  `messengerid` int(10) unsigned NOT NULL default '0',
  `messengerposition` int(10) unsigned NOT NULL default '4',
  `reborns` int(11) NOT NULL default '0',
  `pvpkills` int(11) NOT NULL default '0',
  `pvpdeaths` int(11) NOT NULL default '0',
  `omokwins` int(11) NOT NULL default '0',
  `omoklosses` int(11) NOT NULL default '0',
  `omokties` int(11) NOT NULL default '0',
  `matchcardwins` int(11) NOT NULL default '0',
  `matchcardlosses` int(11) NOT NULL default '0',
  `matchcardties` int(11) NOT NULL default '0',
  `married` int(10) unsigned NOT NULL default '0',
  `partnerid` int(10) unsigned NOT NULL default '0',
  `cantalk` int(10) unsigned NOT NULL default '1',
  `marriagequest` int(10) unsigned NOT NULL default '0',
  `petid` int(10) default '0',
  `mountlevel` int(9) NOT NULL default '1',
  `mountexp` int(9) NOT NULL default '0',
  `mounttiredness` int(9) NOT NULL default '0',
  `karma` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `accountid` (`accountid`),
  KEY `party` (`party`),
  KEY `ranking1` (`level`,`exp`),
  KEY `ranking2` (`gm`,`job`)
) ENGINE=MyISAM AUTO_INCREMENT=30066 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for cheatlog
-- ----------------------------
DROP TABLE IF EXISTS `cheatlog`;
CREATE TABLE `cheatlog` (
  `id` int(11) NOT NULL auto_increment,
  `cid` int(11) NOT NULL default '0',
  `offense` tinytext NOT NULL,
  `count` int(11) NOT NULL default '0',
  `lastoffensetime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `param` tinytext NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `cid` (`cid`)
) ENGINE=MyISAM AUTO_INCREMENT=13021 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for dueyitems
-- ----------------------------
DROP TABLE IF EXISTS `dueyitems`;
CREATE TABLE `dueyitems` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `PackageId` int(10) unsigned NOT NULL default '0',
  `itemid` int(10) unsigned NOT NULL default '0',
  `quantity` int(10) unsigned NOT NULL default '0',
  `upgradeslots` int(11) default '0',
  `level` int(11) default '0',
  `str` int(11) default '0',
  `dex` int(11) default '0',
  `int` int(11) default '0',
  `luk` int(11) default '0',
  `hp` int(11) default '0',
  `mp` int(11) default '0',
  `watk` int(11) default '0',
  `matk` int(11) default '0',
  `wdef` int(11) default '0',
  `mdef` int(11) default '0',
  `acc` int(11) default '0',
  `avoid` int(11) default '0',
  `hands` int(11) default '0',
  `speed` int(11) default '0',
  `jump` int(11) default '0',
  `owner` varchar(13) default NULL,
  PRIMARY KEY  (`id`),
  KEY `PackageId` (`PackageId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for dueypackages
-- ----------------------------
DROP TABLE IF EXISTS `dueypackages`;
CREATE TABLE `dueypackages` (
  `PackageId` int(10) unsigned NOT NULL auto_increment,
  `RecieverId` int(10) unsigned NOT NULL,
  `SenderName` varchar(13) NOT NULL,
  `Mesos` int(10) unsigned default '0',
  `TimeStamp` varchar(10) NOT NULL,
  `Checked` tinyint(1) unsigned default '1',
  `Type` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY  (`PackageId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for eventstats
-- ----------------------------
DROP TABLE IF EXISTS `eventstats`;
CREATE TABLE `eventstats` (
  `eventstatid` int(10) unsigned NOT NULL auto_increment,
  `event` varchar(30) NOT NULL,
  `instance` varchar(30) NOT NULL,
  `characterid` int(11) NOT NULL,
  `channel` int(11) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`eventstatid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for famelog
-- ----------------------------
DROP TABLE IF EXISTS `famelog`;
CREATE TABLE `famelog` (
  `famelogid` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL default '0',
  `characterid_to` int(11) NOT NULL default '0',
  `when` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`famelogid`),
  KEY `characterid` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for gmlog
-- ----------------------------
DROP TABLE IF EXISTS `gmlog`;
CREATE TABLE `gmlog` (
  `id` int(11) NOT NULL auto_increment,
  `cid` int(11) NOT NULL default '0',
  `command` tinytext NOT NULL,
  `when` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for guilds
-- ----------------------------
DROP TABLE IF EXISTS `guilds`;
CREATE TABLE `guilds` (
  `guildid` int(10) unsigned NOT NULL auto_increment,
  `leader` int(10) unsigned NOT NULL default '0',
  `GP` int(10) unsigned NOT NULL default '0',
  `logo` int(10) unsigned default NULL,
  `logoColor` smallint(5) unsigned NOT NULL default '0',
  `name` varchar(45) character set gbk NOT NULL,
  `rank1title` varchar(45) character set gbk NOT NULL default 'Master',
  `rank2title` varchar(45) character set gbk NOT NULL default 'Jr. Master',
  `rank3title` varchar(45) character set gbk NOT NULL default 'Member',
  `rank4title` varchar(45) character set gbk NOT NULL default 'Member',
  `rank5title` varchar(45) character set gbk NOT NULL default 'Member',
  `capacity` int(10) unsigned NOT NULL default '10',
  `logoBG` int(10) unsigned default NULL,
  `logoBGColor` smallint(5) unsigned NOT NULL default '0',
  `notice` varchar(101) character set gbk default NULL,
  `signature` int(11) NOT NULL default '0',
  `hideout` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`guildid`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for htsquads
-- ----------------------------
DROP TABLE IF EXISTS `htsquads`;
CREATE TABLE `htsquads` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `channel` int(10) unsigned NOT NULL,
  `leaderid` int(10) unsigned NOT NULL default '0',
  `status` int(10) unsigned NOT NULL default '0',
  `members` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventoryequipment
-- ----------------------------
DROP TABLE IF EXISTS `inventoryequipment`;
CREATE TABLE `inventoryequipment` (
  `inventoryequipmentid` int(10) unsigned NOT NULL auto_increment,
  `inventoryitemid` int(10) unsigned NOT NULL default '0',
  `upgradeslots` int(11) NOT NULL default '0',
  `level` int(11) NOT NULL default '0',
  `str` int(11) NOT NULL default '0',
  `dex` int(11) NOT NULL default '0',
  `int` int(11) NOT NULL default '0',
  `luk` int(11) NOT NULL default '0',
  `hp` int(11) NOT NULL default '0',
  `mp` int(11) NOT NULL default '0',
  `watk` int(11) NOT NULL default '0',
  `matk` int(11) NOT NULL default '0',
  `wdef` int(11) NOT NULL default '0',
  `mdef` int(11) NOT NULL default '0',
  `acc` int(11) NOT NULL default '0',
  `avoid` int(11) NOT NULL default '0',
  `hands` int(11) NOT NULL default '0',
  `speed` int(11) NOT NULL default '0',
  `jump` int(11) NOT NULL default '0',
  `ringid` int(11) NOT NULL default '-1',
  `locked` int(11) NOT NULL default '0',
  PRIMARY KEY  (`inventoryequipmentid`),
  KEY `inventoryitemid` (`inventoryitemid`)
) ENGINE=MyISAM AUTO_INCREMENT=5230307 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventoryitems
-- ----------------------------
DROP TABLE IF EXISTS `inventoryitems`;
CREATE TABLE `inventoryitems` (
  `inventoryitemid` int(10) unsigned NOT NULL auto_increment,
  `characterid` int(11) default NULL,
  `storageid` int(10) unsigned default NULL,
  `itemid` int(11) NOT NULL default '0',
  `inventorytype` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  `quantity` int(11) NOT NULL default '0',
  `owner` tinytext character set gbk NOT NULL,
  `petid` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`inventoryitemid`),
  KEY `inventoryitems_ibfk_1` (`characterid`),
  KEY `characterid` (`characterid`),
  KEY `inventorytype` (`inventorytype`),
  KEY `storageid` (`storageid`),
  KEY `characterid_2` (`characterid`,`inventorytype`)
) ENGINE=MyISAM AUTO_INCREMENT=17416549 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventorylog
-- ----------------------------
DROP TABLE IF EXISTS `inventorylog`;
CREATE TABLE `inventorylog` (
  `inventorylogid` int(10) unsigned NOT NULL auto_increment,
  `inventoryitemid` int(10) unsigned NOT NULL default '0',
  `msg` tinytext NOT NULL,
  PRIMARY KEY  (`inventorylogid`),
  KEY `inventoryitemid` (`inventoryitemid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for ipbans
-- ----------------------------
DROP TABLE IF EXISTS `ipbans`;
CREATE TABLE `ipbans` (
  `ipbanid` int(10) unsigned NOT NULL auto_increment,
  `ip` varchar(40) NOT NULL default '',
  PRIMARY KEY  (`ipbanid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for iplog
-- ----------------------------
DROP TABLE IF EXISTS `iplog`;
CREATE TABLE `iplog` (
  `iplogid` int(10) unsigned NOT NULL auto_increment,
  `accountid` int(11) NOT NULL default '0',
  `ip` varchar(30) NOT NULL default '',
  `login` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`iplogid`),
  KEY `accountid` (`accountid`,`ip`),
  KEY `ip` (`ip`)
) ENGINE=MyISAM AUTO_INCREMENT=4583 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for keymap
-- ----------------------------
DROP TABLE IF EXISTS `keymap`;
CREATE TABLE `keymap` (
  `id` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL default '0',
  `key` int(11) NOT NULL default '0',
  `type` int(11) NOT NULL default '0',
  `action` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `keymap_ibfk_1` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=5292525 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for loginserver
-- ----------------------------
DROP TABLE IF EXISTS `loginserver`;
CREATE TABLE `loginserver` (
  `loginserverid` int(10) unsigned NOT NULL auto_increment,
  `key` varchar(40) NOT NULL default '',
  `world` int(11) NOT NULL default '0',
  PRIMARY KEY  (`loginserverid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for macbans
-- ----------------------------
DROP TABLE IF EXISTS `macbans`;
CREATE TABLE `macbans` (
  `macbanid` int(10) unsigned NOT NULL auto_increment,
  `mac` varchar(30) NOT NULL,
  PRIMARY KEY  (`macbanid`),
  UNIQUE KEY `mac_2` (`mac`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for macfilters
-- ----------------------------
DROP TABLE IF EXISTS `macfilters`;
CREATE TABLE `macfilters` (
  `macfilterid` int(10) unsigned NOT NULL auto_increment,
  `filter` varchar(30) NOT NULL,
  PRIMARY KEY  (`macfilterid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for marriages
-- ----------------------------
DROP TABLE IF EXISTS `marriages`;
CREATE TABLE `marriages` (
  `marriageid` int(10) unsigned NOT NULL auto_increment,
  `husbandid` int(10) unsigned NOT NULL default '0',
  `wifeid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`marriageid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for monsterdrops
-- ----------------------------
DROP TABLE IF EXISTS `monsterdrops`;
CREATE TABLE `monsterdrops` (
  `monsterdropid` int(10) unsigned NOT NULL auto_increment,
  `monsterid` int(11) NOT NULL default '0',
  `itemid` int(11) NOT NULL default '0',
  `chance` int(11) NOT NULL default '0',
  PRIMARY KEY  (`monsterdropid`)
) ENGINE=MyISAM AUTO_INCREMENT=16083 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mts_cart
-- ----------------------------
DROP TABLE IF EXISTS `mts_cart`;
CREATE TABLE `mts_cart` (
  `id` int(11) NOT NULL auto_increment,
  `cid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mts_items
-- ----------------------------
DROP TABLE IF EXISTS `mts_items`;
CREATE TABLE `mts_items` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `tab` int(11) NOT NULL default '0',
  `type` int(11) NOT NULL default '0',
  `itemid` int(10) unsigned NOT NULL default '0',
  `quantity` int(11) NOT NULL default '1',
  `seller` int(11) NOT NULL default '0',
  `price` int(11) NOT NULL default '0',
  `bid_incre` int(11) default '0',
  `buy_now` int(11) default '0',
  `position` int(11) default '0',
  `upgradeslots` int(11) default '0',
  `level` int(11) default '0',
  `str` int(11) default '0',
  `dex` int(11) default '0',
  `int` int(11) default '0',
  `luk` int(11) default '0',
  `hp` int(11) default '0',
  `mp` int(11) default '0',
  `watk` int(11) default '0',
  `matk` int(11) default '0',
  `wdef` int(11) default '0',
  `mdef` int(11) default '0',
  `acc` int(11) default '0',
  `avoid` int(11) default '0',
  `hands` int(11) default '0',
  `speed` int(11) default '0',
  `jump` int(11) default '0',
  `locked` int(11) default '0',
  `isequip` int(1) default '0',
  `owner` varchar(16) default '',
  `sellername` varchar(16) NOT NULL,
  `sell_ends` varchar(16) NOT NULL,
  `transfer` int(2) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for newcommands
-- ----------------------------
DROP TABLE IF EXISTS `newcommands`;
CREATE TABLE `newcommands` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(45) NOT NULL,
  `monsterid` int(10) unsigned NOT NULL,
  `quantity` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `gmlvl` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for notes
-- ----------------------------
DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes` (
  `id` int(11) NOT NULL auto_increment,
  `to` varchar(13) NOT NULL default '',
  `from` varchar(13) NOT NULL default '',
  `message` text NOT NULL,
  `timestamp` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for nxcode
-- ----------------------------
DROP TABLE IF EXISTS `nxcode`;
CREATE TABLE `nxcode` (
  `code` varchar(15) NOT NULL,
  `valid` int(11) NOT NULL default '1',
  `user` varchar(13) default NULL,
  `type` int(11) NOT NULL default '0',
  `item` int(11) NOT NULL default '10000',
  PRIMARY KEY  (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for pets
-- ----------------------------
DROP TABLE IF EXISTS `pets`;
CREATE TABLE `pets` (
  `petid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(13) default NULL,
  `level` int(10) unsigned NOT NULL,
  `closeness` int(10) unsigned NOT NULL,
  `fullness` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`petid`)
) ENGINE=MyISAM AUTO_INCREMENT=446 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for petsaves
-- ----------------------------
DROP TABLE IF EXISTS `petsaves`;
CREATE TABLE `petsaves` (
  `id` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL,
  `petid1` int(10) NOT NULL default '-1',
  `petid2` int(10) NOT NULL default '-1',
  `petid3` int(10) NOT NULL default '-1',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=251 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for phptitanms_items
-- ----------------------------
DROP TABLE IF EXISTS `phptitanms_items`;
CREATE TABLE `phptitanms_items` (
  `itemid` bigint(9) default '0',
  `itemname` varchar(128) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for phptitanms_jobs
-- ----------------------------
DROP TABLE IF EXISTS `phptitanms_jobs`;
CREATE TABLE `phptitanms_jobs` (
  `id` smallint(6) NOT NULL,
  `job` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for playernpcs
-- ----------------------------
DROP TABLE IF EXISTS `playernpcs`;
CREATE TABLE `playernpcs` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(13) NOT NULL,
  `hair` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `skin` int(11) NOT NULL,
  `dir` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `map` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for playernpcs_equip
-- ----------------------------
DROP TABLE IF EXISTS `playernpcs_equip`;
CREATE TABLE `playernpcs_equip` (
  `id` int(11) NOT NULL auto_increment,
  `npcid` int(11) NOT NULL,
  `equipid` int(11) NOT NULL,
  `equippos` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for questactions
-- ----------------------------
DROP TABLE IF EXISTS `questactions`;
CREATE TABLE `questactions` (
  `questactionid` int(10) unsigned NOT NULL auto_increment,
  `questid` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL default '0',
  `data` blob NOT NULL,
  PRIMARY KEY  (`questactionid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for questrequirements
-- ----------------------------
DROP TABLE IF EXISTS `questrequirements`;
CREATE TABLE `questrequirements` (
  `questrequirementid` int(10) unsigned NOT NULL auto_increment,
  `questid` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL default '0',
  `data` blob NOT NULL,
  PRIMARY KEY  (`questrequirementid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for queststatus
-- ----------------------------
DROP TABLE IF EXISTS `queststatus`;
CREATE TABLE `queststatus` (
  `queststatusid` int(10) unsigned NOT NULL auto_increment,
  `characterid` int(11) NOT NULL default '0',
  `quest` int(11) NOT NULL default '0',
  `status` int(11) NOT NULL default '0',
  `time` int(11) NOT NULL default '0',
  `forfeited` int(11) NOT NULL default '0',
  PRIMARY KEY  (`queststatusid`),
  KEY `characterid` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=103188 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for queststatusmobs
-- ----------------------------
DROP TABLE IF EXISTS `queststatusmobs`;
CREATE TABLE `queststatusmobs` (
  `queststatusmobid` int(10) unsigned NOT NULL auto_increment,
  `queststatusid` int(10) unsigned NOT NULL default '0',
  `mob` int(11) NOT NULL default '0',
  `count` int(11) NOT NULL default '0',
  PRIMARY KEY  (`queststatusmobid`),
  KEY `queststatusid` (`queststatusid`)
) ENGINE=MyISAM AUTO_INCREMENT=14476 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for reactordrops
-- ----------------------------
DROP TABLE IF EXISTS `reactordrops`;
CREATE TABLE `reactordrops` (
  `reactordropid` int(10) unsigned NOT NULL auto_increment,
  `reactorid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL,
  `chance` int(11) NOT NULL,
  PRIMARY KEY  (`reactordropid`),
  KEY `reactorid` (`reactorid`)
) ENGINE=MyISAM AUTO_INCREMENT=113 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for reports
-- ----------------------------
DROP TABLE IF EXISTS `reports`;
CREATE TABLE `reports` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `reporttime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `reporterid` int(11) NOT NULL,
  `victimid` int(11) NOT NULL,
  `reason` tinyint(4) NOT NULL,
  `chatlog` text NOT NULL,
  `status` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for responses
-- ----------------------------
DROP TABLE IF EXISTS `responses`;
CREATE TABLE `responses` (
  `chat` text,
  `response` text,
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for rings
-- ----------------------------
DROP TABLE IF EXISTS `rings`;
CREATE TABLE `rings` (
  `id` int(11) NOT NULL auto_increment,
  `partnerRingId` int(11) NOT NULL default '0',
  `partnerChrId` int(11) NOT NULL default '0',
  `itemid` int(11) NOT NULL default '0',
  `partnername` varchar(255) NOT NULL,
  PRIMARY KEY  USING BTREE (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for savedlocations
-- ----------------------------
DROP TABLE IF EXISTS `savedlocations`;
CREATE TABLE `savedlocations` (
  `id` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL,
  `locationtype` enum('FREE_MARKET','WORLDTOUR','FLORINA','HIDEOUT') default NULL,
  `map` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `savedlocations_ibfk_1` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=109821 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for shopitems
-- ----------------------------
DROP TABLE IF EXISTS `shopitems`;
CREATE TABLE `shopitems` (
  `shopitemid` int(10) unsigned NOT NULL auto_increment,
  `shopid` int(10) unsigned NOT NULL default '0',
  `itemid` int(11) NOT NULL default '0',
  `price` int(11) NOT NULL default '0',
  `position` int(11) NOT NULL default '0',
  PRIMARY KEY  (`shopitemid`),
  KEY `shopid` (`shopid`)
) ENGINE=MyISAM AUTO_INCREMENT=131452241 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops` (
  `shopid` int(10) unsigned NOT NULL auto_increment,
  `npcid` int(11) default '0',
  PRIMARY KEY  (`shopid`)
) ENGINE=MyISAM AUTO_INCREMENT=1314521 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for skillmacros
-- ----------------------------
DROP TABLE IF EXISTS `skillmacros`;
CREATE TABLE `skillmacros` (
  `id` int(11) NOT NULL auto_increment,
  `characterid` int(11) NOT NULL default '0',
  `position` tinyint(1) NOT NULL default '0',
  `skill1` int(11) NOT NULL default '0',
  `skill2` int(11) NOT NULL default '0',
  `skill3` int(11) NOT NULL default '0',
  `name` varchar(13) default NULL,
  `shout` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=68170 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for skills
-- ----------------------------
DROP TABLE IF EXISTS `skills`;
CREATE TABLE `skills` (
  `id` int(11) NOT NULL auto_increment,
  `skillid` int(11) NOT NULL default '0',
  `characterid` int(11) NOT NULL default '0',
  `skilllevel` int(11) NOT NULL default '0',
  `masterlevel` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `skills_ibfk_1` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=8010989 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for spawns
-- ----------------------------
DROP TABLE IF EXISTS `spawns`;
CREATE TABLE `spawns` (
  `id` int(11) NOT NULL auto_increment,
  `idd` int(11) NOT NULL,
  `f` int(11) NOT NULL,
  `fh` int(11) NOT NULL,
  `type` varchar(1) NOT NULL,
  `cy` int(11) NOT NULL,
  `rx0` int(11) NOT NULL,
  `rx1` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `mobtime` int(11) default '1000',
  `mid` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=622 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for storages
-- ----------------------------
DROP TABLE IF EXISTS `storages`;
CREATE TABLE `storages` (
  `storageid` int(10) unsigned NOT NULL auto_increment,
  `accountid` int(11) NOT NULL default '0',
  `slots` int(11) NOT NULL default '0',
  `meso` int(11) NOT NULL default '0',
  PRIMARY KEY  (`storageid`),
  KEY `accountid` (`accountid`)
) ENGINE=MyISAM AUTO_INCREMENT=157 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for wishlist
-- ----------------------------
DROP TABLE IF EXISTS `wishlist`;
CREATE TABLE `wishlist` (
  `id` int(11) NOT NULL auto_increment,
  `charid` int(11) NOT NULL,
  `sn` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for wormphp_items
-- ----------------------------
DROP TABLE IF EXISTS `wormphp_items`;
CREATE TABLE `wormphp_items` (
  `W_itemid` bigint(9) default '0',
  `W_itemname` varchar(128) character set gbk default NULL
) ENGINE=MyISAM DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for wormphp_maps
-- ----------------------------
DROP TABLE IF EXISTS `wormphp_maps`;
CREATE TABLE `wormphp_maps` (
  `W_mapid` bigint(9) default '0',
  `W_mapname` varchar(255) character set gbk default NULL
) ENGINE=MyISAM DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for wormphp_npcs
-- ----------------------------
DROP TABLE IF EXISTS `wormphp_npcs`;
CREATE TABLE `wormphp_npcs` (
  `W_npcid` bigint(9) default '0',
  `W_npcname` varchar(64) character set gbk default NULL
) ENGINE=MyISAM DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for zaksquads
-- ----------------------------
DROP TABLE IF EXISTS `zaksquads`;
CREATE TABLE `zaksquads` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `channel` int(10) unsigned NOT NULL,
  `leaderid` int(10) unsigned NOT NULL default '0',
  `status` int(10) unsigned NOT NULL default '0',
  `members` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
