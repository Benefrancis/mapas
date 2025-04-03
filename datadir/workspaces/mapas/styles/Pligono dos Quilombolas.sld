<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>Pligono dos Quilombolas</Name>
    <UserStyle>
      <Title>Estilo para as comunidades quilombolas</Title>
      <FeatureTypeStyle>
        <Rule>
          <Title>Estilo para as comunidades quilombolas</Title>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">#ab8f10</CssParameter>
              <CssParameter name="fill-opacity">0.4</CssParameter> <!-- 70% de transparência -->
            </Fill>
            <Stroke>
              <CssParameter name="stroke">#736244</CssParameter>
              <CssParameter name="stroke-width">0.7</CssParameter>
            </Stroke>
          </PolygonSymbolizer>

        </Rule>

      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>