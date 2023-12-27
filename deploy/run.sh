echo "Pulling the latest images..."
docker compose pull app

echo "Starting database..."
docker compose up -d mongo
sleep 2

echo "Starting app..."
docker compose up -d app

#echo "Starting mongo express interface on port 8081..."
#docker compose up -d mongo-express
#sleep 2

docker ps

echo "App seems up and running. Enjoy!"


