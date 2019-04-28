Drop database XLR8;
create database XLR8;
USE XLR8;

CREATE TABLE USUARIO(
    nombre VARCHAR (40) NOT NULL,
    apellido_p VARCHAR (40) NOT NULL,
    apellido_m VARCHAR (40) NOT NULL,
    fecha_nac smallint NOT NULL,
    correo VARCHAR (40) NOT NULL UNIQUE,
    sexo VARCHAR (1) not null,
    saldo FLOAT (7) default 0,
    telefono VARCHAR(13)  NOT NULL UNIQUE,
    pass VARCHAR(20)  NOT NULL
);

CREATE TABLE PRODUCTOS (
    codigo_b VARCHAR(13) NOT NULL UNIQUE,
    nombre VARCHAR (40) NOT NULL,
    precio_c FLOAT (7) NOT NULL,
    precio_v FLOAT (7) NOT NULL,
    vuelto FLOAT (7) NOT NULL,
    cantidad int
);

CREATE TABLE TIENDA (
    id_tienda INT NOT NULL UNIQUE,
    nombre VARCHAR (40) NOT NULL,
    latitud_x FLOAT(7) NOT NULL,
    latitud_y FLOAT(7) NOT NULL,
    calif float (1),
    telefono varchar (13),
    admin_tienda varchar (40)
);

CREATE TABLE TICKET (
    id_ticket INT NOT NULL UNIQUE,
    usuario VARCHAR (40),
    calif FLOAT(1),
    tienda INT not null,
    fecha DATE not null default getdate(),
    total FLOAT (7) NOT NULL,
    statusT char (1) default 'X',
    statusU char (1) default 'X',
    vuelto FLOAT (7) NOT NULL,
    tipop char(1)
);


CREATE TABLE COMPRA_TENDERO(
    codigo_b VARCHAR(13) not null,
    tienda INT not null,
    cantidad int not null,
    fechac date not null
);


CREATE TABLE DISPONIBLIDAD (
    codigo_b VARCHAR(13) not null,
    id_tienda int not null,
    cantidad int not null
);

CREATE TABLE COMPRA(
  id_ticket int not null,
  codigo_b varchar (13) not null,
  cantidad int not null
);


create table recompensas (
    id_recompensa int not null identity (1,1),
    nombre varchar (50) not null,
    descripcion text,
    precio float (7),
    cantidad int not null,
);

create table ticket_recomponsa (
    id_recompensa int not null,
    usuario VARCHAR (40),
    cantidad int not null,
    f_compra date
  );


alter table usuario add constraint pk_usuario primary key(correo);
alter table productos add constraint pk_producto primary key (codigo_b);
alter table tienda add constraint pk_tienda primary key (id_tienda);
alter table ticket add constraint pk_ticket primary key (id_ticket);
alter table compra add constraint pk_compra primary key (id_ticket,codigo_b);
alter table DISPONIBLIDAD add constraint pk_disp primary key (codigo_b,id_tienda);

alter table DISPONIBLIDAD add constraint fk_disprod FOREIGN key (codigo_b) references productos(codigo_b);
alter table DISPONIBLIDAD add constraint fk_dispt FOREIGN key (id_tienda) references tienda (id_tienda);
alter table compra add constraint fk_comprod FOREIGN key (codigo_b) references productos(codigo_b);
alter table compra add constraint fk_comprat FOREIGN key (id_ticket) references ticket (id_ticket);



insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('4854930398929' , 'Pan blanco', 20,18,5);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('1505175813777' , 'Coca-Cola 600ml',11,10,1);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('8004204230656' , 'Gansito Marinela',12,11,1);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('4706116949514' , 'Rebanadas Bimbo',5,4,0.5);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('3446928838885' , 'Tortillinas',22,20,2);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('9315921365915' , 'Leche Lala 1lt',19,17,3);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('0393027543805' , 'Tecate Six',90,88,15);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('2425622186465' , 'Churrumais 40gr',5,4.5,0.5);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('6017912754131' , 'Pan Dulce',10,9,1);
insert into PRODUCTOS (codigo_b,nombre,precio_v,precio_c,vuelto) values             ('5348000905741' , 'Leche Hersheys',8,7,1);

INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo,telefono,pass)
VALUES(
    'NAHUM',
    'RODRIGUEZ',
    'CRUZ',
    '1997',
    'nrc545@gmail.com',
    'M',
     0,
    '5537994765',
    '1234'
);


INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo,telefono,pass)
VALUES(
    'DANIELA ALEJANDRA',
    'KAT',
    'RUIZ',
    '1996',
    'DKAT@gmail.com',
    'F',
     0,
    '5589288923',
    '1234'
);


INSERT INTO TIENDA (id_tienda, nombre, latitud_x, latitud_y, calif)
VALUES(
      9096,
      'LA PASADITA',
      19.3231216,
      -99.1417561,
      0
);

INSERT INTO TIENDA (id_tienda, nombre, latitud_x, latitud_y, calif)
VALUES(
      9097,
      'LA CURVA',
      19.3227087,
      -99.1418779,
      0
);


INSERT INTO TIENDA (id_tienda, nombre, latitud_x, latitud_y, calif)
VALUES(
      9098,
      'RICHARD',
      19.32293287,
      -99.1412317,
      0
);

INSERT INTO TIENDA (id_tienda, nombre, latitud_x, latitud_y, calif)
VALUES(
      9099,
      'ZAPATA',
      19.3237039,
      -99.141944,
      0
);

INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('0393027543805',9099,23);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('1505175813777',9099,32);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('2425622186465',9099,13);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('3446928838885',9099,31);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('4706116949514',9099,54);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('4854930398929',9099,43);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('5348000905741',9099,432);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('6017912754131',9099,324);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('8004204230656',9099,43);
INSERT INTO DISPONIBLIDAD(codigo_b,id_tienda,cantidad) VALUES ('9315921365915',9099,43);
