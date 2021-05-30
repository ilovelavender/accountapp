create table accounts (
    id          serial                       not null,
    created_at  timestamp without time zone  not null,
    number      varchar(20)                  not null,
    balance     numeric(40, 2)               not null default 0,

    constraint pk_accounts primary key (id),
    constraint uk_accounts_number unique (number)
);

create table transactions (
    id                serial                       not null,
    txn_id            varchar(32)                  not null,
    created_at        timestamp without time zone  not null,
    from_account_id   integer                      not null,
    to_account_id     integer                      not null,
    amount            numeric(40, 2)               not null,

    constraint pk_transactions primary key (id),
    constraint uk_transactions_txnid unique (txn_id),
    constraint fk_transactions_from_acc_id foreign key (from_account_id) references accounts (id) on delete cascade,
    constraint fk_transactions_to_acc_id foreign key (to_account_id) references accounts (id) on delete cascade
);

create index ix_transactions_from_acc_id on transactions (from_account_id);
create index ix_transactions_to_acc_id on transactions (to_account_id);
