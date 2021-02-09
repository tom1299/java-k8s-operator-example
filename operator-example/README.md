# operator-example project
A simple k8s operator that writes the current list of pods deployed in a namespace to a slack channel

## Building
Using `Slack` as a non extension for quarkus caused problems during building so the safest way was to use the
fast jar:
```
./gradlew build -Dquarkus.package.type=fast-jar
```

## Building the docker image
The docker image build is straight forward:
```
docker build -f src/main/docker/Dockerfile.fast-jar -t ghcr.io/tom1299/k8s-udp-load-balancing/quarkus-k8s-operator:latest .
docker push ghcr.io/tom1299/k8s-udp-load-balancing/quarkus-k8s-operator:latest
```

## k8s Deployment
All k8s deployment files are located in the folder [k8s](./k8s). The deployment needs to be parameterized by
supplying the following values:
* `SLACK_BOT_TOKEN`: The token of the slack app
* `SLACK_CHANNEL`: The id of the channel to write to
* `K8S_NAMESPACE`: The namespace to read the list of pods from

## Additional notes
When deploying this application behind a proxy, don't forget to set the proxy environment variables accordingly.