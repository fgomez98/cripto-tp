# IMPORTANTE!!

### Para poder utilizar el stegobmp es necesario instalar las "unlimited strength JCE policy files" de oracle.
### Si ya tenes tu entorno configurado con estas politicas podes omitir este paso.
### Pasos:
#### Si tenes Java 8u152 u superiror:

1)  Situarse con el comando cd en el siguiente directorio
    ```
    cd <JDK_HOME>/jre/lib/security
    ```
    
2)  Abrir el arcivo java.security

3)  Busca una linea comentada que tenga "crypto.policy=unlimited" y descomentarla
    
#### Caso contrario:

1)  Descargar las "unlimited strength JCE policy files".
    https://www.oracle.com/java/technologies/javase-jce8-downloads.html

2)  Descomprimir y extraer el archivo descargado.

    Se creara un subdirectorio con los siguintes archivos:

        README.txt                   
        local_policy.jar        ->    Unlimited strength local policy file
        US_export_policy.jar    ->    Unlimited strength US export policy file

3)  Instalar las politicas.

    ```
    cd <java-home>/lib/security
    
    # hacemos un back-up de las politicas default  
    sudo mv local_policy.jar local_policy.jar.bak
    sudo mv US_export_policy.jar US_export_policy.jar.bak
    
    # descomprimimos las politicas nuevas
    tar -xzf donde sea que lo quieras guardar/jce_policy-8.zip
    
    sudo cp UnlimitedJCEPolicyJDK8/local_policy.jar local_policy.jar
    sudo cp UnlimitedJCEPolicyJDK8/US_export_policy.jar US_export_policy.jar
    ```    

# Instrucciones de uso

Situarse en la raiz y ejecutar los comandos segun corresponda

#### Compilacion
```
mvn clean install
```
    
#### Ejecución
Otorgar permisos de ejecución en caso de ser necesario
```
chmod 777 ./stegobmp.sh
``` 
Para correr el programa ejecutar el siguiente comando con los parametros deseados
 ```
 ./stegobmp.sh 
 ```
Posibles parametros:
 * -a [AES128 | AES192 | AES256 | DES] : Algoritmo de encripción (default: AES128)
 * -embed                              : Indica que se va a ocultar información
 * -extract                            : Indica que se va a extraer información
 * -in STRING[]                        : Archivo que se va a ocultar
 * -m [ECB | CFB | OFB | CBC]          : Modo de encripción (default: CBC)
 * -out STRING[]                       : Archivo bmp de salida, es decir, el archivo bitmapfile con la información de file incrustada.
 * -p STRING[]                         : Archivo bmp que será el portador.
 * -pass STRING[]                      : password de encripcion
 * -steg [LSB1 | LSB4 | LSBI | MIRROR] : Algoritmo de esteganografiado: LSB de 1bit, LSB de 4 bits, LSB Improved

#### Ejemplos Oportunos
###### Ejemplo 1
  ```
    ./stegobmp.sh -extract -p "./archivos/lado.bmp" -out "./archivos/hidden-message" -steg LSB4
  ```
###### Ejemplo 2
  ```
    ./stegobmp.sh -extract -p "./archivos/lima.bmp" -out "./archivos/hidden-message" -steg LSBI
  ```
###### Ejemplo 3
  ```
    ./stegobmp.sh -extract -p "./archivos/silence.bmp" -out "./archivos/hidden-message" -steg LSB1 -a AES256 -m CFB -pass "solucion"
  ```
###### Ejemplo 4
  ```
    ./stegobmp.sh -embed -in "./archivos/small-message.txt" -p "./archivos/white.bmp" -out "./archivos/hidden-message.bmp" -steg LSB1
  ```
###### Ejemplo 5
  ```
    ./stegobmp.sh -embed -in "./archivos/small-message.txt" -p "./archivos/white.bmp" -out "./archivos/hidden-message.bmp" -steg LSB1 -a AES256 -m CFB -pass "solucion"
  ```