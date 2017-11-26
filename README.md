
# Customer RESTful Service

This is a sample web service which create, read, update, delete customers
profile and addresses.

# Instructions

## Build

```
gradle clean build
```

## Run

To run using default port 8080, use command
```
gradle bootRun
```

To run using custom port, use command

```
gradle -Dserver.port=9990 bootRun
```

Service endpoint URL is http://localhost:{port}/api/customers

# Endpoint Definitions

The root endpoint of this service is *http://{host}:{port}/api/customers*.
The root endpoint accepts all four CRUD operations on both customer and address details

## Customer Web Service Endpoint

| HTTP Method     | URI Format                                      | Description |
|-----------------|-------------------------------------------------|-------------|
| POST            | /api/customers/                                 | creates customer resource |
| PUT             | /api/customers/{id}                             | updates customer resource |
| GET             | /api/customers/{id}                             | get customer profile/addresses |
| GET             | /api/customers?offset=0&limit=10&name=keyword   | find customer profile, by default returns total of top 10 customer profiles. |
| DELETE          | /api/customers/{id}                             | deletes customer resource |

## Address Web Service Endpoint

| POST| /api/customers/{id}/addresses | create address resource |
|-----|-------------------------------|-------------------------|
| PUT | /api/customers/{id}/addresses/{addressId} | updates address resource |
| GET | /api/customers/{id}/addresses/{addressId} | get customer profile addresses |
| DELETE | /api/customers/{id}/addresses/{addressId} | deletes customer resource |


To search, pass query parameters below
* name - name keyword (searched against first name, surname, middle name)
* limit - number of record to return (default 10)
* offset - offset in record matches (default 0)


# Database

This service uses an in-memory H2 database. All data is lost after a restart. 
If you want to add your own test data, please add them in the schema definition file found under
```
    <project directory>/src/main/resources/create-db.sql
```

This script is executed by this service every time it starts up.

There are two sample customer records.
* Record with id=1 is a customer with 2 addresses.
* Record with id=2 only has a customer profile and no address.
