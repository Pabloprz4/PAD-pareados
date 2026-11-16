-- =====================================================================
-- Ars · Esquema de la base de datos (Supabase / PostgreSQL)
-- Ejecutar en Supabase: SQL Editor → New query → pegar → Run.
-- Orden: 01_esquema.sql y después 02_datos.sql.
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. CATÁLOGO (igual para todos los usuarios, solo lectura desde la app)
-- ---------------------------------------------------------------------

create table museo (
    id                      text primary key,           -- p. ej. 'museo_001'
    nombre                  text not null,
    ciudad                  text not null,
    pais                    text not null,
    descripcion             text not null default '',
    destacado               boolean not null default false,
    -- se rellenan después de crear las criaturas (ver 02_datos.sql)
    criatura_bienvenida_id  text,                       -- se consigue al visitar el museo
    recompensa_misiones_id  text                        -- se consigue al completar todas sus misiones
);

create table criatura (
    id           text primary key,                      -- p. ej. 'criatura_001'
    nombre       text not null,
    descripcion  text not null default '',
    museo_id     text references museo (id) on delete set null   -- null = no pertenece a ningún museo de la app
);

-- Ahora que existe la tabla criatura, se añaden las claves foráneas del museo
alter table museo
    add constraint museo_bienvenida_fk foreign key (criatura_bienvenida_id) references criatura (id) on delete set null,
    add constraint museo_recompensa_fk foreign key (recompensa_misiones_id) references criatura (id) on delete set null;

create table obra (
    id           text primary key,                      -- es el id que lleva el QR de la obra
    titulo       text not null,
    autor        text not null,
    museo_id     text not null references museo (id) on delete cascade,
    descripcion  text not null default ''
);

create table mision (
    id                      text primary key,           -- p. ej. 'prado_jardin'
    museo_id                text not null references museo (id) on delete cascade,
    titulo                  text not null,
    -- CANTIDAD_OBRAS: escanear N obras distintas del museo
    -- OBRAS_CONCRETAS: escanear las obras listadas en mision_obra
    tipo                    text not null check (tipo in ('CANTIDAD_OBRAS', 'OBRAS_CONCRETAS')),
    cantidad_obras          int check (cantidad_obras > 0),
    recompensa_criatura_id  text references criatura (id) on delete set null,
    orden                   int not null default 1,     -- orden en el que se muestran en el museo
    -- una misión de cantidad necesita su número; una de obras concretas no
    constraint mision_tipo_coherente check (
        (tipo = 'CANTIDAD_OBRAS'  and cantidad_obras is not null) or
        (tipo = 'OBRAS_CONCRETAS' and cantidad_obras is null)
    )
);

-- Relación N:M entre misiones de tipo OBRAS_CONCRETAS y las obras que piden
create table mision_obra (
    mision_id  text not null references mision (id) on delete cascade,
    obra_id    text not null references obra (id) on delete cascade,
    primary key (mision_id, obra_id)
);

create index on criatura (museo_id);
create index on obra (museo_id);
create index on mision (museo_id);

-- ---------------------------------------------------------------------
-- 2. PROGRESO DE CADA USUARIO
-- usuario_id es el id del usuario de Supabase Auth (también vale el
-- inicio de sesión anónimo, que da un id a cada instalación sin pedir
-- email ni contraseña).
-- ---------------------------------------------------------------------

create table perfil (
    usuario_id           uuid primary key references auth.users (id) on delete cascade,
    criatura_inicial_id  text references criatura (id),
    creado_en            timestamptz not null default now()
);

create table usuario_criatura (                          -- criaturas descubiertas
    usuario_id    uuid not null references auth.users (id) on delete cascade,
    criatura_id   text not null references criatura (id) on delete cascade,
    obtenida_en   timestamptz not null default now(),
    primary key (usuario_id, criatura_id)
);

create table usuario_museo (                             -- museos visitados
    usuario_id    uuid not null references auth.users (id) on delete cascade,
    museo_id      text not null references museo (id) on delete cascade,
    visitado_en   timestamptz not null default now(),
    primary key (usuario_id, museo_id)
);

create table usuario_obra (                              -- obras escaneadas
    usuario_id    uuid not null references auth.users (id) on delete cascade,
    obra_id       text not null references obra (id) on delete cascade,
    escaneada_en  timestamptz not null default now(),
    primary key (usuario_id, obra_id)
);

create table usuario_mision (                            -- misiones completadas
    usuario_id      uuid not null references auth.users (id) on delete cascade,
    mision_id       text not null references mision (id) on delete cascade,
    completada_en   timestamptz not null default now(),
    primary key (usuario_id, mision_id)
);

-- ---------------------------------------------------------------------
-- 3. SEGURIDAD (Row Level Security)
-- Sin RLS cualquiera con la clave pública podría modificar las tablas.
-- · Catálogo: todo el mundo puede leerlo, nadie puede modificarlo desde la app.
-- · Progreso: cada usuario solo ve y modifica sus propias filas.
-- ---------------------------------------------------------------------

alter table museo        enable row level security;
alter table criatura     enable row level security;
alter table obra         enable row level security;
alter table mision       enable row level security;
alter table mision_obra  enable row level security;

create policy "catalogo legible" on museo       for select using (true);
create policy "catalogo legible" on criatura    for select using (true);
create policy "catalogo legible" on obra        for select using (true);
create policy "catalogo legible" on mision      for select using (true);
create policy "catalogo legible" on mision_obra for select using (true);

alter table perfil            enable row level security;
alter table usuario_criatura  enable row level security;
alter table usuario_museo     enable row level security;
alter table usuario_obra      enable row level security;
alter table usuario_mision    enable row level security;

create policy "solo su perfil"     on perfil           for all using (auth.uid() = usuario_id) with check (auth.uid() = usuario_id);
create policy "solo sus criaturas" on usuario_criatura for all using (auth.uid() = usuario_id) with check (auth.uid() = usuario_id);
create policy "solo sus museos"    on usuario_museo    for all using (auth.uid() = usuario_id) with check (auth.uid() = usuario_id);
create policy "solo sus obras"     on usuario_obra     for all using (auth.uid() = usuario_id) with check (auth.uid() = usuario_id);
create policy "solo sus misiones"  on usuario_mision   for all using (auth.uid() = usuario_id) with check (auth.uid() = usuario_id);

-- ---------------------------------------------------------------------
-- 4. VISTA DE PROGRESO POR MUSEO (ejemplo de consulta relacional)
-- Devuelve, para el usuario que hace la consulta, cuántas obras y
-- criaturas lleva de cada museo. Sustituye a los contadores calculados a mano.
-- ---------------------------------------------------------------------

create view progreso_museo with (security_invoker = true) as
select
    m.id                                                         as museo_id,
    (select count(*) from obra o where o.museo_id = m.id)        as obras_totales,
    (select count(*) from usuario_obra uo join obra o on o.id = uo.obra_id
      where o.museo_id = m.id and uo.usuario_id = auth.uid())    as obras_descubiertas,
    (select count(*) from criatura c where c.museo_id = m.id)    as criaturas_totales,
    (select count(*) from usuario_criatura uc join criatura c on c.id = uc.criatura_id
      where c.museo_id = m.id and uc.usuario_id = auth.uid())    as criaturas_descubiertas
from museo m;
