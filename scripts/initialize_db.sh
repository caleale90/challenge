docker cp ../src/main/resources/dump.sql postgres:/tmp/script.sql

docker exec -it postgres psql -U postgres -d challenge -f /tmp/script.sql

