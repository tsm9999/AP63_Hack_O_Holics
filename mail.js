var nodemailer = require('nodemailer');

var transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'tsmadd89',
    pass: '' //insert password here
  }
});

var mailOptions = {
  from: 'tsmadd89@gmail.com',
  to: '',
  subject: 'Sending Email using Node.js',
  text: 'This mail is sent by Nodejs server',
  attachments: [{
    filename: 'file.pdf',
    path: '',//path of pdf
    contentType: 'application/pdf'
  }]
};

transporter.sendMail(mailOptions, function(error, info){
  if (error) {
    console.log(error);
  } else {
    console.log('Email sent: ' + info.response);
  }
});