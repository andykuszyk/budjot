const mongoose = require('mongoose');
const config = require('./config');

var mongoUser = config.mongoUser();
var mongoPassword = config.mongoPassword();
mongoose.connect(`mongodb://${mongoUser}:${mongoPassword}@ds247410.mlab.com:47410/budjot`);
var userSchema = new mongoose.Schema({
    id: { type: 'string', index: true },
    created: 'date',
    lastLogin: 'date'
});

var budjotEntrySchema = new mongoose.Schema({
    name: 'string',
    amount: 'number',
    paid: 'boolean'
});

var budjotSchema = new mongoose.Schema({
    name: { type: 'string', index: true },
    userId: { type: 'string', index: true },
    income: 'number',
    entries: [budjotEntrySchema]
});

module.exports = {
    user: function() {
       return mongoose.model('user', userSchema);
    },
    budjot: function() {
        return mongoose.model('budjot', budjotSchema);
    },
    toBudjotJson: function(budjot) {
        var budjotEntries = [];
        for(var entry of budjot.entries) {
            budjotEntries.push({
                "name": entry.name,
                "amount": entry.amount,
                "paid": entry.paid
            });
        }
        return {
            "id": budjot._id,
            "name": budjot.name,
            "income": budjot.income,
            "entries": budjotEntries
        };
    },
    fromBudjotJson: function(budjot) {
        // todo
    }
};

