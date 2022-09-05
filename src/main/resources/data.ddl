create table users(
    userid int not null auto_increment,
    username varchar_ignorecase(50) not null ,
    password varchar_ignorecase(500) not null,
    enabled boolean not null,
    primary key (userid)
);



create table user_movie(
                           userid int not null,
                           movieid int not null,
                           favorite boolean,
                           personal_rating int,
                           notes varchar(255),
    foreign key (userid) references users(userid)
    );

