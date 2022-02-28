## Exercise Tracker

### Libraries:
- [Ktor](https://github.com/ktorio/ktor) - Kotlin web framework
- [Netty](https://github.com/netty/netty) - Web server
- [Exposed](https://github.com/JetBrains/Exposed) - Kotlin ORM
- [H2](https://github.com/h2database/h2database) - Embeddable database
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - JDBC connection pooling
- [KGraphQL](https://github.com/apurebase/kgraphql) - GraphQL & Ktor server
- [Koin](https://github.com/InsertKoinIO/koin) - Dependency injection framework

### General Flow:
When creating a user we can send a Notifier object, including the list of week days and a fixed time, to schedule getting notifications on those days and fixed time. e.g. ([MON, THU], 18). 
There is a job to check each hour in a day for the users who scheduled on that specific week day and time, and send them notifications if they were not active on that day so far.
Notifications are in the form of a log message and there is a table to keep track of all sent notifications.
Assuming it's a video-based training application, users can pick an exercise and start watching it, the user event will be updated based on the exercise progress and when the exercise progress reaches the end, that user event will be marked as completed(update will be call when finishing, exiting, etc an event).



### Schemas:
Kgraphql provides a playground to test the app, here: http://localhost:8080/graphql

All Schemas are defined inside `src/main/kotlin/com/octopus/web/Schemas.kt`

eg:

### Create a user:
Query

    mutation CreateUser($userInput:UserInput!) {
            createUser(userInput: $userInput) {
                id
                firstName
                lastName
                email
            }
    }

Query Variables

    {
        "userInput": {
            "email": "reza@octopus.com",
            "firstName": "reza",
            "lastName": "mortazavi",
            "notifiers":{
                "weekDay":[1, 3],
                "hour": 23
            }
        }
    }

Returns

    {
        "data": {
            "createUser": {
                "id": "76e22f90-8071-4938-869d-1840a01d2aa6",
                "firstName": "reza",
                "lastName": "mortazavi",
                "email": "reza@email.com"
            }
        }
    }

Weekday Mapping:

    {
        SUN -> 1
        MON -> 2
        TUE -> 3
        WED -> 4
        THU -> 5
        FRI -> 6
        SAT -> 7
    }

For simplicity Hour is just an integer in the range of [0, 23]

### List all users:
Query

    query {
        users {
            id
            firstName
            lastName
            email
        }
    }

Returns

    {
        "data": {
            "users": [
            {
                "id": "4bf269b8-f8c2-4abd-adc2-dfdbbd11b20f",
                "firstName": "reza",
                "lastName": "mortazavi",
                "email": "reza@email.com"
            },
            {
                "id": "76e22f90-8071-4938-869d-1840a01d2aa6",
                "firstName": "reza2",
                "lastName": "mortazavi",
                "email": "reza2@email.com"
            }
            ]
        }
    }

### Create an Exercise:
Query

    mutation CreateExercise($exerciseInput:ExerciseInput!) {
            createExercise(exerciseInput: $exerciseInput) {
                id
                title
                type
                difficulty
                duration
            }
    } 

Query Variables

    {
        "exerciseInput": {
            "title": "Push up", 
            "description": "Push-ups are a basic exercise used in civilian athletic training or physical education and commonly in military physical training",
            "type": "STRETCH", 
            "difficulty": "EASY",
            "duration": 200
        }
    }

Returns

    {
        "data": {
            "createExercise": {
                "id": 1,
                "title": "Push up",
                "type": "STRETCH",
                "difficulty": "EASY",
                "duration": 200
            }
        }
    }

### Generate User Event:
Query

    mutation GenerateEvent($eventInput:UserEventInput!) {
        generateEvent(eventInput: $eventInput) {
            id
            userId
            exerciseId
            createTime
            updateTime
            progress
            isCompleted
        }
    }

Query Variables

    {
        "eventInput": {
            "userId": "76e89c10-0c43-4b65-afdd-0f89fe2b8fb1",
            "exerciseId": 1
        }
    }

Returns

    {
        "data": {
            "generateEvent": {
                "id": "5ec1b8c9-2787-4b7a-af77-47191589e27d",
                "userId": "76e22f90-8071-4938-869d-1840a01d2aa6",
                "exerciseId": 1,
                "createTime": "2022-02-28 12:29:08",
                "updateTime": "2022-02-28 12:29:08",
                "progress": 0,
                "isCompleted": false
            }
        }
    }

### Generate User Event:
Query

    mutation UpdateEvent($eventInput:UpdateUserEventInput!) {
        updateEvent(eventId: "8369d71e-ef81-4674-8323-7758cdebc4aa", eventInput: $eventInput) {
            id
            userId
            exerciseId
            createTime
            updateTime
            progress
            isCompleted
        }
    }

Query Variables

    {
        "eventInput": {
            "progress": 100
        }
    }

Returns

    {
        "data": {
            "generateEvent": {
                "id": "5ec1b8c9-2787-4b7a-af77-47191589e27d",
                "userId": "76e22f90-8071-4938-869d-1840a01d2aa6",
                "exerciseId": 1,
                "createTime": "2022-02-28 12:29:08",
                "updateTime": "2022-02-28 12:30:08",
                "progress": 100,
                "isCompleted": false
            }
        }
    }

