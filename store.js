var request = require('request');

'use strict';

var fs = require('fs');
var dbFile = 'db.json';
var url = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

// Everything is stored here

var db = {
    users: {},
    data: []
};

// Read db file on startup and save on exit

var readDatabase = function () {
    request({
        url: url,
        json: true
    }, function (error, response, body) {
        if (!error && response.statusCode === 200) {
            console.log(body);
            db.data = body;
            saveDatabase();
        }
    });
};

var saveDatabase = function () {
    console.log('Saving db');
    var stringContent = JSON.stringify(db);
    fs.writeFileSync(dbFile, stringContent);
};

readDatabase();
process.on('SIGINT', function () { console.log('SIGINT'); process.exit(); });
process.on('SIGTERM', function () { console.log('SIGTERM'); process.exit(); });
process.on('exit', saveDatabase);

// Accessors
exports.getToken = function (userId) {
    return db.users[userId];
};

exports.saveToken = function (userId, token) {
    db.users[userId] = token;
};

exports.removeUser = function (user) {
    var index = db.data.indexOf(user);
    if (index !== -1) {
        db.data.splice(index, 1);
    }
};

exports.getUser = function (userId) {
    // check all id for user and return
};

exports.connectUsers = function (firstName, lastName, job) {
    console.log('Post: ', firstName + " " + lastName
        + "\nRole: " + job);
};

exports.allUsers = function () {
    return db.data;
};
