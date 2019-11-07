#!/usr/bin/env bash

docker run -d \
    -e ADMIN_PASSWORD="admin" \
    -e DATOMIC_PASSWORD="admin"  \
    -p 4334-4336:4334-4336 \
    --name datomic-free akiel/datomic-free
