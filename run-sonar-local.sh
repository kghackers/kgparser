SONAR_PROJECT=kgparser
SONAR_HOST=http://localhost:9000
SONAR_TOKEN=f104860423196387cbe92048215fb58cc3fdd300

# that is what my local Sonar suggests to execute:
# mvn sonar:sonar \
#   -Dsonar.projectKey=kgparser \
#   -Dsonar.host.url=http://localhost:9000 \
#   -Dsonar.login=f104860423196387cbe92048215fb58cc3fdd300

# setting org on local sonar will fail with
# [ERROR] Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar (default-cli) on project kgparser: Failed to upload report - Organization with key 'kghackers' does not exist
#-Dsonar.organization=kghackers        \

mvn -B verify -Dmaven.javadoc.skip --no-transfer-progress sonar:sonar \
-Dsonar.projectKey=$SONAR_PROJECT \
-Dsonar.host.url=$SONAR_HOST \
-Dsonar.login=$SONAR_TOKEN
