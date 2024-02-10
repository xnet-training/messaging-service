
# Construir la imagen de contenedor

```sh
docker build . -t xnet/messaging_service-channelsadapters:0.1.0
docker push xnet/messaging_service-channelsadapters:0.1.0
```

Para probar el contenedor:

```sh
docker run --rm \
  -v "./src/main/resources/application.yml:/opt/config/application.yml" \
  xnet/messaging_service-channelsadapters:0.1.0
```

Ejecutar componente

```sh
mvn spring-boot:run
```

# Proteger archivos con datos sensibles previo a publicacion

```sh
git update-index --assume-unchanged src/main/resources/application.yml
```

```sh
git update-index --no-assume-unchanged src/main/resources/application.yml
```

# Prueba del componente

## Mensaje del Evento de Negocio

```json
{
  "eventType": "CreditoAprobado",
  "ownerId": 1234556,
  "payload": {
    "cuenta": "123455665435435435",
    "moneda": "PEN",
    "monto": 5000.00,
    "cuotas": 640.0,
    "plazo": 10
  }
}
```

## SMS

```json
{
  "channel": "sms",
  "message": "Bienvenido a Caja Cusco",
  "properties": {
    "number": "992767641",
    "countryCode": 51
  }
}
```

## Mail

```json
{
  "channel": "mail",
  "message": "Bienvenido a Caja Cusco",
  "properties": {
    "mail": "ilver.anache@gmail.com"
  }
}
```

## Webhook

```json
{
  "channel": "webhook",
  "customerId": "C000199985",
  "message": "Bienvenido a Caja Cusco",
  "properties": {
    "url": "https://httpbin.com/demo",
    "security": {
       "url": "https://SERVIDOR/auth/realms/REALM/protocol/openid-connect/token",
       "username": "usuario",
       "password": "CHANGEME",
       "clientId": "aplicacion",
       "clientSecret": "f4c6f94d-6077-4dce-850a-c9f1bd6291cb",
       "grantType": "password"
    }
  }
}
```