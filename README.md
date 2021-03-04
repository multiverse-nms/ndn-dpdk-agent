<h1 align="center"> Multiverse Network Management System [NDN-DPDK Agent] </h1>
<!-- p align="center">
  <img src="docs/images/logo.png" />
</p -->

## Overview 

This is the NDN-DPDK Agent component of the [Multiverse project](https://github.com/multiverse-nms/multiverse-controller).
This agent supports the management of the [NDN-DPDK](https://github.com/usnistgov/ndn-dpdk) forwarder.

## Deployment Instructions

The agent is deployable as a Docker container.
The following instructions have been tested on Ubuntu (16.04, 18.04, 20.04) and Windows 10.

> Note: The agent requires the Multiverse controller to be already running.

### Prerequisites

- Docker (19.03)
- Git Bash (for Windows)

### Create the agent profile

To run properly, the agent needs a configuration file and the Multiverse root CA certificate generated during the deployment of the [Controller](https://github.com/multiverse-nms/multiverse-controller#prepare-the-system).

Create the agent configuration profile by issuing the following commands:
```bash
mkdir my-agent ; cd my-agent ; mkdir ca
curl https://raw.githubusercontent.com/multiverse-nms/ndn-dpdk-agent/master/src/config/config.json -o config.json
```

Update the `config.json` file to fit the NDN-DPDK forwarder and Multiverse controller parameters (e.g., host and port).

Then, place the `<certs_location/ca/MultiverseRootCA.crt.pem>` file in `my-agent/ca/` folder.

### Build and Run
```bash
git clone https://github.com/multiverse-nms/ndn-dpdk-agent.git
cd ndn-dpdk-agent
docker build --tag ndn-dpdk-agent .
docker run -it --rm --name my-agent --network="host" -v <path/to/my-agent>:/opt/data ndn-dpdk-agent
```

> Note: Replace `<path/to/my-agent>` by the actual location of the `my-agent` folder created above.