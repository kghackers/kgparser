-- Соревнование
create table Competition
(
	  id                     integer not null
	, name                   varchar(255) not null
	, link                   varchar(255)
	, comment                varchar(255)

	, zip_file_data          blob
	, zip_file_name          varchar(255) not null
	, zip_file_size          integer not null
	, zip_file_content_type  varchar(255) not null

	, competition_json       blob

	, primary key(id)
);
create generator competition_gen;
set generator competition_gen to 0;

create index competition_name_idx on Competition(name);