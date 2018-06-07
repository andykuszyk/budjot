module.exports = {
    validate: function () {
        if(process.argv.length < 5) {
            console.log('Arguments required, usage is: node app.js [port] [mongo-user] [mongo-password]');
            process.exit(-1);
        }
    },
    port: function(){
       return process.argv[2];
    },
    mongoUser: function() {
       return process.argv[3];
    },
    mongoPassword: function() {
        return process.argv[4];
    }
};

