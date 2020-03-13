const express = require('express'),
      robots = require('./controllers/robot.controller'),
      tournaments = require('./controllers/tournament.controller');

const router = express.Router();

// Create a new robot
router.post('/robots', robots.create);
// Retrieve a single robot with team
router.get('/robots/:robotId', robots.findOne);
// Update a robot with robotId
router.put('/robots/:robotId', robots.update);
// Delete a robot with robotId
router.delete('/robots/:robotId', robots.delete);

// Retrieve a single Tournament with tournamentId
router.get('/tournaments/:tournamentId', tournaments.findOne);
//router.get('/tournaments/rerank/:tournamentId', tournaments.findOneAndRerank);

module.exports = router;
