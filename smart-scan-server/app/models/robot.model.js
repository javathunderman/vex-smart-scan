const mongoose = require('mongoose');

const RobotSchema = mongoose.Schema({
    team: String,
    wheel: Number,
    rpm: Number,
    auton: Number,
    content: String
}, {
    timestamps: true
});

module.exports = mongoose.model('Robot', RobotSchema);
