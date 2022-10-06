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

CREATE TABLE IF NOT EXISTS dead_letter_entry
(
    dead_letter_id character varying(255) NOT NULL,
    cause_message character varying(255),
    cause_type character varying(255),
    diagnostics oid,
    enqueued_at timestamp without time zone NOT NULL,
    index bigint NOT NULL,
    last_touched timestamp without time zone,
    aggregate_identifier character varying(255),
    event_identifier character varying(255) NOT NULL,
    message_type character varying(255) NOT NULL,
    meta_data oid,
    payload oid NOT NULL,
    payload_revision character varying(255),
    payload_type character varying(255) NOT NULL,
    sequence_number bigint,
    time_stamp character varying(255) NOT NULL,
    token oid,
    token_type character varying(255),
    type character varying(255),
    processing_group character varying(255) NOT NULL,
    processing_started timestamp without time zone,
    sequence_identifier character varying(255) NOT NULL,
    CONSTRAINT dead_letter_entry_pkey PRIMARY KEY (dead_letter_id),
    CONSTRAINT uk5o90gyvtpsxm46giirskcl486 UNIQUE (processing_group, sequence_identifier, index)
);


CREATE INDEX IF NOT EXISTS idxe67wcx5fiq9hl4y4qkhlcj9cg
    ON dead_letter_entry USING btree
        (processing_group ASC NULLS LAST);


CREATE INDEX IF NOT EXISTS idxrwucpgs6sn93ldgoeh2q9k6bn
    ON dead_letter_entry USING btree
        (processing_group ASC NULLS LAST, sequence_identifier ASC NULLS LAST);


CREATE TABLE IF NOT EXISTS domain_event_entry
(
    global_index bigint NOT NULL,
    event_identifier character varying(255) NOT NULL,
    meta_data oid,
    payload oid NOT NULL,
    payload_revision character varying(255),
    payload_type character varying(255) NOT NULL,
    time_stamp character varying(255) NOT NULL,
    aggregate_identifier character varying(255) NOT NULL,
    sequence_number bigint NOT NULL,
    type character varying(255),
    CONSTRAINT domain_event_entry_pkey PRIMARY KEY (global_index),
    CONSTRAINT uk8s1f994p4la2ipb13me2xqm1w UNIQUE (aggregate_identifier, sequence_number),
    CONSTRAINT uk_fwe6lsa8bfo6hyas6ud3m8c7x UNIQUE (event_identifier)
);

CREATE TABLE IF NOT EXISTS snapshot_event_entry
(
    aggregate_identifier character varying(255) NOT NULL,
    sequence_number bigint NOT NULL,
    type character varying(255) NOT NULL,
    event_identifier character varying(255) NOT NULL,
    meta_data oid,
    payload oid NOT NULL,
    payload_revision character varying(255),
    payload_type character varying(255) NOT NULL,
    time_stamp character varying(255) NOT NULL,
    CONSTRAINT snapshot_event_entry_pkey PRIMARY KEY (aggregate_identifier, sequence_number, type),
    CONSTRAINT uk_e1uucjseo68gopmnd0vgdl44h UNIQUE (event_identifier)
);

