const express = require('express'),
      mongoose = require('mongoose'),
      dbConfig = require('./config/database.config.js');

// create express app
const app = express();
mongoose.Promise = global.Promise;

// parse application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));
// parse application/json
app.use(express.json());

// Connect to the database
mongoose.connect(dbConfig.url, {useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => {
        console.log('Successfully connected to the database');
    }).catch(err => {
        // Note with unified topology this may take around ~30s
        console.log('Could not connect to the database. Exiting now...', err);
        process.exit();
    });

// define a simple route
app.get('/', (req, res) => {
    res.json({"message": "Welcome to the Smart Scan API. Please refer to the GitHub documentation. "});
});

require('./app/routes/tournament.routes.js')(app);
require('./app/routes/robot.routes.js')(app);

// listen for requests
app.listen(3000, () => {
    console.log('Server is listening on port 3000');
});
