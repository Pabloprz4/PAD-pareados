# Base de datos de Ars (Supabase)

Base de datos relacional en **Supabase** (PostgreSQL) con el catálogo de la app
(museos, obras, criaturas y misiones) y el progreso de cada usuario.

| Archivo | Qué hace |
|---|---|
| `01_esquema.sql` | Crea las tablas, sus relaciones, las reglas de seguridad y la vista `progreso_museo`. |
| `02_datos.sql` | Inserta los datos que ahora mismo están escritos a mano en `MuseosRepositoryImpl`, `CatalogoRepositoryImpl` y `HomeRepositoryImpl` (10 museos, 20 obras, 15 criaturas y 20 misiones). |

## Cómo crearla

1. Entrar en [supabase.com](https://supabase.com), iniciar sesión (por ejemplo con GitHub) y pulsar **New project**.
   Región recomendada: *West EU (Ireland)* o *Central EU (Frankfurt)*. Guardad la contraseña de la base de datos.
2. En el proyecto: **SQL Editor → New query**, pegar el contenido de `01_esquema.sql` y pulsar **Run**.
3. Otra query nueva con `02_datos.sql` → **Run**.
4. Comprobar en **Table Editor** que aparecen las tablas con datos.
5. Para conectar la app hacen falta dos valores de **Project Settings → API**:
   la **Project URL** y la clave **anon / public**. La clave `service_role` **no** debe ir nunca en la app.
6. Para el progreso sin pedir registro: **Authentication → Sign In / Providers → Allow anonymous sign-ins**.

## Modelo

```
museo 1──N obra
museo 1──N criatura            (museo_id puede ser null)
museo 1──N mision ──N:M── obra  (tabla mision_obra, solo misiones OBRAS_CONCRETAS)
museo ──► criatura_bienvenida_id, recompensa_misiones_id
mision ──► recompensa_criatura_id

usuario (auth.users)
  1──1 perfil            (criatura inicial)
  N:M  criatura          → usuario_criatura
  N:M  museo             → usuario_museo
  N:M  obra              → usuario_obra
  N:M  mision            → usuario_mision
```

- Los **id** son de texto (`museo_001`, `obra_002`, `prado_jardin`…) para que coincidan con los del código y con los QR.
- Los contadores de cada museo (obras y criaturas totales/descubiertas) no se guardan: se calculan con la vista `progreso_museo`.
- **Seguridad (RLS):** el catálogo lo puede leer cualquiera pero no se puede modificar desde la app; cada usuario solo ve y modifica su propio progreso.
