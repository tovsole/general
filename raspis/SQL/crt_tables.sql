CREATE TABLE trains (
  ID number(11) NOT NULL ;--COMMENT 'ІД поїзду';
  NUM_TRAIN varchar2(45)  NOT NULL ;--COMMENT 'Номер поїзду';
  NUM_EXPRESS varchar2(45)  DEFAULT NULL ;--COMMENT 'Номер експресу';
  ST1 number(11) NOT NULL ;--COMMENT 'Початкова станція';
  ST2 number(11) NOT NULL ;--COMMENT 'Кінцева станція';
  MNAME_U varchar2(200)  DEFAULT NULL ;--COMMENT 'Назва маршрута (українською)';
  MNAME_R varchar2(200)  DEFAULT NULL ;--COMMENT 'Назва маршрута (російською)';
  FNAME varchar2(100)  DEFAULT NULL ;--COMMENT 'Фірмова назва потягу';
  PERIOD_U varchar2(200)  DEFAULT NULL ;--COMMENT 'Періодичність руху (українською)';
  PERIOD_R varchar2(200)  DEFAULT NULL ;--COMMENT 'Періодичність руху (російською)';
  MOVE_TIME number(11) DEFAULT NULL ;--COMMENT 'Час у дорозі';
  MOVE_STAND number(11) DEFAULT NULL ;--COMMENT 'Час стояння :)';
  COMENT varchar2(400)  DEFAULT NULL ;--COMMENT 'Коментар';
  PERIOD_B varchar2(366)  DEFAULT NULL ;--COMMENT 'Разпарсенная периодичность';
  precep number(1) NOT NULL ;--COMMENT '1 - прицепной вагон ; 0 - не прицепной вагон';
  PRIMARY KEY (ID)
); 

CREATE TABLE  routes (
  ID number(11) NOT NULL  ;--COMMENT 'ІД маршруту';
  TRAIN_ID number(11) NOT NULL ;--COMMENT 'ІД поїзда';
  NUM number(11) NOT NULL ;--COMMENT 'Номер станції п.п.';
  ST number(11) NOT NULL ;--COMMENT 'ІД станції';
  ARR_TIME number(11) NOT NULL ;--COMMENT 'Час прибуття';
  DEP_TIME number(11) NOT NULL ;--COMMENT 'Час відправлення';
  OZN_TECH number(11) DEFAULT NULL ;--COMMENT 'Ознака "технічна стоянка"';
  DIST number(11) DEFAULT NULL ;--COMMENT 'Відстань від початку маршруту';
  ARR_TIME_ACT number(11) NOT NULL ;--COMMENT 'Час прибуття (у хвилинах)';
  DEP_TIME_ACT number(11) NOT NULL ;--COMMENT 'Час відправлення (у хвилинах)';
  PRIMARY KEY (ID)
);


CREATE TABLE stations (
  st1 number(5) NOT NULL ;--COMMENT 'Код станции 1';
  st2 number(9) DEFAULT NULL ;--COMMENT 'Код станции 2';
  nameu varchar2(45)  DEFAULT NULL ;--COMMENT 'Украинское название станции';
  namer varchar2(45)  DEFAULT NULL ;--COMMENT 'Русское название станции';
  namee varchar2(45)  DEFAULT NULL ;--COMMENT 'Английское название станции';
  rating number(9) DEFAULT NULL ;--COMMENT '????????????
  d1 number(9) DEFAULT NULL ;--COMMENT 'Код железной дорогои';
  PRIMARY KEY (st1)
  );
  
alter table TRAINS rename column coment to "COMMENT";  
  
comment on column TRAINS.ID is 'ІД поїзду';
comment on column TRAINS.NUM_TRAIN is 'Номер поїзду';
comment on column TRAINS.NUM_EXPRESS is 'Номер експресу';
comment on column TRAINS.ST1 is 'Початкова станція';
comment on column TRAINS.ST2 is 'Кінцева станція';
comment on column TRAINS.MNAME_U is 'Назва маршрута (українською)';
comment on column TRAINS.MNAME_R  is   'Назва маршрута (російською)';
comment on column TRAINS.FNAME  is  'Фірмова назва потягу';
comment on column TRAINS.PERIOD_U is 'Періодичність руху (українською)';
comment on column TRAINS.PERIOD_R  is 'Періодичність руху (російською)';
comment on column TRAINS.MOVE_TIME is  'Час у дорозі';
comment on column TRAINS.MOVE_STAND  is  'Час стояння :)';
comment on column TRAINS.COMENT  is  'Коментар';
comment on column TRAINS.PERIOD_B  is  'Разпарсенная периодичность';
comment on column TRAINS.precep  is  '1 - прицепной вагон ; 0 - не прицепной вагон';


comment on column routes.ID is   'ІД маршруту';
comment on column routes.TRAIN_ID is  'ІД поїзда';
comment on column routes.NUM is  'Номер станції п.п.';
comment on column routes.ST  is 'ІД станції';
comment on column routes.ARR_TIME  is  'Час прибуття';
comment on column routes.DEP_TIME  is 'Час відправлення';
comment on column routes.OZN_TECH  is  'Ознака "технічна стоянка"';
comment on column routes.DIST  is  'Відстань від початку маршруту';
comment on column routes.ARR_TIME_ACT  is  'Час прибуття (у хвилинах)';
comment on column routes.DEP_TIME_ACT  is  'Час відправлення (у хвилинах)';

comment on column stations.st1  is 'Код станции 1';
comment on column stations.st2  is  'Код станции 2';
comment on column stations.nameu  is  'Украинское название станции';
comment on column stations.namer  is  'Русское название станции';
comment on column stations.namee  is  'Английское название станции';
comment on column stations.d1 is 'Код железной дорогои';

