
    create sequence churches_seq start with 1 increment by 50;

    create sequence engravings_seq start with 1 increment by 50;

    create sequence images_seq start with 1 increment by 50;

    create sequence paintings_seq start with 1 increment by 50;

    create sequence tags_seq start with 1 increment by 50;

    create sequence users_seq start with 1 increment by 50;

    create table church_images (
        church_id bigint not null,
        image_id bigint not null unique
    );

    create table churches (
        is_published boolean not null,
        id bigint not null,
        registered_by bigint,
        bibliography_references varchar(255),
        city varchar(255),
        description varchar(255),
        name varchar(255) not null,
        state varchar(255),
        street varchar(255),
        primary key (id)
    );

    create table engravings (
        id bigint not null,
        painting_id bigint,
        created_by varchar(255),
        name varchar(255) not null,
        url varchar(255) not null,
        primary key (id)
    );

    create table images (
        id bigint not null,
        photographer varchar(255),
        type varchar(255),
        url varchar(255),
        primary key (id)
    );

    create table painting_images (
        image_id bigint not null unique,
        painting_id bigint not null
    );

    create table painting_tags (
        painting_id bigint not null,
        tag_id bigint not null,
        primary key (painting_id, tag_id)
    );

    create table paintings (
        is_published boolean not null,
        church_id bigint,
        id bigint not null,
        registered_by bigint,
        artisan varchar(255),
        bibliography_reference varchar(255),
        bibliography_source varchar(255),
        date_of_creation varchar(255),
        description varchar(255),
        placement varchar(255),
        title varchar(255) not null,
        primary key (id)
    );

    create table tags (
        is_published boolean not null,
        id bigint not null,
        user_id bigint,
        name varchar(255),
        primary key (id)
    );

    create table users (
        is_admin boolean not null,
        id bigint not null,
        email varchar(255),
        name varchar(255),
        password varchar(255),
        primary key (id)
    );

    alter table if exists church_images 
       add constraint FK7e1akn1h3j4oufc136lfy3dqh 
       foreign key (image_id) 
       references images;

    alter table if exists church_images 
       add constraint FKe52dwumx1xwkfa2fds8wr4bgn 
       foreign key (church_id) 
       references churches;

    alter table if exists churches 
       add constraint FKivksbjljx2mwpxecj7hh7du29 
       foreign key (registered_by) 
       references users;

    alter table if exists engravings 
       add constraint FKegvuxiwhswpnv0rc642m84t82 
       foreign key (painting_id) 
       references paintings;

    alter table if exists painting_images 
       add constraint FK92thfunrbytapuxylfw0l2scj 
       foreign key (image_id) 
       references images;

    alter table if exists painting_images 
       add constraint FKgga7sc0t6deuxefontd977i6l 
       foreign key (painting_id) 
       references paintings;

    alter table if exists painting_tags 
       add constraint FKbvbxte7n8xigdq6fmr7h4g2hi 
       foreign key (tag_id) 
       references tags;

    alter table if exists painting_tags 
       add constraint FK6hidi2bemr8gpdao761adnpgq 
       foreign key (painting_id) 
       references paintings;

    alter table if exists paintings 
       add constraint FK8bqruf5qhsq22wtja3hkvtrq1 
       foreign key (church_id) 
       references churches;

    alter table if exists paintings 
       add constraint FK3sbwj77ow6k6ufdesaynrwls1 
       foreign key (registered_by) 
       references users;

    alter table if exists tags 
       add constraint FKpsynysaxl7cyw8mr5c8xevneg 
       foreign key (user_id) 
       references users;
