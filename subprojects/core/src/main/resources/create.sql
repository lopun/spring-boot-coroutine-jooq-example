create table shop
(
    id         int auto_increment primary key,
    name       varchar(255) not null,
    url        varchar(255) not null,
    created_at timestamp    not null default CURRENT_TIMESTAMP,
    updated_at timestamp    not null default CURRENT_TIMESTAMP
)

create index shop_name on shop (name)
