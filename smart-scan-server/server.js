const express = require('express'),
      mongoose = require('mongoose'),
      dbConfig = require('./config/database.config'),
      router = require('./app/router');

// create express app
const app = express();
mongoose.Promise = global.Promise;

// parse application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));
// parse application/json
app.use(express.json());

// tournament and robot routes
app.use('/', router);
// define a simple route
app.get('/', (req, res) => {
    res.json({"message": "Welcome to the Smart Scan API. Please refer to the GitHub documentation. "});
});

// Connect to the database
mongoose.connect(dbConfig.url, {useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => {
        console.log('Successfully connected to the database');
    }).catch(err => {
        // Note with unified topology this may take around ~30s
        console.log('Could not connect to the database. Exiting now...', err);
        process.exit();
    });

// listen for requests
app.listen(3000, () => {
    console.log('Server is listening on port 3000');
});
