
# Construir la imagen de contenedor

```sh
docker build . -t xnet/messaging_service-channelsadapters:0.1.0
docker push xnet/messaging_service-channelsadapters:0.1.0
```

Para probar el contenedor:

```sh
docker run --rm \
  -v "./src/resources/application.yml:/opt/config/application.yml" \
  xnet/messaging_service-channelsadapters:0.1.0
```

# Prueba del componente

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