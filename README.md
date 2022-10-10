# trafficcounter

An application for visualizing OpenTrafficData.

## Run instructions

The trafficcounter application requires Java 17.
A bundled Maven version is available.

The required Node version is downloaded automatically.

### Environment variables
In order to connecto to the OpenTransportData API, 
the API key has to be set as the following environment variable:
`TRAFFICCOUNTER_API_KEY`.

### Development execution
In order to run the application in development mode, the following commands can be used:

Backend:
```
cd trafficcounter-backend
../mvnw spring-boot:run
```

Frontend:
```
cd trafficcounter-frontend
../mvnw install
```


## Contributors
The following people are working on this project.
- Manuel Riesen
- Sandro Rüfenacht
- Sven Trachsel