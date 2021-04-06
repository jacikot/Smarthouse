# Smarthouse
Information systems - university project

Information system of a smarthouse implemented in JAVA (JMS, JPA, REST, MYSQL database)

Includes 5 components:
1. Sound player (using Youtube API)
2. Alarm
3. Planner (using Google Maps API)
4. Service
5. User Device (REST client - Httpcomponents Client API)

User Device communicates with Service via REST API. Service communticates with other components via JMS and shared database (JPA).
