# chaoscraft

chaoscraft is team FuwaFuwaTime/滑滑蛋's project for TiDB Hackathon 2021.

## Proposal

Build an observer system and chaos experiment control system for any kubernetes workload.

There are several key features:

- Display workloads as entities, show their name as entities name.
- Show resource usage(CPU, Memory) of each workload.
- Show network traffic of each workload.
- Inject chaos into workloads.

## Design

There are several parts in this project:

- chaoscraft-mod
- chaoscraft-control-center

### chaoscraft-mod

chaoscraft-mod is a (serverside) mod for minecraft, it provides

- mapping kubernetes workloads to minecraft entities.
- show communication(network traffic) between entities.
- modded weapon for injecting chaos experiment.

### chaoscraft-control-center

chaoscraft-control-center is the bridge between chaoscraft-mod and kubernetes. It grabs data from kubernetes API and
cilium hubble.
