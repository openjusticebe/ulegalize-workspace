## Getting started

First you can read the accessible documentation
https://ulegalize.atlassian.net/wiki/spaces/UC/pages/793378817/Open+source

Don't hesitate to create PR in order to change something in the project

## how to develop

The profile used to develop is the '_dev_'

1. copy paste the `application-dev.default.properties` to `application-dev.properties`
2. change values (ask to the administrator or use it yours locally:
   * IP_MYSQL
   * PORT_MYSQL
   * IP_KAFKA
   * PORT_KAFKA
3. Ask to change if it necessary the username/pwd
   `spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='admin' password='admin-secret';
   `

## deploy

change `gradle.properties` version  
commit your change add a new tag and push all

```
git tag 3.6.3  
git push origin 3.6.3

```

#### DEV

run gradle to deploy

you can access REST :  
http://localhost:8989/swagger-ui/index.html#/

#### PROD

tag and push execute to build image change `gradle.properties` version

```
gradle clean  
gradle bootJar   
```

#### docker compose

```
docker stop debian_ulegalize-*  
docker rmi $(docker imagezs finauxa/ulegalize-* -q)
```
change `docker-compose.yml` file with the right version

```
cd ~
vi docker-compose.yml  
docker stop debian_ulegalize-lawfirm_1  
docker rm debian_ulegalize-lawfirm_1  
docker rmi $(docker images finauxa/ulegalize-lawfirm -q)    
docker-compose up -d

```

##### Environment

###### test

https://stackoverflow.com/questions/31324981/how-to-access-host-port-from-docker-container/61424570#61424570   
`
docker run --name ulegalize-lawfirm --network="host" --restart always -p 127.0.0.1:8989:8989 -it finauxa/ulegalize-lawfirm:3.6.3 --spring.profiles.active=test --server.use-forward-headers=true  
`

###### dev

`
docker run --name ulegalize-lawfirm --restart always -p 127.0.0.1:8989:8989 -it finauxa/ulegalize-lawfirm:3.6.3 --spring.profiles.active=devDocker --server.use-forward-headers=true
`

###### prod

`
docker run --name ulegalize-lawfirm --restart always -p 127.0.0.1:8989:8989 -it finauxa/ulegalize-lawfirm:3.6.3 --spring.profiles.active=prod --server.use-forward-headers=true
`

## more info

https://ulegalize.atlassian.net/l/c/AY0FkzHX

#### docker engine

build the app

```
docker build -t finauxa/ulegalize-lawfirm:3.6.3 .  
docker push finauxa/ulegalize-lawfirm:3.6.3  
```

```
docker stop ulegalize-lawfirm   
docker rm ulegalize-lawfirm  
docker rmi $(docker images finauxa/ulegalize-lawfirm -q)  
docker pull finauxa/ulegalize-lawfirm:3.6.3  
```

## Database

### add user

REVOKE `cloudsqlsuperuser`@`%` FROM `user`@`%`;
REVOKE SELECT, UPDATE, DELETE, INSERT, USAGE ON *.* FROM `user`@'%';
REVOKE ALL PRIVILEGES ON avogest.* FROM 'user'@'%';
GRANT SELECT ON avogest.* TO 'user';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'user';

## dpa

#### integration

Generate Java from wsdl D:\yours\src\main\java>
wsimport -extension -verbose -Xnocompile
-J-Djavax.xml.accessExternalSchema=all  https://cs.acc.dp-a.be/api/ws/depositService?wsdl
wsimport -extension -verbose -Xnocompile
-J-Djavax.xml.accessExternalSchema=all  https://cs.acc.dp-a.be/api/ws/boxService?wsdl
wadl2java -p com.ulegalize.lawfirm.model.dpa  https://cs.acc.dp-a.be/api/rest/deposit?_wadl

#### token

curl --include --cookie "dpatoken=AQI..." --header 'softwarekey:220...' --header '
Accept-Language:en' --header 'Accept:application/json' --header 'Content-Type:
application/json' https://cs.dp-a.be/api/rest/user/
curl --include --cookie "dpatoken=Zv..." --header 'softwarekey:220568D250C5B0D5' --header 'Accept-Language:
en' --header 'Accept:application/json' --header 'Content-Type: application/json' https://cs.dp-a.be/api/rest/user/

#### process

https://idp.acc.dp-a.be/oam/?goto=http://localhost:3000/dpa&service=Lawyercard
access token , link https://dpaservices.atlassian.net/wiki/spaces/DID/pages/14450699/OPENID+CONNECT+PROVIDER
https://idp.acc.dp-a.be/oam/oauth2/authorize?response_type=code&client_id=ZvY7Xkyd43x4wdSG&scope=openid%20profile&redirect_uri=http://localhost:3000/dpa&state=af0ifjsldkj

curl --location --request POST 'https://idp.acc.dp-a.be/oam/oauth2/access_token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic Zm...' \
--header 'Cookie: SERVERID=hello1' \
--data-urlencode 'client_id=ZvY...' \
--data-urlencode 'redirect_uri=http://localhost:3000/dp' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=code received at localhost'

Enjoy
