# Library

## Запуск сервера в докере

1. Если хотите использовать и Postgres в докере, то в `application.yml` замените `localhost` на `host.docker.internal`
2. Запустите `postgres` в докере командой
```bash
docker compose up db -d
```
3. Запустите сервер в докере
```bash
docker compose up server
```


Если же хотите запустить только базу данных в докере, то менять `localhost` на `host.docker.internal` **не нужно!**