/**
     * @param operation the type of {@link Operation} to be performed
     * @param payloadType the {@link PayloadType} that is being accessed
     * @return a {@link ExternalStorageLocation} object which contains the pre-signed URL and the s3
     *     object key for the json payload
     */
    @Override
    public ExternalStorageLocation getLocation(
            Operation operation, PayloadType payloadType, String path) {
        try {
            ExternalStorageLocation externalStorageLocation = new ExternalStorageLocation();

            Date expiration = new Date();
            long expTimeMillis = expiration.getTime() + 1000 * expirationSec;
            expiration.setTime(expTimeMillis);

            HttpMethod httpMethod = HttpMethod.GET;
            if (operation == Operation.WRITE) {
                httpMethod = HttpMethod.PUT;
            }

            String objectKey;
            if (StringUtils.isNotBlank(path)) {
                objectKey = path;
            } else {
                objectKey = getObjectKey(payloadType);
            }
            externalStorageLocation.setPath(objectKey);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(httpMethod)
                            .withExpiration(expiration);

            externalStorageLocation.setUri(
                    s3Client.generatePresignedUrl(generatePresignedUrlRequest)
                            .toURI()
                            .toASCIIString());
            return externalStorageLocation;
        } catch (SdkClientException e) {
            String msg =
                    String.format(
                            "Error communicating with S3 - operation:%s, payloadType: %s, path: %s",
                            operation, payloadType, path);
            LOGGER.error(msg, e);
            throw new TransientException(msg, e);
        } catch (URISyntaxException e) {
            String msg = "Invalid URI Syntax";
            LOGGER.error(msg, e);
            throw new NonTransientException(msg, e);
        }
    }
