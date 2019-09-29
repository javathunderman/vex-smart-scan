module.exports = (app) => {
    const tournaments = require('../controllers/tournament.controller.js');

    // Retrieve a single Note with noteId
    app.get('/tournaments/:tournamentId', tournaments.findOne);
    app.get('/tournaments/rerank/:tournamentId', tournaments.findOneAndRerank);
}
