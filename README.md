Data Warehouse
=============

The NYU Libraries DataWarehouse is a utility library for retrieving data from a data warehouse.
NYU connects to a variety of databases in the warehouse for Primo enrichment plugins,
but the library can be used for any processes requiring connection to RDBMS.

The NYU Libraries DataWarehouse expects a file specifying the connection information.

For more information, see the [API documentation](http://nyulibraries.github.com/datawarehouse/apidocs)

The project leverages [Apache Maven](http://maven.apache.org/) for managing dependencies, building, packaging and generating Javadocs.

To install the package locally, run:

    $ mvn install

NYU uses SQL Server and therefore requires the SQL Server JDBC driver to 
connect to our servers.  In our tests we use this driver.

Once you download the driver, you must it to your local maven repository.

    $ mvn install:install-file -Dfile=sqljdbc4.jar \ 
    -Dpackaging=jar -DgroupId=com.microsoft.sqlserver \ 
    -DartifactId=sqljdbc4 -Dversion=4.0

The NYU Libraries uses [Capistrano](https://github.com/capistrano/capistrano) as its deploy tool. 
The deploy mechanism assumes [rvm](https://rvm.io/ "Ruby Version Manager") and 
[Ruby 1.9.3-p125](http://www.ruby-lang.org/en/news/2012/02/16/ruby-1-9-3-p125-is-released/) 
on the local deploy host but they may not be necessary. 
In order to deploy: 

    $ cap [-S branch=<branch-name>] [-S user=<user-name>] [staging|production] deploy:<task>

Since the DataWarehouse library can be leveraged across systems, deploy tasks are created for specific deploys.
Currently the following deploy tasks are available

  - enrichment
