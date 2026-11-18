-- Guarda las altas y devuelve la partida actual del usuario de la sesión.
create function public.sincronizar_progreso_ars(usuario_esperado uuid, cambios jsonb default null)
returns jsonb
language plpgsql
security invoker
set search_path = ''
as $$
declare
    usuario uuid := auth.uid();
    inicial text := cambios ->> 'criaturaInicialId';
    inicial_guardada text;
begin
    if usuario is null or usuario is distinct from usuario_esperado then
        raise exception 'La sesión ha cambiado' using errcode = '42501';
    end if;

    if cambios is not null then
        insert into public.perfil (usuario_id) values (usuario)
            on conflict (usuario_id) do nothing;

        -- El perfil ordena las escrituras si dos dispositivos guardan a la vez.
        select p.criatura_inicial_id into inicial_guardada
            from public.perfil p where p.usuario_id = usuario for update;

        if inicial is not null and inicial_guardada is null then
            update public.perfil set criatura_inicial_id = inicial where usuario_id = usuario;
            inicial_guardada := inicial;
        end if;

        -- Si otro dispositivo ya eligió otro starter, se devuelve el que ganó.
        if inicial is null or inicial = inicial_guardada then
            insert into public.usuario_museo (usuario_id, museo_id)
                select usuario, id from jsonb_array_elements_text(cambios -> 'museosVisitados') as id
                on conflict do nothing;

            insert into public.usuario_obra (usuario_id, obra_id)
                select usuario, id from jsonb_array_elements_text(cambios -> 'obrasEscaneadas') as id
                on conflict do nothing;

            insert into public.usuario_criatura (usuario_id, criatura_id)
                select usuario, id from jsonb_array_elements_text(cambios -> 'criaturasDescubiertas') as id
                on conflict do nothing;

            insert into public.usuario_mision (usuario_id, mision_id)
                select usuario, id from jsonb_array_elements_text(cambios -> 'misionesCompletadas') as id
                on conflict do nothing;
        end if;
    end if;

    return jsonb_build_object(
        'criaturaInicialId', (select p.criatura_inicial_id from public.perfil p where p.usuario_id = usuario),
        'museosVisitados', coalesce((select jsonb_agg(m.museo_id order by m.visitado_en, m.museo_id)
            from public.usuario_museo m where m.usuario_id = usuario), '[]'::jsonb),
        'obrasEscaneadas', coalesce((select jsonb_agg(o.obra_id order by o.escaneada_en, o.obra_id)
            from public.usuario_obra o where o.usuario_id = usuario), '[]'::jsonb),
        'criaturasDescubiertas', coalesce((select jsonb_agg(c.criatura_id order by c.obtenida_en desc, c.criatura_id)
            from public.usuario_criatura c where c.usuario_id = usuario), '[]'::jsonb),
        'misionesCompletadas', coalesce((select jsonb_agg(m.mision_id order by m.completada_en, m.mision_id)
            from public.usuario_mision m where m.usuario_id = usuario), '[]'::jsonb)
    );
end;
$$;

revoke execute on function public.sincronizar_progreso_ars(uuid, jsonb) from public, anon;
grant execute on function public.sincronizar_progreso_ars(uuid, jsonb) to authenticated;
