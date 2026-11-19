create table Users (
   user_id int primary key auto_increment,
   username varchar(50) not null unique,
   pass varchar(255) not null
);


create table Drawings (
  drawing_id int primary key auto_increment,
  user_id int not null,
  name varchar(255) not null,
  creation_date timestamp default current_timestamp,
  status enum('public', 'private') not null default 'private',
  deleted_at timestamp null,
  foreign key (user_id) references Users(user_id)
);


create table Drawing_versions (
  version_id int primary key auto_increment,
  drawing_id int not null,
  drawing_data json not null,
  created_at timestamp default current_timestamp,
  foreign key (drawing_id) references Drawings(drawing_id)
      on delete cascade
);


create table Permissions (
     permission_id int primary key auto_increment,
     user_id int not null,
     drawing_id int not null,
     permission_type enum('read', 'write') not null,
     foreign key (user_id) references Users(user_id),
     foreign key (drawing_id) references Drawings(drawing_id) on delete cascade,
     unique (user_id, drawing_id)
);


insert into Users (username, pass) values ('Mod', '1234');