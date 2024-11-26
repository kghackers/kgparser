[![Chat on Telegram](https://img.shields.io/badge/Chat%20on-Telegram-brightgreen.svg?style=for-the-badge&logo=telegram)](https://t.me/joinchat/N3tfQFVvmbH21ipSy9yKcA)  [![GitHub issues](https://img.shields.io/github/issues/kghackers/kgparser?style=for-the-badge)](https://github.com/kghackers/kgparser/issues)
[![Github PR](https://img.shields.io/github/issues-pr/kghackers/kgparser?style=for-the-badge)](https://github.com/kghackers/kgparser/pulls)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=kghackers_kgparser&metric=bugs)](https://sonarcloud.io/dashboard?id=kghackers_kgparser) 
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=kghackers_kgparser&metric=code_smells)](https://sonarcloud.io/dashboard?id=kghackers_kgparser)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=kghackers_kgparser&metric=coverage)](https://sonarcloud.io/dashboard?id=kghackers_kgparser)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=kghackers_kgparser&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=kghackers_kgparser)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=kghackers_kgparser&metric=alert_status)](https://sonarcloud.io/dashboard?id=kghackers_kgparser)

# Related resources
* Проект задеплоен на https://klavostat.com
* [Тема на форуме Клавогонок](https://klavogonki.ru/forum/software/59/)
* Мой профиль на Клавогонках — [nosferatum](https://klavogonki.ru/u/#/242585/)
* [Telegram канал](https://t.me/joinchat/N3tfQFVvmbH21ipSy9yKcA) с нотификациями о событиях в этом GitHub-проекте 

# Statistics generation import process
Json config files are used to set and transfer configuration values for main import classes.

## How to execute `PlayerDataDownloader` to download data from Klavogonki to JSON files
`PlayerDataDownloader` takes 2 arguments: `<inputConfigFilePath>` and `<outputConfigFilePath>`.
I will add download start and end dates from `<inputConfigFilePath>` 
and save the update json to `<outputConfigFilePath>`. 


For example, to load players with `playerId in [30000; 30100]` (total 101 players) in 10 threads, set the input json file like this:
```json
{
  "jsonFilesRootDir" : "D:/kg/json",
  "threadsCount" : 10,
  "minPlayerId" : 30000,
  "maxPlayerId" : 30100,
  "statisticsPagesRootDir" : "C:/java/kgparser/kgstats/src/main/webapp/"
}
```

Execution with overriding the `log4j2.xml` configuration file:
```
java -Dlog4j.configurationFile=log4j2.xml -jar kgstatsSrv/target/kgstats-srv-1.0.jar DOWNLOAD_PLAYER_DATA c:/java/config-input.json c:/java/config-output.json
```

Saved output config file will look like this:
```json
{
  "jsonFilesRootDir" : "D:/kg/json",
  "threadsCount" : 100,
  "minPlayerId" : 30000,
  "maxPlayerId" : 30100,
  "dataDownloadStartDate" : "2021-01-17T21:39:19.0911347+01:00",
  "dataDownloadEndDate" : "2021-01-17T21:39:30.0225525+01:00",
  "statisticsPagesRootDir" : "C:/java/kgparser/kgstats/src/main/webapp/",
  "totalPlayers" : 101,
  "dataDownloadStartDateString" : "2021-01-17 21-39-19"
}
```

## How to execute `StatisticsApplication` to import json files save by `PlayerDataDownloader` to the database 
Database must already exist before the execution.

Pass the _output_ json config file saved by `PlayerDataDownloader` as an `<inputConfigFilePath>` for `StatisticsApplication`.

Spring profile `"database"` must be set to turn on the JPA, Spring repositories etc.

This example passes the alternative Spring application properties file to be able to override the database name from the default `application.properties`. 

`log4j2.xml` configures the logging.

```
java -Dlog4j.configurationFile=log4j2.xml -cp kgstats-srv-1.0.jar -Dspring.profiles.active=database -Dspring.config.name=application.actions.properties -Dspring.config.location=kgstatsSrv/src/main/resources/ IMPORT_JSON_TO_DATABASE c:/java/config-output.json 
```

## How to execute `StatisticsApplication` to generate statistics files from the database 
Database must already exist before the execution.

Database must be filled with data on the previous step.

Pass the _output_ json config file saved by `PlayerDataDownloader` as an `<inputConfigFilePath>` for `StatisticsApplication`.

Pass the statistics generation config file as a `<statisticsGeneratorConfigFilePath>` for `StatisticsApplication`.

Spring profile `"database"` must be set to turn on the JPA, Spring repositories etc.

This example passes the alternative Spring application properties file to be able to override the database name from the default `application.properties`. 

`log4j2.xml` configures the logging.

:exclamation: If you have encoding problems in logs and saved files on Windows, also set a `-Dfile.encoding=UTF8` option.

```
java -Dlog4j.configurationFile=log4j2.xml -cp kgstats-srv-1.0.jar -Dspring.profiles.active=database -Dspring.config.name=application.actions.properties -Dspring.config.location=kgstatsSrv/src/main/resources/ GENERATE_STATISTICS_FROM_DATABASE c:/java/config-output.json c:/java/generator-config.json
```

# Какие графики и таблицы можно сделать на текущих данных
* Количество игроков по рангам
* Количество игроков по годам регистрации
* Количество игроков по месяцам регистрации
* Топ N (например, топ-100) игроков по:
    * абсолютному рекорду в обычном
    * по количеству друзей
    * по общему пробегу
    * по количеству используемых словарей
    * по рейтинговому уровню 
    * по количеству достижений 
    * по количеству машин в гараже 
* Пользователи без логина (`"login": ""`)
    * См. https://klavogonki.ru/u/#/109842/
    * На самом деле, таких юзеров полно среди заблокированных, например https://klavogonki.ru/u/#/221411/ и https://klavogonki.ru/u/#/221421/
* Пользователи со скрытым профилем (`"err": "hidden profile"`)
    * См. https://klavogonki.ru/u/#/21/
* Заблокированные пользователи (`"blocked": 1` и вообще `blocked != 0`)
   * У заблокированных пользователей возможна безумная отрицательная дата регистрации (или это только Artch), см. https://klavogonki.ru/u/#/141327/.
   * У https://klavogonki.ru/u/#/141327/ ещё и первая буква А в нике русская :)
   * https://klavogonki.ru/u/#/142478/ — пример с `"blocked": 4`.
   * Сгруппировать по `blocked != 0`
* Пользователи с невалидной датой регистрации
   * См. https://klavogonki.ru/u/#/141327/
* Клавомеханики (`"title": "Клавомеханик"`)
* Пользователи с одинаковыми никами (case-sensitive)
* Пользователи, для которых `get-summary` работает, а `get-index-data` возвращает ошибку
    * https://klavogonki.ru/u/#/161997/
    * https://klavogonki.ru/u/#/498727/ — ошибка на ачивках в MongoDB. 
    * Возможно, это какой-то баг в базе. Например, при импорте текстов из старой базы.
    * (!) Таких юзеров нужно специально парсить для отчётов, так как нет данных, например, по рекордам.
* Количество юзеров без набранных текстов
* Количество юзеров без рекорда в обычном
    * Вообще без текстов
    * С текстами (это значит, что весь пробег был в других режимах)
        * Такие спешиал юзеры можно прям списком
* Машины — использующее количество игроков
    * Нужны коды машин
    * Это будут _только текущие машины_, а НЕ машины в гараже.
* Уровень рейтинга — использующее количество игроков
* Количество действующих id и недействующих id
    * Минимальный действительный id 
    * Максимальный действительный id 
* Минимальная дата регистрации
* Максимальная дата регистрации
* Самый короткий логин
* Самый длинный логин
* Корреляция общего пробега и рекорда
   * **TODO:** а как выбирать данные для корреляции?
* Группировка по общему пробегу: 0, 1-99, 100-999, 1000-9999, 10000-99999, >100000
   
# MySQL

## Create a read-only user
See https://kodejava.org/how-to-create-a-read-only-mysql-user/ 

User name: `report`, user password `secret`:
```
CREATE USER 'report'@'%' IDENTIFIED BY 'secret';

GRANT SELECT ON kgparser.* TO 'report'@'%';

FLUSH PRIVILEGES;
```

## Dump the database
See https://phoenixnap.com/kb/how-to-backup-restore-a-mysql-database
```
mysqldump -u root -p kgparser > c:\java\kg\kgparser.sql
```

## Restore the database from dump
See https://alvinalexander.com/blog/post/mysql/how-restore-mysql-database-from-backup-file/
To restore the dump, we first need to create the database.
```
mysqladmin -u root -p create kgparser_from_dump
mysql -u root -p kgparser_from_dump < c:\java\kg\kgparser.sql
```

## Drop the database
See https://linuxize.com/post/how-to-delete-a-mysql-database/ 
```
mysqladmin -u root -p drop kgparser-from-dump
```

# Maven

## Install main modules without tests and without javadoc generation
From root directory, run
```
mvn install -DskipTests=true -Dmaven.javadoc.skip=true
```

## Execute MapStruct generation in `kgstatsSrv` 
From `kgstatsSrv` directory, run
```
mvn compile
```

:exclamation: Now after making changes in `kgparserSrv`, you have to `mvn install`
this module before running MapStruct in `kgstatsSrv`.

## Legacy modules and Maven profile

By default, the legacy modules `kgparserSrv` and `kgparserWeb` are excluded from the build.

If you want to turn them on, you have to switch on the Maven `legacy-kgparser` profile.

For example:

```bash
mvn clean -P legacy-kgparser
```

```bash
mvn install -P legacy-kgparser
```