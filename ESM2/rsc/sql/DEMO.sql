-- ESM application database demo & testing file
-- rmrdigitalmedia.com 2014

-- cleanup
DROP TABLE IF EXISTS APPDATA;
DROP TABLE IF EXISTS ENTRYPOINTS;
DROP TABLE IF EXISTS ESM_USERS;
DROP TABLE IF EXISTS LICENSE;
DROP TABLE IF EXISTS SPACES;
DROP TABLE IF EXISTS SPACE_COMMENTS;
DROP TABLE IF EXISTS VESSEL_CATEGORIES;
DROP TABLE IF EXISTS VESSEL_TYPES;
DROP TABLE IF EXISTS VESSEL;
DROP TABLE IF EXISTS TABLEIDCOUNTERS;
DROP TABLE IF EXISTS DOC_DATA;
DROP TABLE IF EXISTS PHOTO_DATA;
DROP TABLE IF EXISTS PHOTO_METADATA;
DROP TABLE IF EXISTS SPACE_CHECKLIST_QUESTIONS;
DROP TABLE IF EXISTS SPACE_CHECKLIST_AUDIT;
DROP TABLE IF EXISTS ENTRYPOINT_CHECKLIST_QUESTIONS;
DROP TABLE IF EXISTS ENTRYPOINT_CHECKLIST_AUDIT;
DROP TABLE IF EXISTS SPACE_CLASSIFICATION_QUESTIONS;
DROP TABLE IF EXISTS SPACE_CLASSIFICATION_AUDIT;
DROP TABLE IF EXISTS ENTRYPOINT_CLASSIFICATION_QUESTIONS;
DROP TABLE IF EXISTS ENTRYPOINT_CLASSIFICATION_AUDIT;

-- required by ORM entity framework
CREATE TABLE TABLEIDCOUNTERS (
	`TABLENAME` VARCHAR(100),
	COUNTER INT DEFAULT 0,
	SKIP INT DEFAULT 1
);

-- create tables & relations
CREATE TABLE ESM_USERS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`USERNAME` VARCHAR(32) NOT NULL,
	`PASSWORD` VARCHAR(32) NOT NULL,
	`FORENAME` VARCHAR(64) NOT NULL,
	`SURNAME` VARCHAR(64) NOT NULL,
	`RANK` VARCHAR(128),
	`WORK_IDENTIFIER` VARCHAR(64) NOT NULL,
	`ACCESS_LEVEL` TINYINT DEFAULT 1,
	`DOB` DATE,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);

CREATE TABLE VESSEL_CATEGORIES (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`NAME` VARCHAR(128) NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);

CREATE TABLE VESSEL_TYPES (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`CATEGORY_ID` INT NOT NULL,
	`NAME` VARCHAR(128) NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE VESSEL_TYPES
ADD FOREIGN KEY (CATEGORY_ID) 
REFERENCES VESSEL_CATEGORIES(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE VESSEL (
	`ID` INT NOT NULL DEFAULT 1,
	`NAME` VARCHAR(128) NOT NULL,
	`IMO_NUMBER` VARCHAR(25) NOT NULL,
	`TYPE_ID` INT,
	`OWNER` VARCHAR(128),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	PRIMARY KEY (NAME)
);
ALTER TABLE VESSEL
ADD FOREIGN KEY (TYPE_ID) 
REFERENCES VESSEL_TYPES(ID) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO VESSEL_CATEGORIES (NAME, CREATED_DATE) VALUES ('Vessel', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_CATEGORIES (NAME, CREATED_DATE) VALUES ('Installation', CURRENT_TIMESTAMP());

INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Oil Tanker', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Chemical Tanker', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'LPG Tanker', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'LNG Tanker', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'General Cargo', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Container Ship', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Livestock Carrier', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Bulk Carrier', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Log Carrier', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Car Carrier', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'RORO Ship', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Passenger Ship', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Offshore Support', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Offshore Supply', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Tugs', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Icebreaker', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Research Vessel', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (1, 'Yacht', CURRENT_TIMESTAMP());

INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Fixed Platforms', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Compliant Tower', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Semi-submersible Platform', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Jack-up Drilling Rigs', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Drill Ships', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Floating Production Systems', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Tension Leg Platform', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Gravity-based Structure', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Spar Platform', CURRENT_TIMESTAMP());
INSERT INTO VESSEL_TYPES (CATEGORY_ID, NAME, CREATED_DATE) VALUES (2, 'Condeep Platform', CURRENT_TIMESTAMP());

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
	`SIGNED_OFF` BOOLEAN DEFAULT FALSE,
	`SIGNOFF_ID` INT,
	`SIGNOFF_DATE` TIMESTAMP,
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

CREATE TABLE DOC_DATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`TITLE` VARCHAR(128) NOT NULL,
	`DATA` BLOB NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	PRIMARY KEY (ID)
);
ALTER TABLE DOC_DATA
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE DOC_DATA
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE PHOTO_DATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`DATA_FULL` BLOB NOT NULL,
	`DATA_THUMB` BLOB NOT NULL,
	`CREATED_DATE` TIMESTAMP NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE PHOTO_METADATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`DATA_ID` INT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`AUTHOR_ID` INT NOT NULL,
	`TITLE` VARCHAR(128) NOT NULL,
	`COMMENT` VARCHAR(8000),
	`CREATED_DATE` TIMESTAMP NOT NULL,
	`UPDATE_DATE` TIMESTAMP NOT NULL,
	`APPROVED` BOOLEAN DEFAULT TRUE,
	`DELETED` BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (ID)
);
ALTER TABLE PHOTO_METADATA
ADD FOREIGN KEY (DATA_ID) 
REFERENCES PHOTO_DATA(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE PHOTO_METADATA
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE PHOTO_METADATA
ADD FOREIGN KEY (AUTHOR_ID) 
REFERENCES ESM_USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE APPDATA (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`KEY` VARCHAR(128) NOT NULL,
	`VALUE` VARCHAR(2000) NOT NULL,
	PRIMARY KEY (ID)
);

--- audit tables =============================================================

CREATE TABLE SPACE_CHECKLIST_QUESTIONS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`Q_TEXT` VARCHAR(1000) NOT NULL,
	`Q_HINT` VARCHAR(2000),
	`SEQUENCE`INT NOT NULL,
	PRIMARY KEY (ID)
);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('What are the internal dimensions of the space? (H, W, L metric)','The easiest way to obtain this information is from the ship''s plan. Knowing the dimension of the space helps with the practicalities of movement within the space. i.e. is it low and constricted or large and open. This helps when pre planning the entry.',1);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the enclosed space compartmentalised? Describe internal layout','Many of the spaces onboard are divided into compartments e.g. double bottom tanks. Being aware of these in advance is vital. Look around inside, take photographs, draw pictures, describe the layout.',2);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are internal obstacles present (baffles, pipes etc)?','Identify anything that might obstruct your ability to move through or work in the space. Take  photographs, this helps when planning the entry and travel route',3);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there any restrictive crawl through holes? Please state dimensions','Identify restrictions, take photographs, this is required for planning both entries and rescue procedures. Try to identify if any area within the space is particularly low and constricted, again this helps with the entry and rescue planning. Measure  any restrictive holes from  their widest points e.g. on a lightening hole the measurement is made from top to bottom and side to side not accounting for the radius.',4);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there any pipes running through the space that could contain hazardous liquids or gases?','It is not just the space that you have to be aware of. Pipes running through the space could contain hazardous liquids, or gases and could pose an additional safety risk. If you are not sure discuss with the relevant personnel onboard so that this is known. This is particularly important in case of leakage around a joint which could for example be corrosive .',5);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there electrical cables running through the space?','Be aware of the nature of cabling in the space before carrying out work. If you are not sure what it is or how it might affect operations, discuss with the relevant personnel onboard so that this is known. You do not want to damage cables causing a spark or fire.',6);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are internal vertical ladders present? Rate the condition of these','It is important to rate the condition of these ladders as you have to climb up and down them. For example you should be looking at securing brackets (are they free of corrosion, connected to the bulk head etc.) the uprights and foot rests (corrosion or damage, signs of perish etc.) Take photographs if you can.',7);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do they have staging points/landings? Rate the condition of these','It is important to rate the condition of staging points and landings (look for corrosion or damage, signs of perish etc.)',8);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do they have safety hoops? Rate the condition of these','Look to check they are secured to the ladder and for corrosion or damage, signs of perish etc.)',9);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do they have handrails at landing points? Rate the condition of these','You must take particular care to check that the handrails are in good condition (look to check they are secured to the ladder and for corrosion or damage, signs of perish etc.) Take photographs, remember these are for your safety, if they are damaged or corroded you could fall and be injured.',10);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are internal anchorage points fitted?','Identify and record the position of potential anchorage points (e.g. welded pad eye), you may have to rig a winch system',11);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Does the space contain sloped or curved floors?','If the floor is sloped or curved you may need to rig ropes or ladders for ease of travel, therefore, identifying this in advance helps with the planning stage',12);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is internal lighting fitted into the space?','If not, ensure that this is identified and portable lighting is made available to the entrants.',13);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there any power points present in the space?','Identify the position of the power points as these may be needed e.g. to rig additional lighting. As with electrical cables, if damaged they could create a spark  or fire.',14);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is there potential for communications black spots (steel lined containers)?','Check within the space for ''black spots'' - if you identify some, a secondary means of communication must be put in place.',15);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Would the enclosed space be able to accommodate a stretcher?','You need to know the length and width of your ship''s rescue stretcher and estimate whether or not that stretcher can be taken in and used. There is no point finding this out on an actual rescue situation.',16);
INSERT INTO SPACE_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can someone wearing a BA set move freely within the confined space?','For rescue purposes, you need to know in advance whether or not you can operate in the space wearing a BA, if not alternative arrangements need to be made.',17);

CREATE TABLE SPACE_CHECKLIST_AUDIT (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`Q1_DIMS_H` VARCHAR(12),
	`Q1_DIMS_W` VARCHAR(12),
	`Q1_DIMS_L` VARCHAR(12),
	`Q1_COMMENTS` VARCHAR(2000),
	`Q2_BOOLEAN` VARCHAR(1),
	`Q2_DESC` VARCHAR(2000),
	`Q3_BOOLEAN` VARCHAR(1),
	`Q3_COMMENTS` VARCHAR(2000),
	`Q4_BOOLEAN` VARCHAR(1),
	`Q4_DIMS_H` VARCHAR(12),
	`Q4_DIMS_W` VARCHAR(12),
	`Q4_COMMENTS` VARCHAR(2000),
	`Q5_BOOLEAN` VARCHAR(1),
	`Q5_COMMENTS` VARCHAR(2000),
	`Q6_BOOLEAN` VARCHAR(1),
	`Q6_COMMENTS` VARCHAR(2000),
	`Q7_BOOLEAN` VARCHAR(1),
	`Q7_RATING` INT(1) DEFAULT 0,
	`Q7_COMMENTS` VARCHAR(2000),
	`Q8_BOOLEAN` VARCHAR(1),
	`Q8_RATING` INT(1) DEFAULT 0,
	`Q8_COMMENTS` VARCHAR(2000),
	`Q9_BOOLEAN` VARCHAR(1),
	`Q9_RATING` INT(1) DEFAULT 0,
	`Q9_COMMENTS` VARCHAR(2000),
	`Q10_BOOLEAN` VARCHAR(1),
	`Q10_RATING` INT(1) DEFAULT 0,
	`Q10_COMMENTS` VARCHAR(2000),
	`Q11_BOOLEAN` VARCHAR(1),
	`Q11_COMMENTS` VARCHAR(2000),
	`Q12_BOOLEAN` VARCHAR(1),
	`Q12_COMMENTS` VARCHAR(2000),
	`Q13_BOOLEAN` VARCHAR(1),
	`Q13_COMMENTS` VARCHAR(2000),
	`Q14_BOOLEAN` VARCHAR(1),
	`Q14_COMMENTS` VARCHAR(2000),
	`Q15_BOOLEAN` VARCHAR(1),
	`Q15_COMMENTS` VARCHAR(2000),
	`Q16_BOOLEAN` VARCHAR(1),
	`Q16_COMMENTS` VARCHAR(2000),
	`Q17_BOOLEAN` VARCHAR(1),
	`Q17_COMMENTS` VARCHAR(2000),
	PRIMARY KEY (ID)
);
ALTER TABLE SPACE_CHECKLIST_AUDIT
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE ENTRYPOINT_CHECKLIST_QUESTIONS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`Q_TEXT` VARCHAR(1000) NOT NULL,
	`Q_HINT` VARCHAR(2000),
	`SEQUENCE`INT NOT NULL,
	PRIMARY KEY (ID)
);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the entry point inside or outside?','If the entry point is exposed to weather it can pose an additional hazard to those entering and those standing by.',1);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the outside space protected against the weather?','If the entry point is exposed to weather it can pose an additional hazard to those entering and those standing by.',2);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the entry point clearly marked as an Enclosed Space?','Is there a sign or other mark present that would enable someone to recognise it as an enclosed space?',3);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the entry point: Hinged Hatch/Door/Manhole/Other?','Identifying the type of entry point helps when planning the job. i.e. how long it takes to remove studs etc.',4);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('What is the physical size of the entry point (H,W metric)?','Limited openings prevent ease of entry or rescue therefore knowing in advance again helps with planning the work. Measure the entry point from its widest points e.g. on a lightening hole the measurement is made from top to bottom and side to side not accounting for the radius.',5);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is there anything at the entry point that might interfere with entry?','Look around, are there pipes or girder work impeding entry to the space?',6);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is it a vertical or horizontal entry?','Knowing this helps with the planning stage and identifying equipment which might be required i.e. hoist etc.',7);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there fixed anchorage points available for winching arrangements?','Identify fixed anchorage points (e.g. a welded pad eye) above the space in advance. This helps when setting up winches etc.',8);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can a mobile anchorage point be accommodated?','If no fixed anchorage points are available is there room to put up a tripod/quadpod? If you are not sure if there is sufficient room, check the size of the ship''s tripod/quadpod system and compare. ',9);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can someone enter the space wearing a Breathing Apparatus?','This should be obvious, if you cannot get in wearing a BA you need to re-assess rescue equipment. If you aren''t sure, with the help of a colleague measure the size of a person wearing a BA set and compare with the entry point dimensions.',10);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is there sufficient room to accommodate Entry and Rescue equipment?','This is important! Rescue equipment should be placed in readiness at the point of entry NOT in a store room somewhere! If you aren''t sure, look at the equipment identified in your rescue procedures and consider how easily it could be positioned at the entry point.',11);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is there a fixed communication point nearby (telephone)?','Telephone or tannoy points are usually a clearer and more reliable means of communication than a radio. Is there one in sight of the entry point?',12);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Are there known radio black spots around the confined space?','Check your radio communications at the point of entry. You need to identify black spots in advance as communications are vital during an enclosed space operation.',13);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Is the area around the entry point adequately lit?','Remember that the entry point is also a working area during an enclosed space entry. The entire work area needs to be adequately lit, otherwise portable lighting units may have to be rigged.',14);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can portable Ventilation Machinery be accommodated?','Ventilation machinery is bulky as is air duct, can this be installed at the entry point or elsewhere. If you are not sure, measure your ship''s ventilation equipment and consider how easily it could be positioned at the entry point.',15);
INSERT INTO ENTRYPOINT_CHECKLIST_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Does your company SMS state that the entry point be zoned off to prevent unauthorised access?','Check your company SMS to see what is specified in relation to zoning off e.g. barriers, tape, notices etc.',16);

CREATE TABLE ENTRYPOINT_CHECKLIST_AUDIT (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`ENTRYPOINT_ID` INT NOT NULL,
	`Q1_VALUE` VARCHAR(12),
	`Q1_COMMENTS` VARCHAR(2000),
	`Q2_BOOLEAN` VARCHAR(1),
	`Q2_COMMENTS` VARCHAR(2000),
	`Q3_BOOLEAN` VARCHAR(1),
	`Q3_COMMENTS` VARCHAR(2000),
	`Q4_VALUE` VARCHAR(12),
	`Q4_COMMENTS` VARCHAR(2000),
	`Q5_DIMS_H` VARCHAR(12),
	`Q5_DIMS_W` VARCHAR(12),
	`Q5_COMMENTS` VARCHAR(2000),
	`Q6_BOOLEAN` VARCHAR(1),
	`Q6_COMMENTS` VARCHAR(2000),
	`Q7_VALUE` VARCHAR(12),
	`Q7_COMMENTS` VARCHAR(2000),
	`Q8_BOOLEAN` VARCHAR(1),
	`Q8_COMMENTS` VARCHAR(2000),
	`Q9_BOOLEAN` VARCHAR(1),
	`Q9_COMMENTS` VARCHAR(2000),
	`Q10_BOOLEAN` VARCHAR(1),
	`Q10_COMMENTS` VARCHAR(2000),
	`Q11_BOOLEAN` VARCHAR(1),
	`Q11_COMMENTS` VARCHAR(2000),
	`Q12_BOOLEAN` VARCHAR(1),
	`Q12_COMMENTS` VARCHAR(2000),
	`Q13_BOOLEAN` VARCHAR(1),
	`Q13_COMMENTS` VARCHAR(2000),
	`Q14_BOOLEAN` VARCHAR(1),
	`Q14_COMMENTS` VARCHAR(2000),
	`Q15_BOOLEAN` VARCHAR(1),
	`Q15_COMMENTS` VARCHAR(2000),
	`Q16_BOOLEAN` VARCHAR(1),
	`Q16_COMMENTS` VARCHAR(2000),
	PRIMARY KEY (ID)
);
ALTER TABLE ENTRYPOINT_CHECKLIST_AUDIT
ADD FOREIGN KEY (ENTRYPOINT_ID) 
REFERENCES ENTRYPOINTS(ID) ON DELETE CASCADE ON UPDATE CASCADE;
	

CREATE TABLE SPACE_CLASSIFICATION_QUESTIONS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`Q_TEXT` VARCHAR(1000) NOT NULL,
	`Q_HINT` VARCHAR(2000),
	`SEQUENCE`INT NOT NULL,
	PRIMARY KEY (ID)
);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do height and/or width restrictions within the space make movement: Very difficult, Quite difficult, Not difficult?','Consider the nature and dimensions of the space. How easy is it to move through and work in?',1);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do Internal obstacles (pipes etc) or restrictive crawl ways (lightening holes) within the space make movement: Very difficult, Quite difficult, Not difficult?','Consider the internal obstacles e.g. pipes, cabling, compartments and crawl through holes identified in the audit.',2);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Where internal ladders and stairways are present can a winch be rigged and operated: No, Yes with difficulty, Yes without difficulty?','Consider the ship''s equipment and where it may be needed. Can it be rigged and operated?',3);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('When using radio communications inside the space please rate the signal strength and reception: 1 (poor) to 5 (excellent)','Consider the space overall. If you are not sure check with your colleagues as to what levels of radio communications are acceptable.',4);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Would someone wearing a full Breathing Apparatus find operating in the enclosed space: Very difficult, Quite difficult, Not difficult?','If you aren''t sure, with the help of a colleague measure the size of a person wearing a BA set and compare with the entry point dimensions.',5);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Would a casualty be transported through and removed from the space with: Extreme difficulty, Some difficulty, No difficulty?','In the event that a casualty needs to be transported through the space, can this be done? Have drills been conducted to test this procedure? If not discuss with your colleagues how this could be checked. You don''t want to leave it to find out in a real emergency.',6);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can the contents of the space change? (e.g. cargo hold or store room)?','When carrying certain cargoes, they may give off gases or consume oxygen. Can these pass into other adjacent spaces and affect the breathable air?',7);
INSERT INTO SPACE_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Can the condition of the materials in this space change (e.g. rust)?','In certain circumstances solids may become liquids or liquids may gasify. Check that any changes to materials will not affect the breathable air in adjacent spaces.',8);

CREATE TABLE SPACE_CLASSIFICATION_AUDIT (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`SPACE_ID` INT NOT NULL,
	`Q1_VALUE` INT(1) DEFAULT 0,
	`Q1_COMMENTS` VARCHAR(2000),
	`Q2_VALUE` INT(1) DEFAULT 0,
	`Q2_COMMENTS` VARCHAR(2000),
	`Q3_VALUE` INT(1) DEFAULT 0,
	`Q3_COMMENTS` VARCHAR(2000),
	`Q4_VALUE` INT(1) DEFAULT 0,
	`Q4_COMMENTS` VARCHAR(2000),
	`Q5_VALUE` INT(1) DEFAULT 0,
	`Q5_COMMENTS` VARCHAR(2000),
	`Q6_VALUE` INT(1) DEFAULT 0,
	`Q6_COMMENTS` VARCHAR(2000),
	`Q7_BOOLEAN` VARCHAR(1),
	`Q7_COMMENTS` VARCHAR(2000),
	`Q8_BOOLEAN` VARCHAR(1),
	`Q8_COMMENTS` VARCHAR(2000),
	PRIMARY KEY (ID)
);
ALTER TABLE SPACE_CLASSIFICATION_AUDIT
ADD FOREIGN KEY (SPACE_ID) 
REFERENCES SPACES(ID) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE ENTRYPOINT_CLASSIFICATION_QUESTIONS (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`Q_TEXT` VARCHAR(1000) NOT NULL,
	`Q_HINT` VARCHAR(2000),
	`SEQUENCE`INT NOT NULL,
	PRIMARY KEY (ID)
);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Do the physical dimensions and shape of the entry point make entering the enclosed space: Very difficult, Quite difficult, Not difficult?','Consider the location, angle of entry and any obstructions as identified in the audit.',1);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Relating to vertical entries, can a winch be rigged and operated: No, Yes with difficulty, Yes without difficulty','Consider the location of the entry point, is there either a fixed point for anchoring a winch above the space or enough room to erect a temporary fixture such as the ship''s tripod or quadpod?',2);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('In relation to casualty evacuation from the space. is there sufficient room for a stretcher to be able to be lifted vertically or removed horizontally from the space?','In the event that a casualty needs to be lifted from the space, could a stretcher be used vertically or horizontally? Have drills been conducted to test this procedure? If not, discuss with your colleagues how this could be checked. You don''t want to leave it to find out in a real emergency.',3);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('When using radio communications inside the space please rate the signal strength and reception: 1 (poor) to 5 (excellent)','Consider the space overall. If you are not sure, check with your colleagues as to what levels of radio communications are acceptable.',4);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('According to your company''s SMS, is provision made for an enclosed space rescue team at site?','If you do not know, take the time to check your company SMS and see what provision is made for rescue teams.',5);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('According to your company''s SMS, is provision made for emergency equipment  at site?','If you do not know, take the time to check your company SMS and see what provision is made for rescue equipment.',6);
INSERT INTO ENTRYPOINT_CLASSIFICATION_QUESTIONS (Q_TEXT,Q_HINT,SEQUENCE) VALUES ('Would someone wearing a full Breathing Apparatus find entering the enclosed space via the entry point: Very difficult, Quite difficult, Not difficult?','If you aren''t sure, with the help of a colleague measure the size of a person wearing a BA set and compare with the entry point dimensions.',7);

CREATE TABLE ENTRYPOINT_CLASSIFICATION_AUDIT (
	`ID` INT AUTO_INCREMENT NOT NULL,
	`ENTRYPOINT_ID` INT NOT NULL,
	`Q1_VALUE` INT(1) DEFAULT 0,
	`Q1_COMMENTS` VARCHAR(2000),
	`Q2_VALUE` INT(1) DEFAULT 0,
	`Q2_COMMENTS` VARCHAR(2000),
	`Q3_BOOLEAN` VARCHAR(1),
	`Q3_COMMENTS` VARCHAR(2000),
	`Q4_VALUE` INT(1) DEFAULT 0,
	`Q4_COMMENTS` VARCHAR(2000),
	`Q5_BOOLEAN` VARCHAR(1),
	`Q5_COMMENTS` VARCHAR(2000),
	`Q6_BOOLEAN` VARCHAR(1),
	`Q6_COMMENTS` VARCHAR(2000),
	`Q7_VALUE` INT(1) DEFAULT 0,
	`Q7_COMMENTS` VARCHAR(2000),
	PRIMARY KEY (ID)
);
ALTER TABLE ENTRYPOINT_CLASSIFICATION_AUDIT
ADD FOREIGN KEY (ENTRYPOINT_ID) 
REFERENCES ENTRYPOINTS(ID) ON DELETE CASCADE ON UPDATE CASCADE;

-- required for ORM entity framework
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('APPDATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('DOC_DATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('PHOTO_DATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('PHOTO_METADATA', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACE_CHECKLIST_AUDIT', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACE_CLASSIFICATION_AUDIT', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINT_CHECKLIST_AUDIT', 0, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINT_CLASSIFICATION_AUDIT', 0, 1);

-- set these counters to match number of demo inserts below
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('VESSEL', 1, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ESM_USERS', 3, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACES', 4, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('ENTRYPOINTS', 6, 1);
INSERT INTO TABLEIDCOUNTERS (TABLENAME, COUNTER, SKIP) VALUES ('SPACE_COMMENTS', 8, 1);


-- ============================================================================================
-- sample data for development

INSERT INTO VESSEL (ID,NAME,IMO_NUMBER,TYPE_ID,OWNER,CREATED_DATE, UPDATE_DATE) VALUES (1,'HMS Shiptalk','12345678',1,'Shiptalk UK',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('admin','pass','John','Smith','Captain','ESM001',9, '1980-08-19','Admin user',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('user','pass','Fred','Bloggs','Rating','ESM123',1, '1974-02-11','Basic user - comments etc need approval',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO ESM_USERS
(USERNAME,PASSWORD,FORENAME,SURNAME,RANK,WORK_IDENTIFIER,ACCESS_LEVEL,DOB,COMMENT,CREATED_DATE,UPDATE_DATE) 
VALUES 
('user2','pass','Jack','Sparrow','Seaman','ESM987',2, '1984-01-01','Basic user level 2 - comments are automatically approved',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

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




