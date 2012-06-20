Datawarehouse
=============

The NYU Libraries DataWarehouse is a utility library for retrieving data from a data warehouse.
NYU connects to a variety of databases in the warehouse for Primo enrichment plugins,
but the library can be used for any processes requiring connection to RDBMS.

The NYU Libraries DataWarehouse expects a file specifying the connection information.

The project leverages [Apache Maven](http://maven.apache.org/) for building and deploying jars.

To install the package locally run:

    $ mvn install

NYU uses SQL Server and therefore requires the SQL Server JDBC driver to 
connect to our servers.  In our tests we use this driver.

Once you download the driver, you must it to your local maven repository.

    $ mvn install:install-file -Dfile=sqljdbc4.jar \ 
    -Dpackaging=jar -DgroupId=com.microsoft.sqlserver \ 
    -DartifactId=sqljdbc4 -Dversion=4.0

The NYU Libraries uses [Capistrano](https://github.com/capistrano/capistrano) as its deploy tool.
The deploy mechanism assumes rvm on the deploy host. In order to deploy:

    $ git clone git@github.com:NYULibraries/datawarehouse.git
    $ cd datawarehouse
    ...
    Do you wish to trust this .rvmrc file?
    y[es], n[o], v[iew], c[ancel]> yes
    $ bundle install
    ...
    $ cap [-S branch=<branch-name>] [-S user=<user-name>] [staging|production] deploy:<task>

Since the DataWarehouse libraries can be leveraged across systems, deploy tasks are created for specific deploys.
Currently the following deploy tasks are available

  - deploy:enrichment

[Javadocs](http://nyulibraries.github.com/datawarehouse/apidocs/)