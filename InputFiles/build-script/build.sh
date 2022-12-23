#!bin/bash
docker build -t delian_database .
docker run -d --name "database-container" --publish 3306:3306 delian_database
sleep 30 # Wait for the server to start. A better solution would be pinging
docker exec database-container mysql --user="root" --password="1" --execute="GRANT SELECT ON *.* TO 'CinecubesUser'@'%'"
