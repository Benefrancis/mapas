#!/bin/sh

if [ -d "src/main/resources/static" ]; then
    cp -r src/main/resources/static /app/static
else
    echo "Diretório 'static' não existe, ignorando..."
fi

if [ -d "src/main/resources/templates" ]; then
    cp -r src/main/resources/templates /app/templates
else
    echo "Diretório 'templates' não existe, ignorando..."
fi
