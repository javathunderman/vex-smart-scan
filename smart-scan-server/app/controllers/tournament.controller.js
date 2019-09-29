const Tournament = require('../models/tournament.model.js');
const Robot = require('../models/robot.model.js')
const Wheel = require('../models/wheel.model.js')
var spawn = require('child_process').spawn;
var moment = require('moment')
const { exec } = require('child_process');
var finalRerank;
var parsedout;
// Find a single tournament with an SKU
exports.findOne = (req, res) => {
    Tournament.findOne({sku: req.params.tournamentId})
    .then(tournament => {
        if(!tournament) {
            exec('java -jar app/controllers/smart-scan.jar ' + req.params.tournamentId, (error, stdout, stderr) => {
                  if (error) {
                    console.error(`exec error: ${error}`);
                    return;
                  }
                  // Create a Tournament
                  const tournament = new Tournament({
                      sku: req.params.tournamentId,
                      content: stdout
                  });

                  // Save Tournament in the database
                  tournament.save()
                  .then(data => {
                      //console.log(data);
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
        timeDiff/=60000;
        if (timeDiff > 20) {
          exec('java -jar app/controllers/smart-scan.jar ' + tournament.sku, (error, stdout, stderr) => {
                if (error) {
                  console.error(`exec error: ${error}`);
                  return;
                }
                tournament.content = stdout;
                tournament.save();
                console.log(tournament);
          });
        }
        res.send(tournament);
    }).catch(err => {
        if(err.kind === 'ObjectId') {
            return res.status(404).send({
                message: "Tournament not found with id " + req.params.tournamentId
            });
        }
        return res.status(500).send({
            message: "Error retrieving tournament with id " + req.params.tournamentId
        });
    });
};
exports.findOneAndRerank = (req, res) => {
    Tournament.findOne({sku: req.params.tournamentId})
    .then(tournament => {
        if(!tournament) {
            exec('java -jar app/controllers/smart-scan.jar ' + req.params.tournamentId, (error, stdout, stderr) => {
                  if (error) {
                    console.error(`exec error: ${error}`);
                    return;
                  }
                  // Create a Tournament
                  const tournament = new Tournament({
                      sku: req.params.tournamentId,
                      content: stdout
                  });

                  // Save Tournament in the database
                  tournament.save()
                  .then(data => {
                      //console.log(data);
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
        timeDiff/=60000;
        if (timeDiff > 20) {
          exec('java -jar app/controllers/smart-scan.jar ' + tournament.sku, (error, stdout, stderr) => {
                if (error) {
                  console.error(`exec error: ${error}`);
                  return;
                }
                tournament.content = stdout;
                tournament.save();
          });
        }
        var deserialized = (JSON.parse(tournament.content));
        //console.log(deserialized[0]['team']);
        Robot.find().then(robot => {
          for(var i = 0; i<robot.length; i++) {
          Wheel.findOne({type: robot[i].wheel}).then(wheel => {
          if(!wheel) {
          const wheel = new Wheel({
              type: robot[i].wheel,
              total: total+=deserialized[i]['score'],
              teams: 1,
              average: total
          });
          wheel.save();
        }
        else {
          wheel.teams++;
          wheel.total+=deserialized[i]['score'];
          wheel.save();
        }
        Wheel.find().sort("-average").then(wheel => {
          Robot.find({wheel: wheel[0].type}).then(finalRobot => {
            for(var k = 0; k<finalRobot.length; k++) {
              for(var j = 0; j<deserialized.length; j++) {
                if(deserialized[j]['team'] == finalRobot[i].team) {
                  deserialized[j]['score']*=1.1;
                  console.log(deserialized[j]['score']);
                }
              }
            }
          })
        })
        })
      }
    })
    res.send(tournament);
    }).catch(err => {
        if(err.kind === 'ObjectId') {
            return res.status(404).send({
                message: "Tournament not found with id " + req.params.tournamentId
            });
        }
        return res.status(500).send({
            message: "Error retrieving tournament with id " + req.params.tournamentId
        });
    });
};
