const mongoose = require('mongoose');

const WheelSchema = mongoose.Schema({
    type: String,
    total: Number,
    teams: Number,
    average: Number
});

module.exports = mongoose.model('Wheel', WheelSchema);
