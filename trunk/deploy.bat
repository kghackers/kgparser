set JBOSS_HOME=C:\jboss\jboss-4.2.3.GA\server\default
set DEPLOY_DIR=%JBOSS_HOME%\deploy
set KGPRASER_SRV_PATH=%CD%\kgparserSrv\target\kgparser-srv-1.0.jar
set KGPRASER_WEB_PATH=%CD%\kgparserWeb\target\kgparser-web-1.0.war

copy /B /V /Y %KGPRASER_SRV_PATH% %DEPLOY_DIR%
copy /B /V /Y %KGPRASER_WEB_PATH% %DEPLOY_DIR%