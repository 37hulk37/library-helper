# Library

## Запуск сервера в докере:

1. Чтобы запустить Postgres в Docker application.yml` замените `localhost` на `host.docker.internal`
2. Запустите `postgres` в докере командой
```bash
docker-compose up db -d
```
3. Запустите сервер в докере
```bash
docker-compose up server
```

Если хотите запустить только базу данных в докере, то менять `localhost` на `host.docker.internal` **не нужно!**

## Notes:
1. При интеграции с Spring Security, при условии, что сервис library-helper - resource server 
запрос на создание пользователя можно будет убрать, так как информацию о пользователе можно 
будет получить из токена, и если пользователь - новый, то зарегистрировать его.
2. Open Api: https://github.com/37hulk37/library-helper/blob/master/openapi.yaml