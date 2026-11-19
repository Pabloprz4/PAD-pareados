-- Datos iniciales de Ars, generados a partir de los repositorios en memoria del proyecto.
-- Ejecutar DESPUÉS de 01_esquema.sql.

begin;

insert into museo (id, nombre, ciudad, pais, descripcion, destacado) values
  ('museo_001', 'Museo del Prado', 'Madrid', 'España', 'El Prado reúne obras de Velázquez, Goya y El Bosco. Descubre sus pinturas y las criaturas inspiradas en ellas.', true),
  ('museo_002', 'Museo Reina Sofía', 'Madrid', 'España', 'El Reina Sofía presenta arte moderno y contemporáneo. En Ars puedes explorar las obras de Picasso y sus criaturas.', true),
  ('museo_003', 'Museo Thyssen-Bornemisza', 'Madrid', 'España', 'El Thyssen-Bornemisza reúne pintura de distintas épocas y estilos en Madrid.', true),
  ('museo_004', 'MoMA', 'Nueva York', 'Estados Unidos', 'El MoMA reúne arte moderno y contemporáneo en Nueva York.', false),
  ('museo_005', 'Museo Van Gogh', 'Ámsterdam', 'Países Bajos', 'El Museo Van Gogh permite conocer la pintura y la trayectoria del artista en Ámsterdam.', false),
  ('museo_006', 'Galería Uffizi', 'Florencia', 'Italia', 'La Galería Uffizi reúne obras del Renacimiento en Florencia.', false),
  ('museo_007', 'Museo MUNCH', 'Oslo', 'Noruega', 'El Museo MUNCH está dedicado al artista Edvard Munch en Oslo.', false),
  ('museo_008', 'Museos Vaticanos', 'Ciudad del Vaticano', 'Ciudad del Vaticano', 'Los Museos Vaticanos reúnen colecciones artísticas en la Ciudad del Vaticano.', false),
  ('museo_009', 'The Met', 'Nueva York', 'Estados Unidos', 'El Museo Metropolitano de Arte reúne obras de distintas culturas y épocas en Nueva York.', false),
  ('museo_010', 'Museo del Louvre', 'París', 'Francia', 'El Louvre reúne colecciones artísticas en París.', false);

insert into criatura (id, nombre, descripcion, museo_id) values
  ('criatura_001', 'Menina Fantasmal', 'Criatura conseguida automáticamente al visitar el Museo del Prado.', 'museo_001'),
  ('criatura_002', 'Caballo Guernica', 'Criatura cubista desbloqueada al fotografiar cuadros del Reina Sofía.', 'museo_002'),
  ('criatura_003', 'Reloj Derretido', 'Criatura surrealista inspirada en La persistencia de la memoria de Dalí.', 'museo_004'),
  ('criatura_004', 'Humanoide del Jardín', 'Humanoide inspirado en El jardín de las delicias de El Bosco.', 'museo_001'),
  ('criatura_005', 'Girasol Ardiente', 'Girasol ardiente inspirado en Los girasoles de Vincent van Gogh.', 'museo_005'),
  ('criatura_006', 'Ninfa de Venus', 'Pequeña ninfa inspirada en El nacimiento de Venus de Sandro Botticelli.', 'museo_006'),
  ('criatura_007', 'Espectro del Grito', 'Espectro inspirado en El grito de Edvard Munch.', 'museo_007'),
  ('criatura_008', 'Saturno Devorador', 'Criatura inspirada en Saturno devorando a su hijo de Francisco de Goya.', 'museo_001'),
  ('criatura_009', 'Remolino Estrellado', 'Remolino flotante inspirado en La noche estrellada de Vincent van Gogh.', 'museo_004'),
  ('criatura_010', 'Manos de la Creación', 'Manos flotantes inspiradas en La creación de Adán de Miguel Ángel.', 'museo_008'),
  ('criatura_011', 'Garra de Kanagawa', 'Garra de agua inspirada en La gran ola de Kanagawa de Katsushika Hokusai.', 'museo_009'),
  ('criatura_012', 'Manzana con Traje', 'Manzana con traje inspirada en El hijo del hombre de René Magritte.', 'museo_007'),
  ('criatura_013', 'Dama de la Mona Lisa', 'Dama flotante inspirada en La Mona Lisa de Leonardo da Vinci.', 'museo_010'),
  ('criatura_014', 'Toro Guernica', 'Criatura cubista inspirada en el toro del Guernica de Pablo Picasso.', 'museo_002'),
  ('criatura_015', 'Caballero del Paisaje', 'Caballero inspirado en Joven caballero en un paisaje de Vittore Carpaccio.', 'museo_003');

-- Criatura de bienvenida y recompensa por completar todas las misiones de cada museo
update museo set criatura_bienvenida_id = 'criatura_001' where id = 'museo_001';
update museo set recompensa_misiones_id = 'criatura_015' where id = 'museo_003';
update museo set recompensa_misiones_id = 'criatura_005' where id = 'museo_005';
update museo set recompensa_misiones_id = 'criatura_006' where id = 'museo_006';
update museo set recompensa_misiones_id = 'criatura_010' where id = 'museo_008';
update museo set recompensa_misiones_id = 'criatura_011' where id = 'museo_009';
update museo set recompensa_misiones_id = 'criatura_013' where id = 'museo_010';

insert into obra (id, titulo, autor, museo_id, descripcion) values
  ('obra_001', 'Las meninas', 'Diego Velázquez', 'museo_001', 'Velázquez representa a la infanta Margarita junto a sus acompañantes en el Alcázar de Madrid. El pintor aparece trabajando a la izquierda y un espejo refleja a los reyes al fondo.'),
  ('obra_002', 'El jardín de las delicias', 'El Bosco', 'museo_001', 'Este tríptico del Bosco reúne el paraíso, un mundo de figuras y animales fantásticos y una visión del infierno. Sus escenas están llenas de pequeños detalles.'),
  ('obra_003', 'Guernica', 'Pablo Picasso', 'museo_002', 'Picasso pintó esta obra en 1937. Sus figuras y sus tonos blancos, negros y grises expresan el sufrimiento y la violencia de la guerra.'),
  ('obra_reina_ventana', 'Figura en una ventana', 'Salvador Dalí', 'museo_002', 'Dalí retrata a su hermana Anna Maria de espaldas, mirando el mar desde una ventana en Cadaqués. La luz azul y la quietud de la figura crean una escena tranquila.'),
  ('obra_thyssen_caballero', 'Joven caballero en un paisaje', 'Vittore Carpaccio', 'museo_003', 'Un joven con armadura sostiene su espada ante un paisaje lleno de plantas y animales. El armiño y las inscripciones aluden al honor y a las virtudes del caballero.'),
  ('obra_thyssen_hotel', 'Habitación de hotel', 'Edward Hopper', 'museo_003', 'Una viajera sentada al borde de una cama consulta un horario de trenes, con el equipaje a su lado. Las líneas de la habitación y la luz intensa refuerzan su aislamiento.'),
  ('obra_moma_noche', 'La noche estrellada', 'Vincent van Gogh', 'museo_004', 'Un cielo de estrellas y remolinos domina un pueblo al pie de las montañas. Van Gogh pintó esta vista imaginada en Saint-Rémy en 1889, con un gran ciprés en primer plano.'),
  ('obra_moma_memoria', 'La persistencia de la memoria', 'Salvador Dalí', 'museo_004', 'Varios relojes blandos se doblan sobre una rama y otras superficies en un paisaje casi vacío. Dalí mezcla objetos reconocibles y formas extrañas para crear una escena de sueño.'),
  ('obra_vangogh_girasoles', 'Los girasoles', 'Vincent van Gogh', 'museo_005', 'Un jarrón reúne girasoles en distintas fases de vida, pintados con muchos tonos de amarillo. Esta versión de 1889 pertenece al Museo Van Gogh de Ámsterdam.'),
  ('obra_vangogh_almendro', 'Almendro en flor', 'Vincent van Gogh', 'museo_005', 'Las ramas de un almendro florecen sobre un cielo azul. Van Gogh pintó la obra en 1890 como regalo para su hermano Theo y su cuñada Jo por el nacimiento de su hijo.'),
  ('obra_uffizi_venus', 'El nacimiento de Venus', 'Sandro Botticelli', 'museo_006', 'Venus llega a la costa sobre una concha, impulsada por el viento, mientras una figura se acerca con un manto. Botticelli convierte un relato de la mitología clásica en una imagen de belleza ideal.'),
  ('obra_uffizi_primavera', 'La primavera', 'Sandro Botticelli', 'museo_006', 'Nueve personajes de la mitología se reúnen en un jardín de naranjos y flores. Venus ocupa el centro, con Cupido sobre ella y las tres Gracias danzando a su izquierda.'),
  ('obra_munch_grito', 'El grito', 'Edvard Munch', 'museo_007', 'Una figura se lleva las manos a la cabeza bajo un cielo ondulado y rojizo. Sus líneas y colores transmiten angustia. Esta es la versión pintada hacia 1910 que conserva el MUNCH.'),
  ('obra_munch_madonna', 'Madonna', 'Edvard Munch', 'museo_007', 'Una mujer de ojos cerrados aparece rodeada por líneas curvas y un halo rojo. Munch transforma el retrato con colores y formas que expresan emociones. Esta versión de 1894 pertenece al MUNCH.'),
  ('obra_vaticano_creacion', 'La creación de Adán', 'Miguel Ángel', 'museo_008', 'Dios extiende su mano hacia Adán y sus dedos quedan a punto de tocarse. La escena forma parte de los frescos que Miguel Ángel pintó en la bóveda de la Capilla Sixtina.'),
  ('obra_vaticano_atenas', 'La escuela de Atenas', 'Rafael Sanzio', 'museo_008', 'Platón y Aristóteles conversan en el centro de una arquitectura monumental, rodeados por pensadores de la Antigüedad. Este fresco decora la Estancia de la Signatura en el Vaticano.'),
  ('obra_met_ola', 'La gran ola de Kanagawa', 'Katsushika Hokusai', 'museo_009', 'Una enorme ola se curva sobre varias barcas, mientras el monte Fuji aparece pequeño al fondo. Esta estampa pertenece a la serie Treinta y seis vistas del monte Fuji.'),
  ('obra_met_washington', 'Washington cruzando el Delaware', 'Emanuel Leutze', 'museo_009', 'George Washington avanza en una barca entre bloques de hielo durante la guerra de Independencia de Estados Unidos. Leutze pintó en 1851 esta reconstrucción idealizada del cruce del río Delaware.'),
  ('obra_louvre_mona_lisa', 'La Mona Lisa', 'Leonardo da Vinci', 'museo_010', 'Leonardo retrata a Lisa Gherardini sentada ante un paisaje lejano. Los contornos suaves del sfumato y su sonrisa sutil hacen de este retrato una de las obras más conocidas del Louvre.'),
  ('obra_louvre_libertad', 'La Libertad guiando al pueblo', 'Eugène Delacroix', 'museo_010', 'Una figura que personifica la Libertad alza la bandera francesa y guía a un grupo sobre una barricada. Delacroix se inspiró en la revolución de julio de 1830 en París.');

insert into mision (id, museo_id, titulo, tipo, cantidad_obras, recompensa_criatura_id, orden) values
  ('prado_cantidad', 'museo_001', 'Escanear 2 obras distintas del Prado', 'CANTIDAD_OBRAS', 2, 'criatura_008', 1),
  ('prado_jardin', 'museo_001', 'Escanear El jardín de las delicias', 'OBRAS_CONCRETAS', NULL, 'criatura_004', 2),
  ('reina_cantidad', 'museo_002', 'Escanear 2 obras distintas del Reina Sofía', 'CANTIDAD_OBRAS', 2, 'criatura_014', 1),
  ('reina_guernica', 'museo_002', 'Escanear Guernica', 'OBRAS_CONCRETAS', NULL, 'criatura_002', 2),
  ('thyssen_cantidad', 'museo_003', 'Escanear 2 obras distintas del Thyssen', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('thyssen_caballero', 'museo_003', 'Escanear Joven caballero en un paisaje', 'OBRAS_CONCRETAS', NULL, NULL, 2),
  ('moma_cantidad', 'museo_004', 'Escanear 2 obras distintas del MoMA', 'CANTIDAD_OBRAS', 2, 'criatura_003', 1),
  ('moma_noche', 'museo_004', 'Escanear La noche estrellada', 'OBRAS_CONCRETAS', NULL, 'criatura_009', 2),
  ('vangogh_cantidad', 'museo_005', 'Escanear 2 obras distintas del Museo Van Gogh', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('vangogh_obra', 'museo_005', 'Escanear Los girasoles', 'OBRAS_CONCRETAS', NULL, NULL, 2),
  ('uffizi_cantidad', 'museo_006', 'Escanear 2 obras distintas de la Galería Uffizi', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('uffizi_obra', 'museo_006', 'Escanear El nacimiento de Venus', 'OBRAS_CONCRETAS', NULL, NULL, 2),
  ('munch_cantidad', 'museo_007', 'Escanear 2 obras distintas del Museo MUNCH', 'CANTIDAD_OBRAS', 2, 'criatura_007', 1),
  ('munch_obra', 'museo_007', 'Escanear El grito', 'OBRAS_CONCRETAS', NULL, 'criatura_012', 2),
  ('vaticano_cantidad', 'museo_008', 'Escanear 2 obras distintas de los Museos Vaticanos', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('vaticano_obra', 'museo_008', 'Escanear La creación de Adán', 'OBRAS_CONCRETAS', NULL, NULL, 2),
  ('met_cantidad', 'museo_009', 'Escanear 2 obras distintas del Met', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('met_obra', 'museo_009', 'Escanear La gran ola de Kanagawa', 'OBRAS_CONCRETAS', NULL, NULL, 2),
  ('louvre_cantidad', 'museo_010', 'Escanear 2 obras distintas del Louvre', 'CANTIDAD_OBRAS', 2, NULL, 1),
  ('louvre_obra', 'museo_010', 'Escanear La Mona Lisa', 'OBRAS_CONCRETAS', NULL, NULL, 2);

insert into mision_obra (mision_id, obra_id) values
  ('prado_jardin', 'obra_002'),
  ('reina_guernica', 'obra_003'),
  ('thyssen_caballero', 'obra_thyssen_caballero'),
  ('moma_noche', 'obra_moma_noche'),
  ('vangogh_obra', 'obra_vangogh_girasoles'),
  ('uffizi_obra', 'obra_uffizi_venus'),
  ('munch_obra', 'obra_munch_grito'),
  ('vaticano_obra', 'obra_vaticano_creacion'),
  ('met_obra', 'obra_met_ola'),
  ('louvre_obra', 'obra_louvre_mona_lisa');

commit;
