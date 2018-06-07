const express = require('express')
const app = express()
const mongoose = require('mongoose')

if(process.argv.length < 5) {
    console.log('Arguments required, usage is: node app.js [port] [mongo-user] [mongo-password]')
    process.exit(-1)
}

var port = process.argv[2]
var mongoUser = process.argv[3]
var mongoPassword = process.argv[4]
mongoose.connect(`mongodb://${mongoUser}:${mongoPassword}@ds247410.mlab.com:47410/budjot`)
var userSchema = new mongoose.Schema({
    id: { type: 'string', index: true },
    created: 'date',
    lastLogin: 'date'
})
var User = mongoose.model('user', userSchema)

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

app.post('/users', (req, res) => {
    // creates a new user if one doesn't already exist for the auth header
    var idToken = req.get('Authorization');
    var user = User.findOne({ id: idToken });
    if(user == null) {
        console.log('No user found for idToken, so creating a new one');
        var user = new User({ id: idToken, created: Date.now(), lastLogin: Date.now() });
        user.save();
        res.status(201).send();
    } else {
        user.lastLogin = Date.now();
        user.save();
        res.status(204).send()
    }
})

app.use(express.static('dist/budjot'))
app.use('/login', express.static('dist/budjot'))
app.use('/edit', express.static('dist/budjot'))
app.use('/list', express.static('dist/budjot'))
app.listen(port, () => console.log('Budjot running on port ' + port))
