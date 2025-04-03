<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
		xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
		xmlns="http://www.opengis.net/sld"
		xmlns:ogc="http://www.opengis.net/ogc"
		xmlns:xlink="http://www.w3.org/1999/xlink"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<!-- a named layer is the basic building block of an sld document -->

	<NamedLayer>
		<Name>Default Point</Name>
		<UserStyle>
		    <!-- they have names, titles and abstracts -->

			<Title>Ponto Personalizado Laranja</Title>
			<Abstract>Um ponto laranja</Abstract>
			<!-- FeatureTypeStyles describe how to render different features -->
			<!-- a feature type for points -->

			<FeatureTypeStyle>
				<!--FeatureTypeName>Feature</FeatureTypeName-->
				<Rule>
					<Name>Regra 1</Name>
					<Title>Um ponto laranja</Title>
					<Abstract>Um ponto laranja com tamanho 3</Abstract>

					<!-- like a linesymbolizer but with a fill too -->
					<PointSymbolizer>
						<Graphic>
							<Mark>
								<WellKnownName>circle</WellKnownName>
								<Fill>
									<CssParameter name="fill">#ffa100</CssParameter>
                                  <CssParameter name="fill-opacity">0.5</CssParameter>
								</Fill>
                <Stroke>
                  <CssParameter name="stroke">#9b6508</CssParameter>
                  <CssParameter name="stroke-width">2</CssParameter>
                </Stroke>
							</Mark>
							<Size>3</Size>
						</Graphic>
					</PointSymbolizer>
				</Rule>

		    </FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>