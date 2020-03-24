docker exec docker_keycloak_1 /opt/jboss/keycloak/bin/standalone.sh -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/tmp/estacionamento-realm.json -Dkeycloak.migration.realmName=estacionamento -Dkeycloak.migration.usersExportStrategy=SAME_FILE -Djboss.socket.binding.port-offset=500
docker exec docker_keycloak_1 cat /tmp/estacionamento-realm.json > estacionamento-realm.json