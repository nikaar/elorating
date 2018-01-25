# Elo Rating REST-API

## Manual

This API has a few endpoints, you can list all players, add a new player and update the statistics of players.

### Adding a player

```.../addplayer```

This endpoint requires one ```username``` parameter. It will then insert a new row into a database. You cant add two users with the same username.

EXAMPLE:

```curl -d "username=Niko" -X POST .../addplayer```

### Updating the players statistics

```.../game```

This endpoint requires two parameters, usernames of the winner and the loser. This method will then collect the necessary info of the sent users and calculate the correct amount of points to be subtracted form the loser and given to the winner

EXAMPLE:

```curl -d "winner=janne&loser=niko" -X POST .../game``` 


## Technologies

* Java 8
* Spring Boot
* JPA

## Server

* Ubuntu Server
* Apache Tomcat 8
