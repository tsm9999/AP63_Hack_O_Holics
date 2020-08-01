

//const accountSid = 'AC136518d11af3c74097bc1be8adb3b74b';
//const authToken = '011aac8b6391063de88b90d7cdca6281';
// Download the helper library from https://www.twilio.com/docs/node/install
// Your Account Sid and Auth Token from twilio.com/console
// DANGER! This is insecure. See http://twil.io/secure
const accountSid = 'AC136518d11af3c74097bc1be8adb3b74b';
const authToken = '011aac8b6391063de88b90d7cdca6281';
const client = require('twilio')(accountSid, authToken);

client.messages
      .create({
        mediaUrl: ['https://images.unsplash.com/photo-1545093149-618ce3bcf49d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=668&q=80'],
         from: 'whatsapp:+14155238886',
         body: 'Hello there! Himani here',
         to: 'whatsapp:+919011138622',

      })
         .then(message => {
          console.log(message.sid);
        })
        .catch(err => {
          console.error(err);
        });
