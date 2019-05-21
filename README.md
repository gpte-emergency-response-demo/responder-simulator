Emergency response Responder simulator
=========

Simulates the responders location based on the kafka messages recieved from mission service. The internval settings are in the configmap.


Pre-requisites: Running Kafka Cluster, Configmap

To deploy the service to a running single-node OpenShift cluster:

   ```
$ oc login -u developer -p developer

$ oc new-project MY_PROJECT_NAME

$ oc create -f config/configmap.yml

$ mvn clean fabric8:deploy -Popenshift

   ```
   
 
