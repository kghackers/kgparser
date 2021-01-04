# todo: move stat web sources to a separate module, copy like /js/*, /css/*, *.html etc, not one-by-one

AWS_PROFILE=dpopov-admin-kgparser
BUCKET_NAME=klavostat.com

aws s3 cp ./src/main/webapp/ s3://$BUCKET_NAME --profile=$AWS_PROFILE --recursive


#aws s3 cp ./src/main/webapp/_top-by-chars.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_top_by_races_count_in_noerror.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_top_by_races_count_in_normal.html s3://$BUCKET_NAME --profiple=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_top_by_races_count_in_marathon.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_top_by_best_speed_voc-40933.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_top_by_best_speed_voc-62238.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_players-with-hidden-stats.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_vocabularies_by_total_races_count.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/_vocabularies_by_total_players_count.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/error.html s3://$BUCKET_NAME --profile=$AWS_PROFILE
#aws s3 cp ./src/main/webapp/2012-12-09/stats.html s3://$BUCKET_NAME/2012-12-09/stats.html --profile=$AWS_PROFILE
