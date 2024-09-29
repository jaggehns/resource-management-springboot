# Resource Management - Spring Boot

## Overview

This is a simple **Resource Management application** built using Java Spring Boot. It processes requests to access a shared resource, ensuring that only one request can access the resource at a time, with a mandatory 2-second interval between accesses. The system also supports priority requests, where higher-priority requests are processed ahead of lower-priority ones. 

- **No database was used so we can focus on Concurrency and Priority Handling**.

## Technologies Used

- **Java 8**: The core programming language for the application.
- **Spring Boot**: For creating the REST API and managing the backend.
- **Maven**: For managing dependencies and building the project.

## Features

- **Single Resource Access** - Enforces a delay of 2 seconds between requests to access the resource.
  
- **Request Prioritization** - Requests can be added with varying priorities, and higher-priority requests are processed before lower-priority ones.
  
- **Concurrency Management** - Ensures that multiple incoming requests are queued and processed sequentially, maintaining thread safety.

## How to Run the Application

1. **Clone the repository**:
    ```bash
    git clone https://github.com/jaggehns/resource-management-springboot.git
    cd resource-management-springboot
    ```

2. **Build and run the application**:
    - Build the project using Maven:
        ```bash
        mvn clean install
        ```
        
    - Run the application:
        ```bash
        mvn spring-boot:run
        ```

   - The application will start on the default port `8080`.

## REST API Endpoints

Below are the main API endpoints for the application:

- **GET** `/api/resource/access`: Adds a request to access the resource with a given priority. If no priority is specified, it defaults to `1`.

### Request Parameters:
- `priority` (optional): Integer value representing the request's priority (default: 1).
  
- Example request:
    ```bash
    curl -X GET "http://localhost:8080/api/resource/access?priority=5"
    ```
### Response:

- **200 OK**: "Request added to queue with priority: X"

## Testing the application (Bash & Powershell)

- Use the following curl commands to test different scenarios **(bash)**:

  ### 1. Send Sequential Requests with the same Priority Levels

  ```bash
   # Send 10 sequential requests with the same priority level (priority 5)
   for i in {1..10}; do curl "http://localhost:8080/api/resource/access?priority=5" & done
  ```

  ### 2. Send Low and High-Priority Requests

  ```bash
  # Send 5 low-priority requests (priority 1)
  for i in {1..5}; do curl "http://localhost:8080/api/resource/access?priority=1" & done

  # Send 5 high-priority requests (priority 10)
  for i in {1..5}; do curl "http://localhost:8080/api/resource/access?priority=10" & done
  ```

 ### If you use Windows Powershell:

 ```powershell
# Send 10 sequential requests with the same priority level (priority 5)
1..10 | ForEach-Object { Start-Process -NoNewWindow -FilePath "curl" -ArgumentList "http://localhost:8080/api/resource/access?priority=5" }
```

 ```powershell
# Send 5 low-priority requests (priority 1)
1..5 | ForEach-Object { Start-Process -NoNewWindow -FilePath "curl" -ArgumentList "http://localhost:8080/api/resource/access?priority=1" }

# Send 5 high-priority requests (priority 10)
1..5 | ForEach-Object { Start-Process -NoNewWindow -FilePath "curl" -ArgumentList "http://localhost:8080/api/resource/access?priority=10" }
```
 
# Repository Structure

The following is the structure of the project repository:

```bash
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── jaggehn/
│   │           ├── controller/
│   │           │   └── ResourceController.java
│   │           ├── model/
│   │           │   └── AccessRequest.java
│   │           ├── runnable/
│   │           │   └── ResourceProcessorRunnable.java
│   │           └── service/
│   │               └── ResourceService.java
│   └── resources/
│       └── application.properties
├── test/
│   └── java/
│       └── com/
│           └── jaggehn/
│               └── ResourceManagementApplicationTests.java
└── pom.xml
```
## Explanation and Justifications

- **Thread Management**: The application uses a Thread manually to keep things simple. However, for production-level applications, it is recommended to use ExecutorService for better thread management and scalability.

- **Synchronized Methods**: The addRequest() method is marked as synchronized to ensure that multiple threads cannot add requests concurrently, avoiding potential race conditions.

- **BlockingQueue**: The PriorityBlockingQueue is used to manage the requests, allowing higher-priority requests to be processed first.


## Future Work

- **Refactor Thread Management**: Switch from manual thread management to ExecutorService to improve the scalability of the application.

- **Validation Improvements**: Enhance the validation logic to handle a wider range of invalid inputs more gracefully.
  
- **Logging**: Integrate proper logging instead of System.out.println to make debugging and monitoring easier.
