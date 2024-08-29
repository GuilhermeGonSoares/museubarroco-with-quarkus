
    alter table if exists church_images 
       drop constraint if exists FK7e1akn1h3j4oufc136lfy3dqh;

    alter table if exists church_images 
       drop constraint if exists FKe52dwumx1xwkfa2fds8wr4bgn;

    alter table if exists churches 
       drop constraint if exists FKivksbjljx2mwpxecj7hh7du29;

    alter table if exists engravings 
       drop constraint if exists FKegvuxiwhswpnv0rc642m84t82;

    alter table if exists painting_images 
       drop constraint if exists FK92thfunrbytapuxylfw0l2scj;

    alter table if exists painting_images 
       drop constraint if exists FKgga7sc0t6deuxefontd977i6l;

    alter table if exists painting_tags 
       drop constraint if exists FKbvbxte7n8xigdq6fmr7h4g2hi;

    alter table if exists painting_tags 
       drop constraint if exists FK6hidi2bemr8gpdao761adnpgq;

    alter table if exists paintings 
       drop constraint if exists FK8bqruf5qhsq22wtja3hkvtrq1;

    alter table if exists paintings 
       drop constraint if exists FK3sbwj77ow6k6ufdesaynrwls1;

    alter table if exists suggestion_images 
       drop constraint if exists FK5kvownl21m4x4gxpt7t7vm4j7;

    alter table if exists suggestion_images 
       drop constraint if exists FK6sar44lry9fb01yk1ugutrq1h;

    alter table if exists suggestions 
       drop constraint if exists FK7yns86oadd04hrdcstg7mhirm;

    alter table if exists suggestions 
       drop constraint if exists FKriyx467shlqlqlytjugugvitp;

    alter table if exists tags_users 
       drop constraint if exists FK9k49ayv342gipvq1kkaiemn8v;

    alter table if exists tags_users 
       drop constraint if exists FKtatu4vslteicxphiivhab026i;

    drop table if exists church_images cascade;

    drop table if exists churches cascade;

    drop table if exists engravings cascade;

    drop table if exists images cascade;

    drop table if exists painting_images cascade;

    drop table if exists painting_tags cascade;

    drop table if exists paintings cascade;

    drop table if exists suggestion_images cascade;

    drop table if exists suggestions cascade;

    drop table if exists tags cascade;

    drop table if exists tags_users cascade;

    drop table if exists users cascade;

    drop sequence if exists churches_seq;

    drop sequence if exists engravings_seq;

    drop sequence if exists images_seq;

    drop sequence if exists paintings_seq;

    drop sequence if exists suggestions_seq;

    drop sequence if exists tags_seq;

    drop sequence if exists users_seq;
