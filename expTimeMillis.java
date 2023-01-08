                public S3PayloadStorage(IDGenerator idGenerator, S3Properties properties) {
                  this.idGenerator = idGenerator;
                  bucketName = properties.getBucketName();
                  expirationSec = properties.getSignedUrlExpirationDuration().getSeconds();
                  String region = properties.getRegion();
                  s3Client = AmazonS3ClientBuilder.standard().withRegion(region).build();
            }


            Date expiration = new Date();
            long expTimeMillis = expiration.getTime() + 1000 * expirationSec;
            expiration.setTime(expTimeMillis);
