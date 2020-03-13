const Tournament = require('../models/tournament.model');
const Robot = require('../models/robot.model')
var spawn = require('child_process').spawn;
var moment = require('moment')
const {
  exec
} = require('child_process');
// Find a single tournament with an SKU
exports.findOne = (req, res) => {
  var skuMatch = new RegExp("^RE-VRC-(17|18|19|)-(\\d\\d\\d\\d)");
  if (skuMatch.test(req.params.tournamentId) || req.params.tournamentId == "rundebug") { //anti RCE filtering
    Tournament.findOne({ //look up tournament in the DB
        sku: req.params.tournamentId
      })
      .then(tournament => { //based on result
        if (!tournament) { //if tournament does not exist
          exec('java -jar app/controllers/smart-scan.jar ' + req.params.tournamentId, (error, stdout, stderr) => { //pass the SKU to the jar
            if (error) {
              console.error(`exec error: ${error}`); //catch and return errors
              return;
            }
            // Create a Tournament
            const tournament = new Tournament({
              sku: req.params.tournamentId,
              content: stdout //using jar output here
            });

            // Save Tournament in the database
            tournament.save()
              .then(data => {
              }).catch(err => {
                console.log("Error while accessing newly saved data")
              });
          });
          return res.status(404).send({
            message: "Tournament not found with id " + req.params.tournamentId
          });
        }
        var endDate = new Date();
        var timeDiff = endDate.getTime() - tournament.updatedAt.getTime();
        timeDiff /= 60000;
        if (timeDiff > 20) { //if older than 20 min
          exec('java -jar app/controllers/smart-scan.jar ' + tournament.sku, (error, stdout, stderr) => { //run again
            if (error) {
              console.error(`exec error: ${error}`);
              return;
            }
            tournament.content = stdout;
            tournament.save();
          });
        }
        res.send(tournament);
      }).catch(err => {
        if (err.kind === 'ObjectId') {
          return res.status(404).send({
            message: "Tournament not found with id " + req.params.tournamentId
          });
        }
        return res.status(500).send({
          message: "Error retrieving tournament with id " + req.params.tournamentId
        });
      });
  } else { //if bad request
    res.send("https://www.youtube.com/watch?v=dQw4w9WgXcQ"); //return rick astley
  }
};
