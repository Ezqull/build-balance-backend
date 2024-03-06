#!/bin/bash

HOST="localhost"
PORT="5433"
USER="${POSTGRES_USER}"
DB="${POSTGRES_DB}"

pg_isready -h $HOST -p $PORT -U $USER -d $DB
