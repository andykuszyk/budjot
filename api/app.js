const express = require('express');
const app = express();
const config = require('./config');
const google = require('./google');
const mongo = require('./mongo');

app.get('/jots', (req, res) => {
    // get the list of jots for the user in the auth header
})

app.get('/jots/{id}', (req, res) => {
    // get the jot specified by the given id, provided it is for the user
    // in the auth header
    console.log(req)
})

app.post('/jots', (req, res) => {
    // create a jot for the user in the auth header
    console.log(req)
})

app.put('/jots/{id}', (req, res) => {
    // update the jot with the given id, provided it exists for the user in the auth header
    console.log(req)
})

app.post('/users', async (req, res) => {
    // creates a new user if one doesn't already exist for the auth header
    var idToken = req.get('Authorization');
    var id = await google.verify(idToken);
    if(id == null) {
        res.status(401).send();
    } else {
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
    }
})

app.use(express.static('dist/budjot'));
app.use('/login', express.static('dist/budjot'));
app.use('/edit', express.static('dist/budjot'));
app.use('/list', express.static('dist/budjot'));
app.listen(config.port(), () => console.log('Budjot running on port ' + config.port()));
