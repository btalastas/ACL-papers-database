# ACL-papers-database

## ***Description***

JDBC project that utilizes Oracle database and SQL to store and retrieve ACL Papers.

## ***How to Run***

Navigate to a directory and run the following command

```sh
git clone https://github.com/btalastas/ACL-papers-database.git
```

Compile the java files

```sh
javac -cp .:mybatis-3.5.7.jar:ojdbc11.jar *.java
```

Run the java file

```sh
java -cp .:mybatis-3.5.7.jar:ojdbc11.jar ACL_Papers
```

## ***Examples***

Running SQL Script into Oracle database

![example 1][example]

Viewing database contents

![example 2][example2]

Updating URL for a publication

![example 3][example3]

[example]: ./pictures/example.gif
[example2]: ./pictures/example2.gif
[example3]: ./pictures/example3.gif
