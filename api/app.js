const express = require('express');
const app = express();
const config = require('./config');
const google = require('./google');
const mongo = require('./mongo');
const bodyParser = require('body-parser');
app.use(bodyParser.json());

app.get('/jots', async (req, res) => {
    // get the list of jots for the user in the auth header
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.budjot().find({ userId: id }, function(err, budjots) {
        if(err) {
            return res.status(500).send(err);
        }
        var returnBudjots = [];
        for(var budjot of budjots) {
            returnBudjots.push(mongo.toBudjotJson(budjot));
        }
        res.status(200).send(returnBudjots);
    });
})

app.get('/jots/{id}', async (req, res) => {
    // get the jot specified by the given id, provided it is for the user
    // in the auth header
    console.log(req)
    var id = await google.verify(req.get('Authorization'));
})

app.post('/jots', async (req, res) => {
    // create a jot for the user in the auth header
    console.log(req)
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.budjot().find({ name: req.body.name }).exec()
    .then(function(budjots) {
        if(budjots.length > 0) {
            return res.status(405).send();
        }
        return mongo.fromBudjotJson(req.body).save();
    })
    .then(function() {
        res.status(201).send(mongo.toBudjotJson(budjot));
    })
    .catch(function(error) {
        if(error){
            res.status(500).send(error);
        }
    });
})

app.put('/jots/{id}', async (req, res) => {
    // update the jot with the given id, provided it exists for the user in the auth header
    console.log(req)
    var id = await google.verify(req.get('Authorization'));
})

app.post('/users', async (req, res) => {
    // creates a new user if one doesn't already exist for the auth header
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.user().findOne({ id: id }, function (err, user) {
        if(user == null) {
            console.log('No user found for idToken, so creating a new one');
            var user = new mongo.user()({ id: id, created: Date.now(), lastLogin: Date.now() });
            user.save(function (err, u) {
                if(err) {
                    res.status(500).send(err);
                } else {
                    res.status(201).send();
                }
            });
        } else {
            user.lastLogin = Date.now();
            user.save(function (err, u) {
                if(err) {
                    res.status(500).send(err);
                } else {
                    res.status(204).send();
                }
            });
        }
    });
})

app.use(express.static('dist/budjot'));
app.use('/login', express.static('dist/budjot'));
app.use('/edit', express.static('dist/budjot'));
app.use('/list', express.static('dist/budjot'));
app.listen(config.port(), () => console.log('Budjot running on port ' + config.port()));
