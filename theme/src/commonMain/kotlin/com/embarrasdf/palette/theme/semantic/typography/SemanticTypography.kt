package com.embarrasdf.palette.theme.semantic.typography

import com.embarrasdf.palette.theme.primitive.PrimitiveTokens

data class SemanticTypography(
    val tokens: Map<TypographyToken, TypographyTokenSet> =
        TypographyToken.entries.associateWith { it.default },
) {
    fun withTokenSet(token: TypographyToken, tokenSet: TypographyTokenSet): SemanticTypography =
        copy(tokens = tokens + (token to tokenSet))
}

fun SemanticTypography.resolve(primitiveTokens: PrimitiveTokens): Typography {
    fun style(token: TypographyToken) =
        tokens.getValue(token).toComposeTextStyle(primitiveTokens)
    return Typography(
        display = style(TypographyToken.Display),
        headline = style(TypographyToken.Headline),
        titleLarge = style(TypographyToken.TitleLarge),
        titleMedium = style(TypographyToken.TitleMedium),
        titleSmall = style(TypographyToken.TitleSmall),
        bodyLarge = style(TypographyToken.BodyLarge),
        bodyMedium = style(TypographyToken.BodyMedium),
        bodySmall = style(TypographyToken.BodySmall),
        labelLarge = style(TypographyToken.LabelLarge),
        labelMedium = style(TypographyToken.LabelMedium),
        labelSmall = style(TypographyToken.LabelSmall),
    )
}
