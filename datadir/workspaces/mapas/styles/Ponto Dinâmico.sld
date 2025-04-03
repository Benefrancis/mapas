<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
  xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>Estilo_Ponto_Dinamico</Name>
    <UserStyle>
      <Title>Estilo Ponto Dinâmico por Escala</Title>
      <Abstract>Estilo que altera o tamanho dos pontos conforme o zoom.</Abstract>
      <FeatureTypeStyle>
        <!-- Regra para escalas menores (mais afastado) -->
        <Rule>
          <Name>Escala Pequena</Name>
          <MaxScaleDenominator>100000</MaxScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#062f55</CssParameter>
                </Fill>
              </Mark>
              <Size>4</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <!-- Regra para escalas médias -->
        <Rule>
          <Name>Escala Media</Name>
          <MinScaleDenominator>100000</MinScaleDenominator>
          <MaxScaleDenominator>50000</MaxScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#73400b</CssParameter>
                </Fill>
              </Mark>
              <Size>8</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <!-- Regra para escalas grandes (mais perto) -->
        <Rule>
          <Name>Escala Grande</Name>
          <MinScaleDenominator>50000</MinScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#02024b</CssParameter>
                </Fill>
              </Mark>
              <Size>12</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>