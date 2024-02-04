ARG PORT=8080
FROM maven:3.6.3-openjdk-15 as BUILD

LABEL version=0.1.0

COPY . /usr/src/app

RUN --mount=type=cache,target=/root/.m2 mvn --batch-mode \
  -f /usr/src/app/pom.xml \
  test $(find ./ -type f -name '*.jar' | wc -l) -eq 1

FROM amazoncorretto:17
ENV PORT ${PORT}
ENV APP_PROFILE=kubernetes
ENV APP_NAME=partyreferencedata
ENV VAULT_SECRET=changeme
ENV VAULT_URI=http://vault-service.management.svc.domain.local:8200

WORKDIR /usr/app
COPY --from=BUILD /usr/src/app/api/target/channels-adapters.jar /usr/app/

EXPOSE ${PORT}
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/usr/app/channels-adapters.jar", "--spring.profiles.active=${APP_PROFILE}", "--spring.cloud.vault.token=${VAULT_SECRET}", "--spring.application.name=${APP_NAME}", "--spring.cloud.vault.uri=${VAULT_URI}", "--spring.config.location=file:///opt/config/"]