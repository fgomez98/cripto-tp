# IMPORTANTE!!
### Java soporta muchos algoritmos de encriptación seguros, pero algunos de ellos son débiles para ser utilizados en aplicaciones.  Para hacer funcionar encriptación de 256bit con AES, hay que instalar las políticas ilimitadas de oracle que lo permiten. 
### Pasos:

1)  Download the unlimited strength JCE policy files.
    https://www.oracle.com/java/technologies/javase-jce8-downloads.html

2)  Uncompress and extract the downloaded file.

    This will create a subdirectory called jce.
    This directory contains the following files:

        README.txt                   This file
        local_policy.jar             Unlimited strength local policy file
        US_export_policy.jar         Unlimited strength US export policy file

3)  Install the unlimited strength policy JAR files.

    In case you later decide to revert to the original "strong" but
    limited policy versions, first make a copy of the original JCE
    policy files (US_export_policy.jar and local_policy.jar). Then
    replace the strong policy files with the unlimited strength
    versions extracted in the previous step.

    The standard place for JCE jurisdiction policy JAR files is:

        <java-home>/lib/security           [Unix]
        <java-home>\lib\security           [Windows]

    
        cd
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

### Si tenes Java 8u152 u superiror olvidate de todo esto.
### Pasos:
    
1)  Buscar esta carpeta
    ```
    cd JDK_HOME/jre/lib/security
    ```
    
2)  Abrir el arcivo java.security

3)  Busca una linea comentada que tenga "crypto.policy=unlimited" y descomentala

4)  Goza  
    
    