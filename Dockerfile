FROM openjdk:8-jdk
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY ${DEPENDENCY}/static /app/static


ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul","-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2", "-cp","app:app/lib/*", "social.alone.server.Application"]