# COMP3011 Assignment 1 – Speech-to-Text

## Repository

GitHub repository: https://github.com/traqy021/spring-boot-starter-web.git


## Architecture

The application uses a three-layer Spring Boot design.

Controllers handle HTTP requests.
Services contain business logic.
Records define API response objects.

The browser records audio using MediaRecorder and sends it to
POST /api/v1/transcribe as multipart.

The backend sends the audio to the OpenAI transcription API using
gpt-4o-mini-transcribe. The API credential is read exclusively from
the OPENAI_API_KEY environment variable.

## Security

The OpenAI API key is never:
- sent to the browser
- stored in source code
- committed to Git
- written to application logs

TITAN supplies OPENAI_API_KEY at runtime.

## Concurrency

Global token statistics use LongAdder rather than ordinary long
variables because transcription requests may complete simultaneously.

LongAdder prevents lost updates under concurrent access.

The shutdown state uses AtomicBoolean.compareAndSet() so that only
one simultaneous shutdown request can initiate shutdown.

## Testing

StatisticsServiceTest checks normal counter accumulation.

StatisticsConcurrencyTest deliberately starts 250 threads
simultaneously and verifies that no token updates are lost.

This regression test protects against race conditions in the global
statistics implementation.

## API Endpoints

GET /api/v1/admin/uptime

GET /api/v1/global/stats

POST /api/v1/admin/shutdown

POST /api/v1/transcribe

## Configuration

The application obtains the OpenAI credential dynamically:

OPENAI_API_KEY

No API credential is stored in application.properties.

## Build

./mvnw clean package

The executable JAR is generated under target/.

## Local Testing

Local transcription requires access to a valid OpenAI API credential.
The deployed TITAN environment supplies the required credential.

## Graceful Shutdown

Spring graceful shutdown is enabled using:

server.shutdown=graceful

spring.lifecycle.timeout-per-shutdown-phase=20s

Existing requests are given time to complete before the process exits.