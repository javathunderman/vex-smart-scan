const Robot = require('../models/robot.model.js');

// Create and Save a new Robot
exports.create = (req, res) => {
    // Validate request
    if(!req.body.content) {
        return res.status(400).send({
            message: "Robot content can not be empty"
        });
    }

    // Create a Robot
    const robot = new Robot({
        team: req.body.team,
        wheel: req.body.wheel || 0.0,
        rpm: req.body.rpm || 0,
        auton: req.body.auton || 0,
        content: req.body.content
    });

    // Save Robot in the database
    robot.save()
    .then(data => {
        res.send(data);
    }).catch(err => {
        res.status(500).send({
            message: err.message || "Some error occurred while creating the Robot."
        });
    });
};
// Find a single robot with a robotId
exports.findOne = (req, res) => {
    Robot.findOne({team: req.params.robotId})
    .then(robot => {
        if(!robot) {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        res.send(robot);
    }).catch(err => {
        if(err.kind === 'ObjectId') {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        return res.status(500).send({
            message: "Error retrieving robot with id " + req.params.robotId
        });
    });
};

// Update a robot identified by the robotId in the request
exports.update = (req, res) => {
    // Validate Request
    if(!req.body.content) {
        return res.status(400).send({
            message: "Robot content can not be empty"
        });
    }

    // Find robot and update it with the request body
    Robot.findOneAndUpdate({team: req.params.team}, {
      team: req.body.team || robot.team,
      wheel: req.body.wheel || robot.wheel,
      rpm: req.body.rpm || robot.rpm,
      auton: req.body.auton || robot.auton,
      content: req.body.content
    }, {new: true})
    .then(robot => {
        if(!robot) {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        res.send(robot);
    }).catch(err => {
        if(err.kind === 'ObjectId') {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        return res.status(500).send({
            message: "Error updating robot with id " + req.params.robotId
        });
    });
};

// Delete a robot with the specified robotId in the request
exports.delete = (req, res) => {
    Robot.findByIdAndRemove(req.params.robotId)
    .then(robot => {
        if(!robot) {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        res.send({message: "Robot deleted successfully!"});
    }).catch(err => {
        if(err.kind === 'ObjectId' || err.name === 'NotFound') {
            return res.status(404).send({
                message: "Robot not found with id " + req.params.robotId
            });
        }
        return res.status(500).send({
            message: "Could not delete robot with id " + req.params.robotId
        });
    });
};
