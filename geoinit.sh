#!/bin/bash
set -e

# Substitui as variáveis de ambiente no web.xml
envsubst < /opt/tomcat/webapps/geoserver/WEB-INF/web.xml.template > /opt/tomcat/webapps/geoserver/WEB-INF/web.xml

# Inicia o Tomcat
exec catalina.sh run