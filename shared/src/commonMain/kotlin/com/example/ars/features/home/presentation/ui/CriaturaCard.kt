package com.example.ars.features.home.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ars.shared.generated.resources.*
import com.example.ars.features.home.domain.model.Criatura
import org.jetbrains.compose.resources.painterResource

private val ArsDorado = Color(0xFFB8893F)
private val ArsDoradoClaro = Color(0xFFE8D3A5)
private val ArsCrema = Color(0xFFF7F0E4)
private val ArsMarron = Color(0xFF3D3024)
private val ArsPergamino = Color(0xFFE8D8BC)

@Composable
fun CriaturaCard(
    criatura: Criatura,
    modifier: Modifier = Modifier
) {
    val imagenCriatura = when (criatura.nombre) {
        "Menina Fantasmal" -> Res.drawable.menina
        "Caballo Guernica" -> Res.drawable.caballo_guernica
        "Reloj Derretido" -> Res.drawable.reloj_dali
        "Humanoide del Jardín" -> Res.drawable.jardin_delicias
        "Girasol Ardiente" -> Res.drawable.girasol
        "Ninfa de Venus" -> Res.drawable.ninfa_venus
        "Espectro del Grito" -> Res.drawable.el_grito
        "Saturno Devorador" -> Res.drawable.jupiter_monstruo
        "Remolino Estrellado" -> Res.drawable.noche_estrellada
        "Manos de la Creación" -> Res.drawable.dos_manos
        "Garra de Kanagawa" -> Res.drawable.monstruo_ola
        "Manzana con Traje" -> Res.drawable.manzana_verde
        "Dama de la Mona Lisa" -> Res.drawable.mona_lisa
        "Toro Guernica" -> Res.drawable.toro_guernica
        else -> Res.drawable.ars_icon_image
    }

    Card(
        modifier = modifier.width(230.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ArsCrema),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Fondo decorativo: no modifica la medida del contenido.
            Canvas(modifier = Modifier.matchParentSize()) {
                dibujarPergamino()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Emblema geométrico, sin depender de símbolos de una fuente.
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    val centro = Offset(size.width / 2f, size.height / 2f)
                    val radio = 13.dp.toPx()

                    drawLine(
                        ArsDoradoClaro,
                        Offset(8.dp.toPx(), centro.y),
                        Offset(centro.x - radio - 7.dp.toPx(), centro.y),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        ArsDoradoClaro,
                        Offset(centro.x + radio + 7.dp.toPx(), centro.y),
                        Offset(size.width - 8.dp.toPx(), centro.y),
                        strokeWidth = 1.dp.toPx()
                    )

                    drawCircle(ArsCrema, radio, centro)
                    drawCircle(
                        ArsDorado,
                        radio,
                        centro,
                        style = Stroke(1.dp.toPx())
                    )
                    drawCircle(
                        ArsDoradoClaro,
                        radio - 3.dp.toPx(),
                        centro,
                        style = Stroke(0.7.dp.toPx())
                    )
                    dibujarRombo(centro, 7.dp.toPx(), ArsDorado)
                    drawCircle(ArsCrema, 2.dp.toPx(), centro)
                }

                Text(
                    text = "ARS",
                    color = ArsMarron,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 4.sp
                )

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp)
                        .clip(RoundedCornerShape(70.dp, 70.dp, 12.dp, 12.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFFBF1),
                                    ArsCrema,
                                    ArsPergamino
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = ArsDoradoClaro,
                            shape = RoundedCornerShape(
                                70.dp, 70.dp, 12.dp, 12.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(Modifier.matchParentSize()) {
                        drawOval(
                            brush = Brush.radialGradient(
                                listOf(
                                    ArsMarron.copy(alpha = 0.12f),
                                    Color.Transparent
                                ),
                                center = Offset(
                                    size.width / 2f,
                                    size.height * 0.88f
                                ),
                                radius = size.width * 0.4f
                            ),
                            topLeft = Offset(
                                size.width * 0.15f,
                                size.height * 0.81f
                            ),
                            size = Size(
                                size.width * 0.7f,
                                size.height * 0.14f
                            )
                        )
                    }

                    Image(
                        painter = painterResource(imagenCriatura),
                        contentDescription = criatura.nombre,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(12.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                ) {
                    val centro = Offset(size.width / 2f, size.height / 2f)
                    val separacion = 12.dp.toPx()

                    drawLine(
                        ArsDorado,
                        Offset(12.dp.toPx(), centro.y),
                        Offset(centro.x - separacion, centro.y),
                        strokeWidth = 0.8.dp.toPx()
                    )
                    drawLine(
                        ArsDorado,
                        Offset(centro.x + separacion, centro.y),
                        Offset(size.width - 12.dp.toPx(), centro.y),
                        strokeWidth = 0.8.dp.toPx()
                    )
                    dibujarRombo(centro, 4.dp.toPx(), ArsDorado)
                }

                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArsCrema.copy(alpha = 0.75f))
                        .border(
                            0.7.dp,
                            ArsDoradoClaro,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = criatura.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = ArsMarron,
                        textAlign = TextAlign.Center,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = criatura.museo,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArsMarron.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "BESTIARIO ARTÍSTICO",
                    color = ArsDorado,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp,
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))
            }

            // Marco superpuesto para mantener los adornos bien definidos.
            Canvas(modifier = Modifier.matchParentSize()) {
                dibujarMarcoMedieval()
            }
        }
    }
}

private fun DrawScope.dibujarPergamino() {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                ArsPergamino,
                ArsCrema,
                Color(0xFFFFFAEF),
                ArsCrema,
                ArsPergamino
            )
        )
    )

    // Fibras deterministas: la textura permanece estable al recomponer.
    val paso = 7.dp.toPx()
    val filas = (size.height / paso).toInt()
    val columnas = (size.width / paso).toInt()

    for (fila in 0..filas) {
        for (columna in 0..columnas) {
            val semilla = (fila * 37 + columna * 19) % 23
            val x = columna * paso + (semilla % 5) * 0.7.dp.toPx()
            val y = fila * paso + (semilla % 3) * 1.dp.toPx()

            drawLine(
                color = ArsMarron.copy(
                    alpha = 0.018f + (semilla % 4) * 0.006f
                ),
                start = Offset(x, y),
                end = Offset(x + (1 + semilla % 3).dp.toPx(), y),
                strokeWidth = 0.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.dibujarMarcoMedieval() {
    val exterior = 3.dp.toPx()
    val interior = 9.dp.toPx()

    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                ArsDorado,
                ArsDoradoClaro,
                ArsDorado,
                ArsDoradoClaro,
                ArsDorado
            ),
            start = Offset.Zero,
            end = Offset(size.width, size.height)
        ),
        topLeft = Offset(exterior, exterior),
        size = Size(
            size.width - exterior * 2,
            size.height - exterior * 2
        ),
        cornerRadius = CornerRadius(19.dp.toPx()),
        style = Stroke(2.dp.toPx())
    )

    drawRoundRect(
        color = ArsDorado,
        topLeft = Offset(interior, interior),
        size = Size(
            size.width - interior * 2,
            size.height - interior * 2
        ),
        cornerRadius = CornerRadius(13.dp.toPx()),
        style = Stroke(0.8.dp.toPx())
    )

    // Una misma filigrana reflejada en las cuatro esquinas.
    val margen = 12.dp.toPx()
    val unidad = 1.dp.toPx()

    for (derecha in listOf(false, true)) {
        for (abajo in listOf(false, true)) {
            withTransform({
                translate(
                    left = if (derecha) size.width - margen else margen,
                    top = if (abajo) size.height - margen else margen
                )
                scale(
                    scaleX = if (derecha) -unidad else unidad,
                    scaleY = if (abajo) -unidad else unidad,
                    pivot = Offset.Zero
                )
            }) {
                dibujarFiligrana()
            }
        }
    }

    dibujarRombo(
        centro = Offset(size.width / 2f, size.height - interior),
        radio = 3.dp.toPx(),
        color = ArsDorado
    )
}

/**
 * Coordenadas locales equivalentes a dp mediante la transformación
 * aplicada por dibujarMarcoMedieval().
 */
private fun DrawScope.dibujarFiligrana() {
    val tallo = Path().apply {
        moveTo(0f, 39f)
        cubicTo(0f, 18f, 4f, 4f, 22f, 3f)
        cubicTo(36f, 2f, 38f, 15f, 29f, 16f)
        cubicTo(22f, 17f, 20f, 10f, 25f, 8f)

        moveTo(39f, 0f)
        cubicTo(18f, 0f, 4f, 4f, 3f, 22f)
        cubicTo(2f, 36f, 15f, 38f, 16f, 29f)
        cubicTo(17f, 22f, 10f, 20f, 8f, 25f)

        moveTo(3f, 3f)
        cubicTo(12f, 12f, 17f, 16f, 22f, 22f)
    }

    drawPath(
        path = tallo,
        color = ArsDorado,
        style = Stroke(width = 1.1f, cap = StrokeCap.Round)
    )

    val hoja = Path().apply {
        moveTo(15f, 15f)
        cubicTo(14f, 8f, 8f, 7f, 7f, 7f)
        cubicTo(7f, 12f, 10f, 16f, 15f, 15f)
        close()
    }
    drawPath(hoja, ArsDoradoClaro)
    drawPath(hoja, ArsDorado, style = Stroke(0.65f))

    dibujarRombo(Offset(22f, 22f), 3f, ArsDorado)
    drawCircle(ArsDorado, radius = 1.5f, center = Offset(39f, 0f))
    drawCircle(ArsDorado, radius = 1.5f, center = Offset(0f, 39f))
}

private fun DrawScope.dibujarRombo(
    centro: Offset,
    radio: Float,
    color: Color
) {
    val rombo = Path().apply {
        moveTo(centro.x, centro.y - radio)
        lineTo(centro.x + radio * 0.65f, centro.y)
        lineTo(centro.x, centro.y + radio)
        lineTo(centro.x - radio * 0.65f, centro.y)
        close()
    }
    drawPath(rombo, color)
}