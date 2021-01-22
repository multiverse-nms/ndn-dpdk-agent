# nms-agent
 
#### What you’ll need
 
- JDK 8 or later
- Install Maven

#### Clone the repo

`git clone https://github.com/elmiomar/nms-agent.git`

#### Build the Agent

To build and install the agent, run the following inside the root folder:

- To run Maven, telling it to execute the compile goal: `mvn compile`

- To compile, run tests and package your code in a JAR file that can be found in the target directory: `mvn package`

- Compile, test, package your code and copy it in the local dependency repository: `mvn install`

 
 
 #### Run the Agent
 
 To run the agent use the following command, where `<target>` is the path to your .jar file that was generated during the `mvn package` step.
 
 `java -jar <target>`

 
#### Build and deploy using Docker

A Dockerfile is provided that allows to build a Docker image for the image for the agent.

- To build the image:
`sudo docker build --tag nms-agent:dev`


- To run the agent inside a docker container:
`sudo docker run -it --rm --name nms-agent nms-agent`

> - `-it` : to run container in interactive mode, this way we can stop agent using Ctrl+C
> - `--rm`: automatically removes the container when it exists, allowing us to start the container again without the need to manually remove it


The agents are **statically** configured to connect to the controller automatically on address `10.0.31.26`. This normally should be configured using external files or environement variables (todo).


### Testing using Emulab

- Connect to ANTD's emulab:

```bash
$ ssh access.antd.nist.gov
```

> Note: request credentials from antd/emulab admins if needed

To run the agent on all the nodes in the experiment, run the following commands for each node:

- SSH to the emulab node:

```bash
$ ssh username@node-qualified-name
```

- Clone the agent repo

> Note: assumming git and docker are properly installed

```bash
$ git clone https://github.com/elmiomar/nms-agent.git
```

Then follow same steps from above to deploy the agent in a container.
