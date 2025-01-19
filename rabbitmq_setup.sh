#!/bin/bash

set -e

# Aguarda o RabbitMQ estar pronto
rabbitmq-diagnostics wait 15

# Cria o vhost mapas
rabbitmqctl add_vhost mapas

# Cria usuários
rabbitmqctl add_user api rabbitmq
rabbitmqctl set_user_tags api monitoring
rabbitmqctl set_permissions -p mapas api ".*" ".*" ".*"

rabbitmqctl add_user geoserver rabbitmq
rabbitmqctl set_user_tags geoserver administrator
rabbitmqctl set_permissions -p mapas geoserver ".*" ".*" ".*"

# Defina o usuario para acesso web e conceda as permissões de administrador
rabbitmqctl add_user web rabbitmq
rabbitmqctl set_user_tags web administrator
rabbitmqctl set_permissions -p mapas web ".*" ".*" ".*"

# Cria Queues
rabbitmqctl declare_queue -p mapas geoserver_queue true false {}
rabbitmqctl declare_queue -p mapas api_queue true false {}

# Cria Exchange
rabbitmqctl declare_exchange -p mapas mensagens_exchange topic true false {}

# Cria Bindings
rabbitmqctl bind_queue -p mapas geoserver_queue mensagens_exchange geoserver.*
rabbitmqctl bind_queue -p mapas api_queue mensagens_exchange api.*

# Definições de políticas para ter alta disponibilidade
rabbitmqctl set_policy -p mapas ha-all "^(geoserver_queue|api_queue)$" '{"ha-mode": "all"}' 0

echo "RabbitMQ virtual host 'mapas' created and configured."