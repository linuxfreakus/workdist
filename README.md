# Work Distribution Exercise
Work distribution exercise for assigning hypothetical work items to agents.

### Prerequisites 
 - Docker
 - Java 11 or higher 
## Setup Instructions
Build: `./gradlew clean jibDockerBuild`   
Run: `docker-compose up`  
Reset: `docker-compose down -v && docker-compose up`  
#### Notes:
Normally full unit test coverage would be important, however for this sample exercise there was not time to add that in. 
## Service Details
#### Create a new work item
```
curl --header "Content-Type: application/json" --request POST \
      --data '{"title":"task one", "priorityName":"HIGH", "requiredSkillNames":["Skill 1", "Skill 2"]}' \
      http://localhost:8080/work-items
```
#### Update a work item to completed
```
curl --header "Content-Type: application/json" --request PUT \
     http://localhost:8080/work-items/set-completed/1
```
#### See a list of all agents and their in progress work items
```
curl --header "Content-Type: application/json" --request GET \
     http://localhost:8080/agents
```
#### See a list of all in progress work items
```
curl --header "Content-Type: application/json" --request GET \
     http://localhost:8080/work-items
```