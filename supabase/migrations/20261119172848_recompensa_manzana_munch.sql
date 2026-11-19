update public.criatura set museo_id = 'museo_007' where id = 'criatura_012';
update public.museo set recompensa_misiones_id = null
where id = 'museo_007' and recompensa_misiones_id = 'criatura_007';
update public.mision set recompensa_criatura_id = 'criatura_007'
where id = 'munch_cantidad' and museo_id = 'museo_007';
update public.mision set recompensa_criatura_id = 'criatura_012'
where id = 'munch_obra' and museo_id = 'museo_007';
