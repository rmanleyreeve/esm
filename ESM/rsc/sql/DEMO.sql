DROP TABLE IF EXISTS APPDATA;
DROP TABLE IF EXISTS ENTRYPOINTS;
DROP TABLE IF EXISTS ENTRYPOINT_COMMENTS;
DROP TABLE IF EXISTS ESM_USERS;
DROP TABLE IF EXISTS LICENSE;
DROP TABLE IF EXISTS SPACES;
DROP TABLE IF EXISTS SPACE_COMMENTS;
DROP TABLE IF EXISTS VESSEL;
DROP TABLE IF EXISTS TABLEIDCOUNTERS;

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
	`LICENSEKEY` VARCHAR(128),
	`VERIFIED_DATE` TIMESTAMP,
	`FALLBACK_KEY` VARCHAR(128) NOT NULL
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
REFERENCES ESM_USERS(ID);
ALTER TABLE SPACES
ADD FOREIGN KEY (VESSEL_NAME) 
REFERENCES VESSEL(NAME);

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
REFERENCES SPACES(ID);
ALTER TABLE ENTRYPOINTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID);

CREATE TABLE SPACE_COMMENTS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE SPACE_COMMENTS
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID);
ALTER TABLE SPACE_COMMENTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID);

CREATE TABLE ENTRYPOINT_COMMENTS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`ENTRYPOINT_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE ENTRYPOINT_COMMENTS
ADD FOREIGN KEY (ENTRYPOINT_ID) 
REFERENCES ENTRYPOINTS(ID);
ALTER TABLE ENTRYPOINT_COMMENTS
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID);

CREATE TABLE APPDATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`KEY` VARCHAR(128) NOT NULL,
	`VALUE` VARCHAR(2000) NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE TABLEIDCOUNTERS (
	`TABLENAME` VARCHAR(100),
	COUNTER INT,
	SKIP INT
);

-- set fallback key for emergency remote activation
INSERT INTO LICENSE (LICENSEKEY,VERIFIED_DATE,FALLBACK_KEY) VALUES (NULL,NULL,'6e22f36f9ef6a522b5a84423ecbc309f');

-- required for entity framework
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('APPDATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ESM_USERS', 0, 1);
-- set these counters to zero for prod
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACES', 4, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINTS', 4, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACE_COMMENTS', 4, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINT_COMMENTS', 4, 1);


-- ============================================================================================
-- sample data for development

INSERT INTO VESSEL (NAME,IMO_NUMBER,TYPE,OWNER,CREATED_DATE) VALUES ('HMS Shiptalk','12345678','Freighter','Shiptalk UK',CURRENT_TIMESTAMP());

INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,JOB_TITLE,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,CREATED_DATE,UPDATE_DATE) 
VALUES 
('admin','pass','John','Smith','Captain','System Administrator','ESM001',9, '1980-08-19',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,JOB_TITLE,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,CREATED_DATE,UPDATE_DATE) 
VALUES 
('user','pass','Fred','Bloggs','Rating','Third Engineer','ESM987',1, '1974-02-11',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

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
VALUES (2,'Chain Locker Door','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (3,'Ballast Tank Lid','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINTS (SPACE_ID,NAME,DESCRIPTION,AUTHOR_ID,CREATED_DATE,UPDATE_DATE)
VALUES (4,'Pipe Tunnel Hatch','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',1,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (1,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (2,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (3,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO SPACE_COMMENTS(SPACE_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (4,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (1,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (2,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (3,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ENTRYPOINT_COMMENTS(ENTRYPOINT_ID,AUTHOR_ID,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES (4,1,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
