FROM mysql
# Copy backup files in image and load them to db
COPY backup-files/* /docker-entrypoint-initdb.d/
ENV MYSQL_ROOT_PASSWORD=1 \
    MYSQL_USER=CinecubesUser \
    MYSQL_PASSWORD=Cinecubes
EXPOSE 3306
