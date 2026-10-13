# demo-app

Java 21 / Spring Boot 4.1 demo service for the Agentic DevOps lab.

- `GET /api/greeting?name=...` returns a JSON greeting.
- `GET /actuator/health/{liveness,readiness}` for Kubernetes probes.

## Build
```bash
./mvnw verify          # unit + HTTP tests, JaCoCo coverage report
```

## CI
`Jenkinsfile` runs on the isolated `build` agent: Maven verify, SonarQube analysis with quality gate,
then a container image build through rootless BuildKit. Images are pushed (by digest) only from `main`;
pull requests build the image without pushing.
