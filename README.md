# Demo App to showcase fallback from Primary to DR after exhausting the retries on Primary
## IMPORTANT
**Transaction latencies will increase significantly with this approach and I don't recommend using this approach. I have another github project which demonstrates automatic failover. Strongly recommend that approach:  https://github.com/akscjo/yb-active-active-auto-switch-demo**

## Goal
Keep the application running even if the primary database goes down by connecting to the secondary database in an Active-Active Bi-Directional Multi-Master setup.

### What does this Demo App do?
- App connects to both primary and dr databases.
- Rest API adds new record in customer table. For example:
`    /api/add
`
- There is retry logic built into this transaction. In case of a failure, app will retry the transactions few times with exponential backoff strategy.
- After the retries are exhausted on the primary cluster, it will fallback to DR cluster and try the transcation on that.
- **The latency shoot up significantly. Imagine your primary cluster goes down - you will make multiple retries on primary cluster (say for 30s) then only you fallback to DR. This will happen for every transaction.**


## Deployment Topology

![Topology](src/main/resources/images/topology.png?raw=true "Title")



### How to use this App
- Download the latest release from "releases" section (or build your own from source code -` mvn clean package -DskipTests`)
- Create the schema in your databases using `resources/schema.sql`
- You can override several parameters (see application.yaml)
- Here is one sample command
```
java \
-Dspring.datasource.hikari.username=yugabyte \
-Dspring.datasource.hikari.password=yugabyte \
-Dspring.datasource.hikari.maximumPoolSize=10 \
-Dspring.datasource.hikari.data-source-properties.serverName=127.0.0.2 \
-Dspring.datasource.hikari2.username=yugabyte \
-Dspring.datasource.hikari2.password=yugabyte \
-Dspring.datasource.hikari2.maximumPoolSize=10 \
-Dspring.datasource.hikari2.data-source-properties.serverName=127.0.0.3 \
-jar yb-active-active-auto-switch-demo.jar
```

- Start your simulation using tools like jmeter, postman, etc. Sample jmeter jmx test plan is available in resources/jmeter
```
jmeter -n -t testplan.jmx -l results.jtl
```
- On starting the simulation, you will note that logs indicate that primary datasource is being used
  ![logs](src/main/resources/images/logs1.png?raw=true "Title")
- Failover your primary datasource. You will notice the traffic going to primary and perform  retries on primary. 
- Once the retries on primary cluster are exhausted, you will see the transaction fallback on DR cluster. 
- Analyze the results. All the transactions should be successful since they will be retried  if primary cluster fails.
  ![jmeter](src/main/resources/images/jmeter-status.png?raw=true "Title")



