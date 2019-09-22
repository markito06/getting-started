#### Send Secret Attach

An initiation project with the [https://quarkus.io](https://quarkus.io) solution.

##### Goal
 - Learn about cryptography.
 - Mode : AES/GCM/NoPadding
 
##### Environment
 - Java 8 +.
 - Maven 3.5 +. 

##### Start 

```
./mvnw compile quarkus:dev: --debug \
	-Dquarkus.mailer.from=account@gmail.com \
	-Dquarkus.mailer.username=account@gmail.com \
	-Dquarkus.mailer.password=mysecretekey
```
##### Usage  
 - Send

```
curl -X POST \
  http://localhost:8080/app/send/<mailto>/<key_to_encrypt> \
  -F 'file=@/home/user/somefile'
```
 - Decript

``` 
 curl -X POST \
  http://localhost:8080/app/receive/<key_to_decrypt> \
  -F 'file=@/home/user/some_encripted_file'  

