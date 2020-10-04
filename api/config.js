module.exports = {
    validate: function () {
        if(process.argv.length < 5) {
            console.log('Arguments required, usage is: node app.js [port] [mongo-url]');
            process.exit(-1);
        }
    },
    port: function(){
       return process.argv[2];
    },
    mongoUrl: function() {
       return process.argv[3];
    }
};

