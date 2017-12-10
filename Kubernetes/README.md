# Step1(Optional): Install KubeCtl Kubernetes on MAC/OS

 https://kubernetes.io/docs/tasks/tools/install-kubectl/

 KubeCtl Overview
 https://kubernetes.io/docs/reference/kubectl/overview/



# Step2: Install Kubernetes Minikube instead of Kube-Solo to Start Kubernetes Cluster on MAC/OS

 https://deis.com/docs/workflow/quickstart/provider/minikube/boot/
 https://kubernetes.io/docs/getting-started-guides/minikube/

# Hello MiniKube Example

 https://kubernetes.io/docs/tutorials/stateless-application/hello-minikube/

 %./minikube.run


# Step3(Optional): Run a Hello World Application using the running Kubernetes cluster

 https://kubernetes.io/docs/tasks/access-application-cluster/service-access-application-cluster/

# Execute Steps 1-9

  kubectl run hello-world --replicas=2 --labels="run=load-balancer-example" --image=gcr.io/google-samples/node-hello:1.0  --port=8080

# Step4(Optional): Run the Docker Image for the REST Service

 https://kubernetes.io/docs/tasks/access-application-cluster/service-access-application-cluster/

 Execute Steps 1-9

 kubectl run spring-boot-web --replicas=2 --labels="run=load-balancer-example" --image=joethecoder2/spring-boot-web  --port=8080

%./minikube.setup

# StepAppendix(Deprecated from 2016 Do Not Use): Install Microsoft's Deis Kube-Solo for MACOS
   https://deis.com/blog/2016/run-kubernetes-on-a-mac-with-kube-solo/


# Step5: Execute Kubernetes curl tests for example-service
%./kubectl.test


# Current Limitations

 Kubernetes single POD can not yet read arguments to pass IPAddress and Port to REST Controller for a remote Cassandra node.
 - need to add commands to singlePod.yaml
 - consider launching two docker images within one POD to doublePod.yaml

