AWS_PROFILE=dpopov-admin-kgparser
BUCKET_NAME=kgparser-web

aws s3 cp ./src/main/webapp/error.html s3://$BUCKET_NAME/index.html --profile=$AWS_PROFILE
aws s3 cp ./src/main/webapp/error.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
