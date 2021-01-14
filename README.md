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

## Test
# How to execute `PlayerDataDownloader`
Execution with overriding the `log4j2.xml` configuration file:

For example, to load players with `playerId in [30000; 30100]` (total 101 players) in 10 threads, execute
```
java -Dlog4j.configurationFile=log4j2.xml -cp kgparser-srv-1.0.jar ru.klavogonki.kgparser.PlayerDataDownloader c:/java/kg 30000 30100 10
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

## Execute MapStruct generation in `kgparserSpringBoot` 
From `kgparserSpringBoot` directory, run
```
mvn compile
```

:exclamation: Now after making changes in `kgparserSrv`, you have to `mvn install`
this module before running MapStruct in `kgparserSpringBoot`.
