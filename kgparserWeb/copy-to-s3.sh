# todo: move stat web sources to a separate module, copy like /js/*, /css/*, *.html etc, not one-by-one

AWS_PROFILE=dpopov-admin-kgparser
BUCKET_NAME=kgparser-web

aws s3 cp ./src/main/webapp/ s3://$BUCKET_NAME --profile=$AWS_PROFILE --recursive
