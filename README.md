# securityAndPrivacyLastProject

## Running the project
The projects need a few things to run:
- A MySQL database
- Java
- Maven

### Database
The database is a MySQL database. You can download it from [here](https://dev.mysql.com/downloads/mysql/). 

#### Docker
If you want to use a docker image, you can use the following command:
```bash
docker run -e MYSQL_ROOT_PASSWORD=ROOT_PASSWORD -p 3306:3306 -d mysql
```
Once the image is running, you can configure the database by running the following commands:
```bash
sudo docker exec -i <docker-id> mysql -u root -pROOT_PASSWORD < ./db/Chinook_MySql.sql
```

### Run the Java project
To run the Java project, you need to have Java and Maven installed. You can download them from [here](https://www.oracle.com/java/technologies/javase-downloads.html) and [here](https://maven.apache.org/download.cgi).

Once you have installed Java and Maven, you can run the project by running the following command:
```bash
mvn spring-boot:run
```
Or you can run the project from your IDE.