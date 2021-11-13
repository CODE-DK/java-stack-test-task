create table if not exists public.countries
(
    id           bigserial primary key,
    country_code varchar(2) unique,
    counter      bigint default 0
);
create index if not exists country_code_idx on public.countries (country_code);

insert into public.countries (country_code)
values ('AU'),
       ('AT'),
       ('BG'),
       ('GB'),
       ('HU'),
       ('DE'),
       ('EG'),
       ('CY')
on conflict do nothing;