const {OAuth2Client} = require('google-auth-library');
const client = new OAuth2Client('763708471782-uc93cjsj2c03rm4j3jt8v91tbqu3namq.apps.googleusercontent.com');

module.exports = {
    verify: async function(token) {
        try{
            const ticket = await client.verifyIdToken({
                idToken: token,
                audience: '763708471782-uc93cjsj2c03rm4j3jt8v91tbqu3namq.apps.googleusercontent.com'
            });
            const payload = ticket.getPayload();
            const userId = payload['sub'];
            return userId;
        } catch(err) {
            console.log('An error occured trying to authenticate the user with google: ' + err);
            return null;
        }
    }
};
