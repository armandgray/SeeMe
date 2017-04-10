# SeeMe

The SeeMe app provides Wifi based local area, social networking connections. SeeMe is a native android app with a Go server running on AWS with Nginx. Users can link to anyone connected with the same Wifi address with the click of a button. 

SeeMe fills the gap between limited Facebook and LinkedIn search results to streamline user connection in conference, meetup and networking environments. SeeMe offers an easy 1-click solution to meeting people on the fly.

---------------------------------------
This app is available for download on the [Google Play Store](https://play.google.com/store/search?q=SeeMe&hl=en)

---------------------------------------

## Getting Started

### Clone the repo

In your terminal, navigate to a your GOPATH directory:
```bash
  git clone https://github.com/armandgray/SeeMe.git
```

### Setup mysql with the following tables:

```bash
CREATE TABLE Books (
	pk int NOT NULL AUTO_INCREMENT,  
	title varchar(50) NOT NULL default '', 
	author varchar(50) NOT NULL default '', 
	id varchar(20) NOT NULL default '',
	classification varchar(50) default '', 
	PRIMARY KEY (pk));
```

```bash
CREATE TABLE feebacks (
	username varchar(50) NOT NULL default '', 
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	message TEXT NOT NULL,
	PRIMARY KEY (username, timestamp));
```

```bash
CREATE TABLE connections (
	username varchar(50) NOT NULL default '', 
	connection varchar(50) NOT NULL default '', 
	status varchar(50) NOT NULL default '', 
	PRIMARY KEY (username, connection));
```

```bash
CREATE TABLE notes (
	username varchar(50) NOT NULL default '', 
	note TEXT NOT NULL, 
	PRIMARY KEY (username, note(255)));
```

After installing Go and setting up your GOPATH
### Navigate to the Go server directory (on Mac):
```bash
  cd $GOPATH/src/seeme
```

### Download the following libraries and run the server:
```bash
  go get github.com/go-sql-driver/mysql
  go get golang.org/x/crypto/bcrypt
  go get github.com/urfave/negroni
  go run main.go
```

After installing and setting up Android Studio
### Open Android Studio and run the Project on a preferred device

---------------------------------------

## Requirements
  * Go 1.2 or higher
  * MySQL (4.1+)
  * Android Studio
  * Java 

---------------------------------------

## Usage

The SeeMe app can be used by clicking the SeeMe button on the main tab window. The button then displays a list of users who are using your local Wifi network on the Discover Tab. To send a connection request simply click the plus account icon to the right of the desired users name.

![Main Screenshot](https://github.com/armandgray/SeeMe/blob/master/screenshots/main_screenshot.png)

Click the SeeMe button and see a list of "Available Users" on your local network.

![Discover Screenshot](https://github.com/armandgray/SeeMe/blob/master/screenshots/discover_screenshot.png)

Click the add account icon to send connection requests to users and add them to your local network

![Network Screenshot](https://github.com/armandgray/SeeMe/blob/master/screenshots/network_screenshot.png)

---------------------------------------

## Description

The SeeMe app was developed as a team project for the DeveloperWeek 2016 Hackathon. This repo contains contents from the original Node.js and Ruby on Rails project developed for the Flock.os challenge. SeeMe is a social networking app designed to streamline user connections based on location through network connection. SeeMe fills the gap in other social networking app by connecting any users in your area, where apps like Facebook and LinkedIn limit connection search results. The SeeMe app provides an espcially useful 1-click connection for users at Meetups, Conferences, Talks, Meetings and Networking Events.

## Built With

* [Android Studio](https://developer.android.com/studio/index.html) - The Integrated Development Environment for native Android Apps
* [Go](https://golang.org/) - Server-Side Management
* [MySQL](https://dev.mysql.com/downloads/mysql/) - Used as backend database
* [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html) - Used for http requests
* [Negroni](https://github.com/urfave/negroni) - Http middleware for Golang

## Contributors

* [Christian Salas](https://github.com/SalasC2)
* [Yaritza Perez](https://github.com/yaritzape9)

## Creator

* [Armand Gray](https://armandgray.com)

## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/armandgray/SeeMe/blob/master/LICENSE) file for details

## Acknowledgments

* The idea for this app was developed by [Yaritza Perez](https://github.com/yaritzape9) as the creative design lead on our team for the DeveloperWeek 2016 Hackathon.
