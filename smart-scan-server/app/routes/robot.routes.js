module.exports = (app) => {
    const robots = require('../controllers/robot.controller.js');

    // Create a new robot
    app.post('/robots', robots.create);

    // Retrieve a single robot with team
    app.get('/robots/:robotId', robots.findOne);

    // Update a robot with robotId
    app.put('/robots/:robotId', robots.update);

    // Delete a robot with robotId
    app.delete('/robots/:robotId', robots.delete);
}
