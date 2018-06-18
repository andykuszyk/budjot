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
    mongo.budjot().find({ userId: id }, 'name _id')
    .then(function(budjots) {
        var returnBudjots = [];
        for(var budjot of budjots) {
            returnBudjots.push(mongo.toBudjotJson(budjot));
        }
        res.status(200).send(returnBudjots);
    })
    .catch(function(error) {
        res.status(500).send(err);
    });
})

app.get('/jots/:id', async (req, res) => {
    // get the jot specified by the given id, provided it is for the user
    // in the auth header
    console.log(req.params.id)
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.budjot().findById(req.params.id).exec()
    .then(function(budjot) {
        return res.status(200).send(mongo.toBudjotJson(budjot));
    })
    .catch(function(error) {
        if(error) {
            return res.status(500).send(error);
        }
    });
})

app.post('/jots', async (req, res) => {
    // create a jot for the user in the auth header
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.budjot().find({ name: req.body.name }).exec()
    .then(function(budjots) {
        if(budjots.length > 0) {
            res.status(405).send();
            return new Promise(function(resolve, reject) { return null; })
        }
        return mongo.fromBudjotJson(req.body, id).save();
    })
    .then(function(budjot) {
        if(budjot) {
            res.status(201).send(mongo.toBudjotJson(budjot));
        }
    })
    .catch(function(error) {
        if(error){
            res.status(500).send(error);
        }
    });
})

app.put('/jots/:id', async (req, res) => {
    // update the jot with the given id, provided it exists for the user in the auth header 
    var id = await google.verify(req.get('Authorization'));
    if(id == null) {
        return res.status(401).send();
    }
    mongo.budjot().findById(req.params.id)
    .then(function(budjot) {
        var reqBudjot = mongo.fromBudjotJson(req.body, id)
        budjot.name = reqBudjot.name;
        budjot.income = reqBudjot.income;
        budjot.userId = reqBudjot.userId;
        budjot.entries = reqBudjot.entries;
        return budjot.save();
    })
    .then(function(budjot) {
        return res.status(204).send();
    })
    .catch(function(error) {
        return res.status(500).send(error);
    });
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
