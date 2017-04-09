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
### Navigate to the Go server directory (on Mac) and run the seeme executable:
```bash
  cd $GOPATH/
  seeme
```

### Download the following libraries:
```bash
  go get github.com/go-sql-driver/mysql
  go get golang.org/x/crypto/bcrypt
  go get github.com/urfave/negroni
```

After installing and setting up Android Studio
### Open Android Studio and run the Project on a preferred device

## Requirements
  * Go 1.2 or higher
  * MySQL (4.1+)
  * Android Studio
  * Java 

---------------------------------------

### Usage

The SeeMe app can be used by clicking the SeeMe button on the main tab window. The button then displays a list of users who are using your local Wifi network on the Discover Tab. To send a connection request simply click the plus account icon to the right of the desired users name.

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
