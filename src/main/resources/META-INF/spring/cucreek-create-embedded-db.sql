CREATE TABLE USERROLECODE (ROLECODE VARCHAR(255) NOT NULL, EFFECTIVEDATE TIMESTAMP, ENDDATE TIMESTAMP, ROLEDESCRIPTION VARCHAR(255), VERSION INTEGER, PRIMARY KEY (ROLECODE))
CREATE TABLE HELLOENTITY (HELLOID BIGINT NOT NULL, HELLODATE TIMESTAMP, HELLOTYPECODE VARCHAR(255), SAYSOMETHING VARCHAR(255) NOT NULL, VERSION INTEGER, PRIMARY KEY (HELLOID))
CREATE TABLE USERROLE (USERROLEID BIGINT NOT NULL, VERSION INTEGER, USERACCOUNT_USERACCOUNTID BIGINT, USERROLECODE_ROLECODE VARCHAR(255), PRIMARY KEY (USERROLEID))
CREATE TABLE USERACCOUNT (USERACCOUNTID BIGINT NOT NULL, EMAIL VARCHAR(255), ISSUSPENDED BOOLEAN, PASSWORD VARCHAR(255), USERNAME VARCHAR(255), VERSION INTEGER, PRIMARY KEY (USERACCOUNTID))
ALTER TABLE USERACCOUNT ADD CONSTRAINT UNQ_USERACCOUNT_1 UNIQUE (EMAIL)
ALTER TABLE USERACCOUNT ADD CONSTRAINT UNQ_USERACCOUNT_4 UNIQUE (USERNAME)
ALTER TABLE USERROLE ADD CONSTRAINT FK_USERROLE_USERACCOUNT_USERACCOUNTID FOREIGN KEY (USERACCOUNT_USERACCOUNTID) REFERENCES USERACCOUNT (USERACCOUNTID)
ALTER TABLE USERROLE ADD CONSTRAINT FK_USERROLE_USERROLECODE_ROLECODE FOREIGN KEY (USERROLECODE_ROLECODE) REFERENCES USERROLECODE (ROLECODE)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN_TABLE', 0)
INSERT INTO USERROLECODE(ROLECODE, EFFECTIVEDATE, ENDDATE, ROLEDESCRIPTION, VERSION) VALUES ('ROLE_LOGON', now(), null, 'Logon Role', 1)
INSERT INTO USERACCOUNT (USERACCOUNTID, EMAIL, ISSUSPENDED, PASSWORD, USERNAME, VERSION) VALUES (1,'jason@jljdavidson.com',false, 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f','jason',1)
INSERT INTO USERROLE (USERROLEID, USERROLECODE_ROLECODE, VERSION, USERACCOUNT_USERACCOUNTID) VALUES (1,'ROLE_LOGON',1,1)
