create sequence hibernate_sequence;


create table if not exists association_value_entry
(
	id bigint not null
		constraint association_value_entry_pkey
			primary key,
	association_key varchar(255) not null,
	association_value varchar(255),
	saga_id varchar(255) not null,
	saga_type varchar(255)
);


create index if not exists idxk45eqnxkgd8hpdn6xixn8sgft
	on association_value_entry (saga_type, association_key, association_value);

create index if not exists idxgv5k1v2mh6frxuy5c0hgbau94
	on association_value_entry (saga_id, saga_type);

create table if not exists cmd_courier_entity
(
	id varchar(255) not null
		constraint cmd_courier_entity_pkey
			primary key,
	max_number_of_active_orders integer,
	number_of_active_orders integer
);


create table if not exists courier_entity
(
	id varchar(255) not null
		constraint courier_entity_pkey
			primary key,
	first_name varchar(255),
	last_name varchar(255),
	max_number_of_active_orders integer,
	number_of_active_orders integer
);


create table if not exists saga_entry
(
	saga_id varchar(255) not null
		constraint saga_entry_pkey
			primary key,
	revision varchar(255),
	saga_type varchar(255),
	serialized_saga oid
);


create table if not exists shipment_entity
(
	id varchar(255) not null
		constraint shipment_entity_pkey
			primary key,
	address varchar(255),
	courier_id varchar(255),
	state integer
);


create table if not exists token_entry
(
	processor_name varchar(255) not null,
	segment integer not null,
	owner varchar(255),
	timestamp varchar(255) not null,
	token oid,
	token_type varchar(255),
	constraint token_entry_pkey
		primary key (processor_name, segment)
);

