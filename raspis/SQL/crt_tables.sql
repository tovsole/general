CREATE TABLE trains (
  ID number(11) NOT NULL ;--COMMENT '�� �����';
  NUM_TRAIN varchar2(45)  NOT NULL ;--COMMENT '����� �����';
  NUM_EXPRESS varchar2(45)  DEFAULT NULL ;--COMMENT '����� ��������';
  ST1 number(11) NOT NULL ;--COMMENT '��������� �������';
  ST2 number(11) NOT NULL ;--COMMENT 'ʳ����� �������';
  MNAME_U varchar2(200)  DEFAULT NULL ;--COMMENT '����� �������� (����������)';
  MNAME_R varchar2(200)  DEFAULT NULL ;--COMMENT '����� �������� (���������)';
  FNAME varchar2(100)  DEFAULT NULL ;--COMMENT 'Գ����� ����� ������';
  PERIOD_U varchar2(200)  DEFAULT NULL ;--COMMENT '����������� ���� (����������)';
  PERIOD_R varchar2(200)  DEFAULT NULL ;--COMMENT '����������� ���� (���������)';
  MOVE_TIME number(11) DEFAULT NULL ;--COMMENT '��� � �����';
  MOVE_STAND number(11) DEFAULT NULL ;--COMMENT '��� ������� :)';
  COMENT varchar2(400)  DEFAULT NULL ;--COMMENT '��������';
  PERIOD_B varchar2(366)  DEFAULT NULL ;--COMMENT '������������ �������������';
  precep number(1) NOT NULL ;--COMMENT '1 - ��������� ����� ; 0 - �� ��������� �����';
  PRIMARY KEY (ID)
); 

CREATE TABLE  routes (
  ID number(11) NOT NULL  ;--COMMENT '�� ��������';
  TRAIN_ID number(11) NOT NULL ;--COMMENT '�� �����';
  NUM number(11) NOT NULL ;--COMMENT '����� ������� �.�.';
  ST number(11) NOT NULL ;--COMMENT '�� �������';
  ARR_TIME number(11) NOT NULL ;--COMMENT '��� ��������';
  DEP_TIME number(11) NOT NULL ;--COMMENT '��� �����������';
  OZN_TECH number(11) DEFAULT NULL ;--COMMENT '������ "������� �������"';
  DIST number(11) DEFAULT NULL ;--COMMENT '³������ �� ������� ��������';
  ARR_TIME_ACT number(11) NOT NULL ;--COMMENT '��� �������� (� ��������)';
  DEP_TIME_ACT number(11) NOT NULL ;--COMMENT '��� ����������� (� ��������)';
  PRIMARY KEY (ID)
);


CREATE TABLE stations (
  st1 number(5) NOT NULL ;--COMMENT '��� ������� 1';
  st2 number(9) DEFAULT NULL ;--COMMENT '��� ������� 2';
  nameu varchar2(45)  DEFAULT NULL ;--COMMENT '���������� �������� �������';
  namer varchar2(45)  DEFAULT NULL ;--COMMENT '������� �������� �������';
  namee varchar2(45)  DEFAULT NULL ;--COMMENT '���������� �������� �������';
  rating number(9) DEFAULT NULL ;--COMMENT '????????????
  d1 number(9) DEFAULT NULL ;--COMMENT '��� �������� �������';
  PRIMARY KEY (st1)
  );
  
alter table TRAINS rename column coment to "COMMENT";  
  
comment on column TRAINS.ID is '�� �����';
comment on column TRAINS.NUM_TRAIN is '����� �����';
comment on column TRAINS.NUM_EXPRESS is '����� ��������';
comment on column TRAINS.ST1 is '��������� �������';
comment on column TRAINS.ST2 is 'ʳ����� �������';
comment on column TRAINS.MNAME_U is '����� �������� (����������)';
comment on column TRAINS.MNAME_R  is   '����� �������� (���������)';
comment on column TRAINS.FNAME  is  'Գ����� ����� ������';
comment on column TRAINS.PERIOD_U is '����������� ���� (����������)';
comment on column TRAINS.PERIOD_R  is '����������� ���� (���������)';
comment on column TRAINS.MOVE_TIME is  '��� � �����';
comment on column TRAINS.MOVE_STAND  is  '��� ������� :)';
comment on column TRAINS.COMENT  is  '��������';
comment on column TRAINS.PERIOD_B  is  '������������ �������������';
comment on column TRAINS.precep  is  '1 - ��������� ����� ; 0 - �� ��������� �����';


comment on column routes.ID is   '�� ��������';
comment on column routes.TRAIN_ID is  '�� �����';
comment on column routes.NUM is  '����� ������� �.�.';
comment on column routes.ST  is '�� �������';
comment on column routes.ARR_TIME  is  '��� ��������';
comment on column routes.DEP_TIME  is '��� �����������';
comment on column routes.OZN_TECH  is  '������ "������� �������"';
comment on column routes.DIST  is  '³������ �� ������� ��������';
comment on column routes.ARR_TIME_ACT  is  '��� �������� (� ��������)';
comment on column routes.DEP_TIME_ACT  is  '��� ����������� (� ��������)';

comment on column stations.st1  is '��� ������� 1';
comment on column stations.st2  is  '��� ������� 2';
comment on column stations.nameu  is  '���������� �������� �������';
comment on column stations.namer  is  '������� �������� �������';
comment on column stations.namee  is  '���������� �������� �������';
comment on column stations.d1 is '��� �������� �������';

