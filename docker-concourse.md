docker network create concourse-net

docker pull postgres

docker run --name concourse-db --net=concourse-net -h concourse-postgres -p 5432:5432 -e POSTGRES_USER=concourseuser -e POSTGRES_PASSWORD=concoursepass -e POSTGRES_DB=atc -d postgres

docker run --name concourse -h concourse -p 8080:8080 --detach --privileged --net=concourse-net concourse/concourse quickstart --add-local-user=cuser:cpass --main-team-local-user=cuser --external-url=http://10.104.173.94:8080 --postgres-user=concourseuser --postgres-password=concoursepass --postgres-host=concourse-db --worker-garden-dns-server 8.8.8.8
