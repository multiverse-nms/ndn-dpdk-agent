# nms-agent
 
 
#### Tasks
 
* [X] fw: create face 
* [X] fw: update FIB 
* [X] fw: accept command from event bus 
* [X] rib: turn into a verticle 
* [X] rib: RIB2FIB, FIBdiff 
* [X] rib: send command to *fw* 
* [X] rib: accept command from event bus 
* [X] cli: command line client  
* [X] cli: send command to *rib* instead of *fw*


#### What you’ll need
 
- JDK 9 or later
- Install Maven

#### Build the Agent

`mvn compile`
- To run Maven, telling it to execute the compile goal.

`mvn package`
- To compile, run tests and package your code in a JAR file that can be found in the target directory.

`mvn install`
- Compile, test, package your code and copy it in the local dependency repository.
 
 
 #### Run the Agent
 
 To run the agent use the following command, where `<target>` is the path to your .jar file that was generated during the `mvn package` step.
 
 `java -jar <taget>`

 
 
