S3_BUCKET_TEST=klavostat-test
S3_BUCKET_PRODUCTION=klavostat.com

CLOUD_FRONT_DISTRIBUTION_KLAVOSTAT_COM=E1X4IU1PTHKQR5
CLOUD_FRONT_DISTRIBUTION_WWW_KLAVOSTAT_COM=ECV5IZ62LLD5G

# delete everything from prod bucket
aws s3 rm s3://$S3_BUCKET_PRODUCTION --recursive

echo "Deleted all files from production S3 bucket $S3_BUCKET_PRODUCTION."

# copy from test bucket to prod bucket
aws s3 cp s3://$S3_BUCKET_TEST s3://$S3_BUCKET_PRODUCTION --recursive

echo "Copied all files from test S3 bucket $S3_BUCKET_TEST to production S3 bucket $S3_BUCKET_PRODUCTION."

# invalidate CloudFront distribution for klavostat.com
aws cloudfront create-invalidation --distribution-id $CLOUD_FRONT_DISTRIBUTION_KLAVOSTAT_COM --paths "/*"

echo "Distribution $CLOUD_FRONT_DISTRIBUTION_KLAVOSTAT_COM for klavostat.com invalidated."

# invalidate CloudFront distribution for www.klavostat.com
aws cloudfront create-invalidation --distribution-id $CLOUD_FRONT_DISTRIBUTION_WWW_KLAVOSTAT_COM --paths "/*"

echo "Distribution $CLOUD_FRONT_DISTRIBUTION_WWW_KLAVOSTAT_COM for www.klavostat.com invalidated."
