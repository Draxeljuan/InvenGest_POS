#!/bin/bash
set -e

echo "=== 1. CREANDO BASES DE DATOS ==="
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE auth_db;
    CREATE DATABASE catalog_db;
    CREATE DATABASE transaction_db;
    CREATE DATABASE analytics_db;
EOSQL

echo "=== 2. INICIALIZANDO ESQUEMAS ==="
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "auth_db" -f /docker-entrypoint-initdb.d/sql/01-auth.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "catalog_db" -f /docker-entrypoint-initdb.d/sql/02-catalog.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "transaction_db" -f /docker-entrypoint-initdb.d/sql/03-transaction.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "analytics_db" -f /docker-entrypoint-initdb.d/sql/04-analytics.sql

echo "=== DESPLIEGUE COMPLETADO ==="
