# apiman-prometheus

Simple example of using Prometheus to expose JVM metrics.

## Prerequisites

Download Tomcat 8.5 and unzip:

    unzip apache-tomcat-8.5.51.zip
    cd apache-tomcat-8.5.51/bin
    chmod +x *.sh
    ./catalina.sh run

Tomcat is running.
    
## Build and run

    mvn clean package
    mv servlet/target/servlet-1.0-SNAPSHOT.war apache-tomcat-8.5.51/webapps/apiman-prometheus.war

The webapp is deployed.

## Hit the app:

    while true; do curl http://localhost:8080/apiman-prometheus; sleep 2; done

Notice the counter in the Tomcat logs:

    Counter: 44.0
    Counter: 45.0
    Counter: 46.0
    Counter: 47.0

## Get metrics

Hit the Prometheus endpoint:

    curl http://localhost:9999/prometheus

Here you can see JVM metrics and a single custom metric, named 'requests_total':

```
# HELP jvm_buffer_count_buffers An estimate of the number of buffers in the pool
# TYPE jvm_buffer_count_buffers gauge
jvm_buffer_count_buffers{id="direct",} 15.0
jvm_buffer_count_buffers{id="mapped",} 0.0
# HELP requests_total  
# TYPE requests_total counter
requests_total 52.0
# HELP jvm_memory_max_bytes The maximum amount of memory in bytes that can be used for memory management
# TYPE jvm_memory_max_bytes gauge
jvm_memory_max_bytes{area="heap",id="PS Survivor Space",} 1.6252928E7
jvm_memory_max_bytes{area="heap",id="PS Old Gen",} 2.863661056E9
jvm_memory_max_bytes{area="heap",id="PS Eden Space",} 1.39722752E9
jvm_memory_max_bytes{area="nonheap",id="Metaspace",} -1.0
jvm_memory_max_bytes{area="nonheap",id="Code Cache",} 2.5165824E8
jvm_memory_max_bytes{area="nonheap",id="Compressed Class Space",} 1.073741824E9
# HELP jvm_buffer_total_capacity_bytes An estimate of the total capacity of the buffers in this pool
# TYPE jvm_buffer_total_capacity_bytes gauge
jvm_buffer_total_capacity_bytes{id="direct",} 122880.0
jvm_buffer_total_capacity_bytes{id="mapped",} 0.0
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap",id="PS Survivor Space",} 0.0
jvm_memory_used_bytes{area="heap",id="PS Old Gen",} 1.9286448E7
jvm_memory_used_bytes{area="heap",id="PS Eden Space",} 1.0383116E8
jvm_memory_used_bytes{area="nonheap",id="Metaspace",} 2.1880704E7
jvm_memory_used_bytes{area="nonheap",id="Code Cache",} 1.0099328E7
jvm_memory_used_bytes{area="nonheap",id="Compressed Class Space",} 2397096.0
# HELP jvm_memory_committed_bytes The amount of memory in bytes that is committed for the Java virtual machine to use
# TYPE jvm_memory_committed_bytes gauge
jvm_memory_committed_bytes{area="heap",id="PS Survivor Space",} 1.6252928E7
jvm_memory_committed_bytes{area="heap",id="PS Old Gen",} 9.1226112E7
jvm_memory_committed_bytes{area="heap",id="PS Eden Space",} 8.5721088E8
jvm_memory_committed_bytes{area="nonheap",id="Metaspace",} 2.2986752E7
jvm_memory_committed_bytes{area="nonheap",id="Code Cache",} 1.015808E7
jvm_memory_committed_bytes{area="nonheap",id="Compressed Class Space",} 2801664.0
# HELP jvm_buffer_memory_used_bytes An estimate of the memory that the Java virtual machine is using for this buffer pool
# TYPE jvm_buffer_memory_used_bytes gauge
jvm_buffer_memory_used_bytes{id="direct",} 122880.0
jvm_buffer_memory_used_bytes{id="mapped",} 0.0
```
