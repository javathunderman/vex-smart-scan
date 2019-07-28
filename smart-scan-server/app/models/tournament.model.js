const mongoose = require('mongoose');

const TournamentSchema = mongoose.Schema({
    sku: String,
    content: String
}, {
    timestamps: true
});

module.exports = mongoose.model('Tournament', TournamentSchema);
