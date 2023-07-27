package com.dyntrace.download.metric.util;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

public class S3Util {

	final private int SUCCESS= 200;
	final private int BAD_REQUEST=400;
	final private int UNAUTHORIZED = 401;
	final private int CONNECTION_ERROR = 404;
	private String accessKey;
	private String secretKey;
	private String sessionToken;
	private StaticCredentialsProvider credentialsProvider;
	private Region region;
	private int statusCode;
	private String message;
	private S3Client client;
	public void setRegion(String regionStr) {
		this.region = Region.of(regionStr);
	}
	public boolean getRole(String role) {
		boolean result = false;
		try {
			AwsCredentialsProvider provider = DefaultCredentialsProvider.create();
			StsClient stsClient = StsClient.builder()
			                                .region(Region.AWS_GLOBAL)
			                                .credentialsProvider(provider)
			                                .build();

			String roleArn = role;
			String sessionName = "current-session";
			AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
			                                                        .roleArn(roleArn)
			                                                        .roleSessionName(sessionName)
			                                                        .build();

			AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);

			software.amazon.awssdk.services.sts.model.Credentials temporaryCredentials = assumeRoleResponse.credentials();
			
			this.accessKey = temporaryCredentials.accessKeyId();
		    this.secretKey = temporaryCredentials.secretAccessKey();
			this.sessionToken = temporaryCredentials.sessionToken();
			
			result = true;
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
		}
		return result;
	}
	/**
	 * 
	 * @return
	 */
	public boolean authenticate() {
		boolean result = false;
		try {

              AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(this.accessKey, this.secretKey, this.sessionToken);
              this.credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
	  		  
	  	      this.client  = S3Client.builder()
	            		.credentialsProvider(this.credentialsProvider)
	                    .region(this.region)  
	                    .build();      
	  	      result = true;
		}catch (SdkClientException e) {
			this.statusCode = CONNECTION_ERROR;
		}catch(Exception e) {
			this.statusCode = UNAUTHORIZED;
		}
		return result;
	}
    public boolean saveToS3(String data, String fileName, String bucketName, String s3RootPath) {
    	boolean result = false;
    	try {

    		String fullPath =s3RootPath  + fileName ;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullPath)
                    .build();
 
            PutObjectResponse response = this.client.putObject(putObjectRequest, RequestBody.fromString(data)); 
            if(response.sdkHttpResponse().isSuccessful()) {
            	result= true;
            }

            
    	}catch (AwsServiceException e) {
        	this.statusCode = CONNECTION_ERROR;
        	this.message = e.getMessage(); 

        }catch (SdkClientException e) {
        	this.statusCode = CONNECTION_ERROR;
        	this.message = e.getMessage(); 
        }catch(Exception e) {
    		this.statusCode = BAD_REQUEST;
    		this.message =e.getMessage();
    	}
  
    	return result;
    }
	
}
