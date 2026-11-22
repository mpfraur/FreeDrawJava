
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
      status enum('PUBLIC', 'PRIVATE') not null default 'PRIVATE',
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
     permission_type enum('READ', 'WRITE') not null,
     foreign key (user_id) references Users(user_id),
     foreign key (drawing_id) references Drawings(drawing_id) on delete cascade,
     unique (user_id, drawing_id)
);

-- Mock data

-- users
insert into Users (username, pass)
values
    ('Mod', '1234'),
    ('Admin1', '1234');

-- drawings owned by Mod
insert into Drawings (user_id, name, status)
values
    ((select user_id from Users where username = 'Mod'), 'Private Drawing', 'PRIVATE'),
    ((select user_id from Users where username = 'Mod'), 'Public Drawing', 'PUBLIC'),
    ((select user_id from Users where username = 'Mod'), 'Deleted Drawing', 'PRIVATE');

-- soft delete
update Drawings
set deleted_at = current_timestamp
where name = 'Deleted Drawing';

-- versions
insert into Drawing_versions (drawing_id, drawing_data)
values
    ((select drawing_id from Drawings where name = 'Private Drawing'),
     '[{"color":"#ff0000","lineWidth":10,"position":{"x":200,"y":150},"shape":"square","isFilled":false}]'),

    ((select drawing_id from Drawings where name = 'Private Drawing'),
     '[{"color":"#0000ff","lineWidth":5,"position":{"x":350,"y":250},"shape":"circle","isFilled":true}]'),

    ((select drawing_id from Drawings where name = 'Public Drawing'),
     '[{"color":"#863bff","lineWidth":8,"position":{"x":450,"y":300},"shape":"triangle","isFilled":false}]');

-- Admin1 permissions on Mod's drawings
insert into Permissions (user_id, drawing_id, permission_type)
values
    ((select user_id from Users where username = 'Admin1'),
     (select drawing_id from Drawings where name = 'Private Drawing'), 'READ'),

    ((select user_id from Users where username = 'Admin1'),
     (select drawing_id from Drawings where name = 'Public Drawing'), 'WRITE');
