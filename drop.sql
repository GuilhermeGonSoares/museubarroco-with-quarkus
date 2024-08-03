
    alter table church_images 
       drop constraint FK7e1akn1h3j4oufc136lfy3dqh;

    alter table church_images 
       drop constraint FKe52dwumx1xwkfa2fds8wr4bgn;

    alter table churches 
       drop constraint FKivksbjljx2mwpxecj7hh7du29;

    alter table engravings 
       drop constraint FKegvuxiwhswpnv0rc642m84t82;

    alter table painting_images 
       drop constraint FK92thfunrbytapuxylfw0l2scj;

    alter table painting_images 
       drop constraint FKgga7sc0t6deuxefontd977i6l;

    alter table painting_tags 
       drop constraint FKbvbxte7n8xigdq6fmr7h4g2hi;

    alter table painting_tags 
       drop constraint FK6hidi2bemr8gpdao761adnpgq;

    alter table paintings 
       drop constraint FK8bqruf5qhsq22wtja3hkvtrq1;

    alter table paintings 
       drop constraint FK3sbwj77ow6k6ufdesaynrwls1;

    alter table tags 
       drop constraint FKpsynysaxl7cyw8mr5c8xevneg;

    drop table church_images;

    drop table churches;

    drop table engravings;

    drop table images;

    drop table painting_images;

    drop table painting_tags;

    drop table paintings;

    drop table tags;

    drop table users;

    drop sequence churches_seq;

    drop sequence engravings_seq;

    drop sequence images_seq;

    drop sequence paintings_seq;

    drop sequence tags_seq;

    drop sequence users_seq;
