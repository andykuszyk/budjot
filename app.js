const express = require('express')
const app = express()

if(process.argv.length < 3) {
    port = 8080
    console.log('No port provided, so using default port of 8080')
} else {
    port = process.argv[2]
    console.log('Port provided, so serving on port ' + port)
}

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
    console.log(req)
    res.send({ "spam": "eggs" })
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
