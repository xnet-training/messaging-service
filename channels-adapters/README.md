
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
set JAVA_HOME=D:\00-INSTALADORES\jdk-17.0.9
set PATH=%JAVA_HOME%\bin;%PATH%
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

### Credito Aprobado

#### Kafka Topic

```json
{
  "eventType": "LoanApproved",
  "evendId": 123232323,
  "payload": {
    "cuenta": "123455665435435435",
    "moneda": "PEN",
    "monto": 5000.00,
    "cuotas": 640.0,
    "plazo": 10    
  }
}
```

#### RabbitMQ
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

### Cuota Vencida

```json
{
  "eventType": "CreditoAprobado",
  "ownerId": 1234556,
  "payload": {
    "cuenta": "123455665435435435",
    "moneda": "PEN",
    "montoCuota": 450.00,
    "cuota": 640.0
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

## Kafka

### Crear tópico

Para crear un nuevo tópico kafka se requiere especificar los siguientes parámetros:

**bin/kafka-topics.sh** `--create` `--topic` NOMBRE-DEL-TOPICO `--bootstrap-server` IP_DNS-SERVIDOR:9092


```sh
bin/kafka-topics.sh --create \
  --topic topic.businessevents \
  --bootstrap-server localhost:9092
```
Como resultado se debe obtener un resultado similar al siguiente:

```sh
Created topic topic.businessevents.
```

### Listar los tópicos

Para listar los tópicos existentes se debe utilizar el siguiente comando:

**bin/kafka-topics.sh** `--list`  `--bootstrap-server` IP_DNS-SERVIDOR:9092

Por ejemplo,

```sh
bin/kafka-topics.sh --list  \
  --bootstrap-server localhost:9092
```

Como resultado se debe obtener un resultado similar al siguiente:

```sh
__consumer_offsets
cdc
my_connect_configs
my_connect_offsets
my_connect_statuses
schema-changes.partyreferencedata
topic.businessevents
```

## Eliminar topic

Para eliminar tópicos existentes se debe utilizar el siguiente comando:

**bin/kafka-topics.sh** `--delete` `--topic` NOMBRE-TOPICO `--bootstrap-server` IP_DNS-SERVIDOR:9092

Por ejemplo,

```sh
bin/kafka-topics.sh --delete \
  --topic topic.businessevents \
  --bootstrap-server localhost:9092
```

> El comando anterior no genera un resultado específico, salvo sea un error en su ejecución.

## Lectura de mensajes desde tópico

Para consultar los mensajes que se encuentran en tópicos se debe utilizar el siguiente comando:

1. Para obtener solo los mensajes que se publiquen desde que el consumidor se conecta a Kafka

**bin/kafka-console-consumer.sh** `--topic` NOMBRE-TOPICO `--bootstrap-server` IP_DNS-SERVIDOR:9092

2. Para recibir todos los mensajes publicados desde el inicio del tópico.

**bin/kafka-console-consumer.sh** `--from-beginning` `--topic` NOMBRE-TOPICO `--bootstrap-server` IP_DNS-SERVIDOR:9092

Por ejemplo,

```sh
bin/kafka-console-consumer.sh \
  --from-beginning \
  --topic cdc \
  --bootstrap-server localhost:9092
```

Un resultado posible, de acuerdo con los mensajes en tópico es el siguiente:

```sh
{
  "eventType": "LoanApproved",
  "evendId": 123232323,
  "payload": {
    "cuenta": "123455665435435435",
    "moneda": "PEN",
    "monto": 5000.00,
    "cuotas": 640.0,
    "plazo": 10
  }
}
{
    "eventType": "LoanApproved",
    "evendId": 123232323,
    "payload": {
            "cuenta": "123455665435435435",
            "moneda": "PEN",
            "monto": 3000.00,
            "cuotas": 640.0,
            "plazo": 10
    }
}
```

## Producir mensajes en tópico

**bin/kafka-console-producer.sh** `--topic` NOMBRE-TOPICO `--bootstrap-server` IP_DNS-SERVIDOR:9092

Por ejemplo,

```sh
bin/kafka-console-producer.sh --topic cdc --bootstrap-server localhost:9092
```

> ante el comando anterior, kafka simplemente responde con el prompt **>** 

Una vez enviado el mensaje hacia el tópico tenemos el siguiente resultado:

```sh
>{ "eventType": "LoanApproved","evendId": 123232323,"payload": {"cuenta": "123455665435435435","moneda": "PEN","monto": 3000.00,"cuotas": 640.0,"plazo": 10 }}
```