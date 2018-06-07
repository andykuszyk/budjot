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
    id: 'string',
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
    console.log('Auth header: ' + req.get('Authorization'))
    var user = new User({ id: req.get('Authorization'), created: date.now, lastLogin: date.now })
    user.save()
    res.status(201).send({ "spam": "eggs" })
})

app.get('/users', (req, res) => {
    // gets the status of the user for the auth header
    console.log(req)
})

app.use(express.static('dist/budjot'))
app.use('/login', express.static('dist/budjot'))
app.use('/edit', express.static('dist/budjot'))
app.use('/list', express.static('dist/budjot'))

app.listen(port, () => console.log('Budjot running on port ' + port))
