-- cleanup
DROP TABLE IF EXISTS APPDATA;
DROP TABLE IF EXISTS ENTRYPOINTS;
DROP TABLE IF EXISTS ENTRYPOINT_COMMENTS;
DROP TABLE IF EXISTS ESM_USERS;
DROP TABLE IF EXISTS LICENSE;
DROP TABLE IF EXISTS SPACES;
DROP TABLE IF EXISTS SPACE_COMMENTS;
DROP TABLE IF EXISTS VESSEL;
DROP TABLE IF EXISTS TABLEIDCOUNTERS;
DROP TABLE IF EXISTS PHOTO_METADATA;

-- create tables & relations
CREATE TABLE ESM_USERS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`USERNAME` VARCHAR(32) NOT NULL,
	`PASSWORD` VARCHAR(32) NOT NULL,
	`FORENAME` VARCHAR(64) NOT NULL,
	`SURNAME` VARCHAR(64) NOT NULL,
	`RANK` VARCHAR(64),
	`JOB_TITLE` VARCHAR(128),
	`WORK_IDENTIFIER` VARCHAR(64) NOT NULL,
	`ACCESS_LEVEL` TINYINT DEFAULT 1,
	`DOB` DATE,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);

CREATE TABLE VESSEL (
	`NAME` VARCHAR(128) NOT NULL,
	`IMO_NUMBER` VARCHAR(25) NOT NULL,
	`TYPE` VARCHAR(64),
	`OWNER` VARCHAR(128),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	PRIMARY KEY (NAME)
);

CREATE TABLE LICENSE (
	`LICENSEKEY` VARCHAR(20),
	`VERIFIED_DATE` TIMESTAMP
);

CREATE TABLE SPACES (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`VESSEL_NAME` VARCHAR(128) NOT NULL,
	`NAME` VARCHAR(256) NOT NULL,
	`DESCRIPTION` VARCHAR(4000),
	`AUTHOR_ID` INT NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);

ALTER TABLE SPACES
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE SPACES
ADD FOREIGN KEY (VESSEL_NAME) 
REFERENCES VESSEL(NAME) ON UPDATE CASCADE;

CREATE TABLE ENTRYPOINTS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`NAME` VARCHAR(256) NOT NULL,
	`DESCRIPTION` VARCHAR(4000),
	`AUTHOR_ID` INT NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE ENTRYPOINTS
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE ENTRYPOINTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE SPACE_COMMENTS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`APPROVED` BOOLEAN DEFAULT TRUE,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE SPACE_COMMENTS
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE SPACE_COMMENTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE PHOTO_METADATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`TITLE` VARCHAR(128) NOT NULL,
	`PATH` VARCHAR(256) NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`APPROVED` BOOLEAN DEFAULT TRUE,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE PHOTO_METADATA
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE PHOTO_METADATA
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE ENTRYPOINT_COMMENTS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`ENTRYPOINT_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`APPROVED` BOOLEAN DEFAULT TRUE,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE ENTRYPOINT_COMMENTS
ADD FOREIGN KEY (ENTRYPOINT_ID) 
REFERENCES ENTRYPOINTS(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE ENTRYPOINT_COMMENTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE APPDATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`KEY` VARCHAR(128) NOT NULL,
	`VALUE` VARCHAR(2000) NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE TABLEIDCOUNTERS (
	`TABLENAME` VARCHAR(100),
	COUNTER INT DEFAULT 0,
	SKIP INT DEFAULT 1
);


-- required for entity framework
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('APPDATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ESM_USERS', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('PHOTO_METADATA', 0, 1);
-- set these counters to match number of inserts below
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACES', 4, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACE_COMMENTS', 8, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINTS', 6, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINT_COMMENTS', 8, 1);


-- ============================================================================================
-- sample data for development

INSERT INTO VESSEL (NAME,IMO_NUMBER,TYPE,OWNER,CREATED_DATE) VALUES ('HMS Shiptalk','12345678','Freighter','Shiptalk UK',CURRENT_TIMESTAMP());

INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,JOB_TITLE,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('admin','pass','John','Smith','Captain','System Administrator','ESM001',9, '1980-08-19','Admin user',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,JOB_TITLE,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('user','pass','Fred','Bloggs','Rating','Third Engineer','ESM123',1, '1974-02-11','Basic user - comments etc need approval',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,JOB_TITLE,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('user2','pass','Jack','Sparrow','Seaman','First Engineer','ESM987',2, '1984-01-01','Basic user level 2 - comments are automatically approved',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

INSERT INTO SPACES (VESSEL_NAME,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES ((SELECT TOP 1 NAME FROM VESSEL),'Forepeak Tank','The extreme forward lower compartment or tank usually used for trimming or storage in a ship',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACES (VESSEL_NAME,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES ((SELECT TOP 1 NAME FROM VESSEL),'Chain Locker','A forward compartment in the lower part of a ship for stowing the chain cable',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACES (VESSEL_NAME,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES ((SELECT TOP 1 NAME FROM VESSEL),'Ballast Tank','a tank or compartment either at the bottom of a ship or on the sides, which is filled with liquids for stability of the ship',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACES (VESSEL_NAME,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES ((SELECT TOP 1 NAME FROM VESSEL),'Pipe Tunnel','An underground or underwater passage.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (1,'Forepeak Tank Hatch','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (1,'Forepeak Tank Ladder','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (1,'Forepeak Tank Door','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (2,'Chain Locker Door','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (3,'Ballast Tank Lid','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (4,'Pipe Tunnel Hatch','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (1,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (1,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),FALSE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (2,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (2,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),FALSE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (3,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (3,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (4,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (4,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);

INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (1,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (1,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),FALSE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (2,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (2,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (3,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (3,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (4,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),TRUE);
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE,APPROVED) 
VALUES (4,2,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),FALSE);

